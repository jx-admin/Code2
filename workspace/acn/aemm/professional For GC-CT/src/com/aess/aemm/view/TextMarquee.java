package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TextMarquee extends View{
	public static Thread lrcThread;
	final static int delay = 50;

	static final String LogCat = "TextPlayView";
	private static final String ELLIPSIZE = "...";
	private static int ellipsizeLength;
	/** 方向常量 */
	public static final byte UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, NONE = -1;
	/** 播放状态常量 */
	static final byte STOP = 0, PLAY = 1, PAUSE = 2, DESTROYED = 3;
	/** 播放状态 */
	int state = STOP;
	/** 方向状态 */
	private int direct = LEFT;
	/** 画笔 */
	private Paint paint;
	private FontMetrics fm;
	private float offy, offx;
	private float speed = 1.2f;
	private int fFontHeight;
	private String textFull, textSimple, textCurrent, textSrc;
	int width;
	int height;
	int strLength;
	private static int space = 20;
	private static String strSpace;

	public TextMarquee(Context context) {
		super(context);
		paint = new TextView(context).getPaint();
		paint.setColor(Color.WHITE);
		setPaint(paint);
		if (strSpace == null) {
			strSpace = "           ";
			space = (int) paint.measureText(strSpace);
			ellipsizeLength = (int) paint.measureText(ELLIPSIZE);
		}
	}

	public void setText(String str) {
		textSrc = str;
		textFull = str;
		textSimple = null;
		textCurrent = textFull;
		strLength = (int) paint.measureText(str);
//		Log.d(LogCat, "strLength:" + strLength + " str:" + str);
	}

	public void start() {
		// if(strLength<=width||width<=0){
		// return;
		// }

		if (state == STOP) {
			direct = LEFT;
			offx = 0;
			offy = 0;
			textCurrent = textFull;
		}
		state = PLAY;
		// if(textSimple==null){
		// textSimple=getTextArr(textFull,(int)
		// (width-paint.measureText("...")))+"...";
		// textFull=textFull+strSpace+textSimple;
		// }
		if(MarqueeThread.ls.indexOf(this)<0){
			MarqueeThread.ls.add(this);
		}
		if (lrcThread==null) {
			lrcThread =new  MarqueeThread();
			lrcThread.setName("textMarqueeThread");
			lrcThread.start();
		} else {
			// lrcThread.notify();
		}
		// update();
	}

	public void toResume() {
		state = PLAY;
	}

	public void toStop() {
		state = STOP;
	}

	public void toPause() {
		state = PAUSE;
		// lrcThread.stop();
	}

	public void onDestroy() {
		state = DESTROYED;
//		if (lrcThread != null) {
//			lrcThread.stop();
//		}
	}

	public void setPaint(Paint p) {
		paint = p;
		fm = paint.getFontMetrics();
		fFontHeight = (int) Math.ceil(fm.bottom - fm.top + 0.9);
	}

	public static void interrupt(boolean isInterrupt){
		if(!isInterrupt){
			if(TextMarquee.lrcThread ==null){
				TextMarquee.lrcThread =new  MarqueeThread();
				TextMarquee.lrcThread.setName("textMarqueeThread");
				TextMarquee.lrcThread.start();
			}
		}else{
			if(TextMarquee.lrcThread !=null){
				((MarqueeThread)TextMarquee.lrcThread).isInterrupt=true;
			}
		}
	}
	

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();
		// paint.setColor(0xffff0000);
		// canvas.drawRect(new Rect(0,0,width,height), paint);
		canvas.drawText(textCurrent, offx, offy + fm.descent - fm.ascent,
						paint);
	}

	long mLastMove = 0;
	long mMoveDelay = 100;

	public void update() {
		if (state != STOP) {
			long now = System.currentTimeMillis();
			if (now - mLastMove > mMoveDelay) {
				if (state == PLAY) {
					logic();
				}
				mLastMove = now;
			}
			mRedrawHandler.sleep(mMoveDelay);
		}
	}

	protected void logic() {
		// Log.d("text","onDraw:"+strLength+",width:"+width+" height:"+height);
		if (state == STOP) {
			return;
		}
		if (width <= 0) {
			return;
		}
		if (strLength <= width) {
			state = STOP;
			textCurrent=textSrc;
			offx=0;
			return;
		}
		if (textSimple == null) {
			textSimple = getTextArr(textSrc, (int) (width - ellipsizeLength))+ ELLIPSIZE;
			textFull = textSrc + strSpace + textSimple;
			textCurrent = textFull;
		}
		if (direct == UP) {
			offy -= speed;
		} else if (direct == LEFT) {
			if (textCurrent == null) {
				return;
			}
			offx -= speed;
			if (offx + strLength + space < 0) {
				offx = 0;
				state = STOP;
				textCurrent = textSimple;
			}
		} else if (direct == RIGHT) {
			offx += speed;
		} else if (direct == NONE) {
			// if(width>0&&strLength>width){
			// direct=LEFT;
			// }
		}
	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			update();
			invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	public String getTextArr(String mStrText, int mTextWidth) {
		String str = null;// paint中str
		char ch;
		int w = 0;
		int istart = 0;
		int count = mStrText.length();
		for (int i = 0; i < count; i++) {
			ch = mStrText.charAt(i);
			str = String.valueOf(ch);
			w += paint.measureText(str);// (int) Math.ceil(widths[0]);
			if (w > mTextWidth) {
				i--;
				str = mStrText.substring(istart, i);
				break;
			}
		}
		return str;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}
		// The children are given the same width and height as the scrollLayout
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(fFontHeight,
				MeasureSpec.AT_MOST);
		@SuppressWarnings("unused")
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		@SuppressWarnings("unused")
		final int height = MeasureSpec.getSize(heightMeasureSpec);
//		Log.d(LogCat, "onMeasure(" + width + "," + height + ")");
		super.onMeasure(widthMeasureSpec, cellHeightSpec);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
//		Log.d(LogCat, "changed " + changed);
		if (changed) {
			width = right - left;
			state=PLAY;
			if (strLength <= width || width <= 0) {
				offx = 0;
				textCurrent = textSrc;
				state = STOP;
			} else {
				textSimple = getTextArr(textSrc,(int) (width - ellipsizeLength))+ ELLIPSIZE;
				textFull = textSrc + strSpace + textSimple;
				textCurrent = textFull;
				state = PLAY;
			}
		}
	}

}
final class MarqueeThread extends Thread{
	public static List<TextMarquee>ls=new ArrayList<TextMarquee>();

	public boolean isInterrupt;
	@Override
	public void run() {
		try {TextMarquee tm;
			while (!Thread.interrupted()) {
				for(int i=0;i<ls.size();i++){
					tm=ls.get(i);
				if (tm.state == TextMarquee.PLAY) {
					tm.logic();
					// invalidate();
					tm.postInvalidate();
				} else if (tm.state == TextMarquee.DESTROYED) {
					ls.remove(tm);
					i--;
					Log.d(TextMarquee.LogCat, "ls size:"+ls.size());
				}
				}
				if(isInterrupt||ls.size()<=0){
					isInterrupt=false;
					TextMarquee.lrcThread=null;
					interrupt();
					Log.d(TextMarquee.LogCat, "interrupt");
					start();
				}
				Thread.sleep(TextMarquee.delay);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			interrupt();
		}
	}
}