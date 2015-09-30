
package com.act.mbanking.bean;

public class GenerateOTPResponseModel {
    private String otpAvailable;

    private String otpErrorDescription;

    private String otpKeySession;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public String getOtpAvailable() {
        return otpAvailable;
    }

    public void setOtpAvailable(String otpAvailable) {
        this.otpAvailable = otpAvailable;
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
