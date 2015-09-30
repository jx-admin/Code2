package org.ikags.core;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * I.K.A Engine SystemCanvas类.
 * 
 * @author airzhangfish
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class SystemCanvas extends Canvas implements Runnable
{

    SystemMidlet midlet;
    static boolean IsRun = true;
    StateX mystate = null;

    /**
     * 初始化
     */
    public SystemCanvas(SystemMidlet let, StateX state)
    {
        setFullScreenMode(true);
        midlet = let;
        mystate = state;
        Def.MainState = Def.STATE_LOGO;// 初始状态
        mystate.init();
        new Thread(this).start();
    }

    /**
     * 显示当前状态的帧数
     */
    public static long show_fps;
    private static long thread_startTime;
    private static long thread_endTime;

    /**
     * 主线程运算函数，包含keystate按键状态和repaint事件，独立于绘画的逻辑处理事件。
     */
    public void run()
    {
        while (IsRun)
        {
            thread_startTime = System.currentTimeMillis();
            logic();
            repaint();
            thread_endTime = System.currentTimeMillis();
            try
            {
                if ((thread_endTime - thread_startTime) < Def.SYSTEM_DELAY)
                {
                    Thread.sleep(Def.SYSTEM_DELAY - (thread_endTime - thread_startTime));
                }
                else
                {
                    Thread.sleep(1);
                }
            }
            catch (InterruptedException ex1)
            {}
            show_fps = thread_endTime - thread_startTime;
        }
        midlet.destroyApp(true);
        midlet.notifyDestroyed();
    }

    /**
     * 结束的时候调用
     */
    public static void exit()
    {
        IsRun = false;
    }

    Graphics g;// bufImage用的G

    /**
     * 主绘画函数。定义绘画状态，推荐结构：设置字体，setclip，cliprect，游戏状态switch。
     */
    public void paint(Graphics fish)
    {
        if (Def.bufferImage == null)
        {
            fish.setFont(Def.font);
            fish.setClip(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
            fish.clipRect(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
            Def.SYSTEM_SW = this.getWidth();
            Def.SYSTEM_SH = this.getHeight();
            init(fish);
        }
        else
        {
            g = Def.bufferImage.getGraphics();
            g.setFont(Def.font);
            // g.setClip(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
            // g.clipRect(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
            mystate.paint(g);
            DisplayBufferImage(fish);
        }
    }

    /**
     * 主按键 keyPressed函数。定义返回的key，推荐结构：游戏状态switch。
     * 
     * @param key 返回机器按键信息
     * @see Def
     */
    public void keyPressed(int key)
    {
        key = keyConvert(key);
        mystate.keyPressed(key);
    }

    /**
     * 主释放按键 keyReleased函数。定义返回的key，推荐结构：游戏状态switch。
     * 
     * @param key 返回机器按键信息
     * @see Def
     */
    public void keyReleased(int key)
    {
        key = keyConvert(key);
        mystate.keyReleased(key);
    }

    /**
     * 处理逻辑运算
     */
    public void logic()
    {
        mystate.Logic();
    }

    private int inicount = 0;

    private void init(Graphics g)
    {
        Def.SYSTEM_SW = this.getWidth();
        Def.SYSTEM_SH = this.getHeight();
        g.setColor(0x000000);
        g.fillRect(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
        switch (inicount)
        {
            case 1:
                initBufferImage();
                break;
            case 2:
                Def.MainState = Def.STATE_LOGO;
                break;
        }
        inicount++;
    }

    private int keyConvert(int keycode)
    {
        try
        {
            if (keycode >= 48 && keycode <= 57)
            {
                return keycode;
            }
            if (keycode == 42 || keycode == 35)
            {
                return keycode;
            }
            int gamekeycode = getGameAction(keycode);
            if (gamekeycode == Canvas.UP)
            {
                return Def.KEY_UP;
            }
            if (gamekeycode == Canvas.DOWN)
            {
                return Def.KEY_DOWN;
            }
            if (gamekeycode == Canvas.LEFT)
            {
                return Def.KEY_LEFT;
            }
            if (gamekeycode == Canvas.RIGHT)
            {
                return Def.KEY_RIGHT;
            }
            if (keycode == -20 || keycode == 20 || keycode == -5 || keycode == 5 || keycode == 8)
            {
                return Def.KEY_MIDDLE;
            }
            if (keycode == -21 || keycode == 21 || keycode == -6 || keycode == 6)
            {
                return Def.SOFTKEY_LEFT;
            }
            if (keycode == -22 || keycode == 22 || keycode == -7 || keycode == 7)
            {
                return Def.SOFTKEY_RIGHT;
            }
            if (keycode == -8)
            {
                return Def.KEY_CLEAR;
            }
        }
        catch (Exception ex)
        {
            return keycode;
        }
        return keycode;
    }

    /**
     * 需要改变整个屏幕方向的话请调用这个方法,传入参数为TOPin相关参数
     * 
     * @param theway
     * @return boolean
     */
    public static boolean ChangeTheWay(int theway)
    {
        switch (theway)
        {
            case Def.TOPinNORMAL:
                Def.SYSTEM_SW = Def.DEFAULT_SYSTEM_SW;
                Def.SYSTEM_SH = Def.DEFAULT_SYSTEM_SH;
                Def.bufferImage = Image.createImage(Def.SYSTEM_SW, Def.SYSTEM_SH);
                Def.TheWay = Def.TOPinNORMAL;
                break;
            case Def.TOPinLEFT:
                Def.SYSTEM_SW = Def.DEFAULT_SYSTEM_SH;
                Def.SYSTEM_SH = Def.DEFAULT_SYSTEM_SW;
                Def.bufferImage = Image.createImage(Def.SYSTEM_SW, Def.SYSTEM_SH);
                Def.TheWay = Def.TOPinLEFT;
                break;
            case Def.TOPinRIGHT:
                Def.SYSTEM_SW = Def.DEFAULT_SYSTEM_SH;
                Def.SYSTEM_SH = Def.DEFAULT_SYSTEM_SW;
                Def.bufferImage = Image.createImage(Def.SYSTEM_SW, Def.SYSTEM_SH);
                Def.TheWay = Def.TOPinRIGHT;
                break;
            case Def.TOPinBOTTOM:
                Def.SYSTEM_SW = Def.DEFAULT_SYSTEM_SW;
                Def.SYSTEM_SH = Def.DEFAULT_SYSTEM_SH;
                Def.bufferImage = Image.createImage(Def.SYSTEM_SW, Def.SYSTEM_SH);
                Def.TheWay = Def.TOPinBOTTOM;
                break;
        }
        return true;
    }

    /**
     * 将BufferImage绘画于屏幕(带旋转)<BR>
     * 使用方法:<BR>
     * Graphics gg;//bufImage用的G<BR>
     * gg=Def.bufferImage.getGraphics();//获取gg<BR>
     * gg.drawString("绘画于BUFF上",0,0,0);<BR>
     * Def.DisplayBufferImage(g);//原始的绘画g<BR>
     * 
     * @param g
     */
    private void DisplayBufferImage(Graphics g)
    {
        switch (Def.TheWay)
        {
            case Def.TOPinNORMAL:
                g.drawImage(Def.bufferImage, 0, 0, 0);
                break;
            case Def.TOPinLEFT:
                g.drawRegion(Def.bufferImage, 0, 0, Def.bufferImage.getWidth(), Def.bufferImage.getHeight(), 6, 0, 0, 0);
                break;
            case Def.TOPinRIGHT:
                g.drawRegion(Def.bufferImage, 0, 0, Def.bufferImage.getWidth(), Def.bufferImage.getHeight(), 5, 0, 0, 0);
                break;
            case Def.TOPinBOTTOM:
                g.drawRegion(Def.bufferImage, 0, 0, Def.bufferImage.getWidth(), Def.bufferImage.getHeight(), 3, 0, 0, 0);
                break;
        }
    }

    /**
     * 初始化设置
     * 
     * @param path
     * @return 是否成功获取
     */
    private boolean initBufferImage()
    {
        Def.DEFAULT_SYSTEM_SW = Def.SYSTEM_SW;
        Def.DEFAULT_SYSTEM_SH = Def.SYSTEM_SH;
        Def.bufferImage = Image.createImage(Def.SYSTEM_SW, Def.SYSTEM_SH);
        return true;
    }
}
