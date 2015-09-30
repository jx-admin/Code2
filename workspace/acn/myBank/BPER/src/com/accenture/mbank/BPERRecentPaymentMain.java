package com.accenture.mbank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;

import com.accenture.mbank.BPERPayment.TYPE;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.view.payment.BPERPaymentRecentDetailsPage;

public class BPERRecentPaymentMain extends MenuActivity{
	
	AccountsModel mAccountsModel;
	TransferObject mRecentTransferModel;
	

	boolean isDetailShow;
//	BPERPaymentRecoverPhonePage mBPERPaymentRecoverPhonePage;
	BPERPaymentRecentDetailsPage mBPERPaymentRecentDetailsPage;
	BPERRecentPaymentPage mBPERRecentPaymentPage;
	

	public static void start(Context context) {
		Intent i = new Intent(context, BPERRecentPaymentMain.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBPERRecentPaymentPage=new BPERRecentPaymentPage(this);
		mBPERRecentPaymentPage.setOnRecentItemClickLinstener(mOnSlideItemClickListener);
		setContentView(mBPERRecentPaymentPage.getContentView());
		mBPERRecentPaymentPage.loadData();
		
//		mBPERPaymentRecoverPhonePage=new BPERPaymentRecoverPhonePage(this);
//		BPERPayment.startForPaymentDetail(this, TYPE.RECOVER);
//		BPERPayment.startForPaymentDetail(this, TYPE.CONFIRM);
//		BPERPayment.startForPaymentDetail(this, TYPE.RESULT);
//		BPERPayment.startForPaymentDetail(this, TYPE.DETAIL);
//		setContentView(mBPERPaymentRecentDetailsPage.getContentView());
//		setContentView(mBPERPaymentRecoverPhonePage.getContentView());
	}
	
	OnSlideItemClickListener mOnSlideItemClickListener=new OnSlideItemClickListener(){

		@Override
		public void onHideClick(int position) {
			mAccountsModel=mBPERRecentPaymentPage.getAccountsModel();
			mRecentTransferModel=mBPERRecentPaymentPage.getRecentTransferModel(position);
			startRecentRecoverPage();
		}

		@Override
		public void onItemClick(View view, int position) {
			mAccountsModel=mBPERRecentPaymentPage.getAccountsModel();
			mRecentTransferModel=mBPERRecentPaymentPage.getRecentTransferModel(position);
			startRecentDetailsPage();
		}
		
	};
	
	OnClickListener mOnRecentDetailConfirmClickListener=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			startRecentRecoverPage();
			BPERRecentPaymentMain.this.onBackPressed();
		}};
	
	public void startRecentDetailsPage(){
		if(mBPERPaymentRecentDetailsPage==null){
			mBPERPaymentRecentDetailsPage=new BPERPaymentRecentDetailsPage(this);
			mBPERPaymentRecentDetailsPage.setOnConfirmClickListener(mOnRecentDetailConfirmClickListener);
		}
		mBPERPaymentRecentDetailsPage.show(mAccountsModel, mRecentTransferModel,TransferType.TITLE_RECENTPAYMENT);
		isDetailShow=true;
		setContentView(mBPERPaymentRecentDetailsPage.getContentView());
	}
	
	public void startRecentRecoverPage(){
		BPERPayment.startForPaymentDetail(this,TYPE.RECOVER,mAccountsModel,mRecentTransferModel,TransferType.TITLE_NEWPAYMENT);
	}

	@Override
	public void onBackPressed() {
		if(isDetailShow){
			isDetailShow=false;
			setContentView(mBPERRecentPaymentPage.getContentView());
		}else{
			super.onBackPressed();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
	}

}
