package com.aess.ThirdPartyApp1;

import com.aess.androidsso.SSOCallBack;
import com.aess.androidsso.SSOClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ThirdPartyApp1 extends Activity implements SSOCallBack, OnClickListener {
	SSOClient mSSOClient;

	static TextView mTextView;
	static Handler mHandler = new Handler();
	static String mAuthToken;
	private boolean mLogined = false; 
	private boolean mFirstStart = true;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //mSSOClient = new SSOClient(this, this);

        mTextView = (TextView)findViewById(R.id.textView1);
        button_login  = (Button)findViewById(R.id.button1);
        button_login.setOnClickListener(this);
        button_logout = (Button)findViewById(R.id.button2);
        button_logout.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSSOClient = new SSOClient(this, this);
        //if(mFirstStart) {
        //	mFirstStart = false;
        //} else {
        //	serviceStarted();
        //}
    }

    @Override
    public void onStop() {
    	super.onStop();
        mSSOClient.destroy();    	
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

	@Override
	public void serviceStarted() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
				mHandler.post(new Runnable() {
		            public void run() {
		            	onServiceStarted();
		            }
				});
			}
		}.start();
		//new Thread() {
		//	public void run() {
		        try {
					//mSSOClient.login("androidtest1");
				} catch (Exception e) {
				}
			//}
		//}.start();
	}
	
	private void onServiceStarted() {
		Log.i("TP2", "onServiceStarted");
	    mAuthToken = mSSOClient.getToken();
		mSSOClient.setCallback();
		try {
			mAuthToken = mSSOClient.login(this, "androidtest1");
		} catch (Exception e1) {
		}
	}

    Button button_login;
    Button button_logout;
    Button button_heartbeat;
    
	@Override
	public void onClick(View button) {
    	int result;
        if(button == button_login) {
        	if(!mLogined) {
	            try {
	            	mSSOClient.setCallback();
	            	mAuthToken = mSSOClient.login(this, "androidtest1");
	            	if(true) return;
	            	if(!TextUtils.isEmpty(mAuthToken) && NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this) == 0) {
	            		mLogined = true;
	            		button_login.setText("heartbeat");
	            		mTextView.setText("Login success");
	            	} else {
	            		//mSSOClient.logout("androidtest1");
	                	//mAuthToken = mSSOClient.login("androidtest1");
	                	//if(NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this) != 0) {
	                	//	mTextView.setText("Login success");
	                	//} else {
	            		mAuthToken = "";
	            		mSSOClient.logout("androidtest1");
	                		mTextView.setText("Login fail");
	                	//}
	            	}
	    		} catch (Exception e) {
	    		}
        	} else {
            	if((result = NetworkAuthenticate.authenticate("<request type=\"heartbeat\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this)) == 0) {
            		mTextView.setText("Heartbeat success");        	
            	} else {
            		mAuthToken = "";
            		mSSOClient.logout("androidtest1");
            		mLogined = false;
            		button_login.setText("Login");
            		mTextView.setText("Heartbeat fail: " + result);
            	}
        	}
        } else if(button == button_logout) {
        	mSSOClient.logout("androidtest1");
        	if((result = NetworkAuthenticate.authenticate("<request type=\"logout\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this)) == 0) {
        		mTextView.setText("Logout success");
        	} else {
        		mTextView.setText("Logout fail: " + result);
        	}
        	mAuthToken = "";
        }
        /*else if(button == button_heartbeat) {
        	if((result = NetworkAuthenticate.authenticate("<request type=\"heartbeat\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this)) == 0) {
        		mTextView.setText("Heartbeat success");        	
        	} else {
        		mSSOClient.logout("androidtest1");
        		mTextView.setText("Heartbeat fail: " + result);
        	}
        }*/
	}

	@Override
	public void loginResult(final int error) {
		mHandler.post(new Runnable() {
            public void run() {
                onAuthenticationResult(error);
            }
        });
	}
	
	private void onAuthenticationResult(int result) {
		if(result == -1) {
			finish();
			return;
		}

		mAuthToken = mSSOClient.getToken();
    	if(!TextUtils.isEmpty(mAuthToken) && NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest1" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this) == 0) {
    		mLogined = true;
    		button_login.setText("heartbeat");
    		mTextView.setText("Login success");
    	} else {
    		mTextView.setText("Login failed");
    	}
	}
}