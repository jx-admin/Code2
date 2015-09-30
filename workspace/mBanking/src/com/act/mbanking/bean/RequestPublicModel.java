
package com.act.mbanking.bean;

public class RequestPublicModel {
    public String customerCode = "33330001";// 调试用的 不用的时候放""

    public String sessionId = "";// 调试用的 不用的时候放""

    public static final String bankName = "5";

    public static final String enterpriseId = "5";

    public static final String channel = "0010";

    public String userAgent = "android";

    public String token;
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    boolean debugSeesionErrorException = false;

    public String getSessionId() {

        if (debugSeesionErrorException) {
            return "";
        }
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public String getChannel() {
        return channel;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
