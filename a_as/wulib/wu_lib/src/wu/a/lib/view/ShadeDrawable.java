package wu.a.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.widget.TextView;

public class ShadeDrawable extends StateListDrawable {
	

	public ShadeDrawable(Context context) {
		init(context);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		drawBack(canvas);
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}
	 @Override
	    protected boolean onStateChange(int[] stateSet) {
		 for(int i=0;i<stateSet.length;i++){
			 Log.d(TAG,i+" "+stateSet[i]);
		 }
	        return super.onStateChange(stateSet);
	    }

	public void onBoundsChange(Rect bounds) {
		initDraw(bounds.width(), bounds.height());
	}

	public boolean getPadding(Rect padding) {
		padding.set((int)rf4.left,(int)rf4.top,(int)rf4.right,(int)rf4.bottom);
		return true;
	}

	public static final String TAG = "ChartView";

	// private int w, h;// width,height,half w,half h
	private Paint mPaint;

	private Paint paint2;

	private Path mPath;

	private boolean isSquare = false;

	RectF rf1 = new RectF();

	RectF rf2 = new RectF();

	RectF rf3 = new RectF();

	RectF rf4 = new RectF();

	RectF rf5 = new RectF();

	Context context;

	TextView tv;

	protected int backColor = Color.WHITE;

	protected int adgeLineColor = 0xa6a6a6a6;

	private int middleColor = 0xffDD4DB0;

	protected int adgeLineWidth = 2;

	private void initDraw(int width, int height) {
		if (isSquare) {
			width = height = Math.min(width, height);
		}
		rf1.left = 0;
		rf1.top = 0;
		rf1.right = width;
		rf1.bottom = height;

		rf2.left = (width * 5 / 50) >> 1;
		rf2.top = (height * 5 / 50) >> 1;
		rf2.right = width - rf2.left;
		rf2.bottom = height - rf2.top;

		rf3.left = (width * 8 / 50) >> 1;
		rf3.top = (height * 8 / 50) >> 1;
		rf3.right = width - rf3.left;
		rf3.bottom = height - rf3.top;

		rf4.left = (width * 11 / 50) >> 1;
		rf4.top = (height * 11 / 50) >> 1;
		rf4.right = width - rf4.left;
		rf4.bottom = height - rf4.top;

		rf5.left = (width * 22 / 50) >> 1;
		rf5.top = (height * 22 / 50) >> 1;
		rf5.right = width - rf5.left;
		rf5.bottom = height - rf5.top;
		invalidate();
	}

	LinearGradient shader = new LinearGradient(0, 0, 0, rf1.height(),
			new int[] { 0xe2e2e2e2, 0xcccccccc }, null, TileMode.CLAMP);

	LinearGradient shader2 = new LinearGradient(0, 0, 0, rf2.height(),
			new int[] { 0xb0b0b0b0, 0xeeeeeeee }, null, TileMode.CLAMP);

	LinearGradient shader3 = new LinearGradient(0, 0, 0, rf3.height(),
			new int[] { 0xccDD4DB0, 0xf19E1A69 }, null, TileMode.CLAMP);

	LinearGradient shader4 = new LinearGradient(0, 0, 0, rf4.height(),
			new int[] { 0xbaC72A90, 0xe9AC0F6D }, null, TileMode.CLAMP);

	// LinearGradient shaderAge = new LinearGradient(0, 0, 0, ageHeight, new
	// int[]{0xd8d8d8d8,0xbcbcbcbc},null, TileMode.CLAMP);
	PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0,
			Paint.FILTER_BITMAP_FLAG);

	private void init(Context context) {
		mPaint = new Paint();
		mPaint.setColor(Color.GRAY);
		mPaint.setAntiAlias(true);
		mPath = new Path();
		paint2 = new Paint();
		paint2.setAntiAlias(true);
		// ͼ��ϳ�ģʽ
		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	}

	protected void drawBack(Canvas canvas) {

		// back circle's light
		mPaint.setColor(backColor);
		mPaint.setStyle(Style.FILL);
		// canvas.drawOval(rf1, mPaint);

		// back circle's ageLine
		mPaint.setColor(adgeLineColor);
		mPaint.setStrokeWidth(adgeLineWidth);
		mPaint.setStyle(Style.STROKE);
		canvas.drawOval(rf1, mPaint);
		// shader
		paint2.setShader(shader);
		canvas.drawOval(rf1, paint2);

		// back circle's dark
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		// canvas.drawOval(rf2, mPaint);
		//
		paint2.setShader(shader2);
		canvas.drawOval(rf2, paint2);

		// middle button'bk
		// 846277
		// C12689
		mPaint.setColor(middleColor);
		canvas.drawOval(rf3, mPaint);

		// middle button
		// drawMidButtons(canvas,rf3);
		paint2.setShader(shader3);
		canvas.drawOval(rf3, paint2);

		// center circle effect
		// mPaint.setColor(0xffC72A90);
		// canvas.drawOval(rf4, mPaint);
		// ���Խ���
		paint2.setShader(shader4);
		canvas.drawOval(rf4, paint2);

		// DD4DB0 C0CF76 9A9BCE
		// 9E1A69 8F9D46 6767A7
		// C72A90
		// AC0F6D
	}

	public void invalidate() {
		upDate(rf4);
	}

	private void upDate(RectF rf4) {

	}

	public void setMiddleColor(int color) {
		middleColor = color;
	}

}
