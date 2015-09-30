
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.accenture.mbank.model.DashBoardModel;

public class AnimateAbleLayout extends BankLinearLayout {

    Handler handler;

    public AnimateAbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        handler = new Handler();
    }

    protected DashBoardModel animateToDashBoardAccountCode;

    public void setAnimateTo(DashBoardModel dashBoardModel) {
        animateToDashBoardAccountCode = dashBoardModel;
    }

    /**
     * 通过dashboard跳到过来的时候，需要展开相应的界面时调用该方法
     * 
     * @param isDelay 是否延时滚动
     */
    protected void performShowFromDashBoard(boolean isDelay) {
        if (animateToDashBoardAccountCode != null) {
            showFromDashBoard(isDelay);
        }
    }

    private static final int delayTime = 500;

    protected ViewGroup getContainer() {

        return null;
    }

    private void showFromDashBoard(final boolean isDelay) {
        ViewGroup bankRollContainer = getContainer();
        for (int j = 0; j < bankRollContainer.getChildCount(); j++) {
            View v = bankRollContainer.getChildAt(j);
            String accountCode = "";
            if (v instanceof AccountItemLayout) {
                final AccountItemLayout showAble = (AccountItemLayout)bankRollContainer
                        .getChildAt(j);
                accountCode = showAble.getAccount().getAccountCode();

                if (accountCode.equals(animateToDashBoardAccountCode.getAccountCode())) {
                    int time = 0;
                    if (isDelay) {
                        time = delayTime;
                    }
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showAble.showDelay(isDelay);
                            animateToDashBoardAccountCode=null;
                        }
                    }, time);
                    break;
                }

            } else {
                final LoanItemLayout layout = (LoanItemLayout)bankRollContainer.getChildAt(j);

                accountCode = layout.getAccount().getAccountCode();
                if (accountCode.equals(animateToDashBoardAccountCode.getAccountCode())) {

                    layout.showDelay();
                }
            }
        }

    }

}
