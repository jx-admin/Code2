
package com.accenture.mbank.model;

import java.io.Serializable;

public class RecentTransferModel implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String operationDate;

    private String account;

    private String type;

    private double amount;

    private String description;

    private String beneficiaryAccount;

    private String beneficiaryCardNumber;

    private String purposeCurrency;
    
    private String beneficiaryNumber;
    
    private String beneficiaryProvider;
    
    private String transferState;
    
    private String beneficiaryName;
    
    private String beneficiaryIban;
    
    private String beneficiaryState;
    
    public boolean isExpanded;

    /**
     * @return the beneficiaryNumber
     */
    public String getBeneficiaryNumber() {
        return beneficiaryNumber;
    }

    /**
     * @param beneficiaryNumber the beneficiaryNumber to set
     */
    public void setBeneficiaryNumber(String beneficiaryNumber) {
        this.beneficiaryNumber = beneficiaryNumber;
    }

    /**
     * @return the beneficiaryProvider
     */
    public String getBeneficiaryProvider() {
        return beneficiaryProvider;
    }

    /**
     * @param beneficiaryProvider the beneficiaryProvider to set
     */
    public void setBeneficiaryProvider(String beneficiaryProvider) {
        this.beneficiaryProvider = beneficiaryProvider;
    }

    /**
     * @return the transferState
     */
    public String getTransferState() {
        return transferState;
    }

    /**
     * @param transferState the transferState to set
     */
    public void setTransferState(String transferState) {
        this.transferState = transferState;
    }

    /**
     * @return the beneficiaryName
     */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    /**
     * @param beneficiaryName the beneficiaryName to set
     */
    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    /**
     * @return the beneficiaryIban
     */
    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    /**
     * @param beneficiaryIban the beneficiaryIban to set
     */
    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    /**
     * @return the beneficiaryState
     */
    public String getBeneficiaryState() {
        return beneficiaryState;
    }

    /**
     * @param beneficiaryState the beneficiaryState to set
     */
    public void setBeneficiaryState(String beneficiaryState) {
        this.beneficiaryState = beneficiaryState;
    }

    public String getPurposeCurrency() {
        return purposeCurrency;
    }

    public void setPurposeCurrency(String purposeCurrency) {
        this.purposeCurrency = purposeCurrency;
    }

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public String getBeneficiaryCardNumber() {
        return beneficiaryCardNumber;
    }

    public void setBeneficiaryCardNumber(String beneficiaryCardNumber) {
        this.beneficiaryCardNumber = beneficiaryCardNumber;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
