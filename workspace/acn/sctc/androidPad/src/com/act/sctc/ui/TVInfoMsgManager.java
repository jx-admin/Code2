package com.act.sctc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.SqlUtils;

public class TVInfoMsgManager implements OnClickListener {
	private View view;
	private Context context;
	
	private Button select_setmeal_btn;
	
	private int type=Constant.TYPE_ITV;
	private Button add_to_shopping_cart_btn;
	
	public TVInfoMsgManager(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.tv_info_msg_content, null);
		onCreate();
	}
	public View getView(){
		return view;
	}
	
	public void onCreate(){
		select_setmeal_btn = (Button) view.findViewById(R.id.select_setmeal_btn);
		add_to_shopping_cart_btn = (Button) view.findViewById(R.id.add_to_shopping_cart_btn);
		add_to_shopping_cart_btn.setEnabled(tvPackageId>0);
		iniListener();
	}
	
	private void iniListener() {
		select_setmeal_btn.setOnClickListener(this);
		add_to_shopping_cart_btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v == select_setmeal_btn) {
			PackageActivity.start(((Activity)context),type, Constant.CATEGORY_TV_PACKAGE,tvPackageId,SETMEAL_RESULT_CODE);
		}else if(v==add_to_shopping_cart_btn){
			if(tvPackageId>0){
				ShoppingCarActivity.addGoods(0, 0, tvPackageId,  Constant.CATEGORY_TV_PACKAGE, 1 , "");
			}
		}
	}
	
	private int tvPackageId;
	private static final int SETMEAL_RESULT_CODE=10,CONTRACT_PHONE_SETMEAL_RESULT_CODE=11;
	public boolean onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode!=Activity.RESULT_OK){
			return false;
		}
		
		
		//可以根据多个请求代码来作相应的操作  
		if(requestCode==SETMEAL_RESULT_CODE){
			if(data!=null){
				tvPackageId=data.getIntExtra("data", 0);
				if(tvPackageId>0){
					Cursor cursor=SqlUtils.getPackage(context, tvPackageId);
					if(cursor!=null&&cursor.moveToFirst()){
						select_setmeal_btn.setText(cursor.getString(1));
					}
					cursor.close();
				}
			}else{
				select_setmeal_btn.setText(R.string.click_select_setmeal);
			}
			add_to_shopping_cart_btn.setEnabled(tvPackageId>0);
		}
		
		return true;
	} 
	
}
