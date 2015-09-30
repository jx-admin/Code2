
package com.act.mbanking.bean;

import java.util.List;

public class GetAccountByPhoneNumberModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<AccountsModel> accountList;

    /**
     * @return the accountList
     */
    public List<AccountsModel> getAccountList() {
        return accountList;
    }

    /**
     * @param accountList the accountList to set
     */
    public void setAccountList(List<AccountsModel> accountList) {
        this.accountList = accountList;
    }

}
