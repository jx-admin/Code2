
package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;

public class SecurityInfoActivity extends BaseActivity{

    WebView securityInfo;
    ImageButton help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.security_info);
        
        help = (ImageButton)findViewById(R.id.help_btn);
        help.setVisibility(View.GONE);
        
        
        securityInfo = (WebView)findViewById(R.id.securty_content);
        securityInfo.loadUrl("file:///android_asset/sicurezza-it.html");
    }

    
    
    @Override
    protected void onBackClick() {
        finish();
    }
}
