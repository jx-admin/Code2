
package com.act.mbanking.bean;

import java.util.List;

import org.json.JSONObject;

public class LoginRequestModel extends Model {
    private int serviceType;

    private String enterpriseId;

    private String stepId;

    private String msisdn;

    private List<Object> fieldMap;

    private String bankName;

    private String token;

    private String applicationId;

    private String channel;

    private String userAgent;

    private String operationId;

    private String username;

    private String password;

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public List<Object> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(List<Object> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequestModel() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void generate(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.generate(jsonObject);
    }

    @Override
    public JSONObject toJsonObject() {
        // TODO Auto-generated method stub
        return super.toJsonObject();
    }
}
