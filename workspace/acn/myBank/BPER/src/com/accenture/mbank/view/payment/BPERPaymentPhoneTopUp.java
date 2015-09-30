package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;

import com.accenture.mbank.BPERPayment;
import com.accenture.mbank.BPERPaymentMenu;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.LoginActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.PhoneBookActivity;
import com.accenture.mbank.logic.InsertRecipientJson;
import com.accenture.mbank.logic.InsertTransferResponse;
import com.accenture.mbank.logic.OtpState;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.BperRecentTransferResponseModel;
import com.accenture.mbank.model.CheckSimTopUpResponseModel;
import com.accenture.mbank.model.CompanyAmountResponseModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.view.adapter.PhoneRecipientAdapter;
import com.accenture.mbank.view.payment.BPERPaymentInputPage.OnDoneListener;
import com.accenture.mbank.view.payment.BPERPaymentPhoneTopUpStep1.onProviderclickListener;

public class BPERPaymentPhoneTopUp extends com.accenture.mbank.view.payment.BPERPayment implements OnTabChangeListener {
	
	TransferType paymentType=TransferType.PHONE_TOP_UP;
	CheckSimTopUpResponseModel checkSimTopUp;// checkTransfer;
	PhoneRecipient mBankRecipient;
	TransferObjectSim mRecentTransferModel;
	
	
	BPERPaymentPhoneTopUpStep1 mBperPaymentBankTransferStep1;
	
	public enum TYPE {  
		RECENT,RECENT_DETAIL,NEW_PAYMENT,CONFIRM,RESULT,DETAIL  
	} 
	
	private TYPE type;
	private boolean isforcedCell = false;
	
	public BPERPaymentPhoneTopUp(Context context,TransferType mTransferType) {
		super(context, mTransferType);
		init();
	}

	Handler getLoadDateHandler(){
		return loadDateHandler;
	}
	
	public void invaliDate(){
		showInputPage();
	}
	
	public ViewGroup getContentView(){
		return mBperPaymentBankTransferStep1.getContentView();
	}
	
