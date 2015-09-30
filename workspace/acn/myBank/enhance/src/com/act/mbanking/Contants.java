
package com.act.mbanking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.bean.GetPushPreferencesModel;
import com.act.mbanking.bean.GetUserInfoResponseModel;
import com.act.mbanking.bean.ListAdvNewsModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.SettingModel;
import com.act.mbanking.secure.FileUtil;
import com.act.mbanking.utils.ColorManager;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.ChartModelMapTool;

public class Contants {
    /**
     * //此变量为三星中文输入法包名
     */
    public static final String SAMSUNG_CHINESE_IME = "com.samsung.inputmethod/.SamsungIME";

    /**
     * // 三星输入法包名
     */
    public static final String SAMSUNG_IME = "com.sec.android.inputmethod/.SamsungKeypad";

    public static final String PAYLOAD = "payload";

    public static boolean isNotLastUpdate = false;

    public static String total_assets;

    public static final String decimalPointItaly = ",";

    public static final String decimalPoint = ".";

    public static final String EMPTY = "";

    public static final String COUNTRY = "$";

    /**
     * 保存序列化的文件名应该是当前的用户名+ SAVENAME
     */
    public static final String SAVENAME = "SERIALIZEUSERINFO";

    public static final int ChannelToRecelvePin = SettingModel.EMAIL;
    public static String getOtpChannelName(){
    	if (Contants.ChannelToRecelvePin == SettingModel.EMAIL) {
			return OTP_CHANNEL_MAIL;
		}else// if (Contants.ChannelToRecelvePin == SettingModel.SMS) {
			return OTP_CHANNEL_CELL;
		//}
    }

    /**
     * otp
     */
    public static final String OTP_CHANNEL_MAIL = "MAIL";

    /**
     * otp
     */
    public static final String OTP_CHANNEL_CELL = "CELL";

    public static final String GET_CARDS_MOVEMENTS = "1";

    public static final String GET_ACCOUNTS_MOVEMENTS = "2";
    

	public static final String TRACKER_ID="UA-44000182-4";

    /**
     * IP地址
     */
    public static final String URL_PREFIX_ROME1 = "http://192.168.83.179:8080";

    public static final String URL_PREFIX_ROME2 = "http://10.40.23.54:8080";

    public static final String URL_PREFIX_ROME3 = "https://ams-cms-demo.mobility-managed.com";

    public static final String URL_PREFIX_ROME4 = "http://192.168.83.229:8080";

    /** a new SIT environment with Jboss 6 */
    public static final String URL_PREFIX_ROME5 = "http://10.60.12.20:8080";

    public static final String URL_PREFIX_ROME = URL_PREFIX_ROME3;

    public static String URL_NAME = "";

