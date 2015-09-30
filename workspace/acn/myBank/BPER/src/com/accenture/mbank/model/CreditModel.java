package com.accenture.mbank.model;

import java.io.Serializable;

public class CreditModel implements Serializable{
	/*
	 * 
	 * -insertionDate M calendar
	 * 
	 * -totalAmount M Big Decimal
	 * 
	 * -creditType M String
	 * 
	 * -creditID M String
	 * 
	 * -paymentsNum M Int
	 * 
	 * -state M String
	 */
	private String insertionDate;
	private double totalAmount;
	private String creditType;
	private String creditID;
	private int paymentsNum;
	private String state;

	public String getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(String insertionDate) {
		this.insertionDate = insertionDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public String getCreditID() {
		return creditID;
	}

	public void setCreditID(String creditID) {
		this.creditID = creditID;
	}

	public int getPaymentsNum() {
		return paymentsNum;
	}

	public void setPaymentsNum(int paymentsNum) {
		this.paymentsNum = paymentsNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
