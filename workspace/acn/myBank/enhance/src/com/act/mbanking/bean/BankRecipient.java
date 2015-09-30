
package com.act.mbanking.bean;


public class BankRecipient extends Account{

    /**
     * 
     */
    private static final long serialVersionUID = -3609939320935541828L;

    private String id;

    private String ibanCode;

    private String name;

    private String bic;

    /**
     * @return bic
     */
    public String getBic() {
        return bic;
    }

    /**
     * @param bic 要设置的 bic
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the ibanCode
     */
    public String getIbanCode() {
        return ibanCode;
    }

    /**
     * @param ibanCode the ibanCode to set
     */
    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
