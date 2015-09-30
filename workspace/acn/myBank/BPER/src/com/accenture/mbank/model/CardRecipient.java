
package com.accenture.mbank.model;

import java.io.Serializable;


public class CardRecipient implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String cardNumber;

    private String name;

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
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }
    
    /**
     * @return Last4Digits
     */
    public String getLast4Digits(){
        if(cardNumber==null){
            return null;
        }else if(cardNumber.length()<=4){
            return cardNumber;
        }
        return cardNumber.substring(cardNumber.length()-4);
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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
