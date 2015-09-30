
package com.accenture.mbank.model;

import java.util.List;

public class GetAccountsByServicesResponseModel {
    private long effectiveDate;
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<AccountsForServiceModel> accountsForServiceList;

    public List<AccountsForServiceModel> getAccountsForServiceList() {
        return accountsForServiceList;
    }
    public long getEffectiveDate() {
    	if(effectiveDate==0){
    		return System.currentTimeMillis();
    	}
    	return effectiveDate;
    }
    
    public void setEffectiveDate(long effectiveDate) {
    	this.effectiveDate = effectiveDate;
    }

    public void setAccountsForServiceList(List<AccountsForServiceModel> accountsForServiceList) {
        this.accountsForServiceList = accountsForServiceList;
    }

}