    static {
        if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME3)) {
            URL_NAME = "DEMO";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME1)) {
            URL_NAME = "SIT";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME2)) {
            URL_NAME = "DEV";
        } else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME4)) {
            URL_NAME = "DEV2";
        }
    }

    /**
     * all..
     */
    public static final String mobile_url = URL_PREFIX_ROME + "/mobile-sdk/";

    /**
     * only getBranchList, getHelpItem, getAdvnews
     */
    public static final String public_mobile_url = URL_PREFIX_ROME + "/public-mobile-sdk";

    /**
     * advNews
     */
    public static final String abi = "05387";

    /**
     * 广告列表
     */
    public static List<ListAdvNewsModel> advNewsList;

    /**
     * 用户信息
     */
    public static GetUserInfoResponseModel getUserInfo;

    /**
     * 
     */
    public static GetPushPreferencesModel getPushPreferences;

    public static Bitmap advImageRefThumb;

    public static Bitmap advImageRef;

    /**
     * 
     */
    public static RequestPublicModel publicModel = new RequestPublicModel();

    public static List<AccountsModel> accountsList;

    public static final String PREPAID_CARD_RELOAD = "PREPAID CARD RELOAD";

    public static final String SIM_TOP_UP = "SIM TOP UP";

    public static final String BANK_TRANSFER = "BANK TRANSFER";

    public static final String TRANSFER_ENTRY = "TRANSFER ENTRY";

    public static final String BILL_PAYMENT = "BILL PAYMENT";

    public static final String PRECOMPILED_BILL = "PRECOMPILED BILL";

    public static final String BLANK_BILL = "BLANK BILL";

    public static final String MAV_RAV = "MAV/RAV";
    /**
     * account roll
     */
    public static List<AccountsModel> baseAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> investmentAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> pushAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> assertInvestmentAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> creditCardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> cardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> prepaidCardAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> baseLoanAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> loansAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> mortgateLoanAccounts = new ArrayList<AccountsModel>();

    public static List<AccountsModel> depositInvestmentAccounts = new ArrayList<AccountsModel>();

    public static void setAccounts() {
        allChartModelMapTools = null;
        baseAccounts.clear();

        investmentAccounts.clear();

        pushAccounts.clear();

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
                } else if ("877".equals(serviceCode)) {
                    pushAccounts.add(accountsModel);
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

    /**
     * 
     */
    public static void clearAll() {
        baseAccounts.clear();
        investmentAccounts.clear();
        pushAccounts.clear();
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
        allChartModelMapTools = null;
        getUserInfo = null;
        getPushPreferences = null;
    }

    /**
     * 保存
     */
    public static void saveGetUserInfo(String fileName, Object obj, String password) {
        try {
            FileUtil fileUtil = new FileUtil(App.app);
            fileUtil.save(fileName, obj, FileUtil.AES_EncryptType, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取
     */
    public static void restoreUserInfo(String fileName, String password) {
        GetUserInfoResponseModel getuserInfo;
        try {
            FileUtil fileUtil = new FileUtil(App.app);
            getuserInfo = (GetUserInfoResponseModel)fileUtil.readObject(fileName,
                    FileUtil.AES_EncryptType, password);
            SerializeUserInfo.getInstance().setLocalUserInfo(getuserInfo);
        } catch (Exception e) {
            SerializeUserInfo.getInstance().setLocalUserInfo(null);
        }
    }

    private static List<ChartModelMapTool> allChartModelMapTools;

    // public static List<ChartModelMapTool> getAllChartModelMapTools(Context
    // cxt) {
    //
    // if (allChartModelMapTools == null) {
    // allChartModelMapTools = generateChartModelMapTool(cxt);
    // }
    // return allChartModelMapTools;
    // }

    @Deprecated
    public static List<ChartModelMapTool> generateChartModelMapTool(Context cxt) {
        List<ChartModel> accountChartModels = new ArrayList<ChartModel>();
        List<ChartModel> prepaidCardChartModels = new ArrayList<ChartModel>();
        List<ChartModel> creditCardChartModels = new ArrayList<ChartModel>();
        List<ChartModel> investmentsChartModels = new ArrayList<ChartModel>();
        List<ChartModel> loansChartModels = new ArrayList<ChartModel>();
        List<ChartModel> pushChartModels = new ArrayList<ChartModel>();

        accountChartModels = getChartModel(Contants.baseAccounts);
        prepaidCardChartModels = getChartModel(Contants.prepaidCardAccounts);
        creditCardChartModels = getChartModel(Contants.creditCardAccounts);
        investmentsChartModels = getChartModel(Contants.investmentAccounts);
        loansChartModels = getChartModel(Contants.loansAccounts);
        pushChartModels = getChartModel(Contants.pushAccounts);

        List<ChartModelMapTool> currentChartModelMapTools = new ArrayList<ChartModelMapTool>();
        ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(accountChartModels);
        chartModelMapTool.color = ColorManager.getAccountColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_accounts;
        chartModelMapTool.title = cxt.getString(R.string.legend_accounts);
        currentChartModelMapTools.add(chartModelMapTool);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(loansChartModels);
        chartModelMapTool.color = ColorManager.getLoansColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_loans;
        chartModelMapTool.title = cxt.getString(R.string.legend_total_loans);
        currentChartModelMapTools.add(chartModelMapTool);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(investmentsChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getInvestmentColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_investments;
        chartModelMapTool.title = cxt.getString(R.string.legend_investments);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(prepaidCardChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getPrepaidCardsColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_prepaieds;
        chartModelMapTool.title = cxt.getString(R.string.legend_prepaid_cards);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(creditCardChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getCreditCardsColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_credit;
        chartModelMapTool.title = cxt.getString(R.string.legend_credit_cards);

        return currentChartModelMapTools;
    }

    public static List<ChartModel> getChartModel(List<AccountsModel> accountModels) {
        List<ChartModel> chartModels = new ArrayList<ChartModel>();
        if (accountModels != null) {
            for (int j = 0; j < accountModels.size(); j++) {
                AccountsModel acountModel = accountModels.get(j);
                List<AggregatedAccount> list = acountModel.getChartAggregatedAccountsList();
                if (list != null) {
                    List<AggregatedAccount> aggregatedAccounts = list;
                    for (int i = 0; i < aggregatedAccounts.size(); i++) {
                        List<ChartModel> newPrepaidChartChartModel = list.get(i).getCharts();
                        chartModels.addAll(newPrepaidChartChartModel);
                    }
                }
            }
        }
        return chartModels;
    }

    /**
     * Investments总和
     * 
     * @param accountList
     * @param dstM
     * @return
     */
    public static double investmentsSyncDashBoard(List<AccountsModel> accountList, long dstM) {
        double accountSum = 0;

        if (accountList == null || accountList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < accountList.size(); i++) {
            List<AggregatedAccount> aggregatedAccountList = accountList.get(i)
                    .getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            accountSum += Contants.getInvestmentDashBoardData(aggregatedAccountList, dstM);
        }
        return accountSum;
    }

    public static double getInvestmentDashBoardData(List<AggregatedAccount> aggregatedAccountList,
            long dstM) {
        double sum = 0;
        for (int j = 0; j < aggregatedAccountList.size(); j++) {
            Map<String, List<DashboardDataModel>> hashMap = new HashMap<String, List<DashboardDataModel>>();
            List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                    .getDashboardDataList();
            if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                continue;
            }
            for (DashboardDataModel dashboardData : dashBoardDatalist) {
                String investId = dashboardData.getInvestmentId();
                List<DashboardDataModel> dbs = hashMap.get(investId);
                if (dbs == null) {
                    dbs = new ArrayList<DashboardDataModel>();
                    dbs.add(dashboardData);
                    hashMap.put(investId, dbs);
                    continue;
                }

                if (dbs.contains(dashboardData)) {
                    continue;
                }
                dbs.add(dashboardData);
            }

            /*
             * get dashboard data for somedate
             */
            for (String key : hashMap.keySet()) {
                List<DashboardDataModel> dashBoardDatalist1 = hashMap.get(key);
                if (dashBoardDatalist1 != null) {
                    int index = Utils.getDateIndex(dashBoardDatalist1, dstM);
                    sum += dashBoardDatalist1.get(index).getTotalPortfolio();
                }
            }
        }
        return sum;
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
