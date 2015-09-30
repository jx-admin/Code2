package com.act.sctc.ui;

import java.util.ArrayList;

import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.oneMin.demo.slideListView.SlideAdapter;
import cn.oneMin.demo.slideListView.SlideListView;

import com.act.sctc.BaseActivity;
import com.act.sctc.R;
import com.act.sctc.been.Goods;
import com.act.sctc.db.CustomerColumn;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.GoodsCartColumn;
import com.act.sctc.db.SqlUtils;

public class ShoppingCarActivity extends BaseActivity implements OnClickListener {
	private TitleContentManager mTitleContentManager;
	private SlideListView slideListView;
	private SlideAdapter adapter;
	private static ArrayList<Goods> mGoodsData=new ArrayList<Goods>();
//	private Cursor mCursor;
	private Button sure_btn;
	
	private EditText name_et;
	private EditText phone_number_et;
	private EditText licence_et;
	private EditText address_et;
	
	long customerId;
	
	public void save(Context contex){
		ContentResolver cr=contex.getContentResolver();
		customerId=saveCustomer(this);
		if(mGoodsData!=null&&mGoodsData.size()>0){
			int size=mGoodsData.size();
			for(int i=0;i<size;i++){
				Goods goods=mGoodsData.get(i);
				ContentValues cValues=new ContentValues();
				cValues.put(GoodsCartColumn.user_id, customerId);
				cValues.put(GoodsCartColumn.product_id,goods.getProduct_id());
				cValues.put(GoodsCartColumn.product_type,goods.getProduct_type());
				cValues.put(GoodsCartColumn.count,goods.getCount());
				cValues.put(GoodsCartColumn.mark,goods.getMark());
				if(goods.get_id()>0){
					cr.update(ContentUris.withAppendedId(GoodsCartColumn.CONTENT_URI,goods.get_id()), cValues,null,null);
				}else {
					cr.insert(GoodsCartColumn.CONTENT_URI, cValues);
				}
			}
		}
	}
	public long saveCustomer(Context context){
		ContentValues cValues=new ContentValues();
		cValues.put(CustomerColumn.name, name_et.getText().toString());
		cValues.put(CustomerColumn.phone, phone_number_et.getText().toString());
		cValues.put(CustomerColumn.licence, licence_et.getText().toString());
		cValues.put(CustomerColumn.address, address_et.getText().toString());
		Cursor cursor=SqlUtils.getCustom(context, licence_et.getText().toString());
		if(cursor!=null&&cursor.moveToFirst()){
			long id=cursor.getLong(0);
			int nums=context.getContentResolver().update(ContentUris.withAppendedId(CustomerColumn.CONTENT_URI, id), cValues, null, null);
			if(nums>0){
				return id;
			}
			cursor.close();
		}else{
			Uri uri= context.getContentResolver().insert(CustomerColumn.CONTENT_URI, cValues);
			return ContentUris.parseId(uri);
		}
		return -1;
	}
	
	public void readCustomer(){
		Cursor cursor=SqlUtils.getCustom(this,licence_et.getText().toString());
		if(cursor!=null&&cursor.moveToFirst()){
			name_et.setText(cursor.getString(1));
			phone_number_et.setText(cursor.getShort(2));
			licence_et.setText(cursor.getString(3));
			address_et.setText(cursor.getString(4));
			cursor.close();
		}
	}
	
	public static boolean addGoods(long _id, long user_id, long product_id, int categoryId, int count, String mark){
		Goods goods=new Goods(_id,
				user_id,
				product_id,
				categoryId,
				count,
				mark);
		if(mGoodsData.contains(goods)){
			return false;
		}
		mGoodsData.add(goods);
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_car_page);
		iniController();
		iniVariable();
		iniListener();

	}
	
	private void iniController(){
		slideListView=(SlideListView) findViewById(R.id.sListView);
		sure_btn=(Button) findViewById(R.id.sure_btn);
		mTitleContentManager=new TitleContentManager(findViewById(R.id.title_content));
		mTitleContentManager.setUpdateVisibility(View.GONE);
		name_et=(EditText) findViewById(R.id.name_et);
		phone_number_et=(EditText) findViewById(R.id.phone_number_et);
		licence_et=(EditText) findViewById(R.id.licence_et);
		address_et=(EditText) findViewById(R.id.address_et);
	}
	private void iniVariable(){
		Cursor mCursor=SqlUtils.getShoppingCart(this,customerId);
		if(mCursor!=null&&mCursor.moveToFirst()){
//			mGoodsData
			do{
				Goods goods=new Goods(mCursor.getLong(0),mCursor.getLong(1),mCursor.getLong(2),mCursor.getInt(3),mCursor.getInt(4),mCursor.getString(5));
				mGoodsData.add(goods);
			}while(mCursor.moveToNext());
			mCursor.close();
		}

		
//		mGoodsData = OfflineData.getGoodsData();
		adapter=new SlideAdapter(this);
		adapter.setDatas(mGoodsData);
//		adapter.setDatas(mGoodsData);
		slideListView.setAdapter(adapter);	
	}
	
	protected void onStart(){
		super.onStart();
	}
	private void iniListener(){
		sure_btn.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.sure_btn){
			save(this);
			mGoodsData.clear();
			Intent i=new Intent(this,HomePageActivity.class);
			startActivity(i);
			finish();
		}
	}
}
