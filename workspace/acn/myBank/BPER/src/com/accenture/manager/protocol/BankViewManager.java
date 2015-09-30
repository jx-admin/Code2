
package com.accenture.manager.protocol;

/**
 * 此类的的用意是将布局文件交给manage类管理，这样更有层次感
 * 
 * @author seekting.x.zhang
 */
public abstract class BankViewManager {

    /**
     * 当用户在{@link MainActivity}
     * 里点击了一个图标时，系统会更换UI界面，之后会提醒BankViewManager的子类去调用onShow方法，<br>
     * 子类扩展此方法的目的是，通过此方法可以联网，更新ui里的数据,如果数据在登录的时候已经下载好了，就无需做任何操作
     */
    public abstract void onShow();

}
