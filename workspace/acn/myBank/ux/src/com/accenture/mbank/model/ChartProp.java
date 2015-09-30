package com.accenture.mbank.model;

import com.accenture.mbank.view.table.ChartView;

import android.graphics.Color;
import android.view.View;

/**
 * customized  chartsViewProp
 * 自定义饼状图属性
 *
 */

public class ChartProp
{
	private int mId = 0;
	private float mPercent = 1.0f;
	private View mParent = null;
	private int mColor = Color.WHITE;
	private float mSweepAngle = 0f;
	private String mName = "";
	private int mFontSize = 20;
	private float mStartAngle = 0f;
	private float mEndAngle = 0f;
	
	public ChartProp(ChartView chartView)
	{
		mParent = chartView;
	}

	public void setId(int id)
	{
		mId = id;
		setName("Chart " + id);
	}

	public int getId()
	{
		return mId;
	}
	
	/**
	 * the percent ,such as 0.5f,0.05f, NEIGHTER 50% NOR 50
	 * @param percent
	 */
	public void setPercent(float percent)
	{
	    percent = percent/100;
		mPercent = percent;
		mSweepAngle = percent * 360;
		invalidate();
	}
		public float getPercent()
	{
		return mPercent*100;
	}
	
	public float getSweepAngle()
	{
		return mSweepAngle;
	}
	
	public void setColor(int color)
	{
		mColor = color;
		invalidate();
	}
	
	public int getColor()
	{
		return mColor;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		this.mName = name;
		invalidate();
	}
	
	public int getFontSize()
	{
		return mFontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.mFontSize = fontSize;
		invalidate();
	}
	
	private void invalidate()
	{
		if(mParent != null)
		{
			mParent.invalidate();
		}
	}

	public float getStartAngle()
	{
		return mStartAngle;
	}

	public void setStartAngle(float startAngle)
	{
		this.mStartAngle = startAngle;
	}

	public float getEndAngle()
	{
		return mEndAngle;
	}

	public void setEndAngle(float endAngle)
	{
		this.mEndAngle = endAngle;
	}



}
