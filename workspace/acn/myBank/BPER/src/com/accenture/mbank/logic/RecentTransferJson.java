
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.model.RecentTransferResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
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
            getRecenTransferObj.put("customerNumber", publicModel.getCustomerNumber());
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

		/*
		 * print long string
		 */
		int maxLogSize = 4000;
		for (int i = 0; i <= json.length() / maxLogSize; i++) {
			int start = i * maxLogSize;
			int end = (i + 1) * maxLogSize;
			end = end > json.length() ? json.length() : end;
			LogManager.i(json.substring(start, end));
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
            JSONObject recentTransfers = recentTransferObj.getJSONObject("recentTransfers");

			String[] strTypeArray = { "recentBankTransferList",
					"recentTransferEntryList", "recentSimTopUpList",
					"recentPrepaidCardRechargeList" };

            for (String strType:strTypeArray) {
                JSONArray recentTransferArray = recentTransfers.getJSONArray(strType);

                for (int i = 0; i < recentTransferArray.length(); i++) {
                	RecentTransferModel recentTransfer = new RecentTransferModel();
					/*
					 * Common field
					 */
                	recentTransfer.setOperationDate(recentTransferArray.getJSONObject(i).optString("date"));
                	recentTransfer.setAccount(recentTransferArray.getJSONObject(i).optString("account"));
                	recentTransfer.setType(recentTransferArray.getJSONObject(i).optString("type"));
                	recentTransfer.setAmount(recentTransferArray.getJSONObject(i).optDouble("amount"));
                	recentTransfer.setDescription(recentTransferArray.getJSONObject(i).optString("description"));
                	recentTransfer.setTransferState(recentTransferArray.getJSONObject(i).optString("transferState"));

					/*
					 * BANK_TRANSFER
					 */
                	recentTransfer.setBeneficiaryName(recentTransferArray.getJSONObject(i).optString("beneficiaryName"));
                	recentTransfer.setBeneficiaryIban(recentTransferArray.getJSONObject(i).optString("beneficiaryIban"));
                	recentTransfer.setPurposeCurrency(recentTransferArray.getJSONObject(i).optString("purposeCurrency"));
                	recentTransfer.setBeneficiaryState(recentTransferArray.getJSONObject(i).optString("beneficiaryState"));
                	recentTransfer.setBeneficiaryBic(recentTransferArray.getJSONObject(i).optString("beneficiaryBic"));

					/*
					 * TRANSFER ENTRY
					 */
                	recentTransfer.setBeneficiaryAccount(recentTransferArray.getJSONObject(i).optString("beneficiaryAccount"));
                
					/*
					 * SIM　TOP　UP
					 */
                	recentTransfer.setBeneficiaryName(recentTransferArray.getJSONObject(i).optString("beneficiaryName"));
                	recentTransfer.setBeneficiaryNumber(recentTransferArray.getJSONObject(i).optString("beneficiaryNumber"));
                	recentTransfer.setBeneficiaryProvider(recentTransferArray.getJSONObject(i).optString("beneficiaryProvider"));

					/*
					 * CARD RECHARGE
					 */
                	recentTransfer.setBeneficiaryName(recentTransferArray.getJSONObject(i).optString("beneficiaryName"));
                	recentTransfer.setBeneficiaryCardNumber(recentTransferArray.getJSONObject(i).optString("beneficiaryCardNumber"));

					/*
					 * Add to list
					 */
                	recentTransferList.add(recentTransfer);
                }
            }

            recentTransferResponse.setRecentTransferList(recentTransferList);
        } catch (Exception e) {
            LogManager.d("ParseRecentTransferResponse is error" + e.getLocalizedMessage());
        }

        return recentTransferResponse;
    }
}
