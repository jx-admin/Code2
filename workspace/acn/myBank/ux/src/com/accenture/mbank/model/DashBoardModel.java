
package com.accenture.mbank.model;

import java.util.List;

public class DashBoardModel {
    private List<DashboardDataModel> dashboardDataList;

    private double availableBalance;

    private String accountCode;

    private String personalizedName;

    private double accountBalance;

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
     * @return the personalizedName
     */
    public String getPersonalizedName() {
        return personalizedName;
    }

    /**
     * @param personalizedName the personalizedName to set
     */
    public void setPersonalizedName(String personalizedName) {
        this.personalizedName = personalizedName;
    }

    /**
     * @return the accountBalance
     */
    public double getAccountBalance() {
        return accountBalance;
    }

    /**
     * @param accountBalance the accountBalance to set
     */
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "DashBoardModel [dashboardDataList=" + dashboardDataList.size()
                + ", availableBalance=" + availableBalance + ", accountCode=" + accountCode
                + ", personalizedName=" + personalizedName + ", accountBalance=" + accountBalance
                + "]";
    }
}
