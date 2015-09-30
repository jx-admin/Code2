
package com.accenture.mbank.model;


public class CheckSimTopUpResponseModel extends CheckTransactionResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private double charges;

    private String branchDescription;

    private String pinIsNotRequired;

    private int count;
    
    private boolean otpByEmail;

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public String getBranchDescription() {
        return branchDescription;
    }

    public void setBranchDescription(String branchDescription) {
        this.branchDescription = branchDescription;
    }

    public String getPinIsNotRequired() {
        return pinIsNotRequired;
    }

    public void setPinIsNotRequired(String pinIsNotRequired) {
        this.pinIsNotRequired = pinIsNotRequired;
    }

    public int getCount() {
        return count;
    }

    public boolean isOtpByEmail() {
		return otpByEmail;
	}

	public void setOtpByEmail(boolean otpByEmail) {
		this.otpByEmail = otpByEmail;
	}

	public void setCount(int count) {
        this.count = count;
    }

}
