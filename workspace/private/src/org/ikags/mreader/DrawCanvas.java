package org.ikags.mreader;

import javax.microedition.lcdui.Graphics;
import org.ikags.core.StateX;

/**
 * I.K.A Engine DrawCanvas类<BR>
 * 独立更新出新类,以前的类业务无关,
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class DrawCanvas extends StateX
{

    public static DrawCanvas drawCanvas;

    public static DrawCanvas getInstance()
    {
        if (drawCanvas == null)
        {
            drawCanvas = new DrawCanvas();
        }
        return drawCanvas;
    }

    public SLogo logo = null;
    public SMenu menu = null;
    public SReader reader = null;

    public void Logic()
    {
    }

    public void init()
    {
        logo = new SLogo();
    }

    public void logoinit()
    {
        menu = new SMenu();
        reader = new SReader();
    }

    public void keyPressed(int key)
    {
        switch (Define.mainState)
        {
            case Define.STATE_LOGO:
                logo.keyPressed(key);
                break;
            case Define.STATE_READING:
                reader.keyPressed(key);
                break;
            case Define.STATE_MENU:
                menu.keyPressed(key);
                break;
        }
    }

    public void keyReleased(int key)
    {
        switch (Define.mainState)
        {
            case Define.STATE_LOGO:
                logo.keyReleased(key);
                break;
            case Define.STATE_READING:
                reader.keyReleased(key);
                break;
            case Define.STATE_MENU:
                menu.keyReleased(key);
                break;
        }
    }

    public void paint(Graphics g)
    {
        switch (Define.mainState)
        {
            case Define.STATE_LOGO:
                logo.paint(g);
                break;
            case Define.STATE_READING:
                reader.paint(g);
                break;
            case Define.STATE_MENU:
                reader.paint(g);
                menu.paint(g);
                break;
        }
    }
}
