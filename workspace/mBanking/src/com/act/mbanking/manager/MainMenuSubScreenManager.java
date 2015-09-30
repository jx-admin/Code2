
package com.act.mbanking.manager;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import com.act.mbanking.ChartModelManager;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.view.ChartView;
import com.act.mbanking.view.HorizontalChartView;
import com.custom.view.CircleLinearView;

/**
 * 通过mainMenu点击item切换的子界面管理基类
 * 
 * @author seekting.x.zhang
 */
public abstract class MainMenuSubScreenManager extends Manager {

    protected MainActivity activity;

    protected ViewGroup layout;
    protected CircleLinearView cv;

    protected MainManager mainManager;
    
    protected MainMenuSubScreenManager lastMainMenuSubScreenManager;

    protected boolean needRefresh;
    
    public MainManager getMainManager() {
        return mainManager;
    }

    public void setMainManager(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    public ViewGroup getLayout() {
        return layout;
    }

    public void setLayout(ViewGroup layout) {
        this.layout = layout;
    }

    public MainMenuSubScreenManager(MainActivity activity) {

        this.activity = activity;
        init();
    }
 
    /**
     * 本方法用来初始化layout等一些避免为空的操作
     */
    protected abstract void init();

    /**
     * 
     */
    public void onOrientationChanged(Configuration newConfig) {

    }
    

    public void onLoadChartData(Object obj) {

    }
    
    HorizontalChartView horizontalChartView;
    public void setChartData(){}

    /**
     * 此方法的调用时机是，界面从上一界面跳到这一界面的时候会调用该方法，<br>
     * 但从它的子界面返回的时候，不会调用此方法，因为子界面返回的时候，不需要改变数据的显示<br>
     * 而上一界面跳到这一界面的时候，可能需要控制数据显示，这时候会触发该方法，该方法的实现<br>
     * 细节由它的子类去实现多用来联网获取数据再显示
     */
    protected void onShow(Object object) {

    };

    public void show() {

        if (layout != null) {
            layout.setVisibility(View.VISIBLE);
        }

        activity.setLeftNavigationText(leftText);
    }

    public void hide() {
        if (layout != null) {
            layout.setVisibility(View.GONE);
        }
    }

    /**
     * 左导航按钮被点击的事件处理
     * 
     * @param v
     * @return
     */
    public boolean onLeftNavigationButtonClick(View v) {

        return false;
    }

    String leftText;

    /**
     * 设置左导航文字
     * 
     * @param text
     */
    public void setLeftNavigationText(String text) {

        activity.setLeftNavigationText(text);
        leftText = text;
    }

    /**
     * 设置左导航的文字
     * 
     * @param res
     */
    public void setLeftNavigationText(int res) {

        String text = activity.getResources().getString(res);
        activity.setLeftNavigationText(text);
        leftText = text;
    }

    /**
     * 显示图表方法
     * 
     * @param chartLayout
     */
    protected void showChart(ViewGroup chartLayout) {

    }

    /**
     * 为确保所有的MainMenuSubScreenManager方法统一<br>
     * 特定义此方法来加载联网数据<br>
     * 加载数据完，需要通过UI显示出来：{@link #setUI()};
     * 
     * @see #setUI()
     */
    protected abstract void loadData();

    /**
     * 为确保所有的MainMenuSubScreenManager方法统一<br>
     * 特定义此方法来设置ui，当loadData()数据后，通过此方法setUI()把加载的数据显示出来<br>
     * 
     * @see #loadData()
     */
    protected abstract void setUI();
}
