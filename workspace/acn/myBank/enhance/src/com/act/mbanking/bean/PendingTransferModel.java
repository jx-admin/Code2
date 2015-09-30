
package com.act.mbanking.bean;

import java.io.Serializable;

public class PendingTransferModel implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String transferId;

    private String customerCode;

    private String channel;

    private String type;

    private String accountCode;

    private String transferType;

    private double amount;

    private String executionDate;

    private String purposecurrency;

    private String purposeDescription;

    private String currency;

    private String beneficiaryTitle;

    private String beneficiaryName;

    private String beneficiaryIban;

    private String beneficiaryBic;

    private String beneficiaryProvider;

    private String beneficiaryPhoneNumber;

    private ChargeSizeModel chargeSizeModel;

    private String beneficiaryCardCode;

    private String beneficiaryCardName;

    private String beneficiaryCardTitle;

    private String beneficiaryCardNumber;

    private String billType;

    private String postalAccount;

    private String holderName;

    private String sender;

    private String address;

    private String city;

    private String district;

    private String postalCode;

    private String billNumber;

    /**
     * @return the transferId
     */
    public String getTransferId() {
        return transferId;
    }

    /**
     * @param transferId the transferId to set
     */
    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
     * @return the transferType
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the executionDate
     */
    public String getExecutionDate() {
        return executionDate;
    }

    /**
     * @param executionDate the executionDate to set
     */
    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * @return the purposecurrency
     */
    public String getPurposecurrency() {
        return purposecurrency;
    }

    /**
     * @param purposecurrency the purposecurrency to set
     */
    public void setPurposecurrency(String purposecurrency) {
        this.purposecurrency = purposecurrency;
    }

    /**
     * @return the purposeDescription
     */
    public String getPurposeDescription() {
        return purposeDescription;
    }

    /**
     * @param purposeDescription the purposeDescription to set
     */
    public void setPurposeDescription(String purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the beneficiaryTitle
     */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    /**
     * @param beneficiaryTitle the beneficiaryTitle to set
     */
    public void setBeneficiaryName(String beneficiaryTitle) {
        this.beneficiaryName = beneficiaryName;
    }

    /**
     * @return the beneficiaryTitle
     */
    public String getBeneficiaryTitle() {
        return beneficiaryTitle;
    }

    /**
     * @param beneficiaryTitle the beneficiaryTitle to set
     */
    public void setBeneficiaryTitle(String beneficiaryTitle) {
        this.beneficiaryTitle = beneficiaryTitle;
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
     * @return the beneficiaryBic
     */
    public String getBeneficiaryBic() {
        return beneficiaryBic;
    }

    /**
     * @param beneficiaryBic the beneficiaryBic to set
     */
    public void setBeneficiaryBic(String beneficiaryBic) {
        this.beneficiaryBic = beneficiaryBic;
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
     * @return the beneficiaryPhoneNumber
     */
    public String getBeneficiaryPhoneNumber() {
        return beneficiaryPhoneNumber;
    }

    /**
     * @param beneficiaryPhoneNumber the beneficiaryPhoneNumber to set
     */
    public void setBeneficiaryPhoneNumber(String beneficiaryPhoneNumber) {
        this.beneficiaryPhoneNumber = beneficiaryPhoneNumber;
    }

    /**
     * @return the chargeSizeModel
     */
    public ChargeSizeModel getChargeSizeModel() {
        return chargeSizeModel;
    }

    /**
     * @param chargeSizeModel the chargeSizeModel to set
     */
    public void setChargeSizeModel(ChargeSizeModel chargeSizeModel) {
        this.chargeSizeModel = chargeSizeModel;
    }

    /**
     * @return the beneficiaryCardCode
     */
    public String getBeneficiaryCardCode() {
        return beneficiaryCardCode;
    }

    /**
     * @param beneficiaryCardCode the beneficiaryCardCode to set
     */
    public void setBeneficiaryCardCode(String beneficiaryCardCode) {
        this.beneficiaryCardCode = beneficiaryCardCode;
    }

    /**
     * @return the beneficiaryCardName
     */
    public String getBeneficiaryCardName() {
        return beneficiaryCardName;
    }

    /**
     * @param beneficiaryCardName the beneficiaryCardName to set
     */
    public void setBeneficiaryCardName(String beneficiaryCardName) {
        this.beneficiaryCardName = beneficiaryCardName;
    }

    /**
     * @return the beneficiaryCardTitle
     */
    public String getBeneficiaryCardTitle() {
        return beneficiaryCardTitle;
    }

    /**
     * @param beneficiaryCardTitle the beneficiaryCardTitle to set
     */
    public void setBeneficiaryCardTitle(String beneficiaryCardTitle) {
        this.beneficiaryCardTitle = beneficiaryCardTitle;
    }

    /**
     * @return the beneficiaryCardNumber
     */
    public String getBeneficiaryCardNumber() {
        return beneficiaryCardNumber;
    }

    /**
     * @param beneficiaryCardNumber the beneficiaryCardNumber to set
     */
    public void setBeneficiaryCardNumber(String beneficiaryCardNumber) {
        this.beneficiaryCardNumber = beneficiaryCardNumber;
    }

    /**
     * @return the billType
     */
    public String getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * @return the postalAccount
     */
    public String getPostalAccount() {
        return postalAccount;
    }

    /**
     * @param postalAccount the postalAccount to set
     */
    public void setPostalAccount(String postalAccount) {
        this.postalAccount = postalAccount;
    }

    /**
     * @return the holderName
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * @param holderName the holderName to set
     */
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the billNumber
     */
    public String getBillNumber() {
        return billNumber;
    }

    /**
     * @param billNumber the billNumber to set
     */
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

}
