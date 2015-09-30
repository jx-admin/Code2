package com.android.accenture.aemm.express;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.accenture.aemm.express.updataservice.DeviceAdminLocalSetup;
import com.android.accenture.aemm.express.updataservice.ListenerService;
import com.android.accenture.aemm.express.updataservice.configPreference;

public class Login extends Activity implements OnClickListener{
	private static final String NAME="name";
	private static final String WORD="word";
	private static final String MSG="msg";
	private static final String CANCELABLE="cancelAble";
	private AutoCompleteTextView et_account;
	private EditText et_password;
	private EditText et_imei;
	private TextView tv_message;
	private TextView tv_imei;
	private Button login_sure_btn;
	private Button login_cancel_btn;
	private User user;
	private String msg;
	private String imei;
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mylogin);
	        imei = MoshinInformation.getDeviceId(this);
	        init();
	        Intent intent=getIntent();
	        user=getUser(intent);
	        msg=intent.getStringExtra(MSG);
	        boolean cancelAble=intent.getBooleanExtra(CANCELABLE, true);
	        initData(msg,cancelAble);
	        DeviceAdminLocalSetup da = DeviceAdminLocalSetup.getInstance(this);
	        if (!da.isDeviceAdminEnabled(this))
	        {
	        	Intent i = da.buildAddDeviceAdminIntent();
	        	startActivity(i);
	        }
	}
	
	private void init(){
		 et_account=(AutoCompleteTextView) findViewById(R.id.et_account);
	     et_password=(EditText) findViewById(R.id.et_password);
	     tv_imei=(TextView) findViewById(R.id.tv_imei);
	     et_imei=(EditText) findViewById(R.id.et_imei);
	     tv_message=(TextView) findViewById(R.id.tv_message);
	     login_sure_btn=((Button) findViewById(R.id.login_sure_btn));
	     login_cancel_btn=(Button) findViewById(R.id.login_cancel_btn);
	     login_sure_btn.setOnClickListener(this);
	     login_cancel_btn.setOnClickListener(this);

	     if (imei == null||imei.length()==0) {
	    	 et_imei.setEnabled(true);
	     } else {
//	    	 et_imei.setText(imei);
//	    	 et_imei.setEnabled(false);
	    	 et_imei.setVisibility(View.GONE);
	    	 tv_imei.setVisibility(View.GONE);
	     }
	}
	
	private void initData(String msg,boolean cancelAble){
		if(user==null||user.getUserName()==null){
			et_account.setText("");
        }else{
        	et_account.setText(user.getUserName());
        }
        if(user==null||user.getPassWord()==null){
        	et_password.setText("");
        }else{
        	et_password.setText(user.getPassWord());
        }
//        if(MoshinInformation.getDeviceId(this)==null){
//        }
        if(Main.userDebug){
			et_account.setText(Main.userName);
        	et_password.setText(Main.passWord);
    		et_imei.setText(Main.imei);
        }
        setMessage(msg);
        login_cancel_btn.setClickable(cancelAble);
	}
	private void setMessage(int mesId){
		tv_message.setText(mesId);
	}
	private void setMessage(String message){
		if(message==null){
			tv_message.setText("");
		}else{
			tv_message.setText(message);
		}
	}
	
	private void getInput(){
		user.setUserName(et_account.getText().toString().trim());
		user.setPassWord(et_password.getText().toString().trim());
		if (imei == null || imei.length()< 1) {
			imei = et_imei.getText().toString().trim();
		}
	}
	
	private int checkInput(){
		getInput();
		if(!Utils.CheckNetwork(this)){
			return R.string.network_disable;
		}else if(user.getUserName()==null||user.getUserName().length()<=0){
			return R.string.username_null;
		}else if(user.getPassWord()==null||user.getPassWord().length()<=0){
			return R.string.password_null;
		} else if (imei==null || imei.length()<=0) {
			return R.string.imei_null;
		}
		return -1;
		
	}
	
	public boolean checkLogin(String name,String word){
		if(name.equals("hell")&&"123".equals(word)){
			return true;
		}
		return false;
	}
	
	/**登陆,向services发信息
	 * @param context
	 * @param user
	 */
	private void login(){
		Intent i = new Intent(ListenerService.USER_LOGIN_ACTION);
		i.setClass(this, ListenerService.class);
		i.putExtra(NAME,user.getUserName());
		i.putExtra(WORD, user.getPassWord());
		startService(i);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_sure_btn:
			enterLogin();
			break;
		case R.id.login_cancel_btn:
			finish();
			break;
		}
	}
	private void enterLogin(){
//		getInput();
		if (true == isNetOK()) {
			int mesId=checkInput();
			if(mesId!=-1){
				setMessage(mesId);
				return;
			}
			saveImei(this);
			login();
			finish();
		} else {
			setMessage(R.string.network_disable);
		}

	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&!login_cancel_btn.isClickable()){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_UP&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
			if(et_account.isFocused()){
				et_password.setFocusable(true);
				et_password.requestFocus();
				et_password.setFocusableInTouchMode(true);
				
			}else if(et_password.isFocused()&&et_imei.getVisibility()==View.VISIBLE&&et_imei.isEnabled()){
				et_imei.setFocusable(true);
				et_imei.requestFocus();
				et_imei.setFocusableInTouchMode(true);
			}else {
				enterLogin();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	protected void onStart() {
		super.onStart();
		et_account.clearFocus();
	}

	
	@Override
	protected void onResume() {
//		Log.d("a","onResume"+et_account.isFocused());
		if(et_account.isFocused()){
			InputMethodManager manager = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
	        manager.showSoftInput(et_account,0);//hideSoftInputFromWindow(et_account.getWindowToken(), 0);
		}
		super.onResume();
	}
	

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**读信息
	 * @param intent
	 * @return
	 */
	public static User getUser(Intent intent){
		User user=new User();
		user.setUserName(intent.getStringExtra(NAME));
	    user.setPassWord(intent.getStringExtra(WORD));
	    return user;
	}
	
	/**
	 * @param context
	 * @param userName
	 * @param passWord
	 * @param msg 登陆返回信息
	 * @param cancelAble 是否可取消登陆
	 */
	public static void showLogin(Context context,String userName,String passWord,String msg,boolean cancelAble){
		Intent i=new Intent(context,Login.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(NAME,userName);
		i.putExtra(WORD, passWord);
		i.putExtra(MSG, msg);
		i.putExtra(CANCELABLE, cancelAble);
		context.startActivity(i);
	}

	/**默认弃用
	 * @param title
	 */
	public void ShowLogin(String title ){
		  LayoutInflater inflater = getLayoutInflater();
		  View layout = inflater.inflate(R.layout.login,null);
		  new AlertDialog.Builder(this).setView(layout)
		  .setPositiveButton(android.R.string.ok, null)
		  .setNegativeButton(android.R.string.cancel, null).show();
	}
	
	private void saveImei(Context context) {
		if (imei != null && imei.length()>0) {
			String ibase = MoshinInformation.getDeviceId(this);
			if (ibase == null && imei != null ) {
				configPreference.putImei(context, imei);
			}
		}
	}
	
	private boolean isNetOK() {

		boolean netok = true;

		ConnectivityManager cm = (ConnectivityManager)Login.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		TelephonyManager tm = (TelephonyManager)Login.this
				.getSystemService(Context.TELEPHONY_SERVICE);

		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !cm.getBackgroundDataSetting()) {
			netok = false;
		} else {
			int netType = info.getType();

			int netSubtype = info.getSubtype();

			if (netType == ConnectivityManager.TYPE_WIFI) {
				netok = info.isConnected();
			} else if (netType == ConnectivityManager.TYPE_MOBILE
					&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !tm.isNetworkRoaming()) {
				netok = info.isConnected();
			} else {
				netok = false;
			}
		}
		return netok;
	}
}
