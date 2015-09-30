
package com.act.mbanking.bean;

public class CheckBillPaymentResponseModel extends CheckTransactionResponseModel{
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private CheckBillPaymentValueModel checkBillPaymentValueModel;

    /**
     * @return the checkBillPaymentValueModel
     */
    public CheckBillPaymentValueModel getCheckBillPaymentValueModel() {
        return checkBillPaymentValueModel;
    }

    /**
     * @param checkBillPaymentValueModel the checkBillPaymentValueModel to set
     */
    public void setCheckBillPaymentValueModel(CheckBillPaymentValueModel checkBillPaymentValueModel) {
        this.checkBillPaymentValueModel = checkBillPaymentValueModel;
    }

}
