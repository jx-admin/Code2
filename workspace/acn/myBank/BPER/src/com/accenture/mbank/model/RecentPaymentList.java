
package com.accenture.mbank.model;

import java.util.List;

public class RecentPaymentList {
    private List<RecentTransferModel> recentTransferList;
    

    private String accountCode;

    /**
     * @return the recentTransferList
     */
    public List<RecentTransferModel> getRecentTransferList() {
        return recentTransferList;
    }

    /**
     * @param recentTransferList the recentTransferList to set
     */
    public void setRecentTransferList(List<RecentTransferModel> recentTransferList) {
        this.recentTransferList = recentTransferList;
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

}
