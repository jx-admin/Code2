package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.model.CreditModel;
import com.accenture.mbank.model.GetCreditList;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetCreditsListJson {
	/**
	 * 
	 * @param publicModel
	 * @param accountCode
	 * @return
	 */
	public static String GetCreditsListReportProtocal(RequestPublicModel publicModel, String accountCode,String restartingKey) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject obj = new JSONObject();
			obj.put("serviceType", ServiceType.getCreditsList);
			obj.put("bankName", publicModel.getBankName());
			obj.put("enterpriseId", publicModel.getEnterpriseId());
			obj.put("channel", publicModel.getChannel());
			obj.put("userAgent", publicModel.getUserAgent());
			obj.put("token", publicModel.getToken());
			obj.put("accountCode", accountCode);
			obj.put("numberOfTnx", SettingModel.LAST_20);
			obj.put("applicationId", publicModel.getToken());
			obj.put("customerNumber", publicModel.getCustomerNumber());
			obj.put("sessionId", publicModel.getSessionId());
			obj.put("restartingKey", restartingKey);
			jsonObj.put("GetCreditsListRequest", obj);
			result = jsonObj.toString();
		} catch (Exception e) {
			LogManager.e("GetCreditsListReportProtocal is error" + e.getLocalizedMessage());
		}
		return result;
	}

	public static GetCreditList ParseCreditListResponse(String json) {
		GetCreditList getCreditList = new GetCreditList();
		List<CreditModel> creditList = new ArrayList<CreditModel>();
		if (json == null) {
			LogManager.d("GetCreditsListResponse" + "json null!");
			return null;
		}
		
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONObject getCreditListObj = jsonObj.getJSONObject("GetCreditsListResponse");
			getCreditList.responsePublicModel.setResultCode(getCreditListObj.getInt("resultCode"));
			getCreditList.responsePublicModel.setResultDescription(getCreditListObj.optString("resultDescription"));
//            if (getCreditList.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getCreditListObj.getJSONObject("eventManagement");
                getCreditList.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getCreditList.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
//                return getCreditList;
//            }
            getCreditList.responsePublicModel.setTransactionId(getCreditListObj.optString("transactionId"));
            getCreditList.setRestartingKey(getCreditListObj.optString("restartingKey"));
            getCreditList.setMoreValues(getCreditListObj.optBoolean("moreValues"));
            
            JSONArray creditListArray = getCreditListObj.getJSONArray("creditsList");
            for(int i=0;i<creditListArray.length();i++){
            	CreditModel creditModel = new CreditModel();
            	creditModel.setCreditID(creditListArray.optJSONObject(i).optString("creditID"));
            	creditModel.setCreditType(creditListArray.optJSONObject(i).optString("creditType"));
            	creditModel.setInsertionDate(creditListArray.optJSONObject(i).optString("insertionDate"));
            	creditModel.setPaymentsNum(creditListArray.optJSONObject(i).optInt("paymentsNum"));
            	creditModel.setState(creditListArray.optJSONObject(i).optString("state"));
            	creditModel.setTotalAmount(creditListArray.optJSONObject(i).optDouble("totalAmount"));
            	creditList.add(creditModel);
            }
            getCreditList.setCreditList(creditList);
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.e("ParseInsertRechargeCardResponse is error " + e);
		}
		return getCreditList;
	}
}
