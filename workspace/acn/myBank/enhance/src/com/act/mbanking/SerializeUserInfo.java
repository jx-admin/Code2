
package com.act.mbanking;

import java.io.Serializable;

import com.act.mbanking.bean.GetUserInfoResponseModel;

public class SerializeUserInfo implements Serializable {

    private static SerializeUserInfo uniqueInstance = null;

    private SerializeUserInfo() {
    }

    public static SerializeUserInfo getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SerializeUserInfo();
        }
        return uniqueInstance;
    }

    public GetUserInfoResponseModel localUserInfo;

    /**
     * @return the localUserInfo
     */
    public GetUserInfoResponseModel getLocalUserInfo() {
        return localUserInfo;
    }

    /**
     * @param localUserInfo the localUserInfo to set
     */
    public void setLocalUserInfo(GetUserInfoResponseModel localUserInfo) {
        this.localUserInfo = localUserInfo;
    }

}
