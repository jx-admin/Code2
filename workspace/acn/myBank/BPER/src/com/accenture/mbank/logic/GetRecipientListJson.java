
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.Account;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.Balance;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetRecipientListJson {/**
     * @param publicModel
     * @param paymentMethodId Payment method identifier ( allow value: 1=balance from credit card, 2=balance from bank)
     * @return
     */
    public static String getBalanceReportProtocal(RequestPublicModel publicModel,String accountCode,String paymentMethodId){
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecipientObj = new JSONObject();
            getRecipientObj.put("bankName", publicModel.getBankName());
            getRecipientObj.put("serviceType", "1003");//ServiceType.getRecipientList);
            getRecipientObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecipientObj.put("paymentMethod", paymentMethodId);
            if(accountCode!=null)
            getRecipientObj.put("accountCode", accountCode);
            getRecipientObj.put("channel", publicModel.getChannel());
            getRecipientObj.put("userAgent", publicModel.getUserAgent());
            getRecipientObj.put("customerNumber", publicModel.getCustomerNumber());
            getRecipientObj.put("sessionId", publicModel.getSessionId());
            getRecipientObj.put("token", publicModel.getToken());
            
            jsonObj.put("GetBalanceRequest", getRecipientObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getRecipientListReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    


    public static String getRecipientListReportProtocal(RequestPublicModel publicModel,String searchType) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecipientObj = new JSONObject();
            getRecipientObj.put("serviceType", ServiceType.getRecipientList);
            getRecipientObj.put("bankName", publicModel.getBankName());
            getRecipientObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecipientObj.put("customerNumber", publicModel.getCustomerNumber());
            getRecipientObj.put("channel", publicModel.getChannel());
            getRecipientObj.put("userAgent", publicModel.getUserAgent());
            getRecipientObj.put("sessionId", publicModel.getSessionId());
            getRecipientObj.put("token", publicModel.getToken());
            getRecipientObj.put("searchType",searchType);

            jsonObj.put("GetRecipientListRequest", getRecipientObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getRecipientListReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static GetRecipientListModel parseGetRecipientListResponse(String json) {
        GetRecipientListModel getRecipientList = new GetRecipientListModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getRecipientListObj = jsonObj.getJSONObject("GetRecipientListResponse");

            getRecipientList.responsePublicModel.setResultCode(getRecipientListObj
                    .getInt("resultCode"));
            getRecipientList.responsePublicModel.setResultDescription(getRecipientListObj
                    .optString("resultDescription"));
            if (getRecipientList.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getRecipientListObj
                        .getJSONObject("eventManagement");
                getRecipientList.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                getRecipientList.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getRecipientList;
            }
            getRecipientList.responsePublicModel.setTransactionId(getRecipientListObj .optString("transactionId"));

            JSONArray bankRecipientListArray = getRecipientListObj .getJSONArray("bankRecipientList");
            List<BankRecipient> bankRecipientList = new ArrayList<BankRecipient>();
            for (int i = 0; i < bankRecipientListArray.length(); i++) {
                BankRecipient bankRecipient = new BankRecipient();
                bankRecipient.setId(bankRecipientListArray.getJSONObject(i).optString("id"));

                JSONObject bankObj = bankRecipientListArray.getJSONObject(i).getJSONObject("bank");
                bankRecipient.setIbanCode(bankObj.optString("ibanCode"));
                bankRecipient.setName(bankObj.optString("name"));
                bankRecipient.setBic(bankObj.optString("bic"));
                bankRecipientList.add(bankRecipient);
            }
            getRecipientList.setBankRecipientList(bankRecipientList);

            JSONArray cardRecipientArray = getRecipientListObj.getJSONArray("cardRecipientList");
            List<CardRecipient> cardRecipientList = new ArrayList<CardRecipient>();
            for (int i = 0; i < cardRecipientArray.length(); i++) {
                CardRecipient cardRecipient = new CardRecipient();
                cardRecipient.setId(cardRecipientArray.getJSONObject(i).optString("id"));

                JSONObject card = cardRecipientArray.getJSONObject(i).getJSONObject("card");
                cardRecipient.setCardNumber(card.optString("cardNumber"));
                cardRecipient.setName(card.optString("name"));
                cardRecipientList.add(cardRecipient);
            }
            getRecipientList.setCardRecipientList(cardRecipientList);

            JSONArray phoneRecipientArray = getRecipientListObj.getJSONArray("phoneRecipientList");
            List<PhoneRecipient> phoneRecipientList = new ArrayList<PhoneRecipient>();
            for (int i = 0; i < phoneRecipientArray.length(); i++) {
                PhoneRecipient phoneRecipient = new PhoneRecipient();
                phoneRecipient.setId(phoneRecipientArray.getJSONObject(i).optString("id"));

                JSONObject phone = phoneRecipientArray.getJSONObject(i).getJSONObject("phone");
                phoneRecipient.setPhoneNumber(phone.optString("phoneNumber"));
                phoneRecipient.setName(phone.optString("name"));
                phoneRecipient.setProviderCode(phone.optString("provider"));
                phoneRecipientList.add(phoneRecipient);
            }
            getRecipientList.setPhoneRecipientList(phoneRecipientList);
        } catch (Exception e) {
            LogManager.e("parseGetRecipientListResponse is error" + e.getLocalizedMessage());
        }
        return getRecipientList;
    }
}
