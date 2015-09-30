
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.Contants;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.SynchChartModel;
import com.act.mbanking.utils.LogManager;

public class SynchChartJson {
    /**
     * @param publicModel
     * @param aggregatedAccountType 使用{AggregatedAccountType 这个类}
     * @return
     */
    public static String synchChartReportProtocal(RequestPublicModel publicModel,
            int aggregatedAccountType,String lastUpdate) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject synchChartObj = new JSONObject();
            synchChartObj.put("bankName", publicModel.getBankName());
            synchChartObj.put("channel", publicModel.getChannel());
            synchChartObj.put("customerCode", publicModel.getCustomerCode());
            synchChartObj.put("enterpriseId", publicModel.getEnterpriseId());
            synchChartObj.put("serviceType", ServiceType.SYNCHCHART);
            synchChartObj.put("userAgent", publicModel.getUserAgent());
            synchChartObj.put("sessionId", publicModel.getSessionId());
            synchChartObj.put("token", publicModel.getToken());
            if(lastUpdate !=null || !lastUpdate.equals("")){
                synchChartObj.put("lastUpdate", lastUpdate);                
            }
            synchChartObj.put("aggregatedAccountType", aggregatedAccountType);

            jsonObj.put("SynchChartRequest", synchChartObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("synchChartReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static SynchChartModel parseSynchChartResponse(String json) {

        SynchChartModel synchChartModel = new SynchChartModel();
        List<AggregatedAccount> aggregatedAccountsList = new ArrayList<AggregatedAccount>();
        if (json == null) {
            LogManager.d("parseSynchChartResponse" + "json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject synchChartObj = jsonObj.getJSONObject("SynchChartResponse");

            synchChartModel.responsePublicModel.setResultCode(synchChartObj.getInt("resultCode"));
            synchChartModel.responsePublicModel.setResultDescription(synchChartObj.optString("resultDescription"));
            if (synchChartModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = synchChartObj.getJSONObject("eventManagement");
                synchChartModel.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                synchChartModel.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return synchChartModel;
            }

            synchChartModel.responsePublicModel.setTransactionId(synchChartObj.optString("transactionId"));
            JSONArray aggregatedAccountsArray = synchChartObj.optJSONArray("aggregatedAccounts");
            for (int i = 0; i < aggregatedAccountsArray.length(); i++) {
                AggregatedAccount aggregatedAccount = new AggregatedAccount();
                aggregatedAccount.setAccountCode(aggregatedAccountsArray.getJSONObject(i).optString("accountCode"));
                aggregatedAccount.setAggregatedAccountType(aggregatedAccountsArray.getJSONObject(i).optString("aggregatedAccountType"));
                aggregatedAccount.setAvailableBalance(aggregatedAccountsArray.getJSONObject(i).optDouble("availableBalance"));
                aggregatedAccount.setBankServiceCode(aggregatedAccountsArray.getJSONObject(i).optString("bankServiceCode"));
                aggregatedAccount.setTotalAmount(aggregatedAccountsArray.getJSONObject(i).optDouble("totalAmount"));
                JSONArray synchChartArray = aggregatedAccountsArray.getJSONObject(i).optJSONArray("charts");
                
                List<ChartModel> chartsList = new ArrayList<ChartModel>();
                for (int j = 0; j < synchChartArray.length(); j++) {
                    ChartModel chartModel = new ChartModel();
                    chartModel.setDate(synchChartArray.getJSONObject(j).optString("date"));
                    chartModel.setInvestmentId(synchChartArray.getJSONObject(j).optInt("investmentId"));
                    chartModel.setValue(synchChartArray.getJSONObject(j).optDouble("value"));
                    chartsList.add(chartModel);
                }
                aggregatedAccount.setCharts(chartsList);
                aggregatedAccountsList.add(aggregatedAccount);
                
                add(aggregatedAccount);
            }
            synchChartModel.setAggregatedAccountsList(aggregatedAccountsList);
        } catch (Exception e) {
            LogManager.e("parseSynchChartResponse is error" + e.getLocalizedMessage());
        }

        return synchChartModel;
    }
    /**
     * @param
     */
    public static void add(AggregatedAccount aggregatedAccount){
        for(int i=0;i<Contants.getUserInfo.getAccountList().size();i++){
            if(Contants.getUserInfo.getAccountList().get(i).getAccountCode().equals(aggregatedAccount.getAccountCode())){
                Contants.getUserInfo.getAccountList().get(i).chartAggregatedAccountsList.add(aggregatedAccount);
            }
        }
    }
}
