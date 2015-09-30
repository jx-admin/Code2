
package com.accenture.mbank.model;

public class InvestmentDetail {

    private String currency;

    private double exchangeRate;

    private double price;

    private String priceDate;

    private String priceType;

    private String accrual;

    private int grossValue;

    private String expiringDate;

    private int deposit;

    private String reliability;

    private String title;

    private String description;

    private String ptFlag;

    private String amount;

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
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
     * @return the exchangeRate
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * @param exchangeRate the exchangeRate to set
     */
    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the priceDate
     */
    public String getPriceDate() {
        return priceDate;
    }

    /**
     * @param priceDate the priceDate to set
     */
    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    /**
     * @return the priceType
     */
    public String getPriceType() {
        return priceType;
    }

    /**
     * @param priceType the priceType to set
     */
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    /**
     * @return the accrual
     */
    public String getAccrual() {
        return accrual;
    }

    /**
     * @param accrual the accrual to set
     */
    public void setAccrual(String accrual) {
        this.accrual = accrual;
    }

    /**
     * @return the grossValue
     */
    public int getGrossValue() {
        return grossValue;
    }

    /**
     * @param grossValue the grossValue to set
     */
    public void setGrossValue(int grossValue) {
        this.grossValue = grossValue;
    }

    /**
     * @return the expiringDate
     */
    public String getExpiringDate() {
        return expiringDate;
    }

    /**
     * @param expiringDate the expiringDate to set
     */
    public void setExpiringDate(String expiringDate) {
        this.expiringDate = expiringDate;
    }

    /**
     * @return the deposit
     */
    public int getDeposit() {
        return deposit;
    }

    /**
     * @param deposit the deposit to set
     */
    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    /**
     * @return the reliability
     */
    public String getReliability() {
        return reliability;
    }

    /**
     * @param reliability the reliability to set
     */
    public void setReliability(String reliability) {
        this.reliability = reliability;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the ptFlag
     */
    public String getPtFlag() {
        return ptFlag;
    }

    /**
     * @param ptFlag the ptFlag to set
     */
    public void setPtFlag(String ptFlag) {
        this.ptFlag = ptFlag;
    }

}
