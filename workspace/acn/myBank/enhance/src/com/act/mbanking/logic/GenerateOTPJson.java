
package com.act.mbanking.logic;

import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.GenerateOTPResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GenerateOTPJson {

    /**
     * @param serviceType
     * @param bankName
     * @param enterpriseId
     * @param customerCode
     * @param channel
     * @param otpChannel
     * @param userAgent
     * @param sessionId
     * @return
     */
    public static String GenerateOTPReportProtocal(String otpChannel, RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject generateOTPRequestObj = new JSONObject();

            generateOTPRequestObj.put("serviceType", ServiceType.generateOTP);
            generateOTPRequestObj.put("bankName", publicModel.getBankName());
            generateOTPRequestObj.put("enterpriseId", publicModel.getEnterpriseId());
            generateOTPRequestObj.put("customerCode", publicModel.getCustomerCode());
            generateOTPRequestObj.put("channel", publicModel.getChannel());
            generateOTPRequestObj.put("otpChannel", otpChannel);
            generateOTPRequestObj.put("userAgent", publicModel.getUserAgent());
            generateOTPRequestObj.put("sessionId", publicModel.getSessionId());
            generateOTPRequestObj.put("token", publicModel.getToken());

            jsonObj.put("GenerateOTPRequest", generateOTPRequestObj);

            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("GenerateOTPReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static GenerateOTPResponseModel ParseGenerateOTPResponse(String json) {
        
        GenerateOTPResponseModel generateOTPResponse = new GenerateOTPResponseModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject generateOTPObj = jsonObj.getJSONObject("GenerateOTPResponse");
            
            generateOTPResponse.responsePublicModel.setResultCode(generateOTPObj.optInt("resultCode"));
            generateOTPResponse.responsePublicModel.setResultDescription(generateOTPObj.optString("resultDescription"));
            if (generateOTPResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = generateOTPObj.getJSONObject("eventManagement");
                generateOTPResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                generateOTPResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return generateOTPResponse;
            }
            
            generateOTPResponse.responsePublicModel.setTransactionId(generateOTPObj.optString("transactionId"));

            JSONObject otpStateObj = generateOTPObj.getJSONObject("otpState");
            generateOTPResponse.setOtpAvailable(otpStateObj.optString("otpAvailable"));
            generateOTPResponse.setOtpErrorDescription(otpStateObj.optString("otpErrorDescription"));
            generateOTPResponse.setOtpKeySession(otpStateObj.optString("otpKeySession"));
            
        }catch(Exception e){
            LogManager.e("GenerateOTPReportProtocal is error" + e.getLocalizedMessage());
        }
        
        return generateOTPResponse;
    }
}
