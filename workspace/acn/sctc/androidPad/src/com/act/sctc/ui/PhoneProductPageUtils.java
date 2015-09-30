package com.act.sctc.ui;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;

import com.act.sctc.R;
import com.act.sctc.been.Phone;
import com.act.sctc.ui.PhoneProductItemUtils.OncheckedListener;

public class PhoneProductPageUtils {
	private Context context;
	private View view;
//	private PhoneProductItemUtils ppiu_u1,ppiu_u2,ppiu_u3,ppiu_d1,ppiu_d2,ppiu_d3;
	public PhoneProductItemUtils[] ppItems=new PhoneProductItemUtils[6];

	public PhoneProductPageUtils(List<Phone> datas, int position, Context context) {
		this.context = context;
		iniController();
		setDate(datas,position);
	}
	public PhoneProductPageUtils(Context context) {
		this.context = context;
		iniController();
	}

	private void iniController() {
		view = LayoutInflater.from(context).inflate(R.layout.phone_product_page_item,
				null);
		ppItems[0] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_u1);
		ppItems[0].onCreate();
		ppItems[1] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_u2);
		ppItems[1].onCreate();
		ppItems[2] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_u3);
		ppItems[2].onCreate();
		ppItems[3] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_d1);
		ppItems[3].onCreate();
		ppItems[4] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_d2);
		ppItems[4].onCreate();
		ppItems[5] = (PhoneProductItemUtils) view.findViewById(R.id.ppiu_d3);
		ppItems[5].onCreate();
		
	}
	
	public void setOncheckedListener(OncheckedListener mOncheckedListener){
		ppItems[0].setOnCheckedListener(mOncheckedListener);
		ppItems[1].setOnCheckedListener(mOncheckedListener);
		ppItems[2].setOnCheckedListener(mOncheckedListener);
		ppItems[3].setOnCheckedListener(mOncheckedListener);
		ppItems[4].setOnCheckedListener(mOncheckedListener);
		ppItems[5].setOnCheckedListener(mOncheckedListener);
	}
	
	public void setDate(List<Phone> ls,int position){
		for(int i=0;i<6;i++,position++){
			if(position<ls.size()){
				ppItems[i].setData(ls.get(position));
			}else{
				ppItems[i].setVisibility(View.GONE);
			}
		}
	}

	private void iniListener() {

	}

	private void iniVariable() {

	}

	public View getView() {
		return view;
	}

}
