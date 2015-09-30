
package com.accenture.mbank.model;

import java.util.List;

public class PushCategoryModel {
    /**
     * Settings category
     */
    private int expand;
    
    /**
     * Settings category
     */
    private String category;

    /**
     * The list of setting to be set
     */
    private List<PushSettingModel> pushSettingList;

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * @return the expanded
     */
    public int getExpand() {
        return expand;
    }

    /**
     * @return the pushSettingList
     */
    public List<PushSettingModel> getPushSettingList() {
        return pushSettingList;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * @param category the category to set
     */
    public void setExpand(int expand) {
        this.expand = expand;
    }

    /**
     * @param pushSettingList the pushSettingList to set
     */
    public void setPushSettingList(List<PushSettingModel> pushSettingList) {
        this.pushSettingList = pushSettingList;
    }
}
