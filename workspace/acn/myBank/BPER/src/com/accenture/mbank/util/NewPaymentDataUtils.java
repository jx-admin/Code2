package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.accenture.mbank.BPERPaymentMenu;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.AccountsByServicesJson;
import com.accenture.mbank.logic.CheckRechargeCardJson;
import com.accenture.mbank.logic.CheckSimTopUpJson;
import com.accenture.mbank.logic.CheckTransferJson;
import com.accenture.mbank.logic.CompanyAmountJson;
import com.accenture.mbank.logic.GenerateOTPJson;
import com.accenture.mbank.logic.GetCardsJson;
import com.accenture.mbank.logic.GetRecipientListJson;
import com.accenture.mbank.logic.GetServiceStatusJson;
import com.accenture.mbank.logic.GetServiceStatusJson.StatusService;
import com.accenture.mbank.logic.InsertRechargeCardJson;
import com.accenture.mbank.logic.InsertRecipientJson;
import com.accenture.mbank.logic.InsertTransferJson;
import com.accenture.mbank.logic.InsertTransferResponse;
import com.accenture.mbank.logic.JsonAbstract;
import com.accenture.mbank.logic.JsonBase;
import com.accenture.mbank.logic.RecentTransferJson;
import com.accenture.mbank.logic.SimTopUpJson;
import com.accenture.mbank.logic.TablesJson;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.BankInformationModel;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.BperRecentTransferResponseModel;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.CheckSimTopUpResponseModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.CompanyAmountResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TableWrapperList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.net.HttpConnector;

public class NewPaymentDataUtils {
	public static final String SEPA_COUNTRY_IBAN="SEPA_COUNTRY_IBAN";

	public static final int getServiceStatus=0;
	public static final int GET_ACCOUNT=1;
	public static final int getRecipientList=2;
	public static final int LOAD_SEPA_COUNTRY_IBAN=3;
	public static final int LOAD_PURPOSE_CURRENCY=4;
	public static final int VALIDATE_TRANSFER=5;
	public static final int GET_ASKPIN=6;
	public static final int CONFIRM_TRANSFER=7;
	public static final int LOAD_RECENPAYMENT=8;
	public static final int GET_AMOUNT_AVAILABLE=9;
	public static final int VALIDATE_SIM_TOP_UP=10;
	public static final int CONFIRM_PHONE_TOP_UP=11;
	public static final int VALIDATE_CARD_TOP_UP=12;
	public static final int VERTIFY_CARD=13;
	public static final int CONFIRM_CARD_TOP_UP=14;
	public static final int INSERT_RECIPIENT=15;
	public static final int GET_IBAN_PREPAID_CARD=16;
	
	public static enum TransferType{BANK_TRANSFER(Contants.BANK_TRANSFER,"Bonifico","003"), TRANSFER_ENTRY(Contants.TRANSFER_ENTRY,"Bonifico verso i miei conti","008"), PHONE_TOP_UP(Contants.SIM_TOP_UP,"Ricarica telefonica","022"), CARD_TOP_UP(Contants.PREPAID_CARD_RELOAD,"Ricarica carta","026");
	String jsonMark;
	String serviceCode;
	String jsonItMark;
	public final static int TITLE_NEWPAYMENT=1;
	public final static int TITLE_NEWPAYMENT_RECENT=2;
	public final static int TITLE_RECENTPAYMENT=3;
//		private TransferType(){};
	private TransferType(String jsonMark,String jsonItMark,String serviceCode){
		this.jsonMark=jsonMark;
		this.jsonItMark=jsonItMark;
		this.serviceCode=serviceCode;
	}
	public String getJsonType(){
		return jsonMark;
	}
	public String getJsonItType(){
		return jsonItMark;
	}
	public String getServiceCode(){
		return serviceCode;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public int getPageTitleId(int type){
		int pageTitleId=-1;
		switch(type){
		case TITLE_NEWPAYMENT:
			switch(TransferType.this){
			case BANK_TRANSFER:
				pageTitleId=R.string.transfer_bank_title;
				break;
			case TRANSFER_ENTRY:
				pageTitleId=R.string.transfer_entry_title;
				break;
			case PHONE_TOP_UP:
				pageTitleId=R.string.transfer_phone_top_up_title;
				break;
			case CARD_TOP_UP:
				pageTitleId=R.string.transfer_card_top_up_title;
				break;
			}
			break;
		case TITLE_NEWPAYMENT_RECENT:
			switch(TransferType.this){
			case BANK_TRANSFER:
				pageTitleId=R.string.transfer_bank_recent_title;
				break;
			case TRANSFER_ENTRY:
				pageTitleId=R.string.transfer_entry_recent_title;
				break;
			case PHONE_TOP_UP:
				pageTitleId=R.string.transfer_phone_top_up_recent_title;
				break;
			case CARD_TOP_UP:
				pageTitleId=R.string.transfer_card_top_up_recent_title;
				break;
			}
			break;
		case TITLE_RECENTPAYMENT:
			switch(TransferType.this){
			case BANK_TRANSFER:
				pageTitleId=R.string.recent_transfer_bank_title;
				break;
			case TRANSFER_ENTRY:
				pageTitleId=R.string.recent_transfer_entry_title;
				break;
			case PHONE_TOP_UP:
				pageTitleId=R.string.recent_transfer_phone_title;
				break;
			case CARD_TOP_UP:
				pageTitleId=R.string.recent_transfer_card_title;
				break;
			}
			break;
		}
		return pageTitleId;
	}
	};
	
	public static class PostThread extends Thread{
		 private final String TAG=PostThread.class.getSimpleName();
		 public final static int TYPE_DEF=-1;
		 
		 Context mContext;
		 
		 String url;
		 
		 Object postData;
		 
		 Handler handler;
		 
		 int requastFlag = 0;
		 
		 Object resultData;
		 
		 int type =0;// TYPE_DEF;
		 
		 public PostThread(Context context, String url, Object postData, Handler handler,
				 int requastFlag, int type) {
			 this.mContext = context;
			 this.url = url;
			 this.postData = postData;
			 this.handler = handler;
			 this.requastFlag = requastFlag;
			 this.type = type;
		 }
		 
