
package com.accenture.mbank.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public class SettingModel extends DataBaseModel {

    public static final String TABLE_NAME = "setting";

    private String userId;

    public static final String COUM_USER_ID = "user_id";

    // o sorting = 0  DATE ASC
    // o sorting = 1  DATE DESC
    // o sorting = 0  AMOUNT DESC <-- changed nome to understand

    public static final int SORT_AMOUNT_DESC = 0;

    public static final int SORT_DATE_DESC = 1;

    /**
     * value= sort_amount<br>
     * or sort_date<br>
     */
    private int orderListFor = SORT_DATE_DESC;

    public static final String COUM_ORDER_LIST_FOR = "order_list_for";

    private int dashboardAccounts = 4;

    public static final String COUM_DASHBOARD_ACCOUNTS = "dashboard_accounts";

    private int dashboardCards = 4;

    public static final String COUM_DASHBOARD_CARDS = "dashboard_cards";

    public static final int LAST_20 = 20;

    public static final int LAST_2_MONTH = 1;

    public static final String COUM_SHOW_TRANSACTION_BY = "show_transaction_by";

    private int showTransactionBy = LAST_20;

    public static final int RECENT_10 = 10;

    public static final int RECENT_20 = 20;

    public static final int RECENT_30 = 30;

    private int recentPaymentsDisplayed = RECENT_10;

    public static final String COUM_RECENT_PAYMENTS_DISPLAYED = "recent_payments_displayed";

    public static final int EMAIL = 0;

    // sms 使用的参数叫cell不知道为啥..就这么写吧.
    public static final int SMS = 1;

    private int channelToRecelvePin = SMS;

    public static final String COUM_CHANNEL_TO_RECELVE_PIN = "channel_to_recelve_pin";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 排序
     * 
     * @return
     */
    public int getOrderListFor() {
        return orderListFor;
    }

    public void setOrderListFor(int orderListFor) {
        this.orderListFor = orderListFor;
    }

    public int getDashboardAccounts() {
        return dashboardAccounts;
    }

    public void setDashboardAccounts(int dashboardAccounts) {
        this.dashboardAccounts = dashboardAccounts;
    }

    public int getDashboardCards() {
        return dashboardCards;
    }

    public void setDashboardCards(int dashboardCards) {
        this.dashboardCards = dashboardCards;
    }

    public int getShowTransactionBy() {
        return showTransactionBy;
    }

    public void setShowTransactionBy(int showTransactionBy) {
        this.showTransactionBy = showTransactionBy;
    }

    public int getRecentPaymentsDisplayed() {
        return recentPaymentsDisplayed;
    }

    public void setRecentPaymentsDisplayed(int recentPaymentsDisplayed) {
        this.recentPaymentsDisplayed = recentPaymentsDisplayed;
    }

    public int getChannelToRecelvePin() {
        return channelToRecelvePin;
    }

    public void setChannelToRecelvePin(int channelToRecelvePin) {
        this.channelToRecelvePin = channelToRecelvePin;
    }

    @Override
    public String toString() {
        return "UserInfoDataHelperModel [userId=" + userId + ", orderListFor=" + orderListFor
                + ", dashboardAccounts=" + dashboardAccounts + ", dashboardCards=" + dashboardCards
                + ", showTransactionBy=" + showTransactionBy + ", recentPaymentsDisplayed="
                + recentPaymentsDisplayed + ", channelToRecelvePin=" + channelToRecelvePin + "]";
    }

    @Override
    public ContentValues generateValues() {
        ContentValues contentValues = super.generateValues();
        contentValues.put(COUM_CHANNEL_TO_RECELVE_PIN, this.channelToRecelvePin);
        contentValues.put(COUM_DASHBOARD_ACCOUNTS, this.dashboardAccounts);
        contentValues.put(COUM_DASHBOARD_CARDS, this.dashboardCards);
        contentValues.put(COUM_ORDER_LIST_FOR, this.orderListFor);
        contentValues.put(COUM_RECENT_PAYMENTS_DISPLAYED, this.recentPaymentsDisplayed);
        contentValues.put(COUM_SHOW_TRANSACTION_BY, this.showTransactionBy);
        contentValues.put(COUM_USER_ID, this.userId);

        return contentValues;
    }

    @Override
    public void setDataByValues(Cursor cursor) {
        super.setDataByValues(cursor);

        this.channelToRecelvePin = cursor
                .getInt(cursor.getColumnIndex(COUM_CHANNEL_TO_RECELVE_PIN));

        this.dashboardAccounts = cursor.getInt(cursor.getColumnIndex(COUM_DASHBOARD_ACCOUNTS));

        this.dashboardCards = cursor.getInt(cursor.getColumnIndex(COUM_DASHBOARD_CARDS));
        this.orderListFor = cursor.getInt(cursor.getColumnIndex(COUM_ORDER_LIST_FOR));
        this.recentPaymentsDisplayed = cursor.getInt(cursor
                .getColumnIndex(COUM_RECENT_PAYMENTS_DISPLAYED));
        this.showTransactionBy = cursor.getInt(cursor.getColumnIndex(COUM_SHOW_TRANSACTION_BY));
        this.userId = cursor.getString(cursor.getColumnIndex(COUM_USER_ID));
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String createTableSQL() {

        String sql = "CREATE TABLE " + SettingModel.TABLE_NAME + " (" + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SettingModel.COUM_CHANNEL_TO_RECELVE_PIN
                + " integer, " + SettingModel.COUM_DASHBOARD_ACCOUNTS + " integer, "
                + SettingModel.COUM_DASHBOARD_CARDS + " integer, "
                + SettingModel.COUM_ORDER_LIST_FOR + " ineger, "
                + SettingModel.COUM_RECENT_PAYMENTS_DISPLAYED + " integer, "
                + SettingModel.COUM_SHOW_TRANSACTION_BY + " integer, " + SettingModel.COUM_USER_ID
                + " TEXT " + ");";
        return sql;

    }

}
