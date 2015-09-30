
package com.act.mbanking.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.mbanking.App;
import com.act.mbanking.BaseActivity;
import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.R.string;
import com.act.mbanking.bean.PushNotificationValue;
import com.act.mbanking.bean.SettingModel;
import com.act.mbanking.database.BankSqliteHelper;
import com.act.mbanking.manager.MainManager;
import com.act.mbanking.manager.MainSubManager;
import com.act.mbanking.manager.view.BankMenu;
import com.act.mbanking.manager.view.BankMenu.OnMenuClick;
import com.act.mbanking.utils.DialogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.view.ChartView;
import com.act.mbanking.view.WorkSpace;
import com.act.mbanking.view.WorkSpace.WorkSpaceListener;
import com.custom.view.RectLD;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class MainActivity extends NavigationActivity implements WorkSpaceListener, OnClickListener,
        OnMenuClick {
	public static boolean onResume=false;

    WorkSpace workSpace;

    public static final float menu_width = 5f / 6;

    public BankMenu menu;

    View menu_synthesis;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;
    public static Context getContext() {
        return context;
    }

    // MainLayout mainLayout;

    public static MainManager mainManager;
    static Context context;
    ProgressDialog downloading_pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_space);
        setRightNavigationImg(R.drawable.menu_icon);
        initSettingModel();
        workSpace = (WorkSpace)findViewById(R.id.work_space);
        workSpace.setWorkSpaceListener(this);
        menu = (BankMenu)findViewById(R.id.menu);
        menu.getLayoutParams().width = (int)(screen_width * menu_width);
        menu.init();
        menu.setOnMenuClick(this);
        mainManager = new MainSubManager(this);
        ViewGroup chart_layout = (ViewGroup)findViewById(R.id.chart_layout);
        mainManager.chartLayout = chart_layout;
        downloading_pd = new ProgressDialog(this);
        downloading_pd.setTitle(R.string.waiting);
        setNavigationStatus();
    }

    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		     mainManager.setChartData();
			downloading_pd.dismiss();
		}
    	
    };

    @Override
    protected void onLeftNavigationClick(View v) {
        super.onLeftNavigationClick(v);
        mainManager.dispatchLeftNavigationClick(v);
    }

    @Override
    protected void onRightNavigationClick(View v) {
        super.onRightNavigationClick(v);
        setCanOrientation(false);
        if (workSpace.getCurrrentScreenIndexByRight() == 1) {

            workSpace.snapToPre();

        } else if (workSpace.getCurrrentScreenIndexByRight() == 0) {

            setCanOrientation(false);
            workSpace.snapToNext();
        }
    }

    @Override
    public void onSnapOver() {

        boolean canOrientation = false;
        if (workSpace.getCurrrentScreenIndexByRight() == 0) {
            canOrientation = true;
        }
        canOrientation = canOrientation && this.canOrientation;
        setCanOrientation(canOrientation);
    }

    public boolean canOrientation;

    @Override
    public void onClick(View v) {

        if (v == menu_synthesis) {
            workSpace.snapToPre();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        showChart(newConfig);
    }

    // hongjiao starts
    public static SettingModel setting;

    private void initSettingModel() {
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");

            if (username != null) {
                BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance(this);
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
            BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance(this);
            setting = (SettingModel)bankSqliteHelper.findBykey(SettingModel.TABLE_NAME,
                    SettingModel.COUM_USER_ID, "offline");

            if (setting == null) {
                this.setting = new SettingModel();
                setting.setUserId("offline");
                bankSqliteHelper.insert(setting);
            }
        }
    }

    // hongjiaoends
    @Override
    protected void onStart() {
        super.onStart();
        if (Contants.getUserInfo == null) {
            // 这段代码先加上.目前峥峥的手机切入后台,长时间后可能会崩溃 有可能是该变量被释放了.所以在这儿加一个判断
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        if (App.app.msgTitle != null) {
            notificationMessageDialog(App.app.msgContext);
        }

        showChart(MainActivity.this.getResources().getConfiguration());
    }
    
    private void showChart(final Configuration newConfig){
    	if(mainManager.onOrientationChange(newConfig)){
    		return ;
    	}
        downloading_pd.show();
//    	handler.post(
        new Thread(){
    		@Override
    		public void run() {
    			mainManager.onLoadChartData(newConfig);
//    			downloading_pd.dismiss();
    			handler.sendEmptyMessage(-1);
    		}}.start();
    }
    
    public void onStop(){
    	super.onStop();
    }
    
    private void notificationMessageDialog(String message) {
        final Dialog alertDialog;
        alertDialog = new Dialog(this, R.style.selectorDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);
        alertDialog.setContentView(linearLahyout);
        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        alertDialog.show();
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                App.app.msgTitle=null;
            }
        });
    }

    @Override
    public void onMenuClick(int id) {
    	mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        switch (id) {

            case R.id.menu_item_aggregated_view:
            	mGaTracker1.sendView("view.aggregated"); 
                mainManager.showAggregatedView(false, null);
                break;
            case R.id.menu_item_accounts:
            	mGaTracker1.sendView("view.account"); 
                mainManager.showAccounts(false, null);
                break;
            case R.id.menu_item_investments:
            	mGaTracker1.sendView("view.investments"); 
                mainManager.showInvestments(false, null);
                break;
            case R.id.menu_item_cards:
                mainManager.showCards(false, null);
                break;
            case R.id.menu_item_loans:
                mainManager.showLoans(false, null);
                break;
            case R.id.menu_item_new_payment:         	
                mainManager.showNewPayment(false, null);
                break;
            case R.id.menu_item_recent_payments:
            	mGaTracker1.sendView("view.recent.payment"); 
                mainManager.showRecentPayments(false, null);
                break;
            case R.id.menu_item_guide:
            	mGaTracker1.sendView("view.helpguide");
                mainManager.showGuide(false, null);
                break;
            case R.id.menu_item_user_info:
            	mGaTracker1.sendView("view.userinfo"); 
                mainManager.showUserInfo(false, null);
                break;
            case R.id.menu_item_contacts: {
            	mGaTracker1.sendView("view.contact"); 
                mainManager.showContact(false, null);
                break;
            }
            case R.id.close_sesssion_btn:
            	mGaTracker1.sendView("event.logout"); 
                onBackPressed();
                return;
            default:
                break;
        }
        workSpace.forceSnapToMain();

    }

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		onResume=false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onResume=true;
	}

	@Override
    public void onBackPressed() {

        DialogManager.createMessageExitDialog(this.getString(R.string.sure_exit_msg),
                this).show();
        mainManager.onDestroyed();
    }
   

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainManager.onActivityResult(requestCode, resultCode, data);
    }
}
