
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.GetAccountByPhoneNumberModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetAccountByPhoneNumberJson {
    /**
     * @param publicModel
     * @return
     */
    public static String getAccountByPhoneNumberReportProtocal(RequestPublicModel publicModel,
            String linkedPhoneNumber) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getAccountByPhoneNumberObj = new JSONObject();
            getAccountByPhoneNumberObj.put("bankName", publicModel.getBankName());
            getAccountByPhoneNumberObj.put("channel", publicModel.getChannel());
            getAccountByPhoneNumberObj.put("customerCode", publicModel.getCustomerCode());
            getAccountByPhoneNumberObj.put("enterpriseId", publicModel.getEnterpriseId());
            getAccountByPhoneNumberObj.put("serviceType", ServiceType.getPendingTransfer);
            getAccountByPhoneNumberObj.put("userAgent", publicModel.getUserAgent());
            getAccountByPhoneNumberObj.put("sessionId", publicModel.getSessionId());
            getAccountByPhoneNumberObj.put("token", publicModel.getToken());
            getAccountByPhoneNumberObj.put("linkedPhoneNumber", linkedPhoneNumber);
            getAccountByPhoneNumberObj.put("serviceType", "2086");

            jsonObj.put("GetAccountByPhoneNumberRequest", getAccountByPhoneNumberObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getAccountByPhoneNumber is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static GetAccountByPhoneNumberModel parseGetAccountByPhoneNumberResponse(String json) {
        GetAccountByPhoneNumberModel getAccountByPhoneNumber = new GetAccountByPhoneNumberModel();
        List<AccountsModel> accountList = new ArrayList<AccountsModel>();
        if (json == null) {
            LogManager.d("parseGetAccountByPhoneNumberResponse" + "json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getAccountByPhoneNumberObj = jsonObj
                    .getJSONObject("GetAccountByPhoneNumberResponse");

            getAccountByPhoneNumber.responsePublicModel.setResultCode(getAccountByPhoneNumberObj
                    .getInt("resultCode"));
            getAccountByPhoneNumber.responsePublicModel
                    .setResultDescription(getAccountByPhoneNumberObj.optString("resultDescription"));
            if (getAccountByPhoneNumber.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getAccountByPhoneNumberObj
                        .getJSONObject("eventManagement");
                getAccountByPhoneNumber.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                getAccountByPhoneNumber.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getAccountByPhoneNumber;
            }

            getAccountByPhoneNumber.responsePublicModel.setTransactionId(getAccountByPhoneNumberObj
                    .optString("transactionId"));

            JSONObject accountsObj = getAccountByPhoneNumberObj.getJSONObject("account");
//            for (int i = 0; i < accountsArray.length(); i++) {
                AccountsModel account = new AccountsModel();
                account.setAccountId(accountsObj.optString("accountId"));
                account.setAccountCode(accountsObj.optString("accountCode"));
                account.setIbanCode(accountsObj.optString("ibanCode"));
                account.setIsInformative(accountsObj.optString("isInformative"));
                account.setAccountAlias(accountsObj.optString("accountAlias"));
                account.setBranchCode(accountsObj.optString("branchCode"));
                account.setBankCode(accountsObj.optString("bankCode"));

                BankServiceType bankServiceType = new BankServiceType();
                JSONObject bankServiceTypeObj = accountsObj.getJSONObject(
                        "bankServiceType");
                bankServiceType.setBankServiceCode(bankServiceTypeObj.optString("bankServiceCode"));
                bankServiceType.setServiceCategory(bankServiceTypeObj.optString("serviceCategory"));
                bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
                account.setBankServiceType(bankServiceType);

                account.setCurrencyAcc(accountsObj.optString("currencyAcc"));
                account.setFrontendCategory(accountsObj.optString(
                        "frontendCategory"));
                account.setMortageType(accountsObj.optString("mortageType"));
                account.setCabCode(accountsObj.optString("cabCode"));
                account.setNotes(accountsObj.optString("notes"));
                account.setOpeningDate(accountsObj.optString("openingDate"));
                account.setProdAccount(accountsObj.optString("prodAccount"));
                account.setProdAccountDesc(accountsObj.optString(
                        "prodAccountDesc"));
                account.setPreferred(accountsObj.optString("preferred"));
                accountList.add(account);
//            }
            getAccountByPhoneNumber.setAccountList(accountList);
        } catch (Exception e) {
            LogManager.e("parseGetAccountByPhoneNumberResponse is error" + e.getLocalizedMessage());
        }
        return getAccountByPhoneNumber;
    }
}
