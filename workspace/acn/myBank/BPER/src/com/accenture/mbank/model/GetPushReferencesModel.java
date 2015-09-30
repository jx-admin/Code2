
package com.accenture.mbank.model;

import java.util.List;

public class GetPushReferencesModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<PushCategoryModel> pustCategorList;

    /**
     * @return the pustCategorList
     */
    public List<PushCategoryModel> getPustCategorList() {
        return pustCategorList;
    }

    /**
     * @param pustCategorList the pustCategorList to set
     */
    public void setPustCategorList(List<PushCategoryModel> pustCategorList) {
        this.pustCategorList = pustCategorList;
    }

}