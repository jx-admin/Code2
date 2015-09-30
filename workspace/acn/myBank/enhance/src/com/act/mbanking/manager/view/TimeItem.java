
package com.act.mbanking.manager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.act.mbanking.R;

public class TimeItem extends RelativeLayout {

    TextView time1;

    TextView num2, time2;

    TextView num3, time3, ago;

    ViewGroup time2_layout, time3_layout;

    public TimeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {

        time1 = (TextView)findViewById(R.id.time_1);
        time2 = (TextView)findViewById(R.id.time_2);
        num2 = (TextView)findViewById(R.id.num_2);

        time3 = (TextView)findViewById(R.id.time_3);
        num3 = (TextView)findViewById(R.id.num_3);
        ago = (TextView)findViewById(R.id.ago);
        time2_layout = (ViewGroup)findViewById(R.id.time_2_layout);
        time3_layout = (ViewGroup)findViewById(R.id.time_3_layout);

    }

    private void hideExcept(View v) {
        if (v == null) {
            return;
        }
        time2_layout.setVisibility(View.GONE);
        time3_layout.setVisibility(View.GONE);
        time1.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    public void setTimeText(TimeText t) {

        View visibleView = null;
        if (t instanceof TimeText1) {

            time1.setText(((TimeText1)t).time1);
            visibleView = time1;

        } else if (t instanceof TimeText2) {
            time2.setText(((TimeText2)t).time2);
            num2.setText(((TimeText2)t).num2);
            visibleView = time2_layout;

        } else if (t instanceof TimeText3) {
            time3.setText(((TimeText3)t).time3);
            num3.setText(((TimeText3)t).num3);
            ago.setText(R.string.ago);
            visibleView = time3_layout;

        }
        hideExcept(visibleView);
    }

    public void setSelectedBackGrounnd(int selected, int unselected) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        if (selected == 0 || unselected == 0) {
            throw new IllegalArgumentException(
                    "你忘记给timeSelector设置钮扣图片了亲!参考方法:timeSelectorManager.setSelectedRes(R.drawable.timescale_account__on)");
        }
        Drawable selectedDrawable = getResources().getDrawable(selected);
        Drawable unSelectedDrawable = getResources().getDrawable(unselected);

        stateListDrawable.addState(new int[] {
            android.R.attr.state_selected

        }, selectedDrawable);
        stateListDrawable.addState(new int[] {

        }, unSelectedDrawable);

        this.setBackgroundDrawable(stateListDrawable);
    }

    public static class TimeText {

    }

    public static class TimeText1 extends TimeText {

        public String time1;

        @Override
        public String toString() {
            return "TimeText1 [time1=" + time1 + "]";
        }

    }

    public static class TimeText2 extends TimeText {
        public String time2;

        public String num2;

        @Override
        public String toString() {
            return "TimeText2 [time2=" + time2 + ", num2=" + num2 + "]";
        }
    }

    public static class TimeText3 extends TimeText {
        public String time3;

        public String num3;

//        public String ago = "ago";

        @Override
        public String toString() {
            return "TimeText3 [time3=" + time3 + ", num3=" + num3 + ", ago=" /*+ ago*/ + "]";
        }
    }

}
