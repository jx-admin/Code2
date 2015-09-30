package com.aess.aemmclient.authenticator;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.aess.aemmclient.R;

public class AemmMain extends Activity implements AccountManagerCallback<Bundle>{
    //public static final String ACCOUNT_TYPE = "com.aess.aemmclient.account";
    //public static final String AUTHTOKEN_TYPE = "com.android.contacts";//"com.aess.aemmclient.data.profile";

    static Intent mIntent;
	TextView mTextView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        mIntent = getIntent();
        //checkAccount(this);
        AccountManager am = AccountManager.get(this);
		am.addAccount(Constants.ACCOUNT_TYPE, Constants.AUTHTOKEN_TYPE, null, null, this,
				(AccountManagerCallback<Bundle>)this,
				null);
        finish();
    }

    public static void checkAccount(final Activity context) {
		new Thread() {
			public void run() {
//					boolean result = false;
					AccountManager am = AccountManager.get(context);
					Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
					if (accounts != null && accounts.length > 0) {
						//if(!TextUtils.isEmpty(CommUtils.getSessionId(context))) {
							String authtoken = null;
							try {
								authtoken = am.blockingGetAuthToken(accounts[0],Constants.AUTHTOKEN_TYPE, true);
							} catch (OperationCanceledException e) {
								e.printStackTrace();
							} catch (AuthenticatorException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (authtoken != null) {
								if(AemmMain.mIntent != null) {
									String packageName = AemmMain.mIntent.getPackage();
									String className = AemmMain.mIntent.getAction();
									if(packageName != null && className != null) {
										Intent it = new Intent(Intent.ACTION_MAIN);
										it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										it.setClassName(packageName, //"com.aess.aemmclient",
												className);//"com.aess.aemmclient.authenticator.AemmClient");
										it.setPackage("com.aess.aemmclient");
										context.startActivity(it);
									}
								}
								return;// true;
							}
						} else {
							for(Account account : accounts) {
								am.removeAccount(account, null, null);
							}
						}
					//}
				am.addAccount(Constants.ACCOUNT_TYPE, null, null, null, context,
						(AccountManagerCallback<Bundle>)context,
						null);
				
				return;// result;
			}
		}.start();
	}

	@Override
	public void run(AccountManagerFuture<Bundle> future) {
		
		checkAccount(this);
		if(true) return;

		String authtoken = null;
		try {
			Bundle mBundle = (Bundle) future.getResult();
			authtoken = mBundle.getString(AccountManager.KEY_AUTHTOKEN);
			if(mIntent != null) {
				String packageName = mIntent.getPackage();
				String className = mIntent.getAction();
				if(packageName != null && className != null) {
					Intent it = new Intent(Intent.ACTION_MAIN);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it.setClassName(packageName, //"com.aess.aemmclient",
							className);//"com.aess.aemmclient.authenticator.AemmClient");
					it.setPackage("com.aess.aemmclient");
					startActivity(it);
				}
			}
			return;
		} catch (OperationCanceledException e) {
		} catch (AuthenticatorException e) {
		} catch (IOException e) {
		}
		if(mIntent != null) {
			String packageName = mIntent.getPackage();
			String className = mIntent.getAction();
			if(packageName != null && className != null) {
				Intent it = new Intent(Intent.ACTION_MAIN);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.setClassName(packageName, //"com.aess.aemmclient",
						className);//"com.aess.aemmclient.authenticator.AemmClient");
				it.setPackage("com.aess.aemmclient");
				startActivity(it);
			}
		}
	}
}