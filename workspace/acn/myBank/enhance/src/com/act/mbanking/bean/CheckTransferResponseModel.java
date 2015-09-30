
package com.act.mbanking.bean;

public class CheckTransferResponseModel extends CheckTransactionResponseModel{
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel(); 
    
    private BankInformationModel bankInfomation;

    private double charges;

    private int resultCode;

    private String resultDescription;

    public BankInformationModel getBankInfomation() {
        return bankInfomation;
    }

    public void setBankInfomation(BankInformationModel bankInfomation) {
        this.bankInfomation = bankInfomation;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public boolean isSuccess() {
        if (resultCode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
