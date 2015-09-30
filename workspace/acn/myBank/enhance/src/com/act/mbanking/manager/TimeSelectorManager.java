
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.act.mbanking.R;
import com.act.mbanking.manager.view.TimeItem;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.manager.view.TimeSelector;

/**
 * 界面下面的一排钮扣按钮
 * 
 * @author seekting.x.zhang
 */
public class TimeSelectorManager extends ViewManager implements OnClickListener {

    List<TimeItem.TimeText> list;

    LinearLayout time_item_layout;

    private int selectedRes = R.drawable.timescale_btn_off;

    private int unSelectedRes = R.drawable.timescale_btn_on;

    TimeSelector timeSelector;

    public int getSelectedRes() {
        return selectedRes;
    }

    public void setSelectedRes(int selectedRes) {
        this.selectedRes = selectedRes;
    }

    public int getUnSelectedRes() {
        return unSelectedRes;
    }

    /**
     * 设置选中的item
     * 
     * @param posiion
     */
    public void setSelected(int posiion) {

        int j = 0;
        int childPosiion = 0;
        for (int i = 0; i < time_item_layout.getChildCount(); i++) {
            View child = time_item_layout.getChildAt(i);
            childPosiion = i;
            if (child.getTag() != null) {
                if (j == posiion) {

                    break;
                }
                j++;

            }
        }

        setSelectedBackground(time_item_layout.getChildAt(childPosiion));
    }

    /**
     * 获取当前选中的item
     * 
     * @return
     */
    public int getSelected() {

        for (int i = 0; i < time_item_layout.getChildCount(); i++) {

            View child = time_item_layout.getChildAt(i);
            if (child.getTag() != null) {

                if (child.isSelected()) {
                    return i;
                }

            }
        }
        return 0;
    }

    /**
     * 设置钮扣按钮
     * 
     * @param selectedRes
     * @param unSelectedRes
     */
    public void setSelectStateRes(int selectedRes, int unSelectedRes) {

        this.selectedRes = selectedRes;
        this.unSelectedRes = unSelectedRes;
    }

    public void setUnSelectedRes(int unSelectedRes) {
        this.unSelectedRes = unSelectedRes;
    }

    public TimeSelectorManager(Activity activity, ViewGroup root) {
        super(activity, root);
        list = new ArrayList<TimeItem.TimeText>();
    }

    /**
     * 如果添加完后要调用: {@link #generateButton()}
     * 
     * @param time
     */
    public void addTime(TimeItem.TimeText time) {

        list.add(time);
    }

    public void clear() {
        list.clear();
        time_item_layout.removeAllViews();
    }

    /**
     * 此方法用来生成按钮并显示在界面上
     */
    public void generateButton() {

        addAllView();
    }

    @Override
    protected void init() {

        view = root.findViewById(R.id.time_selector);

        initSubView();

    }

    private View generateView(int i) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LayoutParams.FILL_PARENT);
        if (i == -1 || i == list.size()) {
            layoutParams.weight = 1.5f;
        } else {
            layoutParams.weight = 1;

        }
        View v = new View(activity);
        v.setLayoutParams(layoutParams);
        return v;
    }

    private void addAllView() {

        time_item_layout.removeAllViews();
        if (list.size() > 0) {

            time_item_layout.setWeightSum(list.size() + 2);
            time_item_layout.addView(generateView(-1));

            for (int i = 0; i < list.size(); i++) {
                TimeText timeText = list.get(i);

                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                TimeItem timeItem = (TimeItem)layoutInflater.inflate(R.layout.time_item, null);
                timeItem.init();

                timeItem.setSelectedBackGrounnd(selectedRes, unSelectedRes);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                timeItem.setTag(timeText);
                timeItem.setTimeText(timeText);
                timeItem.setLayoutParams(layoutParams);
                timeItem.setOnClickListener(this);
                timeItem.setSelected(false);
                time_item_layout.addView(timeItem);
                time_item_layout.addView(generateView(i));

            }
        }
    }

    private OnTimeItemSelectedListener onTimeItemSelectedListener;

    public OnTimeItemSelectedListener getOnTimeItemSelectedListener() {
        return onTimeItemSelectedListener;
    }

    public void setOnTimeItemSelectedListener(OnTimeItemSelectedListener onTimeItemSelectedListener) {
        this.onTimeItemSelectedListener = onTimeItemSelectedListener;
    }

    @Override
    public void onClick(View v) {

        setSelectedBackground(v);
        if (onTimeItemSelectedListener != null) {

            TimeItem.TimeText timeItem = (TimeItem.TimeText)v.getTag();
            int position = list.indexOf(timeItem);
            onTimeItemSelectedListener.onTimeSelected(timeItem, position);
        }

    }

    private void setSelectedBackground(View v) {
        for (int i = 0; i < time_item_layout.getChildCount(); i++) {
            View child = time_item_layout.getChildAt(i);
            if (child.getTag() != null) {
                if (v == child) {
                    child.setSelected(true);
                } else {
                    child.setSelected(false);
                }
            }
        }
    }

    /**
     * 钮扣被点击的监听器
     * 
     * @author seekting.x.zhang
     */
    public static interface OnTimeItemSelectedListener {

        /**
         * 钮扣被点中所触发的事件
         * 
         * @param timeItem :被选中的对象
         * @param position：被选中的位置
         */
        public void onTimeSelected(TimeItem.TimeText timeText, int position);
    }

    @Override
    protected void initSubView() {
        time_item_layout = (LinearLayout)view.findViewById(R.id.time_item_layout);
        timeSelector = (TimeSelector)view.findViewById(R.id.time_selector);
        timeSelector.init();
    }

    public void generateDefaultVerticalTimes() {
        int monthId = 5;
        for (int i = 0; i < 5; i++) {
            TimeItem.TimeText3 month = new TimeItem.TimeText3();
            month.num3 = monthId + "";
            if(i<4){
                month.time3 = activity.getString(R.string.months);//"months";
            }else if(i==4){
                month.time3 = activity.getString(R.string.month);//"month";
            }
//            if(i<1){
//            }else{
//            }
            addTime(month);
            monthId--;
        }

        TimeItem.TimeText1 today = new TimeItem.TimeText1();
        today.time1 = activity.getString(R.string.today);
        addTime(today);
    }

    public void generateDefaultHorizontalTimes() {

        TimeItem.TimeText2 oneYear = new TimeItem.TimeText2();
        oneYear.num2 = "1";
        oneYear.time2 =activity.getString(R.string.year);//"year";
        addTime(oneYear);
        TimeItem.TimeText2 halfYear = new TimeItem.TimeText2();
        halfYear.num2 = "6";
        halfYear.time2 = activity.getString(R.string.months);//"months";
        addTime(halfYear);

        TimeItem.TimeText2 oneMonth = new TimeItem.TimeText2();
        oneMonth.num2 = "1";
        oneMonth.time2 = activity.getString(R.string.month);//"month";
        addTime(oneMonth);

        TimeItem.TimeText2 oneWeek = new TimeItem.TimeText2();
        oneWeek.num2 = "1";
        oneWeek.time2 = activity.getString(R.string.week);//"week";
        addTime(oneWeek);

        TimeItem.TimeText2 oneDay = new TimeItem.TimeText2();
        oneDay.num2 = "1";
        oneDay.time2 = activity.getString(R.string.day);//"day";
        addTime(oneDay);

    }
}
