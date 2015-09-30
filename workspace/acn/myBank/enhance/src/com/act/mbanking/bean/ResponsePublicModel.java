
package com.act.mbanking.bean;

import java.io.Serializable;
import java.util.List;

public class ResponsePublicModel implements Serializable {
    private String transactionId;

    private int resultCode;

    public static final int SUCCESS_CODE = 0;

    public static final int FAIL_CODE = 1;

    private String resultDescription;

    public EventManagement eventManagement = new EventManagement();

    private List<Object> fieldMap;

    private String restartingKey;

    private String transferId;

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

    /**
     * @return the restartingKey
     */
    public String getRestartingKey() {
        return restartingKey;
    }

    /**
     * @param restartingKey the restartingKey to set
     */
    public void setRestartingKey(String restartingKey) {
        this.restartingKey = restartingKey;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public List<Object> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(List<Object> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public boolean isSuccess() {
        if (resultCode == SUCCESS_CODE) {
            return true;
        }
        return false;
    }
}
