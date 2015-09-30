package com.act.mbanking.bean;

import java.util.List;


public class GetBalanceResponseModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();
	
	private List<BalanceAccountsModel> banlaceAccounts;

	/**
	 * @return the banlaceAccounts
	 */
	public List<BalanceAccountsModel> getBanlaceAccounts() {
		return banlaceAccounts;
	}

	/**
	 * @param banlaceAccounts the banlaceAccounts to set
	 */
	public void setBanlaceAccounts(List<BalanceAccountsModel> banlaceAccounts) {
		this.banlaceAccounts = banlaceAccounts;
	}
	
	
}
