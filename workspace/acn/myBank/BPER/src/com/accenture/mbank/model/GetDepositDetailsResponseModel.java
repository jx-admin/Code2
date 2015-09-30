package com.accenture.mbank.model;

public class GetDepositDetailsResponseModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();
	private String holder;
	private double portfolioValue;
	private double portfolio;
	private DepositInfo shares;
	private DepositInfo bonds;
	private DepositInfo funds;
	private DepositInfo otherSecurities;

	/**
	 * @return the portfolioValue
	 */
	public double getPortfolioValue() {
		return portfolioValue;
	}

	/**
	 * @param portfolioValue
	 *            the portfolioValue to set
	 */
	public void setPortfolioValue(double portfolioValue) {
		this.portfolioValue = portfolioValue;
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
	 * @return the portfolio
	 */
	public double getPortfolio() {
		return portfolio;
	}

	/**
	 * @param portfolio
	 *            the portfolio to set
	 */
	public void setPortfolio(double portfolio) {
		this.portfolio = portfolio;
	}

	/**
	 * @return the shares
	 */
	public DepositInfo getShares() {
		return shares;
	}

	/**
	 * @param shares
	 *            the shares to set
	 */
	public void setShares(DepositInfo shares) {
		this.shares = shares;
	}

	/**
	 * @return the bond
	 */
	public DepositInfo getBonds() {
		return bonds;
	}

	/**
	 * @param bond
	 *            the bond to set
	 */
	public void setBonds(DepositInfo bonds) {
		this.bonds = bonds;
	}

	/**
	 * @return the fund
	 */
	public DepositInfo getFunds() {
		return funds;
	}

	/**
	 * @param fund
	 *            the fund to set
	 */
	public void setFunds(DepositInfo funds) {
		this.funds = funds;
	}

	/**
	 * @return the otherSecurities
	 */
	public DepositInfo getOtherSecurities() {
		return otherSecurities;
	}

	/**
	 * @param otherSecurities
	 *            the otherSecurities to set
	 */
	public void setOtherSecurities(DepositInfo otherSecurities) {
		this.otherSecurities = otherSecurities;
	}

}
