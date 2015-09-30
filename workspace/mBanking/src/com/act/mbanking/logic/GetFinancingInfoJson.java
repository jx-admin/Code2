package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.GetFinancingInfoModel;
import com.act.mbanking.bean.InstallmentsModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetFinancingInfoJson {
	
	public static String getFinancingInfoReportProtocal(RequestPublicModel publicModel,String accountCode,String financingType){
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject getFinancingInfoObj = new JSONObject();
			getFinancingInfoObj.put("bankName", publicModel.getBankName());
			getFinancingInfoObj.put("serviceType", ServiceType.getFinancingInfo);
			getFinancingInfoObj.put("enterpriseId", publicModel.getEnterpriseId());
			getFinancingInfoObj.put("customerCode", publicModel.getCustomerCode());
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
            
            JSONObject aggregatedMovementsResult = getFinancingInfoObj.optJSONObject("aggregatedMovementsResult");
            
            JSONArray installmentsArray = aggregatedMovementsResult.optJSONArray("installments");
            List<InstallmentsModel> installments = new ArrayList<InstallmentsModel>();
            for(int i=0;i< installmentsArray.length();i++){
            	InstallmentsModel installment = new InstallmentsModel();
            	installment.setAmount(installmentsArray.optJSONObject(i).optDouble("amount"));
            	installment.setAmountCapitalShare(installmentsArray.optJSONObject(i).optDouble("amountCapitalShare"));
            	installment.setAmountInterestShare(installmentsArray.optJSONObject(i).optDouble("amountInterestShare"));
            	installment.setApplyRate(installmentsArray.optJSONObject(i).optDouble("applyRate"));
            	installment.setDeadlineDate(installmentsArray.optJSONObject(i).optString("deadlineDate"));
            	installment.setId(installmentsArray.optJSONObject(i).optString("id"));
            	installment.setInstallmentType(installmentsArray.optJSONObject(i).optString("installmentType"));
            	installment.setPaidState(installmentsArray.optJSONObject(i).optString("paidState"));
            	installments.add(installment);
            }
            getFinancingInfoResponse.setInstallments(installments);
            
        }catch(Exception e){
        	LogManager.e("paresgetFinancingInfoResponse is error " + e.getLocalizedMessage());
        }
		return getFinancingInfoResponse;
	}
}
