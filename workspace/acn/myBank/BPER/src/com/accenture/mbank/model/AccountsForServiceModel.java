
package com.accenture.mbank.model;

import java.io.Serializable;
import java.util.List;

import com.accenture.mbank.util.LogManager;

public class AccountsForServiceModel implements Serializable{
    private String serviceCode;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
        result = prime * result + ((serviceCode == null) ? 0 : serviceCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountsForServiceModel other = (AccountsForServiceModel)obj;
        if (accounts == null) {
            if (other.accounts != null)
                return false;
        } else {
            if (other.accounts == null) {
                return false;
            } else if (accounts.size() != other.accounts.size()) {
                return false;
            } else {
                for (int i = 0; i < accounts.size(); i++) {
                    AccountsModel account1 = accounts.get(i);
                    AccountsModel account2 = other.accounts.get(i);
                    if (account1.equals(account2)) {

                    } else {
                        return false;
                    }

                }
            }

        }
        if (serviceCode == null) {
            if (other.serviceCode != null)
                return false;
        } else if (!serviceCode.equals(other.serviceCode))
            return false;

        LogManager.d("account equals" + "true");
        return true;
    }

    private List<AccountsModel> accounts;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<AccountsModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountsModel> accounts) {
        this.accounts = accounts;
    }

}
