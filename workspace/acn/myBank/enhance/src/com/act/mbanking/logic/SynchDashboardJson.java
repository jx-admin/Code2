
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.Contants;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.SynchDashBoardModel;
import com.act.mbanking.utils.LogManager;

public class SynchDashboardJson {
    /**
     * @param publicModel
     * @param aggregatedAccountType 使用{AggregatedAccountType 这个类}
     * @return
     */
    public static String synchDashboardReportProtocal(RequestPublicModel publicModel,
            int aggregatedAccountType, String lastUpdate) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject synchDashboardObj = new JSONObject();
            synchDashboardObj.put("bankName", publicModel.getBankName());
            synchDashboardObj.put("channel", publicModel.getChannel());
            synchDashboardObj.put("customerCode", publicModel.getCustomerCode());
            synchDashboardObj.put("enterpriseId", publicModel.getEnterpriseId());
            synchDashboardObj.put("serviceType", ServiceType.SYNCHDASHBOARD);
            synchDashboardObj.put("userAgent", publicModel.getUserAgent());
            synchDashboardObj.put("sessionId", publicModel.getSessionId());
            if (lastUpdate != null || !lastUpdate.equals("")) {
                synchDashboardObj.put("lastUpdate", lastUpdate);
            }
            synchDashboardObj.put("token", publicModel.getToken());
            synchDashboardObj.put("aggregatedAccountType", aggregatedAccountType);

