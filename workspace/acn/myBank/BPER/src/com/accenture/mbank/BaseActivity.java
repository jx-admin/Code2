package com.accenture.mbank;

import java.util.List;

import it.gruppobper.ams.android.bper.R;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.GPSListener;
import com.accenture.mbank.view.ReHeightImageButton;
import com.google.android.maps.MapActivity;

public class BaseActivity extends FragmentActivity implements OnClickListener {

	protected View back;

	/**
	 * flag to indicate the state of current user, initialized to be false it
	 * will be changed to true after user logined successfully
	 */
	public static boolean isLogin = false;

	public static boolean isOffline = true;

	public static boolean initValue = false;

	public static final int ADV_NEWS = 0x00;

	public static final int ERROR_MESSAGE = 0x01;

	public static final int CLEAR_USERNAME_PASSWORD = 0x02;

	public static final int NEW_VERSION = 0x03;

	public static final int MSG_DASHBOARD_ANIMATION = 0x04;

	public Handler baseHandler;

	public static int screen_width = 0;

	public static int screen_height = 0;
	
	private LocationManager locationManager;
	private GPSListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LogManager.d(this.getClass() + "oncreate");
		super.onCreate(savedInstanceState);
		baseHandler = new Handler();
		WindowManager windowManager = getWindowManager();