		 public PostThread(Context context, String url, Object postData, Handler handler,
				 int requastFlag) {
			 this(context, url, postData, handler, requastFlag, TYPE_DEF);
		 }
		 
		 public void run(){
			 if(postData!=null){
				 LogManager.d( (String)postData);
			 }
			 switch (requastFlag) {
			 
			 default:
				 HttpConnector httpConnector = new HttpConnector();
				 resultData = httpConnector.requestByHttpPost(url, (String)postData, mContext);
				 break;
			 case getServiceStatus:
				 resultData = getServiceStatus(mContext,(String)postData);
				 break;
			 case GET_ACCOUNT:
				 resultData = NewPaymentDataUtils.getAccountsByService__(mContext,(String)postData);
			 break;
			 case getRecipientList:
				 resultData=getRecipientList(mContext,(String)postData);
				 break;
			 case LOAD_SEPA_COUNTRY_IBAN:
			 case LOAD_PURPOSE_CURRENCY:
				 resultData=getTablesResponseModel(mContext,(String)postData);
				 break;
			 case GET_ASKPIN:
				 resultData=askPin(mContext,(String)postData);
				 break;
			 case VALIDATE_TRANSFER:
				 resultData=validateBankTransfer(mContext,(String)postData);
				 break;
			 case CONFIRM_TRANSFER:
				 resultData=confirmBankTransfer(mContext,(String)postData);
				 break;
			 case LOAD_RECENPAYMENT:
				 resultData=getRecentTransferModels(mContext,(String)postData);
				 break;
			 case GET_AMOUNT_AVAILABLE:
				 resultData=getAmountAvailableLs(mContext,(String)postData);
				 break;
			 case VALIDATE_SIM_TOP_UP:
				 resultData=validateSimTopUp(mContext,(String)postData);
				 break;
			 case CONFIRM_PHONE_TOP_UP:
				 resultData=confirmTranslationSimTopUp(mContext,(String)postData);
				 break;
			 case GET_IBAN_PREPAID_CARD:
				 resultData=getIbanPrepaidCard(mContext,(String)postData);
				 break;
			 case VERTIFY_CARD:
				 resultData=vertifyCard(mContext,(String)postData);
				 break;
			 case VALIDATE_CARD_TOP_UP:
				 resultData=validateCard(mContext,(String)postData);
				 break;
			 case CONFIRM_CARD_TOP_UP:
				 resultData=confirmCardRecharge(mContext,(String)postData);
				 break;
			 case INSERT_RECIPIENT:
				 resultData=insertRecipient(mContext,(String)postData);
				 break;
			 }
			 
			 if (handler != null) {
				 Message msg = handler.obtainMessage(requastFlag, resultData);
				 handler.sendMessage(msg);
			 }
		 }
	 } 
	public static void getServiceStatus(Context context,Handler handler, int requastFlag,String abi, String serviceCode){
		String requestJson=GetServiceStatusJson.GetServiceStatusReportProtocal(Contants.publicModel,abi,serviceCode);
		Thread thread=new PostThread(context, Contants.mobile_url, requestJson, handler, requastFlag);
		thread.start();
	}
	public static GetServiceStatusJson getServiceStatus(Context context,String requestJson){
		GetServiceStatusJson getServiceStatus=new GetServiceStatusJson();
		 if(BPERPaymentMenu.isOffline){
			 getServiceStatus.setResultCode(JsonAbstract.SUCCESS_CODE);
			 List<com.accenture.mbank.logic.GetServiceStatusJson.ServiceCode> serviceCodeList=new ArrayList<com.accenture.mbank.logic.GetServiceStatusJson.ServiceCode>();
			 getServiceStatus.setServiceCodeList(serviceCodeList);
			 com.accenture.mbank.logic.GetServiceStatusJson.ServiceCode mServiceCode=new com.accenture.mbank.logic.GetServiceStatusJson.ServiceCode();
			 serviceCodeList.add(mServiceCode);
			 mServiceCode.setServiceCode("serviceCode");
			 List<StatusService> statusServiceList=new ArrayList<GetServiceStatusJson.StatusService>();
			 mServiceCode.setStatusServiceList(statusServiceList);
			 com.accenture.mbank.logic.GetServiceStatusJson.StatusService mStatusService=new  com.accenture.mbank.logic.GetServiceStatusJson.StatusService();
			 mStatusService.setAbi("abi");
			 mStatusService.setActive(true);
			 mStatusService.setMessage("message");
			 mStatusService.setServiceCode("serviceCode");
			 mStatusService.setServiceDescription("serviceDescription");
			 statusServiceList.add(mStatusService);
			 return getServiceStatus;
		 }
		HttpConnector httpConnector = new HttpConnector();
		 String resultJson = httpConnector.requestByHttpPost(Contants.mobile_url, requestJson, context);
		 getServiceStatus.parseJson(resultJson);
		 return getServiceStatus;
	}
	
	public static void getAccountsByService(Context context,Handler handler, int requastFlag,String[] serviceCode){
		List<ServicesModel> services = new ArrayList<ServicesModel>();
		if (serviceCode != null)
			for (int i = serviceCode.length - 1; i >= 0; i--) {
				ServicesModel service = new ServicesModel();
				service.setServiceCode(serviceCode[i]);
				services.add(service);
			}
		String postData = AccountsByServicesJson.AccountsByServicesReportProtocal( Contants.publicModel, services);
		Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
		thread.start();
	}
	public static GetAccountsByServicesResponseModel getAccountsByService__(Context context,String requestJson){
		if(BPERPaymentMenu.isOffline){
			try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GetAccountsByServicesResponseModel mGetAccountsByServicesResponseModel=new GetAccountsByServicesResponseModel();
			mGetAccountsByServicesResponseModel.responsePublicModel=new ResponsePublicModel();
			mGetAccountsByServicesResponseModel.responsePublicModel.setResultCode(JsonBase.SUCCESS_CODE);
			List<AccountsForServiceModel> accountsForServiceModels=new ArrayList<AccountsForServiceModel>();
			AccountsForServiceModel mAccountsForServiceModel=new AccountsForServiceModel();
			accountsForServiceModels.add(mAccountsForServiceModel);
			mGetAccountsByServicesResponseModel.setEffectiveDate(System.currentTimeMillis());
			mAccountsForServiceModel.setServiceCode(" debug serviceCode");
			List<AccountsModel> accountsModels=new ArrayList<AccountsModel>();
			mAccountsForServiceModel.setAccounts(accountsModels);
			for(int i=0;i<3;i++){
				AccountsModel accountsModel=new AccountsModel();
				accountsModels.add(accountsModel);
				accountsModel.setAccountAlias("debug alisa "+i);
				accountsModel.setIbanCode("ibanCodeabcdefghijklmnopqrstxyz"+i);
				accountsModel.setAvailableBalance(123.45+i);
				accountsModel.setAccountBalance(213.45+i*10);
			}
			mGetAccountsByServicesResponseModel.setAccountsForServiceList(accountsForServiceModels);
			
			return mGetAccountsByServicesResponseModel;
		}

    	HttpConnector httpConnector = new HttpConnector();
    	String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, requestJson, context);
			
