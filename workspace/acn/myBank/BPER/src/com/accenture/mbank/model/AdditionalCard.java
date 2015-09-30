package com.accenture.mbank.model;

import java.io.Serializable;

public class AdditionalCard implements Serializable {
	private double cardBalance;
	private String cardHolder;
	private String cardName;
	private String bankServiceCode;
	private String cardAccountCode;
	private String cardPlafond;
	private String cardExpiredDate;
	private String cardState;
	private String cardNumber;

	public double getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(double cardBalance) {
		this.cardBalance = cardBalance;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getBankServiceCode() {
		return bankServiceCode;
	}

	public void setBankServiceCode(String bankServiceCode) {
		this.bankServiceCode = bankServiceCode;
	}

	public String getCardAccountCode() {
		return cardAccountCode;
	}

	public void setCardAccountCode(String cardAccountCode) {
		this.cardAccountCode = cardAccountCode;
	}

	public String getCardPlafond() {
		return cardPlafond;
	}

	public void setCardPlafond(String cardPlafond) {
		this.cardPlafond = cardPlafond;
	}

	public String getCardExpiredDate() {
		return cardExpiredDate;
	}

	public void setCardExpiredDate(String cardExpiredDate) {
		this.cardExpiredDate = cardExpiredDate;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}