	void init(){
		
		mBperPaymentBankTransferStep1=new BPERPaymentPhoneTopUpStep1(context,mTransferType);
		
		mBperPaymentBankTransferStep1.setDoneListener(new OnDoneListener(){
			
			@Override
			public void onConfirm(Object o) {
				validate();
			}
			
		});
		
		mBperPaymentBankTransferStep1.setOnTabChangeListener(this);
		
		mBperPaymentBankTransferStep1.setOnBeneficiaryClickListener(new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				showPhoneBook();
			}
		});
		
		mBperPaymentBankTransferStep1.setOnProviderclickListener(new onProviderclickListener() {
			
			@Override
			public void onClick(View v, int position) {
				showProgress();
				NewPaymentDataUtils.getAmountAvailableLs(context, loadDateHandler, NewPaymentDataUtils.GET_AMOUNT_AVAILABLE, getPayer().getAccountCode(), mBperPaymentBankTransferStep1.getProviderCode(), mBperPaymentBankTransferStep1.getPhoneNumber());
			}
		});
		
		mBperPaymentBankTransferStep1.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(!isNewPayment){
					loadRecentTransfer();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}
	
	private void showInputPage(){
		mBperPaymentBankTransferStep1.setNewPayment(isNewPayment);
		((BPERPaymentMenu)context).setContentView(mBperPaymentBankTransferStep1.getContentView());
		if(isNewPayment){
			type=TYPE.NEW_PAYMENT;
		}else{
			type=TYPE.RECENT;
		}
	}
	
	private void showConfirm(){
		if(mBPERPaymentConfirmPage==null){
			mBPERPaymentConfirmPage=new BPERPaymentConfirmPage(context);
			mBPERPaymentConfirmPage.setOnAskPinClickListener(onAskPinClickListener);
			mBPERPaymentConfirmPage.setOnConfirmClickListener(mOnConfirmClickListener);
		}
		double amount=0;
		if(mBperPaymentBankTransferStep1.getAmount()!=null){
			amount=mBperPaymentBankTransferStep1.getAmount().getRechargeAmount();
		}
		mBPERPaymentConfirmPage.showPhoneTopUp(mBperPaymentBankTransferStep1.getAccountsModel(), mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getPhoneNumber(), mBperPaymentBankTransferStep1.getProviderName(), amount,TransferType.TITLE_NEWPAYMENT);
		((BPERPaymentMenu)context).setContentView(mBPERPaymentConfirmPage.getContentView());
		type=TYPE.CONFIRM;
	}
	
	private void showResult(){
		if(mBPERPaymentResultUtil==null){
			mBPERPaymentResultUtil=new BPERPaymentResultUtil(context, mTransferType);
			mBPERPaymentResultUtil.setOnDetailClickListener(mOnDetailClickListener);
			mBPERPaymentResultUtil.setOnGoSyntesisClickListener(mOnGoSyntesisClickListener);
			mBPERPaymentResultUtil.setOnNewPaymentClickListener(mOnNewPaymentClickListener);
		}
		mBPERPaymentResultUtil.setResult(insertTransferresponse!=null&&insertTransferresponse.isSuccess(),isNewPayment?TransferType.TITLE_NEWPAYMENT:TransferType.TITLE_NEWPAYMENT_RECENT);
		((BPERPaymentMenu)context).setContentView(mBPERPaymentResultUtil.getContentView());
		type=TYPE.RESULT;
	}
	
	private void showDetail(){
		mBPERPaymentDetailsUtil=new BPERPaymentDetailsUtil(context);
		mBPERPaymentDetailsUtil.setOnbackClickListener(new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				onBackPressed();
			}
		});
		mBPERPaymentDetailsUtil.showPhoneTopUp(mBperPaymentBankTransferStep1.getAccountsModel(), mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getPhoneNumber(), mBperPaymentBankTransferStep1.getProviderName(), mBperPaymentBankTransferStep1.getAmount().getRechargeAmount());
		((BPERPaymentMenu)context).setContentView(mBPERPaymentDetailsUtil.getContentView());
		type=TYPE.DETAIL;
	}
	
	private void showRecentRecoverPage(){
		BPERPayment.startForPaymentDetail(context,BPERPayment.TYPE.RECOVER,getPayer(),mRecentTransferModel,isNewPayment?TransferType.TITLE_NEWPAYMENT:TransferType.TITLE_NEWPAYMENT_RECENT);
	}
	
	private void showRecentDetailsPage(){
		if(mBPERPaymentRecentDetailsPage==null){
			mBPERPaymentRecentDetailsPage=new BPERPaymentRecentDetailsPage(context);
			mBPERPaymentRecentDetailsPage.setOnConfirmClickListener(mOnRecentDetailConfirmClickListener);
		}
		mBPERPaymentRecentDetailsPage.show(this.getPayer(), mRecentTransferModel,TransferType.TITLE_NEWPAYMENT_RECENT);
		((BPERPaymentMenu)context).showViewManagerUtils(mBPERPaymentRecentDetailsPage);
		type=TYPE.RECENT_DETAIL;
	}
	
	OnViewClickListener mOnConfirmClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			confirm();
		}
	};
	
	OnViewClickListener onAskPinClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			askPin(checkSimTopUp.isOtpByEmail());
		}
	};
	
	OnViewClickListener mOnDetailClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			showDetail();
		}
	};
	
	OnViewClickListener mOnGoSyntesisClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			final MainActivity mainActivity = (MainActivity)MainActivity.getContext();
			mainActivity.showTab(0);
			((BPERPaymentMenu)context).finish();
		}
	};
	
	OnViewClickListener onResultButtonOneClickLinstener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			((BPERPaymentMenu)context).show();
		}
	};
	
	OnViewClickListener onResultButtonTowClickLinstener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			showInputPage();
		}
	};

	OnSlideItemClickListener mOnSlideItemClickListener=new OnSlideItemClickListener(){

		@Override
		public void onHideClick(int position) {
			mRecentTransferModel=(TransferObjectSim) mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentRecoverPage();
		}

		@Override
		public void onItemClick(View view, int position) {
			mRecentTransferModel=(TransferObjectSim) mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentDetailsPage();
		}
		
	};
	
	OnViewClickListener mOnRecentDetailConfirmClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			showRecentRecoverPage();
			((BPERPaymentMenu)context).showPop();
		}};
		
		
		private void validate(){
			showProgress();
			String transferId=isNewPayment?"":"";
			NewPaymentDataUtils.validateSimTopUp(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_SIM_TOP_UP, getPayer().getAccountCode(), mBperPaymentBankTransferStep1.getProviderCode(), mBperPaymentBankTransferStep1.getPhoneNumber(), mBperPaymentBankTransferStep1.getAmount(), transferId);
		}
		
		private void askPin(boolean otpMail){
			showProgress();
			if(!otpMail){
				isforcedCell = true;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.PHONE_TOP_UP.getJsonItType(),mBperPaymentBankTransferStep1.getPhoneNumber(),mBperPaymentBankTransferStep1.getAmount().getRechargeAmount(), "false");
			} else {
				isforcedCell = false;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.PHONE_TOP_UP.getJsonItType(),mBperPaymentBankTransferStep1.getPhoneNumber(),mBperPaymentBankTransferStep1.getAmount().getRechargeAmount(), null);
			}
		}
		
		private void confirm(){
			showProgress();
			NewPaymentDataUtils.confirmTranslationSimTopUp(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_PHONE_TOP_UP,mBperPaymentBankTransferStep1.getAccountsModel().getAccountCode(), mBperPaymentBankTransferStep1.getProviderCode(), mBperPaymentBankTransferStep1.getPhoneNumber(), mBPERPaymentConfirmPage.getPinCode(), getGenerateOTPResponseModel().getOtpKeySession(), mBperPaymentBankTransferStep1.getAmount(), checkSimTopUp.getTransferId());
		}
		private void insertRecipient(){
			PhoneRecipient mPhoneRecipient=new PhoneRecipient();
			mPhoneRecipient.setName(mBperPaymentBankTransferStep1.getBeneficiary());
			mPhoneRecipient.setPhoneNumber(mBperPaymentBankTransferStep1.getPhoneNumber());
			mPhoneRecipient.setProviderCode(mBperPaymentBankTransferStep1.getProviderCode());
			NewPaymentDataUtils.insertRecipient(context, loadDateHandler, NewPaymentDataUtils.INSERT_RECIPIENT, InsertRecipientJson.PHONE, mPhoneRecipient);
		}
		
		public boolean onBackPressed() {
			if(type==null){
				return false;
			}
			switch(type){
			case NEW_PAYMENT:
			case RECENT:
				return false;
			case RECENT_DETAIL:
			case CONFIRM:
				showInputPage();
				return true;
			case RESULT:
				showInputPage();
				return true;
			case DETAIL:
				showResult();
				return true;
			}
			return false;
		}
		
		@Override
		public void onTabChanged(String tabId) {
			if(BPERPaymentBankTransferStep1.RECENTPAYMENT_TAB.equals(tabId)){
				isNewPayment=false;
				loadRecentTransfer();
			}else{
				isNewPayment=true;
			}
		}
		
		
		private void loadRecentTransfer(){
			AccountsModel payer=getPayer();
			if(payer!=null){
				showProgress();
				NewPaymentDataUtils.getTablesResponseModel(context,loadDateHandler, NewPaymentDataUtils.LOAD_RECENPAYMENT,payer.getAccountCode());
			}
		}
		
		private Handler loadDateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NewPaymentDataUtils.GET_ACCOUNT:
					GetAccountsByServicesResponseModel mGetAccountsByServicesResponseModel=(GetAccountsByServicesResponseModel) msg.obj;
					if(mGetAccountsByServicesResponseModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel.isSuccess()){
						payerAccounts=mGetAccountsByServicesResponseModel.getAccountsForServiceList();
						if(payerAccounts!=null&&payerAccounts.size()>0){
							accounts=payerAccounts.get(0).getAccounts();
							mBperPaymentBankTransferStep1.mBPERPaymentAccountPaperUtils.setData(accounts);
							if(accounts!=null&&accounts.size()>0){
								NewPaymentDataUtils.getRecipientList(context,loadDateHandler,NewPaymentDataUtils.getRecipientList,TransferType.PHONE_TOP_UP);
								effectiveDate=mGetAccountsByServicesResponseModel.getEffectiveDate();
							}else{
								dismessProgress();
								DialogManager.createMessageDialog(R.string.payment_getaccount_by_services_empty,"", context,new OnClickListener(){
									
									@Override
									public void onClick(View v) {
										((Activity)context).onBackPressed();
									}},null).show();
							}
						}
					}else{
						dismessProgress();
						((Activity)context).onBackPressed();
						EventManagement mEventManagement=null;
						if(mGetAccountsByServicesResponseModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel!=null){
							mEventManagement=mGetAccountsByServicesResponseModel.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
					break;
				case NewPaymentDataUtils.getRecipientList:
					mGetRecipientListModel=(GetRecipientListModel) msg.obj;
					if(mGetRecipientListModel!=null&&mGetRecipientListModel.responsePublicModel.isSuccess()){
						bankRecipientList=mGetRecipientListModel.getBankRecipientList();
						cardRecipientList=mGetRecipientListModel.getCardRecipientList();
						phoneRecipientList=mGetRecipientListModel.getPhoneRecipientList();
						mBperPaymentBankTransferStep1.setAdapter(new PhoneRecipientAdapter(context, R.layout.phonebook_item, phoneRecipientList));
					}else{
						dismessProgress();
						DialogManager.createMessageDialog(R.string.getrecent_list_error, context).show();
					}
					invaliDate();
					dismessProgress();
					break;
				case NewPaymentDataUtils.LOAD_SEPA_COUNTRY_IBAN:
					mBperPaymentBankTransferStep1.setSepaCountryIBAN((TablesResponseModel) msg.obj);
//					NewPaymentDataUtils.getTablesResponseModel(context,loadDateHandler, NewPaymentDataUtils.LOAD_PURPOSE_CURRENCY,"PURPOSE_CURRENCY");
					dismessProgress();
					break;
				case NewPaymentDataUtils.LOAD_PURPOSE_CURRENCY:
					mBperPaymentBankTransferStep1.setPurposeCurrency((TablesResponseModel) msg.obj);
					dismessProgress();
					break;
				case NewPaymentDataUtils.GET_ASKPIN:
					dismessProgress();
					generateOtp = (GenerateOTPResponseModel)msg.obj;
					if (generateOtp != null && generateOtp.responsePublicModel != null&&generateOtp.responsePublicModel.isSuccess()) {
						mBPERPaymentConfirmPage.showAskPin();
						if(isforcedCell){
							Toast.makeText(context, R.string.pin_success_by_sms, Toast.LENGTH_LONG) .show();
						} else if (BaseActivity.isOffline||MainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
							Toast.makeText(context, R.string.pin_success_by_email, Toast.LENGTH_LONG).show();
						} else if (MainActivity.setting.getChannelToRecelvePin() == SettingModel.SMS) {
							Toast.makeText(context, R.string.pin_success_by_sms, Toast.LENGTH_LONG) .show();
						}
					} else {
						EventManagement mEventManagement=null;
						if(generateOtp!=null&&generateOtp.responsePublicModel!=null){
							mEventManagement=generateOtp.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
					break;
				case NewPaymentDataUtils.VALIDATE_SIM_TOP_UP:
					checkSimTopUp = (CheckSimTopUpResponseModel)msg.obj;
						dismessProgress();
						String errorCode=null;
						if(checkSimTopUp.responsePublicModel.eventManagement!=null){
							errorCode=checkSimTopUp.responsePublicModel.eventManagement.getErrorCode();
						}
						if("91068".equals(errorCode)){
							DialogManager.createMessageDialog(R.string.bank_transf_91083, context).show();
						} else if("91083".equals(errorCode)){
							String bankServiceCode=null;
							AccountsModel mAccountsModel =getPayer();
							if(mAccountsModel!=null){
								BankServiceType mBankServiceType=mAccountsModel.getBankServiceType();
								if(mBankServiceType!=null){
									bankServiceCode=mBankServiceType.getBankServiceCode();
								}
								
							}
							if("020".equals(bankServiceCode)){
								DialogManager.createMessageDialog(R.string.error_code_91083_020, context).show();
							}else if("887".equals(bankServiceCode)){
								DialogManager.createMessageDialog(R.string.error_code_91083_887, context).show();
							}else{
								DialogManager.createMessageDialog(R.string.bank_transfer_other_error, context).show();
							}
						} else if("91085".equals(errorCode)){
							DialogManager.createMessageDialog(R.string.error_code_91085, context).show();
						}else if (checkSimTopUp!=null&&checkSimTopUp.responsePublicModel.isSuccess()) {
							showConfirm();//displayPaymentRecap();
							if(mBperPaymentBankTransferStep1.add_phonebook_sbtn.isChecked()){
								insertRecipient();
							}else{
								dismessProgress();
							}
						} else {
							EventManagement mEventManagement=null;
							if(checkSimTopUp!=null&&checkSimTopUp.responsePublicModel!=null){
								mEventManagement=checkSimTopUp.responsePublicModel.eventManagement;
							}
							DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
						}
					break;
					
				case NewPaymentDataUtils.CONFIRM_PHONE_TOP_UP:
					dismessProgress();
					insertTransferresponse = (InsertTransferResponse)msg.obj;
					
					if(insertTransferresponse!=null&&insertTransferresponse.isSuccess()){
						
					}else{
						OtpState mOtpState=null;
						if(insertTransferresponse!=null){
							mOtpState=insertTransferresponse.getOtpState();
						}
						if(mOtpState!=null&&mOtpState.getOtpErrorCode()==8){
							if(mOtpState.getOtpAvailable()==0||mOtpState.getOtpAvailable()==3){
								LoginActivity.logout(context);
								return;
							}else if(mOtpState.getOtpAvailable()>3){
								String errorMsg=context.getString(R.string.opt_available_3,mOtpState.getOtpAvailable()-3);
								DialogManager.createMessageDialog(errorMsg, context).show();
								return;
							}else if(mOtpState.getOtpAvailable()<3||mOtpState.getOtpAvailable()>0){
								String errorMsg=context.getString(R.string.opt_available_out3,mOtpState.getOtpAvailable());
								DialogManager.createMessageDialog(errorMsg, context).show();
								return;
							}
						}else if(mOtpState!=null&&mOtpState.getOtpErrorCode()==12){
							DialogManager.createMessageDialog(R.string.pin_scaduto, context).show();
							return;
						}
					}
					showResult();
					break;
				case NewPaymentDataUtils.INSERT_RECIPIENT:
					dismessProgress();
					ResponsePublicModel mResponsePublicModel = (ResponsePublicModel)msg.obj;
					if(mResponsePublicModel!=null&&mResponsePublicModel.isSuccess()){
					}
					dismessProgress();
					break;
				case NewPaymentDataUtils.LOAD_RECENPAYMENT:
					BperRecentTransferResponseModel mBperRecentTransferResponseModel= (BperRecentTransferResponseModel) msg.obj;
					if(mBperRecentTransferResponseModel!=null){
						List<TransferObjectSim> recentTransferList=mBperRecentTransferResponseModel.getRecentSimTopUpList();
						mBperPaymentBankTransferStep1.setRecentTransferModels(recentTransferList,mOnSlideItemClickListener);
						if(recentTransferList==null){
							DialogManager.displayErrorMessage(mBperRecentTransferResponseModel==null?null:mBperRecentTransferResponseModel.getEventManagement(),-1, context).show();
						}
					}
					dismessProgress();
					if(mBperRecentTransferResponseModel==null||!mBperRecentTransferResponseModel.isSuccess()){
						DialogManager.displayErrorMessage(mBperRecentTransferResponseModel==null?null:mBperRecentTransferResponseModel.getEventManagement(),-1, context).show();
					}
					break;
				case NewPaymentDataUtils.GET_AMOUNT_AVAILABLE:
					dismessProgress();
					CompanyAmountResponseModel companyAmount = (CompanyAmountResponseModel)msg.obj;
					List<AmountAvailable> mAmountAvailableLs =null;
					errorCode=null;
					if(companyAmount!=null&&companyAmount.responsePublicModel!=null&&companyAmount.responsePublicModel.eventManagement!=null){
						errorCode=companyAmount.responsePublicModel.eventManagement.getErrorCode();
					}
					if("91050".equals(errorCode)||"91051".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.error_code_91050_91051, context).show();
						mBperPaymentBankTransferStep1.focusPhoneNumber();
						mBperPaymentBankTransferStep1.setError_code_91050_91051(true);
						return;
					}else if (companyAmount!=null&&companyAmount.responsePublicModel!=null&&companyAmount.responsePublicModel.isSuccess()) {
						mAmountAvailableLs = companyAmount.getAmountAvailable();
						mBperPaymentBankTransferStep1.setError_code_91050_91051(false);
					}
					
					// CompanyAmount
					if (mAmountAvailableLs != null&& mAmountAvailableLs.size() > 0) {
						if(isNewPayment){
							mBperPaymentBankTransferStep1.setAmountAvailableLs(mAmountAvailableLs);
						}
					}else {
						EventManagement eventManagement=null;
						if(companyAmount!=null&&companyAmount.responsePublicModel!=null){
							eventManagement=companyAmount.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(eventManagement, 0, context);
					}
					break;
				default:
					break;
				}
			}
		};
		
		int PHONE_BOOK_RESULT=10;
		public void showPhoneBook(){
			PhoneBookActivity.start(((Activity)context),PHONE_BOOK_RESULT,paymentType);
		}
		
		public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode==Activity.RESULT_OK){
				if(requestCode==PHONE_BOOK_RESULT){//phonebook
					mBankRecipient=(PhoneRecipient) data.getSerializableExtra("item");
					mBperPaymentBankTransferStep1.setRecipient(mBankRecipient);
//					Toast.makeText(context, "onActivityResult", 1).show();
					return true;
				}
			}
			return false;
		}
		
		public AccountsModel getPayer(){
			return mBperPaymentBankTransferStep1.getAccountsModel();
		}


		@Override
		void finish() {
			type=null;
		}
}