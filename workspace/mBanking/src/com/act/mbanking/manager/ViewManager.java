
package com.act.mbanking.manager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewManager extends Manager {

    protected Activity activity;

    /**
     * 当前所管理的view
     */
    protected View view;

    /**
     * 父容器
     */
    protected ViewGroup root;

    public ViewManager(Activity activity, ViewGroup root) {
        this.activity = activity;
        this.root = root;
        init();
    }

    public ViewManager(Activity activity, ViewGroup root, View view) {

        this.activity = activity;
        this.root = root;
        this.view = view;
        initSubView();
    }

    public View getView() {
        return view;
    }

    public View getRoot() {
        return root;
    }

    protected abstract void init();

    protected abstract void initSubView();

}
