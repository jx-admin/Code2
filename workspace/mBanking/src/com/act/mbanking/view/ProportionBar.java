
package com.act.mbanking.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProportionBar extends LinearLayout {
    LinearLayout.LayoutParams lParam = new LayoutParams(1, LayoutParams.FILL_PARENT);

    // public ProportionBar(Context context, AttributeSet attrs, int defStyle) {
    // super(context, attrs, defStyle);
    // // TODO Auto-generated constructor stub
    // }

    Drawable division_dra;

    public ProportionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ProportionBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void addItem(String text, int scale, int color) {
        TextView tv = new TextView(this.getContext());
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (scale == 0) {
            scale = 1;
        }
        lParam.weight = scale;
        tv.setLayoutParams(lParam);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(color);

        if (this.getChildCount() >= 1) {
            lParam.setMargins(2, 0, 0, 0);
        }
        this.addView(tv);
    }

    public void clear() {
        this.removeAllViews();
    }

    public void addItem(String text, int scale, Drawable background) {
        TextView tv = new TextView(this.getContext());
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (scale == 0) {
            scale = 1;
        }
        lParam.weight = scale;
        tv.setLayoutParams(lParam);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundDrawable(background);
        // if(this.division_dra!=null&&this.getChildCount()>=1){
        // ImageView iv=new ImageView(this.getContext());
        // iv.setImageDrawable(division_dra);
        // this.addView(iv);
        // }

        if (this.getChildCount() >= 1) {
            lParam.setMargins(2, 0, 0, 0);
        }
        this.addView(tv);
    }

    public void addItemByRes(String text, int scale, int resid) {
        TextView tv = new TextView(this.getContext());
        tv.setLines(1);
        tv.setMinEms(2);
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lParam = new LayoutParams(0, LayoutParams.FILL_PARENT);
        lParam.weight = scale;
        tv.setLayoutParams(lParam);
        tv.setText(text);
        tv.setBackgroundResource(resid);
        // if(this.division_dra!=null&&this.getChildCount()>=1){
        // ImageView iv=new ImageView(this.getContext());
        // iv.setImageDrawable(division_dra);
        // this.addView(iv);
        // }

        if (this.getChildCount() >= 1) {
            lParam.setMargins(2, 0, 0, 0);
        }
        this.addView(tv);
    }

    public void setDivision(Drawable division) {
        division_dra = division;
    }

}