    	GetAccountsByServicesResponseModel getAccountsByServices = AccountsByServicesJson .ParseGetAccountsByServicesResponse(httpResult);
    	
    	return getAccountsByServices;
	}
	
	public static void getTablesResponseModel(Context context,Handler handler, int requastFlag,String tableName ){
		Thread thread=new PostThread(context, Contants.mobile_url, tableName, handler, requastFlag);
		thread.start();
	}
	
	public static TablesResponseModel getTablesResponseModel(Context context,String tableName){
		if(BaseActivity.isOffline){
			TablesResponseModel mTablesResponseModel=new TablesResponseModel();
			List<TableWrapperList> tablewrapperList=new ArrayList<TableWrapperList>();
			mTablesResponseModel.setTablewrapperList(tablewrapperList);
			for(int i=0;i<3;i++){
				TableWrapperList mTableWrapperList=new TableWrapperList();
				tablewrapperList.add(mTableWrapperList);
				mTableWrapperList.setTableName("debug"+i);
				List<TableContentList> tableContentList=new ArrayList<TableContentList>();
				mTableWrapperList.setTableContentList(tableContentList);
				for(int j=0;j<3;j++){
					TableContentList mTableContentList=new TableContentList();
					tableContentList.add(mTableContentList);
					mTableContentList.setCode("debug"+i);
					mTableContentList.setDescription("debug"+i);;
				}
			}
			return mTablesResponseModel;
		}
		String postData=TablesJson.GetTablesReportProtocal(Contants.publicModel,tableName);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
		return TablesJson.ParseTablesResponse(httpResult);
	}

	
	public static void askPin(Context context,Handler handler, int requastFlag,String operationType,String beneficiary,double amount,String optionalForcePinChannel){
		String otpChannel = Contants.getOtpChannelName();
		if(optionalForcePinChannel != null && !optionalForcePinChannel.equals("")){
			otpChannel = Contants.OTP_CHANNEL_CELL;
		}
        
        String postData = GenerateOTPJson.GenerateOTPReportProtocal(otpChannel, Contants.publicModel,operationType,beneficiary,amount);
		Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
		thread.start();
	}
    public static GenerateOTPResponseModel askPin(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		 GenerateOTPResponseModel generateOtp=new GenerateOTPResponseModel();
    		 generateOtp.setOtpAvailable("debug Available");
    		 generateOtp.setOtpKeySession("otpKeySession");
    		return generateOtp;
    	}
        // String otpChannel = "";
        // if (Contants.ChannelToRecelvePin == SettingModel.EMAIL) {
        // otpChannel = "MAIL";
        // } else if (Contants.ChannelToRecelvePin == SettingModel.SMS) {
        // otpChannel = "SMS";
        // }
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, postData, context);
        GenerateOTPResponseModel generateOtp = GenerateOTPJson.ParseGenerateOTPResponse(httpResult);
        return generateOtp;
    }
    
    
    public static void validateBankTransfer(Context context,Handler handler, int requastFlag,boolean isBankTransfer,String payerAccountCode,String purposecurrency,DestaccountModel mDestaccountModel,double amountInt,String description,long date,String transferId) {
    	
    	String dateString = TimeUtil.getDateString(date, TimeUtil.dateFormat2);
    	//DestaccountModel destAccount = generateDestAccountModel(getPayee());
//    	DestaccountModel mDestaccountModel=new DestaccountModel();
//    	mDestaccountModel.setTitle(title);
//    	mDestaccountModel.setState(state);
//    	mDestaccountModel.setIban(iban);
//    	mDestaccountModel.setBic(bic);
    	
    	String postData = CheckTransferJson.CheckTransferReportProtocal(
    			payerAccountCode, amountInt, purposecurrency, isBankTransfer?"0":"1", description,
    					dateString,mDestaccountModel,Contants.publicModel, transferId);
    	
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }
    
    /**if (getPaymentType() == BANK_TRANSFER) {
    return "0";
	} else if (getPaymentType() == TRANSFER_ENTRY) {
    return "1";
	}
     * @param transferType
     * @param amountInt
     * @param description
     * @param date
     * @param transferId showForm == FORM_PENDING_PAYMENT && mPendingTransferModel != null ? mPendingTransferModel .getTransferId() : "";
     * @return
     */
    public static CheckTransferResponseModel validateBankTransfer(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		CheckTransferResponseModel checkTransfer=new CheckTransferResponseModel();
    		checkTransfer.setTransferId("transferId");
    		checkTransfer.setCharges(2);
    		checkTransfer.setResultCode(JsonAbstract.SUCCESS_CODE);
    		checkTransfer.setResultDescription("resultDescription");
    		BankInformationModel bankInfomation=new BankInformationModel();
    		checkTransfer.setBankInfomation(bankInfomation);
    		bankInfomation.setDestBankName("destBankName");
    		bankInfomation.setDestBranchName("destBranchName");
    		bankInfomation.setSrcBankName("srcBankName");
    		bankInfomation.setSrcBranchName("srcBranchName");
    		checkTransfer.responsePublicModel.eventManagement.setErrorCode("91082");
    		return checkTransfer;
    	}
    	
    	HttpConnector httpConnector = new HttpConnector();
    	String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, postData, context);
    	CheckTransferResponseModel checkTransfer = CheckTransferJson
    			.ParseCheckTransferResponse(httpResult);
    	return checkTransfer;
    }
    


    public static void  confirmBankTransfer(Context context,Handler handler, int requastFlag,String payerAccountCode,double amountInt,String purposeCurrency,String description,long date,DestaccountModel mDestaccountModel,String pin,String otpKeySession,CheckTransferResponseModel checkTransfer,boolean isBankTransferPaymentType) {
        String transferType = isBankTransferPaymentType?"0":"1";
        String dateString = TimeUtil.getDateString(date, TimeUtil.dateFormat2);
        String postData = InsertTransferJson.InsertTransferReportProtocal(Contants.publicModel,
        		payerAccountCode, amountInt, purposeCurrency, transferType, description,
                dateString , mDestaccountModel, pin,otpKeySession /*getGenerateOTPResponseModel().getOtpKeySession()*/,
                Contants.OTP_CHANNEL_MAIL, "USD", checkTransfer);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }

    public static InsertTransferResponse confirmBankTransfer(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		InsertTransferResponse insertTransferresponse = new InsertTransferResponse(TransferType.BANK_TRANSFER);
    		insertTransferresponse.setResultCode(JsonAbstract.SUCCESS_CODE);
    		insertTransferresponse.setResultDescription("resultDescription");
    		insertTransferresponse.setTransactionId("transactionId");
    		return insertTransferresponse;
    	}
    	
    	HttpConnector httpConnector = new HttpConnector();
    	String httpResult = httpConnector
    			.requestByHttpPost(Contants.mobile_url, postData, context);
