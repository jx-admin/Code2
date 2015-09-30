
package com.accenture.mbank.model;

public class DashboardDataModel {
    private String lastUpdate;

    private double accountBalance;

    private double deposits;

    private double withdrawals;
    
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

}
