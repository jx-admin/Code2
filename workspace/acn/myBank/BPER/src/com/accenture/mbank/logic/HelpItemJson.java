
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.GetHelpItemResponseModel;
import com.accenture.mbank.model.HelpItemListModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class HelpItemJson {
    /**
     * @param publicModel
     * @return
     */
    public static String HelpItemReportProtocal(RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject helpItemObj = new JSONObject();
            helpItemObj.put("serviceType", ServiceType.getHelpItems);
            helpItemObj.put("bankName", publicModel.getBankName());
            helpItemObj.put("enterpriseId", publicModel.getEnterpriseId());
            helpItemObj.put("customerNumber", publicModel.getCustomerNumber());
            helpItemObj.put("channel", publicModel.getChannel());
            helpItemObj.put("userAgent", publicModel.getUserAgent());
            
            jsonObj.put("GetHelpItemsRequest", helpItemObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("HelpItemReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
     public static GetHelpItemResponseModel ParseHelpItemResponse(String json){
         
         GetHelpItemResponseModel getHelpItemResponse = new GetHelpItemResponseModel();
         List<HelpItemListModel> helpItems = new ArrayList<HelpItemListModel>();
         
         if (json == null) {
             return null;
         }
         
         try {
             JSONObject jsonObj = new JSONObject(json);
             JSONObject helpItemResponse = jsonObj.getJSONObject("GetHelpItemsResponse");
             
             getHelpItemResponse.responsePublicModel.setResultCode(helpItemResponse.optInt("resultCode"));
             getHelpItemResponse.responsePublicModel.setResultDescription(helpItemResponse.optString("resultDescription"));
             if (getHelpItemResponse.responsePublicModel.getResultCode() != 0) {
                 JSONObject eventManagementObj = helpItemResponse.getJSONObject("eventManagement");
                 getHelpItemResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                 getHelpItemResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                 return getHelpItemResponse;
             }
             
             getHelpItemResponse.responsePublicModel.setTransactionId(helpItemResponse.optString("transactionId"));
             
             JSONArray helpItemArray = helpItemResponse.getJSONArray("helpItemsList");
             for(int i=0;i<helpItemArray.length();i++){
                 HelpItemListModel helpItem = new HelpItemListModel("");
                 helpItem.setHelpItemId(helpItemArray.getJSONObject(i).optInt("helpItemId"));
                 helpItem.setText(helpItemArray.getJSONObject(i).optString("text"));
                 helpItem.setTitle(helpItemArray.getJSONObject(i).optString("title"));
                 helpItems.add(helpItem);
             }
             getHelpItemResponse.setHelpItemList(helpItems);
         }catch(Exception e){
             LogManager.e("ParseHelpItemResponse is error" + e.getLocalizedMessage());
         }
         return getHelpItemResponse;
     }
    
}