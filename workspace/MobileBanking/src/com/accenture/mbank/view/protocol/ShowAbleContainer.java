
package com.accenture.mbank.view.protocol;

/**
 * 实现该接口的类都有添加ShowAble的能力
 * @see #addShowAble(ShowAble)
 * @version 2.0.x 2013-1-25
 * @author seekting.x.zhang
 */
public interface ShowAbleContainer {

    /**
     * 添加一个showAble
     * 
     * @param showAble
     * @see ShowAble
     */
    void addShowAble(ShowAble showAble);

}
