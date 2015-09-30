
package com.act.mbanking.manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.act.mbanking.R;
import com.act.mbanking.manager.LegendManager.OnLegendItemSelectedListener;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.BankBitmapDrawable;
import com.act.mbanking.manager.view.LegendItem;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.TimeItem;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.view.ProportionBar;
import com.custom.view.WheelButton;
import com.custom.view.WheelButtonItem;

public class DashBoardManager extends ViewManager implements OnTimeItemSelectedListener,
        com.custom.view.WheelButton.OnItemClickListener, OnLegendItemSelectedListener {
    LegendManager legendManager;

    TimeSelectorManager monthSelectorManager;

    WheelButton chartButton;

    ProportionBar proportionBar;

    View proportion_bar_layout;

    TextView wellcome_tv;

    TextView total_assets;

    TextView lastUpdate;

    TextView loansProportionBarTitle;

    TextView loansProportionBarValue;

    public DashBoardManager(Activity activity, ViewGroup root) {
        super(activity, root);
    }

    @Override
    protected void init() {

        LayoutInflater inflater = LayoutInflater.from(activity);
        view = inflater.inflate(R.layout.dash_board, null);

        wellcome_tv = (TextView)view.findViewById(R.id.wellcome_tv);
        total_assets = (TextView)view.findViewById(R.id.money);
        lastUpdate = (TextView)view.findViewById(R.id.update_time);
        proportion_bar_layout = view.findViewById(R.id.proportion_bar_layout);

        initSubView();

    }

    @Override
    protected void initSubView() {
        // TODO Auto-generated method stub
        legendManager = new LegendManager(activity, (ViewGroup)view);
        legendManager.setOnLegendItemSelectedListener(this);
        monthSelectorManager = new TimeSelectorManager(activity, (ViewGroup)view);
        monthSelectorManager.setOnTimeItemSelectedListener(this);
        chartButton = (WheelButton)view.findViewById(R.id.cb);
        chartButton.setOnItemClickListener(this);
        proportionBar = (ProportionBar)view.findViewById(R.id.pb);
        loansProportionBarTitle = (TextView)view.findViewById(R.id.loans_title);
        loansProportionBarValue = (TextView)view.findViewById(R.id.loans_value);
    }

    public void selectedTime(int posiion) {
        monthSelectorManager.setSelected(posiion);
    }

    public void setProportionBarClickListener(OnClickListener onclickListener) {
        proportionBar.setOnClickListener(onclickListener);
    }

    public void setLoansProportionBarTitle(String title) {
        loansProportionBarTitle.setText(title);
    }

    public void setLoansProportionBarValue(String value) {
        loansProportionBarValue.setText(value);
    }

    public void setTotalAssets(String text) {
        total_assets.setText(text);
    }

    public void setWellcomeText(String text) {
        wellcome_tv.setText(text);
    }

    public void setLastUpdate(String text) {
        lastUpdate.setText(text);
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

    /**
     * 添加饼图元素（环状的）
     * 
     * @param itemButton
     */
    public void addCircleButton(WheelButtonItem itemButton) {

        chartButton.addCircleButton(itemButton);
    }

    public WheelButton getWheelButton() {
        return chartButton;
    }

    /**
     * 添加饼图元素（中间的）
     * 
     * @param itemButton
     */
    public void addCenterButton(WheelButtonItem itemButton) {
        chartButton.addCenterButton(itemButton);
    }

    /**
     * 添加横条元素
     * 
     * @param text
     * @param scale
     * @param background
     */
    public void addProportionBarItem(String text, int scale, Drawable background) {

        this.proportionBar.addItem(text, scale, background);
        proportionBar.requestLayout();
        proportionBar.invalidate();
    }

    public void hideProportionBar() {

        proportion_bar_layout.setVisibility(View.GONE);
        loansProportionBarTitle.setVisibility(View.GONE);
        loansProportionBarValue.setVisibility(View.GONE);
    }

    public void generateTimeButton() {
        monthSelectorManager.generateButton();
    }

    public void generateMonthSelector() {
        monthSelectorManager.generateDefaultVerticalTimes();
    }

    public void test() {

        LegendItemText legendItemText = new LegendItemText();
        Drawable accountCloseDrawable = activity.getResources().getDrawable(
                R.drawable.loanlegend_closed);
        Drawable accountopenDrawable = activity.getResources().getDrawable(
                R.drawable.loanlegend_opened);
        legendItemText.setData("cards", "500", accountCloseDrawable, accountopenDrawable);

        legendItemText = new LegendItemText();
        legendItemText.setData("investfdasfsdfasfsdafdsaf", "500", activity.getResources()
                .getDrawable(R.drawable.loanlegend_closed),
                activity.getResources().getDrawable(R.drawable.loanlegend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("cards", "500",
                activity.getResources().getDrawable(R.drawable.accounts_legend_closed), activity
                        .getResources().getDrawable(R.drawable.accounts_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("invest", "500",
                activity.getResources().getDrawable(R.drawable.loanlegend_closed), activity
                        .getResources().getDrawable(R.drawable.loanlegend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("cards", "500",
                activity.getResources().getDrawable(R.drawable.accounts_legend_closed), activity
                        .getResources().getDrawable(R.drawable.accounts_legend_opened));
        legendManager.addLegendItem(legendItemText);

        legendItemText = new LegendItemText();
        legendItemText.setData("invest", "500",
                activity.getResources().getDrawable(R.drawable.loanlegend_closed), activity
                        .getResources().getDrawable(R.drawable.loanlegend_opened));
        legendManager.addLegendItem(legendItemText);

        legendManager.generateUi();

        monthSelectorManager.clear();
        // 此处是新建一个钮扣管理类
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
        monthSelectorManager.setOnTimeItemSelectedListener(this);
        // 此处是加入监听器
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

            chartButton.addCircleButton(chile);
        }
        chile = new WheelButtonItem();
        chile.text = "50%";
        chile.weight = (int)(Math.random() * 360);
        chile.backgroundColor = (int)(Math.random() * 0xffffff) + 0xff000000;

        chartButton.addCenterButton(chile);

        chartButton.setOnItemClickListener(this);

        BitmapDrawable drawable = (BitmapDrawable)activity.getResources().getDrawable(
                R.drawable.loanlegend_opened);
        Bitmap bitmap = drawable.getBitmap();

        BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(drawable,
                BankBitmapDrawable.drawable_type_rect);
        bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_LOANS);
        bankBitmapDrawable.setBitmapLevel(1);
        BankBitmapDrawable bankBitmapDrawable2 = new BankBitmapDrawable(drawable,
                BankBitmapDrawable.drawable_type_rect);
        bankBitmapDrawable2.setMainColor(BankBitmapDrawable.COLOR_LOANS);
        bankBitmapDrawable2.setBitmapLevel(2);
        int color = drawable.getBitmap().getPixel(0, bitmap.getHeight() >> 1);
        proportionBar.addItem("20%", 20, bankBitmapDrawable);
        proportionBar.addItem("80%", 80, bankBitmapDrawable2);
        proportionBar.setBackgroundResource(R.drawable.srect);
    }

    @Override
    public void onTimeSelected(TimeText timeText, int position) {

        if (onDashboardClickListener != null) {
            onDashboardClickListener.onTimeClick(timeText, position);
        }
    }

    private OnDashboardClickListener onDashboardClickListener;

    public OnDashboardClickListener getOnDashboardClickListener() {
        return onDashboardClickListener;
    }

    public void setOnDashboardClickListener(OnDashboardClickListener onDashboardClickListener) {
        this.onDashboardClickListener = onDashboardClickListener;
    }

    public void clear() {

        this.monthSelectorManager.clear();
        this.legendManager.clear();
        this.chartButton.clear();
        this.proportionBar.clear();
    }

    public static interface OnDashboardClickListener {

        public void onTimeClick(TimeText timeText, int position);

        public void onChartButtonItemClick(WheelButtonItem child, int position, long type);

        public void onLegendItemSelected(View View,LegendItemText legendItemText, int position);

    }

    @Override
    public void onItemClick(WheelButton parent, WheelButtonItem child, int position, int flag) {
        if (onDashboardClickListener != null) {
            onDashboardClickListener.onChartButtonItemClick(child, position, flag);
        }
    }

    @Override
    public void onLegendItemSelected(View view ,LegendItemText legendItemText, int position) {
        if (onDashboardClickListener != null) {
            onDashboardClickListener.onLegendItemSelected(view,legendItemText, position);
        }
    }

}
