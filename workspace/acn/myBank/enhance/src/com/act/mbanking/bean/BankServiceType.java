
package com.act.mbanking.bean;

import java.io.Serializable;

public class BankServiceType implements Serializable{
    private String bankServiceCode;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankServiceType other = (BankServiceType)obj;
        if (bankServiceCode == null) {
            if (other.bankServiceCode != null)
                return false;
        } else if (!bankServiceCode.equals(other.bankServiceCode))
            return false;
        if (serviceCategory == null) {
            if (other.serviceCategory != null)
                return false;
        } else if (!serviceCategory.equals(other.serviceCategory))
            return false;
        if (subCategory == null) {
            if (other.subCategory != null)
                return false;
        } else if (!subCategory.equals(other.subCategory))
            return false;
        return true;
    }

    private String serviceCategory;

    private String subCategory;

    public String getBankServiceCode() {
        return bankServiceCode;
    }

    public void setBankServiceCode(String bankServiceCode) {
        this.bankServiceCode = bankServiceCode;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
