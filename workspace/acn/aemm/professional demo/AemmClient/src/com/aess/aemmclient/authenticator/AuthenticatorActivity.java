package com.aess.aemmclient.authenticator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aess.aemmclient.R;

/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity  implements View.OnClickListener{

    /** The Intent flag to confirm credentials. **/
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /** The Intent extra to store password. **/
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. **/
    public static final String PARAM_USERNAME = "username";

    /** The Intent extra to store authtoken type. **/
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    /** The tag used to log to adb console. **/
    private static final String TAG = "AuthenticatorActivity";

    private AccountManager mAccountManager;

    private Thread mAuthThread;

    private String mAuthtoken;

    private String mAuthtokenType;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
    private Boolean mConfirmCredentials = false;

    /** for posting authentication attempts back to UI thread */
    private final Handler mHandler = new Handler();

    private TextView mMessage;

    private String mPassword;

    private EditText mPasswordEdit;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    private String mUsername;

    private EditText mUsernameEdit;

    private EditText mImeiEdit;

    private String mImei;
    private Button mLoginBtn;
    private Button mCancelBtn;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate(" + icicle + ")");
        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);
        Log.i(TAG, "loading data from Intent");
        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mAuthtokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
        setContentView(R.layout.login);
        mMessage = (TextView) findViewById(R.id.tv_account);
        mUsernameEdit = (EditText) findViewById(R.id.et_account);
        mPasswordEdit = (EditText) findViewById(R.id.et_password);
        mUsernameEdit.setText(mUsername);
        //mUsernameEdit.setText("cxwtest");
        //mPasswordEdit.setText("qwer1234!");
        mImeiEdit = (EditText)findViewById(R.id.et_imei);

        //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//mImei = tm.getDeviceId();
        mImei = "123";
		init();
    }

	private void init(){
		TextView tv_imei = (TextView)findViewById(R.id.tv_imei);
		EditText et_imei = (EditText)findViewById(R.id.et_imei);
		mLoginBtn = ((Button)findViewById(R.id.login_sure_btn));
		mLoginBtn.setOnClickListener(this);
		mCancelBtn =(Button)findViewById(R.id.login_cancel_btn);
	    mCancelBtn.setOnClickListener(this);

	     if( mImei == null || mImei.length()==0) {
	    	 et_imei.setEnabled(true);
	     } else {
	    	 et_imei.setVisibility(View.GONE);
	    	 tv_imei.setVisibility(View.GONE);
	     }
	}

    /*
     * {@inheritDoc}
     */
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

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication.
     * 
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
        if (mRequestNewAccount) {
            mUsername = mUsernameEdit.getText().toString();
        }
        mPassword = mPasswordEdit.getText().toString();
        if(mImei == null) {
        	mImei = mImeiEdit.getText().toString();
        }
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mImei)) {
            mMessage.setText(getMessage());
        } else {
        	showProgress();

        	//Editor editor = getSharedPreferences(CommUtils.PREF_NAME, 0).edit();
        	//editor.putString(CommUtils.KEY_CONFIG_IMEI, mImei);
        	//editor.commit();

			mAuthThread = new Thread() {
				public void run() {
					final int result;
					result = NetworkAuthenticate
			           //.authenticate(mUsername, mPassword, AuthenticatorActivity.this.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(CommUtils.KEY_CONFIG_IMEI, ""), null/* Handler */, AuthenticatorActivity.this);
						.authenticate(mUsername, mPassword, "", null/* Handler */, AuthenticatorActivity.this);
					mHandler.post(new Runnable() {
			            public void run() {
			                onAuthenticationResult(result);
			            }
			        });
				}
			};
			mAuthThread.start();
        }
    }

	public String parserLoginResult(FileInputStream inStream) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();
			if(!root.getAttribute("state").equals("ok"))
				return null;
			return root.getAttribute("sid");
		} catch (FileNotFoundException e1) {
		} catch (Exception e) {
		}
		return null;
	}

    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     * 
     * @param the confirmCredentials result.
     */
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

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     * 
     * @param the confirmCredentials result.
     */
    private void finishLogin() {

        Log.i(TAG, "finishLogin()");
        final Account account = new Account(mUsername, Constants.ACCOUNT_TYPE);
        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, mPassword, null);
            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account, Constants.AUTHTOKEN_TYPE, true);
            //ContentResolver.setIsSyncable(account, Constants.AUTHTOKEN_TYPE, true);
        	//ContentResolver.setSyncAutomatically(account, "com.xuye.ThridPartyService.ThirdPartyProvider", true);
        	//ContentResolver.setSyncAutomatically(account, Calendar.AUTHORITY, true);
        } else {
        	//ContentResolver.setSyncAutomatically(account, "com.xuye.ThridPartyService.ThirdPartyProvider", true);
        	//ContentResolver.setSyncAutomatically(account, Calendar.AUTHORITY, true);
        	//ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        	ContentResolver.setSyncAutomatically(account, Constants.AUTHTOKEN_TYPE, true);
            mAccountManager.setPassword(account, mPassword);
        }
        final Intent intent = new Intent();
        mAuthtoken = getSharedPreferences("AEMM_CLIENT", 0).getString("AUTHEN_TOKEN", "");
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        if (mAuthtokenType != null && mAuthtokenType.equals(Constants.AUTHTOKEN_TYPE)) {
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Hides the progress UI for a lengthy operation.
     */
    private void hideProgress() {
    	try {
    		dismissDialog(0);
    	} catch(Exception e) {
    	}
    }

    private final static int LOGIN_ERROR_USER_ALREADY_EXIST        = 1103;
    private final static int LOGIN_ERROR_INVALID_USERNAME_PASSWORD = 1100;
    private final static int LOGIN_ERROR_USER_DOESNOT_EXIST        = 1101;
    private final static int LOGIN_ERROR_DEVICE_DOESNOT_EXIST      = 9101;
    private final static int LOGIN_ERROR_CANCEL_ACTIVE        = 2417;
    
    /**
     * Called when the authentication process completes (see attemptLogin()).
     */
    public void onAuthenticationResult(int result) {

        Log.i(TAG, "onAuthenticationResult(" + result + ")");
        // Hide the progress dialog
        hideProgress();
        if (result == 0) {
            if (!mConfirmCredentials) {
                finishLogin();
            } else {
                finishConfirmCredentials(true);
            }
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
            	if(result == LOGIN_ERROR_USER_ALREADY_EXIST) {
            		mMessage.setText(getText(R.string.login_error_user_already_exist));
            	} else if(result == LOGIN_ERROR_INVALID_USERNAME_PASSWORD) {
            		mMessage.setText(getText(R.string.login_error_invalid_username_password));
            	} else if(result == LOGIN_ERROR_USER_DOESNOT_EXIST) {
            		mMessage.setText(getText(R.string.login_error_user_doesnot_exist));
            	} else if(result == LOGIN_ERROR_DEVICE_DOESNOT_EXIST) {
            		mMessage.setText(getText(R.string.login_error_device_doesnot_exist));
            	} else if(result == LOGIN_ERROR_CANCEL_ACTIVE){
            		mMessage.setText(getText(R.string.login_error_device_doesnot_exist));
            	}else {
            		mMessage.setText(getText(R.string.login_activity_loginfail_text_both));
            	}
            } else {
            	if(result == LOGIN_ERROR_USER_ALREADY_EXIST) {
            		mMessage.setText(getText(R.string.login_error_user_already_exist));
            	} else if(result == LOGIN_ERROR_INVALID_USERNAME_PASSWORD) {
            		mMessage.setText(getText(R.string.login_error_invalid_username_password));
            	} else if(result == LOGIN_ERROR_USER_DOESNOT_EXIST) {
            		mMessage.setText(getText(R.string.login_error_user_doesnot_exist));
            	} else if(result == LOGIN_ERROR_DEVICE_DOESNOT_EXIST) {
            		mMessage.setText(getText(R.string.login_error_device_doesnot_exist));
            	} else {
	                // "Please enter a valid password." (Used when the
	                // account is already in the database but the password
	                // doesn't work.)
	                mMessage.setText(getText(R.string.login_activity_loginfail_text_pwonly));
            	}
            }
        }
    }

    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        getString(R.string.label);
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg = getText(R.string.login_activity_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
    }

    /**
     * Shows the progress UI for a lengthy operation.
     */
    private void showProgress() {
        showDialog(0);
    }

	public void onClick(View v) {
		if(mLoginBtn == v) {
			handleLogin(v);
		} else if(mCancelBtn == v){
			finish();
		}
	}
}
