package com.act.mbanking.bean;

import java.util.Calendar;

public class Balance {
    private String holder;
    private double accountBalance;
    private double availableBalance;
    private String lastUpdate;
    private String personalizedName;
    private String currency;
    private int accountCode;
    private String transactionId;
    
    public String getInfo(){
        return "holder:"+holder+" accountBalance:"+accountBalance+" availableBalance:"+availableBalance+ " lastUpdate:"+
       lastUpdate+ " personalizedName:"+
        personalizedName+ " currency:"+
        currency+ " accountCode:"+
        accountCode+ " transactionId:"+
       transactionId;
    }
    
    public String getHolder() {
        return holder;
    }
    public void setHolder(String holder) {
        this.holder = holder;
    }
    public double getAccountBalance() {
        return accountBalance;
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    public double getAvailableBalance() {
        return availableBalance;
    }
    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getPersonalizedName() {
        return personalizedName;
    }
    public void setPersonalizedName(String personalizedName) {
        this.personalizedName = personalizedName;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public int getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(int accountCode) {
        this.accountCode = accountCode;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}
