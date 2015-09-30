
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.model.GetDashBoardDataResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class GetDashBoardDataJson {
    public static String getDashBoardDataReportProtocal(RequestPublicModel publicModel,
            int granularity, int dashboardType, String accountCode) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getDashBoardDataObj = new JSONObject();
            getDashBoardDataObj.put("serviceType", ServiceType.getDashBoardData);
            getDashBoardDataObj.put("bankName", publicModel.getBankName());
            getDashBoardDataObj.put("enterpriseId", publicModel.getEnterpriseId());
            getDashBoardDataObj.put("customerCode", publicModel.getCustomerCode());
            getDashBoardDataObj.put("channel", publicModel.getChannel());
            getDashBoardDataObj.put("userAgent", publicModel.getUserAgent());
            getDashBoardDataObj.put("sessionId", publicModel.getSessionId());
            getDashBoardDataObj.put("token", publicModel.getToken());
            getDashBoardDataObj.put("accountCode", accountCode);
            getDashBoardDataObj.put("granularity", granularity);
            getDashBoardDataObj.put("dashboardType", dashboardType);

            jsonObj.put("GetDashboardDataRequest", getDashBoardDataObj);

            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getDashBoardDataReportProtocal " + e.getLocalizedMessage());
        }
        System.out.println("cards" + result);
        return result;
    }

    public static GetDashBoardDataResponseModel ParseGetDashBoardDataResponse(String json) {
        GetDashBoardDataResponseModel getDashBoardDataResponse = new GetDashBoardDataResponseModel();
        if (json == null) {
            LogManager.d("ParseGetDashBoardDataResponse" + "json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getDashBoardDataObj = jsonObj.getJSONObject("GetDashboardDataResponse");

            getDashBoardDataResponse.responsePublicModel.setResultCode(getDashBoardDataObj
                    .getInt("resultCode"));
            getDashBoardDataResponse.responsePublicModel.setResultDescription(getDashBoardDataObj
                    .optString("resultDescription"));
            if (getDashBoardDataResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getDashBoardDataObj
                        .getJSONObject("eventManagement");
                getDashBoardDataResponse.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                getDashBoardDataResponse.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getDashBoardDataResponse;
            }

            getDashBoardDataResponse.responsePublicModel.setTransactionId(getDashBoardDataObj
                    .optString("transactionId"));

            JSONArray dashboards = getDashBoardDataObj.getJSONArray("dashboards");
            List<DashBoardModel> dashboardsList = new ArrayList<DashBoardModel>();
            for (int i = 0; i < dashboards.length(); i++) {
                DashBoardModel dashBoard = new DashBoardModel();
                dashBoard
                        .setAccountBalance(dashboards.getJSONObject(i).optDouble("accountBalance"));
                dashBoard.setAccountCode(dashboards.getJSONObject(i).optString("accountCode"));
                dashBoard.setAvailableBalance(dashboards.getJSONObject(i).optDouble(
                        "availableBalance"));
                dashBoard.setPersonalizedName(dashboards.getJSONObject(i).optString(
                        "personalizedName"));

                JSONArray dashboardDataArray = dashboards.getJSONObject(i).getJSONArray(
                        "dashboardDataList");
                List<DashboardDataModel> dashboardDataList = new ArrayList<DashboardDataModel>();
                for (int j = 0; j < dashboardDataArray.length(); j++) {
                    DashboardDataModel dashboardData = new DashboardDataModel();

                    dashboardData.setAccountBalance(dashboardDataArray.getJSONObject(j).optDouble(
                            "accountBalance"));
                    dashboardData.setDeposits(dashboardDataArray.getJSONObject(j).optDouble(
                            "deposits"));
                    dashboardData.setLastUpdate(dashboardDataArray.getJSONObject(j).optString(
                            "lastUpdate"));
                    dashboardData.setWithdrawals(dashboardDataArray.getJSONObject(j).optDouble(
                            "withdrawals"));
                    dashboardDataList.add(dashboardData);
                }
                dashBoard.setDashboardDataList(dashboardDataList);
                dashboardsList.add(dashBoard);
            }
            getDashBoardDataResponse.setDashboardsList(dashboardsList);

        } catch (Exception e) {
            LogManager.e("ParseGetDashBoardDataResponse " + e.getLocalizedMessage());
        }
        System.out.println("getDashBoardDataResponse" + getDashBoardDataResponse);
        return getDashBoardDataResponse;
    }
}
