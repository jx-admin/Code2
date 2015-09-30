package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.PaymentStepViewUtils;
import com.accenture.mbank.util.PaymentStepViewUtils.Step;

public class BPERPaymentResultUtil implements ViewManagerUtils{
	Context context;
	TransferType mTransferType;
	View contentView;

	TextView error_title;
	TextView error_msg;
	TextView success;
	PaymentStepViewUtils mPaymentStepViewUtils;
	TextView page_title;
	ImageView imageView1;
	Button paymet_detail_btn;
	Button button_go_syntesis_btn;
	Button new_payment_button_btn;
	public BPERPaymentResultUtil(Context context,TransferType mTransferType){
		this.context=context;
		this.mTransferType=mTransferType;
		getContentView();
	}
	public View getContentView(){
		if(contentView==null){
			contentView=LayoutInflater.from(context).inflate(R.layout.bper_paymenty_result_page, null);
			mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
			mPaymentStepViewUtils.setStep(Step.STEP3);
			page_title=(TextView) contentView.findViewById(R.id.page_title);
			imageView1=(ImageView) contentView.findViewById(R.id.result_iv);
			error_title=(TextView) contentView.findViewById(R.id.error_title);
			error_msg=(TextView) contentView.findViewById(R.id.error_msg);
			success=(TextView) contentView.findViewById(R.id.success);
			paymet_detail_btn=(Button) contentView.findViewById(R.id.paymet_detail_btn);
			button_go_syntesis_btn=(Button) contentView.findViewById(R.id.button_go_syntesis_btn);
			new_payment_button_btn=(Button) contentView.findViewById(R.id.new_payment_button_btn);
		}
		return contentView;
	}
	public void setResult(boolean isSuccessful,int newPayment){
		if(isSuccessful){
			imageView1.setImageResource(R.drawable.payment_v);
			error_title.setVisibility(View.GONE);
			error_msg.setVisibility(View.GONE);
			success.setVisibility(View.VISIBLE);
			paymet_detail_btn.setVisibility(View.VISIBLE);
		}else{
			imageView1.setImageResource(R.drawable.payment_x);
			error_title.setVisibility(View.VISIBLE);
			error_msg.setVisibility(View.VISIBLE);
			success.setVisibility(View.GONE);
			paymet_detail_btn.setVisibility(View.GONE);
		}
		page_title.setText(mTransferType.getPageTitleId(newPayment));
	}
	
	public void setOnDetailClickListener(OnClickListener l){
		paymet_detail_btn.setOnClickListener(l);
	}
	
	public void setOnNewPaymentClickListener(OnClickListener l){
		new_payment_button_btn.setOnClickListener(l);
	}
	
	public void setOnGoSyntesisClickListener(OnClickListener l){
		button_go_syntesis_btn.setOnClickListener(l);
	}
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
