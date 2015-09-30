
package com.act.mbanking.bean;

import java.util.List;

public class GetAccountsByServicesResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<AccountsForServiceModel> accountsForServiceList;

    public List<AccountsForServiceModel> getAccountsForServiceList() {
        return accountsForServiceList;
    }

    public void setAccountsForServiceList(List<AccountsForServiceModel> accountsForServiceList) {
        this.accountsForServiceList = accountsForServiceList;
    }

}
