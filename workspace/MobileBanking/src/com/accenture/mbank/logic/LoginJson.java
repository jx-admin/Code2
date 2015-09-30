
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.EntryModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.LoginResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class LoginJson {

    /**
     * LoginRequest
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
    public static String LoginReportProtocal(RequestPublicModel publicModel, String username,
            String password) {
        String resultStr = null;
        try {
            JSONObject JsonObj = new JSONObject();
            JSONObject loginRequest = new JSONObject();
            loginRequest.put("serviceType", ServiceType.login);
            loginRequest.put("bankName", publicModel.getBankName());
            loginRequest.put("enterpriseId", publicModel.getEnterpriseId());
            loginRequest.put("channel", publicModel.getChannel());
            loginRequest.put("userAgent", publicModel.getUserAgent());
            loginRequest.put("username", username);
            loginRequest.put("password", password);
            JsonObj.put("LoginRequest", loginRequest);
            resultStr = JsonObj.toString();
        } catch (Exception e) {
            LogManager.e("LoginReportProtocal is error :" + e.getLocalizedMessage());
        }
        LogManager.d("LoginReportProtocal " + resultStr);
        return resultStr;
    }

    /**
     * 解析登录结果
     * 
     * @param json
     */
    public static LoginResponseModel ParseLoginResponse(String json) {
        LoginResponseModel loginResponseMode = new LoginResponseModel();
        EntryModel entryModel = new EntryModel();
        List<Object> fieldMap = new ArrayList<Object>();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject loginResponse = jsonObj.getJSONObject("LoginResponse");
            
            loginResponseMode.responsePublicModel.setResultCode(loginResponse.getInt("resultCode"));
            loginResponseMode.responsePublicModel.setResultDescription(loginResponse.optString("resultDescription"));
            if (loginResponseMode.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = loginResponse.getJSONObject("eventManagement");
                loginResponseMode.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                loginResponseMode.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return loginResponseMode;
            }
            
            loginResponseMode.setSessionId(loginResponse.optString("sessionId"));
            Contants.publicModel.setSessionId(loginResponseMode.getSessionId());
            loginResponseMode.setCustomerCode(loginResponse.optString("customerCode"));
            Contants.publicModel.setCustomerCode(loginResponseMode.getCustomerCode());
            
            loginResponseMode.responsePublicModel.setTransactionId(loginResponse.optString("transactionId"));
            
            JSONObject fieldMapObj = loginResponse.getJSONObject("fieldMap");
            JSONArray entryArray = fieldMapObj.getJSONArray("entry");
            for (int i = 0; i < entryArray.length(); i++) {
                entryModel.setKey(entryArray.getJSONObject(i).optString("key"));
                entryModel.setValue(entryArray.getJSONObject(i).optString("value"));
                fieldMap.add(entryModel);
            }
            loginResponseMode.responsePublicModel.setFieldMap(fieldMap);
        } catch (JSONException e) {
            LogManager.e("ParseLoginResponse is error " + e.getLocalizedMessage());
        }
        return loginResponseMode;
    }
}
