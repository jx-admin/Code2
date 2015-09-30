package com.accenture.mbank.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;
import com.accenture.mbank.util.TimeUtil;

public class GetFinancingInfoJson {
	
	public static String getFinancingInfoReportProtocal(RequestPublicModel publicModel,String accountCode,String financingType){
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject getFinancingInfoObj = new JSONObject();
			getFinancingInfoObj.put("bankName", publicModel.getBankName());
			getFinancingInfoObj.put("serviceType", ServiceType.getFinancingInfo);
			getFinancingInfoObj.put("enterpriseId", publicModel.getEnterpriseId());
			getFinancingInfoObj.put("customerNumber", publicModel.getCustomerNumber());
			getFinancingInfoObj.put("channel", publicModel.getChannel());
			getFinancingInfoObj.put("userAgent", publicModel.getUserAgent());
			getFinancingInfoObj.put("sessionId", publicModel.getSessionId());
			getFinancingInfoObj.put("token", publicModel.getToken());
			getFinancingInfoObj.put("accountCode", accountCode);
			
			getFinancingInfoObj.put("financingType", financingType);
			
			jsonObj.put("GetFinancingInfoRequest", getFinancingInfoObj);
			result = jsonObj.toString();
		} catch (Exception e) {
			LogManager.e("getFinancingInfoReportProtocal is error " + e.getLocalizedMessage());
		}
		return result;
	}
	
	public static GetFinancingInfoModel paresgetFinancingInfoResponse(String json){
		GetFinancingInfoModel getFinancingInfoResponse = new GetFinancingInfoModel();
        
        if (json == null) {
            return null;
        }

		/*
		 * print long string
		 */
		int maxLogSize = 4000;
		for (int i = 0; i <= json.length() / maxLogSize; i++) {
			int start = i * maxLogSize;
			int end = (i + 1) * maxLogSize;
			end = end > json.length() ? json.length() : end;
			LogManager.i(json.substring(start, end));
		}

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getFinancingInfoObj = jsonObj.getJSONObject("GetFinancingInfoResponse");
            
            getFinancingInfoResponse.responsePublicModel.setResultCode(getFinancingInfoObj.optInt("resultCode"));
            getFinancingInfoResponse.responsePublicModel.setResultDescription(getFinancingInfoObj.optString("resultDescription"));
            if (getFinancingInfoResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getFinancingInfoObj.getJSONObject("eventManagement");
                getFinancingInfoResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getFinancingInfoResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getFinancingInfoResponse;
            }
            
            getFinancingInfoResponse.responsePublicModel.setTransactionId(getFinancingInfoObj.optString("transactionId"));
            getFinancingInfoResponse.setAccountCode(getFinancingInfoObj.optString("accountCode"));
            getFinancingInfoResponse.setBankCode(getFinancingInfoObj.optString("bankCode"));
            getFinancingInfoResponse.setBankServiceCode(getFinancingInfoObj.optString("bankServiceCode"));
            getFinancingInfoResponse.setBranchCode(getFinancingInfoObj.optString("branchCode"));
            getFinancingInfoResponse.setDisbursementDate(getFinancingInfoObj.optString("disbursementDate"));
            getFinancingInfoResponse.setEndDate(getFinancingInfoObj.optString("endDate"));
            getFinancingInfoResponse.setFinancingType(getFinancingInfoObj.optString("financingType"));
            getFinancingInfoResponse.setHolder(getFinancingInfoObj.optString("holder"));
            getFinancingInfoResponse.setIbanCode(getFinancingInfoObj.optString("ibanCode"));
            getFinancingInfoResponse.setLastUpdate(getFinancingInfoObj.optString("lastUpdate"));
            getFinancingInfoResponse.setMoreValues(getFinancingInfoObj.optString("moreValues"));
            getFinancingInfoResponse.setNumPaymentsRemaning(getFinancingInfoObj.optString("numPaymentsRemaning"));
            getFinancingInfoResponse.setRate(getFinancingInfoObj.optString("rate"));
            getFinancingInfoResponse.setBenchmarksValue(getFinancingInfoObj.optString("benchmarksValue"));
            getFinancingInfoResponse.setBenchmarksDesc(getFinancingInfoObj.optString("benchmarksDesc"));
            getFinancingInfoResponse.setRateType(getFinancingInfoObj.optString("rateType"));
            getFinancingInfoResponse.setResidueAmount(getFinancingInfoObj.optString("residueAmount"));
            getFinancingInfoResponse.setServiceCategory(getFinancingInfoObj.optString("serviceCategory"));
            getFinancingInfoResponse.setTotalAmountl(getFinancingInfoObj.optString("totalAmount"));
            getFinancingInfoResponse.setDuration(getFinancingInfoObj.optString("duration"));
            getFinancingInfoResponse.setWarranty(getFinancingInfoObj.optBoolean("isWarranty"));
            getFinancingInfoResponse.setEndRate(getFinancingInfoObj.optDouble("endRate"));
            
            JSONObject aggregatedMovementsResult = getFinancingInfoObj.optJSONObject("aggregatedMovementsResult");
            
            JSONArray installmentsArray = aggregatedMovementsResult.optJSONArray("installments");
            List<InstallmentsModel> installments = new ArrayList<InstallmentsModel>();
            SimpleDateFormat  format = new SimpleDateFormat(TimeUtil.dateFormat2); 
            
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());
            now.add(Calendar.MONTH, 1);
        	InstallmentsModel lastInstallment=null;
            for(int i=0;i< installmentsArray.length();i++){
            	InstallmentsModel installment = new InstallmentsModel();
            	installment.setAmount(installmentsArray.optJSONObject(i).optDouble("amount"));
            	installment.setAmountCapitalShare(installmentsArray.optJSONObject(i).optDouble("amountCapitalShare"));
            	installment.setAmountInterestShare(installmentsArray.optJSONObject(i).optDouble("amountInterestShare"));
            	installment.setApplyRate(installmentsArray.optJSONObject(i).optDouble("applyRate"));
            	installment.setId(installmentsArray.optJSONObject(i).optString("id"));
            	installment.setInstallmentType(installmentsArray.optJSONObject(i).optString("installmentType"));
            	installment.setPaidState(installmentsArray.optJSONObject(i).optString("paidState"));
            	
            	Date strDate = format.parse(installmentsArray.optJSONObject(i).optString("deadlineDate"));
            	installment.setDeadlineDate(strDate.getTime());
            	
            	Calendar calendarDate = Calendar.getInstance();
            	calendarDate.setTime(strDate);
            	if (calendarDate.before(now)) {
            		installments.add(installment);
            	}else{
            		if(lastInstallment==null){
            			lastInstallment=installment;
            		}else{
            			Calendar lastcalendarDate = Calendar.getInstance();
            			lastcalendarDate.setTimeInMillis(lastInstallment.getDeadlineDate());
            			if(calendarDate.before(lastcalendarDate)){
                			lastInstallment=installment;
            			}
            		}
            	}
            	
            }
            if (lastInstallment!=null) {
        		installments.add(lastInstallment);
        	}
            getFinancingInfoResponse.setInstallments(installments);
            
        }catch(Exception e){
        	LogManager.e("paresgetFinancingInfoResponse is error " + e.getLocalizedMessage());
        }
		return getFinancingInfoResponse;
	}
}
