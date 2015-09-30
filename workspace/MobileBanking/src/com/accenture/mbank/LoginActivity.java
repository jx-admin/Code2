
package com.accenture.mbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.mbank.logic.AdvNewsJson;
import com.accenture.mbank.logic.LoginJson;
import com.accenture.mbank.logic.UserInfoJson;
import com.accenture.mbank.model.AdvNewsResponseModel;
import com.accenture.mbank.model.LoginResponseModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.view.BankImageButton;
import com.accenture.mbank.view.ZoomImageView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.accenture.mbank.view.GoogleAnalya;

public class LoginActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

    private EditText username_input;

    private EditText password_input;

    private ImageButton login_btn;

    private ImageButton safety_btn;

    private ImageButton locate_branch_btn;

    private ImageButton watch_demo_btn;

    private CheckBox remember;

    String postData;

    String httpResult;

    HttpConnector httpConnector;

    ResponsePublicModel responsePublic;

    TextView versionTv;

    private LinearLayout adv;

    private ZoomImageView advImg;

    private TextView advContent;

    private ImageButton help;

    private BankImageButton back;

    View log_top;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        help = (ImageButton)findViewById(R.id.help_btn);
        help.setVisibility(View.GONE);

        back = (BankImageButton)findViewById(R.id.back);
        back.setVisibility(View.GONE);
        initAdvData();

        versionTv = (TextView)findViewById(R.id.version);
        String payment = " payment version:1";
        if (isNewVersion) {
            payment = " payment version:2";
        }
        String version = Contants.URL_NAME + " " + getResources().getString(R.string.version)
                + Contants.Ver + payment;
        versionTv.setText(version);
        username_input = (EditText)findViewById(R.id.username_input);
        password_input = (EditText)findViewById(R.id.password_input);

        if (BaseActivity.initValue) {
            // username_input.setText("mybank");
            username_input.setText("0000");
            password_input.setText("0000");
        }

        login_btn = (ImageButton)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        log_top = findViewById(R.id.log_top);
        log_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                hideKeyboard(log_top);
            }
        });
        safety_btn = (ImageButton)findViewById(R.id.safety_btn);
        safety_btn.setOnClickListener(this);

        locate_branch_btn = (ImageButton)findViewById(R.id.locate_branch_btn);
        locate_branch_btn.setOnClickListener(this);

        watch_demo_btn = (ImageButton)findViewById(R.id.watch_demo_btn);
        watch_demo_btn.setOnClickListener(this);

        remember = (CheckBox)findViewById(R.id.remember_me);
        remember.setOnCheckedChangeListener(this);
        getInfo();
    }

    private void updateADV() {
        adv = (LinearLayout)findViewById(R.id.adv);
        adv.setOnClickListener(this);
        advImg = (ZoomImageView)findViewById(R.id.adv_image);
        advContent = (TextView)findViewById(R.id.adv_content);

        if (Contants.advImageRef != null) {
            advImg.setImageBitmap(Contants.advImageRef);
        }

        if (Contants.advNewsList != null) {
            advContent.setText(Contants.advNewsList.get(0).getTitle());
        } else {
            advContent.setText("Easy mortgage at a fixed rate");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateADV();
    }

    @Override
    public void onClick(View v) {
        if (v == adv) {        	
        	GoogleAnalya.getInstance().getGaInstance(this,"view.news");
            Intent intent = new Intent(LoginActivity.this, Ad_InfoActivity.class);
            startActivityForResult(intent, 0);
        } else if (v == login_btn) {        	
        	GoogleAnalya.getInstance().getGaInstance(this,"view.login"); 
            final String username = username_input.getText().toString();
            final String password = password_input.getText().toString();
            ProgressOverlay progressOverlay = new ProgressOverlay(this);
            if (!BaseActivity.isOffline) {
                progressOverlay.show("", new OnProgressEvent() {
                    @Override
                    public void onProgress() {
                        // 登录
                        postData = LoginJson.LoginReportProtocal(Contants.publicModel, username,
                                password);
                        httpConnector = new HttpConnector();
                        httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                                LoginActivity.this);
                        if (httpResult == null) {
                            return;
                        }
                        LoginResponseModel loginResponse = LoginJson.ParseLoginResponse(httpResult);
                        Contants.publicModel.setCustomerCode(loginResponse.getCustomerCode());
                        Contants.publicModel.setSessionId(loginResponse.getSessionId());

                        if (loginResponse.responsePublicModel != null
                                && loginResponse.responsePublicModel.isSuccess()) {
                            // 用户信息
                            postData = UserInfoJson.UserInfoReportProtocal(Contants.publicModel);
                            httpConnector = new HttpConnector();
                            httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                                    postData, LoginActivity.this);
                            Contants.getUserInfo = UserInfoJson.ParseUserInfoResponse(httpResult);

                            Contants.publicModel.setToken(Contants.getUserInfo.getToken());
                            if (Contants.getUserInfo.responsePublicModel.getResultCode() == 0) {
                                BaseActivity.isLogin = true;
                                Contants.userName = username;
                                if (isChecked) {
                                    saveInfo(Contants.userName, isChecked);
                                } else {
                                    saveInfo("", false);
                                }
                                // 拿到accounts的列表
                                Contants.accountsList = Contants.getUserInfo.getAccountList();
                                Contants.setAccounts();

                                if (BaseActivity.isNewVersion) {
                                    Contants.bankTransferAccounts = NewPaymentDataUtils.geBankTransferAccountst(LoginActivity.this);//getAccountsByService("003");
                                    if (Contants.bankTransferAccounts == null) {
                                        return;
                                    }

                                    Contants.transferEntryAccounts = NewPaymentDataUtils.getTransferEntryAccounts(LoginActivity.this);
                                    if (Contants.transferEntryAccounts == null) {
                                        return;
                                    }
                                    Contants.simTopUpAccounts = NewPaymentDataUtils.getSimTopUpAccounts(LoginActivity.this);
                                    if (Contants.simTopUpAccounts == null) {
                                        return;
                                    }
                                    Contants.chargeAccounts = NewPaymentDataUtils.getChargeAccounts(LoginActivity.this);
                                    if (Contants.chargeAccounts == null) {
                                        return;
                                    }
                                    Contants.getRecipientListModel = NewPaymentDataUtils.getRecipientList(LoginActivity.this);
                                    if (Contants.getRecipientListModel == null) {
                                        return;
                                    }
                                }

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            } else {
                                // TODO 错误提示
                                displayErrorMessage(Contants.getUserInfo.responsePublicModel.eventManagement
                                        .getErrorDescription());
                            }
                        } else {
                            // TODO 这里写错误提示
                            if (loginResponse.responsePublicModel.eventManagement.getErrorCode()
                                    .equals("91503")
                                    || loginResponse.responsePublicModel.eventManagement
                                            .getErrorCode().equals("91501")
                                    || loginResponse.responsePublicModel.eventManagement
                                            .getErrorCode().equals("91502")) {
                                displayErrorMessage(loginResponse.responsePublicModel.eventManagement
                                        .getErrorDescription());
                                myHandler.sendEmptyMessage(CLEAR_USERNAME_PASSWORD);
                            } else {
                                displayErrorMessage(loginResponse.responsePublicModel.eventManagement
                                        .getErrorDescription());
                            }
                        }
                    }
                });
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (v == safety_btn) {
        	GoogleAnalya.getInstance().getGaInstance(this,"event.safety");
            Intent intent = new Intent(LoginActivity.this, SecurityInfoActivity.class);
            startActivity(intent);
        } else if (v == locate_branch_btn) {
            Intent intent = new Intent(LoginActivity.this, ContactSearchActivity.class);
            startActivity(intent);
        } else if (v == watch_demo_btn) {
        	GoogleAnalya.getInstance().getGaInstance(this,"event.watchdemo");
            String url = "http://85.33.32.132:6002/BPER/demo_bper_cell_2.3gp";
            // String testUrl = "http://commonsware.com/misc/test2.3gp";
            setPlayerUrl(url);
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

    // 实例化一个handler
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR_MESSAGE:
                    if (responsePublic != null) {
                        DialogManager.createMessageDialog(
                                responsePublic.eventManagement.getErrorDescription(),
                                LoginActivity.this).show();
                    }
                    break;
                case CLEAR_USERNAME_PASSWORD:
                    username_input.setText("");
                    password_input.setText("");
                    remember.setChecked(false);
                    saveInfo("", false);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    boolean isChecked = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == remember) {
            if (buttonView.isChecked()) {
                this.isChecked = true;
            } else {
                this.isChecked = false;
            }
        }
    }

    private static final String MBANKING = "mbanking";

    private static final String USERNAME = "username";

    private static final String ISCHECKED = "ISCHECKED";

    /**
     * 获取
     */
    private void getInfo() {
        SharedPreferences settings = getSharedPreferences(MBANKING, 0); // 首先获取一个
        String username = settings.getString(USERNAME, "");
        boolean ischeck = settings.getBoolean(ISCHECKED, false);
        if (ischeck) {
            username_input.setText(username);
            remember.setChecked(ischeck);
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


    /**
     * 加载广告数据
     */
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

    // 实例化一个handler
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADV_NEWS:
                    updateADV();
                    break;
            }
        }
    };
}
