
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.accenture.mbank.ServiceType;
import com.accenture.mbank.model.PushSettingModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ResponsePublicModel;
//import com.accenture.mbank.utils.LogManager;

public class SetPushReferencesJson {
    /**
     * @param publicModel
     * @return
     */
    public static String setPushPreferencesReportProtocal(RequestPublicModel publicModel,
            List<PushSettingModel> pushsettinglist) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject setPushNotificationObj = new JSONObject();
            setPushNotificationObj.put("serviceType", "2093");
            setPushNotificationObj.put("enterpriseId", publicModel.getEnterpriseId());
            setPushNotificationObj.put("bankName", publicModel.getBankName());
            setPushNotificationObj.put("token", publicModel.getToken());
            setPushNotificationObj.put("channel", publicModel.getChannel());
            setPushNotificationObj.put("userAgent", publicModel.getUserAgent());
            setPushNotificationObj.put("sessionId", publicModel.getSessionId());
            setPushNotificationObj.put("customerCode", publicModel.getCustomerCode());
            // 添加list
            JSONArray pushSettingArray = new JSONArray();
            for (int i = 0; i < pushsettinglist.size(); i++) {
                JSONObject pushSetting = new JSONObject();
                pushSetting.put("pushMessageType", pushsettinglist.get(i).getPushMessageType());
                pushSetting.put("pushSetting", pushsettinglist.get(i).getPushSetting());
                pushSettingArray.put(i, pushSetting);
            }

            setPushNotificationObj.put("pushSettingList", pushSettingArray);
            jsonObj.put("SetPushPreferencesRequest", setPushNotificationObj);
            result = jsonObj.toString();
        } catch (Exception e) {
          //  LogManager.e("SetPushPreferencesReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    public static ResponsePublicModel ParseSetPushPreferencesResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject advNewsObj = jsonObj.getJSONObject("SetPushPreferencesResponse");
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
         //   LogManager.e("ParseSetPushPreferencesResponse is error" + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
