package com.accenture.mbank.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.AccountsByServicesJson;
import com.accenture.mbank.logic.GetRecipientListJson;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.net.HttpConnector;

public class NewPaymentDataUtils {
	public static List<AccountsForServiceModel> geBankTransferAccountst(Context context){
		if(Contants.bankTransferAccounts==null||Contants.bankTransferAccounts.size()<=0){
			//    Contants.bankTransferAccounts = getAccountsByService("003");
			Contants.bankTransferAccounts = getAccountsByService(context,ServiceCode.SERVER_CODES);
			if(Contants.bankTransferAccounts!=null&&Contants.bankTransferAccounts.size()>0){
				getBalance(context,Contants.bankTransferAccounts.get(0).getAccounts(), null);
			}
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
	public static GetRecipientListModel getRecipientList(Context context){
		if(Contants.getRecipientListModel==null){
			String postData = GetRecipientListJson.getRecipientListReportProtocal(Contants.publicModel);
			HttpConnector httpConnector = new HttpConnector();
			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
			Contants.getRecipientListModel = GetRecipientListJson .parseGetRecipientListResponse(httpResult);
		}
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
	private List<AccountsForServiceModel> getAccountsByService(Context context,String serviceCode) {
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
							.getErrorDescription());
				}
			}
			
			return accountsForServiceModels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public static void getBalance(Context context,List<AccountsModel> ls, String accountCode) {
		String postData = GetRecipientListJson.getBalanceReportProtocal(Contants.publicModel,
				accountCode, "2");
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
		GetRecipientListJson.parseBalanceReportResponseList(httpResult, ls);
	}
	
}