//    	final ResponsePublicModel insertTransferresponse = InsertTransferJson
//    			.ParseInsertTransferResponse(httpResult);
    	InsertTransferResponse mInsertTransferResponse=new InsertTransferResponse(TransferType.BANK_TRANSFER);
    	mInsertTransferResponse.parseJson(httpResult);
    	return mInsertTransferResponse;
    }
    
    public static void getRecentTransferModels(Context context,Handler handler, int requastFlag,String accountCode){
    	Thread thread=new PostThread(context, Contants.mobile_url, accountCode, handler, requastFlag);
    	thread.start();
    }
    public static BperRecentTransferResponseModel getRecentTransferModels(Context context,String accountCode){
    	if(BaseActivity.isOffline){
    		BperRecentTransferResponseModel mBperRecentTransferResponseModel=new BperRecentTransferResponseModel();
    		mBperRecentTransferResponseModel.setResultCode(JsonBase.SUCCESS_CODE);
    		String typeName[]=new String[]{Contants.BANK_TRANSFER,Contants.TRANSFER_ENTRY,Contants.PREPAID_CARD_RELOAD,Contants.SIM_TOP_UP};
    		List<TransferObject> mTransferObjects=new ArrayList<TransferObject>();
    		List<TransferObjectTransfer> RecentTransferModels=new ArrayList<TransferObjectTransfer>();
    		for(int i=0;i<30;i++){
    			TransferObjectTransfer mRecentTransferModel=new TransferObjectTransfer();
    			RecentTransferModels.add(mRecentTransferModel);
    			mTransferObjects.add(mRecentTransferModel);
    			mRecentTransferModel.setAccount("account");
    			mRecentTransferModel.setAmount(i);
    			mRecentTransferModel.setBeneficiaryIban("beneficiaryIban");
    			mRecentTransferModel.setBeneficiaryName("beneficiaryName");
    			mRecentTransferModel.setBeneficiaryBic("beneficiaryBic");
    			mRecentTransferModel.setType(typeName[0]);
    			mRecentTransferModel.setDescription("description");
    			mRecentTransferModel.setData(System.currentTimeMillis()-i*1000*60*60*24);
    			mRecentTransferModel.setTransferState("transferState");
    			mRecentTransferModel.setBeneficiaryBic("beneficiaryBic");
    			mRecentTransferModel.setBeneficiaryIban("beneficiaryIbanabcdefghijklmnopqrstuvwxyz12345678901234567890");
    			mRecentTransferModel.setBeneficiaryCIG("beneficiaryCIG");
    			mRecentTransferModel.setBeneficiaryCUP("beneficiaryCUP");
    			mRecentTransferModel.setPurposeCurrency("purposeCurrency");
    		
    		}
    		mBperRecentTransferResponseModel.setRecentBankTransferList(RecentTransferModels);
    		
    		List<TransferObjectCard> mTransferObjectCards=new ArrayList<TransferObjectCard>();
    		for(int i=0;i<30;i++){
    			TransferObjectCard mRecentTransferModel=new TransferObjectCard();
    			mTransferObjectCards.add(mRecentTransferModel);
    			mTransferObjects.add(mRecentTransferModel);
    			mRecentTransferModel.setAccount("account");
    			mRecentTransferModel.setAmount(i);
    			mRecentTransferModel.setBeneficiaryName("beneficiaryName");
    			mRecentTransferModel.setBeneficiaryCardNumber("getBeneficiaryCardNumber");
    			mRecentTransferModel.setData(System.currentTimeMillis());
    			mRecentTransferModel.setType(typeName[2]);
    			mRecentTransferModel.setDescription("description");
    			mRecentTransferModel.setTransferState("transferState");
    		}
    		mBperRecentTransferResponseModel.setRecentPrepaidCardRechargeList(mTransferObjectCards);

    		List<TransferObjectSim> mTransferObjectSims=new ArrayList<TransferObjectSim>();
    		for(int i=0;i<30;i++){
    			TransferObjectSim mRecentTransferModel=new TransferObjectSim();
    			mTransferObjectSims.add(mRecentTransferModel);
    			mTransferObjects.add(mRecentTransferModel);
    			mRecentTransferModel.setAccount("account");
    			mRecentTransferModel.setAmount(i);
    			mRecentTransferModel.setBeneficiaryName("beneficiaryName");
    			mRecentTransferModel.setBeneficiaryNumber("getBeneficiaryCardNumber");
    			mRecentTransferModel.setData(System.currentTimeMillis());
    			mRecentTransferModel.setType(typeName[3]);
    			mRecentTransferModel.setDescription("description");
    			mRecentTransferModel.setTransferState("transferState");
    			mRecentTransferModel.setBeneficiaryProvider("beneficiaryProvider");
    		}
    		mBperRecentTransferResponseModel.setRecentSimTopUpList(mTransferObjectSims);
    		
    		List<TransferObjectEntry> mTransferObjectEntrys=new ArrayList<TransferObjectEntry>();
    		for(int i=0;i<30;i++){
    			TransferObjectEntry mRecentTransferModel=new TransferObjectEntry();
    			mTransferObjectEntrys.add(mRecentTransferModel);
    			mTransferObjects.add(mRecentTransferModel);
    			mRecentTransferModel.setAccount("account");
    			mRecentTransferModel.setAmount(i);
    			mRecentTransferModel.setBeneficiaryName("beneficiaryName");
    			mRecentTransferModel.setData(System.currentTimeMillis());
    			mRecentTransferModel.setType(typeName[1]);
    			mRecentTransferModel.setDescription("description");
    			mRecentTransferModel.setTransferState("transferState");
    			mRecentTransferModel.setBeneficiaryBic("beneficiaryBic");
    			mRecentTransferModel.setBeneficiaryIban("beneficiaryIbanabcdefghijklmnopqrstuvwxyz12345678901234567890");
    		}
    		mBperRecentTransferResponseModel.setRecentTransferEntryList(mTransferObjectEntrys);
    		mBperRecentTransferResponseModel.setRecentTransfers(mTransferObjects);

     		Collections.sort(RecentTransferModels,TransferObject.dataComparable);
     		Collections.sort(mTransferObjectEntrys,TransferObject.dataComparable);
     		Collections.sort(mTransferObjectSims,TransferObject.dataComparable);
     		Collections.sort(mTransferObjectCards,TransferObject.dataComparable);
     		Collections.sort(mTransferObjects,TransferObject.dataComparable);
    		return  mBperRecentTransferResponseModel;
    	}
    	BperRecentTransferResponseModel mBperRecentTransferResponseModel=new BperRecentTransferResponseModel();
    			String postData = BperRecentTransferResponseModel.RecentTransferReportProtocal(Contants.publicModel, accountCode, 20);
    			HttpConnector httpConnector = new HttpConnector();
    			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,context);
    			mBperRecentTransferResponseModel.parseJson(httpResult);
    		return mBperRecentTransferResponseModel;
    }
    
    public static void getAmountAvailableLs(Context context,Handler handler, int requastFlag,String accountCode,String provider,String phoneNumber){
    	String postData = CompanyAmountJson.CompanyAmountReportProtocal( accountCode, provider, phoneNumber, Contants.publicModel);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }
    
    private static CompanyAmountResponseModel getAmountAvailableLs(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		CompanyAmountResponseModel mCompanyAmountResponseModel=new CompanyAmountResponseModel();
    		List<AmountAvailable> amountAvailable=new ArrayList<AmountAvailable>();
    		mCompanyAmountResponseModel.setAmountAvailable(amountAvailable);
    		for(int i=0;i<10;i++){
    			AmountAvailable mAmountAvailable=new AmountAvailable();
    			mAmountAvailable.setCommissionAmount(i);
    			mAmountAvailable.setDescription(i+"description");
    			mAmountAvailable.setRechargeAmount(i*2);
    			amountAvailable.add(mAmountAvailable);
    		}
    		amountAvailable.get(0).setRechargeAmount(100);
    		return mCompanyAmountResponseModel;
    	}
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, postData, context);
        final CompanyAmountResponseModel companyAmount = CompanyAmountJson .ParseCompanyAmountResponse(httpResult);
        return companyAmount;
    }
    
    public static void validateSimTopUp(Context context,Handler handler, int requastFlag,String accountCode,String provider,String phoneNumber,AmountAvailable amountAvailable,String transferId){
    	 List<AmountAvailable> amountAvailableList = new ArrayList<AmountAvailable>();
         amountAvailableList.add(amountAvailable);
//    	List<AmountAvailable> amountAvailableList = generateAmountAvailableList(generateAmountAvailable());
//    	PhoneRecipient pr = (PhoneRecipient)getPayee();
//    	String transferId = showForm == FORM_PENDING_PAYMENT && mPendingTransferModel != null ? mPendingTransferModel
//    			.getTransferId() : "";
    	String postData = CheckSimTopUpJson.CheckSimTopUpReportProtocal(Contants.publicModel, amountAvailableList, accountCode, provider, phoneNumber, "1000", transferId);

    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }


    private static CheckSimTopUpResponseModel validateSimTopUp(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		CheckSimTopUpResponseModel mCheckSimTopUpResponseModel=new CheckSimTopUpResponseModel();
    		mCheckSimTopUpResponseModel.setBranchDescription("branchDescription");
    		mCheckSimTopUpResponseModel.setCharges(3);
    		mCheckSimTopUpResponseModel.setCount(1);
    		mCheckSimTopUpResponseModel.setPinIsNotRequired("pinIsNotRequired");
    		mCheckSimTopUpResponseModel.setTransferId("transferId");
    		return mCheckSimTopUpResponseModel;
    	}
    	
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, postData, context);
        CheckSimTopUpResponseModel checkSimTopUp = CheckSimTopUpJson .ParseCheckSimTopUpResponse(httpResult);
        return checkSimTopUp;
    }
    
    public static void confirmTranslationSimTopUp(Context context,Handler handler, int requastFlag,
    		String accountCode,String provider,String phoneNumber,
    		String pinCode,String otpKeySession,AmountAvailable amountAvailable,String transferId){
    	List<AmountAvailable> amountAvailableList = new ArrayList<AmountAvailable>();
        amountAvailableList.add(amountAvailable);
    	String postData = SimTopUpJson.SimTopUpReportProtocal(Contants.publicModel,
                accountCode, provider, phoneNumber,
                pinCode, otpKeySession, 
                Contants.OTP_CHANNEL_MAIL,
                amountAvailableList, "1000", transferId);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }
    
    private static InsertTransferResponse confirmTranslationSimTopUp(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		InsertTransferResponse mInsertTransferResponse=new InsertTransferResponse(TransferType.PHONE_TOP_UP);
    		mInsertTransferResponse.setResultCode(JsonAbstract.SUCCESS_CODE);
    		mInsertTransferResponse.setTransactionId("transactionId");
    		return mInsertTransferResponse;
    	}
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector
                .requestByHttpPost(Contants.mobile_url, postData, context);
