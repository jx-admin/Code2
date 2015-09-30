
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RotateTitleView extends RelativeLayout {

    private ImageView titleIcon;

    private TextView accountText;

    private TextView updateText;

    private ImageView rotateIcon;

    public RotateTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        titleIcon = (ImageView)findViewById(R.id.title_icon);
        accountText = (TextView)findViewById(R.id.accountTitle);
        updateText = (TextView)findViewById(R.id.updateTitle);
        rotateIcon = (ImageView)findViewById(R.id.title_rotate_icon);
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

    public void disableRotateImage() {
    	rotateIcon.setVisibility(View.INVISIBLE);
    }
}
