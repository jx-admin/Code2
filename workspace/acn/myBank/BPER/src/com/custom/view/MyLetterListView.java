package com.custom.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



public class MyLetterListView extends View {
	
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	String[] b = {"A","B","C","D","E","F","G","H","I","J","K","L"
			,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int choose = -1;
	String chooseStr;
	Paint paint = new Paint();
	boolean showBkg = false;
	int textColor=0xff3774B7;
	
	public void setSection(int section){
		if(choose==section){
			return;
		}
		choose=section;
		chooseStr=null;
		invalidate();
	}

	public void setSection(String section){
		if((chooseStr==null&&section==null)||(chooseStr!=null&&chooseStr.equals(section))){
			return;
		}
		choose=-1;
		chooseStr=section;
		invalidate();
	}

	public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterListView(Context context) {
		super(context);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(showBkg){
//		    canvas.drawColor(Color.parseColor("#40000000"));
		}
		
	    int height = getHeight();
	    int width = getWidth();
	    int singleHeight = height / b.length;
	    int tempY=singleHeight;
	    for(int i=0;i<b.length;i++){
	       paint.setColor(textColor);
	       paint.setTypeface(Typeface.DEFAULT_BOLD);
	       paint.setAntiAlias(true);
	       paint.setTextSize(getResources().getDimension(R.dimen.text_nomal_size));
//	       if(i == choose||b[i].equals(chooseStr)){
//	    	   paint.setColor(Color.parseColor("#ffffff00"));//("#3399ff"));
//	    	   paint.setFakeBoldText(true);
//	       }
	       float xPos = width/2  - paint.measureText(b[i])/2;
//	       float yPos = singleHeight * i + singleHeight;
	       canvas.drawText(b[i], xPos, tempY, paint);
	       tempY+=singleHeight;
	       paint.reset();
	    }
	   
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY();
	    final int oldChoose = choose;
	    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    final int pos = (int) (y/getHeight()*b.length);
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if(oldChoose != pos && listener != null){
					if(pos > 0 && pos< b.length){
						listener.onTouchingLetterChanged(pos,b[pos]);
						setSection(pos);
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(oldChoose != pos && listener != null){
					if(pos > 0 && pos< b.length){
						listener.onTouchingLetterChanged(pos,b[pos]);
						setSection(pos);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
//				choose = -1;
//				invalidate();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener{
		public void onTouchingLetterChanged(int index,String s);
	}
	public void setData(String[] b){
		this.b=b;
		invalidate();
	}
	
}
