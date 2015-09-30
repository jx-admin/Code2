
package com.accenture.mbank.view.protocol;

/**
 * 实现该接口的view都有展开和收缩的能力 , 监听ShowAble的show事件用到了
 * {@link com.accenture.mbank.view.protocol.ShowListener}
 * 
 * @version 2.0.x,2013-1-25
 * @author seekting.x.zhang
 */
public interface ShowAble {

    /**
     * 通过此方法来展开showAble,关闭调用{@link #close()}
     */
    void show();

    /**
     * 通过此方法来收缩showAble,展开调用{@link #show()}
     */
    void close();

    /**
     * 设置展开监听器
     * 
     * @see ShowListener
     * @param listener
     */
    void setShowListener(ShowListener listener);
}