//        final SimTopUpResponseModel simTopUp = SimTopUpJson.ParseSimTopUpResponse(httpResult);
        InsertTransferResponse mInsertTransferResponse=new InsertTransferResponse(TransferType.PHONE_TOP_UP);
    	mInsertTransferResponse.parseJson(httpResult);
    	return mInsertTransferResponse;
    }
    
    public static void getIbanPrepaidCard(Context context,Handler handler, int requastFlag){
    	Thread thread=new PostThread(context, Contants.mobile_url, null, handler, requastFlag);
    	thread.start();
    }
    
    private static List<AccountsModel> getIbanPrepaidCard(Context context,String postData) {
//    	if(BaseActivity.NEW_PAYMENT_DEBUG){
//    		SimTopUpResponseModel mSimTopUpResponseModel=new SimTopUpResponseModel();
//    		mSimTopUpResponseModel.setOtpKeySession("otpKeySession");
//    		mSimTopUpResponseModel.setOtpErrorDescription("otpErrorDescription");
//    		mSimTopUpResponseModel.setOtpErrorCode(JsonAbstract.SUCCESS_CODE);
//    		mSimTopUpResponseModel.responsePublicModel.setResultCode(JsonAbstract.SUCCESS_CODE);
//    		mSimTopUpResponseModel.responsePublicModel.setTransactionId("transactionId");
//    		return mSimTopUpResponseModel;
//    	}
    	List<AccountsModel>ibanPrepaidCard=new ArrayList<AccountsModel>();
    	if(Contants.ibanCardAccounts!=null){
    		for(AccountsModel mAccountsModel:Contants.ibanCardAccounts){
    			ibanPrepaidCard.add(mAccountsModel);
    		}
    	}
    	if(Contants.prepaidCardAccounts!=null){
    		for(AccountsModel mAccountsModel:Contants.prepaidCardAccounts){
    			ibanPrepaidCard.add(mAccountsModel);
    		}
    	}
        return ibanPrepaidCard;
    }
    
    public static void vertifyCard(Context context,Handler handler, int requastFlag,
    		String accountCode,
            String cardHolder,
            String cardRecipientLast4Digits,long birthdate) {
    	String _birthdate = "";
    	if(birthdate == 0){
    		_birthdate = "";
    	}else{
    		_birthdate = TimeUtil.getDateString(birthdate, TimeUtil.dateFormat2);
    	}
        String postData = GetCardsJson.GetCardsReportProtocal(Contants.publicModel,cardHolder,
        		cardRecipientLast4Digits, accountCode,_birthdate);
        Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }

    public static GetCardsResponseModel vertifyCard(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		GetCardsResponseModel mGetCardsResponseModel=new GetCardsResponseModel();
    		List<InfoCardsModel> infoCardListModel=new ArrayList<InfoCardsModel>();
    		mGetCardsResponseModel.setInfoCardListModel(infoCardListModel);
    		for(int i=0;i<10;i++){
    			InfoCardsModel mInfoCardsModel=new InfoCardsModel();
    			infoCardListModel.add(mInfoCardsModel);
    			mInfoCardsModel.setCardHash(i+"cardHash");
    			mInfoCardsModel.setCardNumberMask(i+"cardNumberMask");
    			mInfoCardsModel.setExpirationDate(TimeUtil.getDateString(System.currentTimeMillis(),TimeUtil.dateFormat5));
    			mInfoCardsModel.setHolderBirthDate(i+"holderBirthDate");
    			mInfoCardsModel.setName(i+"name");
    			mInfoCardsModel.setTitle(i+"title");
    			mInfoCardsModel.setType(i+"type");
    		}
    		return mGetCardsResponseModel;
    	}
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector
                .requestByHttpPost(Contants.mobile_url, postData, context);
        GetCardsResponseModel getCards = GetCardsJson.parseGetCardResponse(httpResult);
        return getCards;
    }
    
    /**
     * @param context
     * @param handler
     * @param requastFlag
     * @param accountCode AccountsModel payer
     * @param cardHash InfoCardsModel 
     * @param recipientCardNumber CardRecipient
     * @param ttitle InfoCardsModel 
     * @param recipientName CardRecipient
     * @param description
     * @param amount
     * @param transferId String transferId = showForm == FORM_PENDING_PAYMENT && mPendingTransferModel != null ? mPendingTransferModel.getTransferId() : "";
     */
    public static void validateCard(Context context,Handler handler, int requastFlag,
    		String accountCode,String cardHash,String recipientCardNumber,String title,String recipientName,
    		String description,double amount,String transferId){
    	String postData = CheckRechargeCardJson.CheckRechargeCardReportProtocal(
    			accountCode, Contants.publicModel, amount, description,
    			cardHash,recipientCardNumber,
    			title, recipientName, "1000", transferId);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	    	thread.start();
    }

    private static CheckRechargeCardResponseModel validateCard(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		CheckRechargeCardResponseModel mCheckRechargeCardResponseModel=new CheckRechargeCardResponseModel();
    		mCheckRechargeCardResponseModel.setCharges(1);
    		mCheckRechargeCardResponseModel.setExecutionDate(TimeUtil.getDateString(System.currentTimeMillis(),TimeUtil.dateFormat5));
    		mCheckRechargeCardResponseModel.setSrcbankName("srcbankName");
    		mCheckRechargeCardResponseModel.setSrcBranchName("srcBranchName");
    		mCheckRechargeCardResponseModel.setTransferId("transferId");
    		mCheckRechargeCardResponseModel.responsePublicModel.setResultCode(JsonAbstract.SUCCESS_CODE);
    		return mCheckRechargeCardResponseModel;
    	}
