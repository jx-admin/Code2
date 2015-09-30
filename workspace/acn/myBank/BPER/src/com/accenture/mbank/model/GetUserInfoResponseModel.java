package com.accenture.mbank.model;

public class GetUserInfoResponseModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

	public UserprofileHb userprofileHb = new UserprofileHb();

	private String token;

	private String marketLink;

	private boolean mandatoryFlag;

	public ResponsePublicModel getResponsePublicModel() {
		return responsePublicModel;
	}

	public void setResponsePublicModel(ResponsePublicModel responsePublicModel) {
		this.responsePublicModel = responsePublicModel;
	}

	public UserprofileHb getUserprofileHb() {
		return userprofileHb;
	}

	public void setUserprofileHb(UserprofileHb userprofileHb) {
		this.userprofileHb = userprofileHb;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMarketLink() {
		return marketLink;
	}

	public void setMarketLink(String marketLink) {
		this.marketLink = marketLink;
	}

	public boolean isMandatoryFlag() {
		return mandatoryFlag;
	}

	public void setMandatoryFlag(boolean mandatoryFlag) {
		this.mandatoryFlag = mandatoryFlag;
	}

}
