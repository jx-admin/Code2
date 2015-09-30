
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * 该类是一个球，点show会展开
 * 
 * @author seekting.x.zhang
 */
public class BankRollView extends LinearLayout implements ShowAble, View.OnClickListener {

    private ImageButton show, close;

    private View content;

    /**
     * @return the content
     */
    public View getContent() {
        return content;
    }

    public int height_top;

    public int height_bottom;

    private ShowListener showListener;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    public BankRollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init() {
        show = (ImageButton)findViewById(R.id.show_img);
        close = (ImageButton)findViewById(R.id.close_img);
        show.setOnClickListener(this);
        close.setOnClickListener(this);
        height_top = (int)(BaseActivity.cycle_top_height * BaseActivity.screen_height);

        height_bottom = (int)(height_top * BaseActivity.cycle_bottom_height);
        LayoutParams layoutParams = (LayoutParams)show.getLayoutParams();
        layoutParams.height = height_bottom;

        LayoutParams layoutParams1 = (LayoutParams)close.getLayoutParams();

        layoutParams1.height = height_top;

    }

    public void setShowImage(int id) {
    	if(R.drawable.btn_investment_show_selector == id){
    	  mGaInstance = GoogleAnalytics.getInstance(getContext());
          mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
          mGaTracker1.sendView("event.investments.deposit");  
          mGaTracker1.sendView("event.investments.asset");
    	}
        show.setImageResource(id);
    }

    public void setCloseImage(int id) {

        close.setImageResource(id);
    }

    public void setContent(View v) {

        this.removeAllViews();
        this.addView(close);
        this.addView(v);
        this.addView(show);
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

    public ShowListener getShowListener() {
        return showListener;
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }

}
