
package com.act.mbanking.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.act.mbanking.R;
import com.act.mbanking.manager.view.NavigationBar;
import com.act.mbanking.manager.view.NavigationBar.OnNavigationClickListener;

public class NavigationActivity extends BaseActivity implements OnNavigationClickListener {

    /**
     * 仿ios导航条
     */
    protected NavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setNavigationStatus();
    }

    public void setNavigationStatus() {
        if (isVertical) {
            showNavigationBar();
        } else {
            hideNavigationBar();
        }
    }

    /**
     * 隐藏导航条
     */
    public void hideNavigationBar() {

        if (navigationBar != null) {
            navigationBar.setVisibility(View.GONE);
        }

    }

    public void setLeftNavigationText(String text) {
        if (navigationBar != null) {
            navigationBar.setLeftText(text);
        }
    }

    /**
     * 显示导航
     */
    public void showNavigationBar() {
        if (navigationBar != null) {
            navigationBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        navigationBar = (NavigationBar)findViewById(R.id.navigation_bar);
        if (navigationBar != null) {
            navigationBar.init();
            navigationBar.setOnNavigationClickListener(this);
        }
    }

    /**
     * 左导航键的图标
     * 
     * @param resId
     */
    public void setLeftNavigationImg(int resId) {

        if (navigationBar != null) {
            navigationBar.setLeftImg(resId);
        }
    }

    /**
     * 右导航键图标
     * 
     * @param resId
     */
    public void setRightNavigationImg(int resId) {

        if (navigationBar != null) {
            navigationBar.setRightImg(resId);
        }
    }

    /**
     * 左导航被点击时触发事件
     * 
     * @param v
     */
    protected void onLeftNavigationClick(View v) {

    }

    /**
     * 右导航被点击时触发事件
     * 
     * @param v
     */
    protected void onRightNavigationClick(View v) {

    }

    @Override
    public void onLeftClick(View v) {
        onLeftNavigationClick(v);
    }

    @Override
    public void onRightClick(View v) {
        onRightNavigationClick(v);

    }

}
