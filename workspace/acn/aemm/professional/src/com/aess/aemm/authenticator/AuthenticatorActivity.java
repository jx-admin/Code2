package com.aess.aemm.authenticator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aess.aemm.AEMMConfig;
import com.aess.aemm.R;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.receiver.AemmDeviceAdminReceiver;
import com.aess.aemm.update.Update;

public class AuthenticatorActivity extends AccountAuthenticatorActivity
		implements View.OnClickListener {

	/** The Intent flag to confirm credentials. **/
	public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

	/** The Intent extra to store password. **/
	public static final String PARAM_PASSWORD = "password";

	/** The Intent extra to store username. **/
	public static final String PARAM_USERNAME = "username";

	/** The Intent extra to store authtoken type. **/
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

	public static final int DEVICE_MAG = 15;

	private static final String TAG = "AuthenticatorActivity";

	private AccountManager mAccountManager;

	private Thread mAuthThread;

	private String mAuthtoken;

	private String mAuthtokenType;

	private Boolean mConfirmCredentials = false;

	private final Handler mHandler = new Handler();

	private TextView mMessage;
	private boolean mImeiLogin;
	private EditText mPasswordEdit;
	private EditText mUsernameEdit;
	private EditText mImeiEdit;
	private Button mLoginBtn;
	private Button mCancelBtn;
	private CheckBox mImsiSelect;

	private String mPassword = null;
	private String mUsername = null;
	private String mDomainname = null;
	private String mImei = null;
	private String mImsi = null;

	protected boolean mRequestNewAccount = false;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.login);

		init();
	}

	@Override
	protected void onStart() {
		startDeviceAdmin(this);

		clearConfigFile(this);

		getImeiAndImsiValue();

		setImsiInput();
		setImeiInput();

		super.onStart();
	}

	public static void clearConfigFile(Context cxt) {

//		CommUtils.setDomain(cxt, "");
		CommUtils.setIMEI(cxt, "");
		CommUtils.setIMSI(cxt, "");

		CommUtils.setSessionId(cxt, "");
	}

	private void init() {
		mAccountManager = AccountManager.get(this);

		final Intent intent = getIntent();
		mUsername = intent.getStringExtra(PARAM_USERNAME);
		mAuthtokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);

		mRequestNewAccount = (mUsername == null ? true : false);

		mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS,
				false);

		mMessage = (TextView) findViewById(R.id.tv_account);

		mUsernameEdit = (EditText) findViewById(R.id.et_account);
		mUsernameEdit.setText(mUsername);
		mPasswordEdit = (EditText) findViewById(R.id.et_password);

		mLoginBtn = ((Button) findViewById(R.id.login_sure_btn));
		mLoginBtn.setOnClickListener(this);
		mCancelBtn = (Button) findViewById(R.id.login_cancel_btn);
		mCancelBtn.setOnClickListener(this);

		mImsiSelect = (CheckBox) findViewById(R.id.imsiselect);

		mImsiSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//				CommUtils.setDomain(AuthenticatorActivity.this, "");
				mDomainname = null;
				setImsiInput();
			}
		});
	}

	private void setImeiInput() {
		if (mImei == null || mImei.length() < 1) {
			TextView tv_imei = (TextView) findViewById(R.id.tv_imei);
			mImeiEdit = (EditText) findViewById(R.id.et_imei);

			mImeiEdit.setEnabled(true);
			mImeiEdit.setVisibility(View.VISIBLE);
			tv_imei.setVisibility(View.VISIBLE);
		}
	}

	private void setImsiInput() {
		int type = mImsiSelect.isChecked() ? 1 : 0;
		int error = 0;
		if (1 == type) {
			mMessage.setText(R.string.selectimsi);

			if (null != mImsi) {
				String begin = mImsi.substring(0, 5);
				int satrnum = mImsi.length() - 2;
				String end = mImsi.substring(satrnum);
				end = begin + "****" + end;
				mUsernameEdit.setText(end);
				mUsernameEdit.setEnabled(false);
				mPasswordEdit.setFocusable(true);
				mPasswordEdit.requestFocus();
				return;
			} else {
				mImsiSelect.toggle();
				error = -1;
			}
		}

		if (error < 0) {
			mMessage.setText(R.string.askinputuser);
		} else {
			mMessage.setText(R.string.name_str);
		}
		mUsernameEdit.setText("");
		mUsernameEdit.setFocusable(true);
		mUsernameEdit.requestFocus();
		mUsernameEdit.setEnabled(true);
		mPasswordEdit.setText("");
	}

	private void getImeiAndImsiValue() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mImsi = tm.getSubscriberId();
		mImei = tm.getDeviceId();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEVICE_MAG) {
			if (Activity.RESULT_CANCELED == resultCode) {
				startDeviceAdmin(this);
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.ui_activity_authenticating));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Log.i(TAG, "dialog cancel has been invoked");
				if (mAuthThread != null) {
					mAuthThread.interrupt();
					mAuthThread = null;
					finish();
				}
			}
		});
		return dialog;
	}

	private void clearUserInput() {
		mPassword = null;
		mUsername = null;
		mDomainname = null;

		AutoAdress ad = AutoAdress.getInstance(this);
		ad.clear();
	}

	public void handleLogin(View view) {
		clearUserInput();

		if (mRequestNewAccount) {
			if (mImsiSelect.isChecked()) {
				mUsername = mImsi;
			} else {
				mUsername = mUsernameEdit.getText().toString();
			}
		}
		mPassword = mPasswordEdit.getText().toString();

		if ((null == mImei || mImei.length() < 1) && (null != mImeiEdit)) {
			mImei = mImeiEdit.getText().toString();
		}
		mImeiLogin = mImsiSelect.isChecked();
		if (!mImeiLogin) {
			if (null != mUsername) {
				String[] keys = mUsername.split("@");
				if (null != keys) {
					if (keys.length > 0) {
						mUsername = keys[0];
					}
					if (keys.length > 1) {
						mDomainname = keys[1];
					}
				}
			}
		}

		if (1 == AEMMConfig.imsiCheck && TextUtils.isEmpty(mImsi)) {
			mMessage.setText(getMessage());
			return;
		}

		if (!mImeiLogin) {
			if (TextUtils.isEmpty(mDomainname)) {
				mMessage.setText(getMessage());
				return;
			}
		}

		if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)
				|| TextUtils.isEmpty(mImei)) {
			mMessage.setText(getMessage());
			return;
		}

		showProgress();

		if (null != mImei) {
			CommUtils.setIMEI(this, mImei);
		}

		mAuthThread = new Thread() {
			public void run() {
				Context cxt = AuthenticatorActivity.this;

				AutoAdress ad = AutoAdress.getInstance(cxt);
				if (!mImeiLogin) {
					CommUtils
							.setDomain(AuthenticatorActivity.this, mDomainname);
				}
				result = ad.init();
				if (result > 0) {
					result = Update.doLogin(cxt, mUsername, mPassword,
							Update.LOGIN);
				} else {
					result = NetUtils.F_LOGIN_ADDRESS_ERROR;
					if (!mImeiLogin) {
						CommUtils.setDomain(AuthenticatorActivity.this, "");
					}
				}

				mHandler.post(new Runnable() {
					public void run() {
						onAuthenticationResult(result);
					}
				});
			}
		};
		mAuthThread.setName("AuthThread");
		mAuthThread.start();
	}

	public String parserLoginResult(FileInputStream inStream) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();
			if (!root.getAttribute("state").equals("ok"))
				return null;
			return root.getAttribute("sid");
		} catch (FileNotFoundException e1) {
		} catch (Exception e) {
		}
		return null;
	}

	private void finishConfirmCredentials(boolean result) {
		Log.i(TAG, "finishConfirmCredentials()");
		final Account account = new Account(mUsername, Constants.ACCOUNT_TYPE);
		mAccountManager.setPassword(account, mPassword);
		final Intent intent = new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void finishLogin() {
		Log.i(TAG, "finishLogin()");

		if (1 == AEMMConfig.imsiCheck) {
			CommUtils.setIMSI(AuthenticatorActivity.this, mImsi);
		}

		CommUtils.setIMEI(AuthenticatorActivity.this, mImei);

		final Account account = new Account(mUsername, Constants.ACCOUNT_TYPE);
		if (mRequestNewAccount) {
			mAccountManager.addAccountExplicitly(account, mPassword, null);

			ContentResolver.setSyncAutomatically(account,
					Constants.AUTHTOKEN_TYPE, true);

		} else {
			ContentResolver.setSyncAutomatically(account,
					Constants.AUTHTOKEN_TYPE, true);
			mAccountManager.setPassword(account, mPassword);
		}
		final Intent intent = new Intent();

		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
		mAuthtoken = CommUtils.getSessionId(this);
		if (mAuthtokenType != null
				&& mAuthtokenType.equals(Constants.AUTHTOKEN_TYPE)) {
			intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
		}
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void hideProgress() {
		try {
			dismissDialog(0);
		} catch (Exception e) {
		}
	}

	public void onClick(View v) {
		if (mLoginBtn == v) {
			handleLogin(v);
		} else if (mCancelBtn == v) {
			finish();
		}
	}

	public void onAuthenticationResult(int result) {

		Log.w(TAG, "onAuthenticationResult(" + result + ")");
		hideProgress();
		if (result == 0) {
			if (!mConfirmCredentials) {
				finishLogin();
			} else {
				finishConfirmCredentials(true);
			}
		} else {
			Log.e(TAG, "onAuthenticationResult: failed to authenticate");
			String info = NetUtils.getErrorString(AuthenticatorActivity.this,
					result);
			mMessage.setText(info);
		}
	}

	private CharSequence getMessage() {
		if (TextUtils.isEmpty(mUsername)) {
			return getText(R.string.login_activity_newaccount_text);
		}
		if (TextUtils.isEmpty(mPassword)) {
			return getText(R.string.login_activity_loginfail_text_pwmissing);
		}
		if (TextUtils.isEmpty(mDomainname)) {
			return getText(R.string.domainerr);
		}
		if (TextUtils.isEmpty(mImei)) {
			return getText(R.string.inputimei_hint);
		}
		if (TextUtils.isEmpty(mImsi)) {
			return getText(R.string.inputimsi_hint);
		}
		return null;
	}

	private void showProgress() {
		showDialog(0);
	}

	private void startDeviceAdmin(Activity aty) {
		boolean need = false;
		DevicePolicyManager dpm = (DevicePolicyManager) aty
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		String pname = this.getPackageName();

		List<ComponentName> dnlist = dpm.getActiveAdmins();
		if (null != dnlist) {
			for (int x = 0; x < dnlist.size(); x++) {
				ComponentName dn = dnlist.get(x);
				String tname = dn.getPackageName();
				if ((null != tname) && (tname.equals(pname))) {
					need = true;
				}

			}
		}

		if (false == need) {
			ComponentName deviceAdmin = new ComponentName(aty,
					AemmDeviceAdminReceiver.class);
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);

			aty.startActivityForResult(intent, DEVICE_MAG);
		}
	}

	private int result = 0;
}
