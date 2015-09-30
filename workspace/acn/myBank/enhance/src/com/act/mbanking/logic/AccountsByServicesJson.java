
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.BankServiceType;
import com.act.mbanking.bean.GetAccountsByServicesResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.ServicesModel;
import com.act.mbanking.utils.LogManager;

public class AccountsByServicesJson {
    /**
     * @param publicModel
     * @param services
     * @return
     */
    public static String AccountsByServicesReportProtocal(RequestPublicModel publicModel,
            List<ServicesModel> services) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getAccountsByServicesObj = new JSONObject();
            getAccountsByServicesObj.put("bankName", publicModel.getBankName());
            getAccountsByServicesObj.put("serviceType", ServiceType.getAccountsByServices);
            getAccountsByServicesObj.put("enterpriseId", publicModel.getEnterpriseId());
            getAccountsByServicesObj.put("customerCode", publicModel.getCustomerCode());
            getAccountsByServicesObj.put("channel", publicModel.getChannel());
            getAccountsByServicesObj.put("userAgent", publicModel.getUserAgent());
            getAccountsByServicesObj.put("sessionId", publicModel.getSessionId());
            getAccountsByServicesObj.put("token", publicModel.getToken());

            JSONArray servicesArray = new JSONArray();
            for (int i = 0; i < services.size(); i++) {
                JSONObject serviceObj = new JSONObject();
                serviceObj.put("serviceCode", services.get(i).getServiceCode());
                servicesArray.put(i, serviceObj);
            }
            getAccountsByServicesObj.put("services", servicesArray);

            jsonObj.put("GetAccountsByServicesRequest", getAccountsByServicesObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("AccountsByServicesReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * @param json
     * @return
     */
    public static GetAccountsByServicesResponseModel ParseGetAccountsByServicesResponse(String json) {

        GetAccountsByServicesResponseModel responsePublicModel = new GetAccountsByServicesResponseModel();

        List<AccountsForServiceModel> accountsForServiceList = new ArrayList<AccountsForServiceModel>();
        if (json == null) {
            
            LogManager.d("ParseInsertRechargeCardResponse"+"json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getAccountbySercicesObj = jsonObj
                    .getJSONObject("GetAccountsByServicesResponse");

            responsePublicModel.responsePublicModel.setResultCode(getAccountbySercicesObj
                    .getInt("resultCode"));
            responsePublicModel.responsePublicModel.setResultDescription(getAccountbySercicesObj
                    .optString("resultDescription"));
            if (responsePublicModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getAccountbySercicesObj
                        .getJSONObject("eventManagement");
                responsePublicModel.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                responsePublicModel.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return responsePublicModel;
            }

            responsePublicModel.responsePublicModel.setTransactionId(getAccountbySercicesObj
                    .optString("transactionId"));

            JSONArray accountsForServiceArray = getAccountbySercicesObj
                    .getJSONArray("accountsForServiceList");
            for (int i = 0; i < accountsForServiceArray.length(); i++) {
                AccountsForServiceModel accountsForService = new AccountsForServiceModel();
                accountsForService.setServiceCode(accountsForServiceArray.getJSONObject(i)
                        .optString("serviceCode"));

                List<AccountsModel> accountsList = new ArrayList<AccountsModel>();
                JSONArray accountsArray = accountsForServiceArray.getJSONObject(i).getJSONArray(
                        "accounts");
                for (int j = 0; j < accountsArray.length(); j++) {
                    AccountsModel account = new AccountsModel();
                    account.setAccountId(accountsArray.getJSONObject(j).optString("accountId"));
                    account.setAccountCode(accountsArray.getJSONObject(j).optString("accountCode"));
                    account.setIbanCode(accountsArray.getJSONObject(j).optString("ibanCode"));
                    account.setIsInformative(accountsArray.getJSONObject(j).optString(
                            "isInformative"));
                    account.setAccountAlias(accountsArray.getJSONObject(j)
                            .optString("accountAlias"));
                    account.setBranchCode(accountsArray.getJSONObject(j).optString("branchCode"));
                    account.setBankCode(accountsArray.getJSONObject(j).optString("bankCode"));

                    BankServiceType bankServiceType = new BankServiceType();
                    JSONObject bankServiceTypeObj = accountsArray.getJSONObject(j).getJSONObject(
                            "bankServiceType");
                    bankServiceType.setBankServiceCode(bankServiceTypeObj
                            .optString("bankServiceCode"));
                    bankServiceType.setServiceCategory(bankServiceTypeObj
                            .optString("serviceCategory"));
                    bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
                    account.setBankServiceType(bankServiceType);

                    account.setCurrencyAcc(accountsArray.getJSONObject(j).optString("currencyAcc"));
                    account.setFrontendCategory(accountsArray.getJSONObject(j).optString(
                            "frontendCategory"));
                    account.setMortageType(accountsArray.getJSONObject(j).optString("mortageType"));
                    account.setCabCode(accountsArray.getJSONObject(j).optString("cabCode"));
                    account.setNotes(accountsArray.getJSONObject(j).optString("notes"));
                    account.setOpeningDate(accountsArray.getJSONObject(j).optString("openingDate"));
                    account.setProdAccount(accountsArray.getJSONObject(j).optString("prodAccount"));
                    account.setProdAccountDesc(accountsArray.getJSONObject(j).optString(
                            "prodAccountDesc"));
                    account.setPreferred(accountsArray.getJSONObject(j).optString("preferred"));
                    accountsList.add(account);
                }
                accountsForService.setAccounts(accountsList);
                accountsForServiceList.add(accountsForService);
            }
            responsePublicModel.setAccountsForServiceList(accountsForServiceList);

        } catch (JSONException e) {
            e.printStackTrace();
            LogManager.e("ParseInsertRechargeCardResponse is error " + e);
        }
        
        return responsePublicModel;
    }
}
