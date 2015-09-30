
package com.accenture.mbank.model;

public class CustomParamsModel {
    /**
     * The name of custom parameter
     */
    private String pushParamName;
    
    /**
     * The value to set for custom paramenter
     */
    private int pushParamValue;

    /**
     * @return the pushParamName
     */
    public String getPushParamName() {
        return pushParamName;
    }

    /**
     * @return the pushParamValue
     */
    public int getPushParamValue() {
        return pushParamValue;
    }

    /**
     * @param pushParamName the pushParamName to set
     */
    public void setPushParamName(String pushParamName) {
        this.pushParamName = pushParamName;
    }

    /**
     * @param pushParamValue the pushParamValue to set
     */
    public void setPushParamValue(int pushParamValue) {
        this.pushParamValue = pushParamValue;
    }

}
