
package com.act.mbanking.bean;

public class DepositInfoModel {
    private String accountAlias;

    private GetDepositInfoResponseModel getDepositInfo;

    /**
     * @return the accountAlias
     */
    public String getAccountAlias() {
        return accountAlias;
    }

    /**
     * @param accountAlias the accountAlias to set
     */
    public void setAccountAlias(String accountAlias) {
        this.accountAlias = accountAlias;
    }

    /**
     * @return the getDepositInfo
     */
    public GetDepositInfoResponseModel getGetDepositInfo() {
        return getDepositInfo;
    }

    /**
     * @param getDepositInfo the getDepositInfo to set
     */
    public void setGetDepositInfo(GetDepositInfoResponseModel getDepositInfo) {
        this.getDepositInfo = getDepositInfo;
    }

}
