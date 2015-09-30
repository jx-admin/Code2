
package com.act.mbanking.logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.App;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AmountAvailable;
import com.act.mbanking.bean.CheckSimTopUpResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.SimTopUpResponseModel;
import com.act.mbanking.utils.LogManager;

public class SimTopUpJson {
    
    
    public static String SimTopUpReportProtocal(RequestPublicModel publicModel, String accountCode,
            String destProvider, String phoneNumber, String otpValue,
            String otpKeySession, String otpChannel,List<AmountAvailable> amountAvailableList,String currency, CheckSimTopUpResponseModel checkSimTopUp) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject simTopUpObj = new JSONObject();
            simTopUpObj.put("serviceType", ServiceType.simTopUp);
            simTopUpObj.put("bankName", publicModel.getBankName());
            simTopUpObj.put("enterpriseId", publicModel.getEnterpriseId());
            simTopUpObj.put("customerCode", publicModel.getCustomerCode());
            simTopUpObj.put("channel", publicModel.getChannel());
            simTopUpObj.put("userAgent", publicModel.getUserAgent());
            simTopUpObj.put("token", publicModel.getToken());
            simTopUpObj.put("sessionId", publicModel.getSessionId());
            simTopUpObj.put("currency", currency);
            if(App.isNewPaymentsUpdate){
            	simTopUpObj.put("transferCheck", true);
            	simTopUpObj.put("transferId", checkSimTopUp.getTransferId());
            }
            
            simTopUpObj.put("accountCode", accountCode);
            simTopUpObj.put("destProvider", destProvider);
            simTopUpObj.put("phoneNumber", phoneNumber);
            
            simTopUpObj.put("otpValue", otpValue);
            simTopUpObj.put("otpKeySession", otpKeySession);
            simTopUpObj.put("otpChannel", otpChannel);
            
            JSONArray amountAvailableArray = new JSONArray();
            for (int i = 0; i < amountAvailableList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("commissionAmount", amountAvailableList.get(i).getCommissionAmount());
                obj.put("description", amountAvailableList.get(i).getDescription());
                obj.put("rechargeAmount", amountAvailableList.get(i).getRechargeAmount());
                amountAvailableArray.put(obj);
            }
            simTopUpObj.put("amountAvailable", amountAvailableArray);
            
            jsonObj.put("SimTopUpRequest", simTopUpObj);

            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("SimTopUpReportProtocal " + e.getLocalizedMessage());
        }
        return result;
    }
    
    public static SimTopUpResponseModel ParseSimTopUpResponse(String json){
        
        SimTopUpResponseModel simTopUpResponse = new SimTopUpResponseModel();
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject simTopUpObj = jsonObj.getJSONObject("SimTopUpResponse");
            
            simTopUpResponse.responsePublicModel.setResultCode(simTopUpObj.getInt("resultCode"));
            simTopUpResponse.responsePublicModel.setResultDescription(simTopUpObj.optString("resultDescription"));
            if (simTopUpResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = simTopUpObj.getJSONObject("eventManagement");
                simTopUpResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                simTopUpResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return simTopUpResponse;
            }
            
            simTopUpResponse.responsePublicModel.setTransactionId(simTopUpObj.optString("transactionId"));
            
        }catch(Exception e){
            LogManager.e("GenerateOTPReportProtocal is error" + e.getLocalizedMessage());
        }
        return simTopUpResponse;
    }
}
