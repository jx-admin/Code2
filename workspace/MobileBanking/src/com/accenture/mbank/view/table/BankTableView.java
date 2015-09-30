
package com.accenture.mbank.view.table;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 所有报表的基类
 * 
 * @version 2.0.x,2013-1-25
 * @author seekting.x.zhang
 */
public class BankTableView extends View {

    /**
     * 处理UI的handler
     */
    protected Handler mHandler;

    /**
     * 报表显示时所需要的数据
     */
    private TableViewData mTableViewData;

    public BankTableView(Context context) {
        super(context);
        init();
    }

    public BankTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    protected void init() {

    }

    public TableViewData getTableViewData() {
        return mTableViewData;
    }

    public void setTableViewData(TableViewData tableViewData) {
        this.mTableViewData = tableViewData;
    }

    /**
     * 通知更新ui
     */
    public void notifyRefresh() {

        refresh();
    }

    /**
     * 此方法是收到notifyRefresh通知后会根据mTableViewData更新ui<br>
     * 具体怎么更新，这些细节，交给它的子类去实现
     */
    protected void refresh() {

    }

}
