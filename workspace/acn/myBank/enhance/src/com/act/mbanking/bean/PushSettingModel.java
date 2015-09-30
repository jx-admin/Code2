
package com.act.mbanking.bean;

import java.util.List;

public class PushSettingModel {
	public static final int DISABLED=0,ENABLED=1,NEVERSETTED=2;
    /**
     * Identifies the message type see the specification for the supported
     * notification
     */
    private int pushMessageType;

    /**
     * 0 – Disabled 1 – Enabled 2 – Never setted
     */
    private int pushSetting;

    /**
     * Description of the notification
     */
    private String pushDescription;

    /**
     * 0 – Option not available in Push preference 1 – Option available in Push
     * preference
     */
    private int available;

    /**
     * List of additional parameters used for the specific push message
     */
    private List<CustomParamsModel> customParamsList;

    /**
     * @return the pushMessageType
     */
    public int getPushMessageType() {
        return pushMessageType;
    }

    /**
     * @return the pushSetting
     */
    public int getPushSetting() {
        return pushSetting;
    }

    /**
     * @return the customParamsList
     */
    public List<CustomParamsModel> getCustomParamsList() {
        return customParamsList;
    }

    /**
     * @param pushMessageType the pushMessageType to set
     */
    public void setPushMessageType(int pushMessageType) {
        this.pushMessageType = pushMessageType;
    }

    /**
     * @param pushSetting the pushSetting to set
     */
    public void setPushSetting(int pushSetting) {
        this.pushSetting = pushSetting;
    }

    /**
     * @param customParamsList the customParamsList to set
     */
    public void setCustomParamsList(List<CustomParamsModel> customParamsList) {
        this.customParamsList = customParamsList;
    }

    /**
     * @return the pushDescription
     */
    public String getPushDescription() {
        return pushDescription;
    }

    /**
     * @param pushDescription the pushDescription to set
     */
    public void setPushDescription(String pushDescription) {
        this.pushDescription = pushDescription;
    }

    /**
     * @return the available
     */
    public int getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(int available) {
        this.available = available;
    }
}
