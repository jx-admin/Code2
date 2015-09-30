
package com.act.mbanking.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.act.mbanking.AggregatedAccountType;
import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.NewPaymentDataUtils;
import com.act.mbanking.R;
import com.act.mbanking.SerializeUserInfo;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AdvNewsResponseModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.bean.GetUserInfoResponseModel;
import com.act.mbanking.bean.LoginResponseModel;
import com.act.mbanking.bean.PushNotificationValue;
import com.act.mbanking.bean.SynchChartModel;
import com.act.mbanking.bean.SynchDashBoardModel;
import com.act.mbanking.logic.AdvNewsJson;
import com.act.mbanking.logic.LoginJson;
import com.act.mbanking.logic.SynchChartJson;
import com.act.mbanking.logic.SynchDashboardJson;
import com.act.mbanking.logic.UserInfoJson;
import com.act.mbanking.manager.DeviceAdminManager;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.KeyBoardUtils;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.AutoScrollTextView;
import com.act.mbanking.view.SimpleImageButton;
import com.act.mbanking.view.ZoomImageView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class LoginActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
    private static final String MBANKING = "mbanking";

    private PushNotificationValue pushNotificationValue;

    /**
     * 保存用户名所使用的KEY
     */
    private static final String USERNAME = "username";

    private static final String ISCHECKED = "ISCHECKED";

    private LinearLayout adv;

    private ZoomImageView advImg;

    private AutoScrollTextView advContent;

    private EditText userName;

    private EditText password;

    private Button login;

    private CheckBox remember_me;

    private TextView fogotPassword;

    private Button safety_btn;

    private Button locate_btn;

    private Button demo_btn;

    private boolean isChecked = false;

    LinearLayout login_center;

    LinearLayout login_bottom;

    FrameLayout center;

    LinearLayout login_progress;

    ProgressBar login_progressbar;

    TextView progress_detail;

    DeviceAdminManager devAdminManager;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        userName = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        password.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				KeyBoardUtils.hideSoftInputFromWindow(LoginActivity.this,password.getWindowToken());
				return true;
			}
		});
        login = (Button)findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        remember_me = (CheckBox)findViewById(R.id.remember_me);
        remember_me.setOnCheckedChangeListener(this);

        fogotPassword = (TextView)findViewById(R.id.forgot_your_password);
        fogotPassword.setText(Html.fromHtml("<u>"
                + getResources().getString(R.string.forgot_your_password) + "</u>"));
        fogotPassword.setOnClickListener(this);
        fogotPassword.setVisibility(View.INVISIBLE); // 暂时先隐藏起来..
                                                     // 目前说是这个forgot没有了

        safety_btn = (Button)findViewById(R.id.safety_btn);
        safety_btn.setOnClickListener(this);
        locate_btn = (Button)findViewById(R.id.locate_btn);
        locate_btn.setOnClickListener(this);
        demo_btn = (Button)findViewById(R.id.demo_btn);
        demo_btn.setOnClickListener(this);

        center = (FrameLayout)findViewById(R.id.center);
        login_center = (LinearLayout)findViewById(R.id.login_center);
        login_bottom = (LinearLayout)findViewById(R.id.login_bottom);
        // login_center.setVisibility(View.GONE);
        login_progress = (LinearLayout)findViewById(R.id.login_progress);
        login_progressbar = (ProgressBar)findViewById(R.id.login_progressbar);
        login_progress.setVisibility(View.GONE);

        progress_detail = (TextView)findViewById(R.id.load_detail_tv);
        progress_detail.setText("");
        getInfo();
        initAdvData();

//        String ss=Settings.Secure.getString(LoginActivity.this.getContentResolver(),Settings.Secure.DEFAULT_INPUT_METHOD);
//        InputMethodService input= new InputMethodService(); com.sohu.inputmethod.sogou/.SogouIME
//        input.switchInputMethod("com.android.inputmethod.pinyin/.PinyinIME");
//        Settings.Secure.putString(LoginActivity.this.getContentResolver(),Settings.Secure.DEFAULT_INPUT_METHOD,"com.android.inputmethod.pinyin/.PinyinIME"); 
        
