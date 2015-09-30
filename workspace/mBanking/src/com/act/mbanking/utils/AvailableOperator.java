
package com.act.mbanking.utils;

public class AvailableOperator {

    public static final String TIM_VALUE = "10013";

    public static final String TIM_NAME = "Tim";

    public static final String VODAFONE_VALUE = "10015";

    public static final String VODAFONE_NAME = "Vodafone";

    public static final String TISCALI_NAME = "Tiscali";

    public static final String TISCALI_VALUE = "10017";

    public static final String WIND_VALUE = "10019";

    public static final String WIND_NAME = "Wind";

    public static final String TRE_VALUE = "10020";

    public static final String TRE_NAME = "Tre";
    
    public static final String []operatorValues={TIM_VALUE,VODAFONE_VALUE,TISCALI_VALUE,WIND_VALUE,TRE_VALUE};
    public static final String []operatorNames={TIM_NAME,VODAFONE_NAME,TISCALI_NAME,WIND_NAME,TRE_NAME};

    public static String getCodeByName(String str) {

        if (str == null || str.equals("")) {

            return null;
        }
        if (str.equals(TIM_NAME)) {
            return TIM_VALUE;
        } else if (str.equals(VODAFONE_NAME)) {
            return VODAFONE_VALUE;
        } else if (str.equals(TISCALI_NAME)) {
            return TISCALI_VALUE;
        } else if (str.equals(WIND_NAME)) {
            return WIND_VALUE;
        } else if (str.equals(TRE_NAME)) {
            return TRE_VALUE;
        }
        return null;
    }
    public static String getNameByCode(String str) {

        if (str == null || str.equals("")) {

            return null;
        }
        if (str.equals(TIM_VALUE)) {
            return TIM_NAME;
        } else if (str.equals(VODAFONE_VALUE)) {
            return VODAFONE_NAME;
        } else if (str.equals(TISCALI_VALUE)) {
            return TISCALI_NAME;
        } else if (str.equals(WIND_VALUE)) {
            return WIND_NAME;
        } else if (str.equals(TRE_VALUE)) {
            return TRE_NAME;
        }
        return null;
    }

}
