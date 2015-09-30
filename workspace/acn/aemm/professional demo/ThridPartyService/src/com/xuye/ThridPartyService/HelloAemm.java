package com.xuye.ThridPartyService;


import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelloAemm extends Activity  implements OnClickListener {
    public static final String ACCOUNT_TYPE = "com.aess.aemmclient.account";
    public static final String AUTHTOKEN_TYPE = "com.aess.aemmclient.data.profile"; //"com.android.contacts";//

	static TextView mTextView;
	static Handler mHandler = new Handler();
	static String mAuthToken;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        mTextView  = (TextView)findViewById(R.id.textView1);
        button_login  = (Button)findViewById(R.id.button1);
        button_login.setOnClickListener(this);
        button_logout = (Button)findViewById(R.id.button2);
        button_logout.setOnClickListener(this);
        button_heartbeat = (Button)findViewById(R.id.button3);
        button_heartbeat.setOnClickListener(this);
        
        checkAccount(this, true);
    }


    Button button_login;
    Button button_logout;
    Button button_heartbeat;
    
    @Override
    public void onClick(View button) {
    	int result;
        if(button == button_login) {
        	checkAccount(this, true);
        } else if(button == button_logout) {
        	if((result = NetworkAuthenticate.authenticate("<request type=\"logout\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", HelloAemm.this)) == 0) {
        		mTextView.setText("Logout success");        	
        	} else {
        		mTextView.setText("Logout fail: " + result);
        	}
        } else if(button == button_heartbeat) {
        	if((result = NetworkAuthenticate.authenticate("<request type=\"heartbeat\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", HelloAemm.this)) == 0) {
        		mTextView.setText("Heartbeat success");        	
        	} else {
        		mTextView.setText("Heartbeat fail: " + result);
        	}
        }
    }
    
    public void checkAccount(final Activity context, final boolean firstCheck) {
		new Thread() {
			public void run() {
//					boolean result = false;
					AccountManager am = AccountManager.get(context);
					Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
					if (accounts != null && accounts.length > 0) {
						ContentResolver.setSyncAutomatically(accounts[0], "com.xuye.ThridPartyService.provider", true);
						//if(!TextUtils.isEmpty(CommUtils.getSessionId(context))) {
							String authtoken = null;
							try {
								authtoken = am.blockingGetAuthToken(accounts[0],AUTHTOKEN_TYPE, true);
							} catch (OperationCanceledException e) {
								e.printStackTrace();
							} catch (AuthenticatorException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(authtoken != null) {
								if(NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest2" + "\" token=\"" + authtoken + "\" />", HelloAemm.this) != 0) {
									am.invalidateAuthToken(ACCOUNT_TYPE, authtoken);
									authtoken = null;
									if(firstCheck) {
										checkAccount(context, false);
										return;
									} else {
										am.removeAccount(accounts[0], null, null);
									}
								}
							}
							if (authtoken == null) {
						        //if(!TextUtils.equals(getIntent().getPackage(), "com.aess.aemmclient")) {
									Intent it = new Intent(Intent.ACTION_MAIN);
									it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									it.setPackage("com.xuye.ThridPartyService");
									it.setClassName("com.aess.aemmclient",
											"com.aess.aemmclient.authenticator.AemmMain");
									it.setAction("com.xuye.ThridPartyService.HelloAemm");
									//startActivity(it);
									startActivityForResult(it, 123);
									finish();
						        //}
								return;// true;
							}
							mAuthToken = authtoken;
							mHandler.post(new Runnable() {
						        public void run() {
						            (HelloAemm.this).onLoginSuccess();
						        }
						    });
						} else {
							for(Account account : accounts) {
								am.removeAccount(account, null, null);
							}
							Intent it = new Intent(Intent.ACTION_MAIN);
							it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							it.setPackage("com.xuye.ThridPartyService");
							it.setClassName("com.aess.aemmclient",
									"com.aess.aemmclient.authenticator.AemmMain");
							it.setAction("com.xuye.ThridPartyService.HelloAemm");
							//startActivity(it);
							startActivityForResult(it, 123);
							finish();
						}
					//}

				
				return;// result;
			}
		}.start();
	}

    private void onLoginSuccess() {    
		mTextView.setText("Login success");
    }

}
