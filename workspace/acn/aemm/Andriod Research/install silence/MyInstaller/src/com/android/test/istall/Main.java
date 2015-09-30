package com.android.test.istall;



import android.app.Activity;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {

    private final int INSTALL_COMPLETE = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }
    
    private void initView(){
    	msg_tv=(TextView) findViewById(R.id.msg_tv);
    	install_btn=(Button) findViewById(R.id.install_btn);
    	unInstall_btn=(Button) findViewById(R.id.unInstall_btn);
    	install_btn.setOnClickListener(this);
    	unInstall_btn.setOnClickListener(this);
    }
    public void installApp(Uri uri){
    	PackageInstallObserver iobserver = new PackageInstallObserver();
    	PackageManagerUtils.installPackage(getPackageManager(),uri, iobserver, 0x00000000, "hhhh");
    }
    public void unInstallApp(){
    	 PackageDeleteObserver dobserver = new PackageDeleteObserver();
    	 PackageManagerUtils.deletePackage(getPackageManager(),"com.hello.test", dobserver, 0);
         
    }
    public void reInstallApp(Uri uri,int flag){
    	PackageInstallObserver iobserver = new PackageInstallObserver();
    	getPackageManager().installPackage(uri, iobserver, flag|= PackageManager.INSTALL_REPLACE_EXISTING, "hhhh");
    }
    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        public void packageInstalled(String packageName, int returnCode) {
        	Log.v("VV",packageName+" return :"+returnCode);
        	Toast.makeText(Main.this,packageName+" return :"+returnCode,Toast.LENGTH_LONG).show();
        	msg_tv.setText(msg_tv.getText()+"\ninstall packageName+ return :"+returnCode);
//            Message msg = mHandler.obtainMessage(INSTALL_COMPLETE);
//            msg.arg1 = returnCode;
//            mHandler.sendMessage(msg);
        }
    }
    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
        public void packageDeleted(boolean succeeded) {
        	Log.v("VV","PackageDeleteObserver return :"+succeeded);
        	Toast.makeText(Main.this,"PackageDeleteObserver return :"+succeeded,Toast.LENGTH_LONG).show();
        	msg_tv.setText(msg_tv.getText()+"\nPackageDeleteObserver return :"+succeeded);
//            Message msg = mHandler.obtainMessage(UNINSTALL_COMPLETE);
//            msg.arg1 = succeeded?SUCCEEDED:FAILED;
//            mHandler.sendMessage(msg);
       }
    }
    
    private TextView msg_tv;
    private Button install_btn,unInstall_btn;
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.install_btn){
			installApp(Uri.parse("/sdcard/Hello.apk"));
		}else if(v.getId()==R.id.unInstall_btn){
			unInstallApp();
		}
	}
}