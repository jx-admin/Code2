package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.accenture.mbank.BPERPaymentMenu.OnViewFinish;
import com.accenture.mbank.logic.InsertTransferResponse;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;

public abstract class BPERPayment implements ViewManagerUtils {
	TransferType mTransferType;
	List<AccountsModel> accounts;
	
	Context context;
	TransferType paymentType;
	int step=-1;
	public static final int STEP_1=0,STEP_2=1,STEP_3=2,STEP_RECENT_SHOW=3,STEP_RECENT_EDIT=4;
	
	//askpin
	GenerateOTPResponseModel generateOtp;
	TransferObject mRecentTransferModel;
	//confirm
	InsertTransferResponse insertTransferresponse;
	
	//page
	BPERPaymentRecentDetailsPage mBPERPaymentRecentDetailsPage;
	
	BPERPaymentConfirmPage mBPERPaymentConfirmPage;
	BPERPaymentResultUtil mBPERPaymentResultUtil;
	BPERPaymentDetailsUtil mBPERPaymentDetailsUtil;
	
	boolean isNewPayment=true;
	
	List<AccountsForServiceModel> payerAccounts;
	GetRecipientListModel mGetRecipientListModel;
	List<BankRecipient> bankRecipientList;
	List<CardRecipient> cardRecipientList;
	List<PhoneRecipient> phoneRecipientList;
	
	long effectiveDate;
	
	public BPERPayment(Context context,TransferType mTransferType){
		this.context=context;
		this.mTransferType=mTransferType;
	}
	
	abstract Handler getLoadDateHandler();
	public abstract void invaliDate();
	public abstract ViewGroup getContentView();
	public abstract boolean onBackPressed();
	
	public void loadData(Object o){
		showProgress();
		NewPaymentDataUtils.getAccountsByService(context,getLoadDateHandler(),NewPaymentDataUtils.GET_ACCOUNT,new String[]{getServiceCode()});
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		return false;
	}
	
	DestaccountModel generateDestAccountModel(Object tmpPayee) {
		if (tmpPayee == null) {
			return null;
		}
		DestaccountModel destAccount = null;
		if (tmpPayee instanceof AccountsModel) {
			AccountsModel accountByPhone = (AccountsModel)tmpPayee;
			destAccount = new DestaccountModel();
			destAccount.setIban(accountByPhone.getIbanCode());
			destAccount.setTitle(accountByPhone.getAccountAlias());
			destAccount.setState(accountByPhone.getCardState());
			destAccount.setBic(accountByPhone.getBankCode());
		} else if (tmpPayee instanceof CardRecipient) {
			CardRecipient cr = (CardRecipient)tmpPayee;
			destAccount = new DestaccountModel();
			destAccount.setIban(cr.getCardNumber());
			destAccount.setTitle(cr.getName());
			destAccount.setState(cr.getLast4Digits());
			destAccount.setBic(cr.getId());
		} else if (tmpPayee instanceof PhoneRecipient) {
			destAccount = new DestaccountModel();
			PhoneRecipient pr = (PhoneRecipient)tmpPayee;
			destAccount.setIban(pr.getPhoneNumber());
			destAccount.setTitle(pr.getName());
			destAccount.setState(pr.getProviderCode());
			destAccount.setBic(pr.getId());
		} else {
			destAccount = (DestaccountModel)tmpPayee;
		}
		
		// bic = value.bic;
		String iban = destAccount.getIban();
		String state = "";
		if (iban == null || iban.length() < 2) {
			state = "IT";
		} else {
			state = iban.substring(0, 2).toUpperCase();
		}
		destAccount.setState(state);
		destAccount.setBic("n/a");
		
		if (TextUtils.isEmpty(destAccount.getTitle())) {
			destAccount.setTitle("n/a");
		}
		if (TextUtils.isEmpty(destAccount.getIban())) {
			destAccount.setIban("n/a");
		}
		return destAccount;
	}
	
	public String getServiceCode(){
		return mTransferType.getServiceCode();
	}
	
	public GenerateOTPResponseModel getGenerateOTPResponseModel() {
		// TODO Auto-generated method stub
		return generateOtp;
	}
	
	ProgressOverlay mProgressOverlay;
	void showProgress(){
		if(mProgressOverlay==null){
			mProgressOverlay=new ProgressOverlay(context);
			mProgressOverlay.createDialog(context, R.string.downloading);
		}
		mProgressOverlay.showDialog();
	}
	void dismessProgress(){
		mProgressOverlay.dismissDialog();
	}
	
	abstract void finish();

	OnViewFinish monViewFinish;
	public void setmOnViewFinish(OnViewFinish monViewFinish){
		this.monViewFinish=monViewFinish;
	}
	
	OnClickListener mOnNewPaymentClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(monViewFinish!=null){
				finish();
				monViewFinish.onfinish();
			}
//			showInputPage();
		}
	};
}
