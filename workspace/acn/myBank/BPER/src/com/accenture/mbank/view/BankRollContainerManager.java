
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;

import com.accenture.manager.protocol.BankViewManager;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.InnerScrollView.OnInnerScrollListener;
import com.accenture.mbank.view.arrow.ArrowController;
import com.accenture.mbank.view.protocol.ShowAble;

public abstract class BankRollContainerManager extends BankViewManager implements OnInnerScrollListener {
    protected BankRollContainer rollContainer;

    private ArrowController arrowController;
    public BankRollContainerManager() {
        arrowController = new ArrowController();
    }

    public BankRollContainer getRollContainer() {
        return rollContainer;
    }

    public void setRollContainer(BankRollContainer rollContainer) {
        this.rollContainer = rollContainer;
        
        InnerScrollView scroll = rollContainer;
        scroll.setOnInnerScrollListener(this);
        arrowController.setScrollView(scroll);
        updateArrowState();
    }
    
	protected void onError(Context context) {
		final MainActivity mainActivity = (MainActivity)context;
		mainActivity.displayErrorMessage("90000",context.getResources().getString(R.string.service_unavailable));
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

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
    	updateArrowState();
    }
    
    public void setFlag(boolean _flag){
    	arrowController.flag = _flag;
    }
    
	public void updateArrowState() {
		LogManager.d("===========================" + getRollContainer().getRollContainer().getChildAt(0).getHeight());
		LogManager.d("===========================" + getRollContainer().getRollContainer().getChildAt(0).getMeasuredHeight());
		
		LogManager.d("===========================" + getRollContainer().getRollContainer().getChildAt(1).getHeight());
		LogManager.d("===========================" + getRollContainer().getRollContainer().getChildAt(1).getMeasuredHeight());
		arrowController.updateArrowState();
	}
	
	public void setArrowThreadhold(int threadhold) {
		arrowController.THREADHOLD = threadhold;
	}
}
