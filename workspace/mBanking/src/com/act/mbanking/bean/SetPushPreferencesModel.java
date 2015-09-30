
package com.act.mbanking.bean;

import java.util.List;

public class SetPushPreferencesModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    /**
     * The list of setting to be set
     */
    private List<PushSettingModel> pushSettingList;

    /**
     * @return the pushSettingList
     */
    public List<PushSettingModel> getPushSettingList() {
        return pushSettingList;
    }

    /**
     * @param pushSettingList the pushSettingList to set
     */
    public void setPushSettingList(List<PushSettingModel> pushSettingList) {
        this.pushSettingList = pushSettingList;
    }

}
