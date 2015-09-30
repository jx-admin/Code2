
package com.accenture.mbank;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;

public class SecurityInfoActivity extends BaseActivity{

//    TextView securityInfoTextView;
    WebView securityInfo;
    ImageButton help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.security_info);
        
        help = (ImageButton)findViewById(R.id.help_btn);
        help.setVisibility(View.GONE);
        
        
        securityInfo = (WebView)findViewById(R.id.aaaa);
        securityInfo.loadUrl("file:///android_asset/sicurezza.html");
    }

    
    
    @Override
    protected void onBackClick() {
        finish();
    }
}
