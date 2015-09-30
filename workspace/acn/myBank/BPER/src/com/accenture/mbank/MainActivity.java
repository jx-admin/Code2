package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.LangUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.accenture.manager.AccountRotateLayoutManager;
import com.accenture.manager.CardsRotateLayoutManager;
import com.accenture.manager.InvestmentRotateLayoutManager;
import com.accenture.manager.LoansRotateLayoutManager;
import com.accenture.mbank.database.BankSqliteHelper;
import com.accenture.mbank.gmap.HttpConnectionUtil;
import com.accenture.mbank.logic.GetBranchListJson;
import com.accenture.mbank.logic.HelpItemJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.model.GetBranchListResponseModel;
import com.accenture.mbank.model.GetHelpItemResponseModel;
import com.accenture.mbank.model.HelpItemListModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountsLayout;
import com.accenture.mbank.view.BankRollContainer;
import com.accenture.mbank.view.BankRollContainerManager;
import com.accenture.mbank.view.BankRollView;
import com.accenture.mbank.view.BankSlidContainer;
import com.accenture.mbank.view.CardsLayoutManager;
import com.accenture.mbank.view.ContactCustomServiceLayout;
import com.accenture.mbank.view.ContactNewRequestLayout;
import com.accenture.mbank.view.ContactTheifLossLayout;
import com.accenture.mbank.view.InvestmentsLayoutManager;
import com.accenture.mbank.view.ItemExpander;
import com.accenture.mbank.view.LoansLayout;
import com.accenture.mbank.view.MapWrapperLayout;
import com.accenture.mbank.view.MarkerMapOnCLickListener;
import com.accenture.mbank.view.MySupportMapFragment;
import com.accenture.mbank.view.PopView;
import com.accenture.mbank.view.ReHeightImageButton;
import com.accenture.mbank.view.UserInfoLayoutManager;
import com.accenture.mbank.view.protocol.CloseListener;
import com.accenture.mbank.view.protocol.ShowAble;
import com.custom.view.KeyboardLayout;
import com.custom.view.KeyboardLayout.onKybdsChangeListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends BaseActivity implements OnClickListener {

	TabHost tabHost;

	Stack<Object> tabStack = new Stack<Object>();

	Object lastTab = null;

	static Context context;

	public static Context getContext() {
		return context;
	}

	private ReHeightImageButton imageView1;

	private ReHeightImageButton imageView2;

	private Drawable drawable1;

	private Drawable drawable2;

	private ReHeightImageButton imageView3;

	private Drawable drawable3;

	public ReHeightImageButton imageView4;

	private Drawable drawable4;

	public static final int SYNTHESIS = 0;

	public static final int ACCOUNTS = 1;

	public static final int CARDS = 2;

	public static final int PAYMENTS = 3;

	Handler mHandler;

	private boolean mNeedFadeIn = false;

	public static final String ACCOUNT_PAYMENTMETHOD = "2";

	public static final String CARDS_PAYMENTMETHOD = "1";

	com.accenture.mbank.view.protocol.ShowListener newVersionRecentPaymentListener;

	com.accenture.mbank.view.protocol.ShowListener newVersionRecentPaymentListener1;

	com.accenture.mbank.view.protocol.ShowListener newVersionNewPaymentListener;
	
	protected GoogleMap map;
	private PopView mPopView;
	
	private LinearLayout searchPoint;
	
	private EditText searchEdt;

	private ImageButton mapLayer, myLocation, showItems;
	private Button searchButton;
	private EditText distance_input;
	
	private ArrayList<BranchListModel> branchListModels;
	private HashMap<Marker,BranchListModel> markBrankBranchList;
	private LatLng myLocationLatLng;
	private Bitmap icon;
	private Bitmap iconBank;
	private MarkerMapOnCLickListener markerClickListener;
	private Marker marker;
	private MapWrapperLayout  mapWrapperLayout;
	private ViewGroup enter_distance_layout;
	private Handler handler;
	private Handler httpSearchHandler;
	private LatLng pt = null;
	private boolean isPopupVisible = false;
	public Context _activity;
	private static final int HELP = 9;
	private static final int HELP_CONTENT=10;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogManager.d("oncreate");
        setCanOrientation(false);
        initSettingModel();
        this.context = this;
        setContentView(R.layout.main);
        ((KeyboardLayout) findViewById(R.id.main_window)).setOnkbdStateListener(new onKybdsChangeListener() {
    		
    		public void onKeyBoardStateChange(int state) {
    			if(contant_us_layout!=null&&contant_us_layout.getVisibility()==View.VISIBLE){
	    			switch (state) {
	    			case KeyboardLayout.KEYBOARD_STATE_HIDE:
	    				mMenuHandler.sendEmptyMessage(View.VISIBLE);
	    				break;
	    			case KeyboardLayout.KEYBOARD_STATE_SHOW:
	    				mMenuHandler.sendEmptyMessage(View.GONE);
	    				break;
	    			}
    			}
    		}
    	});
        
		mHandler = new Handler();
		initTabHost();
		initHelp();
		initHelpContent("", "");
//		initContant();
		initUserInfoLayout();
		initInvestments();
		initLoansLayout();
		initRotateLayout();
		initInvestmentRotateLayout();
		showTab(0);

		tabStack.push((Integer) (0));
		LogManager.i("BackStack: [0]");
		
		setMenu();

		Timer timing = new Timer();
		timing.schedule(new TimerTask() {

			@Override
			public void run() {
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							ViewGroup mainWindow = (ViewGroup) findViewById(R.id.main_window);
							mainWindow.invalidate();
						} catch (Exception e) {
						}

					}
				});

			}
		}, 1000);

	}

	/*
	 * Used to perform fade in animation
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (mNeedFadeIn) {
			mNeedFadeIn = false;
			setCanOrientation(false);
			ViewGroup mainWindow = (ViewGroup) findViewById(R.id.main_window);
			setShowAnimation(mainWindow, Contants.TIME_FADE_IN_ANIMATION, null);
		}
		setCanRotate();
	}

	@Override
	public void onPause() {
		super.onPause();
		setCanOrientation(false);
	}
	
	private void setCanRotate() {
		if (slidMenu != null && slidMenu.isOpened()) {
			setCanOrientation(false);
			return;
		}

		if (currentScreen != null && cardsRotateLayout == currentScreen) {
			if (cardsRotateLayoutManager != null)
				cardsRotateLayoutManager.setCanOrientation();
		} else if (currentScreen != null
				&& accountsRotateLayout == currentScreen) {
			if (accountRotateLayoutManager != null)
				accountRotateLayoutManager.setCanOrientation();
		} else if (currentScreen != null && loansRotateLayout == currentScreen) {
			if (loansRotateLayoutManager != null)
				loansRotateLayoutManager.setCanOrientation();
		} else if (currentScreen != null
				&& (investmentsRotateLayout == currentScreen)) {
			if (investmentRotateLayoutManager != null)
				investmentRotateLayoutManager.setCanOrientation();
		} else {
			setCanOrientation(false);
		}
	}

	private void setMenu() {
		if (slidMenu != null) {
			slidMenu.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener(){
				 @Override
				   public void onDrawerOpened() {
						setCanOrientation(false);
				   }
			} );
			
			slidMenu.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener(){
				 @Override
				   public void onDrawerClosed() {
						setCanRotate();
				   }
			} );
		}
	}

	public static SettingModel setting;

	private void initSettingModel() {
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String username = bundle.getString("username");

			if (username != null) {
				BankSqliteHelper bankSqliteHelper = BankSqliteHelper
						.getInstance();
				setting = (SettingModel) bankSqliteHelper.findBykey(
						SettingModel.TABLE_NAME, SettingModel.COUM_USER_ID,
						username);
				if (setting == null) {
					this.setting = new SettingModel();
					setting.setUserId(username);
					bankSqliteHelper.insert(setting);
				}

			}

		}
		if (BaseActivity.isOffline) {
			BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
			setting = (SettingModel) bankSqliteHelper.findBykey(
					SettingModel.TABLE_NAME, SettingModel.COUM_USER_ID,
					"offline");

			if (setting == null) {
				this.setting = new SettingModel();
				setting.setUserId("offline");
				bankSqliteHelper.insert(setting);
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (slidMenu != null && slidMenu.isOpened()) {
			slidMenu.animateClose();
			return;
		}
		onBackClick();
	}

	@Override
	protected void onBackClick() {
		super.onBackClick();
		LogManager.i("BackStack: onBackClick()");
		
		Object obj = tabStack.pop();
		if(lastTab instanceof Integer && ((Integer)lastTab).intValue() == HELP){
				if(obj instanceof Integer){
					showTab(((Integer)obj).intValue());
				}else {
					if (obj == investments) {
						investmentRotateLayoutManager.setAccount(Contants.accountsList);
						showInvestmentsRotateLayout();
					} else if (obj == cards) {
						showCardsRotateLayout();
					} else if (obj == loans) {
						showLoanssRotateLayout();
					} else if (obj == accounts) {
						showAccountsRotateLayout();
					} else if(obj == menu_investmentsBtn){
						showInvestments();
					} else if(obj == menu_contactsBtn){
						showContant();
					} else if(obj == userInfoBtn){
						showUserInfo();
					} else if(obj == menu_loansBtn){
						showLoans();
					}else if(obj == menu_synthesisBtn){
						showTab(0);
					}
				}
				lastTab = obj;
			}else if(lastTab instanceof Integer && ((Integer)lastTab).intValue() == HELP_CONTENT){
				lastTab = obj;
				showHelpList();
			}else if(!(_activity instanceof MainActivity) && _activity != null){
				if(lastTab instanceof Integer){
					showTab(((Integer)lastTab).intValue());
				}else {
					if (lastTab == investments) {
						investmentRotateLayoutManager.setAccount(Contants.accountsList);
						showInvestmentsRotateLayout();
					} else if (lastTab == cards) {
						showCardsRotateLayout();
					} else if (lastTab == loans) {
						showLoanssRotateLayout();
					} else if (lastTab == accounts) {
						showAccountsRotateLayout();
					}
				}
				lastTab = obj;
				launchMainActivity(this, _activity.getClass());
			}else{
				lastTab = obj;
				showTab(0);
			}
		if (tabStack.size() == 0) {
			tabStack.push((Integer) (0));
			lastTab = null;
		}

		debugBackStack();
	}

	public void addToBackStack(Object o) {
		if (lastTab != null && lastTab != o) {
			tabStack.push(lastTab);
		}
		lastTab = o;
		debugBackStack();
	}

	private void debugBackStack() {
		String output = "BackStack:[";
		if (!tabStack.empty()) {
			for (Object x : tabStack) {
				output += (x.toString() + ",");
			}
		}
		if (output.endsWith(","))
			output = output.substring(0, output.length()- 1);
		output += "]";
		if (lastTab != null)
			output += (" LastTab:" + lastTab.toString());
		LogManager.i(output);
	}

	public static final String TAB_SYNTHESIS = "tab_synthesis";

	public static final String TAB_ACCOUNTS = "tab_accounts";

	public static final String TAB_CARDS = "tab_cards";

	public static final String TAB_PAYMENTS = "tab_payments";

	private LinearLayout tab1, tab2;

	public BankRollContainer tab4, tab3;

	private void initTabHost() {

		tabHost = (TabHost) findViewById(R.id.tab);
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

		tabHost.addTab(tabHost.newTabSpec(TAB_CARDS).setContent(R.id.tab3)
				.setIndicator(imageView3));

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
				addToBackStack(tabHost.getCurrentTab());

				resumeAllTabHostImageButton();

				tab4.setVisibility(View.INVISIBLE);
				hideUserInfo();
				hideArrowWindow();
				hideSynthesis();

				View visibleView = null;
				if (tabId.equals(TAB_PAYMENTS)) {
					// if (Contants.payment_disabled){
					// tabHost.setCurrentTab(index);
					// return;
					// }
					imageView4.setImageDrawable(getResources().getDrawable(
							R.drawable.label_btn_payments_over));
					tab4.setVisibility(View.VISIBLE);

					cardsLayoutManager = CardsLayoutManager.create();
					cardsLayoutManager.setRollContainer(tab4);
					cardsLayoutManager.updateArrowState();
//					showCards();
					visibleView = tab4;
				} else if (tabId.equals(TAB_SYNTHESIS)) {
					imageView1.setImageDrawable(getResources().getDrawable(
							R.drawable.label_btn_synthesis_over));
					visibleView = tab1;
					showSynthesis();
				} else if (tabId.equals(TAB_CARDS)) {
					imageView3.setImageDrawable(getResources().getDrawable(
							R.drawable.label_btn_cards_over));
					cardsLayoutManager = CardsLayoutManager.create();
					cardsLayoutManager.setRollContainer(tab3);
					showCards();
					visibleView = tab3;
				} else if (tabId.equals(TAB_ACCOUNTS)) {
					visibleView = tab2;
					imageView2.setImageDrawable(getResources().getDrawable(
							R.drawable.label_btn_accounts_over));
					showAccounts();
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
		currentScreen = visibleView;

	}

	private void updateNavagition(View visibleView) {
		if (visibleView == tab1) {
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
		if(v == null){
			return;
		}
		if (v.getVisibility() != View.GONE)
			v.setVisibility(View.GONE);
	}

	private void hideArrowWindow() {
		findViewById(R.id.arrow_up).setVisibility(View.INVISIBLE);
		findViewById(R.id.arrow_down).setVisibility(View.INVISIBLE);

		findViewById(R.id.arrow_up_anim).setVisibility(View.INVISIBLE);
		findViewById(R.id.arrow_down_anim).setVisibility(View.INVISIBLE);
	}

	private void hideSynthesis() {
		investments.setVisibility(View.GONE);
		accounts.setVisibility(View.GONE);
		cards.setVisibility(View.GONE);
		loans.setVisibility(View.GONE);

		investments.setOnClickListener(null);
		accounts.setOnClickListener(null);
		cards.setOnClickListener(null);
		loans.setOnClickListener(null);
	}

	private void showSynthesis() {
		investments.setVisibility(View.VISIBLE);
		accounts.setVisibility(View.VISIBLE);
		cards.setVisibility(View.VISIBLE);
		loans.setVisibility(View.VISIBLE);

		enableTab1Buttons();
	}

	/**
	 * 隐藏非tabhost的界面
	 */
	private void hideViews() {
		hideUserInfo();
		hideInvestments();
		hideView(contant_us_layout);
		hideView(helps_layout);
		hideView(helpDetailContainer);
		hideLoans();
		hideView(cardsRotateLayout);
		hideView(accountsRotateLayout);
		hideView(investmentsRotateLayout);
		hideView(loansRotateLayout);

		hideArrowWindow();

		hideSynthesis();
	}

	BankRollContainer userInfoRollContainer;

	ViewGroup userInfoLayout;

	UserInfoLayoutManager userInfoLayoutManager;

	private String getUserInfo() {
		StringBuffer buffer = new StringBuffer(getResources().getString(
				R.string.update_to));
		SimpleDateFormat format = new SimpleDateFormat(TimeUtil.dateFormat5,
				Locale.US);
		buffer.append(" ");
		buffer.append(format.format(Calendar.getInstance().getTime()));
		return buffer.toString();
	}

	private void initUserInfoLayout() {
		userInfoRollContainer = (BankRollContainer) findViewById(R.id.user_info);
		userInfoLayout = (ViewGroup) findViewById(R.id.userinfo_layout);
		TextView userinfo = (TextView) userInfoLayout
				.findViewById(R.id.user_info_time);
		userinfo.setText(getUserInfo());
		userInfoRollContainer.init();
		// show item
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		BankRollView showItemRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		showItemRollView.setIsScale(false);
		showItemRollView.init();
		userInfoRollContainer.addShowAble(showItemRollView);

		showItemRollView
				.setCloseImage(R.drawable.sphere_show_items);
		showItemRollView
				.setShowImage(R.drawable.show_selector);
//		showItemRollView.setBallIcon(R.drawable.icon_black_2);
//		showItemRollView.setBallName(R.string.show_item);

		// pin manager
		BankRollView pinManagerRollView = (BankRollView) layoutInflater
				.inflate(R.layout.bank_roll_view, null);
		pinManagerRollView.setIsScale(false);
		pinManagerRollView.init();
		userInfoRollContainer.addShowAble(pinManagerRollView);

		pinManagerRollView
				.setCloseImage(R.drawable.sphere_pin_manager);
		pinManagerRollView
				.setShowImage(R.drawable.show_selector);
//		pinManagerRollView.setBallIcon(R.drawable.icon_black_1);
//		pinManagerRollView.setBallName(R.string.show_pin);

		// accounts
		BankRollView accountsRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		accountsRollView.setIsScale(false);
		accountsRollView.init();
		userInfoRollContainer.addShowAble(accountsRollView);

		accountsRollView
				.setCloseImage(R.drawable.sphere_accounts);
		accountsRollView
				.setShowImage(R.drawable.show_selector);
//		accountsRollView.setBallIcon(R.drawable.icon_black_3);
//		accountsRollView.setBallName(R.string.show_account);

		// cards
		BankRollView cardsRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		cardsRollView.setIsScale(false);
		cardsRollView.init();
		userInfoRollContainer.addShowAble(cardsRollView);

		cardsRollView
				.setCloseImage(R.drawable.sphere_cards);
		cardsRollView
				.setShowImage(R.drawable.show_selector);
//		cardsRollView.setBallIcon(R.drawable.icon_black_4);
//		cardsRollView.setBallName(R.string.show_card);

		// loans
		BankRollView loansRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		loansRollView.setIsScale(false);
		loansRollView.init();
		userInfoRollContainer.addShowAble(loansRollView);

		loansRollView
				.setCloseImage(R.drawable.sphere_loans);
		loansRollView
				.setShowImage(R.drawable.show_selector);
//		loansRollView.setBallIcon(R.drawable.icon_black_5);
//		loansRollView.setBallName(R.string.show_loans);

		// investments
		BankRollView investmentsRollView = (BankRollView) layoutInflater
				.inflate(R.layout.bank_roll_view, null);
		investmentsRollView.setIsScale(false);
		investmentsRollView.init();
		userInfoRollContainer.addShowAble(investmentsRollView);

		investmentsRollView
				.setCloseImage(R.drawable.sphere_investments);
		investmentsRollView
				.setShowImage(R.drawable.show_selector);
//		investmentsRollView.setBallIcon(R.drawable.icon_black_6);
//		investmentsRollView.setBallName(R.string.show_investment);

		/*
		 * // push notification BankRollView pushNotificationRollView =
		 * (BankRollView)layoutInflater.inflate( R.layout.bank_roll_view, null);
		 * pushNotificationRollView.init();
		 * userInfoRollContainer.pushNotificationRollView
		 * =pushNotificationRollView;
		 * userInfoRollContainer.addShowAble(pushNotificationRollView);
		 * pushNotificationRollView
		 * .setCloseImage(R.drawable.sphere_notification);
		 * pushNotificationRollView.setShowImage(R.drawable.show_selector);
		 */
		userInfoLayoutManager = new UserInfoLayoutManager();
		userInfoLayoutManager.setRollContainer(userInfoRollContainer);
	}

	public ViewGroup contant_us_layout;
	public BankRollContainer contantBankRollContainer;
	BankRollView searchRollView;
	TextView webSitTextView;
	BankRollContainerManager contant_us_bankRollContainerManager;
	CloseListener contantCloseListener;
	CloseListener mapCloseListener;
	private void initContant() {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		contantBankRollContainer = (BankRollContainer) findViewById(R.id.contant_us);
		contant_us_layout = (ViewGroup) findViewById(R.id.contant_us_layout);
		contantBankRollContainer.init();

		BankRollView contactusRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		contactusRollView.setIsScale(false);
		contactusRollView.init();

		contactusRollView.setCloseImage(R.drawable.sphere_contact_us);
		contactusRollView.setShowImage(R.drawable.show_selector);
//		contactusRollView.setBallIcon(R.drawable.icon_black_11);
//		contactusRollView.setBallName(R.string.contact_us_fs);
		final ViewGroup layout = (ViewGroup) layoutInflater.inflate(
				R.layout.contact_us_layout, null);
		contactusRollView.setContent(layout);
		contantBankRollContainer.addShowAble(contactusRollView);
		contantCloseListener = new  CloseListener() {
			@Override
			public void onClose() {
				contant_us_bankRollContainerManager.updateArrowState();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						contant_us_bankRollContainerManager.updateArrowState();
					}
				}, 1000);
			}
		};
		
		contactusRollView.setCloseListener(contantCloseListener);
		// contactusRollView.setPadding(0, contactusRollView.height_bottom, 0,
		// 0);

		ItemExpander itemExpander = (ItemExpander) layout
				.findViewById(R.id.customer_service);
		ContactCustomServiceLayout contactCustomServiceLayout = (ContactCustomServiceLayout) layoutInflater
				.inflate(R.layout.contact_customer_service, null);
		String bankCode = Contants.getBankCode(this);
		TextView us=((TextView)contactCustomServiceLayout.findViewById(R.id.us));
		us.setText(Contants.getUsBankCode(bankCode));
		us.setLinkTextColor(us.getResources().getColor(R.color.link_color));
		
		TextView altro_paese=((TextView)contactCustomServiceLayout.findViewById(R.id.altro_paese));
		altro_paese.setText(Contants.getAltroPaese());
		altro_paese.setLinkTextColor(us.getResources().getColor(R.color.link_color));
		
		TextView business_hour=((TextView)contactCustomServiceLayout.findViewById(R.id.business_hour));
		business_hour.setText(Contants.getBusinessHourBackCode(bankCode));
		business_hour.setLinkTextColor(business_hour.getResources().getColor(R.color.link_color));
		TextView e_mail=((TextView)contactCustomServiceLayout.findViewById(R.id.e_mail));
		e_mail.setText(Contants.getEmailBackCode(bankCode));
		e_mail.setLinkTextColor(e_mail.getResources().getColor(R.color.link_color));
		webSitTextView = (TextView) contactCustomServiceLayout.findViewById(R.id.website);
		webSitTextView.setText(Contants.getWebsiteBankCode(bankCode));
		webSitTextView.setLinkTextColor(webSitTextView.getResources().getColor(R.color.link_color));

		itemExpander.setExpandedContainer(contactCustomServiceLayout);

		itemExpander.setTitle(R.string.customer_server);
		// itemExpander.setTitle("CUSTOMER SERVICE");
		itemExpander.setTypeface(Typeface.DEFAULT);
		itemExpander.onChange(" ");
		itemExpander.setExpandable(true);
		itemExpander.setExpand(false);

		itemExpander = (ItemExpander) layout.findViewById(R.id.thief_and_loss);
		ContactTheifLossLayout contactTheifLossLayout = (ContactTheifLossLayout) layoutInflater
				.inflate(R.layout.contact_theif_loss, null);
		((TextView) contactTheifLossLayout.findViewById(R.id.phone1)).setLinkTextColor(getResources().getColor(R.color.link_color));
		((TextView) contactTheifLossLayout.findViewById(R.id.phone2)).setLinkTextColor(getResources().getColor(R.color.link_color));
		((TextView) contactTheifLossLayout.findViewById(R.id.phone3)).setLinkTextColor(getResources().getColor(R.color.link_color));
		((TextView) contactTheifLossLayout.findViewById(R.id.phone4)).setLinkTextColor(getResources().getColor(R.color.link_color));
		
		
		itemExpander.setExpandedContainer(contactTheifLossLayout);
		itemExpander.setExpandable(true);
		itemExpander.setExpand(false);
		itemExpander.setTitle(R.string.theft_loss);
		// itemExpander.setTitle("THEFT");
		itemExpander.setTypeface(Typeface.DEFAULT);
		itemExpander.onChange(" ");
		/*
		 * menu is only visible after user has logged in
		 */
		itemExpander = (ItemExpander) layout.findViewById(R.id.new_request);
		ContactNewRequestLayout contactNewRequestLayout = (ContactNewRequestLayout) layoutInflater
				.inflate(R.layout.contact_new_request, null);
		itemExpander.setExpandedContainer(contactNewRequestLayout);
		itemExpander.setExpandable(true);
		itemExpander.setResultVisible(false);
		// itemExpander.setTitle("NEW REQUEST");
		itemExpander.setTitle(R.string.new_request);
		itemExpander.setTypeface(Typeface.DEFAULT);
		itemExpander.setExpand(false);
		itemExpander.onChange(" ");
		itemExpander.setVisibility(View.VISIBLE);

