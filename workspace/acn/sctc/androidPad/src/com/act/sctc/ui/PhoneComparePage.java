package com.act.sctc.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.PhoneColumn;

public class PhoneComparePage extends BaseActivity implements OnClickListener {
	private static final String PHONE_IDS="phoneIds";
	private String[] projection=new String[]{PhoneColumn._ID,PhoneColumn.ad_desc,PhoneColumn.brand,PhoneColumn.sale_price,PhoneColumn.start_time,PhoneColumn.touch,PhoneColumn.net_standard,PhoneColumn.os,PhoneColumn.smartphone,PhoneColumn.cpu_core,PhoneColumn.cpu_rate,PhoneColumn.keyborad,PhoneColumn.input};
	String[]strLnames=new String[]{"型号","价格","上市时间","显示屏","3G视频通话","操作系统","智能机","CPU核数","CPU频率","键盘类型","输入方式"};
	//	String[]strParams1=new String[]{"苹果(Apple)","iPhone 5S","5088.00 元","2013年","Retina","支持","iOS 7.0","是","苹果A7 M7","双核","虚拟QWERTY键盘","触控"};
	//	String[]strParams2=new String[]{"三星(Samsung)","GALAXY Note3","4999.00 元","2013年","Super AMOLED","支持","Android 4.3","是","高通 骁龙 800","四核","虚拟QWERTY键盘","触控"};
	//	String[]strParams3=new String[]{"华为(HUAWEI)","C8813DQ","990.00 元","2013年","IPS","不支持","Android 4.1","是","高通MSM8625","双核","虚拟QWERTY键盘","触控"};
	
	// title
	private TitleContentManager mContentManager;
	LayoutInflater mLayoutInflater;
	private String[]phoneIds;
	private final int COLUMN=3;
	public static void start(Activity context,String[] ids,int requestCode){
		Intent intent=new Intent(context,PhoneComparePage.class);
		intent.putExtra(PHONE_IDS, ids);
		context.startActivityForResult(intent, requestCode);
	}
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_compare_page);
		phoneIds=getIntent().getStringArrayExtra(PHONE_IDS);
		mLayoutInflater=LayoutInflater.from(this);
		mContentManager = new TitleContentManager(findViewById(R.id.title_content));
		mContentManager.setUpdateVisibility(View.GONE);
		mContentManager.addSettingBtn(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout table_r=(LinearLayout) findViewById(R.id.table_r);
				table_r.removeAllViews();
				table_r=(LinearLayout) findViewById(R.id.title_r);
				table_r.removeAllViews();
				table_r=(LinearLayout) findViewById(R.id.btn_r);
				table_r.removeAllViews();
				setResult(RESULT_OK);
				finish();
			}
		},R.drawable.clear_selector);
		setLeftTitle();
		initData();
	}
	
	private void initData(){
		if(phoneIds!=null){
			StringBuffer sb=new StringBuffer();
			int length=phoneIds.length-1;
			sb.append(" _id=").append(phoneIds[length]);
			for(int i=length-1;i>=0;i--){
				sb.append(" or _id=").append(phoneIds[i]);
			}
			Cursor phoneCursor=getContentResolver().query(PhoneColumn.CONTENT_URI, projection,sb.toString(), null, null);
			if(phoneCursor!=null&&phoneCursor.moveToFirst()){
				setData(phoneCursor);
				phoneCursor.close();
			}
				
		}
	}

	@Override
	public void onClick(View v) {
		ProductDetailActivity.start(this,Constant.TYPE_PHONE,(Long)v.getTag());
	}
	
	private void setLeftTitle(){
		LinearLayout table_l=(LinearLayout) findViewById(R.id.table_l);
		int size=strLnames.length;
		for(int i=0;i<size;i++){
			TextView tvTextView=(TextView) mLayoutInflater.inflate(R.layout.phone_compare_text_model, null);
			tvTextView.setText(strLnames[i]);
			table_l.addView(tvTextView);
		}
	}

	public void setData(Cursor phoneCursor) {
		LayoutParams lParams=new LayoutParams(0,LayoutParams.WRAP_CONTENT);
		lParams.weight=1;
		
		//data content
		LinearLayout table_r=(LinearLayout) findViewById(R.id.table_r);
		int size=strLnames.length+2;
		for(int i=2;i<size;i++){
			LinearLayout v=new LinearLayout(this);
			v.setWeightSum(COLUMN);
			phoneCursor.moveToFirst();
			do{
				TextView tvTextView=(TextView) mLayoutInflater.inflate(R.layout.phone_compare_text_model, null);
				tvTextView.setText(phoneCursor.getString(i));
				v.addView(tvTextView,lParams);
			}while(phoneCursor.moveToNext());
			table_r.addView(v);
		}	
		
		
		// top title
		table_r=(LinearLayout) findViewById(R.id.title_r);
		table_r.setWeightSum(COLUMN);
		LinearLayout btn_r=(LinearLayout) findViewById(R.id.btn_r);
		btn_r.setWeightSum(COLUMN);
		phoneCursor.moveToFirst();
		do{
			//top title
			TextView tvTextView=(TextView) mLayoutInflater.inflate(R.layout.phone_compare_text_model, null);
			tvTextView.setText(phoneCursor.getString(1));
			table_r.addView(tvTextView,lParams);
			//btn
			LinearLayout btnLiner=new LinearLayout(this);
			Button btn=new Button(this);
			btn.setText(R.string.see_detail);
			btnLiner.addView(btn);
			btn_r.addView(btnLiner,lParams);
			btn.setTag(phoneCursor.getLong(0));
			btn.setOnClickListener(this);
			btn.setBackgroundResource(R.drawable.blue_dark_yellow_selector);
		}while(phoneCursor.moveToNext());
	}
}
