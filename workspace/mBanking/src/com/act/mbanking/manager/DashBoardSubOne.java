
package com.act.mbanking.manager;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.act.mbanking.App;
import com.act.mbanking.R;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.TimeItem;
import com.custom.view.WheelButton;
import com.custom.view.WheelButtonItem;

public class DashBoardSubOne extends ViewManager {
    public DashBoardSubOne(Activity activity, ViewGroup root) {
        super(activity, root);
    }

    LegendManager legendManager;

    TimeSelectorManager monthSelectorManager;

    @Override
    protected void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        view = layoutInflater.inflate(R.layout.dashboard_sub_1, null);

        initSubView();
    }

    @Override
    protected void initSubView() {
        // TODO Auto-generated method stub

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

        // 此处是新建一个钮扣管理类
        monthSelectorManager = new TimeSelectorManager(activity, (ViewGroup)view);

        monthSelectorManager.init();

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
}
