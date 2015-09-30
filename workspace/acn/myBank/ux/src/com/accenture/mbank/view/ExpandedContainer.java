
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExpandedContainer extends LinearLayout {

    protected boolean init = true;

    protected ExpandBarResultChangeListener expandBarResultListener;

    public ExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected Object getValue() {

        return null;
    };

    protected void resetResult() {
        init = true;
        expandBarResultListener.onReset();
    }

    public void setExpandBarResultChangeListener(ExpandBarResultChangeListener barResultListener) {

        expandBarResultListener = barResultListener;
    }

    /**
     * 因为没焦点而被设置
     * 
     * @param str
     * @param object
     */
    protected void expandFocusResultChange(final String str) {

        init = false;
        if (expandBarResultListener != null) {
            expandBarResultListener.onFocusChange(str);

        }
    }

    /**
     * 因为非焦点事件而改变的设置
     * 
     * @param str
     * @param object
     */
    protected void expandResultChange(final String str) {

        init = false;
        if (expandBarResultListener != null) {
            expandBarResultListener.onChange(str);

        }
    }

    public void onExpand() {

    }

    protected void onRecover(String text) {
        if (text == null || text.equals("")) {
            return;
        }

    }
    
    protected void onRecover(Object object) {
        if (object == null || object.equals("")) {
            return;
        }

    }

    public void setEditable(boolean flag) {

    }

    static interface ExpandBarResultChangeListener {

        void onChange(String str);

        void onFocusChange(String str);

        ViewGroup getOwener();

        void onReset();
    }

}
