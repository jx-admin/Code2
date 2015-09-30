
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BasePushContainer extends ExpandedContainer {
    public BasePushContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
    }
}