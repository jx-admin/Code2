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
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.BperRecentTransferResponseModel;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.view.adapter.CardRecipientAdapter;
import com.accenture.mbank.view.payment.BPERPaymentInputPage.OnDoneListener;

public class BPERPaymentCardTopUp extends com.accenture.mbank.view.payment.BPERPayment  implements OnTabChangeListener {
	
	//validate
	CheckRechargeCardResponseModel checkRechargeCard;
	TransferObjectCard mRecentTransferModel;
	//phonebook recipient
	CardRecipient mBankRecipient;
	
	BPERPaymentCardTopUpStep1 mBperPaymentBankTransferStep1;
	
	public enum TYPE {  
		RECENT,RECENT_DETAIL,NEW_PAYMENT,CONFIRM,RESULT,DETAIL  
	} 
	
	private TYPE type;
	private boolean isforcedCell = false;
	
	InfoCardsModel tmp_InfoCardsModel;
	
	TransferType paymentType=TransferType.CARD_TOP_UP;
	
	public BPERPaymentCardTopUp(Context context,TransferType mTransferType) {
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
		mBperPaymentBankTransferStep1=new BPERPaymentCardTopUpStep1(context,mTransferType);
		mBperPaymentBankTransferStep1.setDoneListener(new OnDoneListener(){
			
			@Override
			public void onConfirm(Object o) {
				vertifyCard();
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
				mBperPaymentBankTransferStep1.onDisable(arg0);
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
		mBPERPaymentConfirmPage.showCardTopUp(mBperPaymentBankTransferStep1.getAccountsModel(),  mBperPaymentBankTransferStep1.getBeneficiary(),mBperPaymentBankTransferStep1.getCardNumber(),mBperPaymentBankTransferStep1.getAmount(),mBperPaymentBankTransferStep1.getDescription(),checkRechargeCard.getCharges(),TransferType.TITLE_NEWPAYMENT);
		((BPERPaymentMenu)context).setContentView(mBPERPaymentConfirmPage.getContentView());
		type=TYPE.CONFIRM;
	}
	
	private void insertRecipient(){
		CardRecipient mCardRecipient=new CardRecipient();
		mCardRecipient.setName(mBperPaymentBankTransferStep1.getBeneficiary());
		mCardRecipient.setCardNumber(mBperPaymentBankTransferStep1.getCardNumber());
		NewPaymentDataUtils.insertRecipient(context, loadDateHandler, NewPaymentDataUtils.INSERT_RECIPIENT, InsertRecipientJson.CARD, mCardRecipient);
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
		mBPERPaymentDetailsUtil.showCardTopUp(mBperPaymentBankTransferStep1.getAccountsModel(), mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getCardNumber(), mBperPaymentBankTransferStep1.getAmount(), mBperPaymentBankTransferStep1.getDescription(), checkRechargeCard.getCharges());
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
			askPin(checkRechargeCard.getOtpByEmail());
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
			mRecentTransferModel=(TransferObjectCard) mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentRecoverPage();
		}

		@Override
		public void onItemClick(View view, int position) {
			mRecentTransferModel=(TransferObjectCard) mBperPaymentBankTransferStep1.getRecentTransferModel(position);
			showRecentDetailsPage();
		}
		
	};
	
	OnClickListener mOnRecentDetailConfirmClickListener=new OnClickListener(){
		
		@Override
		public void onClick(View arg0) {
			showRecentRecoverPage();
			((BPERPaymentMenu)context).showPop();
		}};
		
		private void vertifyCard(){
			showProgress();
			NewPaymentDataUtils.vertifyCard(context, loadDateHandler, NewPaymentDataUtils.VERTIFY_CARD, mBperPaymentBankTransferStep1.getAccountsModel().getAccountCode(),  mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getLastCardDigits(),mBperPaymentBankTransferStep1.getDate());
		}
		
		private void validate(){
			String transferId ="";// newPayment && mPendingTransferModel != null ? mPendingTransferModel .getTransferId() : "";
			NewPaymentDataUtils.validateCard(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_CARD_TOP_UP, getPayer().getAccountCode(), tmp_InfoCardsModel.getCardHash(), tmp_InfoCardsModel.getCardNumberMask(), tmp_InfoCardsModel.getTitle(), mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getDescription(), mBperPaymentBankTransferStep1.getAmount(), transferId);
		}
		
		private void askPin(boolean otpMail){
			showProgress();
			
			if(!otpMail){
				isforcedCell = true;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.CARD_TOP_UP.getJsonItType(),mBperPaymentBankTransferStep1.getBeneficiary(),mBperPaymentBankTransferStep1.getAmount(), "false");
			} else {
				isforcedCell = false;
				NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,TransferType.CARD_TOP_UP.getJsonItType(),mBperPaymentBankTransferStep1.getBeneficiary(),mBperPaymentBankTransferStep1.getAmount(), null);
			}
			
		}
		
