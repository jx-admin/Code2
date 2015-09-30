
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.ChargeSizeModel;
import com.accenture.mbank.model.GetPendingTransferModel;
import com.accenture.mbank.model.PendingTransferModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetPendingTransferJson {
    /**
     * @param publicModel
     * @return
     */
    public static String getPendingTransferReportProtocal(RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getCardsObj = new JSONObject();
            getCardsObj.put("bankName", publicModel.getBankName());
            getCardsObj.put("channel", publicModel.getChannel());
            getCardsObj.put("customerCode", publicModel.getCustomerCode());
            getCardsObj.put("enterpriseId", publicModel.getEnterpriseId());
            getCardsObj.put("serviceType", ServiceType.getPendingTransfer);
            getCardsObj.put("userAgent", publicModel.getUserAgent());
            getCardsObj.put("sessionId", publicModel.getSessionId());
            getCardsObj.put("token", publicModel.getToken());

            jsonObj.put("GetPendingTransferRequest", getCardsObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("GetPendingTransferRequest is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static GetPendingTransferModel parseGetPendingTransferResponse(String json) {
        GetPendingTransferModel getPendingTransferModel = new GetPendingTransferModel();
        
        List<PendingTransferModel> pendingTransferList = new ArrayList<PendingTransferModel>();
        if (json == null) {
            LogManager.d("ParseGetDashBoardDataResponse" + "json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getPendingTransferObj = jsonObj.getJSONObject("GetPendingTransferResponse");
            
            getPendingTransferModel.responsePublicModel.setResultCode(getPendingTransferObj.getInt("resultCode"));
            getPendingTransferModel.responsePublicModel.setResultDescription(getPendingTransferObj.optString("resultDescription"));
            if (getPendingTransferModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getPendingTransferObj.getJSONObject("eventManagement");
                getPendingTransferModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getPendingTransferModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getPendingTransferModel;
            }
            
            
            getPendingTransferModel.responsePublicModel.setTransactionId(getPendingTransferObj.optString("transactionId"));

            JSONArray getPendingTransferArray = getPendingTransferObj.getJSONArray("pendingTransferList");
            for (int i = 0; i < getPendingTransferArray.length(); i++) {
                PendingTransferModel pendingTransfer = new PendingTransferModel();
                pendingTransfer.setAccountCode(getPendingTransferArray.getJSONObject(i).optString("accountCode"));
                pendingTransfer.setAddress(getPendingTransferArray.getJSONObject(i).optString("address"));
                pendingTransfer.setAmount(getPendingTransferArray.getJSONObject(i).optDouble("amount"));
                pendingTransfer.setBeneficiaryBic(getPendingTransferArray.getJSONObject(i).optString("beneficiaryBic"));
                pendingTransfer.setBeneficiaryCardCode(getPendingTransferArray.getJSONObject(i).optString("beneficiaryCardCode"));
                pendingTransfer.setBeneficiaryCardName(getPendingTransferArray.getJSONObject(i).optString("beneficiaryCardName"));
                pendingTransfer.setBeneficiaryCardNumber(getPendingTransferArray.getJSONObject(i).optString("beneficiaryCardNumber"));
                pendingTransfer.setBeneficiaryCardTitle(getPendingTransferArray.getJSONObject(i).optString("beneficiaryCardTitle"));
                pendingTransfer.setBeneficiaryIban(getPendingTransferArray.getJSONObject(i).optString("beneficiaryIban"));
                pendingTransfer.setBeneficiaryPhoneNumber(getPendingTransferArray.getJSONObject(i).optString("beneficiaryPhoneNumber"));
                pendingTransfer.setBeneficiaryProvider(getPendingTransferArray.getJSONObject(i).optString("beneficiaryProvider"));
                pendingTransfer.setBeneficiaryName(getPendingTransferArray.getJSONObject(i).optString("beneficiaryName"));
                pendingTransfer.setBeneficiaryTitle(getPendingTransferArray.getJSONObject(i).optString("beneficiaryTitle"));
                pendingTransfer.setBillType(getPendingTransferArray.getJSONObject(i).optString("billType"));
                pendingTransfer.setChannel(getPendingTransferArray.getJSONObject(i).optString("channel"));
                pendingTransfer.setCity(getPendingTransferArray.getJSONObject(i).optString("city"));
                pendingTransfer.setCurrency(getPendingTransferArray.getJSONObject(i).optString("currency"));
                pendingTransfer.setCustomerCode(getPendingTransferArray.getJSONObject(i).optString("customerCode"));
                pendingTransfer.setDistrict(getPendingTransferArray.getJSONObject(i).optString("district"));
                pendingTransfer.setExecutionDate(getPendingTransferArray.getJSONObject(i).optString("executionDate"));
                pendingTransfer.setHolderName(getPendingTransferArray.getJSONObject(i).optString("holderName"));
                pendingTransfer.setPostalAccount(getPendingTransferArray.getJSONObject(i).optString("postalAccount"));
                pendingTransfer.setPostalCode(getPendingTransferArray.getJSONObject(i).optString("postalCode"));
                pendingTransfer.setPurposecurrency(getPendingTransferArray.getJSONObject(i).optString("purposecurrency"));
                pendingTransfer.setPurposeDescription(getPendingTransferArray.getJSONObject(i).optString("purposeDescription"));
                pendingTransfer.setSender(getPendingTransferArray.getJSONObject(i).optString("sender"));
                pendingTransfer.setTransferId(getPendingTransferArray.getJSONObject(i).optString("transferId"));
                pendingTransfer.setTransferType(getPendingTransferArray.getJSONObject(i).optString("transferType"));
                pendingTransfer.setType(getPendingTransferArray.getJSONObject(i).optString("type"));
                pendingTransfer.setBillNumber(getPendingTransferArray.getJSONObject(i).optString("billNumber"));
                
                ChargeSizeModel chargeSizeModel = new ChargeSizeModel();
                JSONObject chargeSizeObj = getPendingTransferArray.getJSONObject(i).optJSONObject("chargeSize");
                chargeSizeModel.setCommissionAmount(chargeSizeObj.optString("commissionAmount"));
                chargeSizeModel.setRechargeAmount(chargeSizeObj.optString("rechargeAmount"));
                chargeSizeModel.setRechargeDescription(chargeSizeObj.optString("rechargeDescription"));
                pendingTransfer.setChargeSizeModel(chargeSizeModel);
                
                pendingTransferList.add(pendingTransfer);
            }
            getPendingTransferModel.setPendingTransferList(pendingTransferList);
        } catch (Exception e) {
            LogManager.e("parseGetPendingTransferResponse is error" + e.getMessage());
        }
        return getPendingTransferModel;
    }
}
