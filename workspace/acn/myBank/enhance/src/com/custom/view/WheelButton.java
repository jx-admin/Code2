package com.custom.view;

import java.util.ArrayList;
import java.util.List;

import com.act.mbanking.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author junxu.wang
 * 
 */
public class WheelButton extends View {
	private static final String TAG = "ChartButton";
	private float mBtnWeightCount = 0;
	private float mCenterBtnWeightCount = 0;
	private float mCenterUserWeightCount = 0;
	private Paint mPaint;
	private Paint paint2;
	private Point mCenterPoint;
	private int w, h;
	private List<WheelButtonItem> mChileLs;
	private List<WheelButtonItem> mChileCenterLs;
	private int selectIndex = -1;
	private boolean centerSelect;
	private int selectColor = 0x22222222;
	private float division = 0;
	private OnItemClickListener mItemClickListener;
	/**
	 * {@link #ITEM_TYPE_CIRCLE}��Բ��ť��־,{@link #ITEM_TYPE_CENTER}���İ�ť��־
	 */
	public static final int ITEM_TYPE_CIRCLE = 0, ITEM_TYPE_CENTER = 1;
	private int ageHeight = 2;
	/**�Ƿ񱣳���Բ*/
	private boolean isSquare = true;
	/** item��Сֵ */
	private static final int MIN_WEGHT=10;
	/**
	 * ��ԲButtonĬ����ʼλ��
	 */
	private static final float DEF_START=-90;