//		final MapLayout maplayout = (MapLayout) layoutInflater.inflate(R.layout.search_width_map, null);
		LinearLayout maplayout = (LinearLayout) layoutInflater.inflate(R.layout.search_width_map_v2, null);
		map = ((MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		myLocationLatLng = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
//		maplayout.init();
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 13));
		
		icon = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
		iconBank = BitmapFactory.decodeResource(getResources(), R.drawable.green_dot);
		
		marker = map.addMarker(new MarkerOptions()
	     .icon(BitmapDescriptorFactory.fromBitmap(icon))
	     .position(myLocationLatLng)
	     .flat(false));
		
		markerClickListener = new MarkerMapOnCLickListener(MainActivity.this);
		markerClickListener.setMarker(marker);
		map.setOnMarkerClickListener(null);

//		maplayout.parentScrollView = contantBankRollContainer;
		searchRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		searchRollView.setIsScale(false);
		searchRollView.init();

		searchRollView.setCloseImage(R.drawable.sphere_search_branch);
		searchRollView.setShowImage(R.drawable.show_selector);
//		searchRollView.setBallIcon(R.drawable.icon_black_12);
//		searchRollView.setBallName(R.string.search_branch_fs);

		contantBankRollContainer.addShowAble(searchRollView);
		searchRollView.setContent(maplayout);
		
		ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        int action = event.getAction();
		        switch (action) {
		           case MotionEvent.ACTION_DOWN:
		                // Disallow ScrollView to intercept touch events.
		        	   contantBankRollContainer.requestDisallowInterceptTouchEvent(true);
		                // Disable touch on transparent view
		                return false;

		           case MotionEvent.ACTION_UP:
		                // Allow ScrollView to intercept touch events.
		        	   contantBankRollContainer.requestDisallowInterceptTouchEvent(false);
		                return true;

		           case MotionEvent.ACTION_MOVE:
		        	   contantBankRollContainer.requestDisallowInterceptTouchEvent(true);
		                return false;

		           default:
		                return true;
		        }   
		    }
		});
		
		mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.relativeLayoutMap);
		
		mPopView = (PopView) this.getLayoutInflater().inflate(R.layout.map_popover, null);
		searchPoint = (LinearLayout) this.getLayoutInflater().inflate(R.layout.search_point, null);
		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		searchPoint.setLayoutParams(layoutParam);
		
		handler = new Handler();
		
		httpSearchHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(HttpConnectionUtil.SUCCESS_GEOCODER == msg.what){
					double[] response = (double[]) msg.getData().get("response");
					
					LatLng geopoint = new LatLng(response[0], response[1]);
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(geopoint, 13));
				} else {
					Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
				}
			}
		};
		
		Button mSearchPointButton = (Button) searchPoint.findViewById(R.id.search_point_button);
		mSearchPointButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pt = map.getCameraPosition().target;
				ProgressOverlay progressOverlay = new ProgressOverlay(MainActivity.this);
				progressOverlay.show(MainActivity.this.getString(R.string.loading), new OnProgressEvent() {
					@Override
					public void onProgress() {
						searchBarch("", pt);
					}
				});
			}
		});
		
		mapWrapperLayout.addView(searchPoint);
		searchPoint.setVisibility(View.GONE);
		
		myLocation = (ImageButton) maplayout.findViewById(R.id.my_location);
		myLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMyLocation();
			}
		});
		
		mapLayer = (ImageButton) maplayout.findViewById(R.id.map_layer);
		showItems = (ImageButton) maplayout.findViewById(R.id.show_items);
		searchButton = (Button) maplayout.findViewById(R.id.search_btn);
		searchEdt = (EditText) maplayout.findViewById(R.id.search_input);
		distance_input = (EditText) findViewById(R.id.distance_input);
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard(MainActivity.this, searchEdt);
				ProgressOverlay progressOverlay = new ProgressOverlay(MainActivity.this);
				progressOverlay.show(MainActivity.this.getString(R.string.loading), new OnProgressEvent() {
					@Override
					public void onProgress() {
						final String keyText = searchEdt.getText().toString();
						LatLng searchGeoPoint = searchLocation(keyText);
						if (searchGeoPoint != null) {
							searchBarch(keyText,searchGeoPoint);
						}
					}
				});
			}
		});
		
		mapLayer.setOnClickListener(new OnClickListener() {
			int i;
			@Override
			public void onClick(View v) {
				i++;
				int value = i % 3;
				switch (value) {
				case 0://normal
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					break;
				case 1: //satellite
					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					break;
				case 2: //hybrid
					map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				}
			}
		});
		
		enter_distance_layout = (ViewGroup) maplayout.findViewById(R.id.enter_distance_layout);
		
		showItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (enter_distance_layout.getVisibility() == View.VISIBLE) {
					enter_distance_layout.setVisibility(View.GONE);
				} else {
					enter_distance_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		mapCloseListener = new  CloseListener() {
			@Override
			public void onClose() {
				contant_us_bankRollContainerManager.updateArrowState();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						contant_us_bankRollContainerManager.updateArrowState();
					}
				}, 1000);
			}
		};
		
		searchRollView.setCloseListener(mapCloseListener);
		
		
		contant_us_bankRollContainerManager = new BankRollContainerManager() {

			@Override
			public void createUiByData() {
			}

			@Override
			public void onShow(ShowAble showAble) {
				contant_us_bankRollContainerManager.updateArrowState();
//				if (((BankRollView) showAble).getContent() == maplayout) {
//					maplayout.myLocation.performClick();
//				} else if (((BankRollView) showAble).getContent() == layout) {
//
//				}
			}

			@Override
			public void onShow() {
			}

		};
		contant_us_bankRollContainerManager
				.setRollContainer(contantBankRollContainer);
		contantBankRollContainer
				.setBankRollContainerManager(contant_us_bankRollContainerManager);
	}
	
	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

	ListView helpListView;
	ViewGroup helps_layout;
	List<HelpItemListModel> helpTitles = new ArrayList<HelpItemListModel>();
	int position = -1;

	private void offlineTest() {
        helpTitles.add(new HelpItemListModel("activation"));
        helpTitles.add(new HelpItemListModel("home"));
        helpTitles.add(new HelpItemListModel("mobile token"));
        helpTitles.add(new HelpItemListModel("login"));
        helpTitles.add(new HelpItemListModel("menu"));
        helpTitles.add(new HelpItemListModel("account"));
        helpTitles.add(new HelpItemListModel("payee list"));
        helpTitles.add(new HelpItemListModel("debit report"));

        helpTitles.add(new HelpItemListModel("activation"));
        helpTitles.add(new HelpItemListModel("home"));
        helpTitles.add(new HelpItemListModel("mobile token"));
        helpTitles.add(new HelpItemListModel("login"));
        helpTitles.add(new HelpItemListModel("menu"));
        helpTitles.add(new HelpItemListModel("account"));
        helpTitles.add(new HelpItemListModel("payee list"));
        helpTitles.add(new HelpItemListModel("debit report"));
    }
	
	private void initHelp() {
		helps_layout = (ViewGroup) findViewById(R.id.helps);
		helpListView = (ListView) findViewById(R.id.help_list_view);
		helpListView.setAdapter(new HelpListAdapter());

		helpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
				for (int i = 0; i < parent.getChildCount(); i++) {
					View child = parent.getChildAt(i);
					child.setBackgroundDrawable(null);
				}
				MainActivity.this.position = position;
				HelpItemListModel helpItemListModel = helpTitles.get(position);

				initHelpContent(helpItemListModel.getTitle(),helpItemListModel.getText());
				showView(helpDetailContainer);
				setCanOrientation(false);
				addToBackStack(HELP_CONTENT);
				view.setBackgroundResource(R.drawable.list_item_over);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						view.setBackgroundDrawable(null);
					}
				}, 500);
			}
		});
	}

	LinearLayout helpDetailContainer;

	private void initHelpContent(String title, String content) {
		helpDetailContainer = (LinearLayout) findViewById(R.id.help_detail_container);
		TextView titleText = (TextView) findViewById(R.id.help_detail_title);
		TextView contentText = (TextView) findViewById(R.id.help_detail_content);
		titleText.setText(title);
		contentText.setText(content);
	}

	void getHelpList() {
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		progressOverlay.show(getResources().getString(R.string.waiting),
				new OnProgressEvent() {
					@Override
					public void onProgress() {
						String postData = HelpItemJson.HelpItemReportProtocal(Contants.publicModel);
						HttpConnector httpConnector = new HttpConnector();
						String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
						final GetHelpItemResponseModel getHelpItemResponse = HelpItemJson.ParseHelpItemResponse(httpResult);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (getHelpItemResponse != null && getHelpItemResponse.responsePublicModel.isSuccess()) {
									if (getHelpItemResponse.getHelpItemList() != null) {
										helpTitles = getHelpItemResponse.getHelpItemList();
									} else {

									}
									BaseAdapter baseAdapter = (BaseAdapter) helpListView.getAdapter();
									baseAdapter.notifyDataSetChanged();
								} else {
									if (getHelpItemResponse != null) {
										displayErrorMessage(getHelpItemResponse.responsePublicModel.eventManagement.getErrorCode(),getHelpItemResponse.responsePublicModel.eventManagement.getErrorDescription());
									}
								}
							}
						});
					}
				});
	}

	class HelpListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return helpTitles.size();
		}

		@Override
		public Object getItem(int position) {
			return helpTitles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater
						.from(MainActivity.this);

				convertView = (View) inflater.inflate(R.layout.help_item, null);
			}
			TextView text = (TextView) convertView
					.findViewById(R.id.help_title_text);

			text.setText(helpTitles.get(position).getTitle());
			return convertView;
		}

	}

	ViewGroup loansLayout;

	BankRollContainer investmentsBankRollContainer;

	ViewGroup investmentLayout;

	ViewGroup accountsRotateLayout, cardsRotateLayout, loansRotateLayout;

	public InvestmentsLayoutManager investmentsLayoutManager;
	public BankRollView depositesRollView, assetRollView;

	private void initInvestments() {

		investmentsBankRollContainer = (BankRollContainer) findViewById(R.id.investments);
		investmentLayout = (ViewGroup) findViewById(R.id.investments_layout);
		investmentsBankRollContainer.init();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		depositesRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		depositesRollView.init();
		investmentsBankRollContainer.addShowAble(depositesRollView);
		depositesRollView
				.setCloseImage(R.drawable.sphere_investments_top_active);
		depositesRollView
				.setShowImage(R.drawable.sphere_investments_lower_active);
		depositesRollView.setBallIcon(R.drawable.investment_deposits_icon);
		depositesRollView.setBallName(R.string.deposit_1);

		assetRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		assetRollView.init();
		investmentsBankRollContainer.addShowAble(assetRollView);
		assetRollView.setBallIcon(R.drawable.investment_asset_management_icon);
		assetRollView.setBallName(R.string.assets_management_1);
		assetRollView.setCloseImage(R.drawable.sphere_investments_top_active);
		assetRollView.setShowImage(R.drawable.sphere_investments_lower_active);

		investmentsLayoutManager = new InvestmentsLayoutManager();
		investmentsLayoutManager.setRollContainer(investmentsBankRollContainer);
	}

	public LoansLayout loansLayoutContainer;

	private void initLoansLayout() {
		loansLayout = (ViewGroup) findViewById(R.id.loans);
		loansLayoutContainer = (LoansLayout) findViewById(R.id.loans_layout);
	}

	InvestmentRotateLayoutManager investmentRotateLayoutManager;

	ViewGroup investmentsRotateLayout;

	private void initInvestmentRotateLayout() {
		investmentsRotateLayout = (ViewGroup) findViewById(R.id.investments_rotate_layout);

		ViewGroup accountRotateContainer = (ViewGroup) investmentsRotateLayout
				.findViewById(R.id.rotate_container);
		investmentRotateLayoutManager = new InvestmentRotateLayoutManager();
		investmentRotateLayoutManager.setContainer(accountRotateContainer);
	}

	private void showInvestmentsRotateLayout() {
		showView(investmentsRotateLayout);
		investmentRotateLayoutManager.onShow();
		investmentRotateLayoutManager.updateArrowState();
	}

	AccountRotateLayoutManager accountRotateLayoutManager;

	CardsRotateLayoutManager cardsRotateLayoutManager;

	LoansRotateLayoutManager loansRotateLayoutManager;

	private void initRotateLayout() {
		cardsRotateLayout = (ViewGroup) findViewById(R.id.crads_rotate_layout);
		accountsRotateLayout = (ViewGroup) findViewById(R.id.accounts_rotate_layout);
		ViewGroup cardRotateContainer = (ViewGroup) cardsRotateLayout
				.findViewById(R.id.rotate_container);

		ViewGroup accountRotateContainer = (ViewGroup) accountsRotateLayout
				.findViewById(R.id.rotate_container);
		accountRotateLayoutManager = new AccountRotateLayoutManager();
		accountRotateLayoutManager.setContainer(accountRotateContainer);

		cardsRotateLayoutManager = new CardsRotateLayoutManager();
		cardsRotateLayoutManager.setContainer(cardRotateContainer);

		// loans
		loansRotateLayout = (ViewGroup) findViewById(R.id.loans_rotate_layout);
		ViewGroup loansContainer = (ViewGroup) loansRotateLayout
				.findViewById(R.id.rotate_container);
		loansRotateLayoutManager = new LoansRotateLayoutManager();
		loansRotateLayoutManager.setContainer(loansContainer);

	}

	private void showCardsRotateLayout() {
		showView(cardsRotateLayout);
		cardsRotateLayoutManager.onShow();
		cardsRotateLayoutManager.updateArrowState();
		cardsRotateLayoutManager.setCanOrientation();
	}

	private void showAccountsRotateLayout() {
		showView(accountsRotateLayout);
		accountRotateLayoutManager.onShow();
		accountRotateLayoutManager.updateArrowState();
	}

	private void showLoanssRotateLayout() {
		showView(loansRotateLayout);
		loansRotateLayoutManager.onShow();
		loansRotateLayoutManager.updateArrowState();
	}

	public void showCards() {
		cardsLayoutManager.onShow();
		cardsLayoutManager.updateArrowState();
	}

	public void showAccounts() {
		accountsLayout.onShow();
		accountsLayout.updateArrowState();
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
		loansLayoutContainer.updateArrowState();
	}

	public void showHelpList() {
		if (helpTitles.size() <= 0) {
			getHelpList();
		}
//		offlineTest();
		showView(helps_layout);
		setCanOrientation(false);
	}
	
	public void showContant() {
		if(contant_us_layout == null){
			initContant();
		}
		
		showView(contant_us_layout);
		contant_us_bankRollContainerManager.onShow();
		setCanOrientation(false);
		contant_us_bankRollContainerManager.setArrowThreadhold(1);
		contant_us_bankRollContainerManager.setFlag(true);
		contant_us_bankRollContainerManager.updateArrowState();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				contant_us_bankRollContainerManager.updateArrowState();
			}
		}, 1000);
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

	ImageView investments, accounts, cards, loans;

	private void doAnimation() {
		final Animation anim_bottom_right = AnimationUtils.loadAnimation(this,
				R.anim.bottom_right_to_center);
		final Animation anim_bottom_left = AnimationUtils.loadAnimation(this,
				R.anim.bottom_left_to_center);
		final Animation anim_top_left = AnimationUtils.loadAnimation(this,
				R.anim.top_left_to_center);
		final Animation anim_top_right = AnimationUtils.loadAnimation(this,
				R.anim.top_right_to_center);

		anim_bottom_right.setFillAfter(true);
		anim_bottom_left.setFillAfter(true);
		anim_top_left.setFillAfter(true);
		anim_top_right.setFillAfter(true);

		accounts.startAnimation(anim_top_left);
		cards.startAnimation(anim_top_right);
		investments.startAnimation(anim_bottom_left);
		loans.startAnimation(anim_bottom_right);
	}

	private void enableTab1Buttons() {
		tab1 = (LinearLayout) tabHost.findViewById(R.id.tab1);

		accounts = (ImageView) tab1.findViewById(R.id.splash_accounts);
		if (Contants.baseAccounts == null || Contants.baseAccounts.size() == 0) {

			if (isOffline) {
				accounts.setOnClickListener(this);
			} else {
				accounts.setImageBitmap(Utils.getBitmap(this,
						R.drawable.btn_account_disable));
			}

		} else {
			accounts.setOnClickListener(this);
		}

		investments = (ImageView) tab1.findViewById(R.id.splash_investments);
		if (isOffline) {
			investments.setOnClickListener(this);
		}
		if (Contants.investmentAccounts == null
				|| Contants.investmentAccounts.size() == 0) {
			investments.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_investimenti_disable));
		} else {
			investments.setOnClickListener(this);
		}

		cards = (ImageView) tab1.findViewById(R.id.splash_cards);
		if (isOffline) {
			cards.setOnClickListener(this);
		}
		if (Contants.cardAccounts == null || Contants.cardAccounts.size() == 0) {
			cards.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_carte_disable));
		} else {
			cards.setOnClickListener(this);
		}

		loans = (ImageView) tab1.findViewById(R.id.splash_loans);
		if (isOffline) {
			loans.setOnClickListener(this);
		}
		if (Contants.loansAccounts == null
				|| Contants.loansAccounts.size() == 0) {
			loans.setImageBitmap(Utils.getBitmap(this,
					R.drawable.btn_finanziamenti_disable));
		} else {
			loans.setOnClickListener(this);
		}
	}

	private void initTab1() {
		enableTab1Buttons();

		showSynthesis();
		doAnimation();
	}

	public AccountsLayout accountsLayout;

	private void initTab2() {

		tab2 = (LinearLayout) tabHost.findViewById(R.id.tab2);
		accountsLayout = (AccountsLayout) tab2
				.findViewById(R.id.accounts_layout);
	}

	public BankRollView creditRollView;

	public BankRollView prepaidRollView;

	public BankRollView ibanRollView;

	private void initTab3() {
		tab3 = (BankRollContainer) tabHost.findViewById(R.id.tab3);
		tab3.init();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		creditRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		creditRollView.init();
		tab3.addShowAble(creditRollView);
		creditRollView.setCloseImage(R.drawable.sfera_carte_superiore);
		creditRollView.setShowImage(R.drawable.sfera_carte_inferiore);
		creditRollView.setBallIcon(R.drawable.credit_icon);
		creditRollView.setBallName(R.string.object_credit_cards);

		ibanRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		ibanRollView.init();
		tab3.addShowAble(ibanRollView);
		ibanRollView.setCloseImage(R.drawable.sfera_carte_superiore);
		ibanRollView.setShowImage(R.drawable.sfera_carte_inferiore);
		ibanRollView.setBallIcon(R.drawable.credit_icon);
		ibanRollView.setBallName(R.string.carte_iban);

		prepaidRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		prepaidRollView.init();
		tab3.addShowAble(prepaidRollView);
		prepaidRollView.setCloseImage(R.drawable.sfera_carte_superiore);
		prepaidRollView.setShowImage(R.drawable.sfera_carte_inferiore);
		prepaidRollView.setBallIcon(R.drawable.credit_icon);
		prepaidRollView.setBallName(R.string.prepaid_cards1);

		cardsLayoutManager = CardsLayoutManager.create();
		cardsLayoutManager.setRollContainer(tab3);
	}

	public CardsLayoutManager cardsLayoutManager;

	public BankSlidContainer bankSlidContainer;

	private void initTab4() {
		tab4 = (BankRollContainer) tabHost.findViewById(R.id.tab4);
		tab4.init();

		if (Contants.getUserInfo != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList() != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList()
						.size() > 0) {
			List<AccountsModel> accountList = Contants.getUserInfo
					.getUserprofileHb().getAccountList();
			for (int i = 0; i < accountList.size(); i++) {
				if (accountList.get(i).getIsInformative().equals("N")) {
					Contants.payment_disabled = false;
					break;
				}
			}
		} else {
			Contants.payment_disabled = true;
		}

		if (Contants.payment_disabled) {
			imageView4.setImageDrawable(getResources().getDrawable(
					R.drawable.label_btn_payments_disable));
		}

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		BankRollView paymentRoll = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		paymentRoll.init();
		paymentRoll.setShowImage(R.drawable.sfera_bonifico_inferiore);
		paymentRoll.setCloseImage(R.drawable.sfera_bonifico_superiore);
		paymentRoll.setBallIcon(R.drawable.new_payment_icon);
		paymentRoll.setBallName(R.string.new_payee_title);
		tab4.addShowAble(paymentRoll);
		newVersionNewPaymentListener = new com.accenture.mbank.view.protocol.ShowListener() {
			@Override
			public void onShow(ShowAble showAble) {
				Intent intent = new Intent(MainActivity.this,
						BPERPaymentMenu.class);
				startActivity(intent);
			}
		};
		paymentRoll.setShowListener(newVersionNewPaymentListener);

		/*
		 * Recent payment
		 */
		BankRollView recentPaymentRoll = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		recentPaymentRoll.init();
		recentPaymentRoll.setShowImage(R.drawable.sfera_bonifico_inferiore);
		recentPaymentRoll.setCloseImage(R.drawable.sfera_bonifico_superiore);
		recentPaymentRoll.setBallIcon(R.drawable.recent_icon);
		recentPaymentRoll.setBallName(R.string.recent_payment);
		tab4.addShowAble(recentPaymentRoll);
		newVersionRecentPaymentListener = new com.accenture.mbank.view.protocol.ShowListener() {
			@Override
			public void onShow(ShowAble showAble) {
				BPERRecentPaymentMain.start(MainActivity.this);
			}
		};
		recentPaymentRoll.setShowListener(newVersionRecentPaymentListener);

		/*
		 * Recent deposit
		 */
		BankRollView recentDepositsRoll = (BankRollView) layoutInflater
				.inflate(R.layout.bank_roll_view, null);
		recentDepositsRoll.init();
		recentDepositsRoll.setShowImage(R.drawable.sfera_bonifico_inferiore);
		recentDepositsRoll.setCloseImage(R.drawable.sfera_bonifico_superiore);
		recentDepositsRoll.setBallIcon(R.drawable.icon_black_13);
		recentDepositsRoll.setBallName(R.string.recent_deposits1);
		tab4.addShowAble(recentDepositsRoll);

		if (Contants.getUserInfo != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList() != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList()
						.size() > 0) {
			for (AccountsModel accountModel : Contants.getUserInfo
					.getUserprofileHb().getAccountList()) {
				if (accountModel.getPortafoglio().equals("Y")
						&& Contants.getUserInfo.getUserprofileHb().getTypeSB()
								.equals("SB")) {
					isPortafoglio = true;
				}
			}
		}
		if (isPortafoglio) {
			newVersionRecentPaymentListener1 = new com.accenture.mbank.view.protocol.ShowListener() {
				@Override
				public void onShow(ShowAble showAble) {
					Intent intent = new Intent(MainActivity.this,
							RecentDepositActivity.class);
					startActivity(intent);
				}
			};
		} else {
			recentDepositsRoll
					.setShowImage(R.drawable.sfera_bonifico_inferiore_disattivo);
			recentDepositsRoll
					.setCloseImage(R.drawable.sfera_bonifico_superiore_disattivo);
		}

		recentDepositsRoll.setShowListener(newVersionRecentPaymentListener1);
	}

	private boolean isPortafoglio = false;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == investments) {
			investmentRotateLayoutManager.setAccount(Contants.accountsList);
			showInvestmentsRotateLayout();
		} else if (v == cards) {
			showCardsRotateLayout();
		} else if (v == loans) {
			showLoanssRotateLayout();
		} else if (v == accounts) {
			showAccountsRotateLayout();
		}

		if (v == userInfoBtn || v == menu_investmentsBtn || v == menu_loansBtn
				|| v == investments || v == cards || v == loans
				|| v == accounts || v == menu_synthesisBtn || v == menu_contactsBtn){
			addToBackStack(v);
		}else if(v == help){
			addToBackStack(HELP);
		}
	}

	public void showTab(int index) {
		if (tabHost.getCurrentTab() == index) {
			if (index == CARDS) {
				showCards();
			} else if (index == ACCOUNTS) {
				showAccounts();
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
			src = R.drawable.label_btn_synthesis_over;
			imageView = imageView1;
			visible = tab1;
			showSynthesis();
			break;
		case ACCOUNTS:
			src = R.drawable.label_btn_accounts_over;
			imageView = imageView2;
			visible = tab2;
			showAccounts();
			break;
		case CARDS:
			src = R.drawable.label_btn_cards_over;
			imageView = imageView3;
			visible = tab3;
			showCards();
			break;
		case PAYMENTS:
			imageView = imageView4;
			src = R.drawable.label_btn_payments_over;
			visible = tab4;
			break;
		default:
			break;
		}
		hideKeyboard(visible);
		imageView.setImageDrawable(getResources().getDrawable(src));
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setCanOrientation(false);
		onScreenChange(visible);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogManager.d("Main onConfigurationChanged");
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			LogManager
					.d("Main onConfigurationChanged new ORIENTATION_LANDSCAPE");

			String chartTypeTemp = "";
			if (accountsRotateLayout == currentScreen) {
				chartTypeTemp = "ACCOUNTS";
			} else if (cardsRotateLayout == currentScreen) {
				chartTypeTemp = "CARDS";
			} else if (investmentsRotateLayout == currentScreen) {
				chartTypeTemp = "INVESTMENTS";
			} else if (loansRotateLayout == currentScreen) {
				chartTypeTemp = "LOANS";
			}

			final String chartType = chartTypeTemp;

			if (!chartType.equals("")) {
				/*
				 * Need to do Animation back from chart window. Will handle
				 * later onResume.
				 */
				mNeedFadeIn = true;
				setCanOrientation(false);

				ViewGroup mainWindow = (ViewGroup) findViewById(R.id.main_window);
				setHideAnimation(mainWindow, Contants.TIME_FADE_OUT_ANIMATION,
						new AnimationListener() {

							@Override
							public void onAnimationEnd(Animation animation) {
								Intent intent = new Intent(MainActivity.this,
										ChartActivity.class);

								intent.putExtra("CURRENT_SCREEN", chartType);
								startActivity(intent);
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationStart(Animation animation) {
							}
						});
			}
		}
		super.onConfigurationChanged(newConfig);
	}
	
	public boolean isPopupVisible() {
		return isPopupVisible;
	}
	public void setPopupVisible(boolean isPopupVisible) {
		this.isPopupVisible = isPopupVisible;
	}
	
	public View getSearchPoint() {
		return searchPoint;
	}

	public void setSearchPoint(LinearLayout searchPoint) {
		this.searchPoint = searchPoint;
	}
	
	public void initializeMarkerWindowMap() {

		 map.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(final Marker arg0) {
	            	
	            	//position of popup window
	            	arg0.setInfoWindowAnchor(5.5f, -0.1f);
	            	BranchListModel brankBranchListModel = markBrankBranchList.get(arg0);
	            	if (brankBranchListModel != null) {
	            		mPopView.setBranchListModel(brankBranchListModel);
	            		ImageButton directionBtn = (ImageButton)mPopView.findViewById(R.id.getdirections);
	            		// Setting custom OnTouchListener which deals with the pressed state
	                    // so it shows up 
//	                    infoButtonListener = new OnInfoWindowElemTouchListener(directionBtn,
//	                            getResources().getDrawable(R.drawable.btn_get_directions),
//	                            getResources().getDrawable(R.drawable.btn_get_directions)) 
//	                    {
//	                        @Override
//	                        protected void onClickConfirmed(View v, Marker marker) {
//	                            // Here we can perform some action triggered after clicking the button
//	                            Toast.makeText(ContactSearchActivity.this, "'s button clicked!", Toast.LENGTH_SHORT).show();
//	                        }
//	                    }; 
//	                    directionBtn.setOnTouchListener(infoButtonListener);
	                    
//	            		directionBtn.setOnClickListener(new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+ map.getMyLocation().getLatitude()+","+map.getMyLocation().getLongitude()+"&daddr="+arg0.getPosition().latitude+","+arg0.getPosition().longitude;
//								Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//								intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//								ContactSearchActivity.this.startActivity(intent);
//							}
//						});
	                 // MapWrapperLayout initialization
	                    // 39 - default marker height
	                    // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
//	                    mapWrapperLayout.init(map, getPixelsFromDp(ContactSearchActivity.this, 39 + 20)); 
//	            		mapWrapperLayout.setMarkerWithInfoWindow(arg0, mPopView);
	            		return mPopView;
					} else{
						return null;
					}
	            		
	                
	            }

	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker arg0) {

	            	return null;
	            }
	        });
		 	searchPoint.setVisibility(View.GONE);
	        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					setPopupVisible(true);
		    	  	searchPoint.setVisibility(View.GONE);
		    	  	
		    	  	String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+ myLocationLatLng.latitude+","+myLocationLatLng.longitude+"&daddr="+arg0.getPosition().latitude+","+arg0.getPosition().longitude;
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
					MainActivity.this.startActivity(intent);

				}
			});
	 }
	
	public void searchBarch(String keyText,LatLng pt) {
		if (pt != null) {
		} else {
			return;
		}
		double latitude = pt.latitude;
		double longitude = pt.longitude;
		
		int distance = 15000;
		String distanceInput = distance_input.getText().toString();
		if (distance_input.getVisibility() == View.VISIBLE && !distanceInput.equals("")) {
			try {
				distance = Integer.parseInt(distanceInput);
			} catch (Exception e) {
			}
		}
		String postData = GetBranchListJson.GetBranchListReportProtocal(Contants.publicModel, latitude, longitude, distance, keyText);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.public_mobile_url, postData, this);
		GetBranchListResponseModel getBranchListResponse = GetBranchListJson.ParseGetBranchListResponse(httpResult);

		if (getBranchListResponse == null) {
			return;
		}
		branchListModels = new ArrayList<BranchListModel>(getBranchListResponse.getBranchList());
		if (branchListModels.size() > 0) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					addbanks();
				}
			});
		}
	}
	
	private void addbanks() {
		map.clear();
		LatLng mapCenter = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
		map.addMarker(new MarkerOptions()
	     .icon(BitmapDescriptorFactory.fromBitmap(icon))
	     .position(mapCenter)
	     .flat(false));
		
		markBrankBranchList = new HashMap<Marker, BranchListModel>();
		map.setOnMarkerClickListener(markerClickListener);
		if (branchListModels != null) {
			for (BranchListModel brankBranchListModel : branchListModels) {
				addBank(brankBranchListModel);
			}
		}
		initializeMarkerWindowMap();
	}
	private void addBank(BranchListModel brankBranchListModel) {
		
		LatLng mapMarker = new LatLng(brankBranchListModel.getLatitude(), brankBranchListModel.getLongitude());
		
			Marker marker =map.addMarker(new MarkerOptions()
	     .icon(BitmapDescriptorFactory.fromBitmap(iconBank))
	     .position(mapMarker)
	     .flat(false));
		
		 markBrankBranchList.put(marker, brankBranchListModel);
		
	}
	
	private void goToMyLocation() {
		myLocationLatLng = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 13));
	}
	
	private LatLng searchLocation(String locationName) {
		if (locationName == null || locationName.equals("")) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					goToMyLocation();
				}
			});
			return myLocationLatLng;
		}
		
		Geocoder geocoder = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocationName(locationName, 1);
	        LatLng geoPoint = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            Message message = httpSearchHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = HttpConnectionUtil.SUCCESS_GEOCODER;
            b.putDoubleArray("response", new double[]{addresses.get(0).getLatitude(),addresses.get(0).getLongitude()});
            message.setData(b);
            httpSearchHandler.sendMessage(message);
	        return geoPoint;
		} catch (IOException e) {
			Message message = httpSearchHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = HttpConnectionUtil.ERROR;
            message.setData(b);
            httpSearchHandler.sendMessage(message);
			return null;
		}
		
	}
	public static void hideSoftKeyboard (Activity activity, View view) 
	{
	    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

}
