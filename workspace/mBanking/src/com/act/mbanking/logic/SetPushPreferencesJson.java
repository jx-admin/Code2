
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.PushSettingModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.utils.LogManager;

public class SetPushPreferencesJson {
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
            setPushNotificationObj.put("serviceType", ServiceType.SETPUSHPREFERENCES);
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
            LogManager.e("SetPushPreferencesReportProtocal is error" + e.getLocalizedMessage());
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
            LogManager.e("ParseSetPushPreferencesResponse is error" + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
