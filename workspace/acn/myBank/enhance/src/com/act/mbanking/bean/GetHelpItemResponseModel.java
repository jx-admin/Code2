
package com.act.mbanking.bean;

import java.util.List;

public class GetHelpItemResponseModel {
    private List<HelpItemListModel> helpItemList;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<HelpItemListModel> getHelpItemList() {
        return helpItemList;
    }

    public void setHelpItemList(List<HelpItemListModel> helpItemList) {
        this.helpItemList = helpItemList;
    }
}
