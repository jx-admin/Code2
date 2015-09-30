
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import com.accenture.mbank.MainActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class TabHeaderControl extends LinearLayout implements OnClickListener {

    public TabHeaderControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            child.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        MainActivity mainActivity = (MainActivity)getContext();
        int i = indexOfChild(v);
        if (getId() != R.id.userinfo) {
            if (i == 0) {
                mainActivity.showTab(0);
                return;
            }
        }
        mainActivity.showTab(i);
    }
}
