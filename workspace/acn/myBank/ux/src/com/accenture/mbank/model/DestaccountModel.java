
package com.accenture.mbank.model;


public class DestaccountModel extends Account {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String title;

    private String state;

    private String iban;

    private String bic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

}
