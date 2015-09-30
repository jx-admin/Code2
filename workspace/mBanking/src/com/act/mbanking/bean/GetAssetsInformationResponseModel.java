package com.act.mbanking.bean;

import java.util.List;


public class GetAssetsInformationResponseModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

	private double portfolioValue;
	private double percentage;
	private List<AssetDetailModel> assetDetails;
	
	/**
	 * @return the assetDetails
	 */
	public List<AssetDetailModel> getAssetDetails() {
		return assetDetails;
	}

	/**
	 * @param assetDetails
	 *            the assetDetails to set
	 */
	public void setAssetDetails(List<AssetDetailModel> assetDetails) {
		this.assetDetails = assetDetails;
	}

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
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
