package com.act.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.sctc.R;
import com.act.sctc.db.DBHelper;


public class Test extends Activity implements OnClickListener {
	private TextView request_tv;
	private TextView respons_tv;
	private LinearLayout operate_ll;
	private int operateCount;

	private DBHelper dBHelper;
	
	private int db_w_id=0,db_r_id=0,db_u_id=0,db_d_id=0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		request_tv=(TextView) findViewById(R.id.request_tv);
		respons_tv=(TextView) findViewById(R.id.respons_tv);
		operate_ll=(LinearLayout) findViewById(R.id.operate_ll);
		
		operateCount=0;
		db_w_id=addOperator("wdb");
		db_r_id=addOperator("rdb");
		db_u_id=addOperator("udb");
		db_d_id=addOperator("ddb");
		
		dBHelper = DBHelper.getInstance(this);

	}
	
	public int addOperator(String str){
		Button btn=new Button(this);
		btn.setText(str);
		btn.setOnClickListener(this);
		btn.setId(operateCount);
		operate_ll.addView(btn);
		return operateCount++;
	}


	@Override
	public void onClick(View v) {
		int vId=v.getId();
		if(vId==db_w_id){
//			ProductPhone pp=new ProductPhone();
//			pp.setName("jx");
//			pp.setDescr("test");
//			// 声明Uri
//			Uri uri = ProductPhoneColumn.CONTENT_URI;
//			// 实例化ContentValues
//			ContentValues values = new ContentValues();
//			// 添加员工信息
//			values.put(ProductPhoneColumn.DESCR,pp.getDescr());
//			values.put(ProductPhoneColumn.NAME,pp.getName());
//			// 获得ContentResolver，并插入
//			getContentResolver().insert(uri, values);
			
		}else if(vId==db_r_id){
//			Uri uri = ContentUris.withAppendedId(ProductPhoneColumn.CONTENT_URI, 3);
//			Cursor cursor = dBHelper.query(true, ProductPhoneColumn.TABLE_NAME, ProductPhoneColumn.SELECTION, null, null, null, null, null, null, null);
////					dBHelper.query(ProductPhoneColumn.TABLE_NAME, ProductPhoneColumn.SELECTION, null, null, null, null, null, "3 offset 3");
////					getContentResolver().query(ProductPhoneColumn.CONTENT_URI, ProductPhoneColumn.SELECTION, /*"_id>3 and _id<10"*/null, null, null);
//			if(cursor==null||!cursor.moveToFirst()){
//				respons_tv.setText("null");
//				return ;
//			}
//			respons_tv.setText(Utils.toString(cursor));
//			cursor.close();

		}else if(vId==db_u_id){
//			// 更新ID为1的记录
//			Uri uri = ContentUris.withAppendedId(ProductPhoneColumn.CONTENT_URI, 1);
//			ContentValues values = new ContentValues();
//			// 添加员工信息
//			values.put(ProductPhoneColumn.DESCR, "dupdate");
//			// 获得ContentResolver，并更新
//			getContentResolver().update(uri, values, null, null);

		}else if(vId==db_d_id){
//			// 删除ID为1的记录
//			Uri uri = ContentUris.withAppendedId(ProductPhoneColumn.CONTENT_URI, 1);
//			// 获得ContentResolver，并删除
//			getContentResolver().delete(uri, null, null);

		}
		
	}
	
}
