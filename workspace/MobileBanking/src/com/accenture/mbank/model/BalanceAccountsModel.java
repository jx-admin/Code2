
package com.accenture.mbank.model;

public class BalanceAccountsModel {
    private String holder;

    private double availableBalance;

    private String personalizedName;

    private String currency;

    private String accountCode;

    private String cardState;

    private String expirationDate;

    private double plafond;

    private String bankServiceCode;

    private String accountBalance;

    /**
     * @return the holder
     */
    public String getHolder() {
        return holder;
    }

    /**
     * @param holder the holder to set
     */
    public void setHolder(String holder) {
        this.holder = holder;
    }

    /**
     * @return the availableBalance
     */
    public double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * @param availableBalance the availableBalance to set
     */
    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * @return the cardState
     */
    public String getCardState() {
        return cardState;
    }

    /**
     * @param cardState the cardState to set
     */
    public void setCardState(String cardState) {
        this.cardState = cardState;
    }

    /**
     * @return the expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the plafond
     */
    public double getPlafond() {
        return plafond;
    }

    /**
     * @param plafond the plafond to set
     */
    public void setPlafond(double plafond) {
        this.plafond = plafond;
    }

    /**
     * @return the bankServiceCode
     */
    public String getBankServiceCode() {
        return bankServiceCode;
    }

    /**
     * @param bankServiceCode the bankServiceCode to set
     */
    public void setBankServiceCode(String bankServiceCode) {
        this.bankServiceCode = bankServiceCode;
    }

    /**
     * @return the accountBalance
     */
    public String getAccountBalance() {
        return accountBalance;
    }

    /**
     * @param accountBalance the accountBalance to set
     */
    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * @return the personalizedName
     */
    public String getPersonalizedName() {
        return personalizedName;
    }

    /**
     * @param personalizedName the personalizedName to set
     */
    public void setPersonalizedName(String personalizedName) {
        this.personalizedName = personalizedName;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the accountCode
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * @param accountCode the accountCode to set
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

}
