
package com.act.mbanking.bean;

public class OtpStateModel {
    private int otpErrorCode;

    private String otpErrorDescription;

    private String otpKeySession;

    private int otpAvailable;

    /**
     * @return the otpErrorCode
     */
    public int getOtpErrorCode() {
        return otpErrorCode;
    }

    /**
     * @return the otpErrorDescription
     */
    public String getOtpErrorDescription() {
        return otpErrorDescription;
    }

    /**
     * @return the otpKeySession
     */
    public String getOtpKeySession() {
        return otpKeySession;
    }

    /**
     * @return the otpAvailable
     */
    public int getOtpAvailable() {
        return otpAvailable;
    }

    /**
     * @param otpErrorCode the otpErrorCode to set
     */
    public void setOtpErrorCode(int otpErrorCode) {
        this.otpErrorCode = otpErrorCode;
    }

    /**
     * @param otpErrorDescription the otpErrorDescription to set
     */
    public void setOtpErrorDescription(String otpErrorDescription) {
        this.otpErrorDescription = otpErrorDescription;
    }

    /**
     * @param otpKeySession the otpKeySession to set
     */
    public void setOtpKeySession(String otpKeySession) {
        this.otpKeySession = otpKeySession;
    }

    /**
     * @param otpAvailable the otpAvailable to set
     */
    public void setOtpAvailable(int otpAvailable) {
        this.otpAvailable = otpAvailable;
    }

}
