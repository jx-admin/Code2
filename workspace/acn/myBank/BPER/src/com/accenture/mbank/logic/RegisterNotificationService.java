
package com.accenture.mbank.logic;

import org.json.JSONObject;

import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class RegisterNotificationService {

    /**
     * RegisterNotificationServiceReportProtocal
     * 
     * @param serviceType
     * @param bankName
     * @param enterprisedId
     * @param channel
     * @param userAgent
     * @param username
     * @param password
     * @return
     */
    public static String RegisterNotificationServiceReportProtocal(RequestPublicModel publicModel,String applicationId,String apnsToken) {
        String resultStr = null;
        try {
            JSONObject JsonObj = new JSONObject();
            JSONObject registerNotificationServiceRequest = new JSONObject();
            registerNotificationServiceRequest.put("serviceType",ServiceType.REGISTERNOTIFICATIONSERVICE);
            registerNotificationServiceRequest.put("enterpriseId", publicModel.getEnterpriseId());
            registerNotificationServiceRequest.put("bankName", publicModel.getBankName());
            registerNotificationServiceRequest.put("applicationId", applicationId);
            registerNotificationServiceRequest.put("token", publicModel.getToken());
            registerNotificationServiceRequest.put("channel", publicModel.getChannel());
            registerNotificationServiceRequest.put("userAgent", publicModel.getUserAgent());
            registerNotificationServiceRequest.put("sessionId", publicModel.getSessionId());
            registerNotificationServiceRequest.put("customerNumber", publicModel.getCustomerNumber());
            registerNotificationServiceRequest.put("APNSToken", apnsToken);
            
            JsonObj.put("RegisterNotificationServiceRequest", registerNotificationServiceRequest);
            resultStr = JsonObj.toString();
        } catch (Exception e) {
            LogManager.e("RegisterNotificationServiceReportProtocal is error :" + e.getLocalizedMessage());
        }
        LogManager.d("RegisterNotificationServiceReportProtocal " + resultStr);
        return resultStr;
    }

    /**
     * @param json
     * @return
     */
    public static ResponsePublicModel ParseRegisterNotificationServiceResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject advNewsObj = jsonObj.getJSONObject("RegisterNotificationServiceResponse");
            responsePublicModel.setResultCode(advNewsObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(advNewsObj.optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = advNewsObj.getJSONObject("eventManagement");
                responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return responsePublicModel;
            }

            responsePublicModel.setTransactionId(advNewsObj.optString("transactionId"));
        } catch (Exception e) {
            LogManager.e("ParseRegisterNotificationServiceResponse is error"+ e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
