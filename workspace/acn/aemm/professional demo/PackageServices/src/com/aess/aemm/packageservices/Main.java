package com.android.test.packageservices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.apk_in)).setOnClickListener(this);
        ((Button)findViewById(R.id.apk_un)).setOnClickListener(this);
    }

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.apk_in:
			Intent i=new Intent(PackageServices.ADD_PACKAGE);
			i.addCategory(PackageServices.CATEGORY);
			i.putExtra("apkUri", "/sdcard/Hello.apk");
			startService(i);
			break;
		case R.id.apk_un:
			i=new Intent(PackageServices.DELETE_PACKAGE);
			i.addCategory(PackageServices.CATEGORY);
			i.putExtra("package", "com.hello.test");
			startService(i);
			break;
		}
	}
}