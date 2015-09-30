
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.model.GetBranchListResponseModel;
import com.accenture.mbank.model.HelpItemListModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetBranchListJson {
    /**
     * @param publicModel
     * @param latitude
     * @param longitude
     * @param range
     * @return
     */
    public static String GetBranchListReportProtocal(RequestPublicModel publicModel, double latitude,double longitude, int range, String searchText) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject branchListObj = new JSONObject();
            branchListObj.put("serviceType", ServiceType.getBranchList);
            branchListObj.put("bankName", publicModel.getBankName());
            branchListObj.put("enterpriseId", publicModel.getEnterpriseId());
            branchListObj.put("customerNumber", publicModel.getCustomerNumber());
            branchListObj.put("channel", publicModel.getChannel());
            branchListObj.put("userAgent", publicModel.getUserAgent());
            branchListObj.put("latitude", latitude);
            branchListObj.put("longitude", longitude);
            branchListObj.put("range", range);
            branchListObj.put("searchText", searchText);

            jsonObj.put("GetBranchListRequest", branchListObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("GetBranchListReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static GetBranchListResponseModel ParseGetBranchListResponse(String json) {
        
        GetBranchListResponseModel getBranchListResponse = new GetBranchListResponseModel();
        List<BranchListModel> branchLists = new ArrayList<BranchListModel>();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getBranchListObj = jsonObj.getJSONObject("GetBranchListResponse");
            
            getBranchListResponse.responsePublicModel.setResultCode(getBranchListObj.getInt("resultCode"));
            getBranchListResponse.responsePublicModel.setResultDescription(getBranchListObj.optString("resultDescription"));
            if (getBranchListResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getBranchListObj.getJSONObject("eventManagement");
                getBranchListResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getBranchListResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getBranchListResponse;
            }
            getBranchListResponse.responsePublicModel.setTransactionId(getBranchListObj.optString("transactionId"));
            
            JSONArray branchListArray = getBranchListObj.getJSONArray("branchList");
            for(int i=0;i<branchListArray.length();i++){
                BranchListModel branchList = new BranchListModel();
                branchList.setCountry(branchListArray.getJSONObject(i).optString("country"));
                branchList.setRegion(branchListArray.getJSONObject(i).optString("region"));
                branchList.setProvince(branchListArray.getJSONObject(i).optString("province"));
                branchList.setCity(branchListArray.getJSONObject(i).optString("city"));
                branchList.setName(branchListArray.getJSONObject(i).optString("name"));
                branchList.setAddress(branchListArray.getJSONObject(i).optString("address"));
                branchList.setPostalcode(branchListArray.getJSONObject(i).optString("postalcode"));
                branchList.setBankDescription(branchListArray.getJSONObject(i).optString("bankDescription"));
                branchList.setPhoneNumber(branchListArray.getJSONObject(i).optString("phoneNumber"));
                branchList.setMailAddress(branchListArray.getJSONObject(i).optString("mailAddress"));
                branchList.setFaxNumber(branchListArray.getJSONObject(i).optString("faxNumber"));
                branchList.setMondayTime(branchListArray.getJSONObject(i).optString("mondayTime"));
                branchList.setTuesdayTime(branchListArray.getJSONObject(i).optString("tuesdayTime"));
                branchList.setWednesdayTime(branchListArray.getJSONObject(i).optString("wednesdayTime"));
                branchList.setThursdayTime(branchListArray.getJSONObject(i).optString("thursdayTime"));
                branchList.setFridayTime(branchListArray.getJSONObject(i).optString("fridayTime"));
                branchList.setSaturdayTime(branchListArray.getJSONObject(i).optString("saturdayTime"));
                branchList.setSundayTime(branchListArray.getJSONObject(i).optString("sundayTime"));
                branchList.setLatitude(branchListArray.getJSONObject(i).optDouble("latitude"));
                branchList.setLongitude(branchListArray.getJSONObject(i).optDouble("longitude"));
                branchList.setApproximation(branchListArray.getJSONObject(i).optString("approximation"));
                branchList.setDirector(branchListArray.getJSONObject(i).optString("director"));
                branchList.setViceDirector(branchListArray.getJSONObject(i).optString("viceDirector"));
                branchLists.add(branchList);
            }
            getBranchListResponse.setBranchList(branchLists);
        } catch (Exception e) {
            LogManager.e("ParseGetBranchListResponse is error" + e.getLocalizedMessage());
        }
        return getBranchListResponse;
    }
}
