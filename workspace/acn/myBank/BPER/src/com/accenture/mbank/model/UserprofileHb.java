package com.accenture.mbank.model;

import java.util.List;

public class UserprofileHb {
	private String crae;
	private String abiCode;
	private String alias;
	private String bankCust;
	private String birthDate;
	private String branchAddress;
	private String branchCity;
	private String branchCode;
	private String branchName;
	private String branchPostalCode;
	private String cabCode;
	private String cciaNumber;
	private String companyName;
	private String creationDate;
	private String currencyChargeCode;
	private String customerCode;
	private String customerName;
	private String customerSurname;
	private String managment;
	private ProductModel product;
	private String referenceEmail;
	private String referenceFax;
	private String referenceTelephoneNumber1;
	private String referenceTelephoneNumber2;
	private String taxCode;
	private String note;
	private String contactPhone;
	private String contactMail;
	private NdgHB ndgHB;
	private OptionalServices optionalServices;
	private String typeSB;
	private String riba;

	public String getTypeSB() {
		return typeSB;
	}

	public void setTypeSB(String typeSB) {
		this.typeSB = typeSB;
	}

	public String getRiba() {
		return riba;
	}

	public void setRiba(String riba) {
		this.riba = riba;
	}

	/**
	 * all list
	 */
	private List<AccountsModel> accountList;

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCrae() {
		return crae;
	}

	public void setCrae(String crae) {
		this.crae = crae;
	}

	public String getAbiCode() {
		return abiCode;
	}

	public void setAbiCode(String abiCode) {
		this.abiCode = abiCode;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBankCust() {
		return bankCust;
	}

	public void setBankCust(String bankCust) {
		this.bankCust = bankCust;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCabCode() {
		return cabCode;
	}

	public void setCabCode(String cabCode) {
		this.cabCode = cabCode;
	}

	public String getCciaNumber() {
		return cciaNumber;
	}

	public void setCciaNumber(String cciaNumber) {
		this.cciaNumber = cciaNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getManagment() {
		return managment;
	}

	public void setManagment(String managment) {
		this.managment = managment;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
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

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMail() {
		return contactMail;
	}

	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	public NdgHB getNdgHB() {
		return ndgHB;
	}

	public void setNdgHB(NdgHB ndgHB) {
		this.ndgHB = ndgHB;
	}

	public OptionalServices getOptionalServices() {
		return optionalServices;
	}

	public void setOptionalServices(OptionalServices optionalServices) {
		this.optionalServices = optionalServices;
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
