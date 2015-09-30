
package com.act.mbanking;

/**
 * 货币符号查找
 * 
 * @author yang.c.li
 */
public class CurrencyStyle {
    // 美元
    private static String DOLLARO = "Dollaro";

    private static String DOLLARO_STYLE = "$";

    // 欧元
    private static String EURO = "Euro";

    private static String EURO_STYLE = "€";

    public static final String getCurrency(String currency) {
        if (currency == null || currency.equals("")) {
            return null;
        } else if (currency.equals(DOLLARO)) {
            return DOLLARO_STYLE;
        } else if (currency.equals(EURO)) {
            return EURO_STYLE;
        }
        return null;
    }

}
