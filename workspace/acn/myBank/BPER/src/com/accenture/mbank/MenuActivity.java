package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.custom.view.KeyboardLayout;
import com.custom.view.KeyboardLayout.onKybdsChangeListener;

public class MenuActivity extends BaseActivity{

    private LinearLayout conLayout;
    
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	super.setContentView(R.layout.menu_activity);
    	conLayout=(LinearLayout) findViewById(R.id.content_layout);
    	((KeyboardLayout) findViewById(R.id.keyboardLayout1)).setOnkbdStateListener(new onKybdsChangeListener() {
    		
    		public void onKeyBoardStateChange(int state) {
    			switch (state) {
    			case KeyboardLayout.KEYBOARD_STATE_HIDE:
    				mMenuHandler.sendEmptyMessage(View.VISIBLE);
    				break;
    			case KeyboardLayout.KEYBOARD_STATE_SHOW:
    				mMenuHandler.sendEmptyMessage(View.GONE);
    				break;
    			}
    		}
    	});
    }
    
    @Override
    public void setContentView(int layoutResID) {
        contentView=LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(contentView);
    }
    
    public View getConentView(){
    	return contentView;
    }
    
    public void setContentView(View contentView) {

        LayoutParams lParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        conLayout.removeAllViews();
        conLayout.addView(contentView,lParams);
    }
}
