
package com.act.mbanking.bean;

import java.util.List;

public class SynchChartModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public double availableBalanceSum;

    private List<AggregatedAccount> aggregatedAccountsList;

    /**
     * @return the aggregatedAccountsList
     */
    public List<AggregatedAccount> getAggregatedAccountsList() {
        return aggregatedAccountsList;
    }

    /**
     * @param aggregatedAccountsList the aggregatedAccountsList to set
     */
    public void setAggregatedAccountsList(List<AggregatedAccount> aggregatedAccountsList) {
        this.aggregatedAccountsList = aggregatedAccountsList;
    }

    /**
     * @return the availableBalanceSum
     */
    public double isAvailableBalanceSum() {
        return availableBalanceSum;
    }

    /**
     * @param availableBalanceSum the availableBalanceSum to set
     */
    public void setAvailableBalanceSum(double availableBalanceSum) {
        this.availableBalanceSum = availableBalanceSum;
    }
}
