
package com.act.mbanking.bean;

import java.io.Serializable;
import java.util.List;

public class AggregatedAccount implements Serializable {
    private String aggregatedAccountType;

    private double availableBalance;

    private String accountCode;

    private double totalAmount;

    private String bankServiceCode;

    private List<ChartModel> charts;

    private List<DashboardDataModel> dashboardDataList;

    /**
     * @return the aggregatedAccountType
     */
    public String getAggregatedAccountType() {
        return aggregatedAccountType;
    }

    /**
     * @param aggregatedAccountType the aggregatedAccountType to set
     */
    public void setAggregatedAccountType(String aggregatedAccountType) {
        this.aggregatedAccountType = aggregatedAccountType;
    }

    /**
     * @return the availableBalance
     */
    public double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * @param availableBalance the availableBalance to set
     */
    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * @return the accountCode
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * @param accountCode the accountCode to set
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the bankServiceCode
     */
    public String getBankServiceCode() {
        return bankServiceCode;
    }

    /**
     * @param bankServiceCode the bankServiceCode to set
     */
    public void setBankServiceCode(String bankServiceCode) {
        this.bankServiceCode = bankServiceCode;
    }

    /**
     * @return the charts
     */
    public List<ChartModel> getCharts() {
        return charts;
    }

    /**
     * @param charts the charts to set
     */
    public void setCharts(List<ChartModel> charts) {
        this.charts = charts;
    }

    /**
     * @return the dashboardDataList
     */
    public List<DashboardDataModel> getDashboardDataList() {
        return dashboardDataList;
    }

    /**
     * @param dashboardDataList the dashboardDataList to set
     */
    public void setDashboardDataList(List<DashboardDataModel> dashboardDataList) {
        this.dashboardDataList = dashboardDataList;
    }

}
