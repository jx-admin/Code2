package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.manager.InvestmentRotateLayoutManager;
import com.accenture.mbank.logic.AdvNewsJson;
import com.accenture.mbank.logic.LoginJson;
import com.accenture.mbank.logic.UserInfoJson;
import com.accenture.mbank.model.AdvNewsResponseModel;
import com.accenture.mbank.model.ListAdvNewsModel;
import com.accenture.mbank.model.LoginResponseModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountsLayout;
import com.accenture.mbank.view.AutoScrollTextView;
import com.accenture.mbank.view.BankImageButton;
import com.accenture.mbank.view.CardsLayoutManager;
import com.accenture.mbank.view.LoansLayout;

public class LoginActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private EditText username_input;

	private EditText password_input;

	private ImageButton login_btn;

	private ImageButton safety_btn;

	private ImageButton locate_branch_btn;

	private ImageButton watch_demo_btn;

	private ImageButton bank_logo_btn;

	private CheckBox remember;

	String postData;

	String httpResult;

	HttpConnector httpConnector;

	ResponsePublicModel responsePublic;

	TextView versionTv;

	private LinearLayout adv;

	private ImageView advImg;

	private AutoScrollTextView advContent;

	private ImageButton help;

	private BankImageButton back;

	private String marketLink;
	
	private boolean mandatoryFlag;
	private String errorMessage;

	View log_top;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences settings = this.getSharedPreferences(
				Contants.SETTING_FILE_NAME, MODE_PRIVATE);
		String strBankCode = settings.getString(Contants.BANK_CODE, "");
		if (strBankCode==null||strBankCode.equals("")) {
			Intent intent = new Intent(LoginActivity.this, PreLogin.class);
			startActivity(intent);
			finish();
		} 
		if(!Contants.abi.equals(strBankCode)){
			Contants.advNewsList = null;
			Contants.abi = strBankCode;
		}
		initAdvData();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		bank_logo_btn = (ImageButton) findViewById(R.id.bank_logo);
		bank_logo_btn.setOnClickListener(this);

		help = (ImageButton) findViewById(R.id.help_btn);
		help.setVisibility(View.GONE);

		back = (BankImageButton) findViewById(R.id.back);
		back.setVisibility(View.GONE);

		versionTv = (TextView) findViewById(R.id.version);
		// String payment = " " + getString(R.string.payment_version) + ":1";
		// if (isNewVersion) {
		// payment = " " + getString(R.string.payment_version) + ":2";
		// }
		String version = Contants.URL_NAME
				+ getResources().getString(R.string.version) + Contants.Ver;
		versionTv.setText(version);
		username_input = (EditText) findViewById(R.id.username_input);
		password_input = (EditText) findViewById(R.id.password_input);
		
		username_input.setText("62989341");//94933688 62989341 70445929 45804982  35696726 u:33330002P:33330003 
		password_input.setText("12345678");
		//55189059/11111111
		//33330001/33330001
		//50268468 12345678 
		//39630915/12345678
		//33809141/11111111 SB user
		//11898224/12345678
		//48486515/12345678
		//68657685/12345678 SB user
		//65561693
		//
		


		login_btn = (ImageButton) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);

		log_top = findViewById(R.id.log_top);
		log_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard(log_top);
			}
		});
		safety_btn = (ImageButton) findViewById(R.id.safety_btn);
		safety_btn.setOnClickListener(this);

		locate_branch_btn = (ImageButton) findViewById(R.id.locate_branch_btn);
		locate_branch_btn.setOnClickListener(this);

		watch_demo_btn = (ImageButton) findViewById(R.id.watch_demo_btn);
		watch_demo_btn.setOnClickListener(this);

		remember = (CheckBox) findViewById(R.id.remember_me);
		remember.setVisibility(View.GONE);
		remember.setOnCheckedChangeListener(this);
		getInfo();
	}
	
	public static void logout(Context context){
		Contants.logOut(); // lyandyjoe
		Contants.clearAll();
		Intent intent = new Intent(context, LoginActivity.class);
		if(context instanceof Activity){
			context.startActivity(intent);
			((Activity)context).finish();
		}else{
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	private void updateADV() {
		adv = (LinearLayout) findViewById(R.id.adv);
		adv.setOnClickListener(this);
		advImg = (ImageView) findViewById(R.id.adv_image);
		advContent = (AutoScrollTextView) findViewById(R.id.adv_content);
		updateADVBar();
	}
	private String defaultAdvContent = "";
	private void updateADVBar(){
		String str = "　　　　　　　　　　　　　　　";
		if(Contants.advNewsList == null){
			defaultAdvContent = getResources().getString(R.string.default_adv);
			advContent.setText(defaultAdvContent + str);
		}else {
			if (Contants.advNewsList.get(count).get_imageRef() != null) {
				Bitmap advImage = Contants.advNewsList.get(count).get_imageRef();
				int ratio = advImage.getWidth() / advImage.getHeight();
				int scaledWidth = advImage.getHeight() * ratio;
				LayoutParams layoutParams = advImg.getLayoutParams();
				layoutParams.width = scaledWidth;
				layoutParams.height = (int) getResources().getDimension(R.dimen.adv_image_height);
				advImg.setImageBitmap(Contants.advNewsList.get(count).get_imageRef());
			} else{
				advImg.setImageDrawable(null);
			}
			
			if (Contants.advNewsList.get(count).getTitle() != null) {
				advContent.setText(Contants.advNewsList.get(count).getTitle() + str);
			} else{
				advContent.setText("");
			}
			adv.invalidate();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateADV();
	}

	private void doLogin(final String username, final String password,
			final String strBankCode) {
		Contants.userName = username;
		Contants.publicModel.setCustomerNumber(username);
		Contants.publicModel
				.setSessionId(username + System.currentTimeMillis());

		Contants.DASHBOARD_ROTATE_ANIMATION_ACCOUNTS = true;
		Contants.DASHBOARD_ROTATE_ANIMATION_CARDS = true;
		Contants.DASHBOARD_ROTATE_ANIMATION_LOANS = true;
		
        AccountsLayout.setNeedUpdate(true);
        //CardsLayoutManager.setNeedUpdate(true);
        LoansLayout.setNeedUpdate(true);
        InvestmentRotateLayoutManager.setNeedUpdate(true);

		/*
		 * Login
		 */
		httpConnector = new HttpConnector();

		postData = "username=" + username + "&password=" + password + "&bank="
				+ strBankCode + "&chnl=" + "IBMO" + "&SITE=" + "MAPPL";
		String loginUrl = Contants.URL_PREFIX_ROME
				+ "/wps/PA_bper-sec-servlet-w/login/LoginServlet";

		httpResult = httpConnector.requestByHttpPost(loginUrl, postData,
				LoginActivity.this);

		/*
		 * Result Parse
		 */
		if (httpResult == null) {
			return;
		}

		if (httpResult.equals("")) {
			displayErrorMessage("",getResources().getString(
					R.string.service_unavailable));
			return;
		}

		LoginResponseModel loginResponse = LoginJson
				.ParseLoginResponse(httpResult);

		if (loginResponse == null || loginResponse.responsePublicModel == null) {
			displayErrorMessage("",getResources().getString(
					R.string.error_data_received));
			return;
		}

		if (loginResponse.responsePublicModel.isSuccess())
			doGetUserInfo();
		else if (loginResponse.responsePublicModel.eventManagement == null
				|| loginResponse.responsePublicModel.eventManagement
						.getErrorDescription().equals("")) {
			displayErrorMessage("",getResources().getString(
					R.string.service_unavailable));
		} else {
			String errorDes = loginResponse.responsePublicModel.eventManagement.getErrorDescription();
			errorDes = Html.fromHtml(errorDes).toString();
			displayErrorMessage(loginResponse.responsePublicModel.eventManagement.getErrorCode(), errorDes);
		}
	}

	private void doGetUserInfo() {
		postData = UserInfoJson.UserInfoReportProtocal(Contants.publicModel);
		httpConnector = new HttpConnector();
		httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
				postData, LoginActivity.this);
		Contants.getUserInfo = UserInfoJson.ParseUserInfoResponse(httpResult);

		Contants.publicModel.setToken(Contants.getUserInfo.getToken());
		if (Contants.getUserInfo.responsePublicModel.getResultCode() == 0) {
				BaseActivity.isLogin = true;
				if (isChecked) {
					saveInfo(Contants.userName, isChecked);
				} else {
					saveInfo("", false);
				}
				// 拿到accounts的列表
				Contants.accountsList = Contants.getUserInfo.getUserprofileHb()
						.getAccountList();
				Contants.setAccounts();

				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("username", Contants.userName);
				startActivity(intent);
				finish();
		} else {
			if(Contants.getUserInfo.responsePublicModel.eventManagement.getErrorCode() != null){
				if(Contants.getUserInfo.responsePublicModel.eventManagement.getErrorCode().equals("95000")){
					marketLink = Contants.getUserInfo.getMarketLink();
					mandatoryFlag = Contants.getUserInfo.isMandatoryFlag();
					errorMessage = Contants.getUserInfo.responsePublicModel.eventManagement.getErrorDescription();
					handler.sendEmptyMessage(NEW_VERSION);
				}else {
					displayErrorMessage(Contants.getUserInfo.responsePublicModel.eventManagement.getErrorCode(),Contants.getUserInfo.responsePublicModel.eventManagement
							.getErrorDescription());
				}
			}else {
				displayErrorMessage(Contants.getUserInfo.responsePublicModel.eventManagement.getErrorCode(),Contants.getUserInfo.responsePublicModel.eventManagement
					.getErrorDescription());
			}
		}
	}

	@Override
	public void onClick(View v) {
		String str = "　　　　　　　　　　　　　　　";
		if (v == adv && !advContent.getText().equals(defaultAdvContent + str)) {
			Intent intent = new Intent(LoginActivity.this,Ad_InfoActivity.class);
			intent.putExtra("INDEX", count);
			startActivityForResult(intent, 0);
		} else if (v == login_btn) {
			final String username = username_input.getText().toString().trim();
			final String password = password_input.getText().toString().trim();
			
			if (username.length() == 0) {
				displayErrorMessage("",
						getResources().getString(R.string.caution) + "\n" + getResources().getString(R.string.enter_username));
				return;
			}

			if (password.length() == 0) {
				displayErrorMessage("",
						getResources().getString(R.string.caution) + "\n" + getResources().getString(R.string.enter_password));
				return;
			}

			final SharedPreferences settings = this.getSharedPreferences(
					Contants.SETTING_FILE_NAME, MODE_PRIVATE);
			final String strBankCode = settings.getString(Contants.BANK_CODE,
					"");

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(password_input.getWindowToken(), 0);

			ProgressOverlay progressOverlay = new ProgressOverlay(this);
			if (!BaseActivity.isOffline) {
				progressOverlay.show("", new OnProgressEvent() {
					@Override
					public void onProgress() {
						doLogin(username, password, strBankCode);
					}
				});
			} else {
				Contants.DASHBOARD_ROTATE_ANIMATION_ACCOUNTS = true;
				Contants.DASHBOARD_ROTATE_ANIMATION_CARDS = true;
				Contants.DASHBOARD_ROTATE_ANIMATION_LOANS = true;

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		} else if (v == safety_btn) {
			Intent intent = new Intent(LoginActivity.this,
					SecurityInfoActivity.class);
			startActivity(intent);
		} else if (v == locate_branch_btn) {
			Intent intent = new Intent(LoginActivity.this,
					ContactSearchActivity.class);
			startActivity(intent);
		} else if (v == watch_demo_btn) {
			String url = "http://www.gruppobper.it/multimedia/demo_mobile_def_high.mp4";
			//String url = "http://www.gruppobper.it/multimedia/demo_mobile_def_high.mp4";
			// String testUrl = "http://commonsware.com/misc/test2.3gp";
			setPlayerUrl(url);
		} else if (v == bank_logo_btn) {
			Intent intent = new Intent(LoginActivity.this, PreLogin.class);
			startActivity(intent);
			finish();
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
		if (BaseActivity.isOffline)
			return;
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		progressOverlay.runBackground(new OnProgressEvent() {
			@Override
			public void onProgress() {
				// 广告新闻
				String postData = AdvNewsJson.AdvNewsReportProtocal(Contants.publicModel, Contants.abi);
				HttpConnector httpConnector = new HttpConnector();
				String httpResult = httpConnector.requestByHttpPost(Contants.public_mobile_url, postData, LoginActivity.this);
				final AdvNewsResponseModel advNewResponse = AdvNewsJson.ParseAdvNewsResponse(httpResult);
				if (advNewResponse != null && advNewResponse.responsePublicModel.isSuccess()) {
					// 判断如果error code == 950000 有新的版本
						Contants.advNewsList = advNewResponse.getListAdvNews();
						loadAdvImage(advNewResponse.getListAdvNews());
						handler.postDelayed(task, 10000);
//						handler.sendEmptyMessage(ADV_NEWS);
				} else {
					defaultAdvContent = getResources().getString(R.string.default_adv);
					Contants.advNewsList = advNewResponse.getListAdvNews();
					if (advNewResponse.responsePublicModel.eventManagement.getErrorCode().equals("95000")) {
						marketLink = advNewResponse.getMarketLink();
						mandatoryFlag = advNewResponse.isMandatoryFlag();
						errorMessage = advNewResponse.responsePublicModel.eventManagement.getErrorDescription();
						handler.sendEmptyMessage(NEW_VERSION);
					}
				}
			}
		});
	}

	private boolean run = true;
	private int count = 0;
	private Runnable task = new Runnable() {
		public void run() {
			if (run) {
				if(Contants.advNewsList!=null){
					count++;
					if(count >= Contants.advNewsList.size()){
						count = 0;
					}
				}
				handler.postDelayed(this, 10000);
			}
			handler.sendEmptyMessage(ADV_NEWS);
		}
	};
	
	public void showNewVersion(boolean mandatoryFlag, String errorMessage) {
		AlertDialog.Builder builder = new Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout linearLahyout = (LinearLayout) inflater.inflate(R.layout.message_dialog_layout, null);
		builder.setView(linearLahyout);
		final AlertDialog alertDialog = builder.create();
		Button imageButton = (Button) linearLahyout.findViewById(R.id.ok_btn);
		imageButton.setText(this.getString(R.string.new_version_update));
		Button cancel_ibtn = (Button) linearLahyout.findViewById(R.id.cancel_btn);
		cancel_ibtn.setText(this.getString(R.string.remind_me_later));
		if(mandatoryFlag){
			cancel_ibtn.setVisibility(View.GONE);
		}else {
			cancel_ibtn.setVisibility(View.VISIBLE);
		}
		
		TextView text = (TextView) linearLahyout.findViewById(R.id.message_text);
		text.setText(errorMessage);
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openBroswer();
				alertDialog.dismiss();
			}
		});
		cancel_ibtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		setDialogWidth(alertDialog, this);
		alertDialog.show();
	}

	private void openBroswer() {
		if (marketLink != null) {
			Utils.gotoUrl(marketLink, this);
		}
	}

	private void setDialogWidth(final AlertDialog alertDialog, Context context) {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()) * 3 / 4; // 设置宽度
		lp.height = (int) (display.getWidth()) * 2 / 4;
		alertDialog.getWindow().setAttributes(lp);
	}

	public void loadAdvImage(List<ListAdvNewsModel> advList) {
		for(int i = 0;i<advList.size();i++){
			HttpConnector httpConnector = new HttpConnector();
			try {
				Bitmap imageRef = BitmapFactory.decodeStream(httpConnector.getImageStream(Contants.advNewsList.get(i).getImageRef()));
				//if (imageRef != null) {
					Contants.advNewsList.get(i).set_imageRef(imageRef);
				//} else {

				//}
				Bitmap imageRefThumb = BitmapFactory.decodeStream(httpConnector.getImageStream(Contants.advNewsList.get(i).getImageRefThumb()));
				//if (imageRefThumb != null) {
					Contants.advNewsList.get(i).set_imageRefThumb(imageRefThumb);
				//} else {
					
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 实例化一个handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADV_NEWS:
//				updateADV();
				updateADVBar();
				break;
			case NEW_VERSION:
				showNewVersion(mandatoryFlag, errorMessage);
				break;
			}
		}
	};
	
	public void close(){
		
	}
}
