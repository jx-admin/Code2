
package com.act.mbanking.manager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.act.mbanking.R;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.utils.LogManager;

/**
 * com.act.mbanking.manager.view.LegendItem
 * 
 * @author seekting.x.zhang
 */
public class LegendItem extends RelativeLayout {
    public static final int ColorAdd = 10;

    TextView legend_title_tv;

    TextView legend_opened_tv;

    ImageView legend_cycle_img;

    boolean isOpen = false;

    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public LegendItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {

        legend_title_tv = (TextView)findViewById(R.id.legend_title_tv);
        legend_opened_tv = (TextView)findViewById(R.id.legend_opened_tv);
        legend_cycle_img = (ImageView)findViewById(R.id.legend_cycle_img);
        if (isOpen) {
            open();
        } else {
            close();
        }
    }

    public void setUi(String textTitle, String textOpened, Drawable cycleDrawable,
            Drawable openedDrawable) {

        legend_title_tv.setText(textTitle);

        legend_opened_tv.setText(textOpened);
        legend_cycle_img.setImageDrawable(cycleDrawable);
        legend_opened_tv.setBackgroundDrawable(openedDrawable);
    }

    public void setUi(LegendItemText legendItemText) {

        this.setUi(legendItemText.titleText, legendItemText.openText, legendItemText.cycleDrawable,
                legendItemText.openDrawable);
    }

    public void close() {

        legend_opened_tv.setVisibility(View.GONE);
        isOpen = false;
    }

    public void open() {

        legend_opened_tv.setVisibility(View.VISIBLE);
        isOpen = true;
    }

    public static class LegendItemText {
        public String titleText;

        public String openText;

        public Drawable cycleDrawable;

        public Drawable openDrawable;

        public String type;

        public int backgroundColor;

        public void setData(String titleText, String openText, Drawable cycleDrawable,
                Drawable openDrawable) {

            this.titleText = titleText;
            this.openText = openText;
            this.cycleDrawable = cycleDrawable;
            this.openDrawable = openDrawable;
        }

        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the backgroundColor
         */
        public int getBackgroundColor() {
            return backgroundColor;
        }

        /**
         * @param backgroundColor the backgroundColor to set
         */
        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

    }

    int minWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        boolean needClose = false;
        if (legend_opened_tv.getVisibility() == View.GONE) {
            needClose = true;
            legend_opened_tv.setVisibility(View.VISIBLE);
        }
        BaseActivity baseActivity = (BaseActivity)getContext();
        minWidth = baseActivity.screen_width / 5;
        LogManager.d("width=" + MeasureSpec.getSize(widthMeasureSpec));
        LogManager.d("height=" + MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // if (needClose) {
        // setMeasuredDimension(getMeasuredWidth(),
        // getMeasuredHeight() - legend_opened_tv.getMeasuredHeight());
        // }

        if (getMeasuredWidth() <= minWidth) {
            setMeasuredDimension(minWidth, getMeasuredHeight());

            super.onMeasure(MeasureSpec.makeMeasureSpec(minWidth, MeasureSpec.EXACTLY),
                    heightMeasureSpec);
            if (needClose) {
                setMeasuredDimension(getMeasuredWidth(),
                        getMeasuredHeight() - legend_opened_tv.getMeasuredHeight());
            }
        } else {
            super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    heightMeasureSpec);
            if (needClose) {
                int measureHeight = getMeasuredHeight();
                int legendHeight = legend_opened_tv.getMeasuredHeight();
                setMeasuredDimension(getMeasuredWidth(), measureHeight - legendHeight);
            }

        }
        if (needClose) {
            legend_opened_tv.setVisibility(View.GONE);
        }
        LogManager.d("width---" + getMeasuredWidth());
        LogManager.d("height---" + getMeasuredHeight());
    }
}
