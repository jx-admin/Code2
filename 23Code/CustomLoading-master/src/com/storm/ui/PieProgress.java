/*
 * Copyright (C) 2013 Storm Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.storm.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.storm.customloading.R;

/**
 * Class used internally by {@link RefreshActionItem} to show a determinate
 * progress indicator. Two display modes are supported "wheel" and "pie"
 */
class PieProgress extends View {
	private final RectF mRect = new RectF();
	private final RectF mRectInner = new RectF();
	private final Paint mPaintForeground = new Paint();
	private final Paint mPaintBackground = new Paint();
	private final Paint mPaintErase = new Paint();
	private static final Xfermode PORTER_DUFF_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private int mColorForeground = Color.WHITE;
	private int mColorBackground = Color.BLACK;
	private int mProgress;
	/**
	 * Value which makes our custom drawn indicator have roughly the same size
	 * as the built-in ProgressBar indicator. Unit: dp
	 */
	private static final float PADDING = 4;
	private float mPadding;
	private Bitmap mBitmap;
	/**
	 * Value which makes our custom drawn indicator have roughly the same
	 * thickness as the built-in ProgressBar indicator. Expressed as the ration
	 * between the inner and outer radiuses
	 */
	private static final float INNER_RADIUS_RATIO = 0.84f;

	public PieProgress(Context context) {
		this(context, null);
	}

	public PieProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.PieProgress));
		
		Resources r = context.getResources();
		float scale = r.getDisplayMetrics().density;
		mPadding = scale * PADDING + r.getDimension(R.dimen.actionbar_vertival_padding);
		mPaintForeground.setColor(mColorForeground);
		mPaintForeground.setAntiAlias(true);
		mPaintBackground.setColor(mColorBackground);
		mPaintBackground.setAntiAlias(true);
		mPaintErase.setXfermode(PORTER_DUFF_CLEAR);
		mPaintErase.setAntiAlias(true);
	}
	
	/**
	 * Parse the attributes passed to the view from the XML
	 * 
	 * @param a
	 *            the attributes to parse
	 */
	private void parseAttributes(TypedArray a) {
		mColorForeground = a.getColor(R.styleable.PieProgress_foregroundColor, mColorForeground);
		mColorBackground = a.getColor(R.styleable.PieProgress_backgroundColor, mColorBackground);
		// Recycle
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2, getHeight() / 2
				- mBitmap.getHeight() / 2, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float bitmapWidth = w - 2 * mPadding;
		float bitmapHeight = h - 2 * mPadding;
		float radius = Math.min(bitmapWidth / 2, bitmapHeight / 2);
		mRect.set(0, 0, bitmapWidth, bitmapHeight);
		radius *= INNER_RADIUS_RATIO;
		mRectInner.set(bitmapWidth / 2f - radius, bitmapHeight / 2f - radius, bitmapWidth / 2f
				+ radius, bitmapHeight / 2f + radius);
		updateBitmap();
	}

	/**
	 * Set the foreground color for this indicator. The foreground is the part
	 * of the indicator that shows the actual progress
	 */
	public void setForegroundColor(int color) {
		this.mColorForeground = color;
		mPaintForeground.setColor(color);
		invalidate();
	}

	/**
	 * Set the background color for this indicator. The background is a dim and
	 * subtle part of the indicator that appears below the actual progress
	 */
	public void setBackgroundColor(int color) {
		this.mColorBackground = color;
		mPaintBackground.setColor(color);
		invalidate();
	}

	/**
	 * @param progress
	 *            A number between 0 and 360
	 */
	public synchronized void setProgress(int progress) {
		mProgress = progress;
		if (progress > 360) {
			mProgress = 360;
		}
		updateBitmap();
	}
	
	public void reset() {
		mProgress = 0;
		updateBitmap();
	}

	private void updateBitmap() {
		if (mRect == null || mRect.width() == 0) {
			return;
		}
		mBitmap = Bitmap.createBitmap((int) mRect.width(), (int) mRect.height(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		canvas.drawArc(mRect, -90, 360, true, mPaintBackground);
		if (mProgress < 3) {
			canvas.drawLine(mRect.width() / 2, mRect.height() / 2, mRect.width() / 2, 0,
					mPaintForeground);
		}
		canvas.drawArc(mRect, -90, mProgress, true, mPaintForeground);
		postInvalidate();
	}
}
