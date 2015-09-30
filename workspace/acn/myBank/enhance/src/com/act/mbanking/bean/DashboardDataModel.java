
package com.act.mbanking.bean;

import java.io.Serializable;

public class DashboardDataModel implements Serializable {
    private String lastUpdate;

    private double accountBalance;

    private double deposits;

    private double withdrawals;

    private String investmentId;

    private double totalPortfolio;

    private double residueAmount;

    /**
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    /**
     * @return the deposits
     */
    public double getDeposits() {
        return deposits;
    }

    /**
     * @param deposits the deposits to set
     */
    public void setDeposits(double deposits) {
        this.deposits = deposits;
    }

    /**
     * @return the withdrawals
     */
    public double getWithdrawals() {
        return withdrawals;
    }

    /**
     * @param withdrawals the withdrawals to set
     */
    public void setWithdrawals(double withdrawals) {
        this.withdrawals = withdrawals;
    }

    /**
     * @return the investmentId
     */
    public String getInvestmentId() {
        return investmentId;
    }

    /**
     * @param investmentId the investmentId to set
     */
    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    /**
     * @return the totalPortfolio
     */
    public double getTotalPortfolio() {
        return totalPortfolio;
    }

    /**
     * @param totalPortfolio the totalPortfolio to set
     */
    public void setTotalPortfolio(double totalPortfolio) {
        this.totalPortfolio = totalPortfolio;
    }

    /**
     * @return the residueAmount
     */
    public double getResidueAmount() {
        return residueAmount;
    }

    /**
     * @param residueAmount the residueAmount to set
     */
    public void setResidueAmount(double residueAmount) {
        this.residueAmount = residueAmount;
    }

}