            jsonObj.put("SynchDashboardRequest", synchDashboardObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("SynchDashboardObj is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static SynchDashBoardModel parseSynchDashBoardResponse(String json) {
        SynchDashBoardModel synchDashBoardModel = new SynchDashBoardModel();
        List<AggregatedAccount> aggregatedAccountsList = new ArrayList<AggregatedAccount>();
        if (json == null) {
            LogManager.d("parseSynchDashBoardResponse" + "json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject synchDashboardObj = jsonObj.getJSONObject("SynchDashboardResponse");

            synchDashBoardModel.responsePublicModel.setResultCode(synchDashboardObj
                    .getInt("resultCode"));
            synchDashBoardModel.responsePublicModel.setResultDescription(synchDashboardObj
                    .optString("resultDescription"));
            if (synchDashBoardModel.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = synchDashboardObj.getJSONObject("eventManagement");
                synchDashBoardModel.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                synchDashBoardModel.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return synchDashBoardModel;
            }

            synchDashBoardModel.responsePublicModel.setTransactionId(synchDashboardObj
                    .optString("transactionId"));
            JSONArray aggregatedAccountsArray = synchDashboardObj
                    .optJSONArray("aggregatedAccounts");
            for (int i = 0; i < aggregatedAccountsArray.length(); i++) {
                AggregatedAccount aggregatedAccount = new AggregatedAccount();
                aggregatedAccount.setAccountCode(aggregatedAccountsArray.getJSONObject(i).optString("accountCode"));
                aggregatedAccount.setAggregatedAccountType(aggregatedAccountsArray.getJSONObject(i).optString("aggregatedAccountType"));
                aggregatedAccount.setAvailableBalance(aggregatedAccountsArray.getJSONObject(i).optDouble("availableBalance"));
                aggregatedAccount.setBankServiceCode(aggregatedAccountsArray.getJSONObject(i).optString("bankServiceCode"));
                aggregatedAccount.setTotalAmount(aggregatedAccountsArray.getJSONObject(i).optDouble("totalAmount"));
                
                JSONArray dashboardDataArray = aggregatedAccountsArray.getJSONObject(i).optJSONArray("dashboardDataList");
            	List<DashboardDataModel> dashboardDataList = new ArrayList<DashboardDataModel>();
            	for (int j = 0; j < dashboardDataArray.length(); j++) {
            		DashboardDataModel dashboardDataModel = new DashboardDataModel();
            		dashboardDataModel.setAccountBalance(dashboardDataArray.getJSONObject(j).optDouble("accountBalance"));
            		dashboardDataModel.setDeposits(dashboardDataArray.getJSONObject(j).optDouble("deposits"));
            		dashboardDataModel.setInvestmentId(dashboardDataArray.getJSONObject(j).optString("investmentId"));
            		dashboardDataModel.setLastUpdate(dashboardDataArray.getJSONObject(j).optString("lastUpdate"));
            		dashboardDataModel.setResidueAmount(dashboardDataArray.getJSONObject(j).optDouble("residueAmount"));
            		dashboardDataModel.setTotalPortfolio(dashboardDataArray.getJSONObject(j).optDouble("totalPortfolio"));
            		dashboardDataModel.setWithdrawals(dashboardDataArray.getJSONObject(j).optDouble("withdrawals"));
            		dashboardDataList.add(dashboardDataModel);
            	}
            	aggregatedAccount.setDashboardDataList(dashboardDataList);       
                
                add(aggregatedAccount);
                aggregatedAccountsList.add(aggregatedAccount);
            }

            synchDashBoardModel.setAggregatedAccountsList(aggregatedAccountsList);
        } catch (Exception e) {
            LogManager.e("parseSynchDashBoardResponse is error" + e.getLocalizedMessage());
        }
        return synchDashBoardModel;
    }

    /**
     * @param
     */
    public static void add(AggregatedAccount aggregatedAccount) {
        for (int i = 0; i < Contants.getUserInfo.getAccountList().size(); i++) {
            //调试一下看看这块儿怎么走的.
            if (Contants.getUserInfo.getAccountList().get(i).getAccountCode().equals(aggregatedAccount.getAccountCode())) {
                List<AggregatedAccount> dashboardAggregatedAccountsList = Contants.getUserInfo.getAccountList().get(i).dashboardAggregatedAccountsList;
                if(dashboardAggregatedAccountsList !=null && dashboardAggregatedAccountsList.size() !=0){
                    for(int j=0;j<dashboardAggregatedAccountsList.size();j++){
                        List<DashboardDataModel> dashBoardDataList = dashboardAggregatedAccountsList.get(j).getDashboardDataList();
                        List<DashboardDataModel> newDashBoardDataList = aggregatedAccount.getDashboardDataList();
                        if(dashBoardDataList !=null){
                            for(DashboardDataModel dashboard : newDashBoardDataList){
//                                if(aggregatedAccount.getAggregatedAccountType().equals(AggregatedAccountType.INVESTMENT+"")){
//                                    for(int a = 0;a<dashBoardDataList.size();a++){
//                                        String oldLastUpdate = TimeUtil.changeFormattrString(dashBoardDataList.get(a).getLastUpdate(), TimeUtil.dateFormat2, TimeUtil.dateFormat4);
//                                        String newLastUpdate = TimeUtil.changeFormattrString(dashboard.getLastUpdate(), TimeUtil.dateFormat2, TimeUtil.dateFormat4);
//                                        String oldInvestmentId = dashBoardDataList.get(a).getInvestmentId();
//                                        String newInvestmentId = dashboard.getInvestmentId();
//                                        if(oldLastUpdate.equals(newLastUpdate) && oldInvestmentId.equals(newInvestmentId)){
//                                            dashBoardDataList.remove(a);
//                                            dashBoardDataList.add(a,dashboard);
//                                        }
//                                    }//
//                                    if (!dashBoardDataList.contains(dashboard)) {
//                                        dashBoardDataList.add(dashboard);
//                                    }   
//                                }else{
                                    if (!dashBoardDataList.contains(dashboard)) {
                                        dashBoardDataList.add(dashboard);
                                    }                                    
//                                }
                            }
                        }
                    }
                }else{
                    Contants.getUserInfo.getAccountList().get(i).dashboardAggregatedAccountsList.add(aggregatedAccount);
                }
            }
        }
    }

    /**
     * 将数据添加至getUserInfo下的AccountList模块下面
     * 
     * @param aggregatedAccountsList
     */
    public static void add(List<AggregatedAccount> aggregatedAccountsList) {
        for (int i = 0; i < Contants.getUserInfo.getAccountList().size(); i++) {
            for (int j = 0; j < aggregatedAccountsList.size(); j++) {
                if (Contants.getUserInfo.getAccountList().get(i).getAccountCode()
                        .equals(aggregatedAccountsList.get(j).getAccountCode())) {
                    Contants.getUserInfo.getAccountList().get(i)
                            .setDashboardAggregatedAccountsList(aggregatedAccountsList);
                }
            }
        }
    }
}
