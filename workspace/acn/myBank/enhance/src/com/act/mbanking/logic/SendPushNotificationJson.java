
package com.act.mbanking.logic;

import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.utils.LogManager;

public class SendPushNotificationJson {

    /**
     * @param publicModel
     * @return
     */
    public static String sendPushNotificationReportProtocal(RequestPublicModel publicModel,
            String pushMessage) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject sendPushNotificationObj = new JSONObject();
            sendPushNotificationObj.put("serviceType", ServiceType.SENDPUSHNOTIFICATION);
            sendPushNotificationObj.put("enterpriseId", publicModel.getEnterpriseId());
            sendPushNotificationObj.put("bankName", publicModel.getBankName());
            sendPushNotificationObj.put("token", publicModel.getToken());
            sendPushNotificationObj.put("channel", publicModel.getChannel());
            sendPushNotificationObj.put("userAgent", publicModel.getUserAgent());
            sendPushNotificationObj.put("sessionId", publicModel.getSessionId());
            sendPushNotificationObj.put("customerCode", publicModel.getCustomerCode());
            sendPushNotificationObj.put("pushMessage", pushMessage);
            sendPushNotificationObj.put("pushTitle", pushMessage);

            jsonObj.put("SendPushNotificationRequest", sendPushNotificationObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("SendPushNotificationRequest is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static ResponsePublicModel ParseSendPushNotificationResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject advNewsObj = jsonObj.getJSONObject("SendPushNotificationResponse");
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
            LogManager.e("ParseSendPushNotificationResponse is error" + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
