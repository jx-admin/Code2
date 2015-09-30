
package com.act.mbanking.bean;

public class SyncDashboardDataAmount {
    private double amountSum;

    private double loansAmountSum;

    private double residual_Amount;

    private double payedLoans_Amount;

    /**
     * @return the amountSum
     */
    public double getAmountSum() {
        return amountSum;
    }

    /**
     * @return the loansAmountSum
     */
    public double getLoansAmountSum() {
        return loansAmountSum;
    }

    /**
     * @return the residual_Amount
     */
    public double getResidual_Amount() {
        return residual_Amount;
    }

    /**
     * @return the payedLoans_Amount
     */
    public double getPayedLoans_Amount() {
        return payedLoans_Amount;
    }

    /**
     * @param amountSum the amountSum to set
     */
    public void setAmountSum(double amountSum) {
        this.amountSum = amountSum;
    }

    /**
     * @param loansAmountSum the loansAmountSum to set
     */
    public void setLoansAmountSum(double loansAmountSum) {
        this.loansAmountSum = loansAmountSum;
    }

    /**
     * @param residual_Amount the residual_Amount to set
     */
    public void setResidual_Amount(double residual_Amount) {
        this.residual_Amount = residual_Amount;
    }

    /**
     * @param payedLoans_Amount the payedLoans_Amount to set
     */
    public void setPayedLoans_Amount(double payedLoans_Amount) {
        this.payedLoans_Amount = payedLoans_Amount;
    }

}
