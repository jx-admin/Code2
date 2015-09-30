
package com.accenture.mbank.model;

import java.io.Serializable;

public class InfoCardsModel implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String expirationDate;

    private String cardHash;

    private String cardNumberMask;

    private String type;

    private String title;

    private String name;

    private String holderBirthDate;

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardHash() {
        return cardHash;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

    public String getCardNumberMask() {
        return cardNumberMask;
    }

    public void setCardNumberMask(String cardNumberMask) {
        this.cardNumberMask = cardNumberMask;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHolderBirthDate() {
        return holderBirthDate;
    }

    public void setHolderBirthDate(String holderBirthDate) {
        this.holderBirthDate = holderBirthDate;
    }
}