		private void confirm(){
			showProgress();
			NewPaymentDataUtils.confirmCardRecharge(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_CARD_TOP_UP, mBperPaymentBankTransferStep1.getAccountsModel().getAccountCode(), tmp_InfoCardsModel,mBperPaymentBankTransferStep1.getCardNumber(),mBperPaymentBankTransferStep1.getBeneficiary(), mBperPaymentBankTransferStep1.getDescription(), mBperPaymentBankTransferStep1.getAmount(), mBPERPaymentConfirmPage.getPinCode(), getGenerateOTPResponseModel().getOtpKeySession(), checkRechargeCard);
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
				showProgress();
				NewPaymentDataUtils.getTablesResponseModel(context,loadDateHandler, NewPaymentDataUtils.LOAD_SEPA_COUNTRY_IBAN,"SEPA_COUNTRY_IBAN");
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
								NewPaymentDataUtils.getRecipientList(context,loadDateHandler,NewPaymentDataUtils.getRecipientList,TransferType.CARD_TOP_UP);
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
					if(mGetRecipientListModel!=null){
						bankRecipientList=mGetRecipientListModel.getBankRecipientList();
						cardRecipientList=mGetRecipientListModel.getCardRecipientList();
						phoneRecipientList=mGetRecipientListModel.getPhoneRecipientList();
						
						mBperPaymentBankTransferStep1.setAdapter(new CardRecipientAdapter(context, R.layout.phonebook_item, cardRecipientList));
					}
					invaliDate();
					NewPaymentDataUtils.getIbanPrepaidCard(context,loadDateHandler,NewPaymentDataUtils.GET_IBAN_PREPAID_CARD);
					break;
				case NewPaymentDataUtils.GET_IBAN_PREPAID_CARD:
					if(msg.obj!=null){
						mBperPaymentBankTransferStep1.setAccounts((List<AccountsModel>)msg.obj);
					}
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
				case NewPaymentDataUtils.VERTIFY_CARD:
					GetCardsResponseModel getCards = (GetCardsResponseModel)msg.obj;
					tmp_InfoCardsModel=null;
					dismessProgress();
					if (getCards!=null&&getCards.responsePublicModel!=null&&getCards.responsePublicModel.isSuccess()) {
						List<InfoCardsModel> list = getCards.getInfoCardListModel();
						if (list != null && list.size() > 0) {//在这里判断如果size大于0 再填加一个生日
							if(list.size() > 1){
								DialogManager.createMessageDialog(R.string.card_top_up_return_more_value_msg, context).show();
								mBperPaymentBankTransferStep1.birth_date_container.setVisibility(View.VISIBLE);
							}else{
								tmp_InfoCardsModel = list.get(0);
								validate();	
							}
						} 
					}else{
						EventManagement eventManagement=null;
						if (getCards!=null&&getCards.responsePublicModel!=null){
							eventManagement=getCards.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(eventManagement,R.string.bank_transfer_other_error, context,R.string.as_cancel,-1).show();
					}
					break;
				case NewPaymentDataUtils.VALIDATE_CARD_TOP_UP:
					checkRechargeCard = (CheckRechargeCardResponseModel)msg.obj;
						dismessProgress();
						String errorCode=null;
						if(checkRechargeCard.responsePublicModel.eventManagement!=null){
							errorCode=checkRechargeCard.responsePublicModel.eventManagement.getErrorCode();
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
							if(BaseActivity.isOffline||"020".equals(bankServiceCode)){
								DialogManager.createMessageDialog(R.string.error_code_91083_020,"", context,R.string.as_cancel,-1,null,null).show();
							}else if("887".equals(bankServiceCode)){
								DialogManager.createMessageDialog(R.string.error_code_91083_887,"", context,R.string.as_cancel,-1,null,null).show();
							}else{
								DialogManager.createMessageDialog(R.string.bank_transfer_other_error,"", context,R.string.as_cancel,-1,null,null).show();
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
							if("020".equals(bankServiceCode)){
								msgId=R.string.error_code_91083_020;
							}else if("887".equals(bankServiceCode)){
								msgId=R.string.error_code_91083_887;
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
						} else if("91085".equals(errorCode)){
							DialogManager.createMessageDialog(R.string.error_code_91085,"", context,R.string.as_cancel,-1,null,null).show();
						}else if("91087".equals(errorCode)){
							DialogManager.createMessageDialog(R.string.error_code_91087,"", context,R.string.as_cancel,-1,null,null).show();
						}else if (checkRechargeCard!=null&&checkRechargeCard.responsePublicModel!=null&&checkRechargeCard.responsePublicModel.isSuccess()) {
							showConfirm();//displayPaymentRecap();
							if(mBperPaymentBankTransferStep1.isAddPhoneBook()){
								insertRecipient();
							}else{
								dismessProgress();
							}
						} else {
							EventManagement mEventManagement=null;
							if(checkRechargeCard!=null&&checkRechargeCard.responsePublicModel!=null){
								mEventManagement=checkRechargeCard.responsePublicModel.eventManagement;
							}
							DialogManager.displayErrorMessage(mEventManagement,-1, context,R.string.as_cancel,-1).show();
						}
					break;
					
				case NewPaymentDataUtils.CONFIRM_CARD_TOP_UP:
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
						mBperPaymentBankTransferStep1.setRecentTransferModels(mBperRecentTransferResponseModel.getRecentPrepaidCardRechargeList(),mOnSlideItemClickListener);
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
			PhoneBookActivity.start(((Activity)context),PHONE_BOOK_RESULT,paymentType);
		}
		
		public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode==Activity.RESULT_OK){
				if(requestCode==PHONE_BOOK_RESULT){//phonebook
					mBankRecipient=(CardRecipient) data.getSerializableExtra("item");
					mBperPaymentBankTransferStep1.setBeneficiaryName(mBankRecipient.getName());
					mBperPaymentBankTransferStep1.setBeneficiaryIban(mBankRecipient.getLast4Digits());
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
