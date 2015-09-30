package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.accenture.mbank.logic.InsertTransferResponse;
import com.accenture.mbank.logic.OtpState;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.CheckSimTopUpResponseModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.CompanyAmountResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.view.payment.BPERPaymentConfirmPage;
import com.accenture.mbank.view.payment.BPERPaymentDetailsUtil;
import com.accenture.mbank.view.payment.BPERPaymentRecoverPage;
import com.accenture.mbank.view.payment.BPERPaymentRecoverPhonePage;
import com.accenture.mbank.view.payment.BPERPaymentResultUtil;
/**recent payment
 * @author junxu.wang
 *
 */
public class BPERPayment  extends MenuActivity {
	public static final String PARAM_TYPE="type";
	private static final String PARAM_NEWPAYMENT="newPayment";
	public static final String PARAM_AccountsModel="AccountsModel";
	public static final String PARAM_RecentTransferModel="RecentTransferModel";
	public enum TYPE {  
		STEP1,STEP2,STEP3,RECOVER,CONFIRM,RESULT,DETAIL  
	} 
	int isNewPayment=TransferType.TITLE_RECENTPAYMENT;
	
	// Check transaction requast
	private CheckTransferResponseModel checkTransfer;
	
	private CheckSimTopUpResponseModel checkSimTopUp;
	
	private CheckRechargeCardResponseModel checkRechargeCard;
	//
	
	//ask pin
	GenerateOTPResponseModel generateOtp;
	
	//confirm 
	InsertTransferResponse insertTransferresponse;
	
	
	//?? 
	private boolean isCardVerfy;
	private InfoCardsModel tmp_InfoCardsModel;
	
	
	AccountsModel mAccountsModel;
	TransferObject mRecentTransferModel;
	TransferType paymentType;
//	Object curPayee;
	
	Context context;
	private TYPE type;
	BPERPaymentRecoverPage mBPERPaymentRecoverPage;
	BPERPaymentRecoverPhonePage mBPERPaymentRecoverPhonePage;
	BPERPaymentConfirmPage mBPERPaymentConfirmPage;
	BPERPaymentResultUtil mBPERPaymentResultUtil;
	BPERPaymentDetailsUtil mBPERPaymentDetailsUtil;
	public static void startForPaymentDetail(Context context,TYPE type,AccountsModel mAccountsModel, TransferObject mRecentTransferModel,int newPayment){
		Intent intent=new Intent(context,BPERPayment.class);
		intent.putExtra(PARAM_TYPE, type);
		intent.putExtra(PARAM_NEWPAYMENT, newPayment);
		intent.putExtra(PARAM_AccountsModel, mAccountsModel);
		intent.putExtra(PARAM_RecentTransferModel, mRecentTransferModel);
		context.startActivity(intent);
	}
	
	
	/**
	 * @see PaymentsManager#setRecover
	 */
	public static void startForRecover(Activity activity, TransferObject recentTransferModel,AccountsModel accountModel,int newPayment) {
		startForPaymentDetail(activity,TYPE.RECOVER,accountModel,recentTransferModel,newPayment);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		Intent intent=getIntent();
		type=(TYPE)intent.getSerializableExtra(PARAM_TYPE);
		isNewPayment=intent.getIntExtra(PARAM_NEWPAYMENT, TransferType.TITLE_RECENTPAYMENT);
		switch(type){
		case RECOVER:
			onRecover((AccountsModel) intent.getSerializableExtra(PARAM_AccountsModel),(TransferObject) intent.getSerializableExtra(PARAM_RecentTransferModel));
			
			mBPERPaymentRecoverPage=new BPERPaymentRecoverPage(this);
			mBPERPaymentRecoverPage.setConfirmClickListener(mOnRecoverConfirmclickListener);
			
			mBPERPaymentRecoverPhonePage=new BPERPaymentRecoverPhonePage(this);
			mBPERPaymentRecoverPhonePage.setConfirmClickListener(mOnRecoverConfirmclickListener);
			
			showRecentRecover();
			break;
		case CONFIRM:
			mBPERPaymentConfirmPage=new BPERPaymentConfirmPage(this);
			setContentView(mBPERPaymentConfirmPage.getContentView());
			break;
		case RESULT:
			mBPERPaymentResultUtil=new BPERPaymentResultUtil(this, TransferType.TRANSFER_ENTRY);
			mBPERPaymentResultUtil.setResult(false,isNewPayment);
			setContentView(mBPERPaymentResultUtil.getContentView());
			break;
		case DETAIL:
			mBPERPaymentDetailsUtil=new BPERPaymentDetailsUtil(this);
			mBPERPaymentDetailsUtil.setOnbackClickListener(new OnViewClickListener() {
				
				@Override
				public void onClick(View v) {
					super.onClick(v);
					// TODO Auto-generated method stub
					
				}
			});
			setContentView(mBPERPaymentDetailsUtil.getContentView());
			break;
		}
	}
	
