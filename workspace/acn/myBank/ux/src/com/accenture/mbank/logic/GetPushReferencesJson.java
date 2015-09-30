
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.act.mbanking.ServiceType;
import com.accenture.mbank.model.GetPushReferencesModel;
import com.accenture.mbank.model.PushCategoryModel;
import com.accenture.mbank.model.PushSettingModel;
import com.accenture.mbank.model.RequestPublicModel;
//import com.act.mbanking.utils.LogManager;

public class GetPushReferencesJson {
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
    public static String GetPushPreferencesReportProtocal(RequestPublicModel publicModel,String applicationId) {
        String resultStr = null;
        try {
            JSONObject JsonObj = new JSONObject();
            JSONObject getPushPreferencesRequest = new JSONObject();
            getPushPreferencesRequest.put("serviceType", "2094");
            getPushPreferencesRequest.put("enterpriseId", publicModel.getEnterpriseId());
            getPushPreferencesRequest.put("bankName", publicModel.getBankName());
            getPushPreferencesRequest.put("applicationId",applicationId);
            getPushPreferencesRequest.put("token", publicModel.getBankName());
            getPushPreferencesRequest.put("channel", publicModel.getChannel());
            getPushPreferencesRequest.put("userAgent", publicModel.getUserAgent());
            getPushPreferencesRequest.put("sessionId", publicModel.getSessionId());
            getPushPreferencesRequest.put("customerCode", publicModel.getCustomerCode());

            JsonObj.put("GetPushPreferencesRequest", getPushPreferencesRequest);
            resultStr = JsonObj.toString();
        } catch (Exception e) {
          //  LogManager.e("GetPushPreferencesReportProtocal is error :" + e.getLocalizedMessage());
        }
      //  LogManager.d("GetPushPreferencesReportProtocal " + resultStr);
        return resultStr;
    }

    public static GetPushReferencesModel ParseGetPushPreferencesResponse(String json) {
        GetPushReferencesModel getPushPreferencesModel = new GetPushReferencesModel();
        
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getPushPreferencesObj = jsonObj.getJSONObject("GetPushPreferencesResponse");
            getPushPreferencesModel.responsePublicModel.setResultCode(getPushPreferencesObj.getInt("resultCode"));
            getPushPreferencesModel.responsePublicModel.setResultDescription(getPushPreferencesObj.optString("resultDescription"));
            if (getPushPreferencesModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getPushPreferencesObj.getJSONObject("eventManagement");
                getPushPreferencesModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getPushPreferencesModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getPushPreferencesModel;
            }

            getPushPreferencesModel.responsePublicModel.setTransactionId(getPushPreferencesObj.optString("transactionId"));
            JSONArray pushCategoryArray = getPushPreferencesObj.optJSONArray("pushCategoryList");
            List<PushCategoryModel> pushCategoryList = new ArrayList<PushCategoryModel>();
            for(int i=0;i<pushCategoryArray.length();i++){
                PushCategoryModel pushCategoryModel = new PushCategoryModel(); 
                pushCategoryModel.setCategory(pushCategoryArray.getJSONObject(i).optString("category"));
                
                JSONArray pushSettingArray = pushCategoryArray.optJSONObject(i).optJSONArray("pushSettingList");
                List<PushSettingModel> pushSettingList = new ArrayList<PushSettingModel>();
                for(int j=0;j<pushSettingArray.length();j++){
                    PushSettingModel pushsettingModel = new PushSettingModel();
                    pushsettingModel.setAvailable(pushSettingArray.optJSONObject(j).optInt("available"));
                    pushsettingModel.setPushMessageType(pushSettingArray.optJSONObject(j).optInt("pushMessageType"));
                    pushsettingModel.setPushSetting(pushSettingArray.optJSONObject(j).optInt("pushSetting"));
                    pushsettingModel.setPushDescription(pushSettingArray.optJSONObject(j).optString("pushDescription"));
                    pushSettingList.add(pushsettingModel);
                }
                pushCategoryModel.setPushSettingList(pushSettingList);
                pushCategoryList.add(pushCategoryModel);
            }
            getPushPreferencesModel.setPustCategorList(pushCategoryList);
        } catch (Exception e) {
           // LogManager.e("ParseGetPushPreferencesResponse is error" + e.getLocalizedMessage());
        }
        return getPushPreferencesModel;
    }
}
