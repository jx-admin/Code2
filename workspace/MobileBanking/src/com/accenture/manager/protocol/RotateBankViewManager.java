
package com.accenture.manager.protocol;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 此类基于{@link BankViewManager}上又增加了一点功能,让扩展该类的子类都有控制横向图表显示的能力
 * 
 * @author seekting.x.zhang
 */
public abstract class RotateBankViewManager extends BankViewManager {
    protected ViewGroup container;

    /**
     * 用来装载横屏图表的容器
     */
    public ViewGroup chartLayout;

    public abstract void onShow();

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

    /**
     * 显示相应的数据的报表，这个方法的调用时机是当横屏的时候，它需要知道竖屏的时候显示区域的数据是什么，<br>
     * 可以通过调用{@link #getVisiableRotateViewIndex} 方法来获取可显示区域的最佳数据<br>
     * 这个方法的操作细节需要子类去扩展它的目的是控制横屏的时候显示一个报表<br>
     * 此时需要{@link #chartLayout}来控制显示图表
     */
    public abstract void showChart();
}
