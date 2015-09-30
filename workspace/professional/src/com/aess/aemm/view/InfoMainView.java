package com.aess.aemm.view;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.update.UpdateExecutor;
import com.aess.aemm.view.data.BPUtil;
import com.aess.aemm.view.data.User;

public class InfoMainView extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        type=i.getByteExtra(TYPE, USER_INFOMATION);
        switch(type){
        case USER_INFOMATION:
        	setContentView(R.layout.userinfomation);
        	userTitle_ev=(TextView) findViewById(R.id.user_title_tv);
        	userName_et=(EditText) findViewById(R.id.user_name_et);
        	userEmail_et=(EditText) findViewById(R.id.user_email_et);
        	userIM_et=(EditText) findViewById(R.id.user_im_et);
        	userAddress_et=(EditText) findViewById(R.id.user_address_et);
        	userRepair_btn=((Button)findViewById(R.id.repair_btn));
        	userRepair_btn.setOnClickListener(this);
        	
        	SharedPreferences settings = getSharedPreferences(InfoMainView.USERINFODATA, 0);
        	String mUserName=settings.getString(User.Setting.NAME,null);
			String mUserMail=settings.getString(User.Setting.MAIL, null);
			String mUserIM=settings.getString(User.Setting.IM, null);
			String mUserAddress=settings.getString(User.Setting.ADDR,null);
			
			userName_et.setText(mUserName);
        	userEmail_et.setText(mUserMail);
        	userIM_et.setText(mUserIM);
        	userAddress_et.setText(mUserAddress);
        	break;
        case REPAIR_PASSWORD:
        	setContentView(R.layout.repair_password);
        	oldpwd_et=(EditText) findViewById(R.id.oldpwd_et);
        	newpwd_et=(EditText) findViewById(R.id.newpwd_et);
        	repwd_et=(EditText) findViewById(R.id.repwd_et);
        	userRepair_btn=((Button)findViewById(R.id.submit_btn));
        	userRepair_btn.setOnClickListener(this);
        	break;
        case CHANGE_DERMA:
        	setContentView(R.layout.change_derma);
        	userRepair_btn=(Button) findViewById(R.id.submit_btn);
        	userRepair_btn.setOnClickListener(this);
        	RadioGroup change_derma_rg=(RadioGroup) findViewById(R.id.change_derma_rg);
        	if(CommUtils.getDermaId(this)==R.drawable.new_bg){
        	change_derma_rg.check(R.id.new_derma_rb);
        	}
        	break;
        case USER_INFOMATION_SECURITY_CEDE:
        	
        	initUserSecurityCode();
        	break;
        case PWD_AUTH_QUANSTION:
        	setPwdSecurityCode();
        	break;
        }
    }

    private void setPwdSecurityCode() {
    	setContentView(R.layout.security_code);
    	((TextView) findViewById(R.id.title_tv)).setText(R.string.security_code);
    	userRepair_btn=(Button) findViewById(R.id.submit_btn);
    	userRepair_btn.setOnClickListener(this);
    	((TextView) findViewById(R.id.decription_tv)).setText(R.string.anser_quanstion);
    	TextView tv=(TextView) findViewById(R.id.quanstion_tv);
    	tv.setVisibility(View.VISIBLE);
    	tv.setText(R.string.what_is_capital);
    	answer_et=(EditText) findViewById(R.id.answer_et);
	}

	public static void start(Context activity,byte type){
		Intent intent=new Intent(activity,InfoMainView.class);
		intent.putExtra(InfoMainView.TYPE,type);
		activity.startActivity(intent);
    }
	
    public UpdateResult sendLogin(Context cxt, String user, String ps) {
		Log.i(TAG, "sendtest");

		String url = AutoAdress.getInstance(cxt).updateUrl();

		Log.d(TAG, "Upate Url == "+url);
		if (null == url) {
			return null;
		}
//		XmlBuilder.Info arg = XmlBuilder.Info.getInsByUser(user, ps);
//		UserInfo ui = new UserInfo(user, ps);

		String lgInfo = DomXmlBuilder.buildInfo(cxt, false, DomXmlBuilder.USER, null);

		Log.d(TAG, "XmlBuilder.buildInfo == "+lgInfo);
		if (null == lgInfo || lgInfo.length() < 1) {
			return null;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(cxt, url, lgInfo,
				"/adcard/log.txt");

		if (null == upResult) {
			Log.d(TAG, "aemmHttpPost == null");
			return null;
		}

		UpdateResult urlt = UpdateXmlParser.getUpdateResultByErr();

		urlt = new UpdateXmlParser(cxt).parseUpdateXml(upResult,0);

		return urlt;
	}
    
    void initUserSecurityCode(){
		Log.d(TAG,"initUserSecurityCode");
		handler = new Handler(){
	    	public void handleMessage(Message msg) {
				Log.d(TAG,"handler recieve "+ msg.what );
	    		switch (msg.what) {
	    		case 1:
	    			InfoMainView.this.setVerify((ImageView) findViewById(R.id.verify_iv));
	    			break;
	    			}
	    		super.handleMessage(msg);
	    		}
	    	}; 
	    task = new TimerTask(){
	    		public void run() {
	    			Message message = new Message(); 
	    			message.what = 1; 
	    			handler.sendMessage(message); 
	    			Log.d(TAG,"task run");
	    	}
		};
    	setContentView(R.layout.security_code);
		findViewById(R.id.verify_iv).setVisibility(View.VISIBLE);
		TextView refre=(TextView)findViewById(R.id.refresh_tv);
		refre.setVisibility(View.VISIBLE);
		refre.getPaint().setUnderlineText(true);
		refre.setOnClickListener(this);
    	((TextView) findViewById(R.id.title_tv)).setText(R.string.security_code);
    	((TextView) findViewById(R.id.decription_tv)).setText(R.string.input_security_code);
    	answer_et=(EditText) findViewById(R.id.answer_et);
    	userRepair_btn=(Button) findViewById(R.id.submit_btn);
    	userRepair_btn.setOnClickListener(this);
    	mBPUtil=BPUtil.getInstance();
		mTimer = new Timer(true);
    	mTimer.schedule(task,60000, 60000); //延时1000ms后执行，1000ms执行一次
    	setVerify((ImageView) findViewById(R.id.verify_iv));
    }
    
    private void setVerify(ImageView verifyImg){
		((TextView)findViewById(R.id.error_tv)).setText("");
    	verifyImg.setImageBitmap(mBPUtil.createBitmap());
		Log.d(TAG,"create verify code "+mBPUtil.getCode());
    }    

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.refresh_tv){
			setVerify((ImageView) findViewById(R.id.verify_iv));
		}else{
			Log.d("wjx","userInfo:"+type);
			switch(type){
			case USER_INFOMATION:
				type=REPAIR_USER_INFOMATION;
				findViewById(R.id.user_pwd_tv).setVisibility(View.VISIBLE);
				oldpwd_et=(EditText) findViewById(R.id.user_pwd_et);
				oldpwd_et.setVisibility(View.VISIBLE);
				userTitle_ev.setText(R.string.repairSTR);
				userRepair_btn.setText(R.string.finishSTR);
				userName_et.setInputType(InputType.TYPE_CLASS_TEXT);
				userName_et.setEnabled(true);
				userEmail_et.setInputType(InputType.TYPE_CLASS_TEXT);
				userEmail_et.setEnabled(true);
				userIM_et.setInputType(InputType.TYPE_CLASS_TEXT);
				userIM_et.setEnabled(true);
				userAddress_et.setInputType(InputType.TYPE_CLASS_TEXT);
				userAddress_et.setEnabled(true);
				break;
			case REPAIR_USER_INFOMATION:
				if(TextUtils.isEmpty(oldpwd_et.getText().toString())){
					((TextView)findViewById(R.id.error_tv)).setText(R.string.pwd_cannot_null);
					return ;
				}
				((TextView)findViewById(R.id.error_tv)).setText("");
				if(mUser==null){
					mUser=new User(oldpwd_et.getText().toString(),userName_et.getText().toString().trim(),userEmail_et.getText().toString().trim(),userIM_et.getText().toString().trim(),userAddress_et.getText().toString().trim());
				}else{
					mUser.set(oldpwd_et.getText().toString(),userName_et.getText().toString().trim(),userEmail_et.getText().toString().trim(),userIM_et.getText().toString().trim(),userAddress_et.getText().toString().trim());
				}
				type=USER_INFOMATION_SECURITY_CEDE;
				initUserSecurityCode();
			break;
			case REPAIR_PASSWORD:
				if(!TextUtils.isEmpty(newpwd_et.getText().toString())&&newpwd_et.getText().toString().equals(repwd_et.getText().toString())){
					if(mUser==null){
						mUser=new User(oldpwd_et.getText().toString().trim(),newpwd_et.getText().toString().trim());
					}else{
						mUser.set(oldpwd_et.getText().toString().trim(),newpwd_et.getText().toString().trim());
					}
			    type=PWD_AUTH_QUANSTION;
			    setPwdSecurityCode();
//				start(this, PWD_AUTH_QUANSTION);
				}else{
					((TextView)findViewById(R.id.error_tv)).setText(R.string.double_pwd_unsame);
					newpwd_et.setText("");
					repwd_et.setText("");
				}
				break;
			case CHANGE_DERMA:
	        	RadioGroup change_derma_rg=(RadioGroup) findViewById(R.id.change_derma_rg);
				if(change_derma_rg.getCheckedRadioButtonId()==R.id.new_derma_rb){
    				CommUtils.setDermaId(InfoMainView.this, R.drawable.new_bg);
    				Log.d(TAG, "RadioGroup change: new");
    			}else{
    				CommUtils.setDermaId(InfoMainView.this, R.drawable.bg);
    				Log.d(TAG, "RadioGroup change: default");
    			}
				finish();
			break;
			case USER_INFOMATION_SECURITY_CEDE:
				if(answer_et.getText().toString().trim().equalsIgnoreCase(mBPUtil.getCode())){
					mTimer.cancel();
					mUser.saveCreate(this);
//					Update.startUpdate(this, Update.MANUAL);
//					finish();
				    new UpdateExecutor(this,UpdateExecutor.POSTUSER,mHandler).execute();
					
				}else{
					Log.d(TAG,"input code:"+answer_et.getText().toString().trim());
					((TextView)findViewById(R.id.error_tv)).setText(R.string.verify_error);
				}
				break;
			case PWD_AUTH_QUANSTION:
				if(getResources().getString(R.string.beijing).equals(answer_et.getText().toString().trim())){
					mUser.saveCreate(this);
//				    Update.startUpdate(this, Update.MANUAL);
				    new UpdateExecutor(this,UpdateExecutor.POSPWD,mHandler).execute();
				}else{
					((TextView)findViewById(R.id.error_tv)).setText(R.string.verify_error);
				}
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		if(mTimer!=null){
			mTimer.cancel();
		}
		super.onBackPressed();
	}
	
	private Handler mHandler=new  Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.obj!=null){
				UpdateResult ur=(UpdateResult) msg.obj;
				if(0 == ur.mErrorMsg){
					Toast.makeText(InfoMainView.this, User.SUCCESSED, Toast.LENGTH_SHORT).show();
					InfoMainView.this.finish();
				}else{
					Toast.makeText(InfoMainView.this, User.mErrorInfo.get(Integer.toString(ur.mErrorMsg)), Toast.LENGTH_SHORT).show();
				}
			}
			super.handleMessage(msg);
		}
		
	};
    
    private TextView userTitle_ev;
    private EditText userName_et,userEmail_et,userIM_et,userAddress_et;
    private EditText oldpwd_et,newpwd_et,repwd_et;
    private Button userRepair_btn;
    User mUser;
    
    //security-code
    private  EditText answer_et;
    private BPUtil mBPUtil;
    private Timer mTimer;
    private TimerTask task;
    private Handler handler;
    
    private byte type;
    public static final String TAG="InfoMainView";
    public static final byte USER_INFOMATION=1;
    public static final byte REPAIR_USER_INFOMATION=2;
    public static final byte REPAIR_PASSWORD=3;
    public static final byte CHANGE_DERMA=4;
    public static final byte USER_INFOMATION_SECURITY_CEDE=5;
    public static final byte PWD_AUTH_QUANSTION=6;
    public static final String TYPE="TYPE";
    public static final String USERINFODATA="userInfomation.dat";
    public static final String USERINFODATA_NEW="userInfomation_new.dat";
	
}
