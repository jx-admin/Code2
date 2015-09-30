
package com.accenture.mbank.util;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetPushReferencesModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.GetUserInfoResponseModel;
import com.accenture.mbank.model.ListAdvNewsModel;
import com.accenture.mbank.model.PushCategoryModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.SettingModel;

public class Contants {
    /**
     * //此变量为三星中文输入法包名
     */
    public static final String SAMSUNG_CHINESE_IME = "com.samsung.inputmethod/.SamsungIME";

    /**
     * // 三星输入法包名
     */
    public static final String SAMSUNG_IME = "com.sec.android.inputmethod/.SamsungKeypad";

    public static String msgTitle = null;

    public static String msgContext = null;

    public static final String PAYLOAD = "payload";

    public static final String COUNTRY = "$";

    public static final int ChannelToRecelvePin = SettingModel.EMAIL;

    public static String userName;

    public static String password;

    public static String Ver = "";

    public static final String TRACKER_ID = "UA-44000182-3";

    public static RequestPublicModel publicModel = new RequestPublicModel();

    public static List<AccountsModel> accountsList;

    public static final String URL_PREFIX_ROME1 = "http://192.168.83.179:8080";

    public static final String URL_PREFIX_ROME2 = "http://10.40.23.54:8080";

    public static final String URL_PREFIX_ROME3 = "https://ams-cms-demo.mobility-managed.com/";

    public static final String URL_PREFIX_ROME4 = "http://192.168.83.229:8080";

    /** a new SIT environment with Jboss 6 */
    public static final String URL_PREFIX_ROME5 = "http://10.60.12.20:8080";

    public static final String URL_PREFIX_ROME = URL_PREFIX_ROME3;

    public static String URL_NAME = "";

