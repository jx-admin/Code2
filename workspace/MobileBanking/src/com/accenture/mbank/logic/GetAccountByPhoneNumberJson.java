
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

            jsonObj.put("getAccountByPhoneNumber", getAccountByPhoneNumberObj);
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
                    .getJSONObject("getAccountByPhoneNumberResponse");

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

            JSONArray accountsArray = getAccountByPhoneNumberObj.getJSONArray("account");
            for (int i = 0; i < accountsArray.length(); i++) {
                AccountsModel account = new AccountsModel();
                account.setAccountId(accountsArray.getJSONObject(i).optString("accountId"));
                account.setAccountCode(accountsArray.getJSONObject(i).optString("accountCode"));
                account.setIbanCode(accountsArray.getJSONObject(i).optString("ibanCode"));
                account.setIsInformative(accountsArray.getJSONObject(i).optString("isInformative"));
                account.setAccountAlias(accountsArray.getJSONObject(i).optString("accountAlias"));
                account.setBranchCode(accountsArray.getJSONObject(i).optString("branchCode"));
                account.setBankCode(accountsArray.getJSONObject(i).optString("bankCode"));

                BankServiceType bankServiceType = new BankServiceType();
                JSONObject bankServiceTypeObj = accountsArray.getJSONObject(i).getJSONObject(
                        "bankServiceType");
                bankServiceType.setBankServiceCode(bankServiceTypeObj.optString("bankServiceCode"));
                bankServiceType.setServiceCategory(bankServiceTypeObj.optString("serviceCategory"));
                bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
                account.setBankServiceType(bankServiceType);

                account.setCurrencyAcc(accountsArray.getJSONObject(i).optString("currencyAcc"));
                account.setFrontendCategory(accountsArray.getJSONObject(i).optString(
                        "frontendCategory"));
                account.setMortageType(accountsArray.getJSONObject(i).optString("mortageType"));
                account.setCabCode(accountsArray.getJSONObject(i).optString("cabCode"));
                account.setNotes(accountsArray.getJSONObject(i).optString("notes"));
                account.setOpeningDate(accountsArray.getJSONObject(i).optString("openingDate"));
                account.setProdAccount(accountsArray.getJSONObject(i).optString("prodAccount"));
                account.setProdAccountDesc(accountsArray.getJSONObject(i).optString(
                        "prodAccountDesc"));
                account.setPreferred(accountsArray.getJSONObject(i).optString("preferred"));
                accountList.add(account);
            }
            getAccountByPhoneNumber.setAccountList(accountList);
        } catch (Exception e) {
            LogManager.e("parseGetAccountByPhoneNumberResponse is error" + e.getLocalizedMessage());
        }
        return getAccountByPhoneNumber;
    }
}
