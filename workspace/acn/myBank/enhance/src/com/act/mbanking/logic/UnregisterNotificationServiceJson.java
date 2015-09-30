package com.act.mbanking.logic;

import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.utils.LogManager;

public class UnregisterNotificationServiceJson {
    /**
     * @param publicModel
     * @return
     */
    public static String unregisterNotificationServiceReportProtocal(RequestPublicModel publicModel,
            String pushMessage) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject unregisterNotificationServiceObj = new JSONObject();
            unregisterNotificationServiceObj.put("serviceType",ServiceType.REGISTERNOTIFICATIONSERVICE);
            unregisterNotificationServiceObj.put("enterpriseId", publicModel.getEnterpriseId());
            unregisterNotificationServiceObj.put("bankName", publicModel.getBankName());
            unregisterNotificationServiceObj.put("applicationId", "");
            unregisterNotificationServiceObj.put("token", publicModel.getBankName());
            unregisterNotificationServiceObj.put("channel", publicModel.getChannel());
            unregisterNotificationServiceObj.put("userAgent", publicModel.getUserAgent());
            unregisterNotificationServiceObj.put("sessionId", publicModel.getSessionId());
            unregisterNotificationServiceObj.put("customerCode",publicModel.getCustomerCode());
            
            jsonObj.put("UnunregisterNotificationServiceObj", unregisterNotificationServiceObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("unregisterNotificationServiceReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    /**
     * @param json
     * @return
     */
    public static ResponsePublicModel ParseUnregisterNotificationServiceResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject advNewsObj = jsonObj.getJSONObject("UnregisterNotificationServiceResponse");
            responsePublicModel.setResultCode(advNewsObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(advNewsObj.optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = advNewsObj.getJSONObject("eventManagement");
                responsePublicModel.eventManagement.setErrorCode(eventManagementObj
                        .optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(eventManagementObj
                        .optString("errorDescription"));
                return responsePublicModel;
            }

            responsePublicModel.setTransactionId(advNewsObj.optString("transactionId"));
        } catch (Exception e) {
            LogManager.e("ParseUnregisterNotificationServiceResponse is error"
                    + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