    public static final String URL_PREFIX_LOCAL = "http://localhost:81";
    static {
        if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME3)) {
            URL_NAME = "DEMO";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME1)) {
            URL_NAME = "SIT";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME2)) {
            URL_NAME = "DEV";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME4)) {
            URL_NAME = "DEV2";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME5)) {
            URL_NAME = "SIT2";
        }
    }

    public static final boolean isNewPaymentsUpdate = true;

    /**
     * all..
     */
    public static final String mobile_url = URL_PREFIX_ROME + "/mobile-sdk/";

    /**
     * only getBranchList, getHelpItem, getAdvnews
     */
    public static final String public_mobile_url = URL_PREFIX_ROME + "/public-mobile-sdk";

    /**
     * 查找周边银行的范围
     */
    public static final int range = 15000;

    /**
     * advNews
     */
    public static final String abi = "05387";

    /**
     * otp
     */
    public static final String OTP_CHANNEL_MAIL = "MAIL";

    public static final String OTP_CHANNEL_CELL = "CELL";

    public static String getOtpChannelName() {
        if (Contants.ChannelToRecelvePin == SettingModel.EMAIL) {
            return OTP_CHANNEL_MAIL;
        } else
            // if (Contants.ChannelToRecelvePin == SettingModel.SMS) {
            return OTP_CHANNEL_CELL;
        // }
    }

    /**
     * 转账类型
     */
    public static final String TRANSFER_TYPE = "0";

    /**
     * 数量
     */
    public static final int AMOUNT = 10;

    public static final String PREPAID_CARD_RELOAD = "PREPAID CARD RELOAD";

    public static final String SIM_TOP_UP = "SIM TOP UP";

    public static final String BANK_TRANSFER = "BANK TRANSFER";

    public static final String TRANSFER_ENTRY = "TRANSFER ENTRY";

    public static final String BILL_PAYMENT = "BILL PAYMENT";

    public static final String PRECOMPILED_BILL = "PRECOMPILED BILL";

    public static final String BLANK_BILL = "BLANK BILL";

    public static final String MAV_RAV = "MAV RAV";

    public static Drawable advNewsThumb = null;

    public static GetUserInfoResponseModel getUserInfo;

    /**
     * 
     */
    public static GetPushReferencesModel getPushPreferences;

    /**
     * account roll
     */
    public static List<AccountsModel> baseAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> investmentAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> assertInvestmentAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> creditCardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> cardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> prepaidCardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> baseLoanAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> loansAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> mortgateLoanAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> depositInvestmentAccounts = new ArrayList<AccountsModel>();

    public static List<PushCategoryModel> pushSettings = new ArrayList<PushCategoryModel>();

    public static Bitmap advImageRefThumb;

    public static Bitmap advImageRef;

    /**
     * 广告列表
     */
    public static List<ListAdvNewsModel> advNewsList;

    public static List<AccountsForServiceModel> bankTransferAccounts;

    public static List<AccountsForServiceModel> transferEntryAccounts;

    public static List<AccountsForServiceModel> simTopUpAccounts;

    public static List<AccountsForServiceModel> chargeAccounts;

    public static GetRecipientListModel getRecipientListModel;

    /**
     * 注销
     */
    public static void logOut() {
        if (getUserInfo != null) {
            getUserInfo = null;
        }

        if (bankTransferAccounts != null) {
            bankTransferAccounts.clear();
        }
        if (transferEntryAccounts != null) {
            transferEntryAccounts.clear();
        }
        if (simTopUpAccounts != null) {
            simTopUpAccounts.clear();
        }
        if (chargeAccounts != null) {
            chargeAccounts.clear();
        }
        bankTransferAccounts = null;
        transferEntryAccounts = null;
        simTopUpAccounts = null;
        chargeAccounts = null;

        getRecipientListModel = null;

    }

    public static void clearAll() {
        userName = null;
        password = null;
        baseAccounts.clear();
        investmentAccounts.clear();
        assertInvestmentAccounts.clear();
        creditCardAccounts.clear();
        cardAccounts.clear();
        prepaidCardAccounts.clear();
        baseLoanAccounts.clear();
        loansAccounts.clear();
        mortgateLoanAccounts.clear();
        depositInvestmentAccounts.clear();
        if (accountsList != null) {
            accountsList.clear();
        }

        publicModel = new RequestPublicModel();

        BaseActivity.isLogin = false;
    }

    public static void setAccounts() {
        baseAccounts.clear();

        investmentAccounts.clear();

        assertInvestmentAccounts.clear();

        creditCardAccounts.clear();

        cardAccounts.clear();

        prepaidCardAccounts.clear();

        baseLoanAccounts.clear();

        loansAccounts.clear();

        mortgateLoanAccounts.clear();

        depositInvestmentAccounts.clear();

        if (getUserInfo != null && getUserInfo.getAccountList() != null
                && getUserInfo.getAccountList().size() > 0) {
            for (AccountsModel accountsModel : getUserInfo.getAccountList()) {
                String serviceCode = accountsModel.getBankServiceType().getBankServiceCode();
                String type = accountsModel.getMortageType();
                if (serviceCode.equals("020")) {
                    baseAccounts.add(accountsModel);
                } else if (serviceCode.equals("827")) {
                    investmentAccounts.add(accountsModel);
                    if ("DT".equals(type)) {
                        depositInvestmentAccounts.add(accountsModel);
                    } else if ("GP".equals(type)) {
                        assertInvestmentAccounts.add(accountsModel);
                    }
                } else if ("872".equals(serviceCode)) {
                    creditCardAccounts.add(accountsModel);
                    cardAccounts.add(accountsModel);
                } else if ("867".equals(serviceCode)) {
                    prepaidCardAccounts.add(accountsModel);
                    cardAccounts.add(accountsModel);
                } else if ("034".equals(serviceCode)) {
                    accountsModel.setFinanceType("MU");
                    mortgateLoanAccounts.add(accountsModel);
                    loansAccounts.add(accountsModel);
                } else if ("041".equals(serviceCode)) {
                    accountsModel.setFinanceType("PR");
                    mortgateLoanAccounts.add(accountsModel);
                    loansAccounts.add(accountsModel);
                }

            }
        }
    }

    public static boolean getInitSetting(SharedPreferences sp) {
        return sp.getBoolean(PUSH_INIT_KEY, false);
    }

    public static void updataInitSetting(Editor editor, boolean flag) {
        editor.putBoolean(PUSH_INIT_KEY, flag);
        editor.commit();
    }

    /**
     * push notification 初始化key
     */
    public static final String PUSH_INIT_KEY = "PUSH_INIT_KEY";

    public static final String SETTING_FILE_NAME = "setting";
}
