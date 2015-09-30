package org.ikags.core;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * I.K.A Engine Midlet类
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.3
 */
public class SystemMidlet extends MIDlet
{

    SystemMidlet midlet;
    static SystemCanvas drawCanvas;
    static Display display;

    public SystemMidlet()
    {
    }

    public void init(StateX st)
    {
        if (drawCanvas == null)
        {
            midlet = this;
            drawCanvas = new org.ikags.core.SystemCanvas(midlet, st);
            display = Display.getDisplay(midlet);
            display.setCurrent(drawCanvas);
        }
        else
        {
            display.setCurrent(drawCanvas);
        }
    }

    public void startApp()
    {
    }

    public void pauseApp()
    {
    }

    public void destroyApp(boolean unconditional)
    {
    }

    public static void setCanvas()
    {
        display.setCurrent(drawCanvas);
    }

    public static Display getDisplay()
    {
        return display;
    }
}
