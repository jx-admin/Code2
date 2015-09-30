
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.GetPaymentTempLatesResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetPaymentTemplatesJson {
    public static String GetPaymentTemplatesReportProtocal(RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getPaymentTemplatesObj = new JSONObject();
            getPaymentTemplatesObj.put("serviceType", ServiceType.GETPAYMENTTEMPLATE);
            getPaymentTemplatesObj.put("enterpriseId", publicModel.getEnterpriseId());
            getPaymentTemplatesObj.put("bankName", publicModel.getBankName());
            getPaymentTemplatesObj.put("token", publicModel.getToken());
            getPaymentTemplatesObj.put("customerCode", publicModel.getCustomerCode());
            getPaymentTemplatesObj.put("channel", publicModel.getChannel());
            getPaymentTemplatesObj.put("userAgent", publicModel.getUserAgent());
            getPaymentTemplatesObj.put("sessionId", publicModel.getSessionId());
            
            jsonObj.put("GetPaymentTemplatesRequest", getPaymentTemplatesObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("GetPaymentTemplatesReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    
    public static GetPaymentTempLatesResponseModel ParseGetPaymentTempLatesResponse(String json){
        GetPaymentTempLatesResponseModel getPaymentTempLatesResponseModel = new GetPaymentTempLatesResponseModel();
         List<PaymentTemplate> paymentTemplateList = new ArrayList<PaymentTemplate>();
         if (json == null) {
             return null;
         }
         try {
             JSONObject jsonObj = new JSONObject(json);
             JSONObject getPaymentTemplateObj = jsonObj.getJSONObject("GetgetPaymentTempLatesResponse");
             getPaymentTempLatesResponseModel.responsePublicModel.setResultCode(getPaymentTemplateObj.getInt("resultCode"));
             getPaymentTempLatesResponseModel.responsePublicModel.setResultDescription(getPaymentTemplateObj.optString("resultDescription"));
             if (getPaymentTempLatesResponseModel.responsePublicModel.getResultCode() != 0) {
                 JSONObject eventManagementObj = getPaymentTemplateObj.getJSONObject("eventManagement");
                 getPaymentTempLatesResponseModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                 getPaymentTempLatesResponseModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                 return getPaymentTempLatesResponseModel;
             }
             getPaymentTempLatesResponseModel.responsePublicModel.setTransactionId(getPaymentTemplateObj.optString("transactionId"));
             
             JSONArray paymentTemplatesArray = getPaymentTemplateObj.getJSONArray("paymentTemplateList");
             for(int i=0;i<paymentTemplatesArray.length();i++){
                 PaymentTemplate paymentTemplate = new  PaymentTemplate();
                 paymentTemplate.setBillType(paymentTemplatesArray.getJSONObject(i).optString("billType"));
                 paymentTemplate.setHolderName(paymentTemplatesArray.getJSONObject(i).optString("holderName"));
                 paymentTemplate.setPostalAccount(paymentTemplatesArray.getJSONObject(i).optString("postalAccount"));
                 paymentTemplate.setTemplateName(paymentTemplatesArray.getJSONObject(i).optString("templateName"));
                 paymentTemplateList.add(paymentTemplate);
             }
             getPaymentTempLatesResponseModel.setPaymentTempLatesList(paymentTemplateList);
         }catch(Exception e){
             LogManager.e("ParseGetPaymentTempLatesResponse is error" + e.getLocalizedMessage());
         }
         return getPaymentTempLatesResponseModel;
     }
    
    
}
