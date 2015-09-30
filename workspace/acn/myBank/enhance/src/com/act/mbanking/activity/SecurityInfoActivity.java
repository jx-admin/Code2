package com.act.mbanking.activity;

import com.act.mbanking.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;

public class SecurityInfoActivity extends NavigationActivity {

	WebView securityInfo;
	ImageButton help;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.security_info);
        
        showNavigationBar();

        setLeftNavigationText(getString(R.string.back));
        
        securityInfo = (WebView)findViewById(R.id.aaaa);
        securityInfo.loadUrl("file:///android_asset/sicurezza.html");
    }
	
    protected void onLeftNavigationClick(View v) {
    	finish();
    }
}
