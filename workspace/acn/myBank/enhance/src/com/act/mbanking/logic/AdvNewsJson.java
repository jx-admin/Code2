
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.Contants;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AdvNewsResponseModel;
import com.act.mbanking.bean.ListAdvNewsModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

/**广告新闻
 * @author junxu.wang
 *
 */
public class AdvNewsJson {
    /**
     * 
     * @param publicModel
     * @param abi Contants.abi
     * @return
     */
    public static String AdvNewsReportProtocal(RequestPublicModel publicModel, String abi) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject advNewsObj = new JSONObject();
            advNewsObj.put("serviceType", ServiceType.getAdvNews);
            advNewsObj.put("bankName", publicModel.getBankName());
            advNewsObj.put("enterpriseId", publicModel.getEnterpriseId());
            advNewsObj.put("customerCode", publicModel.getCustomerCode());
            advNewsObj.put("channel", publicModel.getChannel());
            advNewsObj.put("userAgent", publicModel.getUserAgent());
            advNewsObj.put("abi", abi);

            jsonObj.put("GetAdvNewsRequest", advNewsObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("AdvNewsReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    public static AdvNewsResponseModel ParseAdvNewsResponse(String json){
        AdvNewsResponseModel advNewsResponse = new AdvNewsResponseModel();
         List<ListAdvNewsModel> listAdvNews = new ArrayList<ListAdvNewsModel>();
         
         if (json == null) {
             return null;
         }
         
         try {
             JSONObject jsonObj = new JSONObject(json);
             JSONObject advNewsObj = jsonObj.getJSONObject("GetAdvNewsResponse");
             
             advNewsResponse.responsePublicModel.setResultCode(advNewsObj.getInt("resultCode"));
             advNewsResponse.responsePublicModel.setResultDescription(advNewsObj.optString("resultDescription"));
             if (advNewsResponse.responsePublicModel.getResultCode() != 0) {
                 JSONObject eventManagementObj = advNewsObj.getJSONObject("eventManagement");
                 advNewsResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                 advNewsResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                 return advNewsResponse;
             }
             
             advNewsResponse.responsePublicModel.setTransactionId(advNewsObj.optString("transactionId"));
             
             JSONArray listAdvNewsArray = advNewsObj.getJSONArray("listAdvNews");
             for(int i=0;i<listAdvNewsArray.length();i++){
                 ListAdvNewsModel advNews = new ListAdvNewsModel();
                 advNews.setDate(listAdvNewsArray.getJSONObject(i).optString("date"));
                 advNews.setImageRef(Contants.URL_PREFIX_ROME + listAdvNewsArray.getJSONObject(i).optString("imageRef"));
                 advNews.setImageRefThumb(Contants.URL_PREFIX_ROME + listAdvNewsArray.getJSONObject(i).optString("imageRefThumb"));
                 advNews.setText(listAdvNewsArray.getJSONObject(i).optString("text"));
                 advNews.setTitle(listAdvNewsArray.getJSONObject(i).optString("title"));
                 listAdvNews.add(advNews);
             }
             advNewsResponse.setListAdvNews(listAdvNews);
         }catch(Exception e){
             LogManager.e("ParseHelpItemResponse is error" + e.getLocalizedMessage());
         }
         return advNewsResponse;
     }
}
