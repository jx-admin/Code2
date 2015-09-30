
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.R;

public class RotateTitleView extends LinearLayout {

    private ImageView titleIcon;

    private TextView accountText;

    private TextView updateText;

    public RotateTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        titleIcon = (ImageView)findViewById(R.id.title_icon);
        accountText = (TextView)findViewById(R.id.accountTitle);
        updateText = (TextView)findViewById(R.id.updateTitle);
    }

    public void setTitleText(String account) {
        accountText.setText("" + account);
    }

    public void setUpdateTitle(String update) {
        updateText.setText("" + update);
    }

    public void setTitleIcon(int id) {
        titleIcon.setImageResource(id);
    }

}
