package org.accenture.product.lemonade;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LockActivity extends Activity
{	
	
	Button lock;
	Button unlock;
//	private KeyguardManager keyguardManager;
//
//	private KeyguardLock keyguardLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		

		setContentView(R.layout.lock); 
		
		lock=(Button)findViewById(R.id.lockbutton);
		unlock=(Button)findViewById(R.id.unlockbutton);
				
		lock.setOnTouchListener(new View.OnTouchListener()
		{
			
			int rightMax=0;
			
			int lastX,right;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int top = v.getTop();
				int bottom = v.getBottom();
				
				int ea=event.getAction();
				Log.i("TAG", "Touch:"+ea);

				switch(ea){
				case MotionEvent.ACTION_DOWN:
					rightMax=unlock.getLeft()+15;
					lastX = (int) event.getRawX();
					break;
				case MotionEvent.ACTION_MOVE:
					int dx =(int)event.getRawX() - lastX;
					int left = v.getLeft() + dx;
					right = v.getRight() + dx;
					if(left < 0){
						left = 0;
						right = left + v.getWidth();
					}
					
					if(right > rightMax){
						right = rightMax;
						left = right - v.getWidth();
					}
					
					v.layout(left, top, right, bottom);
					
					lastX = (int) event.getRawX();
					
					break;
				case MotionEvent.ACTION_UP:
					
					if(right>=unlock.getLeft()){
//						finish();
						LockActivity.this.moveTaskToBack(true);
					}else{
						v.layout(0, top, 0+v.getWidth(), bottom);
					}
					
					
					break;        		
				}
				return false;
			}});
		
		
		
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		
		lock.layout(0, lock.getTop(), 0+lock.getWidth(), lock.getBottom());
		super.onResume();
	}
	
	public   void  onAttachedToWindow() {  
	    // TODO Auto-generated method stub   
	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);  
	    super.onAttachedToWindow();  
	}  
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return false;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && status == Status.RunApp) {
//			status = Status.Lock;
//			refresh();
//		} else {
//		}
		return true;
	}

}