	OnViewClickListener mOnRecoverConfirmclickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			validate();
		}
	};
	
	OnViewClickListener onAskPinClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			askPin();
		}
	};
	
	private void askPin(){
		showProgress();
		String beneficiary=null;
		if(mRecentTransferModel instanceof TransferObjectSim){
			beneficiary=((TransferObjectSim) mRecentTransferModel).getBeneficiaryNumber();
		}else{
			beneficiary=mRecentTransferModel.getBeneficiaryName();
		}
		NewPaymentDataUtils.askPin(context, loadDateHandler, NewPaymentDataUtils.GET_ASKPIN,paymentType.getJsonItType(),beneficiary,mBPERPaymentRecoverPage.getAmount(), null);
	}
	
	OnViewClickListener mOnConfirmClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			
			showProgress(); 
			confirm();
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
			Intent intent=new Intent();
			intent.setClass(BPERPayment.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//加上Flag
			startActivity(intent);
			final MainActivity mainActivity = (MainActivity)MainActivity.getContext();
			mainActivity.showTab(0);
			finish();
		}
	};
	
	OnViewClickListener mOnNewPaymentClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			if(isNewPayment!=TransferType.TITLE_RECENTPAYMENT){
				BPERPayment.this.finish();
				BPERPaymentMenu.mBPERPaymentMenu.showPop();
			}else{
				showRecentRecover();
			}
		}
	};
	
	public void onBackPressed() {
		if(type!=null){
			switch(type){
			case RECOVER:
				super.onBackPressed();
				break;
			case CONFIRM:
				showRecentRecover();
				break;
			case RESULT:
				if(insertTransferresponse!=null&&insertTransferresponse.isSuccess()){
					showRecentRecover();
				}else{
					showConfirm();
				}
				break;
			case DETAIL:
				showResult();
				break;
			}
		}
		
	};
	
	private void onRecover(AccountsModel accountsModel, TransferObject recentTransferModel){
		this.mAccountsModel=accountsModel;
		this.mRecentTransferModel=recentTransferModel;
		String type=mRecentTransferModel.getType();
		if (Contants.SIM_TOP_UP.equals(type)) {
			paymentType = TransferType.PHONE_TOP_UP;
		} else if (Contants.TRANSFER_ENTRY.equals(type)) {
			paymentType = TransferType.TRANSFER_ENTRY;
		} else if (Contants.BANK_TRANSFER.equals(type)) {
			paymentType = TransferType.BANK_TRANSFER;
		} else if (Contants.PREPAID_CARD_RELOAD.equals(type)) {
			paymentType = TransferType.CARD_TOP_UP;
		}
	}
	
	private void showRecentRecover(){
		switch (paymentType) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			mBPERPaymentRecoverPage.show(mAccountsModel, mRecentTransferModel,isNewPayment);
			setContentView(mBPERPaymentRecoverPage.getContentView());
			break;
		case CARD_TOP_UP:
			mBPERPaymentRecoverPage.show(mAccountsModel, mRecentTransferModel,isNewPayment);
			setContentView(mBPERPaymentRecoverPage.getContentView());
			break;
		case PHONE_TOP_UP:
			showProgress();
			NewPaymentDataUtils.getAmountAvailableLs(context, loadDateHandler, NewPaymentDataUtils.GET_AMOUNT_AVAILABLE, mAccountsModel.getAccountCode(), ((TransferObjectSim)mRecentTransferModel).getBeneficiaryProviderCode(),((TransferObjectSim)mRecentTransferModel).getBeneficiaryNumber());
			mBPERPaymentRecoverPhonePage.show(mAccountsModel, mRecentTransferModel,isNewPayment);
			setContentView(mBPERPaymentRecoverPhonePage.getContentView());
			break;
		}
		type=TYPE.RECOVER;
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
	
	private Handler loadDateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewPaymentDataUtils.GET_ASKPIN:
				generateOtp = (GenerateOTPResponseModel)msg.obj;
				dismessProgress();
				if(generateOtp!=null&&generateOtp.responsePublicModel!=null&&generateOtp.responsePublicModel.isSuccess()){
					if (BaseActivity.isOffline||MainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
						Toast.makeText(context, R.string.pin_success_by_email, Toast.LENGTH_LONG).show();
					} else if (MainActivity.setting.getChannelToRecelvePin() == SettingModel.SMS) {
						Toast.makeText(context, R.string.pin_success_by_sms, Toast.LENGTH_LONG) .show();
					}
				}else{
					mBPERPaymentConfirmPage.hiddPin();
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

				String errorCode=null;
				if(checkTransfer.responsePublicModel!=null&&checkTransfer.responsePublicModel.eventManagement!=null){
					errorCode=checkTransfer.responsePublicModel.eventManagement .getErrorCode();
				}
				if("90".equals(errorCode)){
					DialogManager.createMessageDialog(0, checkTransfer.responsePublicModel.eventManagement.getErrorDescription(), context,  R.string.as_cancel, R.string.as_proced,new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showConfirm();
						}
					}).show();
				}else if("91".equals(errorCode)){
					DialogManager.createMessageDialog(0, checkTransfer.responsePublicModel.eventManagement.getErrorDescription(), context,  R.string.as_cancel, R.string.as_proced,new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String bankServiceCode=null;
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
				}else{
					EventManagement mEventManagement=null;
					if(checkTransfer!=null&&checkTransfer.responsePublicModel!=null){
						mEventManagement=checkTransfer.responsePublicModel.eventManagement;
					}
					DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
				}
				break;
			case NewPaymentDataUtils.CONFIRM_TRANSFER:
			case NewPaymentDataUtils.CONFIRM_PHONE_TOP_UP:
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
					}else{
//						com.accenture.mbank.logic.EventManagement mEventManagement=null;
//						if(insertTransferresponse!=null){
//							mEventManagement=insertTransferresponse.getEventManagement();
//						}
//						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
				}
				showResult();
				break;
			case NewPaymentDataUtils.VERTIFY_CARD:
				GetCardsResponseModel getCards = (GetCardsResponseModel)msg.obj;
				tmp_InfoCardsModel=null;
				isCardVerfy=false;
				if (getCards!=null&&getCards.responsePublicModel!=null&&getCards.responsePublicModel.isSuccess()) {
					List<InfoCardsModel> list = getCards.getInfoCardListModel();
					if (list != null && list.size() > 0) {
						tmp_InfoCardsModel = list.get(0);
						isCardVerfy=true;
						validate();
					} 
				}else {
					dismessProgress();
					EventManagement mEventManagement=null;
					if(getCards!=null&&getCards.responsePublicModel!=null){
						mEventManagement=getCards.responsePublicModel.eventManagement;
					}
					DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
				}
				break;
			case NewPaymentDataUtils.VALIDATE_CARD_TOP_UP:
				checkRechargeCard = (CheckRechargeCardResponseModel)msg.obj;
					dismessProgress();
					errorCode=null;
					if(checkRechargeCard!=null&&checkRechargeCard.responsePublicModel.eventManagement!=null){
						errorCode=checkRechargeCard.responsePublicModel.eventManagement.getErrorCode();
					}
					if("91068".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.bank_transf_91083, context).show();
					} else if("91083".equals(errorCode)){
						String bankServiceCode=null;
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
					}else if("91082".equals(errorCode)){
						String bankServiceCode=null;
						if(mAccountsModel!=null){
							BankServiceType mBankServiceType=mAccountsModel.getBankServiceType();
							if(mBankServiceType!=null){
								bankServiceCode=mBankServiceType.getBankServiceCode();
							}
							
						}
						int msgId=R.string.bank_transfer_other_error;
						if(BaseActivity.isOffline||"020".equals(bankServiceCode)){
							msgId=R.string.error_code_91083_020;
						}else if(BaseActivity.isOffline||"887".equals(bankServiceCode)){
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
						DialogManager.createMessageDialog(R.string.error_code_91085, context).show();
					}else if("91087".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.error_code_91087, context).show();
					}else if (checkRechargeCard!=null&&checkRechargeCard.responsePublicModel!=null&&checkRechargeCard.responsePublicModel.isSuccess()) {
						showConfirm();//displayPaymentRecap();
					} else {
						EventManagement mEventManagement=null;
						if(checkRechargeCard!=null&&checkRechargeCard.responsePublicModel!=null){
							mEventManagement=checkRechargeCard.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
				break;
			case NewPaymentDataUtils.VALIDATE_SIM_TOP_UP:
				dismessProgress();
				checkSimTopUp = (CheckSimTopUpResponseModel)msg.obj;
					errorCode=null;
					if(checkSimTopUp.responsePublicModel.eventManagement!=null){
						errorCode=checkSimTopUp.responsePublicModel.eventManagement.getErrorCode();
					}
					if("91068".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.bank_transf_91083, context).show();
					}else if("91083".equals(errorCode)){
						String bankServiceCode=null;
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
					}else if (checkSimTopUp!=null&&checkSimTopUp.responsePublicModel.isSuccess()) {
						showConfirm();//displayPaymentRecap();
					} else {
						EventManagement mEventManagement=null;
						if(checkSimTopUp!=null&&checkSimTopUp.responsePublicModel!=null){
							mEventManagement=checkSimTopUp.responsePublicModel.eventManagement;
						}
						DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
					}
				break;
			case NewPaymentDataUtils.GET_AMOUNT_AVAILABLE:
				dismessProgress();
				CompanyAmountResponseModel companyAmount = (CompanyAmountResponseModel)msg.obj;
				List<AmountAvailable> mAmountAvailableLs =null;
					EventManagement eventManagement=null;
					errorCode=null;
					if(companyAmount!=null&&companyAmount.responsePublicModel!=null){
						eventManagement=companyAmount.responsePublicModel.eventManagement;
						if(eventManagement!=null){
							errorCode=eventManagement.getErrorCode();
						}
					}
					if("91050".equals(errorCode)||"91051".equals(errorCode)){
						DialogManager.createMessageDialog(R.string.error_code_91050_91051, context).show();
						return;
					}else if (companyAmount!=null&&companyAmount.responsePublicModel!=null&&companyAmount.responsePublicModel.isSuccess()) {
						mAmountAvailableLs = companyAmount.getAmountAvailable();
						if (mAmountAvailableLs != null&& mAmountAvailableLs.size() > 0) {
							mBPERPaymentRecoverPhonePage.setAmountAvailableLs(mAmountAvailableLs);
						}
					}else{
						DialogManager.displayErrorMessage(eventManagement, 0, context).show();
					}
				break;
			}
		}
	};
	
	
	DestaccountModel mDestaccountModel;
	/**
	 * Check transaction
	 * 
	 * @return
	 */
	private void validate() {
		if(mRecentTransferModel instanceof TransferObjectTransfer){
			showProgress();
			checkTransfer = null;
			TransferObjectTransfer mTransferObjectTransfer=(TransferObjectTransfer) mRecentTransferModel;
			mDestaccountModel=new DestaccountModel();
			mDestaccountModel.setTitle(mTransferObjectTransfer.getBeneficiaryName());
			mDestaccountModel.setState(mTransferObjectTransfer.getBeneficiaryIban()==null?"":mTransferObjectTransfer.getBeneficiaryIban().substring(0, 2));
			mDestaccountModel.setIban(mTransferObjectTransfer.getBeneficiaryIban());
			mDestaccountModel.setBic(mTransferObjectTransfer.getBeneficiaryBic());
			NewPaymentDataUtils.validateBankTransfer(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_TRANSFER, true, mAccountsModel.getAccountCode(),mTransferObjectTransfer.getPurposeCurrency(),mDestaccountModel, mBPERPaymentRecoverPage.getAmount(), mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getData(),null);
			
		}else if(mRecentTransferModel instanceof TransferObjectEntry){
			TransferObjectEntry mTransferObjectTransfer=(TransferObjectEntry) mRecentTransferModel;
			showProgress();
			checkTransfer = null;
			mDestaccountModel=new DestaccountModel();
			mDestaccountModel.setTitle(mTransferObjectTransfer.getBeneficiaryName());
			mDestaccountModel.setState(mTransferObjectTransfer.getBaneficiaryState());
			mDestaccountModel.setIban(mTransferObjectTransfer.getBeneficiaryIban());
			mDestaccountModel.setBic(mTransferObjectTransfer.getBeneficiaryBic());
			NewPaymentDataUtils.validateBankTransfer(this, loadDateHandler, NewPaymentDataUtils.VALIDATE_TRANSFER, true, mAccountsModel.getAccountCode(),null, mDestaccountModel,mBPERPaymentRecoverPage.getAmount(), mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getData(),null);
			
		}else if(mRecentTransferModel instanceof TransferObjectCard){
			showProgress();
			TransferObjectCard mTransferObjectSim=(TransferObjectCard) mRecentTransferModel;
			if(isCardVerfy){
				NewPaymentDataUtils.validateCard(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_CARD_TOP_UP, mAccountsModel.getAccountCode(), tmp_InfoCardsModel.getCardHash(), tmp_InfoCardsModel.getCardNumberMask(), tmp_InfoCardsModel.getTitle(), mTransferObjectSim.getBeneficiaryName(), mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getAmount(), "");
			}else{
				vertifyCard(mTransferObjectSim);
			}
		}else if(mRecentTransferModel instanceof TransferObjectSim){
			showProgress();
			TransferObjectSim mTransferObjectSim=(TransferObjectSim) mRecentTransferModel;
			NewPaymentDataUtils.validateSimTopUp(context, loadDateHandler, NewPaymentDataUtils.VALIDATE_SIM_TOP_UP, mAccountsModel.getAccountCode(),mTransferObjectSim.getBeneficiaryProviderCode(),mTransferObjectSim.getBeneficiaryNumber(), mBPERPaymentRecoverPhonePage.getAmount(), "");
			
		}
	}
	
	private void vertifyCard(TransferObjectCard mTransferObjectSim){
		NewPaymentDataUtils.vertifyCard(context, loadDateHandler, NewPaymentDataUtils.VERTIFY_CARD, mAccountsModel.getAccountCode(),mTransferObjectSim.getBeneficiaryName(), mTransferObjectSim.getLast4Digits(),/*mTransferObjectSim.getDate()*/0);
	}
	
	private void confirm(){
		if(mRecentTransferModel instanceof TransferObjectTransfer){
			TransferObjectTransfer mTransferObjectTransfer=(TransferObjectTransfer) mRecentTransferModel;
			NewPaymentDataUtils.confirmBankTransfer(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_TRANSFER, mAccountsModel.getAccountCode(), mBPERPaymentRecoverPage.getAmount(),null, mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getData(), mDestaccountModel, mBPERPaymentConfirmPage.getPinCode(), generateOtp.getOtpKeySession(), checkTransfer, true);
		}else
			if(mRecentTransferModel instanceof TransferObjectEntry){
				TransferObjectEntry mTransferObjectEntry=(TransferObjectEntry) mRecentTransferModel;
				NewPaymentDataUtils.confirmBankTransfer(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_TRANSFER, mAccountsModel.getAccountCode(), mBPERPaymentRecoverPage.getAmount(),null, mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getData(),mDestaccountModel, mBPERPaymentConfirmPage.getPinCode(), generateOtp.getOtpKeySession(), checkTransfer, true);
				
			}else
				if(mRecentTransferModel instanceof TransferObjectSim){
					TransferObjectSim mTransferObjectSim=(TransferObjectSim) mRecentTransferModel;
					NewPaymentDataUtils.confirmTranslationSimTopUp(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_PHONE_TOP_UP, mAccountsModel.getAccountCode(),mTransferObjectSim.getBeneficiaryProviderCode(),mTransferObjectSim.getBeneficiaryNumber(), mBPERPaymentConfirmPage.getPinCode(),generateOtp.getOtpKeySession(), mBPERPaymentRecoverPhonePage.getAmount(), checkSimTopUp.getTransferId());
				}else if(mRecentTransferModel instanceof TransferObjectCard){
					TransferObjectCard mTransferObjectCard=(TransferObjectCard) mRecentTransferModel;
					NewPaymentDataUtils.confirmCardRecharge(context, loadDateHandler, NewPaymentDataUtils.CONFIRM_CARD_TOP_UP, mAccountsModel.getAccountCode(), tmp_InfoCardsModel, mTransferObjectCard.getBeneficiaryCardNumber(),mTransferObjectCard.getBeneficiaryName(), mBPERPaymentRecoverPage.getDescription(), mBPERPaymentRecoverPage.getAmount(), mBPERPaymentConfirmPage.getPinCode(), generateOtp.getOtpKeySession(), checkRechargeCard);
					
				}
	}
	
	private void showConfirm(){
		if(mBPERPaymentConfirmPage==null){
			mBPERPaymentConfirmPage=new BPERPaymentConfirmPage(this);
			mBPERPaymentConfirmPage.setOnAskPinClickListener(onAskPinClickListener);
			mBPERPaymentConfirmPage.setOnConfirmClickListener(mOnConfirmClickListener);
		}
		switch (paymentType) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			mBPERPaymentConfirmPage.show(mAccountsModel, mRecentTransferModel,mBPERPaymentRecoverPage.getAmount(),mBPERPaymentRecoverPage.getDescription(),checkTransfer.getCharges(),mBPERPaymentRecoverPage.getData(),isNewPayment);
			break;
		case CARD_TOP_UP:
			mBPERPaymentConfirmPage.show(mAccountsModel, mRecentTransferModel,mBPERPaymentRecoverPage.getAmount(),mBPERPaymentRecoverPage.getDescription(),checkRechargeCard.getCharges(),0,isNewPayment);
			break;
		case PHONE_TOP_UP:
			double amount=0;
			if(mBPERPaymentRecoverPhonePage.getAmount()!=null){
				amount=mBPERPaymentRecoverPhonePage.getAmount().getRechargeAmount();
			}
			mBPERPaymentConfirmPage.show(mAccountsModel,mRecentTransferModel,amount,null,0,0 ,isNewPayment);
			break;
		}
		setContentView(mBPERPaymentConfirmPage.getContentView());
		type=TYPE.CONFIRM;
	}
	
	private void showResult(){
		if(mBPERPaymentResultUtil==null){
			mBPERPaymentResultUtil=new BPERPaymentResultUtil(this,paymentType);
			mBPERPaymentResultUtil.setOnDetailClickListener(mOnDetailClickListener);
			mBPERPaymentResultUtil.setOnGoSyntesisClickListener(mOnGoSyntesisClickListener);
			mBPERPaymentResultUtil.setOnNewPaymentClickListener(mOnNewPaymentClickListener);
		}
		mBPERPaymentResultUtil.setResult(insertTransferresponse!=null&&insertTransferresponse.isSuccess(),isNewPayment);
		setContentView(mBPERPaymentResultUtil.getContentView());
		type=TYPE.RESULT;
	}
	
	private void showDetail(){
		mBPERPaymentDetailsUtil=new BPERPaymentDetailsUtil(this);
		mBPERPaymentDetailsUtil.setOnbackClickListener(new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				onBackPressed();
			}
		});
		
		switch (paymentType) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			mBPERPaymentDetailsUtil.show(mAccountsModel, mRecentTransferModel, mBPERPaymentRecoverPage.getAmount(), mBPERPaymentRecoverPage.getDescription(), checkTransfer.getCharges(), mBPERPaymentRecoverPage.getData());
			break;
		case CARD_TOP_UP:
			mBPERPaymentDetailsUtil.show(mAccountsModel, mRecentTransferModel, mBPERPaymentRecoverPage.getAmount(), mBPERPaymentRecoverPage.getDescription(), checkRechargeCard.getCharges(), mBPERPaymentRecoverPage.getData());
			break;
		case PHONE_TOP_UP:
			AmountAvailable mAmountAvailable=mBPERPaymentRecoverPhonePage.getAmount();
			double amount=mAmountAvailable==null?0:mAmountAvailable.getRechargeAmount();
			mBPERPaymentDetailsUtil.show(mAccountsModel, mRecentTransferModel, amount, null, checkSimTopUp.getCharges(), 0);
			break;
		}
		setContentView(mBPERPaymentDetailsUtil.getContentView());
		type=TYPE.DETAIL;
	}
	

}
