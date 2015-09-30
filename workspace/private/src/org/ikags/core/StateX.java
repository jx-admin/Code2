package org.ikags.core;

import javax.microedition.lcdui.Graphics;

/**
 * I.K.A Engine<BR>
 * State类，用于派生符合ika Engine标准的接口类<BR>
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2008.11.15 最后更新 2009.5.20
 * @version 0.2
 */
public abstract class StateX
{

    /**
     * 初始化调用方法
     */
    public abstract void init();

    /**
     * 绘画方法
     * 
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * 按键方法keyPressed
     * 
     * @param key
     */
    public abstract void keyPressed(int key);

    /**
     * 按键方法keyReleased
     * 
     * @param key
     */
    public abstract void keyReleased(int key);

    /**
     * 逻辑处理（入线程）
     */
    public abstract void Logic();
// 用于复制
// public void keyPressed(int key){
// switch (key) {
// case Def.KEY_UP:
// case Def.KEY_2:
// break;
// case Def.KEY_LEFT:
// case Def.KEY_4:
// break;
// case Def.KEY_RIGHT:
// case Def.KEY_6:
// break;
// case Def.KEY_DOWN:
// case Def.KEY_8:
// break;
// case Def.SOFTKEY_LEFT:
// case Def.KEY_MIDDLE:
// break;
// case Def.SOFTKEY_RIGHT:
// break;
// }
//        
//        
// }
}
