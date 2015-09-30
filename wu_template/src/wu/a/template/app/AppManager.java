package wu.a.template.app;

import java.util.List;

import wu.a.template.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AppManager {
	GridView app_gv;
	View app_gv_back;
	Context context;
	AppAdapter<AppInfo> adapter;
	PackageInfoProvider pip;

	public AppManager(Context context) {
		this.context = context;
		pip = new PackageInfoProvider(context);
	}

	public void setGridView(View back, GridView gv) {
		this.app_gv_back = back;
		// back.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// hide();
		// }
		// });
		this.app_gv = gv;
		adapter = new AppAdapter();
		app_gv.setAdapter(adapter);
	}

	public void startLoader() {
		new Loader().start();
	}

	class Loader extends Thread {
		@Override
		public void run() {
			PackageInfoProvider pp=new PackageInfoProvider(context);
			adapter.setDatas(pp.getToAppInfo(pp.getAudioAppInfo()));
			mHandler.sendEmptyMessage(0);
		}
	}

	public boolean isShow() {
		return app_gv_back.getVisibility() == View.VISIBLE;
	}

	public void show() {
		if (isShow()) {
			return;
		}
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setInterpolator(new DecelerateInterpolator());
		alphaAnimation.setDuration(300);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setInterpolator(new DecelerateInterpolator());
		scaleAnimation.setDuration(300);
		// AnimationSet animationSet=new AnimationSet(true);
		// animationSet.addAnimation(alphaAnimation);
		// animationSet.addAnimation(scaleAnimation);
		// Bitmap wb= CombinedScreenWorkspace.wallpaperBlur.getWallpaperBlur();
		// mViewShade.setBackBitmap(wb);
		// app_gv_back.setBackground(new BitmapDrawable(wb));
		app_gv_back.startAnimation(alphaAnimation);
		app_gv.startAnimation(scaleAnimation);
	}

	public void hide() {
		if (!isShow()) {
			return;
		}
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setInterpolator(new AccelerateInterpolator());
		alphaAnimation.setDuration(300);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setDuration(300);
		// AnimationSet animationSet=new AnimationSet(true);
		// animationSet.addAnimation(alphaAnimation);
		// animationSet.addAnimation(scaleAnimation);
		app_gv_back.startAnimation(alphaAnimation);
		app_gv.startAnimation(scaleAnimation);
		scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				app_gv_back.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			app_gv_back.setVisibility(View.VISIBLE);
			app_gv.setAdapter(adapter);
			show();
		}
	};

	public interface OnAppInfoSelectedLinstener {
		public void onSelected(AppInfo appInfo);
	}

	OnAppInfoSelectedLinstener mOnAppInfoSelectedLinstener;

	public void setOnItemSelectedListener(OnAppInfoSelectedLinstener l) {
		mOnAppInfoSelectedLinstener = l;
	}

	class AppAdapter<T> extends BaseAdapter implements ListAdapter {

		private List<T> appInfos;

		public void setDatas(List<T> datas) {
			appInfos = datas;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getCount() {
			return appInfos == null ? 0 : appInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return appInfos == null ? null : appInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo appInfo = (AppInfo) appInfos.get(position);
			Holder mHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.app_item, null);
				mHolder = new Holder();
				convertView.setOnClickListener(appItemClickListener);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			mHolder.setView(convertView, position);
			ImageView app_icon = (ImageView) convertView
					.findViewById(R.id.app_icon_iv);
			TextView app_name = (TextView) convertView
					.findViewById(R.id.app_label_iv);
			Drawable drawable = pip.getAppIcon(appInfo.getPackageName());
			if (drawable != null) {
				app_icon.setImageDrawable(drawable);
			}
			app_name.setText(appInfo.getAppName());
			return convertView;
		}

		OnClickListener appItemClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Holder mHolder = (Holder) v.getTag();
				AppInfo mAppInfo = (AppInfo) appInfos.get(mHolder.position);
				if (mOnAppInfoSelectedLinstener != null) {
					mOnAppInfoSelectedLinstener.onSelected(mAppInfo);
				}
				// hide();
			}
		};

		class Holder {

			View view;
			int position;

			public void setView(View view, int position) {
				this.view = view;
				view.setTag(this);
				this.position = position;
			}

		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
