package com.act.mbanking.bean;

import java.util.List;

public class DepositInfo {
	private String type;
	private double value;
	private double percentage;
	
	private List<InvestmentDetail> investmentDetails;
	/**
	 * @return the investmentDetails
	 */
	public List<InvestmentDetail> getInvestmentDetails() {
		return investmentDetails;
	}
	/**
	 * @param investmentDetails the investmentDetails to set
	 */
	public void setInvestmentDetails(List<InvestmentDetail> investmentDetails) {
		this.investmentDetails = investmentDetails;
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
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
