
package com.accenture.mbank;

import static com.accenture.mbank.CommonUtilities.SENDER_ID;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.accenture.manager.AccountRotateLayoutManager;
import com.accenture.manager.CardsRotateLayoutManager;
import com.accenture.manager.InvestmentRotateLayoutManager;
import com.accenture.manager.LoansRotateLayoutManager;
import com.accenture.mbank.database.BankSqliteHelper;
import com.accenture.mbank.model.DataBaseModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountExpandedLayout;
import com.accenture.mbank.view.AccountsLayout;
import com.accenture.mbank.view.AmountExpandedLayout;
import com.accenture.mbank.view.BankRollContainer;
import com.accenture.mbank.view.BankRollView;
import com.accenture.mbank.view.BankSlidContainer;
import com.accenture.mbank.view.CardsLayoutManager;
import com.accenture.mbank.view.DateExpandedLayout;
import com.accenture.mbank.view.DescriptionExpandedLayout;
import com.accenture.mbank.view.InnerScrollView;
import com.accenture.mbank.view.InvestmentsLayoutManager;
import com.accenture.mbank.view.ItemExpander;
import com.accenture.mbank.view.LoansLayout;
import com.accenture.mbank.view.PayeeExpandedLayout;
import com.accenture.mbank.view.PaymentConfirmLayout;
import com.accenture.mbank.view.PinExpandedLayout;
import com.accenture.mbank.view.ReHeightImageButton;
import com.accenture.mbank.view.RecentPayments;
import com.accenture.mbank.view.TypeOfPayMentExpandedLayout;
import com.accenture.mbank.view.UserInfoLayoutManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends BaseActivity implements OnClickListener {

    ImageButton help;

    TabHost tabHost;

    LinearLayout chartsLayout;

    static Context context;

    public static Context getContext() {
        return context;
    }

    InnerScrollView scroll2;

    Button button;

    private ReHeightImageButton imageView1;

    private ReHeightImageButton imageView2;

    private Drawable drawable1;

    private Drawable drawable2;

    private ReHeightImageButton imageView3;

    private Drawable drawable3;

    public ReHeightImageButton imageView4;

    private Drawable drawable4;

    private GoogleAnalytics mGaInstance;

    private Tracker mGaTracker1;

    public static final int SYNTHESIS = 0;

    public static final int ACCOUNTS = 1;

    public static final int CARDS = 2;

    public static final int PAYMENTS = 3;

    Handler mHandler;

    public static final String ACCOUNT_PAYMENTMETHOD = "2";

    public static final String CARDS_PAYMENTMETHOD = "1";

    com.accenture.mbank.view.protocol.ShowListener newVersionRecentPaymentListener;

    com.accenture.mbank.view.protocol.ShowListener newVersionNewPaymentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyTracker.getInstance().activityStart(this); // Add this method.
        LogManager.d("oncreate");
        initSettingModel();
        this.context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initCharts();
        help = (ImageButton)findViewById(R.id.help_btn);
        help.setOnClickListener(this);

        mHandler = new Handler();
        initTabHost();
        initMenu();
        initUserInfoLayout();
        initInvestments();
        initLoansLayout();
        initRotateLayout();
        initInvestmentRotateLayout();
        showTab(0);

        SharedPreferences sp = getSharedPreferences(Contants.SETTING_FILE_NAME, MODE_PRIVATE);
        boolean isInitPushNotification = Contants.getInitSetting(sp);
        if (!isInitPushNotification) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notification();
                }
            });
        }
    }

    protected void notification() {
        final SharedPreferences sp = this.getSharedPreferences(Contants.SETTING_FILE_NAME,
                MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(this, R.style.selectorDialog);
        dialog.setContentView(linearLahyout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(R.string.is_receive_notifictions);
        dialog.show();
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Contants.updataInitSetting(sp.edit(), true);
                registerGCM();
                DialogManager.createMessageDialog(context.getResources().getString(R.string.notifictions_confirmation),context).show();
            }
        });
    }

    private void registerGCM() {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            Log.v("Test GCM", "Already registered" + regId);
        }
    }

    private void initCharts() {
        chartsLayout = (LinearLayout)findViewById(R.id.charts_layout);
        chartsLayout.setVisibility(View.GONE);
    }

    public static SettingModel setting;

    private void initSettingModel() {
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");

            if (username != null) {
                BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
                setting = (SettingModel)bankSqliteHelper.findBykey(SettingModel.TABLE_NAME,
                        SettingModel.COUM_USER_ID, username);
                if (setting == null) {
                    this.setting = new SettingModel();
                    setting.setUserId(username);
                    bankSqliteHelper.insert(setting);
                }

            }

        }
        if (BaseActivity.isOffline) {
            BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
            setting = (SettingModel)bankSqliteHelper.findBykey(SettingModel.TABLE_NAME,
                    SettingModel.COUM_USER_ID, "offline");

            if (setting == null) {
                this.setting = new SettingModel();
                setting.setUserId("offline");
                bankSqliteHelper.insert(setting);
            }
        }
    }

    @Override
    protected void onBackClick() {
        // TODO Auto-generated method stub
        super.onBackClick();
        showTab(0);
    }

    @SuppressWarnings("unused")
    private void testUpdate() {
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");

        if (username != null) {
            BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
            DataBaseModel model = bankSqliteHelper.findBykey(SettingModel.TABLE_NAME,
                    SettingModel.COUM_USER_ID, username);
            LogManager.d(model.toString());
            SettingModel settingModel = (SettingModel)model;
            settingModel.setOrderListFor(1);
            bankSqliteHelper.update(settingModel);
            DataBaseModel model1 = bankSqliteHelper.findBykey(SettingModel.TABLE_NAME,
                    SettingModel.COUM_USER_ID, username);
            LogManager.d("model1=" + model1.toString());
        }
    }

    public static final String TAB_SYNTHESIS = "tab_synthesis";

    public static final String TAB_ACCOUNTS = "tab_accounts";

    public static final String TAB_CARDS = "tab_cards";

    public static final String TAB_PAYMENTS = "tab_payments";

    private LinearLayout tab1, tab2;

    public BankRollContainer tab4, tab3;

    private void initTabHost() {

        tabHost = (TabHost)findViewById(R.id.tab);
        tabHost.setup();

        imageView1 = new ReHeightImageButton(this);
        imageView1.setScaleType(ScaleType.FIT_CENTER);
        imageView1.setOnClickListener(this);
        drawable1 = getResources().getDrawable(R.drawable.label_btn_synthesis);
        imageView1.setImageDrawable(drawable1);
        tabHost.addTab(tabHost.newTabSpec(TAB_SYNTHESIS).setContent(R.id.tab1)
                .setIndicator(imageView1));

        imageView2 = new ReHeightImageButton(this);
        imageView2.setScaleType(ScaleType.FIT_CENTER);
        drawable2 = getResources().getDrawable(R.drawable.label_btn_accounts);
        imageView2.setImageDrawable(drawable2);
        tabHost.addTab(tabHost.newTabSpec(TAB_ACCOUNTS).setContent(R.id.tab2)
                .setIndicator(imageView2));

        imageView3 = new ReHeightImageButton(this);
        imageView3.setScaleType(ScaleType.FIT_CENTER);
        drawable3 = getResources().getDrawable(R.drawable.label_btn_cards);

        imageView3.setImageDrawable(drawable3);

        tabHost.addTab(tabHost.newTabSpec(TAB_CARDS).setContent(R.id.tab3).setIndicator(imageView3));

        imageView4 = new ReHeightImageButton(this);
        imageView4.setScaleType(ScaleType.FIT_CENTER);
        drawable4 = getResources().getDrawable(R.drawable.label_btn_payments);
        imageView4.setImageDrawable(drawable4);

        tabHost.addTab(tabHost.newTabSpec(TAB_PAYMENTS).setContent(R.id.tab4)
                .setIndicator(imageView4));
        imageView1.setBackgroundDrawable(null);
        imageView4.setBackgroundDrawable(null);
        imageView2.setBackgroundDrawable(null);
        imageView3.setBackgroundDrawable(null);
        initTab1();
        initTab2();
        initTab3();
        initTab4();

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                resumeAllTabHostImageButton();

                tab4.setVisibility(View.INVISIBLE);
                hideUserInfo();

                View visibleView = null;
                if (tabId.equals(TAB_PAYMENTS)) {
                    imageView4.setImageDrawable(getResources().getDrawable(
                            R.drawable.label_btn_payments_over));
                    tab4.setVisibility(View.VISIBLE);
                    visibleView = tab4;
                } else if (tabId.equals(TAB_SYNTHESIS)) {
                    imageView1.setImageDrawable(getResources().getDrawable(
                            R.drawable.label_btn_synthesis_over));
                    visibleView = tab1;
                } else if (tabId.equals(TAB_CARDS)) {
                    imageView3.setImageDrawable(getResources().getDrawable(
                            R.drawable.label_btn_cards_over));
                    if (!BaseActivity.isOffline) {
                        cardsLayoutManager.onShow();
                    }
                    visibleView = tab3;
                } else if (tabId.equals(TAB_ACCOUNTS)) {
                    visibleView = tab2;
                    imageView2.setImageDrawable(getResources().getDrawable(
                            R.drawable.label_btn_accounts_over));
                    if (!BaseActivity.isOffline) {
                        accountsLayout.onShow();
                        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
                        mGaTracker1.sendView("view.accounts.movement");
                    }
                }
                setCanOrientation(false);
                onScreenChange(visibleView);

            }

        });
    }

    /**
     * 设置是否能黑屏
     * 
     * @param flag
     */
    public void setCanOrientation(boolean flag) {

        if (flag) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    View currentScreen;

    /**
     * 当tab，或userinfo或loans,investments cards之间交替显示的时候 会响应此方法
     */
    private void onScreenChange(View visibleView) {
        updateNavagition(visibleView);
        if (currentScreen == tab4) {
            paymentConfirmLayout.reset();

        }
        currentScreen = visibleView;

    }

    private void updateNavagition(View visibleView) {
        if (visibleView == tab1 || visibleView == tab2 || visibleView == tab3
                || visibleView == tab4) {

            hideBack();
        } else {
            showBack();
        }

    }

    public void showUserInfo() {

        showView(userInfoLayout);
        userInfoLayoutManager.onShow();
        setCanOrientation(false);
    }

    private void resumeAllTabHostImageButton() {
        imageView1.setImageDrawable(drawable1);
        imageView2.setImageDrawable(drawable2);
        imageView3.setImageDrawable(drawable3);
        imageView4.setImageDrawable(drawable4);
    }

    private void hideUserInfo() {

        hideView(userInfoLayout);
    }

    private void hideView(View v) {
        if (v.getVisibility() != View.GONE)
            v.setVisibility(View.GONE);
    }

    /**
     * 隐藏非tabhost的界面
     */
    private void hideViews() {
        hideUserInfo();
        hideInvestments();

        hideLoans();
        hideView(cardsRotateLayout);
        hideView(accountsRotateLayout);
        hideView(investmentsRotateLayout);
        hideView(loansRotateLayout);

    }

    BankRollContainer userInfoRollContainer;

    ViewGroup userInfoLayout;

    UserInfoLayoutManager userInfoLayoutManager;

    private String getUserInfo() {
        StringBuffer buffer = new StringBuffer("last updated on ");
        SimpleDateFormat format = new SimpleDateFormat(TimeUtil.dateFormat5, Locale.US);
        buffer.append(format.format(Calendar.getInstance().getTime()));
        return buffer.toString();
    }

    private void initUserInfoLayout() {
        userInfoRollContainer = (BankRollContainer)findViewById(R.id.user_info);
        userInfoLayout = (ViewGroup)findViewById(R.id.userinfo_layout);
        TextView userinfo = (TextView)userInfoLayout.findViewById(R.id.user_info_time);
        userinfo.setText(getUserInfo());
        userInfoRollContainer.init();
        // show item
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        BankRollView showItemRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        showItemRollView.init();
        userInfoRollContainer.addShowAble(showItemRollView);
        showItemRollView.setCloseImage(R.drawable.sphere_show_items);
        showItemRollView.setShowImage(R.drawable.show_selector);

        // pin manager
        BankRollView pinManagerRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        pinManagerRollView.init();
        userInfoRollContainer.addShowAble(pinManagerRollView);
        pinManagerRollView.setCloseImage(R.drawable.sphere_pin_manager);
        pinManagerRollView.setShowImage(R.drawable.show_selector);

        // accounts
        BankRollView accountsRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        accountsRollView.init();
        userInfoRollContainer.addShowAble(accountsRollView);
        accountsRollView.setCloseImage(R.drawable.sphere_accounts);
        accountsRollView.setShowImage(R.drawable.show_selector);
        // cards
        BankRollView cardsRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view,
                null);
        cardsRollView.init();
        userInfoRollContainer.addShowAble(cardsRollView);
        cardsRollView.setCloseImage(R.drawable.sphere_cards);
        cardsRollView.setShowImage(R.drawable.show_selector);
        // loans
        BankRollView loansRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view,
                null);
        loansRollView.init();
        userInfoRollContainer.addShowAble(loansRollView);
        loansRollView.setCloseImage(R.drawable.sphere_loans);
        loansRollView.setShowImage(R.drawable.show_selector);
        // investments
        BankRollView investmentsRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        investmentsRollView.init();
        userInfoRollContainer.addShowAble(investmentsRollView);
        investmentsRollView.setCloseImage(R.drawable.sphere_investments);
        investmentsRollView.setShowImage(R.drawable.show_selector);
        // push notification
        BankRollView pushNotificationRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        pushNotificationRollView.init();
        userInfoRollContainer.pushNotificationRollView=pushNotificationRollView;
        userInfoRollContainer.addShowAble(pushNotificationRollView);
        pushNotificationRollView.setCloseImage(R.drawable.sphere_notification);
        pushNotificationRollView.setShowImage(R.drawable.show_selector);

        userInfoLayoutManager = new UserInfoLayoutManager();
        userInfoLayoutManager.setRollContainer(userInfoRollContainer);
    }

    ViewGroup loansLayout;

    BankRollContainer investmentsBankRollContainer;

    ViewGroup investmentLayout;

    ViewGroup accountsRotateLayout, cardsRotateLayout, loansRotateLayout;

    public InvestmentsLayoutManager investmentsLayoutManager;

    private void initInvestments() {

        investmentsBankRollContainer = (BankRollContainer)findViewById(R.id.investments);
        investmentLayout = (ViewGroup)findViewById(R.id.investments_layout);
        investmentsBankRollContainer.init();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        BankRollView depositesRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        depositesRollView.init();
        investmentsBankRollContainer.addShowAble(depositesRollView);
        depositesRollView.setCloseImage(R.drawable.sphere_deposits);
        depositesRollView.setShowImage(R.drawable.btn_investment_show_selector);

        BankRollView assetRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view,
                null);
        assetRollView.init();
        investmentsBankRollContainer.addShowAble(assetRollView);
        assetRollView.setCloseImage(R.drawable.sphere_asset_management);
        assetRollView.setShowImage(R.drawable.btn_investment_show_selector);

        investmentsLayoutManager = new InvestmentsLayoutManager();
        investmentsLayoutManager.setRollContainer(investmentsBankRollContainer);
    }

    public LoansLayout loansLayoutContainer;

    private void initLoansLayout() {
        loansLayout = (ViewGroup)findViewById(R.id.loans);
        loansLayoutContainer = (LoansLayout)findViewById(R.id.loans_layout);
    }

    InvestmentRotateLayoutManager investmentRotateLayoutManager;

    ViewGroup investmentsRotateLayout;

    private void initInvestmentRotateLayout() {
        investmentsRotateLayout = (ViewGroup)findViewById(R.id.investments_rotate_layout);

        ViewGroup accountRotateContainer = (ViewGroup)investmentsRotateLayout
                .findViewById(R.id.rotate_container);
        investmentRotateLayoutManager = new InvestmentRotateLayoutManager();
        investmentRotateLayoutManager.setContainer(accountRotateContainer);
    }

    private void showInvestmentsRotateLayout() {
        showView(investmentsRotateLayout);
        investmentRotateLayoutManager.onShow();
    }

    AccountRotateLayoutManager accountRotateLayoutManager;

    CardsRotateLayoutManager cardsRotateLayoutManager;

    LoansRotateLayoutManager loansRotateLayoutManager;

    private void initRotateLayout() {
        cardsRotateLayout = (ViewGroup)findViewById(R.id.crads_rotate_layout);
        accountsRotateLayout = (ViewGroup)findViewById(R.id.accounts_rotate_layout);
        ViewGroup cardRotateContainer = (ViewGroup)cardsRotateLayout
                .findViewById(R.id.rotate_container);

        ViewGroup accountRotateContainer = (ViewGroup)accountsRotateLayout
                .findViewById(R.id.rotate_container);
        accountRotateLayoutManager = new AccountRotateLayoutManager();
        accountRotateLayoutManager.setContainer(accountRotateContainer);

        cardsRotateLayoutManager = new CardsRotateLayoutManager();
        cardsRotateLayoutManager.setContainer(cardRotateContainer);

        // loans
        loansRotateLayout = (ViewGroup)findViewById(R.id.loans_rotate_layout);
        ViewGroup loansContainer = (ViewGroup)loansRotateLayout.findViewById(R.id.rotate_container);
        loansRotateLayoutManager = new LoansRotateLayoutManager();
        loansRotateLayoutManager.setContainer(loansContainer);

    }

    private void showCardsRotateLayout() {

        showView(cardsRotateLayout);
        cardsRotateLayoutManager.onShow();
    }

    private void showAccountsRotateLayout() {
        showView(accountsRotateLayout);
        accountRotateLayoutManager.onShow();
    }

    private void showLoanssRotateLayout() {
        showView(loansRotateLayout);

        loansRotateLayoutManager.onShow();
    }

    public void showInvestments() {
        showView(investmentLayout);
        investmentsLayoutManager.onShow();
        setCanOrientation(false);
    }

    public void showLoans() {
        showView(loansLayout);
        loansLayoutContainer.onShow();
        setCanOrientation(false);

    }

    private void hideInvestments() {
        hideView(investmentLayout);
    }

    private void hideLoans() {
        hideView(loansLayout);
    }

    private void showView(View v) {
        hideViews();
        if (v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
        }

        resumeAllTabHostImageButton();
        setCanOrientation(true);
        onScreenChange(v);
    }

    private SlidingDrawer slidMenu;

    private View userInfoBtn;

    private ReHeightImageButton menu_accountsBtn;

    private ReHeightImageButton menu_investmentsBtn;

    private ReHeightImageButton menu_cardsBtn;

    private ReHeightImageButton menu_loansBtn;

    private View menu_paymentBtn;

    private View menu_contactsBtn;

    private View menu_guideBtn;

    private View menu_log_outBtn;

    private View menu_synthesisBtn;

    private TextView version;

    private void initMenu() {
        userInfoBtn = findViewById(R.id.menu_user_info);
        userInfoBtn.setOnClickListener(this);

        menu_accountsBtn = (ReHeightImageButton)findViewById(R.id.menu_accounts);
        if (Contants.baseAccounts == null || Contants.baseAccounts.size() == 0) {
            menu_accountsBtn.setImageBitmap(Utils.getBitmap(this, R.drawable.menu_account_disable));
        } else {
            menu_accountsBtn.setOnClickListener(this);
        }

        menu_investmentsBtn = (ReHeightImageButton)findViewById(R.id.menu_investments);
        if (Contants.investmentAccounts == null || Contants.investmentAccounts.size() == 0) {
            menu_investmentsBtn.setImageBitmap(Utils.getBitmap(this,
                    R.drawable.menu_investimenti_disable));
        } else {
            menu_investmentsBtn.setOnClickListener(this);
        }

        menu_cardsBtn = (ReHeightImageButton)findViewById(R.id.menu_cards);
        if (Contants.cardAccounts == null || Contants.cardAccounts.size() == 0) {
            menu_cardsBtn.setImageBitmap(Utils.getBitmap(this, R.drawable.menu_cards_disable));
        } else {
            menu_cardsBtn.setOnClickListener(this);
        }

        menu_loansBtn = (ReHeightImageButton)findViewById(R.id.menu_loans);
        if (Contants.loansAccounts == null || Contants.loansAccounts.size() == 0) {
            menu_loansBtn.setImageBitmap(Utils.getBitmap(this, R.drawable.menu_loans_disable));
        } else {
            menu_loansBtn.setOnClickListener(this);
        }

        menu_paymentBtn = findViewById(R.id.menu_payment);
        menu_paymentBtn.setOnClickListener(this);
        menu_contactsBtn = findViewById(R.id.menu_contacts);
        menu_contactsBtn.setOnClickListener(this);
        menu_guideBtn = findViewById(R.id.menu_guide);
        menu_guideBtn.setOnClickListener(this);
        menu_log_outBtn = findViewById(R.id.menu_log_out);
        menu_log_outBtn.setOnClickListener(this);
        menu_synthesisBtn = findViewById(R.id.menu_synthesis);
        menu_synthesisBtn.setOnClickListener(this);
        // version = (TextView)findViewById(R.id.version);
        // version.setVisibility(View.GONE);
        //
        // version.setText(Contants.URL_NAME + " " +
        // getResources().getString(R.string.version)
        // + Contants.Ver);
        // version.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // Intent intent = new Intent(MainActivity.this,
        // // TestCycleActivity.class);
        // // startActivity(intent);
        // }
        // });

        slidMenu = (SlidingDrawer)findViewById(R.id.slid_menu);
    }

    ImageView investments, accounts, cards, loans;

    private void initTab1() {

        tab1 = (LinearLayout)tabHost.findViewById(R.id.tab1);

        accounts = (ImageView)tab1.findViewById(R.id.splash_accounts);
        if (Contants.baseAccounts == null || Contants.baseAccounts.size() == 0) {

            if (isOffline) {
                accounts.setOnClickListener(this);
            } else {
                accounts.setImageBitmap(Utils.getBitmap(this, R.drawable.btn_account_disable));
            }

        } else {
            accounts.setOnClickListener(this);
        }

        investments = (ImageView)tab1.findViewById(R.id.splash_investments);
        if (Contants.investmentAccounts == null || Contants.investmentAccounts.size() == 0) {
            investments.setImageBitmap(Utils.getBitmap(this, R.drawable.btn_investimenti_disable));
        } else {
            investments.setOnClickListener(this);
        }

        cards = (ImageView)tab1.findViewById(R.id.splash_cards);
        if (Contants.cardAccounts == null || Contants.cardAccounts.size() == 0) {
            cards.setImageBitmap(Utils.getBitmap(this, R.drawable.btn_carte_disable));
        } else {
            cards.setOnClickListener(this);
        }

        loans = (ImageView)tab1.findViewById(R.id.splash_loans);

        if (isOffline) {
            loans.setOnClickListener(this);
        }
        if (Contants.loansAccounts == null || Contants.loansAccounts.size() == 0) {
            loans.setImageBitmap(Utils.getBitmap(this, R.drawable.btn_finanziamenti_disable));
        } else {
            loans.setOnClickListener(this);
        }
    }

    public AccountsLayout accountsLayout;

    private void initTab2() {

        tab2 = (LinearLayout)tabHost.findViewById(R.id.tab2);
        accountsLayout = (AccountsLayout)tab2.findViewById(R.id.accounts_layout);
        if (BaseActivity.isOffline) {
            accountsLayout.addAccount();
            accountsLayout.addAccount();
            accountsLayout.addAccount();
        } else {

        }

    }

    public BankRollView creditRollView;

    public BankRollView prepaidRollView;

    private void initTab3() {
        tab3 = (BankRollContainer)tabHost.findViewById(R.id.tab3);
        tab3.init();
        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        mGaTracker1.sendView("view.cards.synthesis");
        mGaTracker1.sendView("event.creditcards.show");
        mGaTracker1.sendView("event.prepaidcards.show");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        creditRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view, null);
        creditRollView.init();
        tab3.addShowAble(creditRollView);
        creditRollView.setCloseImage(R.drawable.sphere_credit_cards);
        creditRollView.setShowImage(R.drawable.btn_cards_show_selector);

        prepaidRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view, null);
        prepaidRollView.init();
        tab3.addShowAble(prepaidRollView);
        prepaidRollView.setCloseImage(R.drawable.sphere_prepaid_cards);
        prepaidRollView.setShowImage(R.drawable.btn_cards_show_selector);

        cardsLayoutManager = new CardsLayoutManager();
        cardsLayoutManager.setRollContainer(tab3);
    }

    public CardsLayoutManager cardsLayoutManager;

    public PaymentConfirmLayout paymentConfirmLayout;

    public BankSlidContainer bankSlidContainer;

    private void initTab4() {
        tab4 = (BankRollContainer)tabHost.findViewById(R.id.tab4);
        tab4.init();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        BankRollView paymentRoll = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view,
                null);
        paymentRoll.init();
        tab4.addShowAble(paymentRoll);
        paymentConfirmLayout = (PaymentConfirmLayout)layoutInflater.inflate(
                R.layout.payment_confirm, null);
        paymentRoll.setContent(paymentConfirmLayout);
        initPaymentConfirmLayout();

        BankRollView recentPaymentRoll = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        recentPaymentRoll.init();
        tab4.addShowAble(recentPaymentRoll);
        recentPaymentRoll.setCloseImage(R.drawable.sphere_recent_payment);

        RecentPayments recent = (RecentPayments)layoutInflater.inflate(R.layout.recent_payments,
                null);
        recentPaymentRoll.setContent(recent);
        newVersionRecentPaymentListener = new com.accenture.mbank.view.protocol.ShowListener() {

            @Override
            public void onShow(ShowAble showAble) {

                Intent intent = new Intent(MainActivity.this, RecentPaymentActivity.class);
                startActivity(intent);

            }

        };
        if (BaseActivity.isNewVersion) {
            recentPaymentRoll.setShowListener(newVersionRecentPaymentListener);
        }
        newVersionNewPaymentListener = new com.accenture.mbank.view.protocol.ShowListener() {

            @Override
            public void onShow(ShowAble showAble) {
                mGaInstance = GoogleAnalytics.getInstance(getContext());
                mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
                mGaTracker1.sendView("view.newpayment");
                Intent intent = new Intent(MainActivity.this, NewPayments.class);
                startActivity(intent);

            }

        };
        if (BaseActivity.isNewVersion) {
            paymentRoll.setShowListener(newVersionNewPaymentListener);
        }

    }

    /**
     * @deprecated
     */
    @SuppressWarnings("unused")
    private void initTab41() {
        // tab4 = (LinearLayout)tabHost.findViewById(R.id.tab4);

        bankSlidContainer = (BankSlidContainer)tab4.findViewById(R.id.slid_container);
        bankSlidContainer.init();
        bankSlidContainer.setSlid1CloseImage(R.drawable.sphere_new_payment);
        bankSlidContainer.setSlid2CloseImage(R.drawable.sphere_recent_payment);

        LayoutInflater inflater = LayoutInflater.from(this);
        paymentConfirmLayout = (PaymentConfirmLayout)inflater.inflate(R.layout.payment_confirm,
                null);
        bankSlidContainer.setSlid1ContentLayout(paymentConfirmLayout);

        initPaymentConfirmLayout();

        // recent payments
        RecentPayments recent = (RecentPayments)inflater.inflate(R.layout.recent_payments, null);

    }

    private void initPaymentConfirmLayout() {
        ItemExpander typeOfPayment = (ItemExpander)paymentConfirmLayout
                .findViewById(R.id.typeof_payment_step);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        TypeOfPayMentExpandedLayout typeOfPayMentExpandedLayout = (TypeOfPayMentExpandedLayout)layoutInflater
                .inflate(R.layout.new_payment_step_1_expanded_layout, null);

        typeOfPayment.setExpandedContainer(typeOfPayMentExpandedLayout);
        typeOfPayment.setTitle(getResources().getString(R.string.type_of_payment1));

        ItemExpander account = (ItemExpander)paymentConfirmLayout.findViewById(R.id.account_step);
        AccountExpandedLayout accountExpandedLayout = (AccountExpandedLayout)layoutInflater
                .inflate(R.layout.new_payment_step_2_expanded_layout, null);
        account.setExpandedContainer(accountExpandedLayout);
        account.setTitle(getResourceString(R.string.account1));

        ItemExpander date = (ItemExpander)paymentConfirmLayout.findViewById(R.id.date_step);
        DateExpandedLayout dateExpandedLayout = (DateExpandedLayout)layoutInflater.inflate(
                R.layout.new_payment_step_3_expanded_layout, null);
        date.setExpandedContainer(dateExpandedLayout);
        date.setTitle(getResourceString(R.string.date1));

        ItemExpander payee = (ItemExpander)paymentConfirmLayout.findViewById(R.id.payee_step);
        PayeeExpandedLayout payeeExpandedLayout = (PayeeExpandedLayout)layoutInflater.inflate(
                R.layout.new_payment_step_4_expanded_layout, null);
        payee.setExpandedContainer(payeeExpandedLayout);
        payee.setTitle(getResourceString(R.string.payee1));

        ItemExpander amount = (ItemExpander)paymentConfirmLayout.findViewById(R.id.amount_step);
        AmountExpandedLayout amountExpandedLayout = (AmountExpandedLayout)layoutInflater.inflate(
                R.layout.new_payment_step_5_expanded_layout, null);
        amount.setExpandedContainer(amountExpandedLayout);
        amount.setTitle(getResourceString(R.string.amount1));

        ItemExpander description = (ItemExpander)paymentConfirmLayout
                .findViewById(R.id.description_step);
        DescriptionExpandedLayout descriptionExpandedLayout = (DescriptionExpandedLayout)layoutInflater
                .inflate(R.layout.new_payment_step_6_expanded_layout, null);
        description.setExpandedContainer(descriptionExpandedLayout);
        description.setTitle(getResourceString(R.string.description1));

        ItemExpander pin = (ItemExpander)paymentConfirmLayout.findViewById(R.id.pin_step);
        PinExpandedLayout pinExpandedLayout = (PinExpandedLayout)layoutInflater.inflate(
                R.layout.new_payment_step_7_expanded_layout, null);
        pin.setExpandedContainer(pinExpandedLayout);
        pin.setTitle(getResourceString(R.string.pin));
        paymentConfirmLayout.initData();
    }

    @Override
    public void onBackPressed() {
        if (slidMenu.isOpened()) {
            slidMenu.animateClose();
            return;
        }

        DialogManager.createMessageExitDialog("Are you sure you want to exit the application?",
                this).show();
    }

    @Override
    public void onClick(View v) {
        mGaInstance = GoogleAnalytics.getInstance(getContext());
        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        if (v == help) {
            Intent intent = new Intent(MainActivity.this, HelpListActivity.class);
            startActivity(intent);
        } else if (v == userInfoBtn) {
            mGaTracker1.sendView("view.user.info");
            showUserInfo();
            slidMenu.animateClose();
        } else if (v == menu_accountsBtn) {
            showTab(ACCOUNTS);
            slidMenu.animateClose();
        } else if (v == menu_cardsBtn) {
            showTab(CARDS);
            slidMenu.animateClose();
        } else if (v == menu_contactsBtn) {
            mGaTracker1.sendView("view.contact");
            Intent intent = new Intent(MainActivity.this, ContactSearchActivity.class);
            startActivity(intent);
            slidMenu.animateClose();
        } else if (v == menu_guideBtn) {
            mGaTracker1.sendView("view.help");
            help.performClick();
        } else if (v == menu_investmentsBtn) {
            mGaTracker1.sendView("view.investments");
            showInvestments();
            slidMenu.animateClose();
        } else if (v == menu_loansBtn) {
            mGaTracker1.sendView("view.loans");
            showLoans();
            slidMenu.animateClose();
        } else if (v == menu_log_outBtn) {
            mGaTracker1.sendView("event.logout");
            DialogManager.createMessageExitDialog("Are you sure you want to exit the application?",
                    this).show();
        } else if (v == menu_paymentBtn) {
            mGaTracker1.sendView("view.newpayment");
            showTab(PAYMENTS);
            slidMenu.animateClose();
        } else if (v == investments) {
            mGaTracker1.sendView("view.investments");
            investmentRotateLayoutManager.setAccount(Contants.accountsList);
            showInvestmentsRotateLayout();
        } else if (v == cards) {
            showCardsRotateLayout();
        } else if (v == loans) {
            mGaTracker1.sendView("view.loans");
            showLoanssRotateLayout();
        } else if (v == accounts) {
            mGaTracker1.sendView("view.accounts.movement");
            showAccountsRotateLayout();
        } else if (v == menu_synthesisBtn) {
            mGaTracker1.sendView("view.home");
            showTab(SYNTHESIS);
            slidMenu.animateClose();
        }
    }

    public void showTab(int index) {
        if (tabHost.getCurrentTab() == index) {
            if (index == CARDS) {

                cardsLayoutManager.onShow();
            } else if (index == ACCOUNTS) {
                accountsLayout.onShow();
            } else if (index == PAYMENTS) {
                tab4.setVisibility(View.GONE);
                tab4.setVisibility(View.VISIBLE);

            }

        }
        tabHost.setCurrentTab(index);
        hideViews();
        ImageView imageView = null;
        int src = 0;
        View visible = null;
        switch (index) {
            case SYNTHESIS:
                mGaTracker1.sendView("view.home");
                src = R.drawable.label_btn_synthesis_over;
                imageView = imageView1;
                visible = tab1;
                break;
            case ACCOUNTS:
                mGaTracker1.sendView("view.accounts.synthesis");
                src = R.drawable.label_btn_accounts_over;
                imageView = imageView2;
                visible = tab2;
                break;
            case CARDS:

                src = R.drawable.label_btn_cards_over;

                imageView = imageView3;
                visible = tab3;
                break;
            case PAYMENTS:

                imageView = imageView4;
                src = R.drawable.label_btn_payments_over;
                visible = tab4;
                break;

            default:
                break;
        }

        imageView.setImageDrawable(getResources().getDrawable(src));
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setCanOrientation(false);
        onScreenChange(visible);
    }

    public static interface ShowListener {
        /**
         * 当整个界面被显示的时候会触发此方法
         */
        void onShow();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            chartsLayout.setVisibility(View.VISIBLE);
            setChartsLayout();

        } else {
            chartsLayout.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
    }

    public void setChartsLayout() {
        // getVisibleAccount

        if (accountsRotateLayout == currentScreen) {
            accountRotateLayoutManager.chartLayout = this.chartsLayout;
            accountRotateLayoutManager.showChart();

        } else if (cardsRotateLayout == currentScreen) {
            cardsRotateLayoutManager.chartLayout = this.chartsLayout;
            cardsRotateLayoutManager.showChart();

        } else if (investmentsRotateLayout == currentScreen) {
            mGaTracker1.sendView("view.investments.synthesis");
            investmentRotateLayoutManager.chartLayout = this.chartsLayout;
            investmentRotateLayoutManager.showChart();
        } else if (loansRotateLayout == currentScreen) {
            mGaTracker1.sendView("view.loans.synthesis");
            loansRotateLayoutManager.chartLayout = this.chartsLayout;
            loansRotateLayoutManager.showChart();
        }

    }
}
