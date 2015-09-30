
package com.accenture.mbank.model;

import java.util.List;

public class CompanyAmountResponseModel {
    private List<AmountAvailable> amountAvailable;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<AmountAvailable> getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(List<AmountAvailable> amountAvailable) {
        this.amountAvailable = amountAvailable;
    }
}
