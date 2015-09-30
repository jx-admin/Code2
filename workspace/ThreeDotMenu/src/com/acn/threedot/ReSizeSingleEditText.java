package com.acn.threedot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author junxu.wang
 *
 */
public class ReSizeSingleEditText extends EditText {
	
	public ReSizeSingleEditText(Context context) {
		super(context);
		initialise();
	}
	
	public ReSizeSingleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customView);//TypedArray是一个数组容器  
        resizeSingleLine = a.getBoolean(R.styleable.customView_reTextSize, false);//防止在XML文件里没有定义，就加上了默认值30  
        a.recycle();
		initialise();
	}
	DisplayMetrics mDisplayMetrics;
	private DisplayMetrics getDisplayMetrics(){
		if(mDisplayMetrics==null){
			mDisplayMetrics=getContext().getApplicationContext().getResources().getDisplayMetrics();
		}
		return mDisplayMetrics;
	}
	
	private void initialise() {
		testPaint = new Paint();
		testPaint.set(this.getPaint());
		//max size defaults to the intially specified text size unless it is too small
		maxTextSize = this.getTextSize();
		if (maxTextSize < 5) {
			maxTextSize = 20;
		}
		minTextSize = 5;
	}
	
	boolean resizeSingleLine=false;
	public void setReSizeSingleLine(boolean resizeSingleLine){
		this.resizeSingleLine=resizeSingleLine;
	}
	
	/* Re size the font so the specified text fits in the text box
	 * assuming the text box is the specified width.
	 */
	private void refitText(String text, TextView tv) { 
		if(!resizeSingleLine){
			return;
		}
		int width=tv.getWidth();
		if (width > 0) {
			int availableWidth = width - tv.getPaddingLeft() - tv.getPaddingRight();
			float trySize =tv.getTextSize();
			testPaint.setTextSize(trySize);
//	            Log.d(this.getClass().getSimpleName(),"textSize:"+trySize+ " textWidth:"+testPaint.measureText(text)+" editWidth:"+width+" availableWidth:"+availableWidth);
			while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
				trySize -= 1;
				testPaint.reset();
				testPaint.setTextSize(trySize);
//	                Log.d(this.getClass().getSimpleName(),"textSize:"+trySize+ " textWidth:"+testPaint.measureText(text)+" editWidth:"+width+" availableWidth:"+availableWidth);
			}
			
			testPaint.setTextSize(trySize+1);
			while(trySize<maxTextSize&&testPaint.measureText(text)<availableWidth){
				++trySize;
				testPaint.setTextSize(trySize+1);
			}
			
//	            Log.d(this.getClass().getSimpleName(),"textSize:"+trySize);
			this.setTextSize(trySize/getDisplayMetrics().density);
		}
	}
	
	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		refitText(text.toString(), this);
	}
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		if (w != oldw) {
			refitText(this.getText().toString(), this);
		}
	}
	
	//Getters and Setters
	public float getMinTextSize() {
		return minTextSize;
	}
	
	public void setMinTextSize(int minTextSize) {
		this.minTextSize = minTextSize;
	}
	
	public float getMaxTextSize() {
		return maxTextSize;
	}
	
	public void setMaxTextSize(int minTextSize) {
		this.maxTextSize = minTextSize;
	}
	
	//Attributes
	private Paint testPaint;
	private float minTextSize;
	private float maxTextSize;
	
}
