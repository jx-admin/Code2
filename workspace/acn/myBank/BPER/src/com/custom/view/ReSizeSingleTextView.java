package com.custom.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author junxu.wang
 *
 */
public class ReSizeSingleTextView extends TextView {
	
	public ReSizeSingleTextView(Context context) {
		super(context);
		initialise();
	}
	
	public ReSizeSingleTextView(Context context, AttributeSet attrs) {
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
	private void refitText(TextView tv) {
		if(!resizeSingleLine){
			return;
		}
		String text=getText().toString();
		if(TextUtils.isEmpty(text)){
			text=getHint().toString();
		}
		float stepSize=getDisplayMetrics().density;
		int width=tv.getWidth();
		if (width > 0) {
			int availableWidth = width - tv.getPaddingLeft() - tv.getPaddingRight();
			float trySize =tv.getTextSize();
			testPaint.setTextSize(trySize);
			while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
				trySize -= stepSize;
				testPaint.reset();
				testPaint.setTextSize(trySize);
			}
			
			testPaint.setTextSize(trySize+stepSize);
			while(trySize<maxTextSize&&testPaint.measureText(text)<availableWidth){
				trySize+=stepSize;
				testPaint.setTextSize(trySize+1);
			}
			this.setTextSize(trySize/stepSize);
		}
	}
	
	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		refitText(this);
	}
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		if (w != oldw) {
			refitText(this);
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
