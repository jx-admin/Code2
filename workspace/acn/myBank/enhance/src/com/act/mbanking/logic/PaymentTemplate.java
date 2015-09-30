
package com.act.mbanking.logic;

public class PaymentTemplate {
    private String templateName;

    private String billType;

    private String postalAccount;

    private String holderName;

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @return the billType
     */
    public String getBillType() {
        return billType;
    }

    /**
     * @return the postalAccount
     */
    public String getPostalAccount() {
        return postalAccount;
    }

    /**
     * @return the holderName
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * @param postalAccount the postalAccount to set
     */
    public void setPostalAccount(String postalAccount) {
        this.postalAccount = postalAccount;
    }

    /**
     * @param holderName the holderName to set
     */
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
