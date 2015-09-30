
package com.accenture.mbank.view.protocol;

/**
 * 
 * 
 * 实现该接口的类都有保存和读取的能力
 * 
 * @author seekting.x.zhang
 * @version 2.0.x,2013-1-25
 */
public interface SaveEnAble {

    /**
     * 执行数据保存操作
     */
    void save();

    /**
     * 执行数据恢复操作
     */
    void restore();

}
