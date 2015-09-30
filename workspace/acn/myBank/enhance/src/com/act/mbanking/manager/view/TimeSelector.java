
package com.act.mbanking.manager.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.act.mbanking.R;

public class TimeSelector extends LinearLayout {
    ImageView leftBg, rightBg;

    public TimeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        init();

    }

    public void init() {
        leftBg = (ImageView)findViewById(R.id.timescale_img_left);
        rightBg = (ImageView)findViewById(R.id.timescale_img_right);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            leftBg.setVisibility(View.GONE);
            rightBg.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            leftBg.setVisibility(View.VISIBLE);
            rightBg.setVisibility(View.VISIBLE);

        }
        super.onConfigurationChanged(newConfig);
    }

}
