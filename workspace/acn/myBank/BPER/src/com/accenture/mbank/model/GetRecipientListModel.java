
package com.accenture.mbank.model;

import java.util.List;

public class GetRecipientListModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<BankRecipient> bankRecipientList;

    private List<CardRecipient> cardRecipientList;

    private List<PhoneRecipient> phoneRecipientList;

    /**
     * @return the responsePublicModel
     */
    public ResponsePublicModel getResponsePublicModel() {
        return responsePublicModel;
    }

    /**
     * @param responsePublicModel the responsePublicModel to set
     */
    public void setResponsePublicModel(ResponsePublicModel responsePublicModel) {
        this.responsePublicModel = responsePublicModel;
    }

    /**
     * @return the bankRecipientList
     */
    public List<BankRecipient> getBankRecipientList() {
        return bankRecipientList;
    }

    /**
     * @param bankRecipientList the bankRecipientList to set
     */
    public void setBankRecipientList(List<BankRecipient> bankRecipientList) {
        this.bankRecipientList = bankRecipientList;
    }

    /**
     * @return the cardRecipientList
     */
    public List<CardRecipient> getCardRecipientList() {
        return cardRecipientList;
    }

    /**
     * @param cardRecipientList the cardRecipientList to set
     */
    public void setCardRecipientList(List<CardRecipient> cardRecipientList) {
        this.cardRecipientList = cardRecipientList;
    }

    /**
     * @return the phoneRecipientList
     */
    public List<PhoneRecipient> getPhoneRecipientList() {
        return phoneRecipientList;
    }

    /**
     * @param phoneRecipientList the phoneRecipientList to set
     */
    public void setPhoneRecipientList(List<PhoneRecipient> phoneRecipientList) {
        this.phoneRecipientList = phoneRecipientList;
    }

}
