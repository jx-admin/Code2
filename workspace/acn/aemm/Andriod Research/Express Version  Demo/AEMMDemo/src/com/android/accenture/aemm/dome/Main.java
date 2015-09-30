package com.android.accenture.aemm.dome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aemm.config_demo.ListenerService;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startActivity(new Intent(this,ApkHall.class));
        finish();
    }
}