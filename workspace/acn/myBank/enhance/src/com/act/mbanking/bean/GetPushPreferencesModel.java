
package com.act.mbanking.bean;

import java.util.List;

public class GetPushPreferencesModel {
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
