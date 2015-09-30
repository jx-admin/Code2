package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.GetMovementsModel;
import com.act.mbanking.bean.MovementsModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.SettingModel;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;

public class GetMovementsJson {
    /**
     * 
     * @param publicModel
     * @param paymentMethod
     * @param accountCode
     * @param sorting 排序 0 1
     * @param transactionBy 20 = 最近20条 否则就是取最近两个月的
     * @param restartingKey
     * @return
     */
	public static String getMovementsReportProtocal(RequestPublicModel publicModel,String paymentMethod,String accountCode,int sorting
	        ,int transactionBy,String restartingKey){
	     
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject getMovementsObj = new JSONObject();
			getMovementsObj.put("bankName", publicModel.getBankName());
			getMovementsObj.put("serviceType", ServiceType.getMovements);
			getMovementsObj.put("enterpriseId", publicModel.getEnterpriseId());
			getMovementsObj.put("customerCode", publicModel.getCustomerCode());
			getMovementsObj.put("channel", publicModel.getChannel());
			getMovementsObj.put("userAgent", publicModel.getUserAgent());
			getMovementsObj.put("sessionId", publicModel.getSessionId());
			if(transactionBy == SettingModel.LAST_20){
			    getMovementsObj.put("numberOfTnx",20);
			}else{
	            String dateFrom = Utils.getDateLastMonth(transactionBy);
	            getMovementsObj.put("dateFrom", dateFrom);
	            String dateTo = TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat2);
	            getMovementsObj.put("dateTo", dateTo);
			}
			getMovementsObj.put("sorting", sorting);
			
			if(restartingKey !=null){
			    getMovementsObj.put("restartingKey", restartingKey);
			}
			
			getMovementsObj.put("token", publicModel.getToken());
			getMovementsObj.put("paymentMethod", paymentMethod);
			getMovementsObj.put("accountCode", accountCode);
			
			jsonObj.put("GetMovementsRequest", getMovementsObj);
			result = jsonObj.toString();
		}catch(Exception e){
			LogManager.e("getMovementsReportProtocal is error " + e.getLocalizedMessage());
		}
		return result;
	}
	
	public static GetMovementsModel parseGetMovementsResponse(String json){
		GetMovementsModel getMovementsResponse = new GetMovementsModel();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject  getMovementsObj = jsonObj.getJSONObject("GetMovementsResponse");
            
            getMovementsResponse.responsePublicModel.setResultCode(getMovementsObj.optInt("resultCode"));
            getMovementsResponse.responsePublicModel.setResultDescription(getMovementsObj.optString("resultDescription"));
            if (getMovementsResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getMovementsObj.getJSONObject("eventManagement");
                getMovementsResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getMovementsResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getMovementsResponse;
            }
            
            getMovementsResponse.responsePublicModel.setTransactionId(getMovementsObj.optString("transactionId"));
            getMovementsResponse.responsePublicModel.setRestartingKey(getMovementsObj.optString("restartingKey"));
            
            JSONArray movementArray = getMovementsObj.optJSONArray("movements");
            List<MovementsModel> movements = new ArrayList<MovementsModel>();
            for(int i=0;i<movementArray.length();i++){
            	MovementsModel movement = new MovementsModel();
            	movement.setAmount(movementArray.optJSONObject(i).optDouble("amount"));
            	movement.setDescription(movementArray.optJSONObject(i).optString("description"));
            	movement.setOperationDate(movementArray.optJSONObject(i).optString("operationDate"));
            	movement.setValueDate(movementArray.optJSONObject(i).optString("valueDate"));
            	movements.add(movement);
            }
            getMovementsResponse.setMovements(movements);
            
        }catch(Exception e){
        	LogManager.e("parseGetMovementsResponse is error " + e.getLocalizedMessage());
        }
		return getMovementsResponse;
	}
}
