
package com.accenture.mbank.model;

import java.util.List;

public class GetUserInfoResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private String token;

    private String bankCode;

    private String alias;

    private String birthDate;

    private String branchName;

    private String branchCode;

    private String branchAddress;

    private String branchPostalCode;

    private String branchCity;

    private String creationDate;

    private String currencyChargeCode;

    private String customerCode;

    private String customerName;

    private String customerSurname;

    private String management;

    private String ndgHBCode;

    private String ndgType;

    private String notes;

    private String referenceEmail;

    private String referenceFax;

    private String referenceTelephoneNumber1;

    private String referenceTelephoneNumber2;

    private String taxCode;

    /**
     * all list
     */
    private List<AccountsModel> accountList;

    private ProductModel product;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchPostalCode() {
        return branchPostalCode;
    }

    public void setBranchPostalCode(String branchPostalCode) {
        this.branchPostalCode = branchPostalCode;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCurrencyChargeCode() {
        return currencyChargeCode;
    }

    public void setCurrencyChargeCode(String currencyChargeCode) {
        this.currencyChargeCode = currencyChargeCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getNdgHBCode() {
        return ndgHBCode;
    }

    public void setNdgHBCode(String ndgHBCode) {
        this.ndgHBCode = ndgHBCode;
    }

    public String getNdgType() {
        return ndgType;
    }

    public void setNdgType(String ndgType) {
        this.ndgType = ndgType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReferenceEmail() {
        return referenceEmail;
    }

    public void setReferenceEmail(String referenceEmail) {
        this.referenceEmail = referenceEmail;
    }

    public String getReferenceFax() {
        return referenceFax;
    }

    public void setReferenceFax(String referenceFax) {
        this.referenceFax = referenceFax;
    }

    public String getReferenceTelephoneNumber1() {
        return referenceTelephoneNumber1;
    }

    public void setReferenceTelephoneNumber1(String referenceTelephoneNumber1) {
        this.referenceTelephoneNumber1 = referenceTelephoneNumber1;
    }

    public String getReferenceTelephoneNumber2() {
        return referenceTelephoneNumber2;
    }

    public void setReferenceTelephoneNumber2(String referenceTelephoneNumber2) {
        this.referenceTelephoneNumber2 = referenceTelephoneNumber2;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public List<AccountsModel> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountsModel> accountList) {
        this.accountList = accountList;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

}