//        String dateString = TimeUtil.getDateString(generateDate(), TimeUtil.dateFormat2);
//        AccountsModel accountsModel = getPayer();
//        CardRecipient cr = (CardRecipient)getPayee();// generateCardPayee();
//        double amountdouble = generateAmount();
//        String description = generateDescription();
//        String title = "10";
//        String transferId = showForm == FORM_PENDING_PAYMENT && mPendingTransferModel != null ? mPendingTransferModel
//                .getTransferId() : "";
//        String postData = CheckRechargeCardJson.CheckRechargeCardReportProtocal(
//                accountsModel.getAccountCode(), Contants.publicModel, amountdouble, description,
//                tmp_InfoCardsModel.getCardHash(), cr.getCardNumber(),
//                tmp_InfoCardsModel.getTitle(), cr.getName(), "1000", transferId);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector
                .requestByHttpPost(Contants.mobile_url, postData, context);
        CheckRechargeCardResponseModel checkRechargeCard = CheckRechargeCardJson
                .ParseCheckRechargeCardResponse(httpResult);
        return checkRechargeCard;
    }
    
    public static void confirmCardRecharge(Context context,Handler handler, int requastFlag,
    		String accountCode,InfoCardsModel tmp_InfoCardsModel,String cardNumber,String name,
    		String description,double amount,String pinCode,String otpKeySession,CheckRechargeCardResponseModel checkRechargeCard){
    	 String postData = InsertRechargeCardJson.InsertRechargeCardReportProtocal(
                 Contants.publicModel, accountCode, amount,
                 tmp_InfoCardsModel.getCardHash(), cardNumber, description, name,
                 tmp_InfoCardsModel.getTitle(), pinCode, otpKeySession,
                 Contants.OTP_CHANNEL_MAIL, "1000", checkRechargeCard);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	    	thread.start();
    }
    
    private static InsertTransferResponse confirmCardRecharge(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		InsertTransferResponse mInsertTransferResponse=new InsertTransferResponse(TransferType.CARD_TOP_UP);
    		mInsertTransferResponse.setResultCode(JsonAbstract.SUCCESS_CODE);
    		mInsertTransferResponse.setResultDescription("resultDescription");
    		mInsertTransferResponse.setTransactionId("transactionId");
    		return mInsertTransferResponse;
    	}
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector .requestByHttpPost(Contants.mobile_url, postData, context);
        InsertTransferResponse mInsertTransferResponse=new InsertTransferResponse(TransferType.CARD_TOP_UP);
    	mInsertTransferResponse.parseJson(httpResult);
    	return mInsertTransferResponse;
