package org.accenture.product.lemonade;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.accenture.product.lemonade.content.SceneDateBaseAdapter;
import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.model.WallpapersBean;
import org.accenture.product.lemonade.receiver.IconReceiver;
import org.accenture.product.lemonade.receiver.SceneReceiver;
import org.accenture.product.lemonade.receiver.WallpapersReceiver;
import org.accenture.product.lemonade.util.MyImg;
import org.accenture.product.lemonade.util.ResourcesUtil;
import org.accenture.product.lemonade.view.MyGallery;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class SettingActivity extends Activity implements OnItemSelectedListener, ViewFactory
{

	private ImageSwitcher mSwitcher;
	private MyGallery setting_gallery;
	private Button apply;

	// private Integer[] mThumbIds =
	// { R.drawable.moka1, R.drawable.moka2, R.drawable.moka3, R.drawable.moka4,
	// R.drawable.moka5, };
	//
	// private Integer[] mImageIds =
	// { R.drawable.moka1, R.drawable.moka2, R.drawable.moka3, R.drawable.moka4,
	// R.drawable.moka5, };

	// private PackageManager mPackageManager;
	// private static final String LOG_TAG = "LiveWallpapersPicker";

	private ArrayList<Bitmap> mThumbnails = new ArrayList<Bitmap>();
	private ArrayList<Bitmap> mPic = new ArrayList<Bitmap>();

	private String TAG = "SettingActivity";

	private int type;

	private static final String WALLPAPER_ACTION = "org.accenture.product.lemonade.update_wallpapers";	
	
	public static final int ICON_SIZE=2;		//目前有多少套ICON供选择
	public static final String ICON_PICKER_PRE="personalization_picker_icon_";
	public static final String THUM_ICON_PICKER_PRE="personalization_picker_icon_thumbnail_";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

		type = getIntent().getIntExtra("type", 0);
		int index = 0;

		if (type == PersonalizationActivity.WALLPAPER)
		{
			ResourcesUtil resource = ResourcesUtil.getInstance();

			Vector<LemonadeBean> picList = resource.wallPaperList;

			int currentId = ResourcesUtil.getInstance()._CurrentWallpapers.get_Id();

			if (picList != null && picList.size() > 0)
			{
				for (int i = 0; i < picList.size(); i++)
				{
					WallpapersBean wallpapersBean = (WallpapersBean) picList.elementAt(i);
					if (currentId == wallpapersBean.get_Id())
					{
						index = i;
					}

					try
					{
						InputStream picIs = getAssets().open(wallpapersBean.get_PagePath());
						BitmapDrawable picDrawable = new BitmapDrawable(picIs);
						Bitmap picBitmap = picDrawable.getBitmap();
						mPic.add(picBitmap);
						picIs.close();

						InputStream thumbIs = getAssets().open(wallpapersBean.get_ThumPath());
						BitmapDrawable thumbDrawable = new BitmapDrawable(thumbIs);
						Bitmap thumbBitmap = thumbDrawable.getBitmap();
						mThumbnails.add(thumbBitmap);
						thumbIs.close();
					}
					catch (Exception e)
					{
						Log.e(TAG, e.toString());
					}
				}
			}
			else
			{
				this.finish();
				Toast toast = Toast.makeText(this, R.string.no_wallpaper_resource, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		else if (type == PersonalizationActivity.SCENE)
		{
			ResourcesUtil resource = ResourcesUtil.getInstance();
			Vector<LemonadeBean> picList = resource.sceneList;
			if (picList != null && picList.size() > 0)
			{
				int currentId = ResourcesUtil.getInstance()._CurrentSceneBean.getId();

				for (int i = 0; i < picList.size(); i++)
				{
					SceneBean sceneBean = (SceneBean) picList.elementAt(i);

					if (currentId == sceneBean.getId())
					{
						index = i;
					}

					try
					{
						InputStream picIs = getAssets().open(sceneBean.getPagePath());
						BitmapDrawable picDrawable = new BitmapDrawable(picIs);
						Bitmap picBitmap = picDrawable.getBitmap();
						mPic.add(picBitmap);
						picIs.close();

						InputStream thumbIs = getAssets().open(sceneBean.getThumPath());
						BitmapDrawable thumbDrawable = new BitmapDrawable(thumbIs);
						Bitmap thumbBitmap = thumbDrawable.getBitmap();
						mThumbnails.add(thumbBitmap);
						thumbIs.close();
					}
					catch (Exception e)
					{
						Log.e(TAG, e.toString());
					}
				}
			}
			else
			{
				this.finish();
				Toast toast = Toast.makeText(this, R.string.no_scene_resource, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		else if (type == PersonalizationActivity.ICON)
		{
			try{
				for(int i=0;i<ICON_SIZE;i++){
					StringBuffer sb=new StringBuffer();
					sb.append("iconinfo/");
					sb.append(i);
					sb.append("/");
					sb.append(ICON_PICKER_PRE);
					sb.append(i);
					sb.append(".png");
					InputStream picIs = getAssets().open(sb.toString());
					BitmapDrawable picDrawable = new BitmapDrawable(picIs);
					Bitmap picBitmap = picDrawable.getBitmap();
					mPic.add(picBitmap);
					picIs.close();
					
					sb=null;
					sb=new StringBuffer();
					sb.append("iconinfo/");
					sb.append(i);
					sb.append("/");
					sb.append(THUM_ICON_PICKER_PRE);
					sb.append(i);
					sb.append(".png");
					InputStream thumbIs = getAssets().open(sb.toString());
					BitmapDrawable thumbDrawable = new BitmapDrawable(thumbIs);
					Bitmap thumbBitmap = thumbDrawable.getBitmap();
					mThumbnails.add(thumbBitmap);
					thumbIs.close();
					
					int currentId=Launcher.luanchr.getCurrentScene().getIconsId();
					Log.e(TAG,"currentId:"+currentId);
					
					if (currentId == i)
					{
						index = i;
					}
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		else if (type == PersonalizationActivity.LOCKSCREEN)
		{
			try{
				for(int i=0;i<ICON_SIZE;i++){
					StringBuffer sb=new StringBuffer();
					sb.append("iconinfo/");
					sb.append(i);
					sb.append("/");
					sb.append(ICON_PICKER_PRE);
					sb.append(i);
					sb.append(".png");
					InputStream picIs = getAssets().open(sb.toString());
					BitmapDrawable picDrawable = new BitmapDrawable(picIs);
					Bitmap picBitmap = picDrawable.getBitmap();
					mPic.add(picBitmap);
					picIs.close();
					
					sb=null;
					sb=new StringBuffer();
					sb.append("iconinfo/");
					sb.append(i);
					sb.append("/");
					sb.append(THUM_ICON_PICKER_PRE);
					sb.append(i);
					sb.append(".png");
					InputStream thumbIs = getAssets().open(sb.toString());
					BitmapDrawable thumbDrawable = new BitmapDrawable(thumbIs);
					Bitmap thumbBitmap = thumbDrawable.getBitmap();
					mThumbnails.add(thumbBitmap);
					thumbIs.close();
					
					int currentId=ResourcesUtil.getInstance()._CurrentSceneBean.getIconsId();
					
					if (currentId == i)
					{
						index = i;
					}
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}

		setting_gallery = (MyGallery) findViewById(R.id.setting_gallery);
		setting_gallery.setAdapter(new ImageAdapter(this));
		setting_gallery.setOnItemSelectedListener(this);
		setting_gallery.setSelection(index);

		apply = (Button) findViewById(R.id.setting_apply);

		apply.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 发送广播
				if (type == PersonalizationActivity.WALLPAPER)
				{
					int index = setting_gallery.getSelectedItemPosition();
					WallpapersBean wallpapersBean = (WallpapersBean) ResourcesUtil.getInstance().wallPaperList.elementAt(index);
					// WallpapersDateBaseAdapter
					// adapter=WallpapersDateBaseAdapter.getInstance();
					// adapter.open();
					// adapter.update(wallpapersBean);
					// adapter.close();

					Intent intent = new Intent();
					intent.setAction(WALLPAPER_ACTION);
					intent.putExtra(WallpapersReceiver.EXTRAS_KEY, wallpapersBean);

//					finish();

					sendBroadcast(intent);
					
					Intent goHome=new Intent(SettingActivity.this,Launcher.class);
					startActivity(goHome);
				}
				else if (type == PersonalizationActivity.SCENE)
				{
					int index = setting_gallery.getSelectedItemPosition();

					SceneBean sceneBean = new SceneBean();
					sceneBean.setId(index + 1);
					sceneBean.setUse(true);
					Intent intent = new Intent();
					intent.setAction(SceneReceiver.UPDATE_SCENE);
					intent.putExtra(SceneReceiver.EXTRAS_KEY, sceneBean);

					// finish();

					sendBroadcast(intent);

					Intent goToHome = new Intent(SettingActivity.this, Launcher.class);
					startActivity(goToHome);
				}
				else if (type == PersonalizationActivity.ICON)
				{

					int index = setting_gallery.getSelectedItemPosition();
					
					// update Scene_table's icons
					SceneDateBaseAdapter sceneDateBaseAdapter = SceneDateBaseAdapter.getInstance();
					sceneDateBaseAdapter.open();
					SceneBean currentBean = sceneDateBaseAdapter.getCurrentScene();
					sceneDateBaseAdapter.close();
					currentBean.setIconsId(index);
					sceneDateBaseAdapter.open();
					sceneDateBaseAdapter.updateIcons(currentBean);
					sceneDateBaseAdapter.close();
					Launcher.luanchr.refreshDesttopforIconChange();
					
//					finish();
					
//					Toast toast=Toast.makeText(SettingActivity.this,SettingActivity.this.getString(R.string.update_icon_success), Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					
					Intent intent=new Intent(SettingActivity.this,Launcher.class);
					startActivity(intent);

				}
				else if (type == PersonalizationActivity.LOCKSCREEN)
				{
					int index = setting_gallery.getSelectedItemPosition();
					if (index == 0)
					{
//						//停止锁屏服务
//						Intent intent = new Intent(SettingActivity.this, LockService.class);
//						stopService(intent);
					}
					else if (index == 1)
					{
//						//停止锁屏服务
//						Intent intent = new Intent(SettingActivity.this, LockService.class);
//						stopService(intent);
//						
//						// 启动锁屏服务
//						startService(intent);
					}

					finish();

					Toast toast = Toast.makeText(SettingActivity.this, SettingActivity.this
							.getString(R.string.update_lock_success), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		
	}
	
	public class ImageAdapter extends BaseAdapter
	{
		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return mThumbnails.size();
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{

			ImageView i = new ImageView(mContext);
			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			// Resources re = mContext.getResources();
			// InputStream is = re.openRawResource(mThumbnails.get(position));
			// BitmapDrawable mapdraw = new BitmapDrawable(is);
			// Bitmap bitmap = mapdraw.getBitmap();
			// mThumbnails.get(position)
			// Drawable drawable=mThumbnails.get(position);
			// Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			Bitmap bitmap = mThumbnails.get(position);
			
			if(position==0){
				i.setImageBitmap(MyImg.createReflectedImage(bitmap,false));
			}else{
				i.setImageBitmap(MyImg.createReflectedImage(bitmap,true));
			}
			
//			BlurMaskFilter blurFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER);
//			Paint shadowPaint = new Paint();
//			shadowPaint.setMaskFilter(blurFilter);
//			Bitmap shadowBitmap = bitmap.extractAlpha(shadowPaint,new int[2]);
//			Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
//			i.setBackgroundDrawable(new BitmapDrawable(shadowImage32));
			
			// i.setImageDrawable(mThumbnails.get(position));
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			// i.setBackgroundResource(R.drawable.picture_frame);
			return i;
		}

		private Context mContext;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View v, int position, long id)
	{
		mSwitcher.setImageDrawable(new BitmapDrawable(mPic.get(position)));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	@Override
	public View makeView()
	{
		ImageView i = new ImageView(this);
		// i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	// private void findLiveWallpapers() {
	// List<ResolveInfo> list = mPackageManager.queryIntentServices(
	// new Intent(WallpaperService.SERVICE_INTERFACE),
	// PackageManager.GET_META_DATA);
	//
	// int listSize = list.size();
	//        
	// mThumbnails = new ArrayList<Bitmap>(listSize);
	// mPic=new ArrayList<Bitmap>(listSize);
	//        
	// for (int i = 0; i < listSize; i++) {
	// ResolveInfo resolveInfo = list.get(i);
	// ComponentInfo ci = resolveInfo.serviceInfo;
	// WallpaperInfo info;
	// try {
	// info = new WallpaperInfo(this , resolveInfo);
	// } catch (XmlPullParserException e) {
	// Log.w(LOG_TAG, "Skipping wallpaper " + ci, e);
	// continue;
	// } catch (IOException e) {
	// Log.w(LOG_TAG, "Skipping wallpaper " + ci, e);
	// continue;
	// }
	//            
	// Drawable thumb = info.loadThumbnail(mPackageManager);
	// // Drawable pic=info.loadIcon(mPackageManager);
	// Drawable pic=info.loadThumbnail(mPackageManager);
	//            
	// thumb.setDither(true);
	// pic.setDither(true);
	//            
	// mThumbnails.add(thumb);
	// mPic.add(pic);
	// }
	// }
}