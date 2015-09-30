package com.accenture.mbank.model;

import java.util.List;

public class GetCreditList {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

	private boolean moreValues;

	private String restartingKey;

	private List<CreditModel> creditList;
	
	public boolean isMoreValues() {
		return moreValues;
	}

	public void setMoreValues(boolean moreValues) {
		this.moreValues = moreValues;
	}

	public String getRestartingKey() {
		return restartingKey;
	}

	public void setRestartingKey(String restartingKey) {
		this.restartingKey = restartingKey;
	}

	public List<CreditModel> getCreditList() {
		return creditList;
	}

	public void setCreditList(List<CreditModel> creditList) {
		this.creditList = creditList;
	}

}