	public WheelButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WheelButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WheelButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(getResources().getDimension(R.dimen.text_nomal_size));
		mPaint.setColor(Color.GRAY);
		// mPaint.setTextSize(20);
		mCenterPoint = new Point();
		paint2 = new Paint();
		paint2.setAntiAlias(true);
		// ͼ��ϳ�ģʽ
		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		mChileLs = new ArrayList<WheelButtonItem>();
		mChileCenterLs = new ArrayList<WheelButtonItem>();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		// Log.d(TAG, "action " + action);
		switch (action) {
		case MotionEvent.ACTION_CANCEL:
			if (selectIndex >= 0) {
				invalidate();
				selectIndex = -1;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			int distance = (int) getDistance(mCenterPoint.x, mCenterPoint.y, x,
					y);
			if (distance < ((int) rf5.width() >> 1)) {
				selectIndex = getCenterIndex(y
						- ((int) (rf1.bottom - rf5.height()) >> 1));
				if (selectIndex >= 0
						&& mChileCenterLs.get(selectIndex).clickAble) {
					centerSelect = true;
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CENTER);
					// }
					invalidate();
					return true;
				} else if (selectIndex >= 0) {
					invalidate();
					selectIndex = -1;
				}
			} else if (distance < (int) rf3.width() >> 1) {
				centerSelect = false;
				selectIndex = getUpChartProp(x, y);

				if (selectIndex >= 0 && mChileLs.get(selectIndex).clickAble) {
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CIRCLE);
					// }
					invalidate();
					return true;
				} else if (selectIndex >= 0) {
					invalidate();
					selectIndex = -1;
				}
			} else if (selectIndex >= 0) {
				invalidate();
				selectIndex = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (selectIndex >= 0 && mItemClickListener != null) {
				if (centerSelect) {
					mItemClickListener.onItemClick(this, this.mChileCenterLs.get(selectIndex), selectIndex,
							ITEM_TYPE_CENTER);
				} else {
					mItemClickListener.onItemClick( this, mChileLs.get(selectIndex),selectIndex,
							ITEM_TYPE_CIRCLE);
				}
			}
			selectIndex = -1;
			invalidate();
			return true;
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			distance = (int) getDistance(mCenterPoint.x, mCenterPoint.y, x, y);
			if (distance < ((int) rf5.width() >> 1)) {
				selectIndex = getCenterIndex(y
						- ((int) (rf1.bottom - rf5.height()) >> 1));
				if (selectIndex >= 0
						&& mChileCenterLs.get(selectIndex).clickAble) {
					centerSelect = true;
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CENTER);
					// }
					invalidate();
					return true;
				} else {
					selectIndex = -1;
				}
			} else if (distance < (int) rf3.width() >> 1) {
				centerSelect = false;
				selectIndex = getUpChartProp(x, y);

				if (selectIndex >= 0 && mChileLs.get(selectIndex).clickAble) {
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CIRCLE);
					// }
					invalidate();
					return true;
				} else {
					selectIndex = -1;
				}
			} else {
				selectIndex = -1;
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}


	RectF rf1 = new RectF();
	RectF rf2 = new RectF();
	RectF rf3 = new RectF();
	RectF rf4 = new RectF();
	RectF rf5 = new RectF();

	// ���Խ���
	LinearGradient shader;
	LinearGradient shader2;
	LinearGradient shader3;
	LinearGradient shader4;
	private static final PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
	/**
	 * RadialGradient for shadow in middel
	 */
	private RadialGradient rg;
	
	private void initDraw(int width, int height) {
		if (isSquare) {
			w = h = Math.min(width, height);
		} else {
			w = width;
			h = height;
		}
		mCenterPoint.x = w >> 1;
		mCenterPoint.y = h >> 1;
		rf1.right = w;
		rf1.bottom = h;

		rf2.left = (w * 5 / 50) >> 1;
		rf2.top = (h * 5 / 50) >> 1;
		rf2.right = w - rf2.left;
		rf2.bottom = h - rf2.top;

		rf3.left = (w * 8 / 50) >> 1;
		rf3.top = (h * 8 / 50) >> 1;
		rf3.right = w - rf3.left;
		rf3.bottom = h - rf3.top;

		rf4.left = (w * 11 / 50) >> 1;
		rf4.top = (h * 11 / 50) >> 1;
		rf4.right = w - rf4.left;
		rf4.bottom = h - rf4.top;

		rf5.left = (w * 22 / 50) >> 1;
		rf5.top = (h * 22 / 50) >> 1;
		rf5.right = w - rf5.left;
		rf5.bottom = h - rf5.top;
		
		// ���Խ���
		shader = new LinearGradient(0, 0, 0, rf1.height(),
				new int[] { 0xe2e2e2e2, 0xcccccccc }, null, TileMode.CLAMP);
		shader2 = new LinearGradient(0, 0, 0, rf2.height(),
				new int[] { 0xb6b6b6b6, 0xedededed }, null, TileMode.CLAMP);
		shader3 = new LinearGradient(0, 0, 0, rf3.height(),
				new int[] { 0xcccccccc, 0xf1f1f1f1 }, null, TileMode.CLAMP);
		shader4 = new LinearGradient(0, 0, 0, rf4.height(),
				new int[] { 0xbabababa, 0xe9e9e9e9 }, null, TileMode.CLAMP);
		rg = new RadialGradient(rf5.centerX() + rf5.width()
				* 0.05f, rf5.centerY() + rf5.height() * 0.05f,
				rf5.width() * 0.6f, new int[] { 0xffff0000, 0x80000000 },
				new float[] { 0.77f, 1f }, TileMode.CLAMP);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// back circle's light
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		canvas.drawOval(rf1, mPaint);

		// back circle's ageLine
		mPaint.setColor(0xa6a6a6a6);
		mPaint.setStrokeWidth(2);
		mPaint.setStyle(Style.STROKE);
		canvas.drawOval(rf1, mPaint);

		//back circle's light edge
		paint2.setShader(shader);
		canvas.drawOval(rf1, paint2);

		// back circle's dark edge
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		canvas.drawOval(rf2, mPaint);
		paint2.setShader(shader2);
		canvas.drawOval(rf2, paint2);

		// middle button1's light edge
		drawOuterButtons(canvas, rf3);
		paint2.setShader(shader3);
		canvas.drawOval(rf3, paint2);
		
		// middle button2's dark part
		drawOuterButtons(canvas, rf3);
		paint2.setShader(shader4);
		canvas.drawOval(rf4, paint2);

		// center Button bk
		mPaint.setColor(Color.WHITE);
		canvas.drawOval(rf5, mPaint);

		// center'bk shadow 0xe0e0e0e0,0x89898989
		paint2.setShader(rg);
		canvas.drawOval(rf5, paint2);

		drawCenterButtons(canvas, rf5);
		
		//draw text label
		drawText(canvas, rf3);
		drawCenterText(canvas);
		
		super.onDraw(canvas);
	}

	/**draw outer circle buttons
	 * @param canvas
	 * @param r rect
	 */
	private void drawOuterButtons(Canvas canvas, RectF r) {
		float sc = DEF_START;
		int size = mChileLs.size();
		WheelButtonItem chile;
		mPaint.setStyle(Style.FILL);
		for (int i = 0; i < size; i++) {
			chile = mChileLs.get(i);
			mPaint.setColor(chile.backgroundColor);
			canvas.drawArc(r, sc, chile.value, true, mPaint);
			if (!centerSelect && i == selectIndex) {
				mPaint.setColor(selectColor);
				canvas.drawArc(r, sc, chile.value, true, mPaint);
			}
			sc += chile.value;
			mPaint.setColor(Color.WHITE);
			canvas.drawArc(r, sc, division, true, mPaint);

			sc += division;
		}

	}

	private void drawText(Canvas canvas, RectF r) {
		float sc = DEF_START;
		int size = mChileLs.size();
		WheelButtonItem chile;
		mPaint.setStyle(Style.FILL);
		for (int i = 0; i < size; i++) {
			chile = mChileLs.get(i);
			sc += chile.value;
			sc += division;
			float arg = sc - chile.value / 2;
			double mPiv = arg * (Math.PI / 180);
			float x = (r.width() / 2) * 0.8f * (float) Math.cos(mPiv)
					+ (((int) rf1.right >> 1)+mPaint.descent());
			float y = (r.height() / 2) * 0.8f * (float) Math.sin(mPiv)
					+ (((int) rf1.right >> 1)+mPaint.descent());
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setColor(chile.shaderColor);
			// canvas.drawText(chile.text, x+2, y+2, mPaint);
			canvas.drawText(chile.text, x + 1, y + 1, mPaint);
			mPaint.setColor(chile.textColor);
			canvas.drawText(chile.text, x, y, mPaint);
		}
	}

	private void drawCenterButtons(Canvas canvas, RectF rf) {
		int size=mChileCenterLs.size();
		WheelButtonItem chile;
//		float lastHu=0,hu=0;
		float childTop=rf.bottom;
//		float midleValue1=rf.height()/100*MIN_WEGHT;
		for(int i=0;i<size;i++){
			chile=mChileCenterLs.get(i);
//			if(chile.value<midleValue1){
//				chile.value=midleValue1;
//			}
//			hu=chile.value/rf.height() *360+lastHu;
//			lastHu=hu;
			childTop-=chile.value;
			if (chile.visibility) {
				canvas.save();
				if(i==0){
					canvas.clipRect(rf.left-3, childTop, rf.right+3, childTop+chile.value+3);
				}else if(i==size-1){
					canvas.clipRect(rf.left-3, childTop-3, rf.right+3, childTop+chile.value);
				}else{
					canvas.clipRect(rf.left-3, childTop, rf.right+3, childTop+chile.value);
				}
				mPaint.setColor(chile.backgroundColor);
				mPaint.setStyle(Style.FILL);
				canvas.drawOval(rf, mPaint);
				if (centerSelect && selectIndex == i) {
					mPaint.setColor(selectColor);
//					canvas.drawArc(rf, (180 - hu) / 2, hu, false, mPaint);
					canvas.drawOval(rf, mPaint);
				}
				canvas.restore();
				//debug
				/*mPaint.setColor(Color.RED);
				mPaint.setStyle(Style.STROKE);
				canvas.drawRect(rf.left, ch, rf.right, ch+chile.value, mPaint);*/
			}
			
		}
		if(childTop<rf.top){
		childTop-=ageHeight-1;
		canvas.save();
		canvas.clipRect(rf.left, childTop, rf.right, childTop+ageHeight);
		mPaint.setColor(0x88bcbcbc);
		mPaint.setStyle(Style.FILL);
		canvas.drawOval(rf, mPaint);
		canvas.restore();
		}
	}

	private void drawCenterText(Canvas canvas) {
		WheelButtonItem chile;
		float ch = rf5.bottom;
		mPaint.setTextAlign(Paint.Align.CENTER);
		for (int i = 0; i < mChileCenterLs.size(); i++) {
			chile = mChileCenterLs.get(i);
			ch -= chile.value;
			if (chile.visibility) {
				mPaint.setColor(chile.shaderColor);
				// canvas.drawText(chile.text, rf5.centerX()+2, ch + ((int)
				// chile.value >> 1)+2, mPaint);
				canvas.drawText(chile.text, rf5.centerX() + 1, ch
						+ ((int) chile.value >> 1) + 1, mPaint);
				mPaint.setColor(chile.textColor);
				canvas.drawText(chile.text, rf5.centerX(), ch
						+ ((int) chile.value >> 1), mPaint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isSquare) {
			int mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
			int mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if(mScreenWidth<mScreenHeight){
				setMeasuredDimension(mScreenWidth, mScreenWidth);
			}else{
				setMeasuredDimension(mScreenHeight, mScreenHeight);
			}
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			Log.d(TAG, "onSizeChanged w " + w + " h" + h);
			initDraw(this.getWidth(), this.getHeight());
			this.initValue(ITEM_TYPE_CENTER);
			this.initValue(ITEM_TYPE_CIRCLE);
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * ���̧��ʱ�ĽǶȣ���ȡ���ܵ�ChartProp
	 * 
	 * @param ̧��ʱ�ĽǶ�
	 * @return ���ܵ�charProp����Ϊ��Ҫ�ж��ǲ�����Բ�ڡ�
	 */
	private int getPosibleChartProp(double angle) {
		int size = this.mChileLs.size();
		if (angle < 0) {
			angle += 360;
		}
		int ch = 0;
		for (int i = 0; i < size; i++) {
			ch += mChileLs.get(i).value;
			if (angle <= ch) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ��ȡ��̧��ʱ��������ڵ�charProp
	 * 
	 * @param x
	 *            action_up's x upʱ��x���
	 * @param y
	 *            action_up's y upʱ��y���
	 * @return ����ֵΪnull��˵�������κε������ڡ�
	 */
	private int getUpChartProp(float x, float y) {
		double angle = Math.atan2(y - mCenterPoint.y, x - mCenterPoint.x) * 180
				/ Math.PI-DEF_START;
		if (angle < 0) {
			angle = 360 + angle;
		}
		Log.d("test", "up angle = " + angle);

		return getPosibleChartProp(angle);
	}

	private int getCenterIndex(float y) {
		int size = mChileCenterLs.size();
		int ch = (int) rf5.height();
		// float percent=(float)mCenterBtnH/mCenterBtnWeightCount;
		for (int i = 0; i < size; i++) {
			ch -= mChileCenterLs.get(i).value;// mChileCenterLs.get(i).value*percent;
			if (y >= ch) {
				return i;
			}
		}
		return -1;
	}

	private double getDistance(int x, int y, float x1, float y1) {
		return Math.sqrt(Math.pow(Math.abs(x - x1), 2)
				+ Math.pow(Math.abs(y - y1), 2));
	}

	public void setOnItemClickListener(
			OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	public WheelButtonItem getCircleButton(int location) {
		if (location < 0 || location >= mChileLs.size()) {
			return null;
		}
		return mChileLs.get(location);
	}

	public WheelButtonItem getCenterButton(int location) {
		if (location < 0 || location >= mChileCenterLs.size()) {
			return null;
		}
		return mChileCenterLs.get(location);
	}

	public void addCircleButton(WheelButtonItem chile) {
		if(chile.weight<0){
			chile.weight=-chile.weight;
		}
		mChileLs.add(chile);
		this.mBtnWeightCount += chile.weight;
		initValue(WheelButton.ITEM_TYPE_CIRCLE);
	}

	public void addCenterButton(WheelButtonItem chile) {
		if(chile.weight<0){
			chile.weight=-chile.weight;
		}
		mChileCenterLs.add(chile);
		this.mCenterBtnWeightCount += chile.weight;
		initValue(WheelButton.ITEM_TYPE_CENTER);
	}
	/**
	 * @param weightSum �ܱ���
	 * @param minValue ��Сֵ
	 * @param division ���
	 * @param totleWeight ����childItem��weight��
	 * @param items ����Child Items
	 */
	private void calcuValue(float weightSum,float minValue,float division,float totleWeight,List<WheelButtonItem> items){
		int size = items.size();
		float average=weightSum*MIN_WEGHT/100;
		if(average<=division){
			division=0;
		}
		float percent = ((weightSum - (size-1) * division-size*average)) / totleWeight;
		WheelButtonItem item;
		for (--size; size >= 0; size--) {
			item = items.get(size);
			item.value = percent * item.weight+average;
		}
	}

	private void initValue(int type) {
		if (type == WheelButton.ITEM_TYPE_CIRCLE) {
			int size = mChileLs.size();
			if (size > 1) {
				calcuValue(360,MIN_WEGHT,division,mBtnWeightCount,mChileLs);
			} else if (size == 1) {
				mChileLs.get(0).value = 360;
			}
		} else if (type == WheelButton.ITEM_TYPE_CENTER) {
			float weight;
			if (mCenterUserWeightCount > 0) {
				weight=mCenterUserWeightCount;
			} else {
				weight=mCenterBtnWeightCount;
			}
			calcuValue(rf5.height(),MIN_WEGHT,division,weight,mChileCenterLs);
		}
	}

	private void setTextSize(float textSize) {
		mPaint.setTextSize(textSize);
	}

	public float getmCenterUserWeightCount() {
		return mCenterUserWeightCount;
	}

	public void setmCenterUserWeightCount(float mCenterUserWeightCount) {
		this.mCenterUserWeightCount = mCenterUserWeightCount;
	}

	public void clear() {
		this.mBtnWeightCount = 0;
		this.mCenterBtnWeightCount = 0;
		this.mChileCenterLs.clear();
		this.mChileLs.clear();
	}
	
	/**{@link #ITEM_TYPE_CIRCLE },{@link #ITEM_TYPE_CENTER}
	 * @param position
	 * @param flag
	 * @return
	 */
	public WheelButtonItem getItem(int position,int flag){
	    if(flag==ITEM_TYPE_CIRCLE){
	        return getCircleButton(position);
	    }else if(flag==ITEM_TYPE_CENTER){
	    	return getCenterButton(position);
	    }
	    return null;
	}
	
	public interface OnItemClickListener {
        void onItemClick(WheelButton parent, WheelButtonItem child, int position, int flag);
    }

}
