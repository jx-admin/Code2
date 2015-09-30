
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.model.RecentTransferResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class RecentTransferJson {
    public static String RecentTransferReportProtocal(RequestPublicModel publicModel,
            String accountCode, int recentPaymentDislayed) {
        String resultStr = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecenTransferObj = new JSONObject();

            getRecenTransferObj.put("serviceType", ServiceType.getRecentTransfer);
            getRecenTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecenTransferObj.put("bankName", publicModel.getBankName());
            getRecenTransferObj.put("customerCode", publicModel.getCustomerCode());
            getRecenTransferObj.put("channel", publicModel.getChannel());
            getRecenTransferObj.put("userAgent", publicModel.getUserAgent());
            getRecenTransferObj.put("accountCode", accountCode);
            getRecenTransferObj.put("numberOfTnx", recentPaymentDislayed);
            getRecenTransferObj.put("sessionId", publicModel.getSessionId());
            getRecenTransferObj.put("token", publicModel.getToken());

            jsonObj.put("GetRecentTransferRequest", getRecenTransferObj);

            resultStr = jsonObj.toString();
        } catch (Exception e) {
            LogManager.d("RecentTransferReportProtocal id error" + e.getLocalizedMessage());
        }
        return resultStr;
    }

    public static RecentTransferResponseModel ParseRecentTransferResponse(String json) {
        RecentTransferResponseModel recentTransferResponse = new RecentTransferResponseModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject recentTransferObj = jsonObj.getJSONObject("GetRecentTransferResponse");

            recentTransferResponse.responsePublicModel.setResultCode(recentTransferObj
                    .getInt("resultCode"));
            recentTransferResponse.responsePublicModel.setResultDescription(recentTransferObj
                    .optString("resultDescription"));
            if (recentTransferResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = recentTransferObj.getJSONObject("eventManagement");
                recentTransferResponse.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                recentTransferResponse.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return recentTransferResponse;
            }
            recentTransferResponse.responsePublicModel.setTransactionId(recentTransferObj
                    .optString("transactionId"));

            List<RecentTransferModel> recentTransferList = new ArrayList<RecentTransferModel>();
            JSONArray recentTransferArray = recentTransferObj.getJSONArray("recentTransferList");
            for (int i = 0; i < recentTransferArray.length(); i++) {
                RecentTransferModel recentTransfer = new RecentTransferModel();
                recentTransfer.setType(recentTransferArray.getJSONObject(i).optString("type"));
                recentTransfer.setOperationDate(recentTransferArray.getJSONObject(i).optString("operationDate"));
                if (recentTransfer.getType().equals(Contants.PREPAID_CARD_RELOAD)) {
                    recentTransfer.setBeneficiaryAccount(recentTransferArray.getJSONObject(i).optString("beneficiaryAccount"));
                    recentTransfer.setBeneficiaryCardNumber(recentTransferArray.getJSONObject(i).optString("beneficiaryCardNumber"));
                    recentTransfer.setBeneficiaryName(recentTransferArray.getJSONObject(i).optString("beneficiaryName"));
                } else if (recentTransfer.getType().equals(Contants.SIM_TOP_UP)) {
                    // SIM　TOP　UP
                    recentTransfer.setBeneficiaryNumber(recentTransferArray.getJSONObject(i).optString("beneficiaryNumber"));
                    recentTransfer.setBeneficiaryProvider(recentTransferArray.getJSONObject(i).optString("beneficiaryProvider"));
                    recentTransfer.setTransferState(recentTransferArray.getJSONObject(i).optString("transferState"));
                } else if (recentTransfer.getType().equals(Contants.BANK_TRANSFER) || recentTransfer.getType().equals(Contants.TRANSFER_ENTRY)) {
                    recentTransfer.setBeneficiaryName(recentTransferArray.getJSONObject(i).optString("beneficiaryName"));
                    recentTransfer.setBeneficiaryIban(recentTransferArray.getJSONObject(i).optString("beneficiaryIban"));
                    recentTransfer.setBeneficiaryState(recentTransferArray.getJSONObject(i).optString("beneficiaryState"));
                    recentTransfer.setTransferState(recentTransferArray.getJSONObject(i).optString("transferState"));
                    try {
                        recentTransfer.setPurposeCurrency(recentTransferArray.getJSONObject(i).optString("purposeCurrency"));
                    } catch (Exception e) {
                        recentTransfer.setPurposeCurrency("");
                    }
                }else if(recentTransfer.getType().equals(Contants.BILL_PAYMENT)){
                    recentTransfer.setBillType(recentTransferArray.getJSONObject(i).optString("billType")); // Value from the BILL_TYPE table. E.g. “123”, “451”,”896”, “674”
                    recentTransfer.setTransferState(recentTransferArray.getJSONObject(i).optString("transferState"));
                    recentTransfer.setPostalAccount(recentTransferArray.getJSONObject(i).optString("postalAccount"));
                    recentTransfer.setHolderName(recentTransferArray.getJSONObject(i).optString("holderName"));
                    recentTransfer.setSender(recentTransferArray.getJSONObject(i).optString("sender"));
                    recentTransfer.setAddress(recentTransferArray.getJSONObject(i).optString("address"));
                    recentTransfer.setCity(recentTransferArray.getJSONObject(i).optString("city"));
                    recentTransfer.setDistrict(recentTransferArray.getJSONObject(i).optString("district"));
                    recentTransfer.setPostalCode(recentTransferArray.getJSONObject(i).optString("postalCode"));
                }

                recentTransfer.setAccount(recentTransferArray.getJSONObject(i).optString("account"));
                recentTransfer.setAmount(recentTransferArray.getJSONObject(i).optDouble("amount"));
                recentTransfer.setDescription(recentTransferArray.getJSONObject(i).optString("description"));
                recentTransferList.add(recentTransfer);
            }
            recentTransferResponse.setRecentTransferList(recentTransferList);
        } catch (Exception e) {
            LogManager.d("ParseRecentTransferResponse is error" + e.getLocalizedMessage());
        }

        return recentTransferResponse;
    }
}
