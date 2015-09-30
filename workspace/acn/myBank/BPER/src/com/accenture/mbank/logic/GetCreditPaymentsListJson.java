package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.CreditPaymentModel;
import com.accenture.mbank.model.GetCreditPaymentList;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;
import com.accenture.mbank.util.TimeUtil;

public class GetCreditPaymentsListJson {

	public static String GetCreditPaymentsListReportProtocal(RequestPublicModel publicModel, String accountCode,String creditID,String insertionDate,String filter,String restartingKey) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject obj = new JSONObject();
			obj.put("serviceType", ServiceType.getCreditPaymentsList);
			obj.put("bankName", publicModel.getBankName());
			obj.put("enterpriseId", publicModel.getEnterpriseId());
			obj.put("channel", publicModel.getChannel());
			obj.put("userAgent", publicModel.getUserAgent());
			obj.put("token", publicModel.getToken());
			obj.put("accountCode", accountCode);
			if(!filter.equals("")){
				obj.put("filter", filter);
			}
			
			obj.put("numberOfTnx", SettingModel.LAST_20);
			obj.put("applicationId", publicModel.getToken());
			obj.put("customerNumber", publicModel.getCustomerNumber());
			obj.put("sessionId", publicModel.getSessionId());
			obj.put("restartingKey", restartingKey);
			if (creditID != null) {
				obj.put("creditId", creditID);
				if(insertionDate != null){
					insertionDate = TimeUtil.changeFormattrString(insertionDate, TimeUtil.dateFormat2a, TimeUtil.dateFormat2);
					obj.put("insertionDate", insertionDate);
				}
			}
			jsonObj.put("GetCreditPaymentsListRequest", obj);
			result = jsonObj.toString();
		} catch (Exception e) {
			LogManager.e("GetCreditPaymentsListRequest is error"+ e.getLocalizedMessage());
		}
		return result;
	}
	
	public static GetCreditPaymentList ParseCreditPaymentsListResponse(String json) {
		GetCreditPaymentList getCreditPaymentList = new GetCreditPaymentList();
		List<CreditPaymentModel> creditPaymentList = new ArrayList<CreditPaymentModel>();
		if (json == null) {
			LogManager.d("ParseCreditPaymentsListResponse" + "json null!");
			return null;
		}
		
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONObject getCreditListObj = jsonObj.getJSONObject("GetCreditPaymentsListResponse");
			getCreditPaymentList.responsePublicModel.setResultCode(getCreditListObj.getInt("resultCode"));
			getCreditPaymentList.responsePublicModel.setResultDescription(getCreditListObj.optString("resultDescription"));
//            if (getCreditPaymentList.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getCreditListObj.getJSONObject("eventManagement");
                getCreditPaymentList.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getCreditPaymentList.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
//                return getCreditPaymentList;
//            }
            getCreditPaymentList.responsePublicModel.setTransactionId(getCreditListObj.optString("transactionId"));
            getCreditPaymentList.setRestartingKey(getCreditListObj.optString("restartingKey"));
            getCreditPaymentList.setMoreValues(getCreditListObj.optBoolean("moreValues"));
            
            JSONArray creditPaymentListArray = getCreditListObj.getJSONArray("creditsPaymentsList");
            for(int i=0;i<creditPaymentListArray.length();i++){
            	CreditPaymentModel creditPaymentModel = new CreditPaymentModel();
            	creditPaymentModel.setCreditID(creditPaymentListArray.optJSONObject(i).optString("creditID"));
            	creditPaymentModel.setInsertionDate(creditPaymentListArray.optJSONObject(i).optString("insertionDate"));
            	creditPaymentModel.setState(creditPaymentListArray.optJSONObject(i).optString("state"));
            	creditPaymentModel.setTotalAmount(creditPaymentListArray.optJSONObject(i).optDouble("totalAmount"));
            	creditPaymentModel.setExecutionDate(creditPaymentListArray.optJSONObject(i).optString("executionDate"));
            	creditPaymentModel.setDebtorName(creditPaymentListArray.optJSONObject(i).optString("debtorName"));
            	creditPaymentModel.setPaymentID(creditPaymentListArray.optJSONObject(i).optString("paymentID"));
            	creditPaymentModel.setPortfolio(creditPaymentListArray.optJSONObject(i).optInt("portfolio"));
            	creditPaymentModel.setListTypeItem(creditPaymentListArray.optJSONObject(i).optString("listTypeItem"));
            	creditPaymentList.add(creditPaymentModel);
            }
            getCreditPaymentList.setCreditPaymentList(creditPaymentList);
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.e("ParseCreditPaymentsListResponse is error " + e);
		}
		return getCreditPaymentList;
	}
	
}
