
package com.act.mbanking.bean;

import java.util.List;

import com.act.mbanking.logic.PaymentTemplate;

public class GetPaymentTempLatesResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<PaymentTemplate> paymentTempLatesList;

    /**
     * @return the paymentTempLatesList
     */
    public List<PaymentTemplate> getPaymentTempLatesList() {
        return paymentTempLatesList;
    }

    /**
     * @param paymentTempLatesList the paymentTempLatesList to set
     */
    public void setPaymentTempLatesList(List<PaymentTemplate> paymentTempLatesList) {
        this.paymentTempLatesList = paymentTempLatesList;
    }

}
