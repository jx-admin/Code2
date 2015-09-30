package com.aess.ThirdPartyApp2;

import com.aess.androidsso.IRemoteServiceCallback;
import com.aess.androidsso.SSOCallBack;
import com.aess.androidsso.SSOClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ThirdPartyApp2 extends Activity implements SSOCallBack, OnClickListener {
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
    public void onResume() {
    	Log.i("TP2", "onResume");
        super.onResume();
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
					//mSSOClient.login("androidtest2");
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
			mAuthToken = mSSOClient.login(this, "androidtest2");
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
        	//try {
        	//	Intent it = new Intent(Intent.ACTION_MAIN);
        		//it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		//it.setPackage("com.xuye.ThridPartyService");
        		//it.setClassName("com.aess.aemmclient",
        		//		"com.aess.androidsso.LoginActivity");
        		//it.addCategory("android.intent.category.AEMMLAUNCHER");
        		//it.setAction("com.aess.ThirdPartyApp1.ThirdPartyApp1");
        		//it.setClassName("com.aess.androidsso",
				//	"com.aess.androidsso.LoginActivity");
        		//startActivity(it);
        		
				//mSSOClient.login(this, "androidtest2");
			//} catch (Exception e1) {
			//}
        	//if(true) return;
        	
            if(button == button_login) {
            	if(!mLogined) {
    	            try {
    	            	mSSOClient.setCallback();
    	            	mAuthToken = mSSOClient.login(this, "androidtest2");
    	            	if(true) return;
    	            	if(!TextUtils.isEmpty(mAuthToken) && NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp2.this) == 0) {
    	            		mLogined = true;
    	            		button_login.setText("heartbeat");
    	            		mTextView.setText("Login success");
    	            	} else {
    	            		//mSSOClient.logout("androidtest2");
    	                	//mAuthToken = mSSOClient.login("androidtest2");
    	                	//if(NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp1.this) != 0) {
    	                	//	mTextView.setText("Login success");
    	                	//} else {
    	            		mAuthToken = "";
    	            		mSSOClient.logout("androidtest2");
    	                		mTextView.setText("Login fail");
    	                	//}
    	            	}
    	    		} catch (Exception e) {
    	    		}
            	} else {
                	if((result = NetworkAuthenticate.authenticate("<request type=\"heartbeat\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp2.this)) == 0) {
                		mTextView.setText("Heartbeat success");        	
                	} else {
                		mAuthToken = "";
                		mSSOClient.logout("androidtest2");
                		mLogined = false;
                		button_login.setText("Login");
                		mTextView.setText("Heartbeat fail: " + result);
                	}
            	}
            }
        } else if(button == button_logout) {
        	mSSOClient.logout("androidtest2");
        	if((result = NetworkAuthenticate.authenticate("<request type=\"logout\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp2.this)) == 0) {
        		mTextView.setText("Logout success");
        	} else {
        		mTextView.setText("Logout fail: " + result);
        	}
        	mAuthToken = "";
        }
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
    	if(!TextUtils.isEmpty(mAuthToken) && NetworkAuthenticate.authenticate("<request type=\"login\" appid=\"" + "androidtest2" + "\" token=\"" + mAuthToken + "\" />", ThirdPartyApp2.this) == 0) {
    		mLogined = true;
    		button_login.setText("heartbeat");
    		mTextView.setText("Login success");
    	} else {
    		mTextView.setText("Login failed");
    	}
	}
}