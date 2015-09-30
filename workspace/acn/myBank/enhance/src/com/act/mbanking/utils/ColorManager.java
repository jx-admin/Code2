
package com.act.mbanking.utils;

import android.graphics.Color;

import com.act.mbanking.App;
import com.act.mbanking.R;

/**
 * 统一设置颜色 0为首页显示的颜色
 * 
 * @author seekting.x.zhang
 */
public class ColorManager {

    public static final int DefaultColor = Color.argb(255, 255, 255, 255);

    public static int getInvestmentColor(int level) {

        int result = DefaultColor;
        int colorRes = R.color.legend_investments_color;
        if (level == 0) {

        } else {
            level = 10 - level;
        }
        switch (level) {
            case 0:

                break;
            case 1:
                colorRes = R.color.investment_1;

                break;
            case 2:
                colorRes = R.color.investment_2;
                break;
            case 3:
                colorRes = R.color.investment_3;
                break;
            case 5:
                colorRes = R.color.investment_5;
                break;
            case 6:
                colorRes = R.color.investment_6;
                break;
            case 7:
                colorRes = R.color.investment_7;
                break;

            case 8:
                colorRes = R.color.investment_8;
                break;
            case 9:
                colorRes = R.color.investment_9;
                break;

        }
        result = App.app.getResources().getColor(colorRes);
        return result;
    }

    public static int getLoansColor(int level) {

        if (level == 0) {

        } else {
            level = 11 - level;
        }
        int result = DefaultColor;
        int colorRes = R.color.legend_total_loans_color;

        switch (level) {
            case 0:

                break;
            case 1:
                colorRes = R.color.loan_1;

                break;
            case 2:
                colorRes = R.color.loan_2;
                break;
            case 3:
                colorRes = R.color.loan_3;
                break;
            case 5:
                colorRes = R.color.loan_5;
                break;
            case 6:
                colorRes = R.color.loan_6;
                break;
            case 7:
                colorRes = R.color.loan_7;
                break;

            case 8:
                colorRes = R.color.loan_8;
                break;
            case 9:
                colorRes = R.color.loan_9;
                break;

        }
        result = App.app.getResources().getColor(colorRes);
        return result;
    }

    public static int getAccountColor(int level) {
        if (level == 0) {

        } else {
            level = 10 - level;
        }
        int result = DefaultColor;
        int colorRes = R.color.legend_accounts_color;

        switch (level) {
            case 0:

                break;
            case 1:
                colorRes = R.color.account_1;

                break;
            case 2:
                colorRes = R.color.account_2;
                break;
            case 3:
                colorRes = R.color.account_3;
                break;
            case 5:
                colorRes = R.color.account_5;
                break;
            case 6:
                colorRes = R.color.account_6;
                break;
            case 7:
                colorRes = R.color.account_7;
                break;

            case 8:
                colorRes = R.color.account_8;
                break;
            case 9:
                colorRes = R.color.account_9;
                break;

        }
        result = App.app.getResources().getColor(colorRes);
        return result;
    }

    public static int getCreditCardsColor(int level) {
        if (level == 0) {

        } else {
            level = 10 - level;
        }
        int result = DefaultColor;
        int colorRes = R.color.legend_credit_cards_color;

        switch (level) {
            case 0:

                break;
            case 1:
                colorRes = R.color.credit_card_1;

                break;
            case 2:
                colorRes = R.color.credit_card_2;
                break;
            case 3:
                colorRes = R.color.credit_card_3;
                break;
            case 5:
                colorRes = R.color.credit_card_5;
                break;
            case 6:
                colorRes = R.color.credit_card_6;
                break;
            case 7:
                colorRes = R.color.credit_card_7;
                break;

            case 8:
                colorRes = R.color.credit_card_8;
                break;
            case 9:
                colorRes = R.color.credit_card_9;
                break;

        }
        result = App.app.getResources().getColor(colorRes);
        return result;
    }

    public static int getPrepaidCardsColor(int level) {
        if (level == 0) {

        } else {
            level = 10 - level;
        }
        int result = DefaultColor;
        int colorRes = R.color.legend_prepaid_cards_color;

        switch (level) {
            case 0:

                break;
            case 1:
                colorRes = R.color.prepaid_card_1;

                break;
            case 2:
                colorRes = R.color.prepaid_card_2;
                break;
            case 3:
                colorRes = R.color.prepaid_card_3;
                break;
            case 5:
                colorRes = R.color.prepaid_card_5;
                break;
            case 6:
                colorRes = R.color.prepaid_card_6;
                break;
            case 7:
                colorRes = R.color.prepaid_card_7;
                break;

            case 8:
                colorRes = R.color.prepaid_card_8;
                break;
            case 9:
                colorRes = R.color.prepaid_card_9;
                break;

        }
        result = App.app.getResources().getColor(colorRes);
        return result;
    }
}
