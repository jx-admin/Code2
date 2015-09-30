
package com.accenture.mbank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WellComeActivity extends BaseActivity {

    Handler mHander;

    int delayTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mHander = new Handler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wellcome);
        mHander.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(WellComeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, delayTime);
    }
}
