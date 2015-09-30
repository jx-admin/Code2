package com.act.mbanking;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetAccountsByServicesResponseModel;
import com.act.mbanking.bean.GetRecipientListModel;
import com.act.mbanking.bean.ServicesModel;
import com.act.mbanking.logic.AccountsByServicesJson;
import com.act.mbanking.logic.GetRecipientListJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.utils.LogManager;

public class NewPaymentDataUtils {
	public static List<AccountsForServiceModel> geBankTransferAccountst(Context context){
		if(App.app.bankTransferAccounts==null||App.app.bankTransferAccounts.size()<=0){
			//    App.app.bankTransferAccounts = getAccountsByService("003");
			App.app.bankTransferAccounts = getAccountsByService(context,ServiceCode.SERVER_CODES);
			if(App.app.bankTransferAccounts!=null&&App.app.bankTransferAccounts.size()>0){
				getBalance(context,App.app.bankTransferAccounts.get(0).getAccounts(), null);
			}
		}
		return App.app.bankTransferAccounts;
	}
	
	public static List<AccountsForServiceModel> getTransferEntryAccounts(Context context){
		if(App.app.transferEntryAccounts==null||App.app.transferEntryAccounts.size()<=0){
//		    App.app.transferEntryAccounts = getAccountsByService("008");
//	        getBalance(App.app.transferEntryAccounts.get(0).getAccounts(), null);
			App.app.transferEntryAccounts=geBankTransferAccountst(context);
		}
		return App.app.transferEntryAccounts;
	}
	
	public static List<AccountsForServiceModel> getSimTopUpAccounts(Context context){
		if(App.app.simTopUpAccounts==null||App.app.simTopUpAccounts.size()<=0){
// App.app.simTopUpAccounts = getAccountsByService("022");
//	        getBalance(App.app.simTopUpAccounts.get(0).getAccounts(), null);
			App.app.simTopUpAccounts=geBankTransferAccountst(context);
		}
		return App.app.simTopUpAccounts;
	}
	
	public static List<AccountsForServiceModel> getChargeAccounts(Context context){
		if(App.app.chargeAccounts==null||App.app.chargeAccounts.size()<=0){
//	          App.app.chargeAccounts = getAccountsByService("026");
//	        getBalance(App.app.chargeAccounts.get(0).getAccounts(), null);
			App.app.chargeAccounts=geBankTransferAccountst(context);
		}
		return App.app.chargeAccounts;
	}
	public static GetRecipientListModel getRecipientList(Context context){
		if(App.app.getRecipientListModel==null){
			String postData = GetRecipientListJson.getRecipientListReportProtocal(Contants.publicModel);
			HttpConnector httpConnector = new HttpConnector();
			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
			App.app.getRecipientListModel = GetRecipientListJson .parseGetRecipientListResponse(httpResult);
		}
		return App.app.getRecipientListModel;
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
