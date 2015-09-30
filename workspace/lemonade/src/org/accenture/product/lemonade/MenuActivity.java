package org.accenture.product.lemonade;

import java.lang.reflect.Method;

import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.actions.DefaultAction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MenuActivity extends Activity implements View.OnTouchListener,View.OnFocusChangeListener
{
	
	LinearLayout add;
	LinearLayout personalization; 
	LinearLayout appmanager;
	LinearLayout notification;
	LinearLayout setting;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu);
		
		overridePendingTransition(R.anim.push_bottom_in,0);
		
		add=(LinearLayout)findViewById(R.id.add_layout);
		add.setOnTouchListener(this);
		add.setOnFocusChangeListener(this);
		personalization=(LinearLayout)findViewById(R.id.personalizationLineLayout);
		personalization.setOnTouchListener(this);
		personalization.setOnFocusChangeListener(this);
		appmanager=(LinearLayout)findViewById(R.id.appmanageLayout);
		appmanager.setOnTouchListener(this);
		appmanager.setOnFocusChangeListener(this);
		notification=(LinearLayout)findViewById(R.id.notificationLayout);
		notification.setOnTouchListener(this);
		notification.setOnFocusChangeListener(this);
		setting=(LinearLayout)findViewById(R.id.settingLayout);
		setting.setOnTouchListener(this);
		setting.setOnFocusChangeListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_MOVE || event.getAction()==MotionEvent.ACTION_DOWN){
			v.setBackgroundColor(0xffF3FF00);
		}else if(event.getAction()==MotionEvent.ACTION_UP){
			v.setBackgroundColor(0x00000000);			
			if(v==add){
				Launcher.luanchr.addItems();
			}else if(v==personalization){
				Intent intent = new Intent(this, PersonalizationActivity.class);
				startActivity(intent);
			}else if(v==appmanager){
				startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
			}else if(v==notification){
				Method expandStatusBar=null;
				Object mStatusBarManager = getSystemService("statusbar");
				Method[] methods = mStatusBarManager.getClass().getDeclaredMethods();
				for (Method method : methods)
				{
					if (method.getName().compareTo("expand") == 0)
					{
						expandStatusBar = method;
					}
				}
				try
				{
					if(expandStatusBar!=null)
						expandStatusBar.invoke(mStatusBarManager);
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
			}else if(v==setting){
				Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
		        settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		        startActivity(settings);
			}
			
			finish();
		}
		return true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		// TODO Auto-generated method stub
		if(hasFocus){
			v.setBackgroundColor(0x00000000);
		}else{
			v.setBackgroundColor(0xffF3FF00);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		finish();
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
}
