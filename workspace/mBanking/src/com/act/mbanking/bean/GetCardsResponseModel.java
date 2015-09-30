
package com.act.mbanking.bean;

import java.util.List;

public class GetCardsResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<InfoCardsModel> infoCardListModel;

    public List<InfoCardsModel> getInfoCardListModel() {
        return infoCardListModel;
    }

    public void setInfoCardListModel(List<InfoCardsModel> infoCardListModel) {
        this.infoCardListModel = infoCardListModel;
    }

}
