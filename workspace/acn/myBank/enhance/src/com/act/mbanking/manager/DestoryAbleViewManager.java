
package com.act.mbanking.manager;

import android.app.Activity;
import android.view.ViewGroup;

public class DestoryAbleViewManager extends ViewManager {

    private String back;

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public DestoryAbleViewManager(Activity activity, ViewGroup root) {
        super(activity, root);
    }

    @Override
    protected void init() {

    }

    public void onShow() {

    }

    public void destory() {

        if (root != null && root.indexOfChild(view) > 0) {
            root.removeView(view);
        }
    }

    @Override
    protected void initSubView() {
        // TODO Auto-generated method stub

    }

}
