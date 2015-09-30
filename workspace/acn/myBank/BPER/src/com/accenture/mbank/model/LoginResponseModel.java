
package com.accenture.mbank.model;

import java.util.List;

public class LoginResponseModel extends Model {

    private String sessionId;

    private String customerNumber;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setcustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
