package com.accenture.mbank.model;

import java.io.Serializable;
import java.util.List;

public class BalanceAccountsModel implements Serializable{
	private String holder;

	private double availableBalance;

	private String personalizedName;

	private String currency;

	private String accountCode;

	private String cardState;

	private String expirationDate;

	private double plafond;

	private String bankServiceCode;

	private double accountBalance;

	private String closed;

	private String cac;

	private String accountName;

	private int accountNumber;

	private String lastUpdate;

	private String accountType;

	private int overdraftGranted;

	private int overdraftUsed;

	private int overdraftDiscounted;

	private int totalPresentations;

	private List<AdditionalCard> additionalCardsList;

	private boolean preferred;

	private boolean dipiuActive;
	
	private boolean dipiuAvailable;

	private double dipiuBalance;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getOverdraftGranted() {
		return overdraftGranted;
	}

	public void setOverdraftGranted(int overdraftGranted) {
		this.overdraftGranted = overdraftGranted;
	}

	public int getOverdraftUsed() {
		return overdraftUsed;
	}

	public void setOverdraftUsed(int overdraftUsed) {
		this.overdraftUsed = overdraftUsed;
	}

	public int getOverdraftDiscounted() {
		return overdraftDiscounted;
	}

	public void setOverdraftDiscounted(int overdraftDiscounted) {
		this.overdraftDiscounted = overdraftDiscounted;
	}

	public int getTotalPresentations() {
		return totalPresentations;
	}

	public void setTotalPresentations(int totalPresentations) {
		this.totalPresentations = totalPresentations;
	}

	public List<AdditionalCard> getAdditionalCardsList() {
		return additionalCardsList;
	}

	public void setAdditionalCardsList(List<AdditionalCard> additionalCardsList) {
		this.additionalCardsList = additionalCardsList;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}
	/**
	 * 如果要这个是true 显示3个
	 * @return
	 */
	public boolean isDipiuActive() {
		return dipiuActive;
	}

	public void setDipiuActive(boolean dipiuActive) {
		this.dipiuActive = dipiuActive;
	}

	public boolean isDipiuAvailable() {
		return dipiuAvailable;
	}

	public void setDipiuAvailable(boolean dipiuAvailable) {
		this.dipiuAvailable = dipiuAvailable;
	}

	public double getDipiuBalance() {
		return dipiuBalance;
	}

	public void setDipiuBalance(double dipiuBalance) {
		this.dipiuBalance = dipiuBalance;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the holder
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * @param holder
	 *            the holder to set
	 */
	public void setHolder(String holder) {
		this.holder = holder;
	}

	/**
	 * @return the availableBalance
	 */
	public double getAvailableBalance() {
		return availableBalance;
	}

	/**
	 * @param availableBalance
	 *            the availableBalance to set
	 */
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	/**
	 * @return the cardState
	 */
	public String getCardState() {
		return cardState;
	}

	/**
	 * @param cardState
	 *            the cardState to set
	 */
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the plafond
	 */
	public double getPlafond() {
		return plafond;
	}

	/**
	 * @param plafond
	 *            the plafond to set
	 */
	public void setPlafond(double plafond) {
		this.plafond = plafond;
	}

	/**
	 * @return the bankServiceCode
	 */
	public String getBankServiceCode() {
		return bankServiceCode;
	}

	/**
	 * @param bankServiceCode
	 *            the bankServiceCode to set
	 */
	public void setBankServiceCode(String bankServiceCode) {
		this.bankServiceCode = bankServiceCode;
	}

	/**
	 * @return the accountBalance
	 */
	public double getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance
	 *            the accountBalance to set
	 */
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * @return the personalizedName
	 */
	public String getPersonalizedName() {
		return personalizedName;
	}

	/**
	 * @param personalizedName
	 *            the personalizedName to set
	 */
	public void setPersonalizedName(String personalizedName) {
		this.personalizedName = personalizedName;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}

	/**
	 * @param accountCode
	 *            the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getCac() {
		return cac;
	}

	public void setCac(String cac) {
		this.cac = cac;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

}
