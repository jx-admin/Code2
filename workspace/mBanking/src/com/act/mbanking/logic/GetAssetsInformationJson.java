package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AssetDetailModel;
import com.act.mbanking.bean.GetAssetsInformationResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetAssetsInformationJson {
	public static String GetAssetsInformantionReportProtocal(RequestPublicModel publicModel, String accountCode) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject getAssetsInformantionObj = new JSONObject();
			getAssetsInformantionObj.put("bankName", publicModel.getBankName());
			getAssetsInformantionObj.put("serviceType", ServiceType.getAssetsInformation);
			getAssetsInformantionObj.put("enterpriseId", publicModel.getEnterpriseId());
			getAssetsInformantionObj.put("customerCode", publicModel.getCustomerCode());
			getAssetsInformantionObj.put("channel", publicModel.getChannel());
			getAssetsInformantionObj.put("userAgent", publicModel.getUserAgent());
			getAssetsInformantionObj.put("sessionId", publicModel.getSessionId());
			getAssetsInformantionObj.put("token", publicModel.getToken());
			getAssetsInformantionObj.put("accountCode", accountCode);
			
			jsonObj.put("GetAssetsInformationRequest", getAssetsInformantionObj);
			result = jsonObj.toString();
		} catch (Exception e) {
			LogManager.e("GetAssetsInformantionReportProtocal is error " + e.getLocalizedMessage());
		}
		return result;
	}
	
	
	public static GetAssetsInformationResponseModel parseGetAssetsInformationResponse(String json){
		GetAssetsInformationResponseModel getAssetsInformationResponse = new GetAssetsInformationResponseModel();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getAssetsInformationObj = jsonObj.getJSONObject("GetAssetsInformationResponse");
            
            getAssetsInformationResponse.responsePublicModel.setResultCode(getAssetsInformationObj.optInt("resultCode"));
            getAssetsInformationResponse.responsePublicModel.setResultDescription(getAssetsInformationObj.optString("resultDescription"));
            if (getAssetsInformationResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getAssetsInformationObj.getJSONObject("eventManagement");
                getAssetsInformationResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getAssetsInformationResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getAssetsInformationResponse;
            }
            
            getAssetsInformationResponse.responsePublicModel.setTransactionId(getAssetsInformationObj.optString("transactionId"));
            getAssetsInformationResponse.setPercentage(getAssetsInformationObj.optDouble("percentage"));
            getAssetsInformationResponse.setPortfolioValue(getAssetsInformationObj.optDouble("portfolioValue"));
            
            JSONArray assetDetailArray = getAssetsInformationObj.optJSONArray("assetDetails");
            List<AssetDetailModel> assetDetails = new ArrayList<AssetDetailModel>();
            for(int i=0;i<assetDetailArray.length();i++){
            	AssetDetailModel assetDetail = new AssetDetailModel();
            	assetDetail.setGrossValue(assetDetailArray.optJSONObject(i).optString("grossValue"));
            	assetDetail.setDescription(assetDetailArray.optJSONObject(i).optString("description"));
            	assetDetails.add(assetDetail);
            }
            getAssetsInformationResponse.setAssetDetails(assetDetails);
            
        }catch( Exception e){
        	LogManager.e("parseGetAssetsInformationResponse is error " + e.getLocalizedMessage());
        }
        return getAssetsInformationResponse;
	}
}
