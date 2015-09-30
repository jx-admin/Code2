package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.PaymentStepViewUtils;
import com.accenture.mbank.util.PaymentStepViewUtils.Step;

public abstract class BPERPaymentInputPage implements ViewManagerUtils {
	Context context;
	LayoutInflater mLayoutInflater;
	ViewGroup contentView;
	
	AccountsModel mAccountsModel;
	BPERPaymentAccountPaperUtils mBPERPaymentAccountPaperUtils;
	TextView page_title;
	PaymentStepViewUtils mPaymentStepViewUtils;
	
	private boolean clearToExecute = false;
	
	TransferType mTransferType;
	
	public BPERPaymentInputPage(Context context,TransferType mTransferType){
		this.context=context;
		this.mTransferType=mTransferType;
		mLayoutInflater=LayoutInflater.from(context);
		getContentView();
	}
	
	public ViewGroup getContentView(){
		if(contentView==null){
			contentView=(ViewGroup) mLayoutInflater.inflate(getViewId(), null);
			init();
			setPageTitle(mTransferType);
		}
		onCheckForConfirm(false);
		return contentView;
	}
	
	abstract int getViewId();
	
	void init(){
		page_title=(TextView) contentView.findViewById(R.id.page_title);
		mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
		mPaymentStepViewUtils.setStep(Step.STEP1);
		mBPERPaymentAccountPaperUtils = new BPERPaymentAccountPaperUtils(contentView.findViewById(R.id.account_include));
//		mBPERPaymentAccountPaperUtils.accounts_vp.setOnPageChangeListener(this);
		contentView.findViewById(R.id.root_content).setOnClickListener(new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
			}
		});
	}
	void init(OnViewClickListener mOnViewClickListener){
		page_title=(TextView) contentView.findViewById(R.id.page_title);
		mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
		mPaymentStepViewUtils.setStep(Step.STEP1);
		mBPERPaymentAccountPaperUtils = new BPERPaymentAccountPaperUtils(contentView.findViewById(R.id.account_include));
//		mBPERPaymentAccountPaperUtils.accounts_vp.setOnPageChangeListener(this);
		contentView.findViewById(R.id.root_content).setOnClickListener(mOnViewClickListener);
	}
	
	public void setPageTitle(TransferType type){
		page_title.setText(type.getPageTitleId(TransferType.TITLE_NEWPAYMENT));
	}
	
	public void setOnPageChangeListener(OnPageChangeListener mOnPageChangeListener){
		mBPERPaymentAccountPaperUtils.setOnPageChangeListener(mOnPageChangeListener);
	}

	public AccountsModel getAccountsModel(){
		return mBPERPaymentAccountPaperUtils.getAccountsModel();
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * The confirmation button (Continua) is disabled until the user fill all
	 * mandatory fields.
	 * 
	 * After the user clicks confirmation button (Continua), the app must check
	 * all the mandatory fields.
	 * 
	 * @return true if user fill all mandatory fields
	 */
	abstract boolean onCheckForConfirm(boolean showError);
	abstract void disableConfirmButn();
	
    void onConfirm(){
    	if(mOnDoneListener!=null){
    		mOnDoneListener.onConfirm(this);
    	}
    }

	
	OnTouchListener mOnConfirmTouchListener=new OnTouchListener() {
		boolean result=true;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			switch(event.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				result=onCheckForConfirm();
//				break;
//			}
//			if(result){
//				return true;
//			}else {
				return v.onTouchEvent(event);
//			}
		}
	};
	
	OnViewClickListener mOnConfirmClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			onConfirm();
		}
	};
	
	public void setDoneListener(OnDoneListener mOnDoneListener){
		this.mOnDoneListener=mOnDoneListener;
	}
	OnDoneListener mOnDoneListener;
	public interface OnDoneListener{
		
		public void onConfirm(Object o);
		
	}
	TextWatcher confirmTextChangeListener=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onCheckForConfirm(false);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	TextWatcher confirmTextChangeForPhoneNumberListener=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onCheckForConfirm(false);
			disableConfirmButn();
			if(s.toString().indexOf("*") > -1){
				clearToExecute = true;
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(clearToExecute){
				s.clear();
				clearToExecute = false;
			}
						
		}
	};
}
abstract class OnConfirmCheckUtils{

	/**
	 * The confirmation button (Continua) is disabled until the user fill all
	 * mandatory fields.
	 * 
	 * After the user clicks confirmation button (Continua), the app must check
	 * all the mandatory fields.
	 * 
	 * @return true if user fill all mandatory fields
	 */
	abstract boolean onCheckForConfirm(boolean showError);
	
    void onConfirm(){
    	if(mOnDoneListener!=null){
    		mOnDoneListener.onConfirm(this);
    	}
    }


	OnTouchListener mOnConfirmTouchListener=new OnTouchListener() {
		boolean result=true;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			switch(event.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				result=onCheckForConfirm();
//				break;
//			}
//			if(result){
//				return true;
//			}else {
				return v.onTouchEvent(event);
//			}
		}
	};
	
	OnViewClickListener mOnConfirmClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			onConfirm();
		}
	};
	
	public void setDoneListener(OnDoneListener mOnDoneListener){
		this.mOnDoneListener=mOnDoneListener;
	}
	OnDoneListener mOnDoneListener;
	public interface OnDoneListener{
		
		public void onConfirm(Object o);
		
	}
	TextWatcher confirmTextChangeListener=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onCheckForConfirm(false);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
}