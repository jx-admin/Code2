
package com.accenture.mbank.model;


public class CheckRechargeCardResponseModel extends CheckTransactionResponseModel{
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private double charges;

    private String executionDate;

    private String srcbankName;

    private String srcBranchName;

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getSrcbankName() {
        return srcbankName;
    }

    public void setSrcbankName(String srcbankName) {
        this.srcbankName = srcbankName;
    }

    public String getSrcBranchName() {
        return srcBranchName;
    }

    public void setSrcBranchName(String srcBranchName) {
        this.srcBranchName = srcBranchName;
    }

}
