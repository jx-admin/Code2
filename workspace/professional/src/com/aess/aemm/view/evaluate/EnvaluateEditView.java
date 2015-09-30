package com.aess.aemm.view.evaluate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.ImageGet;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.DomXmlBuilder.CommentInfo;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.view.TextMarquee;

public class EnvaluateEditView extends Activity implements OnClickListener {
	private static final int GETURLS=1;
	public static final String LOGCAT="EnvaluateEditView";
	public static final String URL="url",APPID="appId",VER="ver";
	public static HashMap<String,Bitmap> cache;
	private Gallery gallery;
	private ImageAdapter adapter;
	private List<String> imgUrl;
	String url;String appId; String ver; String content;
	Button submit_btn;
	EditText mConmentEt;
	EditText mTitleET;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        	setContentView(R.layout.envaluate_edit);
        	Intent i=getIntent();
        	url=i.getStringExtra(URL);
        	appId=i.getStringExtra(APPID);
        	ver=i.getStringExtra(VER);
        	new Thread(){
        		public void run(){
        			imgUrl=ImageGet.getImage(EnvaluateEditView.this,url);
//        			if(imgUrl==null){
//        				Log.d(LOGCAT, "imgUrls is null");
//        				return ;
//        			}
        			mHandler.sendEmptyMessage(GETURLS);
        		};
			}.start();
        	init();
     }
    public void loadImage(String url){
    	new Thread(new LoadImageThread(url,cache,mHandler)).start();
    }
    private Handler mHandler=new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		if(msg.what==GETURLS){
    			((ProgressBar)findViewById(R.id.geturls_pb)).setVisibility(View.GONE);
    			((TextView)findViewById(R.id.geturls_tv)).setVisibility(View.GONE);
    			if(imgUrl!=null&&imgUrl.size()>0){
	    			adapter = new ImageAdapter(EnvaluateEditView.this, imgUrl);
	    	    	gallery.setAdapter(adapter);
//    			}else{
//    				((TextView)findViewById(R.id.geturls_tv)).setText(R.string.getsnapshot_fail);
    			}
    		}else{
    			adapter.notifyDataSetChanged();
    		}
    		super.handleMessage(msg);
    	}
    };
    public void init(){
    	cache = new HashMap<String, Bitmap>();
    	gallery = (Gallery) findViewById(R.id.app_gallery);
    	gallery.setFadingEdgeLength(0);
//    	imgUrl=ImageGet.getImage(this,url); 

    	
    	submit_btn=(Button) findViewById(R.id.submit_btn);
    	submit_btn.setOnClickListener(this);
    	mTitleET=(EditText)findViewById(R.id.conment_title_et);
    	mConmentEt=(EditText)findViewById(R.id.envaluate_et);
    }
	@Override
	public void onClick(View arg0) {
//		生成提交应用评价的xml
		CommentInfo ci = new CommentInfo(appId, ver,mTitleET.getText().toString().trim(), mConmentEt.getText().toString().trim());

		int msg=-1;
		if (TextUtils.isEmpty(ci.mTitle)) {
			msg= R.string.title_null;
		}else if(TextUtils.isEmpty(ci.mContent)){
			msg= R.string.conment_null;
		}
		if(msg!=-1){
			Toast.makeText(this, this.getString(msg), Toast.LENGTH_SHORT).show();
		}else{
			sendEnvaluete(ci);
		}

	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_ENTER){
			InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
			imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	public UpdateResult sendEnvaluete(CommentInfo ci) {
		ConmentAsyncTask mConmentAsyncTask=new ConmentAsyncTask();
		mConmentAsyncTask.execute(ci);
		return null;
	}
	public static void start(Context mContext, String url,String appId,String ver) {

		Intent intent=new Intent(mContext,EnvaluateEditView.class);
		intent.putExtra(URL, url);
		intent.putExtra(APPID, appId);
		intent.putExtra(VER, ver);
		mContext.startActivity(intent);
	}

class ConmentAsyncTask extends AsyncTask<CommentInfo, Integer, Integer> {   
	public static final String LOGCAT="ProgressAyncTask";
   
    @Override   
    protected Integer doInBackground(CommentInfo... params) {
//    	Log.d(LOGCAT, "doInBackground "+params.length +" <- "+params[0]);
    	
    	AutoAdress address = AutoAdress.getInstance(EnvaluateEditView.this);
		String url = address.getUpdateURL();

		Log.d(LOGCAT, "submit conment to -> " + url);
		if (null == url) {
			return R.string.conment_check_net;
		}
		if(params==null||params.length<1){
			return R.string.conment_null;
		}
		String lgInfo = DomXmlBuilder.buildInfo(EnvaluateEditView.this,false, DomXmlBuilder.COMMENT, params[0]);

		Log.d(LOGCAT, "sendEnvaluete XmlBuilder.buildInfo == " + lgInfo);
		if (null == lgInfo || lgInfo.length() < 1) {
			return R.string.conment_xmlbuild_error;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(EnvaluateEditView.this, url, lgInfo,"/sdcard/comment.txt");

		return R.string.conment_submit;
    }   
   
   
    @Override   
    protected void onPostExecute(Integer result) {
        Log.d(LOGCAT, "onPostExecute <-"+result); 
		if(R.string.conment_submit==result){
			mTitleET.setText("");
			mConmentEt.setText("");
			EnvaluateEditView.this.finish();
		}else{
			Toast.makeText(EnvaluateEditView.this,EnvaluateEditView.this.getString(result), Toast.LENGTH_SHORT).show();
			submit_btn.setEnabled(true);
		}
    }   
   
   
    @Override   
    protected void onPreExecute() {
    	Log.d(LOGCAT, "onPreExecute");
    	submit_btn.setEnabled(false);
    }   
   
   
    @Override   
    protected void onProgressUpdate(Integer... values) { 
    	Log.d(LOGCAT, "onProgressUpdate "+values.length+" <-"+values[0]);
    } 
    
    protected void onCancelled(){
    	Log.d(LOGCAT, "onCancelled");
    }
}
}

