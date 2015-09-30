package com.example.jxdigitpasswordkeypad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
 
public class FullscreenActivity extends Activity {
    private DigitPasswordKeyPad dpk;
    private View passwdview;
    private Context content ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        
        content = this;
        
        dpk = new DigitPasswordKeyPad(this);
        passwdview = dpk.setup();
        
        EditText editText = (EditText)findViewById(R.id.input);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassWdPadView();
            }
        });
        editText.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL); 
    }
    
    private void showPassWdPadView() {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    // 让一个视图浮动在你的应用程序之上
                    WindowManager windowmanager = (WindowManager) content.getSystemService(Context.WINDOW_SERVICE);
                    LayoutParams layoutparams = new LayoutParams(-1, -1, WindowManager.LayoutParams.FIRST_SUB_WINDOW, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGBA_8888);
                    layoutparams.gravity = Gravity.BOTTOM;
                    passwdview.findViewById(R.id.transpwdpdpanel).getBackground().setAlpha(140);
                    windowmanager.addView(passwdview, layoutparams);
                }
            });
    }
    
}