//        final ResponsePublicModel insertRechargeCard = InsertRechargeCardJson .ParseInsertRechargeCardResponse(httpResult);
//        return insertRechargeCard;
    }
    
    public static void insertRecipient(Context context,Handler handler,int requastFlag,String recipientType,Object data){
    	String postData = InsertRecipientJson.InsertRecipientReportProtocal( Contants.publicModel, recipientType, data);
    	Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
    }
    
    public static ResponsePublicModel insertRecipient(Context context,String postData) {
    	if(BaseActivity.isOffline){
    		ResponsePublicModel mResponsePublicModel=new ResponsePublicModel();
    		mResponsePublicModel.setResultCode(JsonAbstract.SUCCESS_CODE);
    		mResponsePublicModel.setResultDescription("resultDescription");
    		mResponsePublicModel.setTransactionId("transactionId");
    		return mResponsePublicModel;
    	}
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost( Contants.mobile_url, postData, context);
		ResponsePublicModel responsePublicModel = InsertRecipientJson .ParseInsertRecipientResponse(httpResult);
		return responsePublicModel;
	}

    
	/////
	
	public static List<AccountsForServiceModel> geBankTransferAccountst(Context context){
		if(Contants.bankTransferAccounts==null||Contants.bankTransferAccounts.size()<=0){
			//    Contants.bankTransferAccounts = getAccountsByService("003");
			Contants.bankTransferAccounts = getAccountsByService(context,ServiceCode.SERVER_CODES);
		}
		return Contants.bankTransferAccounts;
	}
	
	public static List<AccountsForServiceModel> getTransferEntryAccounts(Context context){
		if(Contants.transferEntryAccounts==null||Contants.transferEntryAccounts.size()<=0){
//		    Contants.transferEntryAccounts = getAccountsByService("008");
//	        getBalance(Contants.transferEntryAccounts.get(0).getAccounts(), null);
			Contants.transferEntryAccounts=geBankTransferAccountst(context);
		}
		return Contants.transferEntryAccounts;
	}
	
	public static List<AccountsForServiceModel> getSimTopUpAccounts(Context context){
		if(Contants.simTopUpAccounts==null||Contants.simTopUpAccounts.size()<=0){
// Contants.simTopUpAccounts = getAccountsByService("022");
//	        getBalance(Contants.simTopUpAccounts.get(0).getAccounts(), null);
			Contants.simTopUpAccounts=geBankTransferAccountst(context);
		}
		return Contants.simTopUpAccounts;
	}
	
	public static List<AccountsForServiceModel> getChargeAccounts(Context context){
		if(Contants.chargeAccounts==null||Contants.chargeAccounts.size()<=0){
//	          Contants.chargeAccounts = getAccountsByService("026");
//	        getBalance(Contants.chargeAccounts.get(0).getAccounts(), null);
			Contants.chargeAccounts=geBankTransferAccountst(context);
		}
		return Contants.chargeAccounts;
	}

	/**getRecipientList
	 * @param context
	 * @param handler
	 * @param requastFlag
	 * @param transferType
	 */
	public static void getRecipientList(Context context,Handler handler, int requastFlag,TransferType transferType){
		String searchType=null;
		if(transferType==TransferType.BANK_TRANSFER||transferType==TransferType.TRANSFER_ENTRY){
			searchType="BANKSEPA";
		}else if(transferType==TransferType.PHONE_TOP_UP){
			searchType="PHONE";
		}else if(transferType==TransferType.CARD_TOP_UP){
			searchType="CARD";
		}
		String postData = GetRecipientListJson.getRecipientListReportProtocal(Contants.publicModel,searchType);
		Thread thread=new PostThread(context, Contants.mobile_url, postData, handler, requastFlag);
    	thread.start();
	}
	public static GetRecipientListModel getRecipientList(Context context,String postData){
		if(BaseActivity.isOffline){
			GetRecipientListModel getRecipientListModel=new GetRecipientListModel();
			getRecipientListModel.responsePublicModel=new ResponsePublicModel();
			getRecipientListModel.responsePublicModel.setResultCode(0);
			List<BankRecipient> bankRecipientList=new ArrayList<BankRecipient>();
			getRecipientListModel.setBankRecipientList(bankRecipientList);
			List<CardRecipient> cardRecipientList=new ArrayList<CardRecipient>();
			getRecipientListModel.setCardRecipientList(cardRecipientList);
			List<PhoneRecipient> phoneRecipientList=new ArrayList<PhoneRecipient>();
			getRecipientListModel.setPhoneRecipientList(phoneRecipientList);
			ResponsePublicModel responsePublicModel=new ResponsePublicModel();
			getRecipientListModel.setResponsePublicModel(responsePublicModel);
			for(int i=0;i<60;i++){
				BankRecipient mBankRecipient=new BankRecipient();
				bankRecipientList.add(mBankRecipient);
				mBankRecipient.setBic("bic"+i);
				mBankRecipient.setIbanCode("ibanCode"+i);
				mBankRecipient.setId("id"+i);
				mBankRecipient.setName((char)('a'+(i%20))+"Bankname"+i);
				
				CardRecipient mCardRecipient=new CardRecipient();
				cardRecipientList.add(mCardRecipient);
				mCardRecipient.setCardNumber("cardNumber"+i);
				mCardRecipient.setId("id"+i);
				mCardRecipient.setName((char)('a'+(i%26))+"Cardname"+i);
				
				PhoneRecipient mPhoneRecipient=new PhoneRecipient();
				phoneRecipientList.add(mPhoneRecipient);
				mPhoneRecipient.setId(i+"");
				mPhoneRecipient.setName((char)('a'+(i%26))+"phonename"+i);
				mPhoneRecipient.setProviderCode(TransferObjectSim.providerCodes[i%TransferObjectSim.providerCodes.length]);
				mPhoneRecipient.setPhoneNumber(Long.toString(131215810+i));
			}
			return getRecipientListModel;
		}
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
		Contants.getRecipientListModel = GetRecipientListJson .parseGetRecipientListResponse(httpResult);
		return Contants.getRecipientListModel;
	}
	
	private static List<AccountsForServiceModel> getAccountsByService(Context context,String[] serviceCode) {
		List<ServicesModel> services = new ArrayList<ServicesModel>();
		if (serviceCode != null)
			for (int i = serviceCode.length - 1; i >= 0; i--) {
				ServicesModel service = new ServicesModel();
				service.setServiceCode(serviceCode[i]);
				services.add(service);
			}
		return getAccountsByService(context,services);
	}
	public static List<AccountsForServiceModel> getAccountsByService(Context context,String serviceCode) {
		if(BaseActivity.isOffline){
			List<AccountsForServiceModel> accountsForServiceModels=new ArrayList<AccountsForServiceModel>();
			AccountsForServiceModel mAccountsForServiceModel=new AccountsForServiceModel();
			accountsForServiceModels.add(mAccountsForServiceModel);
//			mAccountsForServiceModel.setEffectiveDate(System.currentTimeMillis());
			mAccountsForServiceModel.setServiceCode(serviceCode);
			List<AccountsModel> accountsModels=new ArrayList<AccountsModel>();
			mAccountsForServiceModel.setAccounts(accountsModels);
			for(int i=0;i<3;i++){
				AccountsModel accountsModel=new AccountsModel();
				accountsModels.add(accountsModel);
				accountsModel.setAccountAlias("debug alisa "+i);
			}
			
			return accountsForServiceModels;
		}
		List<ServicesModel> services = new ArrayList<ServicesModel>();
		ServicesModel service = new ServicesModel();
		service.setServiceCode(serviceCode);
		services.add(service);
		return getAccountsByService(context,services);
	}
	private static List<AccountsForServiceModel> getAccountsByService(Context context,List<ServicesModel> services) {
		try {
			
			List<AccountsForServiceModel> accountsForServiceModels = null;
			
			String postData = AccountsByServicesJson.AccountsByServicesReportProtocal(
					Contants.publicModel, services);
			HttpConnector httpConnector = new HttpConnector();
			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
					context);
			GetAccountsByServicesResponseModel getAccountsByServices = AccountsByServicesJson
			.ParseGetAccountsByServicesResponse(httpResult);
			if (getAccountsByServices == null) {
				LogManager.d("responsePublicModelgetAccountsByServices=null" + postData);
				return null;
			}
			
			if (getAccountsByServices.responsePublicModel != null) {
				if (getAccountsByServices.responsePublicModel.isSuccess()) {
					accountsForServiceModels = getAccountsByServices.getAccountsForServiceList();
				} else {
					MainActivity baseActivity = (MainActivity)context;
					baseActivity
					.displayErrorMessage(getAccountsByServices.responsePublicModel.eventManagement
							.getErrorCode(),getAccountsByServices.responsePublicModel.eventManagement
							.getErrorDescription());
				}
			}
			
			return accountsForServiceModels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//	public static final String DATE_FORMAT="dd.MM.yy";//"yyyy-MM-dd HH:mm:ss"
	public static final Locale locale=Locale.ITALY;
	public static String getCurrency(){
		return Currency.getInstance(locale).getSymbol();
	}

	 public static String formateAmount(String str,int minimumFractionDigits,int maximumFractionDigits) {
	        NumberFormat format = NumberFormat.getInstance(locale);
	        try {
	        	Number number=format.parse(str);
	        	double amount=number.doubleValue();
	            format.setMinimumFractionDigits(minimumFractionDigits);
	            format.setMaximumFractionDigits(maximumFractionDigits);
	            str = format.format(amount);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return str;
	    }

	 public static String formateAmount(double amount,int minimumFractionDigits,int maximumFractionDigits) {
	        NumberFormat format = NumberFormat.getInstance(locale);
	            format.setMinimumFractionDigits(minimumFractionDigits);
	            format.setMaximumFractionDigits(maximumFractionDigits);
	            String str = format.format(amount);
	        return str;
	  }
	 
	 public static String formateAmount(String str) {
	        NumberFormat format = NumberFormat.getInstance(locale);
	        try {
	        	Number number=format.parse(str);
	        	double amount=number.doubleValue();
	            format.setMinimumFractionDigits(2);
	            format.setMaximumFractionDigits(2);
	            str = format.format(amount);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return str;
	    }
	 public static double parseDouble(String str) {
	        NumberFormat format = NumberFormat.getInstance(locale);
	        try {
	        	Number number=format.parse(str);
	        	return number.doubleValue();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	
}
