package com.aess.aemm.view.evaluate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.aess.aemm.R;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.view.data.AppItem;

public final class EnvaluateDialog  {
//	private static final String LOGCAT = "EnvaluateDialog";
	Dialog alertDialog;
	ListView list;
	View customTitle;
	Context mContext;
	BaseAdapter mMenuAdapter;
	LayoutInflater inflater;
	Cursor mCursor;

	public EnvaluateDialog(Context context) {
		mContext = context;
		init();
	}

	private void init() {
		getItems();
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customTitle = inflater.inflate(R.layout.menu_dialog_title, null);
		((TextView)customTitle.findViewById(R.id.title_tv)).setText(R.string.app_manager);
		((Button) customTitle.findViewById(R.id.title_btn)).setVisibility(View.GONE);
		
		createDialog();
	}

	private void createDialog() {

		list = new ListView(mContext);//(ListView) contentView.findViewById(R.id.envaluate_lv);
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		list.setFadingEdgeLength(0);
		mMenuAdapter = new MenuAdapter(mCursor);
		list.setAdapter(mMenuAdapter);
		list.setScrollingCacheEnabled(false);
		list.setBackgroundResource(android.R.color.white);
		alertDialog = new AlertDialog.Builder(mContext)
				.setNegativeButton(android.R.string.cancel, null)
				.setCustomTitle(customTitle).setView(list).create();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int location,
					long arg3) {
				if(mCursor!=null&&mCursor.moveToPosition(location)){
					EnvaluateEditView.start(mContext,mCursor.getString(5),mCursor.getString(1),mCursor.getString(6));
				}
				dismiss();
			}
		});

	}

	public void show() {
		alertDialog.show();
	}

	public void dismiss() {
		alertDialog.dismiss();
	}


	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		list.setOnItemClickListener(mOnItemClickListener);
	}

	private void getItems() {
		mCursor=ApkContent.queryAppForEnvaluete(mContext);
//		for(int i=0;i<40;i++){
//			mDatals.add(new AppItem(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon), "name"+i, "cat"+i%4));
//		}
	}

	class MenuAdapter extends BaseAdapter  {
		AppItem mAppItem;
		Cursor mCursor;
		
		public MenuAdapter(Cursor mCursor){
			this.mCursor=mCursor;
		}

		@Override
		public int getCount() {
			if(mCursor==null){
				return 0;
			}
			return mCursor.getCount();
//			return mDatals.size();
		}

		@Override
		public AppItem getItem(int index) {
			if(mCursor!=null&&mCursor.moveToPosition(index)){
				byte[] temp = Base64.decode(mCursor.getBlob(3),0);
				InputStream is  = new ByteArrayInputStream(temp);
				Bitmap icon = BitmapFactory.decodeStream(is);
//				icon=BitmapFactory.decodeByteArray(data, offset, length)
				return new AppItem(icon,mCursor.getString(2),mCursor.getString(4));
			}
			return null;//mDatals.get(index);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int i, View v, ViewGroup g) {
			if (v == null) {
//				if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
					v = inflater.inflate(R.layout.app_list_item, null);
//				} else {
//					v = inflater.inflate(R.layout.menu_multiple_item, null);
//				}
			}
			mAppItem=getItem(i);
			((ImageView) v.findViewById(R.id.icon_iv)).setImageBitmap(mAppItem.icon);
				((TextView) v.findViewById(R.id.name_tv)).setText(mAppItem.name);
				((TextView) v.findViewById(R.id.categery_tv)).setText(mAppItem.categery);
//				v = inflater.inflate(R.layout.menu_multiple_item, null);
//				((TextView) v.findViewById(R.id.menu_list_text)).setText(menuList[i]);
//				CheckBox mCheckBox = (CheckBox) v.findViewById(R.id.menu__list_checkbox);
//				mCheckBox.setChecked(menuSelects[i]);
//				mCheckBox.setTag(i);
//				mCheckBox.setOnCheckedChangeListener(this);
			return v;
		}
	}
}
