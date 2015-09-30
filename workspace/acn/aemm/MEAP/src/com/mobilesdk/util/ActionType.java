
package com.mobilesdk.util;

/**
 * @author yang.c.li
 */
public class ActionType {
    public static final int MODELNAME = 10101;

    public static final String modelName = "modelName";

    public static final int PLATFORM = 10102;

    public static final String platform = "platform";

    public static final int PLATFORMVERSION = 10103;

    public static final String platformVersion = "platformVersion";

    public static final int UUID = 10104;

    public static final int SIMCARRIERNAME = 10105;

    public static final String simCarrierName = "simCarrierName";

    public static String getActionKey(int actionType) {
        if (actionType == MODELNAME) {
            return modelName;
        } else if (actionType == PLATFORM) {
            return platform;
        } else if (actionType == PLATFORMVERSION) {
            return platformVersion;
        } else if (actionType == SIMCARRIERNAME) {
            return simCarrierName;
        }
        return null;
    }
}
