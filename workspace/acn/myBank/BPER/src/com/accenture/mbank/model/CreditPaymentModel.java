package com.accenture.mbank.model;

import java.io.Serializable;

public class CreditPaymentModel implements Serializable{
	/*
	 * creditPayment M object
	 * 
	 * -insertionDate M calendar
	 * 
	 * -executionDate M calendar
	 * 
	 * -totalAmount M Big Decimal
	 * 
	 * -paymentID M String
	 * 
	 * -creditID M String
	 * 
	 * -portfolio M Int
	 * 
	 * -state M String
	 * 
	 * -debtorName M String
	 */

	private String insertionDate;
	private String executionDate;
	private double totalAmount;
	private String paymentID;
	private String creditID;
	private int portfolio;
	private String State;
	private String debtorName;
	private String listTypeItem;

	public String getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(String insertionDate) {
		this.insertionDate = insertionDate;
	}

	public String getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}

	public String getCreditID() {
		return creditID;
	}

	public void setCreditID(String creditID) {
		this.creditID = creditID;
	}

	public int getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(int portfolio) {
		this.portfolio = portfolio;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}

	public String getListTypeItem() {
		return listTypeItem;
	}

	public void setListTypeItem(String listTypeItem) {
		this.listTypeItem = listTypeItem;
	}

	@Override
    public CreditPaymentModel clone() {
		CreditPaymentModel model = new CreditPaymentModel();
		model.creditID = this.creditID;
		model.debtorName = this.debtorName;
		model.executionDate = this.executionDate;
		model.insertionDate = this.insertionDate;
		model.listTypeItem = this.listTypeItem;
		model.paymentID = this.paymentID;
		model.portfolio = this.portfolio;
		model.State = this.State;
		model.totalAmount = this.totalAmount;

        return model;
    }
	
}