		screen_width = Math.min(windowManager.getDefaultDisplay().getWidth(),
				windowManager.getDefaultDisplay().getHeight());
		screen_height = Math.max(windowManager.getDefaultDisplay().getWidth(),
				windowManager.getDefaultDisplay().getHeight());
		
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		this.listener = new GPSListener(locationManager);
	}

	public void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initMenu();
		back = (View) findViewById(R.id.back);
		if (back != null) {
			back.setOnClickListener(this);
		}

		help = (ImageButton) findViewById(R.id.help_btn);
		if (help != null)
			help.setOnClickListener(this);
	}

	public SlidingDrawer slidMenu;

	public View userInfoBtn;

	public ReHeightImageButton menu_accountsBtn;

	public ReHeightImageButton menu_investmentsBtn;

	public ReHeightImageButton menu_cardsBtn;

	public ReHeightImageButton menu_loansBtn;

	public ReHeightImageButton menu_paymentBtn;

	public View menu_contactsBtn;

	public View menu_guideBtn;

	public View menu_log_outBtn;

	public View menu_synthesisBtn;

	ImageButton help;

	private void initMenu() {
		slidMenu = (SlidingDrawer) findViewById(R.id.slid_menu);
		if(slidMenu == null){
			return;
		}
		userInfoBtn = findViewById(R.id.menu_user_info);
		userInfoBtn.setOnClickListener(this);

		menu_accountsBtn = (ReHeightImageButton) findViewById(R.id.menu_accounts);
		if (Contants.baseAccounts == null || Contants.baseAccounts.size() == 0) {
			menu_accountsBtn.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_accounts_disable));
		} else {
			menu_accountsBtn.setOnClickListener(this);
		}
        if (isOffline) {
			menu_accountsBtn.setOnClickListener(this);
        }

		menu_investmentsBtn = (ReHeightImageButton) findViewById(R.id.menu_investments);
		if (Contants.investmentAccounts == null
				|| Contants.investmentAccounts.size() == 0) {
			menu_investmentsBtn.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_investments_disable));
		} else {
			menu_investmentsBtn.setOnClickListener(this);
		}
        if (isOffline) {
        	menu_investmentsBtn.setOnClickListener(this);
        }

		menu_cardsBtn = (ReHeightImageButton) findViewById(R.id.menu_cards);
		if (Contants.cardAccounts == null || Contants.cardAccounts.size() == 0) {
			menu_cardsBtn.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_cards_disable));
		} else {
			menu_cardsBtn.setOnClickListener(this);
		}
        if (isOffline) {
        	menu_cardsBtn.setOnClickListener(this);
        }

		menu_loansBtn = (ReHeightImageButton) findViewById(R.id.menu_loans);
		if (Contants.loansAccounts == null
				|| Contants.loansAccounts.size() == 0) {
			menu_loansBtn.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_loans_disable));
		} else {
			menu_loansBtn.setOnClickListener(this);
		}
        if (isOffline) {
        	menu_loansBtn.setOnClickListener(this);
        }

		menu_paymentBtn = (ReHeightImageButton) findViewById(R.id.menu_payment);
		menu_paymentBtn.setOnClickListener(this);

		if (isOffline) {
        	menu_paymentBtn.setOnClickListener(this);
        }
		menu_contactsBtn = findViewById(R.id.menu_contacts);
		menu_contactsBtn.setOnClickListener(this);
		menu_guideBtn = findViewById(R.id.menu_guide);
		menu_guideBtn.setOnClickListener(this);
		menu_log_outBtn = findViewById(R.id.menu_log_out);
		menu_log_outBtn.setOnClickListener(this);
		menu_synthesisBtn = findViewById(R.id.menu_synthesis);
		menu_synthesisBtn.setOnClickListener(this);
	}

	protected void onBackClick() {
		
	}

	@Override
	public void onBackPressed() {
		if (slidMenu != null && slidMenu.isOpened()) {
			slidMenu.animateClose();
			return;
		}
		super.onBackPressed();
	}

	protected String getResourceString(int id) {
		String str = getResources().getString(id);
		return str;

	}

	public boolean isFinished = false;

	public AlertDialog errorMessageDialog;

	public void displayErrorMessage(final String errorCode,final String errordec) {
		LogManager.d("displayErrorMessage");
		if (isFinished) {
			return;
		}
		baseHandler.post(new Runnable() {
			@Override
			public void run() {
				if (errorMessageDialog != null) {
					errorMessageDialog.dismiss();
					errorMessageDialog = null;
				}
				errorMessageDialog = DialogManager.createMessageDialog(errorCode,errordec, BaseActivity.this);
				errorMessageDialog.show();
			}
		});
	}


	public void displayErrorMessage(final com.accenture.mbank.logic.EventManagement eventManagement) {
		LogManager.d("displayErrorMessage");
		if (isFinished) {
			return;
		}
		baseHandler.post(new Runnable() {
			@Override
			public void run() {
				if (errorMessageDialog != null) {
					errorMessageDialog.dismiss();
					errorMessageDialog = null;
				}
				if(eventManagement!=null){
					errorMessageDialog = DialogManager.createMessageDialog(eventManagement.getErrorCode(),eventManagement.getErrorDescription(), BaseActivity.this);
				}else{
					errorMessageDialog = DialogManager.createMessageDialog("",getString(R.string.unknown_connection_error), BaseActivity.this);
				}
				errorMessageDialog.show();

			}
		});
	}
	
	public void displayErrorMessage(final EventManagement eventManagement) {
		LogManager.d("displayErrorMessage");
		if (isFinished) {
			return;
		}
		baseHandler.post(new Runnable() {
			@Override
			public void run() {
				if (errorMessageDialog != null) {
					errorMessageDialog.dismiss();
					errorMessageDialog = null;
				}
				if(eventManagement!=null){
					errorMessageDialog = DialogManager.createMessageDialog(eventManagement.getErrorCode(),eventManagement.getErrorDescription(), BaseActivity.this);
				}else{
					errorMessageDialog = DialogManager.createMessageDialog("",getString(R.string.unknown_connection_error), BaseActivity.this);
				}
				errorMessageDialog.show();

			}
		});
	}

	public void hideBack() {
		if (back != null) {
			back.setVisibility(View.GONE);
		}
	}

	public void showBack() {
		if (back != null) {
			back.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogManager.d(this.getClass() + "onResume");
		showBankLogo();
		
		IntentFilter filter = new IntentFilter();  
        filter.addAction(LOGOUT);  
        registerReceiver(this.broadcastReceiver, filter); // 注册  
	}

	public void displaySuccessMessage(final String msg) {
		baseHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	protected void showBankLogo() {
		ImageButton btn = (ImageButton) findViewById(R.id.bank_logo);
		if (btn == null)
			return;

		final SharedPreferences settings = this.getSharedPreferences(
				Contants.SETTING_FILE_NAME, MODE_PRIVATE);
		String strBankCode = settings.getString(Contants.BANK_CODE, "");

		for (int i = 0; i < Contants.idBankButton.length; i++) {
			if (strBankCode.equals(Contants.strBankCode[i])) {
				btn.setImageDrawable(getResources().getDrawable(
						Contants.idBankLogo[i]));
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		MainActivity mainAcitivty = (MainActivity) MainActivity.getContext();
		if(v == back){
			onBackPressed();
		}else if (v == help) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showHelpList();
		} else if (v == userInfoBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showUserInfo();
			slidMenu.animateClose();
		} else if (v == menu_accountsBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showTab(MainActivity.ACCOUNTS);
			slidMenu.animateClose();
		} else if (v == menu_cardsBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showTab(MainActivity.CARDS);
			slidMenu.animateClose();
		} else if (v == menu_contactsBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showContant();
			slidMenu.animateClose();
		} else if (v == menu_guideBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showHelpList();
			slidMenu.animateClose();
		} else if (v == menu_investmentsBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showInvestments();
			slidMenu.animateClose();
		} else if (v == menu_loansBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showLoans();
			slidMenu.animateClose();
		} else if (v == menu_log_outBtn) {
			DialogManager.createMessageExitDialog(
					getResources().getString(R.string.sure_to_exit), this)
					.show();
		} else if (v == menu_paymentBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showTab(MainActivity.PAYMENTS);
			slidMenu.animateClose();
		}  else if (v == menu_synthesisBtn) {
			launchMainActivity(this,MainActivity.class);
			mainAcitivty.showTab(MainActivity.SYNTHESIS);
            slidMenu.animateClose();
        }
	}
	
	public void launchMainActivity(Context packageContext,Class<?> cls) {
		MainActivity mainAcitivty = (MainActivity) MainActivity.getContext();
		mainAcitivty._activity = packageContext;
		Intent intent=new Intent();
		intent.setClass(packageContext,cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//Flag
		startActivity(intent);
	}

	private AlphaAnimation mHideAnimation = null;
	private AlphaAnimation mShowAnimation = null;

	/**
	 * View fade out
	 */
	protected void setHideAnimation(View view, int duration,
			AnimationListener listener) {
		if (null == view || duration < 0) {
			return;
		}
		if (null != mHideAnimation) {
			mHideAnimation.cancel();
		}
		mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
		mHideAnimation.setDuration(duration);
		mHideAnimation.setInterpolator(new AccelerateInterpolator());
		mHideAnimation.setFillAfter(true);
		if (listener != null) {
			mHideAnimation.setAnimationListener(listener);
		}
		view.startAnimation(mHideAnimation);
		LogManager.d("setHideAnimation finish");
	}

	/**
	 * View fade in
	 */
	protected void setShowAnimation(View view, int duration,AnimationListener listener) {
		if (null == view || duration < 0) {
			return;
		}
		if (null != mShowAnimation) {
			mShowAnimation.cancel();
		}
		mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
		mShowAnimation.setInterpolator(new AccelerateInterpolator());
		mShowAnimation.setDuration(duration);
		mShowAnimation.setFillAfter(true);
		if (listener != null) {
			mShowAnimation.setAnimationListener(listener);
		}
		view.startAnimation(mShowAnimation);
		LogManager.d("setShowAnimation finish");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		Toast.makeText(this, "BaseActivity onCreateOptionsMenu", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	Handler mMenuHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		setMenuVisibility(msg.what);
    	};
    };
    
    void setMenuVisibility(int visibility){
    	if(visibility==View.VISIBLE){
    		findViewById(R.id.menu).setVisibility(visibility);
    		findViewById(R.id.menu_highter).setVisibility(View.INVISIBLE);
//    		Toast.makeText(this, "BaseActivity keyboard hide & menu show", Toast.LENGTH_SHORT).show();
    	}else{
    		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        		findViewById(R.id.menu).setVisibility(visibility);
    			findViewById(R.id.menu_highter).setVisibility(visibility);
//    			Toast.makeText(this, "BaseActivity keyboard show & menu hide", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  

        @Override 

        public void onReceive(Context context, Intent intent) {  

            close();  

            unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错  

        }  

    };  
    public static final String LOGOUT="logout_bper";
    public void close() {  
        finish();  
    }  
    public static void Logout(Context context){
    	Intent intent = new Intent();  

        intent.setAction(LOGOUT); // 说明动作  

        context.sendBroadcast(intent);// 该函数用于发送广播  
    }
    
    public Location getLocation() {
		Location location = getLastKnownLocation();
		
		if(location==null) {
			location = new Location(LocationManager.PASSIVE_PROVIDER);
			location.setLatitude(0);
			location.setLongitude(0);
		}

		return location;
	}
    
	private Location getLastKnownLocation() {
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
		locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
		locationManager.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, listener, null);
		
		List<String> providers = locationManager.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location location = null;
		for (int i=providers.size()-1; i>=0; i--) {
			location = locationManager.getLastKnownLocation(providers.get(i));
			if (location != null)
				break;
		}
		return location;
	}
}
