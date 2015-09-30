
package com.accenture.manager.protocol;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.InnerScrollView;
import com.accenture.mbank.view.InnerScrollView.OnInnerScrollListener;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.arrow.ArrowController;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;

/**
 * 此类基于{@link BankViewManager}上又增加了一点功能,让扩展该类的子类都有控制横向图表显示的能力
 * 
 * @author seekting.x.zhang
 */
public abstract class RotateBankViewManager extends BankViewManager implements OnInnerScrollListener{
    protected ViewGroup container;

    private ArrowController arrowController;

    public ViewGroup getContainer() {
        return container;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
        
        InnerScrollView scroll = (InnerScrollView)container.getParent();
        scroll.setOnInnerScrollListener(this);
        
        arrowController = new ArrowController();
        arrowController.setScrollView(scroll);
    }

    /**
     * 用来装载横屏图表的容器
     */
    public ViewGroup chartLayout;

    public abstract void onShow();

	protected void onError(Context context) {
		final MainActivity mainActivity = (MainActivity)context;
		mainActivity.displayErrorMessage(Contants.ERR_GENERIC_ERROR, context.getResources().getString(R.string.service_unavailable));
	}

	protected void drawAccountsRotateTableView(
			final AccountsRotateTableView rotatTableView0,
			final DashBoardModel dashBoardModel,
			final RotateTitleView rTitleView, int dashboardAccounts) {
		rotatTableView0.setCount(dashboardAccounts);

		rotatTableView0.parentScrollView = (ScrollView) container.getParent();
		rotatTableView0.setAccountBalanceValue(dashBoardModel
				.getPersonalizedName());
		rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
		rotatTableView0
				.setButtonImageOver(R.drawable.account_btn_transaction_over);
		String accountBalanceValue = Utils.generateFormatMoney(dashBoardModel.getAccountBalance());
		rotatTableView0.setAccountBalanceValue(accountBalanceValue);
		
		String avaString = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
		rotatTableView0.setAvailableBalanceValue(avaString);
		if (dashBoardModel != null
				&& dashBoardModel.getDashboardDataList() != null
				&& dashBoardModel.getDashboardDataList().size() > 0) {
			setDashboardUi(dashBoardModel, rotatTableView0, rTitleView, 0);
		}
		rotatTableView0.setOnSlidListener(new OnSlidListener() {
			@Override
			public void onSlid(View v, int index) {

				if (index != 0) {
					rotatTableView0.setAvailableBalanceValue(container
							.getContext().getResources()
							.getString(R.string.not_able));
				} else {
					String avaString = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
					rotatTableView0.setAvailableBalanceValue(avaString);
				}
				setDashboardUi(dashBoardModel, rotatTableView0, rTitleView,
						index);
			}

		});
	}

    protected void setDashboardUi(final DashBoardModel dashBoardModel2,final AccountsRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<DashboardDataModel> list = dashBoardModel2.getDashboardDataList();

		if (index < 0 || index >= list.size())
			return;

        DashboardDataModel dashboardDataModel = list.get(index);

        String deposite = Utils.generateFormatMoney(dashboardDataModel.getDeposits());
        rotatTableView0.setDepositValue(deposite);
        String width = Utils.generateFormatMoney(dashboardDataModel.getWithdrawals());
        rotatTableView0.setWidthdrawalsValue(width);

        double value = dashboardDataModel.getAccountBalance();
        String accountBalanceValue = Utils.generateFormatMoney(value);

        rotatTableView0.setAccountBalanceValue(accountBalanceValue);
        String date = TimeUtil.changeFormattrString(dashboardDataModel.getLastUpdate(),TimeUtil.dateFormat2, TimeUtil.dateFormat5);
        titleView.setUpdateTitle(container.getContext().getResources().getString(R.string.updated_date)+ " " + date);
    }

    public BalanceAccountsModel getAccountsByAccountCode(List<BalanceAccountsModel> accounts, List<DashBoardModel> list) {
        int index = getVisiableRotateViewIndex();
        DashBoardModel dashBoardModel = list.get(index);
        
        BalanceAccountsModel ret = null;
        for (BalanceAccountsModel balanceAccountsModel : accounts) {
            if(balanceAccountsModel.getAccountCode().equals(dashBoardModel.getAccountCode()))
            	ret = balanceAccountsModel;
        }
        return ret;
    }

    
    public double getBoundBalanceFromAccountCode(String accountCode, List<BalanceAccountsModel> accountsList) {
    	double ret = 0;
    	
    	if (accountsList == null || accountsList.size() ==0 )
    		return ret;

    	for (BalanceAccountsModel accountsModel : accountsList) {
			if (accountsModel.getAccountCode().equals(accountCode)) {
				ret = accountsModel.getDipiuBalance();
			}
    	}
    	return ret;
    }
    
    public boolean getIsPreferredFromAccountCode(String accountCode, List<BalanceAccountsModel> accountsList) {
    	boolean ret = false;
    	
    	if (accountsList == null || accountsList.size() ==0 )
    		return ret;

    	for (BalanceAccountsModel accountsModel : accountsList) {
			if (accountsModel.getAccountCode().equals(accountCode)) {
				ret = accountsModel.isPreferred();
			}
    	}
    	return ret;
    }

    /**
     * 此方法是用来计算当前界面最优的球的索引值。</br>
     * 当一个界面有多个球的时候，需要判断哪个球显示的百分比最大，</br>这样横屏的时候就显示这个球对应的数据的报表。<br>
     * 这个方法经常为 {@link #showChart()} 方法提供显示依据
     * 
     * @return
     */
    public int getVisiableRotateViewIndex() {

        if (container == null) {
            return -1;
        }
        ScrollView scrollView = (ScrollView)container.getParent();

        float scrollY = scrollView.getScrollY();

        // 子view在scrollView的显示的比例
        float yy = 0f;
        int result = 0;
        for (int i = 0; i < container.getChildCount(); i++) {

            if (i % 2 == 0) {
                continue;
            }

            // 算出球占整个显示区域的百分比
            View child = container.getChildAt(i);

            float childY = child.getTop();

            // 没在显示区
            if (scrollY > childY + child.getHeight() || scrollY + scrollView.getHeight() < childY) {

            } else {
                // 在显示区内
                if (scrollY > childY) {
                    // 上半部分被挡了
                    float size = 1 - (scrollY - childY) / child.getHeight();

                    if (size > yy) {
                        yy = size;
                        result = i;

                    }

                } else if (scrollY < childY
                        && scrollY + scrollView.getHeight() > childY + child.getHeight()) {
                    // 百分百显示
                    result = i;
                    yy = 1;
                    break;
                } else {
                    // 下半部分被挡了
                    float size = (scrollY + scrollView.getHeight() - childY) / child.getHeight();
                    if (size > yy) {
                        yy = size;
                        result = i;

                    }
                }

            }

        }
        result = result / 2;

        return result;
    }
  
	public void updateArrowState() {
		arrowController.updateArrowState();
	}

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
    	updateArrowState();
    }

    /**
     * 显示相应的数据的报表，这个方法的调用时机是当横屏的时候，它需要知道竖屏的时候显示区域的数据是什么，<br>
     * 可以通过调用{@link #getVisiableRotateViewIndex} 方法来获取可显示区域的最佳数据<br>
     * 这个方法的操作细节需要子类去扩展它的目的是控制横屏的时候显示一个报表<br>
     * 此时需要{@link #chartLayout}来控制显示图表
     */
    public abstract void showChart();
}
