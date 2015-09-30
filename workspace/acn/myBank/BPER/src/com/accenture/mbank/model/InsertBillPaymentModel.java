
package com.accenture.mbank.model;

public class InsertBillPaymentModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private OtpStateModel otpStateModel;

    private String transferId;

    /**
     * @return the otpStateModel
     */
    public OtpStateModel getOtpStateModel() {
        return otpStateModel;
    }

    /**
     * @param otpStateModel the otpStateModel to set
     */
    public void setOtpStateModel(OtpStateModel otpStateModel) {
        this.otpStateModel = otpStateModel;
    }

    /**
     * @return the transferId
     */
    public String getTransferId() {
        return transferId;
    }

    /**
     * @param transferId the transferId to set
     */
    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

}
