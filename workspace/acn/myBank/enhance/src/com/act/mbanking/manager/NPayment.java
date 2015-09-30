package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.act.mbanking.Contants;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetAccountsByServicesResponseModel;
import com.act.mbanking.bean.GetRecipientListModel;
import com.act.mbanking.bean.ServicesModel;
import com.act.mbanking.logic.AccountsByServicesJson;
import com.act.mbanking.logic.GetRecipientListJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.utils.LogManager;

public abstract class NPayment<Payer,Payee> {
    Payer mPayer;
    Payee mPayee;

    protected List<Payer> mPayers;
    protected List<Payee> mPayees;
    
    protected GetRecipientListModel getRecipientListModel;
    
    public List<Payer>getPayers(){
        return mPayers;
    }
    
    abstract List<Payee>getPayees();
    

    public static  List<AccountsForServiceModel> getAccountsByService(Context context,String serviceCode) {
        List<ServicesModel> services = new ArrayList<ServicesModel>();
        ServicesModel service = new ServicesModel();
        service.setServiceCode(serviceCode);
        services.add(service);
        return getAccountsByService(context,services);
    }

    /**get Account by service codes
     * @param context
     * @param serviceCode
     * @return
     */
    public static List<AccountsForServiceModel> getAccountsByService(Context context,String[] serviceCode) {
        List<ServicesModel> services = new ArrayList<ServicesModel>();
        if (serviceCode != null)
            for (int i = serviceCode.length - 1; i >= 0; i--) {
                ServicesModel service = new ServicesModel();
                service.setServiceCode(serviceCode[i]);
                services.add(service);
            }
        return getAccountsByService(context,services);
    }


    /**get Account by service code
     * @param context
     * @param services
     * @return
     */
    public static List<AccountsForServiceModel> getAccountsByService(Context context,List<ServicesModel> services) {
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
                    BaseActivity baseActivity = (BaseActivity)context;
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
    


    public static  void getBalance(Context context,List<AccountsModel> ls, String accountCode) {
        String postData = GetRecipientListJson.getBalanceReportProtocal(Contants.publicModel,
                accountCode, "2");
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector
                .requestByHttpPost(Contants.mobile_url, postData, context);

//        LogManager.d("getRecipientListModel" + getRecipientListModel);
        GetRecipientListJson.parseBalanceReportResponseList(httpResult, ls);
    }

}
