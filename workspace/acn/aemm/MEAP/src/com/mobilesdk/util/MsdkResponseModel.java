
package com.mobilesdk.util;

public class MsdkResponseModel {
    String commandID;

    int code;

    Object value;

    String response;

    /**
     * @return the commandID
     */
    public String getCommandID() {
        return commandID;
    }

    /**
     * @param commandID the commandID to set
     */
    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

}
