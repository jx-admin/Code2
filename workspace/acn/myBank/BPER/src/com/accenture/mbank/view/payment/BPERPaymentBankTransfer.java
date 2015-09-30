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
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.BperRecentTransferResponseModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TableWrapperList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.view.adapter.BankRecipientAdapter;
import com.accenture.mbank.view.payment.BPERPaymentInputPage.OnDoneListener;

public class BPERPaymentBankTransfer extends com.accenture.mbank.view.payment.BPERPayment implements OnTabChangeListener {
	
	//validate
	private CheckTransferResponseModel checkTransfer;
	//phonebook recipient
	private BankRecipient mBankRecipient;
	
	BPERPaymentBankTransferStep1 mBperPaymentBankTransferStep1;
	
	public enum TYPE {  
		RECENT,RECENT_DETAIL,NEW_PAYMENT,CONFIRM,RESULT,DETAIL  
	} 
	
	private TYPE type;
	private boolean isforcedCell = false;
	
	public BPERPaymentBankTransfer(Context context,TransferType mTransferType) {
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
		mBperPaymentBankTransferStep1=new BPERPaymentBankTransferStep1(context,mTransferType);
		mBperPaymentBankTransferStep1.setOnPurposeCurrencyClickListener(new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				List<TableContentList> mPurposeCurrencyTableContentList=mBperPaymentBankTransferStep1.getPurposeCurrencyTablesResponseModel();
				if(mPurposeCurrencyTableContentList==null||mPurposeCurrencyTableContentList.size()<=0){
					loadPurposeCurrency();
				}else{
					mBperPaymentBankTransferStep1.showPurposeCurrencySelectDialog();
				}
			}
		});
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
		mBPERPaymentConfirmPage.showBankTranser(mBperPaymentBankTransferStep1.getAccountsModel(),mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getIban(), mBperPaymentBankTransferStep1.getBIC(),mBperPaymentBankTransferStep1.getCUP(),mBperPaymentBankTransferStep1.getCIG(),mBperPaymentBankTransferStep1.getAmount(), mBperPaymentBankTransferStep1.getDescription(), mBperPaymentBankTransferStep1.getPurposeCurrencyName(),mBperPaymentBankTransferStep1.getDate(),checkTransfer==null?0:checkTransfer.getCharges(),TransferType.TITLE_NEWPAYMENT);
		((BPERPaymentMenu)context).setContentView(mBPERPaymentConfirmPage.getContentView());
		type=TYPE.CONFIRM;
	}
	
	private void insertRecipient(){
		BankRecipient mPhoneRecipient=new BankRecipient();
		mPhoneRecipient.setName(mBperPaymentBankTransferStep1.getBeneficiary());
		mPhoneRecipient.setBic(mBperPaymentBankTransferStep1.getBIC());
		mPhoneRecipient.setIbanCode(mBperPaymentBankTransferStep1.getIban());
		NewPaymentDataUtils.insertRecipient(context, loadDateHandler, NewPaymentDataUtils.INSERT_RECIPIENT, InsertRecipientJson.BANK, mPhoneRecipient);
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
		mBPERPaymentDetailsUtil.setOnbackClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mBPERPaymentDetailsUtil.showBankTranser(mBperPaymentBankTransferStep1.getAccountsModel(), mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getIban(), mBperPaymentBankTransferStep1.getBIC(), mBperPaymentBankTransferStep1.getCUP(), mBperPaymentBankTransferStep1.getCIG(), mBperPaymentBankTransferStep1.getAmount(), mBperPaymentBankTransferStep1.getDescription(), mBperPaymentBankTransferStep1.getPurposeCurrencyName(), mBperPaymentBankTransferStep1.getDate(), checkTransfer.getCharges());
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
			askPin(checkTransfer.isOtpByEmail());
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
			mRecentTransferModel=mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentRecoverPage();
		}

		@Override
		public void onItemClick(View view, int position) {
			mRecentTransferModel=mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentDetailsPage();
		}
		
	};
	
	OnViewClickListener mOnRecentDetailConfirmClickListener=new OnViewClickListener(){
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			showRecentRecoverPage();
			((BPERPaymentMenu)context).showPop();
		}};
		
		DestaccountModel mDestaccountModel;
		private void validate(){
			showProgress();
			mDestaccountModel=new DestaccountModel();
	    	mDestaccountModel.setTitle(mBperPaymentBankTransferStep1.getBeneficiary());
	    	mDestaccountModel.setIban(mBperPaymentBankTransferStep1.getIban());
	    	mDestaccountModel.setBic(mBperPaymentBankTransferStep1.getBIC());
	    	mDestaccountModel.setCig(mBperPaymentBankTransferStep1.getCIG());
	    	mDestaccountModel.setCup(mBperPaymentBankTransferStep1.getCUP());
			NewPaymentDataUtils.validateBankTransfer(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_TRANSFER, true, getPayer().getAccountCode(),mBperPaymentBankTransferStep1.getPurposeCurrencyCode(),mDestaccountModel,mBperPaymentBankTransferStep1.getAmount(),mBperPaymentBankTransferStep1.getDescription(),mBperPaymentBankTransferStep1.getDate(),null);
		}
		
		private void askPin(boolean otpMail){
			showProgress();
			if(!otpMail){
				isforcedCell = true;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.BANK_TRANSFER.getJsonItType(),mBperPaymentBankTransferStep1.getBeneficiary(),mBperPaymentBankTransferStep1.getAmount(), "false");
			} else {
				isforcedCell = false;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.BANK_TRANSFER.getJsonItType(),mBperPaymentBankTransferStep1.getBeneficiary(),mBperPaymentBankTransferStep1.getAmount(), null);
			}
		}
		
		private void confirm(){
			showProgress();
			NewPaymentDataUtils.confirmBankTransfer(context,loadDateHandler,NewPaymentDataUtils.CONFIRM_TRANSFER,getPayer().getAccountCode() ,mBperPaymentBankTransferStep1.getAmount(),mBperPaymentBankTransferStep1.getPurposeCurrencyCode(),mBperPaymentBankTransferStep1.getDescription(),mBperPaymentBankTransferStep1.getDate(),mDestaccountModel,mBPERPaymentConfirmPage.getPinCode(),getGenerateOTPResponseModel().getOtpKeySession(),checkTransfer,true);
		}
		
		public boolean onBackPressed() {
			if(type==null){
				return false;
			}
			switch(type){
			case NEW_PAYMENT:
			case RECENT:
				return mBperPaymentBankTransferStep1.onBackPressed();
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
				showProgress();
				NewPaymentDataUtils.getTablesResponseModel(context,loadDateHandler, NewPaymentDataUtils.LOAD_SEPA_COUNTRY_IBAN,"SEPA_COUNTRY_IBAN");
			}
		}
		
		private void loadPurposeCurrency(){
			showProgress();
			NewPaymentDataUtils.getTablesResponseModel(context,loadDateHandler, NewPaymentDataUtils.LOAD_PURPOSE_CURRENCY,"PURPOSE_CURRENCY");
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
				if(msg.obj==null){
					dismessProgress();
					((BaseActivity)context).displayErrorMessage("",context.getString(R.string.connection_error));
					return;
				}
				switch (msg.what) {
				case NewPaymentDataUtils.GET_ACCOUNT:
					GetAccountsByServicesResponseModel mGetAccountsByServicesResponseModel=(GetAccountsByServicesResponseModel) msg.obj;
					if(mGetAccountsByServicesResponseModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel.isSuccess()){
						payerAccounts=mGetAccountsByServicesResponseModel.getAccountsForServiceList();
						if(payerAccounts!=null&&payerAccounts.size()>0){
							accounts=payerAccounts.get(0).getAccounts();
							mBperPaymentBankTransferStep1.mBPERPaymentAccountPaperUtils.setData(accounts);
							if(accounts!=null&&accounts.size()>0){
								NewPaymentDataUtils.getRecipientList(context,loadDateHandler,NewPaymentDataUtils.getRecipientList,TransferType.BANK_TRANSFER);
								effectiveDate=mGetAccountsByServicesResponseModel.getEffectiveDate();
								mBperPaymentBankTransferStep1.setDate(effectiveDate);
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
					if(mGetRecipientListModel!=null){
						bankRecipientList=mGetRecipientListModel.getBankRecipientList();
						cardRecipientList=mGetRecipientListModel.getCardRecipientList();
						phoneRecipientList=mGetRecipientListModel.getPhoneRecipientList();
						mBperPaymentBankTransferStep1.setAdapter(new BankRecipientAdapter(context, R.layout.phonebook_item, bankRecipientList));
					}
					invaliDate();
					dismessProgress();
					break;
				case NewPaymentDataUtils.LOAD_SEPA_COUNTRY_IBAN:
					List<TableContentList> mTableContentList=null;
					TablesResponseModel mTablesResponseModel=(TablesResponseModel) msg.obj;
					if(mTablesResponseModel!=null&&mTablesResponseModel.responsePublicModel!=null&&mTablesResponseModel.responsePublicModel.isSuccess()){
						TableWrapperList mTableWrapperList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN);
						if(mTableWrapperList!=null){
							mTableContentList=mTableWrapperList.getTableContentList();
						}
					}
					
					dismessProgress();
					if(mTableContentList!=null&&mTableContentList.size()>0){
						mBperPaymentBankTransferStep1.setSepaCountryIBAN(mTablesResponseModel);
					}else{
						isNewPayment=true;
						showInputPage();
//						((Activity)context).onBackPressed();
					}
					break;
				case NewPaymentDataUtils.LOAD_PURPOSE_CURRENCY:
					mTablesResponseModel=(TablesResponseModel) msg.obj;
					List<TableContentList> purposeCurrencyTableContentList=null;
					if(mTablesResponseModel!=null){
						purposeCurrencyTableContentList=mTablesResponseModel.getTableWrapperList("PURPOSE_CURRENCY");
					}
					dismessProgress();
					if(purposeCurrencyTableContentList!=null&&purposeCurrencyTableContentList.size()>0){
						mBperPaymentBankTransferStep1.setPurposeCurrency(purposeCurrencyTableContentList);
						mBperPaymentBankTransferStep1.showPurposeCurrencySelectDialog();
					}else{
						com.accenture.mbank.model.EventManagement mEventManagement=null;
						if(mTablesResponseModel!=null&&mTablesResponseModel.responsePublicModel!=null){
							mEventManagement=mTablesResponseModel.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement, 0, context).show();
					}
					break;
				case NewPaymentDataUtils.GET_ASKPIN:
					generateOtp = (GenerateOTPResponseModel)msg.obj;
					dismessProgress();
					if(generateOtp!=null&&generateOtp.responsePublicModel!=null&&generateOtp.responsePublicModel.isSuccess()){
						mBPERPaymentConfirmPage.showAskPin();
						if(isforcedCell){
//							Toast.makeText(context, R.string.pin_success_by_sms, Toast.LENGTH_LONG) .show();
						} else if (BaseActivity.isOffline||MainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
//							Toast.makeText(context, R.string.pin_success_by_email, Toast.LENGTH_LONG).show();
						} else if (MainActivity.setting.getChannelToRecelvePin() == SettingModel.SMS) {
//							Toast.makeText(context, R.string.pin_success_by_sms, Toast.LENGTH_LONG) .show();
						}
					}else{
						EventManagement mEventManagement=null;
						if(generateOtp!=null&&generateOtp.responsePublicModel!=null){
							mEventManagement=generateOtp.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
					break;
				case NewPaymentDataUtils.VALIDATE_TRANSFER:
					dismessProgress();
					checkTransfer = (CheckTransferResponseModel)msg.obj;
					com.accenture.mbank.model.EventManagement mEventManagement=null;
					String errorCode=null;
					if(checkTransfer.responsePublicModel!=null&&checkTransfer.responsePublicModel.eventManagement!=null){
						mEventManagement=checkTransfer.responsePublicModel.eventManagement;
						errorCode=checkTransfer.responsePublicModel.eventManagement .getErrorCode();
					}
					if("90".equals(errorCode)){
						DialogManager.createMessageDialog(0, checkTransfer.responsePublicModel.eventManagement.getErrorDescription(), context,R.string.as_cancel, R.string.as_proced,new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							}
						}, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								showConfirm();
								if(mBperPaymentBankTransferStep1.add_phonebook_sbtn.isChecked()){
									showProgress();
									insertRecipient();
								}else{
									dismessProgress();
								}
							}
						}).show();
					}else if("91".equals(errorCode)){
						DialogManager.createMessageDialog(0, checkTransfer.responsePublicModel.eventManagement.getErrorDescription(), context, R.string.as_cancel, R.string.as_proced,new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							}
						}, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								String bankServiceCode=null;
								AccountsModel mAccountsModel =getPayer();
								if(mAccountsModel!=null){
									BankServiceType mBankServiceType=mAccountsModel.getBankServiceType();
									if(mBankServiceType!=null){
										bankServiceCode=mBankServiceType.getBankServiceCode();
									}
									
								}
								int msgId=R.string.bank_transfer_other_error;
								if(BaseActivity.isOffline||"020".equals(bankServiceCode)){
									msgId=R.string.error_code_91083_020_transfer;
								}else if(BaseActivity.isOffline||"887".equals(bankServiceCode)){
									msgId=R.string.error_code_91083_887_transfer;
								}
								DialogManager.createMessageDialog(msgId,null, context,  R.string.as_cancel, R.string.as_proced,new OnClickListener() {
									
									@Override
									public void onClick(View v) {
									}
								}, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										showConfirm();
									}
								}).show();
							}
						}).show();
					}else if("91083".equals(errorCode)){
						String bankServiceCode=null;
						AccountsModel mAccountsModel =getPayer();
						if(mAccountsModel!=null){
							BankServiceType mBankServiceType=mAccountsModel.getBankServiceType();
							if(mBankServiceType!=null){
								bankServiceCode=mBankServiceType.getBankServiceCode();
							}
							
						}
						if("020".equals(bankServiceCode)){
							DialogManager.createMessageDialog(R.string.error_code_91083_020_transfer, context).show();
						}else if("887".equals(bankServiceCode)){
							DialogManager.createMessageDialog(R.string.error_code_91083_887_transfer, context).show();
						}else{
							DialogManager.createMessageDialog(R.string.bank_transfer_other_error, context).show();
						}
					}else if("91082".equals(errorCode)){
						String bankServiceCode=null;
						AccountsModel mAccountsModel =getPayer();
						if(mAccountsModel!=null){
							BankServiceType mBankServiceType=mAccountsModel.getBankServiceType();
							if(mBankServiceType!=null){
								bankServiceCode=mBankServiceType.getBankServiceCode();
							}
							
						}
						int msgId=R.string.bank_transfer_other_error;
						if(BaseActivity.isOffline||"020".equals(bankServiceCode)){
							msgId=R.string.error_code_91083_020_transfer;
						}else if(BaseActivity.isOffline||"887".equals(bankServiceCode)){
							msgId=R.string.error_code_91083_887_transfer;
						}
						DialogManager.createMessageDialog(msgId,null, context,  R.string.as_cancel, R.string.as_proced,new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							}
						}, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								showConfirm();
							}
						}).show();
					}else if("91085".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.error_code_91085, context).show();
					}else if("91091".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.error_code_91091, context).show();
					}else if(checkTransfer!=null&&checkTransfer.isSuccess()){
							showConfirm();
							if(mBperPaymentBankTransferStep1.add_phonebook_sbtn.isChecked()){
								showProgress();
								insertRecipient();
							}else{
								dismessProgress();
							}
					}else{
						DialogManager.displayErrorMessage(mEventManagement,R.string.bank_transfer_other_error, context).show();
					}
					break;

				case NewPaymentDataUtils.INSERT_RECIPIENT:
					dismessProgress();
					ResponsePublicModel mResponsePublicModel = (ResponsePublicModel)msg.obj;
					if(mResponsePublicModel!=null&&mResponsePublicModel.isSuccess()){
					}
					break;
					
				case NewPaymentDataUtils.CONFIRM_TRANSFER:
					dismessProgress();
					insertTransferresponse = (InsertTransferResponse)msg.obj;
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
						}else if(insertTransferresponse!=null&&insertTransferresponse.isSuccess()){
							
						}
					showResult();
					break;
				case NewPaymentDataUtils.LOAD_RECENPAYMENT:
					BperRecentTransferResponseModel mBperRecentTransferResponseModel= (BperRecentTransferResponseModel) msg.obj;
					if(mBperRecentTransferResponseModel!=null){
						mBperPaymentBankTransferStep1.setRecentTransferModels(mBperRecentTransferResponseModel.getRecentBankTransferList(),mOnSlideItemClickListener);
					}
					dismessProgress();
					if(mBperRecentTransferResponseModel==null||!mBperRecentTransferResponseModel.isSuccess()){
						DialogManager.displayErrorMessage(mBperRecentTransferResponseModel==null?null:mBperRecentTransferResponseModel.getEventManagement(),-1, context).show();
					}
					break;
				default:
					break;
				}
			}
		};
		
		int PHONE_BOOK_RESULT=10;
		public void showPhoneBook(){
			PhoneBookActivity.start(((Activity)context),PHONE_BOOK_RESULT,TransferType.BANK_TRANSFER);
		}
		
		public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode==Activity.RESULT_OK){
				if(requestCode==PHONE_BOOK_RESULT){//phonebook
					mBankRecipient=(BankRecipient) data.getSerializableExtra("item");
					mBperPaymentBankTransferStep1.setBeneficiaryName(mBankRecipient.getName());
					mBperPaymentBankTransferStep1.setBeneficiaryIban(mBankRecipient.getIbanCode());
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
