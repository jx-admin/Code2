package com.act.mbanking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.act.mbanking.R;

public class Level3CircleActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        WindowManager windowManager = getWindowManager();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.level3_circle_layout);
    }

}
