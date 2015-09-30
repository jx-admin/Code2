
package com.accenture.mbank.model;

public class SimTopUpResponseModel {

    private int otpErrorCode;

    private String otpErrorDescription;

    private String otpKeySession;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public int getOtpErrorCode() {
        return otpErrorCode;
    }

    public void setOtpErrorCode(int otpErrorCode) {
        this.otpErrorCode = otpErrorCode;
    }

    public String getOtpErrorDescription() {
        return otpErrorDescription;
    }

    public void setOtpErrorDescription(String otpErrorDescription) {
        this.otpErrorDescription = otpErrorDescription;
    }

    public String getOtpKeySession() {
        return otpKeySession;
    }

    public void setOtpKeySession(String otpKeySession) {
        this.otpKeySession = otpKeySession;
    }

}
