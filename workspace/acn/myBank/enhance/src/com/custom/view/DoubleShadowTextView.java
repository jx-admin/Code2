package com.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author junxu.wang
 *
 */
public class DoubleShadowTextView extends TextView{
    private int shadowColorSelected=Color.WHITE;
    private int shadowColorDefault=Color.BLACK;

    public DoubleShadowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public DoubleShadowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public DoubleShadowTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        // TODO Auto-generated method stub
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setSelected(boolean selected) {
        if(selected){
            this.setShadowLayer(1, 0, 1, shadowColorSelected);
        }else{
            this.setShadowLayer(1, 0, 1, shadowColorDefault);
        }
        super.setSelected(selected);
    }
    
    public void setShadowColorDefault(int c){
        this.shadowColorDefault=c;
    }
    
    public void setShadowColorSelected(int c){
        this.shadowColorSelected=c;
    }
    

}
