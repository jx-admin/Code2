package com.accenture.mbank.model;

import java.util.List;

public class AdvNewsResponseModel {
	private List<ListAdvNewsModel> listAdvNews;

	private String marketLink;

	private boolean mandatoryFlag;

	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

	public List<ListAdvNewsModel> getListAdvNews() {
		return listAdvNews;
	}

	public void setListAdvNews(List<ListAdvNewsModel> listAdvNews) {
		this.listAdvNews = listAdvNews;
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
