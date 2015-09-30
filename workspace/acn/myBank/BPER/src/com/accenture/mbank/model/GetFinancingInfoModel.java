
package com.accenture.mbank.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author seekting.x.zhang
 */
public class GetFinancingInfoModel implements Serializable{
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private String holder;

    private String bankCode;

    private String serviceCategory;

    private String bankServiceCode;

    private String branchCode;

    private String accountCode;

    private String ibanCode;

    private String numPaymentsRemaning;

    private String disbursementDate;

    private String endDate;

    private String financingType;

    private String rate;

    private String rateType;

    private String benchmarksValue;

    private String benchmarksDesc;

    private String lastUpdate;

    private String totalAmountl;

    private String residueAmount;

    private String moreValues;
    
    private String duration;
    
    private boolean isWarranty; 

    private double endRate;

    private List<InstallmentsModel> installments;

    /**
     * @return benchmarksValue
     */
    public String getBenchmarksValue() {
        return benchmarksValue;
    }

    /**
     * @param benchmarksValue 要设置的 benchmarksValue
     */
    public void setBenchmarksValue(String benchmarksValue) {
        this.benchmarksValue = benchmarksValue;
    }

    /**
     * @return benchmarksDesc
     */
    public String getBenchmarksDesc() {
        return benchmarksDesc;
    }

    /**
     * @param benchmarksDesc 要设置的 benchmarksDesc
     */
    public void setBenchmarksDesc(String benchmarksDesc) {
        this.benchmarksDesc = benchmarksDesc;
    }

    /**
     * @return the holder
     */
    public String getHolder() {
        return holder;
    }

    /**
     * @param holder the holder to set
     */
    public void setHolder(String holder) {
        this.holder = holder;
    }

    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return the serviceCategory
     */
    public String getServiceCategory() {
        return serviceCategory;
    }

    /**
     * @param serviceCategory the serviceCategory to set
     */
    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    /**
     * @return the bankServiceCode
     */
    public String getBankServiceCode() {
        return bankServiceCode;
    }

    /**
     * @param bankServiceCode the bankServiceCode to set
     */
    public void setBankServiceCode(String bankServiceCode) {
        this.bankServiceCode = bankServiceCode;
    }

    /**
     * @return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
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
     * @return the ibanCode
     */
    public String getIbanCode() {
        return ibanCode;
    }

    /**
     * @param ibanCode the ibanCode to set
     */
    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    /**
     * @return the numPaymentsRemaning
     */
    public String getNumPaymentsRemaning() {
        return numPaymentsRemaning;
    }

    /**
     * @param numPaymentsRemaning the numPaymentsRemaning to set
     */
    public void setNumPaymentsRemaning(String numPaymentsRemaning) {
        this.numPaymentsRemaning = numPaymentsRemaning;
    }

    /**
     * @return the disbursementDate
     */
    public String getDisbursementDate() {
        return disbursementDate;
    }

    /**
     * @param disbursementDate the disbursementDate to set
     */
    public void setDisbursementDate(String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the financingType
     */
    public String getFinancingType() {
        return financingType;
    }

    /**
     * @param financingType the financingType to set
     */
    public void setFinancingType(String financingType) {
        this.financingType = financingType;
    }

    /**
     * @return the rate
     */
    public String getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(String rate) {
        this.rate = rate;
    }

    /**
     * @return the rateType
     */
    public String getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

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
     * @return the totalAmountl
     */
    public String getTotalAmountl() {
        return totalAmountl;
    }

    /**
     * @param totalAmountl the totalAmountl to set
     */
    public void setTotalAmountl(String totalAmountl) {
        this.totalAmountl = totalAmountl;
    }

    /**
     * @return the residueAmount
     */
    public String getResidueAmount() {
        return residueAmount;
    }

    /**
     * @param residueAmount the residueAmount to set
     */
    public void setResidueAmount(String residueAmount) {
        this.residueAmount = residueAmount;
    }

    /**
     * @return the moreValues
     */
    public String getMoreValues() {
        return moreValues;
    }

    /**
     * @param moreValues the moreValues to set
     */
    public void setMoreValues(String moreValues) {
        this.moreValues = moreValues;
    }

    /**
     * @return the installments
     */
    public List<InstallmentsModel> getInstallments() {
        return installments;
    }

    /**
     * @param installments the installments to set
     */
    public void setInstallments(List<InstallmentsModel> installments) {
        this.installments = installments;
    }

    public double getEndRate() {
		return endRate;
	}

	public void setEndRate(double endRate) {
		this.endRate = endRate;
	}

	@Override
    public String toString() {
        return "GetFinancingInfoModel [responsePublicModel=" + responsePublicModel + ", holder="
                + holder + ", bankCode=" + bankCode + ", serviceCategory=" + serviceCategory
                + ", bankServiceCode=" + bankServiceCode + ", branchCode=" + branchCode
                + ", accountCode=" + accountCode + ", ibanCode=" + ibanCode
                + ", numPaymentsRemaning=" + numPaymentsRemaning + ", disbursementDate="
                + disbursementDate + ", endDate=" + endDate + ", financingType=" + financingType
                + ", rate=" + rate + ", rateType=" + rateType + ", lastUpdate=" + lastUpdate
                + ", totalAmountl=" + totalAmountl + ", residueAmount=" + residueAmount
                + ", moreValues=" + moreValues + ", installments=" + installments + "]";
    }

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public boolean isWarranty() {
		return isWarranty;
	}

	public void setWarranty(boolean isWarranty) {
		this.isWarranty = isWarranty;
	}

}
