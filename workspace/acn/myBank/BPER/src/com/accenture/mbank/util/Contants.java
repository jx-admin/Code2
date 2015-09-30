package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.BasicCookieStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.GetUserInfoResponseModel;
import com.accenture.mbank.model.ListAdvNewsModel;
import com.accenture.mbank.model.PushCategoryModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.SettingModel;

public class Contants {
	public static final String COUNTRY = "€";

	public static final int ChannelToRecelvePin = SettingModel.EMAIL;

	public static String userName;

	public static String password;

	public static String Ver = "";

	public static boolean DASHBOARD_ROTATE_ANIMATION_ACCOUNTS = true;
	public static boolean DASHBOARD_ROTATE_ANIMATION_CARDS = true;
	public static boolean DASHBOARD_ROTATE_ANIMATION_LOANS = true;
	public static String userAgent;
	/**
	 * payment disable
	 */
	public static boolean payment_disabled = true;

	public static RequestPublicModel publicModel = new RequestPublicModel();

	public static BasicCookieStore cookieStore = new BasicCookieStore();

	public static List<AccountsModel> accountsList;
	/** BPER SIT */
	public static final String URL_PREFIX_ROME1 = "http://10.40.23.61:9004";
	/** BPER DEV */
	public static final String URL_PREFIX_ROME2 = "http://10.40.23.80:8080";

	public static final String URL_PREFIX_ROME3 = "https://mobilebanking.mobility-managed.com";
	
	public static final String URL_PREFIX_ROME4 = "http://10.40.23.61:9104";// "http://192.168.80.181:81";
	
	public static final String URL_PREFIX_ROME5 = "http://192.168.80.181:81";

	public static final String URL_PREFIX_ROME6 = "https://homebanking-stag.bpergroup.net";
	
	public static final String URL_PREFIX_ROME7 = "https://10.40.23.85";

	public static final String URL_PREFIX_ROME8 = "https://homebanking.bpergroup.net";

	public static final String URL_PREFIX_ROME = URL_PREFIX_ROME7;
	
	public static String URL_NAME = "";
	
	public static String PATTERN = "[a-zA-Z0-9/:()\\.,'\\+ \\-]|(\\?)";

	// Use only in test environment!!!!
	public static final boolean IS_SSL_TO_BYPASS = true;
	
