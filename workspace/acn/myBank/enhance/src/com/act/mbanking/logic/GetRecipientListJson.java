package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.Account;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.Balance;
import com.act.mbanking.bean.BankRecipient;
import com.act.mbanking.bean.CardRecipient;
import com.act.mbanking.bean.GetRecipientListModel;
import com.act.mbanking.bean.PhoneRecipient;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetRecipientListJson {
    
    /**
     * @param publicModel
     * @param paymentMethodId Payment method identifier ( allow value: 1=balance from credit card, 2=balance from bank)
     * @return
     */
    public static String getBalanceReportProtocal(RequestPublicModel publicModel,String accountCode,String paymentMethodId){
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecipientObj = new JSONObject();
            getRecipientObj.put("bankName", publicModel.getBankName());
            getRecipientObj.put("serviceType", "1003");//ServiceType.getRecipientList);
            getRecipientObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecipientObj.put("paymentMethod", paymentMethodId);
            if(accountCode!=null)
            getRecipientObj.put("accountCode", accountCode);
            getRecipientObj.put("channel", publicModel.getChannel());
            getRecipientObj.put("userAgent", publicModel.getUserAgent());
            getRecipientObj.put("customerCode", publicModel.getCustomerCode());
            getRecipientObj.put("sessionId", publicModel.getSessionId());
            getRecipientObj.put("token", publicModel.getToken());
            
            jsonObj.put("GetBalanceRequest", getRecipientObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getRecipientListReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    public static Balance parseBalanceReportResponseList(String json,List<AccountsModel> ls){
        Balance mBalance=null;
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject GetBalanceRequest = jsonObj.getJSONObject("GetBalanceResponse");
            if(GetBalanceRequest.optInt("resultCode")==0){
            JSONArray accounts=GetBalanceRequest.getJSONArray("accounts");
            JSONObject jsonObj_;
            
            for(int i=0;i<accounts.length();i++){
                mBalance=new Balance();
                jsonObj_= accounts.getJSONObject(i);
                mBalance.setHolder(jsonObj_.optString("holder"));
                mBalance.setAvailableBalance(jsonObj_.optDouble("availableBalance"));
                mBalance.setAccountBalance(jsonObj_.optDouble("accountBalance"));
                mBalance.setLastUpdate(jsonObj_.optString("lastUpdate"));
                mBalance.setPersonalizedName(jsonObj_.optString("personalizedName"));
                mBalance.setCurrency(jsonObj_.optString("currency"));
                mBalance.setAccountCode(jsonObj_.optInt("accountCode"));
//                Log.d("balance","balance:"+(jsonObj_.optInt("accountCode")));
                for(int j=ls.size()-1;j>=0;j--){
//                    Log.d("balance","account:"+ls.get(j).getAccountCode());
                    if(ls.get(j).getAccountCode().equals(mBalance.getAccountCode()+"")){
                        ls.get(j).setBalance(mBalance);
                        break;
                    }
                }
            }
            }
                

        }catch(Exception e){
            e.printStackTrace();
            LogManager.e("parseGetRecipientListResponse is error" + e.getLocalizedMessage());
        }
        return mBalance;
    }
    
    
    public static String getRecipientListReportProtocal(RequestPublicModel publicModel){
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecipientObj = new JSONObject();
            getRecipientObj.put("serviceType", ServiceType.getRecipientList);
            getRecipientObj.put("bankName", publicModel.getBankName());
            getRecipientObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecipientObj.put("customerCode", publicModel.getCustomerCode());
            getRecipientObj.put("channel", publicModel.getChannel());
            getRecipientObj.put("userAgent", publicModel.getUserAgent());
            getRecipientObj.put("sessionId", publicModel.getSessionId());
            getRecipientObj.put("token", publicModel.getToken());
            
            jsonObj.put("GetRecipientListRequest", getRecipientObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getRecipientListReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    public static GetRecipientListModel parseGetRecipientListResponse(String json){
        GetRecipientListModel getRecipientList = new GetRecipientListModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getRecipientListObj = jsonObj.getJSONObject("GetRecipientListResponse");

            getRecipientList.responsePublicModel.setResultCode(getRecipientListObj.getInt("resultCode"));
            getRecipientList.responsePublicModel.setResultDescription(getRecipientListObj.optString("resultDescription"));
            if (getRecipientList.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getRecipientListObj.getJSONObject("eventManagement");
                getRecipientList.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getRecipientList.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getRecipientList;
            }
            getRecipientList.responsePublicModel.setTransactionId(getRecipientListObj.optString("transactionId"));
            
            JSONArray bankRecipientListArray = getRecipientListObj.getJSONArray("bankRecipientList");
            List<Account> bankRecipientList = new ArrayList<Account>();
            for(int i=0;i<bankRecipientListArray.length();i++){
                BankRecipient bankRecipient = new BankRecipient();
                bankRecipient.setId(bankRecipientListArray.getJSONObject(i).optString("id"));
                
                JSONObject bankObj = bankRecipientListArray.getJSONObject(i).getJSONObject("bank");
                bankRecipient.setIbanCode(bankObj.optString("ibanCode"));
                bankRecipient.setName(bankObj.optString("name"));
                bankRecipientList.add(bankRecipient);
            }
            getRecipientList.setBankRecipientList(bankRecipientList);
            
            JSONArray cardRecipientArray = getRecipientListObj.getJSONArray("cardRecipientList");
            List<CardRecipient> cardRecipientList = new ArrayList<CardRecipient>();
            for(int i =0;i<cardRecipientArray.length();i++){
                CardRecipient cardRecipient = new CardRecipient();
                cardRecipient.setId(cardRecipientArray.getJSONObject(i).optString("id"));
                
                JSONObject card = cardRecipientArray.getJSONObject(i).getJSONObject("card");
                cardRecipient.setCardNumber(card.optString("cardNumber"));
                cardRecipient.setName(card.optString("name"));
                cardRecipientList.add(cardRecipient);
            }
            getRecipientList.setCardRecipientList(cardRecipientList);
            
            JSONArray phoneRecipientArray = getRecipientListObj.getJSONArray("phoneRecipientList");
            List<PhoneRecipient> phoneRecipientList = new ArrayList<PhoneRecipient>();
            for(int i =0;i<phoneRecipientArray.length();i++){
                PhoneRecipient phoneRecipient = new PhoneRecipient();
                phoneRecipient.setId(phoneRecipientArray.getJSONObject(i).optString("id"));
                
                JSONObject phone = phoneRecipientArray.getJSONObject(i).getJSONObject("phone");
                phoneRecipient.setPhoneNumber(phone.optString("phoneNumber"));
                phoneRecipient.setName(phone.optString("name"));
                phoneRecipient.setProvider(phone.optString("provider"));
                phoneRecipientList.add(phoneRecipient);
            }
            getRecipientList.setPhoneRecipientList(phoneRecipientList);
        }catch(Exception e){
            LogManager.e("parseGetRecipientListResponse is error" + e.getLocalizedMessage());
        }
        return getRecipientList;
    }
}
