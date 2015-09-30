
package com.accenture.mbank.model;

import java.util.List;

public class LoginResponseModel extends Model {

    private String sessionId;

    private String customerCode;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