//        LogManager.d("ss" + ss);
        
        devAdminManager = new DeviceAdminManager(this);
        if (App.app.initValue) {
            userName.setText("0000");
            password.setText("0000");
            login.performClick();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pushNotificationValue = (PushNotificationValue)getIntent().getSerializableExtra(Contants.PAYLOAD);
        
        updateADV();
    }

    @Override
    public void onClick(View view) {
    	mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        if (view == adv) {
        	 mGaTracker1.sendView("view.news"); 
            Intent intent = new Intent(LoginActivity.this, Ad_InfoActivity.class);
            startActivityForResult(intent, 0);
        } else if (view == login) {
        	 mGaTracker1.sendView("view.login"); 
            if (devAdminManager.isActivity()) {
                devAdminManager.checkPasswordQuality();
                if (devAdminManager.isActivePasswordSufficient()) {
                    Log.d(this.getClass().getSimpleName(),
                            "devAdminManager.isActivePasswordSufficient() "
                                    + devAdminManager.isActivePasswordSufficient());
                } else {
                    devAdminManager.openSetPsw(this);
                    return;
                }
            } else if (!App.app.initValue) {
                devAdminManager.enable(this);
                return;
            }
            
            KeyBoardUtils.hideSoftInputFromWindow(this, login.getWindowToken());
            final String username = userName.getText().toString();
            final String pwd = password.getText().toString();
            center.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0, 9.0f));
            login_center.setVisibility(View.GONE);
            login_progress.setVisibility(View.VISIBLE);

            final ProgressThread progressThread = new ProgressThread();
            login_progressbar.setProgress(0);
            progressThread.running = true;
            progressThread.progressBar = this.login_progressbar;

            progressThread.intent = new Intent(LoginActivity.this, MainActivity.class);
            progressThread.intent.putExtra("username", username);
            if(pushNotificationValue != null){
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Contants.PAYLOAD, pushNotificationValue);
                progressThread.intent.putExtras(mBundle);
            }
            progressThread.baseActivity = this;

            progressThread.start();
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    progressThread.setProgress(10);

                    progressThread.setProgress(15);
                    updateProgressDetail(getString(R.string.ss_validate_password_));

                    // 登录
                    String postData = LoginJson.LoginReportProtocal(Contants.publicModel, username,
                            pwd);
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, LoginActivity.this);
                    LoginResponseModel loginResponse = LoginJson.ParseLoginResponse(httpResult);

                    if (loginResponse == null || loginResponse.responsePublicModel == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    } else if (!loginResponse.responsePublicModel.isSuccess()) {
                        loginFailed(true, loginResponse.responsePublicModel.eventManagement
                                .getErrorDescription());
                        progressThread.running = false;
                        return;
                    }

                    App.app.setUserName(username);
                    App.app.setPassword(pwd);
                    Contants.publicModel.setCustomerCode(loginResponse.getCustomerCode());
                    Contants.publicModel.setSessionId(loginResponse.getSessionId());

                    // 用户信息

                    progressThread.setProgress(20);

                    updateProgressDetail(getString(R.string.ss_restore_user_info_));

                    updateProgressDetail(getString(R.string.ss_ownloading_user_info_));
                    postData = UserInfoJson.UserInfoReportProtocal(Contants.publicModel);
                    httpConnector = new HttpConnector();
                    httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                            LoginActivity.this);
                    if (httpResult == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }
                    progressThread.setProgress(25);
                    Contants.getUserInfo = UserInfoJson.ParseUserInfoResponse(httpResult);
                    Contants.publicModel.setToken(Contants.getUserInfo.getToken());
                    if (Contants.getUserInfo.responsePublicModel.getResultCode() == 0) {
                        saveInfo(App.app.getUserName(), isChecked);
                        // 拿到accounts的列表
                        Contants.accountsList = Contants.getUserInfo.getAccountList();
                        Contants.setAccounts();
                    } else {
                        loginFailed(true, Contants.getUserInfo.responsePublicModel.eventManagement
                                .getErrorDescription());
                        progressThread.running = false;
                        return;
                    }


                	//
                	updateProgressDetail(getString(R.string.ss_downloading_accounts_));
                	progressThread.setProgress(30);
                  App.app.bankTransferAccounts =NewPaymentDataUtils.geBankTransferAccountst(LoginActivity.this);
                  if (App.app.bankTransferAccounts == null) {
              		loginFailed(true);
              		progressThread.running = false;
              		return;
              	}
                      progressThread.setProgress(35);
              App.app.transferEntryAccounts = NewPaymentDataUtils.getTransferEntryAccounts(LoginActivity.this);
              if (App.app.transferEntryAccounts == null) {
                  loginFailed(true);
                  progressThread.running = false;
                  return;
              } 

            progressThread.setProgress(40);
            App.app.simTopUpAccounts = NewPaymentDataUtils.getSimTopUpAccounts(LoginActivity.this);
            if (App.app.simTopUpAccounts == null) {
                loginFailed(true);
                progressThread.running = false;
                return;
            }
                progressThread.setProgress(45);
          App.app.chargeAccounts = NewPaymentDataUtils.getChargeAccounts(LoginActivity.this);
          if (App.app.chargeAccounts == null) {
              loginFailed(true);
              progressThread.running = false;
              return;
          }
      	
      	updateProgressDetail(getString(R.string.ss_ownload_recipientList_));
      	progressThread.setProgress(50);
      	App.app.getRecipientListModel = NewPaymentDataUtils.getRecipientList(LoginActivity.this);
      	if (App.app.getRecipientListModel == null) {
      		loginFailed(true);
      		progressThread.running = false;
      		return;
      	}
                    // 保存序列化的文件名
                    String fileName = username + Contants.SAVENAME + Contants.URL_NAME;
                    String password1 = App.app.getUserName() + App.app.getPassword();
                    Contants.restoreUserInfo(fileName, password1);
                    String lastUpdate = "";
                    if (SerializeUserInfo.getInstance().getLocalUserInfo() != null) {
                        lastUpdate = SerializeUserInfo.getInstance().getLocalUserInfo()
                                .getLastUpdate();
                    }

                    if (lastUpdate.equals("")) {
                        Contants.isNotLastUpdate = true;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_cards_synchchart_));
                    progressThread.setProgress(55);
                    SynchChartModel cardSynchChartModel = synchCharts(AggregatedAccountType.CARDS,
                            lastUpdate);
                    if (cardSynchChartModel == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ?
                    updateProgressDetail(getString(R.string.ss_download_account_synchchart_));
                    progressThread.setProgress(60);
                    SynchChartModel accountSynchChartModel = synchCharts(
                            AggregatedAccountType.ACCOUNTS, lastUpdate);
                    if (accountSynchChartModel == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_investments_synchchart_));
                    progressThread.setProgress(65);
                    SynchChartModel investmentsSynchChartModel = synchCharts(
                            AggregatedAccountType.INVESTMENT, lastUpdate);
                    if (investmentsSynchChartModel == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_financing_synchchart_));
                    progressThread.setProgress(75);
                    SynchChartModel financingSynchChartModel = synchCharts(
                            AggregatedAccountType.FINANCING, lastUpdate);
                    if (financingSynchChartModel == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_cards_synchdashboard_));
                    progressThread.setProgress(75);
                    SynchDashBoardModel cardSynchDashBoard = synchDashBoard(
                            AggregatedAccountType.CARDS, lastUpdate);
                    if (cardSynchDashBoard == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_account_synchdashboard_));
                    progressThread.setProgress(80);
                    SynchDashBoardModel accountSynchDashBoard = synchDashBoard(
                            AggregatedAccountType.ACCOUNTS, lastUpdate);
                    if (accountSynchDashBoard == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_investments_synchdashboard_));
                    progressThread.setProgress(85);
                    SynchDashBoardModel investmentsSynchDashBoard = synchDashBoard(
                            AggregatedAccountType.INVESTMENT, lastUpdate);
                    if (investmentsSynchDashBoard == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    // ??
                    updateProgressDetail(getString(R.string.ss_download_investments_synchdashboard_));
                    progressThread.setProgress(90);
                    SynchDashBoardModel financingSynchDashBoard = synchDashBoard(
                            AggregatedAccountType.FINANCING, lastUpdate);
                    if (financingSynchDashBoard == null) {
                        loginFailed(true);
                        progressThread.running = false;
                        return;
                    }

                    updateProgressDetail(getString(R.string.ss_save_local_database_));
                    progressThread.setProgress(95);
                    lastUpdate = TimeUtil.getDateString(System.currentTimeMillis(),
                            TimeUtil.dateFormat2);
                    Contants.getUserInfo.setLastUpdate(lastUpdate);

                    // 取出本地序列化读取出来的数据 (临时变量)
                    GetUserInfoResponseModel localUserInfo = SerializeUserInfo.getInstance()
                            .getLocalUserInfo();
                    // 判断序列化中是否有数据
                    if (localUserInfo != null) {
                        // 首先循环更新的account数据列表
                        for (int i = 0; i < Contants.getUserInfo.getAccountList().size(); i++) {
                            // 然后取出更新后的account
                            AccountsModel newAccountModel = Contants.getUserInfo.getAccountList()
                                    .get(i);
                            if (newAccountModel == null) {
                                continue;
                            }
                            // 取出序列化本地的旧的account数据列表
                            for (int j = 0; j < localUserInfo.getAccountList().size(); j++) {
                                AccountsModel oldAccountModel = localUserInfo.getAccountList().get(
                                        j);
                                if (oldAccountModel == null) {
                                    continue;
                                }
                                // 判断新的accountCode与旧的accountCode是否一至
                                if (newAccountModel.getAccountCode().equals(
                                        oldAccountModel.getAccountCode())) {
                                    // 如果一至取出account中的dashBoardAggregatedAccountList,并循环出该list下面的
                                    for (int o = 0; o < newAccountModel
                                            .getDashboardAggregatedAccountsList().size(); o++) {
                                        // 拿到新/老dashboardAggregatedAccountsList
                                        List<AggregatedAccount> newDashBoardDataList = newAccountModel.dashboardAggregatedAccountsList;
                                        List<AggregatedAccount> oldDashBoardDataList = oldAccountModel.dashboardAggregatedAccountsList;
                                        // 判断新的newDashBoardDataList 不为空
                                        if (newDashBoardDataList != null
                                                && newDashBoardDataList.size() != 0) {
                                            // 判断newDashBoardDataList
                                            // 下面的getDashboardDataList
                                            // 不为空,添加到本地的旧的数据列表的临时变量中
                                            if (newDashBoardDataList.get(o).getDashboardDataList() != null
                                                    && newDashBoardDataList.get(o)
                                                            .getDashboardDataList().size() != 0) {
                                                oldDashBoardDataList
                                                        .get(o)
                                                        .getDashboardDataList()
                                                        .addAll(newDashBoardDataList.get(o)
                                                                .getDashboardDataList());
                                            }
                                            // 判断newDashBoardDataList
                                            // 下面的getCharts
                                            // 不为空,添加到本地的旧的数据列表的临时变量中
                                            if (newDashBoardDataList.get(o).getCharts() != null
                                                    && newDashBoardDataList.get(o).getCharts()
                                                            .size() != 0) {
                                                if (oldDashBoardDataList.get(o).getCharts() == null) {
                                                    oldDashBoardDataList.get(o).setCharts(
                                                            new ArrayList<ChartModel>());
                                                }
                                                oldDashBoardDataList
                                                        .get(o)
                                                        .getCharts()
                                                        .addAll(newDashBoardDataList.get(o)
                                                                .getCharts());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // 将临时变量添加到序列化的对象中
                        SerializeUserInfo.getInstance().setLocalUserInfo(localUserInfo);
                    } else {
                        SerializeUserInfo.getInstance().setLocalUserInfo(Contants.getUserInfo);
                    }
                    // 进行序列化存储
                    Contants.saveGetUserInfo(fileName, SerializeUserInfo.getInstance()
                            .getLocalUserInfo(), password1);
                    // 保存成功后将序列化对象赋值给getUserInfo(因为后面的数据都是从这个Contants.getUserInfo中拿到的)
                    Contants.getUserInfo = SerializeUserInfo.getInstance().getLocalUserInfo();
                    LogManager.d("over");
                    progressThread.setProgress(99);
                    progressThread.setProgress(100);
                }
            });
            thread.start();

        } else if (view == safety_btn) {
        	
            mGaTracker1.sendView("event.safety");           
            startActivity(new Intent(LoginActivity.this, SecurityInfoActivity.class));
        } else if (view == locate_btn) {
            startActivity(new Intent(LoginActivity.this, ContactActivity.class));
        } else if (view == demo_btn) {
        	 mGaTracker1.sendView("event.watchdemo");  
            String url = "http://85.33.32.132:6002/BPER/demo_bper_cell_2.3gp";
            setPlayerUrl(url);
        } else if (view == fogotPassword) {

        }
    }

    private SynchDashBoardModel synchDashBoard(int aggregatedAccountType, String lastUpdate) {
        String postData = SynchDashboardJson.synchDashboardReportProtocal(Contants.publicModel,
                aggregatedAccountType, lastUpdate);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
        SynchDashBoardModel syncDashBoard = SynchDashboardJson
                .parseSynchDashBoardResponse(httpResult);

        if (syncDashBoard == null) {
            LogManager.d("syncDashBoard = null" + postData);
            return null;
        }
        return syncDashBoard;
    }

    private void updateADV() {
        adv = (LinearLayout)findViewById(R.id.adv);
        adv.setOnClickListener(this);
        advImg = (ZoomImageView)findViewById(R.id.adv_image);
        advContent = (AutoScrollTextView)findViewById(R.id.adv_content);

        if (Contants.advImageRef != null) {
            advImg.setImageBitmap(Contants.advImageRef);
        }

        if(Contants.advNewsList == null || Contants.advNewsList.size() == 0){
            
        }else{
            if(Contants.advNewsList.get(0).getTitle() !=null){
                advContent.setText(Contants.advNewsList.get(0).getTitle());
            }else{
                advContent.setText("");
            }
            advContent.setSpeed(1.5f);
            advContent.init(advContent.getWidth() * 2);// width通常就是屏幕宽！
            advContent.startScroll();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADV_NEWS:
                    updateADV();
                    break;
            }
        }
    };
    
    private SynchChartModel synchCharts(int aggregatedAccountType, String lastUpdate) {
        String postData = SynchChartJson.synchChartReportProtocal(Contants.publicModel,
                aggregatedAccountType, lastUpdate);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
        SynchChartModel synchChart = SynchChartJson.parseSynchChartResponse(httpResult);
        if (synchChart == null) {
            LogManager.d("synchCharts = null" + postData);
            return null;
        }
        return synchChart;
    }

    public void loginFailed(boolean needshowLoginInput, String msg) {
        if (msg == null) {
            msg = getString(R.string.login_error);
        }
        displayErrorMessage(msg);
        updateLoginUI(needshowLoginInput);
    }

    public void loginFailed(boolean needshowLoginInput) {
        loginFailed(needshowLoginInput, null);
    }

    public void updateLoginUI(final boolean loginInput) {
        baseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loginInput) {
                    login_center.setVisibility(View.VISIBLE);
                    login_progress.setVisibility(View.GONE);
                    login_bottom.setVisibility(View.VISIBLE);
                    center.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                            0, 7.0f));
                    userName.setText("");
                    password.setText("");
                    remember_me.setChecked(false);
                } else {
                    login_center.setVisibility(View.GONE);
                    login_progress.setVisibility(View.VISIBLE);
                    login_bottom.setVisibility(View.GONE);
                }
            }
        });
    }

    public void updateProgressDetail(final String msg) {

        baseHandler.post(new Runnable() {

            @Override
            public void run() {

                progress_detail.setText(msg);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == remember_me) {
            if (buttonView.isChecked()) {
                this.isChecked = true;
            }
        }
    }

    /**
     * 获取
     */
    private void getInfo() {
        SharedPreferences settings = getSharedPreferences(MBANKING, 0); // 首先获取一个
        String username = settings.getString(USERNAME, "");
        boolean ischeck = settings.getBoolean(ISCHECKED, false);
        if (ischeck) {
            userName.setText(username);
            remember_me.setChecked(ischeck);
            isChecked = ischeck;
        }
    }

    /**
     * 保存用户名
     * 
     * @param username
     */
    private void saveInfo(String username, boolean ischeck) {
        SharedPreferences settings = getSharedPreferences(MBANKING, 0); // 首先获取一个
        Editor edit = settings.edit();
        edit.putString(USERNAME, username);
        edit.putBoolean(ISCHECKED, ischeck);
        edit.commit();
    }

    private void initAdvData() {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.runBackground(new OnProgressEvent() {
            @Override
            public void onProgress() {
                // 广告新闻
                String postData = AdvNewsJson.AdvNewsReportProtocal(Contants.publicModel,
                        Contants.abi);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        LoginActivity.this);
                final AdvNewsResponseModel advNewResponse = AdvNewsJson
                        .ParseAdvNewsResponse(httpResult);
                if (advNewResponse != null && advNewResponse.responsePublicModel.isSuccess()) {
                    // 判断如果加载成功 跳转界面
                    Contants.advNewsList = advNewResponse.getListAdvNews();
                    handler.sendEmptyMessage(ADV_NEWS);
                    loadAdvImage();
                    handler.sendEmptyMessage(ADV_NEWS);
                } else if (advNewResponse != null
                        && !advNewResponse.responsePublicModel.isSuccess()) {
                    displayErrorMessage(advNewResponse.responsePublicModel.eventManagement
                            .getErrorDescription());
                } else {
                    Contants.advNewsList = advNewResponse.getListAdvNews();
                }
            }
        });
    }

    public void loadAdvImage() {
        HttpConnector httpConnector = new HttpConnector();
        try {
            Bitmap imageRef = BitmapFactory.decodeStream(httpConnector
                    .getImageStream(Contants.advNewsList.get(0).getImageRef()));
            if (imageRef != null) {
                Contants.advImageRef = imageRef;

            } else {

            }
            Bitmap imageRefThumb = BitmapFactory.decodeStream(httpConnector
                    .getImageStream(Contants.advNewsList.get(0).getImageRefThumb()));
            if (imageRef != null) {
                Contants.advImageRefThumb = imageRefThumb;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ProgressThread extends Thread {

        int sleep = 150;

        boolean running;

        ProgressBar progressBar;

        int lastProgress = 0;;

        int maxProgress = 0;

        long newMaxTime = 0;

        long lastTime = 0;

        BaseActivity baseActivity;

        Intent intent;

        /**
         * @param progress the max value of Progress
         */
        public void setProgress(int progress) {
            lastProgress = maxProgress;
            maxProgress = progress;
            lastTime = newMaxTime;
            newMaxTime = System.currentTimeMillis();
        }

        @Override
        public void run() {

            while (running) {

                int current = progressBar.getProgress();
                LogManager.d("lastProgress" + lastProgress + "current" + current + "max"
                        + maxProgress);
                if (current == 100) {

                    running = false;
                    App.app.isFrist = true;
                    baseActivity.startActivity(intent);
                    baseActivity.finish();
                }
                if (current < lastProgress) {
                    current = current + 1;
                    progressBar.setProgress(current);

                    // LogManager.d("sleep" + "current < lastProgress");
                    sleep = 50;

                } else if (current >= lastProgress && current < maxProgress) {
                    current = current + 1;
                    progressBar.setProgress(current);

                    sleep = 300;
                    // LogManager.d("sleep" +
                    // "current > lastProgress && current < maxProgress");
                }
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 调用播放器
     * 
     * @param url
     */
    public void setPlayerUrl(String url) {
        int a = url.lastIndexOf(".");
        String b = url.substring(a, url.length());
        if (b.toLowerCase().equals(".mp4") || b.toLowerCase().equals(".3gp")) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setType("video/*");
            intent.putExtra("Caller", "OTHER");
            intent.setDataAndType(uri, "video/*");
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "视频链接已损坏！", Toast.LENGTH_SHORT).show();
        }
    }
}
