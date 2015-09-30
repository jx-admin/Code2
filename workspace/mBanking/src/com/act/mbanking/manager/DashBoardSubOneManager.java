
package com.act.mbanking.manager;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.act.mbanking.App;
import com.act.mbanking.R;
import com.act.mbanking.manager.LegendManager.OnLegendItemSelectedListener;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.TimeItem;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.custom.view.WheelButton;
import com.custom.view.WheelButtonItem;

public class DashBoardSubOneManager extends ViewManager implements OnTimeItemSelectedListener,
        com.custom.view.WheelButton.OnItemClickListener, OnLegendItemSelectedListener {
    public DashBoardSubOneManager(Activity activity, ViewGroup root) {
        super(activity, root);
    }

    LegendManager legendManager;

    public WheelButton chartButton;

    TimeSelectorManager monthSelectorManager;

    TextView total_assets;

    TextView lastUpdate;

    TextView totalTitle;

    @Override
    protected void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        view = layoutInflater.inflate(R.layout.dashboard_sub_1, null);
        totalTitle = (TextView)view.findViewById(R.id.total);
        total_assets = (TextView)view.findViewById(R.id.money);
        lastUpdate = (TextView)view.findViewById(R.id.update_time);
        initSubView();
    }

    @Override
    protected void initSubView() {
        legendManager = new LegendManager(activity, (ViewGroup)view);
        legendManager.setOnLegendItemSelectedListener(this);
        monthSelectorManager = new TimeSelectorManager(activity, (ViewGroup)view);
        monthSelectorManager.setOnTimeItemSelectedListener(this);

        chartButton = (WheelButton)view.findViewById(R.id.cb);
        chartButton.setOnItemClickListener(this);
    }

    /**
     * 添加legendItem
     * 
     * @param legendItemText
     */
    public void addLegendItem(LegendItemText legendItemText) {
        legendManager.addLegendItem(legendItemText);
    }

    /**
     * 添加legendItem之后生成ui
     */
    public void generateLegend() {
        legendManager.generateUi();

    }

    /**
     * 清空所有legend
     */
    public void clearLegend() {
        legendManager.clear();
    }

    /**
     * 添加钮扣
     * 
     * @param timeText
     */
    public void addTime(TimeItem.TimeText timeText) {
        monthSelectorManager.addTime(timeText);
    }

    public void selectedTime(int posiion) {
        monthSelectorManager.setSelected(posiion);
    }

    /**
     * 清空所有的扭扣
     */
    public void clearTime() {
        monthSelectorManager.clear();
    }

    /**
     * 设置钮扣的图片
     * 
     * @param selected
     * @param unslected
     */
    public void setTimeSelectDrawableRes(int selected, int unslected) {
        monthSelectorManager.setSelectStateRes(selected, unslected);
    }

    public void generateTimeButton() {
        monthSelectorManager.generateButton();
    }

    public void setTotalAssets(String text) {
        total_assets.setText(text);
    }

    public void setTotalTitle(String text) {
        totalTitle.setText(text);
    }

    public void setLastUpdate(String text) {
        lastUpdate.setText(text);
    }

    /**
     * 添加饼图元素（环状的）
     * 
     * @param itemButton
     */
    public void addCircleButton(WheelButtonItem itemButton) {

        chartButton.addCircleButton(itemButton);
    }

    public void generateMonthSelector() {
        monthSelectorManager.generateDefaultVerticalTimes();
    }

    /**
     * 添加饼图元素（中间的）
     * 
     * @param itemButton
     */
    public void addCenterButton(WheelButtonItem itemButton) {
        chartButton.addCenterButton(itemButton);
    }

    public void clear() {
        legendManager.clear();
        monthSelectorManager.clear();
        chartButton.clear();
    }

    void test() {

        legendManager = new LegendManager(activity, (ViewGroup)view);
        LegendItemText legendItemText = new LegendItemText();
        legendItemText.setData("cards", "1000", getDrawable(R.drawable.accounts_legend_closed),
                getDrawable(R.drawable.accounts_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("investments", "1000",
                getDrawable(R.drawable.investments_legend_closed),
                getDrawable(R.drawable.investments_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("cards", "1000", getDrawable(R.drawable.accounts_legend_closed),
                getDrawable(R.drawable.accounts_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("investments", "1000",
                getDrawable(R.drawable.investments_legend_closed),
                getDrawable(R.drawable.investments_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("cards", "1000", getDrawable(R.drawable.accounts_legend_closed),
                getDrawable(R.drawable.accounts_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("investments", "1000",
                getDrawable(R.drawable.investments_legend_closed),
                getDrawable(R.drawable.investments_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendManager.generateUi();

        monthSelectorManager.clear();
        for (int i = 0; i < 6; i++) {
            // 此处是为钮扣添加文字
            TimeItem.TimeText1 timeText4 = new TimeItem.TimeText1();
            timeText4.time1 = "today";
            monthSelectorManager.addTime(timeText4);
        }

        // 此处加入钮扣按钮，一个是选中的，一个是没选中的
        monthSelectorManager.setSelectStateRes(R.drawable.timescale_btn_off,
                R.drawable.timescale_btn_on);
        monthSelectorManager.generateButton();

        // monthSelectorManager.setOnTimeItemSelectedListener(this);
        // 此处是加入监听器
        WheelButton cb = (WheelButton)view.findViewById(R.id.cb);
        WheelButtonItem chile;
        for (int i = 0; i < 4; i++) {
            chile = new WheelButtonItem();
            chile.weight = (int)(Math.random() * 360);
            chile.backgroundColor = (int)(Math.random() * 0xffffff) + 0xff000000;
            if (i == 2) {
                chile.backgroundColor = Color.rgb(201, 40, 116);
            }
            if (i == 3) {
                chile.backgroundColor = Color.rgb(62, 62, 136);
            }
            if (i == 1) {
                chile.backgroundColor = Color.rgb(158, 177, 68);
            }
            if (i == 0) {
                chile.backgroundColor = Color.rgb(99, 153, 198);
            }
            chile.textColor = (int)(Math.random() * 0xffffff) + 0xff000000;
            chile.text = "btn" + i;

            cb.addCircleButton(chile);
        }
        chile = new WheelButtonItem();
        chile.text = "50%";
        chile.weight = (int)(Math.random() * 360);
        chile.backgroundColor = (int)(Math.random() * 0xffffff) + 0xff000000;

        cb.addCenterButton(chile);

    }

    @Override
    public void onTimeSelected(TimeText timeText, int position) {

        if (onDashboardSub1ClickListener != null) {
            onDashboardSub1ClickListener.onTimeClick(timeText, position);
        }
    }

    private OnDashboardSub1ClickListener onDashboardSub1ClickListener;

    public OnDashboardSub1ClickListener getOnDashboardSub1ClickListener() {
        return onDashboardSub1ClickListener;
    }

    public void setOnDashboardSub1ClickListener(
            OnDashboardSub1ClickListener onDashboardClickListener) {
        this.onDashboardSub1ClickListener = onDashboardClickListener;
    }

    public static interface OnDashboardSub1ClickListener {

        public void onTimeClick(TimeText timeText, int position);

        public void onChartButtonItemClick(View view, int position, long type);

        public void onLegendItemSelected(View view,LegendItemText legendItemText, int position);

    }

    @Override
    public void onItemClick(WheelButton parent, WheelButtonItem child, int position, int flag) {
        if (onDashboardSub1ClickListener != null) {
            onDashboardSub1ClickListener.onChartButtonItemClick(parent, position, flag);
        }
    }

    @Override
    public void onLegendItemSelected(View view,LegendItemText legendItemText, int position) {
        if (onDashboardSub1ClickListener != null) {
            onDashboardSub1ClickListener.onLegendItemSelected(view,legendItemText, position);
        }
    }

}
