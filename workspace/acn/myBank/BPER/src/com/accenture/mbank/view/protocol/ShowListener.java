
package com.accenture.mbank.view.protocol;

/**
 * 监听被打开时候的操作
 * 
 * @version 2.0.2,2013-1-25
 * @see {@link #onShow}
 * @author seekting.x.zhang
 */
public interface ShowListener {

    /**
     * 当被打开的时候会告诉监听者，showAble打开以后会调用此方法,一般用来通知父容器联网加载数据，<br>
     * 或是滚动到可视区域，或是把其它的showAble关闭
     * 
     * @see ShowAble
     * @param showAble
     */
    void onShow(ShowAble showAble);

}
