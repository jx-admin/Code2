package com.aess.aemm.view.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.aess.aemm.R;

public final class MenuDialog implements OnClickListener {
	private static final String LOGCAT = "MenuDialog";
	private static final String PREF_NAME = "menuItemState";
	private static final String NODE_COUNT="COUNT";
	Dialog alertDialog;
	ListView list;
	View customTitle;
	Button btn;
	Context mContext;
	BaseAdapter mMenuAdapter;
	String curentList[];
	String menuList[];
	boolean menuSelects[];
	LayoutInflater inflater;

	public MenuDialog(Context context) {
		mContext = context;
		init();
	}

	private void init() {
		menuList = mContext.getResources().getStringArray(R.array.menu_list);
		menuSelects = new boolean[menuList.length];
		getMenuItemState();
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customTitle = inflater.inflate(R.layout.menu_dialog_title, null);
		btn = (Button) customTitle.findViewById(R.id.title_btn);
		btn.setOnClickListener(this);
		list = new ListView(mContext);
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		list.setFadingEdgeLength(0);
		mMenuAdapter = new MenuAdapter();
		list.setAdapter(mMenuAdapter);
		list.setChoiceMode(ListView.CHOICE_MODE_NONE);
		 list.setScrollingCacheEnabled(false);
		 list.setBackgroundResource(android.R.color.white);

		list.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int localtion, long arg3) {
				menuSelects[localtion] = true;
				Toast.makeText(mContext, localtion + "selcect",  Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// Ìí¼Óµã»÷
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(mContext, arg2 + " click",  Toast.LENGTH_SHORT).show();
			}
		});
		createDialog();
	}

	private void createDialog() {

		alertDialog = new AlertDialog.Builder(mContext)
				.setNegativeButton(android.R.string.cancel, null)
				.setCustomTitle(customTitle).setView(list).create();
	}

	public void show() {
		alertDialog.show();
	}

	public void dismiss() {
		alertDialog.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
			list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			btn.setText(R.string.finishSTR);
		} else {
			setMenuItemState();
			getMenuItemState();
			list.setChoiceMode(ListView.CHOICE_MODE_NONE);
			btn.setText(R.string.edit);
		}
		list.setAdapter(mMenuAdapter);
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		list.setOnItemClickListener(mOnItemClickListener);
	}

	public int getLocalId(int index) {
		for (int i = menuList.length - 1; i >= 0; i--) {
			if (menuList[i].equals(curentList[index])) {
				return i;
			}
		}
		return -1;
	}

	private void setMenuItemState() {
		int count = 0;
		Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
		editor.clear();
		for (int i =0;i< menuList.length ; i++) {
			if (menuSelects[i]) {
				editor.putInt(Integer.toString(count), i);
				count++;
			}
		}
		editor.putInt(NODE_COUNT, count);
		editor.commit();
	}
	
	public static void delMenuItem(Context cxt) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.clear();
		editor.putInt(NODE_COUNT, -1);
		editor.commit();
	}

	private void getMenuItemState() {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(
				PREF_NAME, 0);
		int count = mSharedPreferences.getInt(NODE_COUNT, 0);
		Log.d(LOGCAT, "read count " + count);
		if (count > 0) {
			curentList = new String[count];
			int temp = -1;
			for (int i = 0; i < count; i++) {
				temp = mSharedPreferences.getInt(Integer.toString(i), -1);
				Log.d(LOGCAT, "read temp " + temp);
				if (temp != -1) {
					menuSelects[temp] = true;
					curentList[i] = menuList[temp];

					Log.d(LOGCAT, i + " read " + curentList[i] + " menulist "
							+ menuList[temp]);
					// }else{
					// menuSelects[temp]=false;
				}
			}
		} else {
			setAll();
		}
	}

	private void setAll() {
		curentList = new String[menuList.length];
		for (int i = menuList.length - 1; i >= 0; i--) {
			menuSelects[i] = true;
			curentList[i] = menuList[i];
		}
	}

	class MenuAdapter extends BaseAdapter implements OnCheckedChangeListener {

		@Override
		public int getCount() {
			if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
				return curentList.length;
			} else {
				return menuList.length;
			}
		}

		@Override
		public Object getItem(int index) {
			if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
				return curentList[index];
			} else {
				return menuList[index];
			}
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int i, View v, ViewGroup g) {
			if (v == null) {
				if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
					v = inflater.inflate(R.layout.menu_list_item, null);
				} else {
					v = inflater.inflate(R.layout.menu_multiple_item, null);
				}
			}
			if (list.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
				((TextView) v.findViewById(R.id.menu_list_text)).setText(curentList[i]);
			} else {
				v = inflater.inflate(R.layout.menu_multiple_item, null);
				((TextView) v.findViewById(R.id.menu_list_text)).setText(menuList[i]);
				CheckBox mCheckBox = (CheckBox) v.findViewById(R.id.menu__list_checkbox);
				mCheckBox.setChecked(menuSelects[i]);
				mCheckBox.setTag(i);
				mCheckBox.setOnCheckedChangeListener(this);
			}
			return v;
		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			int i = (Integer) arg0.getTag();
			Log.d(LOGCAT, i + " check " + arg1);
			menuSelects[i] = arg1;
		}

	}
}
