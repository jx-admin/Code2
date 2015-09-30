package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.view.protocol.CloseListener;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

/**
 * 该类是一个球，点show会展开
 * 
 * @author seekting.x.zhang
 */
public class BankRollView extends LinearLayout implements ShowAble,
		View.OnClickListener {

	private ImageButton show, close;

	private ImageView ball_icon;

	private View content;

	private TextView ball_name;
	
	private boolean isScale = true;

	public int height_top;

	public int height_bottom;

	private ShowListener showListener;
	private CloseListener closeListener;
	public BankRollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * 上半圆占的高度比例
	 */
	public static final double cycle_top_height = 110d / 360d;

	/**
	 * 上半圆与下半圆的比例
	 */
	public static final double cycle_bottom_height = 101d / 294;
	
	
	
	 /**
     * 上半圆占的高度比例
     */
    public static final double scale_cycle_top_height = 80d / 360d; //80

    /**
     * 上半圆与下半圆的比例
     */
    public static final double scale_cycle_bottom_height = 111d / 193;
	
	public void init() {
		close = (ImageButton) findViewById(R.id.close_img);
		ball_icon = (ImageView) findViewById(R.id.ball_icon);
		ball_name = (TextView) findViewById(R.id.ball_name);
		show = (ImageButton) findViewById(R.id.show_img);
		show.setOnClickListener(this);
		close.setOnClickListener(this);
		
		if(this.isScale) {
			height_top = (int)(cycle_top_height * BaseActivity.screen_height);
	        height_bottom = (int)(height_top * cycle_bottom_height) + 1 ;
			
	        close.getLayoutParams().height = height_top;
	        show.getLayoutParams().height = height_bottom;
		}else{
			height_top = (int)(scale_cycle_top_height * BaseActivity.screen_height);

	        height_bottom = (int)(height_top * scale_cycle_bottom_height);
	        LayoutParams layoutParams = (LayoutParams)show.getLayoutParams();
	        layoutParams.height = height_bottom;
	        
	        LayoutParams layoutParams1 = (LayoutParams)close.getLayoutParams();
	        layoutParams1.height = height_top;
		}
	}

	public void setBallName(int id) {
		ball_name.setText(id);
	}

	public void setBallIcon(int id) {
		ball_icon.setImageResource(id);
	}

	public void setShowImage(int id) {
		show.setImageResource(id);
	}

	public void setCloseImage(int id) {
		close.setImageResource(id);
		if (content==null||content.getVisibility() == View.GONE) {
			close.setEnabled(false);
		}
	}

	public void setContent(View v) {
		this.addView(v);
		this.content = v;
		v.setVisibility(View.GONE);
		requestLayout();
	}

	@Override
	public void show() {
		if (content != null) {
			content.setVisibility(View.VISIBLE);
			if (content.getVisibility() == View.GONE) {
			} else {
				show.setVisibility(View.GONE);
				close.setEnabled(true);
			}
		}
		if (showListener != null) {
			showListener.onShow(this);
		}
	}

	@Override
	public void close() {
		if (content != null) {
			content.setVisibility(View.GONE);
			show.setVisibility(View.VISIBLE);
			close.setEnabled(false);
		}
		
		if(closeListener !=null){
			closeListener.onClose();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == show) {
			show();
		} else if (v == close) {
			close();
		}
	}

	public CloseListener getCloseListener() {
		return closeListener;
	}

	public void setCloseListener(CloseListener closeListener) {
		this.closeListener = closeListener;
	}
	
	public ShowListener getShowListener() {
		return showListener;
	}

	public void setShowListener(ShowListener showListener) {
		this.showListener = showListener;
	}

	public void disableCloseButton() {
		close.setOnClickListener(null);
	}

	public void setIsScale(boolean isScale) {
		this.isScale = isScale;
	}
	
	/**
	 * @return the content
	 */
	public View getContent() {
		return content;
	}
}
