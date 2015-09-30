package com.accenture.mbank.model;

import java.util.List;

public class GetCreditPaymentList {

	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

	private boolean moreValues;

	private String restartingKey;

	private List<CreditPaymentModel> creditPaymentList;

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

	public List<CreditPaymentModel> getCreditPaymentList() {
		return creditPaymentList;
	}

	public void setCreditPaymentList(List<CreditPaymentModel> creditPaymentList) {
		this.creditPaymentList = creditPaymentList;
	}

}