	static {
		if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME1)) {
			URL_NAME = "SIT";
		} else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME2)) {
			URL_NAME = "DEV";
		} else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME4)) {
			URL_NAME = "SIT2";
		} else if (URL_PREFIX_ROME.equals(URL_PREFIX_ROME3)) {
			URL_NAME = "PUBLIC IP";
		} else if(URL_PREFIX_ROME.equals(URL_PREFIX_ROME5)){
			URL_NAME = "SIT3";
		}
		else if(URL_PREFIX_ROME.equals(URL_PREFIX_ROME6)){
			URL_NAME = "SIT2";
		}
		else if(URL_PREFIX_ROME.equals(URL_PREFIX_ROME8)){
			URL_NAME = "";
		}
	}

	/**
	 * all..
	 */
	public static final String mobile_url = URL_PREFIX_ROME + "/mobile-sdk/";

	/**
	 * only getBranchList, getHelpItem, getAdvnews
	 */
	public static final String public_mobile_url = URL_PREFIX_ROME
			+ "/public-mobile-sdk/";

	/**
	 * advNews
	 */
	public static String abi = "";

	/**
	 * otp
	 */
	public static final String OTP_CHANNEL_MAIL = "MAIL";

	public static final String OTP_CHANNEL_CELL = "CELL";

	public static String getOtpChannelName() {
		if (MainActivity.setting!=null&&MainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
			return OTP_CHANNEL_MAIL;
		} else{
			return OTP_CHANNEL_CELL;
		}
	}

	public static final String PREPAID_CARD_RELOAD = "PREPAID_CARD_RELOAD";

	public static final String SIM_TOP_UP = "SIM TOP UP";

	public static final String BANK_TRANSFER = "BANK TRANSFER";

	public static final String TRANSFER_ENTRY = "TRANSFER ENTRY";

	public static GetUserInfoResponseModel getUserInfo;

	/**
	 * account roll
	 */
	public static List<AccountsModel> baseAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> investmentAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> assertInvestmentAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> creditCardAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> cardAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> prepaidCardAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> ibanCardAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> baseLoanAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> loansAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> mortgateLoanAccounts = new ArrayList<AccountsModel>();

	public static List<AccountsModel> depositInvestmentAccounts = new ArrayList<AccountsModel>();

	public static List<PushCategoryModel> pushSettings = new ArrayList<PushCategoryModel>();

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
		ibanCardAccounts.clear();
		baseLoanAccounts.clear();
		loansAccounts.clear();
		mortgateLoanAccounts.clear();
		depositInvestmentAccounts.clear();
		if (accountsList != null) {
			accountsList.clear();
		}

		publicModel = new RequestPublicModel();
		publicModel.setUserAgent(userAgent);
		BaseActivity.isLogin = false;
		cookieStore.clear();
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

		if (getUserInfo != null
				&& getUserInfo.getUserprofileHb().getAccountList() != null
				&& getUserInfo.getUserprofileHb().getAccountList().size() > 0) {
			for (AccountsModel accountsModel : getUserInfo.getUserprofileHb()
					.getAccountList()) {
				String serviceCode = accountsModel.getBankServiceType()
						.getBankServiceCode();
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
				} else if ("867".equals(serviceCode)
						|| "869".equals(serviceCode)) {
					prepaidCardAccounts.add(accountsModel);
					cardAccounts.add(accountsModel);
				} else if (Contants.IBAN_CARD_CODE.equals(serviceCode)) {
					ibanCardAccounts.add(accountsModel);
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

	public static final int TIME_FADE_IN_ANIMATION = 700;
	public static final int TIME_FADE_OUT_ANIMATION = 400;

	/**
	 * push notification 初始化key
	 */
	public static final String PUSH_INIT_KEY = "PUSH_INIT_KEY";

	public static final String SETTING_FILE_NAME = "setting";

	/*
	 * Card Type Code
	 */
	public static final String CREDIT_CARD_CODE = "872";

	public static final String PREPAID_CARD_CODE = "867";

	public static final String PREPAID_CARD_CODE_1 = "869";

	public static final String IBAN_CARD_CODE = "887";

	/*
	 * ERROR CODE
	 */
	public static final String ERR_GENERIC_ERROR = "90000";
	
	public static final String ERR_SESSION_EXPIRED_1 = "91400";
	
	public static final String ERR_SESSION_EXPIRED_2 = "91402";

	/*
	 * Short month will be displayed, if months more than or equal to this
	 * value.
	 */
	public static final int CHART_THRESHOLD_MONTHS = 9;

	public static final String PAID_STATUS = "Pagata";
	/**
	 * Bank code
	 */
	public static final String BANK_CODE = "BANK_CODE";

	public static String getBankCode(Context context) {
		final SharedPreferences settings = context.getSharedPreferences(
				Contants.SETTING_FILE_NAME, Context.MODE_PRIVATE);
		return settings.getString(Contants.BANK_CODE, "");
	}

	public static final Integer[] idBankButton = { R.id.banca_01015,
			R.id.banca_05256, R.id.banca_05387, R.id.banca_05392,
			R.id.banca_05640, R.id.banca_05676 };

	public static final String[] strBankCode = { "01015", "05256", "05387",
			"05392", "05640", "05676" };

	public static final Integer[] idBankLogo = { R.drawable.logo_01015,
			R.drawable.logo_05256, R.drawable.logo_05387,
			R.drawable.logo_05392, R.drawable.logo_05640, R.drawable.logo_05676 };

	public static final String[] usBankCode = new String[] {};

	public static String getUsBankCode(String bankCode) {
		return "800.22.77.88";
	}
	
	public static String getAltroPaese(){
		return "+39 059 2919622";
	}

	public static String getBusinessHourBackCode(String bankCode) {
		return "08.30 - 20.30 Lunedì - Venerdì";
	}

	public static String getEmailBackCode(String bankCode) {
		return "servizio.clienti@gruppobper.it";
	}

	private static String[] webSideBackCode = new String[] {
			"http://www.bancosardegna.it", "http://www.bpmezzogiorno.it",
			"http://www.bper.it", "http://www.bancacampania.it",
			"http://www.bpr.it", "http://www.bancasassari.it" };

	public static String getWebsiteBankCode(String bankCode) {
		for (int i = 0; i < strBankCode.length; i++) {
			if (strBankCode[i].endsWith(bankCode)) {
				return webSideBackCode[i];
			}
		}
		return "";
	}
}

