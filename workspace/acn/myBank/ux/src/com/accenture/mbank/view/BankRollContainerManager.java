
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;

import com.accenture.manager.protocol.BankViewManager;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.view.protocol.ShowAble;

public abstract class BankRollContainerManager extends BankViewManager {
    protected BankRollContainer rollContainer;

    public BankRollContainerManager() {
    }

    public BankRollContainer getRollContainer() {
        return rollContainer;
    }

    public void setRollContainer(BankRollContainer rollContainer) {
        this.rollContainer = rollContainer;
    }

    public abstract void createUiByData();

    /**
     * 当一个球被打开时触发
     * 
     * @param bankRollView
     */
    public abstract void onShow(ShowAble showAble);

    protected Context getContext() {

        if (rollContainer != null) {
            return rollContainer.getContext();
        }
        return null;
    }

    protected DashBoardModel animateToDashBoardModel;

    public void setAnimateTo(DashBoardModel dashBoardModel) {
        animateToDashBoardModel = dashBoardModel;
    }

    /**
     * 通过dashboard跳到过来的时候，需要展开相应的界面时调用该方法
     * 
     * @param isDelay 是否延时滚动
     */
    protected void performShowFromDashBoard(boolean isDelay) {
        if (animateToDashBoardModel != null) {
            showFromDashBoard(isDelay);
        }
    }

    protected void showFromDashBoard(final boolean isDelay) {
    }

}
