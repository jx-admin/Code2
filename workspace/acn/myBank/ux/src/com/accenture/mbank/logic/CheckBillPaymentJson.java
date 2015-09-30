
package com.accenture.mbank.logic;

import org.json.JSONObject;

import com.accenture.mbank.model.CheckBillPaymentResponseModel;
import com.accenture.mbank.model.CheckBillPaymentValueModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class CheckBillPaymentJson {
    public static String CheckBillPaymentReportProtocal(RequestPublicModel publicModel,
            CheckBillPaymentValueModel checkBillPaymentValue) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject checkBillPaymentObj = new JSONObject();
            checkBillPaymentObj.put("serviceType", ServiceType.CHECKBILLPAYMENT);
            checkBillPaymentObj.put("enterpriseId", publicModel.getEnterpriseId());
            checkBillPaymentObj.put("bankName", publicModel.getBankName());
            checkBillPaymentObj.put("token", publicModel.getToken());
            checkBillPaymentObj.put("customerCode", publicModel.getCustomerCode());
            checkBillPaymentObj.put("channel", publicModel.getChannel());
            checkBillPaymentObj.put("userAgent", publicModel.getUserAgent());
            checkBillPaymentObj.put("sessionId", publicModel.getSessionId());
            checkBillPaymentObj.put("accountCode", checkBillPaymentValue.getAccountCode());
            checkBillPaymentObj.put("billType", checkBillPaymentValue.getBillType());
            checkBillPaymentObj.put("amount", checkBillPaymentValue.getAmount());
            checkBillPaymentObj.put("currency", checkBillPaymentValue.getCurrency());
            checkBillPaymentObj.put("postalAccount", checkBillPaymentValue.getPostalAccount());
            checkBillPaymentObj.put("holderName", checkBillPaymentValue.getHolderName());
            // Opt
            checkBillPaymentObj.put("purposeDescription",
                    checkBillPaymentValue.getPurposeDescription());
            checkBillPaymentObj.put("transferId", checkBillPaymentValue.getTransferId());
            checkBillPaymentObj.put("transferCheck", checkBillPaymentValue.getTransferCheck());
            checkBillPaymentObj.put("billNumber", checkBillPaymentValue.getBillNumber());
            checkBillPaymentObj.put("beneficiaryName", checkBillPaymentValue.getBeneficiaryName());
            checkBillPaymentObj.put("address", checkBillPaymentValue.getAddress());
            checkBillPaymentObj.put("city", checkBillPaymentValue.getCity());
            checkBillPaymentObj.put("district", checkBillPaymentValue.getDistrict());
            checkBillPaymentObj.put("postalCode", checkBillPaymentValue.getPostalCode());
            checkBillPaymentObj.put("dueDate", checkBillPaymentValue.getDueDate());
            
            checkBillPaymentObj.put("sender", checkBillPaymentValue.getHolderName());
            if(Contants.isNewPaymentsUpdate){
            	checkBillPaymentObj.put("transferCheck", true);
            	checkBillPaymentObj.put("transactionId", checkBillPaymentValue.getTransferId());
            }

            jsonObj.put("CheckBillPaymentRequest", checkBillPaymentObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("CheckBillPaymentReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static CheckBillPaymentResponseModel ParseCheckBillPaymentResponse(String json) {
        CheckBillPaymentResponseModel checkBillPaymentResponseModel = new CheckBillPaymentResponseModel();
        CheckBillPaymentValueModel checkBillPaymentValue = new CheckBillPaymentValueModel();
        if (json == null) {
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject checkBillPaymentObj = jsonObj.getJSONObject("CheckBillPaymentResponse");
            checkBillPaymentResponseModel.responsePublicModel.setResultCode(checkBillPaymentObj.getInt("resultCode"));
            checkBillPaymentResponseModel.responsePublicModel.setResultDescription(checkBillPaymentObj.optString("resultDescription"));
            if (checkBillPaymentResponseModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = checkBillPaymentObj.getJSONObject("eventManagement");
                checkBillPaymentResponseModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                checkBillPaymentResponseModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return checkBillPaymentResponseModel;
            }
            checkBillPaymentResponseModel.responsePublicModel.setTransactionId(checkBillPaymentObj.optString("transactionId"));
            checkBillPaymentValue.setTransferId(checkBillPaymentObj.optString("transferId"));
            checkBillPaymentValue.setCharges(checkBillPaymentObj.optDouble("charges"));
            checkBillPaymentValue.setPostalCharges(checkBillPaymentObj.optDouble("postalCharges"));
            checkBillPaymentValue.setAmount(checkBillPaymentObj.optDouble("amount"));
            checkBillPaymentValue.setPostalAccount(checkBillPaymentObj.optString("postalAccount"));
            checkBillPaymentValue.setHolderName(checkBillPaymentObj.optString("holderName"));

            if(Contants.isNewPaymentsUpdate){
            	checkBillPaymentResponseModel.setTransferId(checkBillPaymentObj.optString("transferId"));
            }
            checkBillPaymentResponseModel.setCheckBillPaymentValueModel(checkBillPaymentValue);

        } catch (Exception e) {
            LogManager.e("ParseCheckBillPaymentResponse is error" + e.getLocalizedMessage());
        }
        return checkBillPaymentResponseModel;
    }

}
