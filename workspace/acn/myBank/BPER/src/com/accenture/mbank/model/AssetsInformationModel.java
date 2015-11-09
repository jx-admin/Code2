
package com.accenture.mbank.model;

public class AssetsInformationModel {
    private String accountAlias;

    private boolean isPreferred;
    
    public boolean getIsPreferred() {
		return isPreferred;
	}

	public void setIsPreferred(boolean isPreferred) {
		this.isPreferred = isPreferred;
	}

	private GetAssetsInformationResponseModel getAssetsInfomation;

    /**
     * @return the accountAlias
     */
    public String getAccountAlias() {
        return accountAlias;
    }

    /**
     * @param accountAlias the accountAlias to set
     */
    public void setAccountAlias(String accountAlias) {
        this.accountAlias = accountAlias;
    }

    /**
     * @return the getAssetsInfomation
     */
    public GetAssetsInformationResponseModel getGetAssetsInfomation() {
        return getAssetsInfomation;
    }

    /**
     * @param getAssetsInfomation the getAssetsInfomation to set
     */
    public void setGetAssetsInfomation(GetAssetsInformationResponseModel getAssetsInfomation) {
        this.getAssetsInfomation = getAssetsInfomation;
    }
}