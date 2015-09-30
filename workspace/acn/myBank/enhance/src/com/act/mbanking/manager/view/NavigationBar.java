
package com.act.mbanking.manager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.act.mbanking.R;

/**
 * com.act.mbanking.manager.view.NavigationBar
 * 
 * @author seekting.x.zhang
 */
public class NavigationBar extends RelativeLayout implements OnClickListener {

    TextView leftButton;

    public ImageButton rightButton;

    private OnNavigationClickListener onNavigationClickListener;

    public OnNavigationClickListener getOnNavigationClickListener() {
        return onNavigationClickListener;
    }

    public void setOnNavigationClickListener(OnNavigationClickListener onNavigationClickListener) {
        this.onNavigationClickListener = onNavigationClickListener;
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        leftButton = (TextView)findViewById(R.id.left_button);
        rightButton = (ImageButton)findViewById(R.id.right_button);
        if (null != rightButton) {
            rightButton.setOnClickListener(this);
        }
        if (null != leftButton) {
            leftButton.setOnClickListener(this);
        }

    }

    /**
     * 设置左侧文字
     * 
     * @param text
     */
    public void setLeftText(String text) {

        if (text == null || text.equals("")) {
            leftButton.setVisibility(View.GONE);
            return;
        }
        leftButton.setText(text);
        leftButton.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右侧图片
     * 
     * @param resId
     */
    public void setRightImg(int resId) {
        rightButton.setImageResource(resId);
        rightButton.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左侧图片
     * 
     * @param resId
     */
    public void setLeftImg(int resId) {
        if (resId == -1) {
            leftButton.setVisibility(View.INVISIBLE);
            return;
        }
        leftButton.setBackgroundResource(resId);

        leftButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (onNavigationClickListener != null) {
            if (v == leftButton) {
                onNavigationClickListener.onLeftClick(v);
            } else if (v == rightButton) {
                onNavigationClickListener.onRightClick(v);
            }
        }
    };

    public static interface OnNavigationClickListener {

        void onLeftClick(View v);

        void onRightClick(View v);
    }

}
