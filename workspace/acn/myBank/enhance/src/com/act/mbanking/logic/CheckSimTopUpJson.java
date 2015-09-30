
package com.act.mbanking.logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.App;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AmountAvailable;
import com.act.mbanking.bean.CheckSimTopUpResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class CheckSimTopUpJson {
    public static String CheckSimTopUpReportProtocal(RequestPublicModel publicModel,
            List<AmountAvailable> amountAvailableList, String accountCode, String destProvider,
            String phoneNumber,String currency,String transactionId) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject checkSimTopUpObj = new JSONObject();
            checkSimTopUpObj.put("serviceType", ServiceType.checkSimTopUp);
            checkSimTopUpObj.put("bankName", publicModel.getBankName());
            checkSimTopUpObj.put("enterpriseId", publicModel.getEnterpriseId());
            checkSimTopUpObj.put("customerCode", publicModel.getCustomerCode());
            checkSimTopUpObj.put("channel", publicModel.getChannel());
            checkSimTopUpObj.put("userAgent", publicModel.getUserAgent());
            checkSimTopUpObj.put("token", publicModel.getToken());
            checkSimTopUpObj.put("sessionId", publicModel.getSessionId());
            checkSimTopUpObj.put("accountCode", accountCode);
            checkSimTopUpObj.put("destProvider", destProvider);
            checkSimTopUpObj.put("phoneNumber", phoneNumber);
            checkSimTopUpObj.put("currency",currency);
            if(App.isNewPaymentsUpdate){
            	checkSimTopUpObj.put("transferCheck", true);
            	checkSimTopUpObj.put("transactionId", transactionId);
            }
            
            JSONArray amountAvailableArray = new JSONArray();
            for (int i = 0; i < amountAvailableList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("commissionAmount", amountAvailableList.get(i).getCommissionAmount());
                obj.put("description", amountAvailableList.get(i).getDescription());
                obj.put("rechargeAmount", amountAvailableList.get(i).getRechargeAmount());
                amountAvailableArray.put(obj);
            }
            checkSimTopUpObj.put("amountAvailable", amountAvailableArray);
            jsonObj.put("CheckSimTopUpRequest", checkSimTopUpObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("CheckSimTopUpReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    
    /**
     * 
     * @param json
     * @return
     */
    public static CheckSimTopUpResponseModel ParseCheckSimTopUpResponse(String json) {
        
        CheckSimTopUpResponseModel checksimTopUpResponse = new CheckSimTopUpResponseModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject checksimTopUpObj = jsonObj.getJSONObject("CheckSimTopUpResponse");

            checksimTopUpResponse.responsePublicModel.setResultCode(checksimTopUpObj.getInt("resultCode"));
            checksimTopUpResponse.responsePublicModel.setResultDescription(checksimTopUpObj.optString("resultDescription"));
            if (checksimTopUpResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = checksimTopUpObj.getJSONObject("eventManagement");
                checksimTopUpResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                checksimTopUpResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return checksimTopUpResponse;
            }
            
            checksimTopUpResponse.responsePublicModel.setTransactionId(checksimTopUpObj.optString("transactionId"));
            checksimTopUpResponse.setBranchDescription(checksimTopUpObj.optString("branchDescription"));
            if(App.isNewPaymentsUpdate){
            	checksimTopUpResponse.setTransferId(checksimTopUpObj.optString("transferId"));
            }
            checksimTopUpResponse.setCharges(checksimTopUpObj.optDouble("charges"));
            checksimTopUpResponse.setCount(checksimTopUpObj.optInt("count"));
            checksimTopUpResponse.setPinIsNotRequired(checksimTopUpObj.optString("pinIsNotRequired"));
            
            

        } catch (Exception e) {
            LogManager.e("ParseCheckSimTopUpResponse is error" + e.getLocalizedMessage());
        }
        return checksimTopUpResponse;
    }
}
