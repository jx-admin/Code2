
package com.accenture.mbank.logic;

import org.json.JSONObject;

import com.accenture.mbank.model.CheckBillPaymentValueModel;
import com.accenture.mbank.model.InsertBillPaymentModel;
import com.accenture.mbank.model.OtpStateModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class InsertBillPaymentJson {
    /**
     * 
     * @param publicModel
     * @param checkBillPaymentValue
     * @param optKeySession
     * @param otpValue
     * @param otpchannel
     * @return
     */
    public static String InsertBillPaymentReportProtocal(RequestPublicModel publicModel,CheckBillPaymentValueModel checkBillPaymentValue,String optKeySession,String otpValue,String otpchannel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject insterBillPaymentObj = new JSONObject();
            insterBillPaymentObj.put("serviceType", ServiceType.INSERTBILLPAYMENT);
            insterBillPaymentObj.put("enterpriseId", publicModel.getEnterpriseId());
            insterBillPaymentObj.put("bankName", publicModel.getBankName());
            insterBillPaymentObj.put("token", publicModel.getToken());
            insterBillPaymentObj.put("customerCode", publicModel.getCustomerCode());
            insterBillPaymentObj.put("channel", publicModel.getChannel());
            insterBillPaymentObj.put("userAgent", publicModel.getUserAgent());
            insterBillPaymentObj.put("sessionId", publicModel.getSessionId());
            insterBillPaymentObj.put("accountCode", checkBillPaymentValue.getAccountCode());
            insterBillPaymentObj.put("billType", checkBillPaymentValue.getBillType());
            insterBillPaymentObj.put("amount", checkBillPaymentValue.getAmount());
            insterBillPaymentObj.put("currency", checkBillPaymentValue.getCurrency());
            insterBillPaymentObj.put("postalAccount", checkBillPaymentValue.getPostalAccount());
            insterBillPaymentObj.put("holderName", checkBillPaymentValue.getHolderName());
            insterBillPaymentObj.put("billNumber",checkBillPaymentValue.getBillNumber());
            insterBillPaymentObj.put("sender",checkBillPaymentValue.getHolderName());
            insterBillPaymentObj.put("otpKeySession", optKeySession);
            insterBillPaymentObj.put("otpValue", otpValue);
            insterBillPaymentObj.put("otpChannel", otpchannel);
            
            // Opt
            insterBillPaymentObj.put("purposeDescription",checkBillPaymentValue.getPurposeDescription());
            insterBillPaymentObj.put("transferId", checkBillPaymentValue.getTransferId());
            insterBillPaymentObj.put("transferCheck", checkBillPaymentValue.getTransferCheck());
            insterBillPaymentObj.put("billNumber", checkBillPaymentValue.getBillNumber());
            insterBillPaymentObj.put("beneficiaryName", checkBillPaymentValue.getBeneficiaryName());
            insterBillPaymentObj.put("address", checkBillPaymentValue.getAddress());
            insterBillPaymentObj.put("city", checkBillPaymentValue.getCity());
            insterBillPaymentObj.put("district", checkBillPaymentValue.getDistrict());
            insterBillPaymentObj.put("postalCode", checkBillPaymentValue.getPostalCode());
            insterBillPaymentObj.put("dueDate", checkBillPaymentValue.getDueDate());
            
            jsonObj.put("InsertBillPaymentRequest", insterBillPaymentObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("InsertBillPaymentReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 
     * @param json
     * @return
     */
    public static InsertBillPaymentModel ParseCheckBillPaymentResponse(String json) {
        InsertBillPaymentModel insterBillPaymentResponseModel = new InsertBillPaymentModel();
        OtpStateModel otpStateModel = new OtpStateModel();
        
        if (json == null) {
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject insterBillPaymentObj = jsonObj.getJSONObject("InsertBillPaymentResponse");
            insterBillPaymentResponseModel.responsePublicModel.setResultCode(insterBillPaymentObj.getInt("resultCode"));
            insterBillPaymentResponseModel.responsePublicModel.setResultDescription(insterBillPaymentObj.optString("resultDescription"));
            if (insterBillPaymentResponseModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = insterBillPaymentObj.getJSONObject("eventManagement");
                insterBillPaymentResponseModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                insterBillPaymentResponseModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return insterBillPaymentResponseModel;
            }
            insterBillPaymentResponseModel.responsePublicModel.setTransactionId(insterBillPaymentObj.optString("transactionId"));
            insterBillPaymentResponseModel.setTransferId(insterBillPaymentObj.optString("transferId"));
            
            JSONObject otpState = insterBillPaymentObj.optJSONObject("otpState");
            otpStateModel.setOtpAvailable(otpState.optInt("otpAvailable"));
            otpStateModel.setOtpErrorCode(otpState.optInt("otpErrorCode"));
            otpStateModel.setOtpErrorDescription(otpState.optString("otpErrorDescription"));
            otpStateModel.setOtpKeySession(otpState.optString("otpKeySession"));
            
            insterBillPaymentResponseModel.setOtpStateModel(otpStateModel);

        } catch (Exception e) {
            LogManager.e("ParseCheckBillPaymentResponse is error" + e.getLocalizedMessage());
        }
        return insterBillPaymentResponseModel;
    }

}
