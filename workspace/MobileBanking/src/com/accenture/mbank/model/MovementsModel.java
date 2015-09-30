
package com.accenture.mbank.model;

public class MovementsModel {
    private String description = "";

    private double amount;

    private String operationDate = "";

    private String valueDate = "";

    public boolean isExpanded = false;

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
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the operationDate
     */
    public String getOperationDate() {
        return operationDate;
    }

    /**
     * @param operationDate the operationDate to set
     */
    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    /**
     * @return the valueDate
     */
    public String getValueDate() {
        return valueDate;
    }

    /**
     * @param valueDate the valueDate to set
     */
    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    @Override
    public MovementsModel clone() {
        MovementsModel model = new MovementsModel();
        model.description = this.description;
        model.amount = this.amount;
        model.operationDate = this.operationDate;
        model.valueDate = this.valueDate;
        model.isExpanded = this.isExpanded;

        return model;
    }
}
