package org.ikags.mreader.reader;

import java.io.DataInputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.ikags.core.Def;
import org.ikags.util.ImageTools;

public class ImageReader
{

    Font font = Def.font;
    Image img = null;
    int x = 0;
    int y = 0;
    String title = "";
    String size = "(0 x 0)";
    public int indexLegth = 0;
    public void clean()
    {
        font = Def.font;
        y = 0;
        GoSpeedY = 0;
        x = 0;
        GoSpeedX = 0;
        img = null;
    }

    public void setTitle(String str)
    {
        title = str;
    }

    /**
     * 读取IMG
     * 
     * @param url
     */
    public void loadingIMG(String url)
    {
        if (url.length() > 5 && url.substring(0, 5).equals("file:"))
        {
            // jsr75
            try
            {
                FileConnection fc = ( FileConnection ) Connector.open(url);
                DataInputStream dis = fc.openDataInputStream();
                img = Image.createImage(dis);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            // 资源包内
            if(!url.substring(0, 1).equals("/")){
                url="/"+url;
            }
            img = ImageTools.creatImage(url);
        }
        if (img != null)
        {
            size = "(" + img.getWidth() + " x " + img.getHeight() + ")";
        }
        else
        {
            System.out.println("读取图片错误");
        }
    }

    public void paint(Graphics g)
    {
        keyState();
        g.setColor(0x000000);
        if (img != null)
        {
            g.drawImage(img, x, y, 0);
        }
        if (GoSpeedY != 0 || GoSpeedX != 0 || keyState != KEYSTATE_STOP)
        {
            g.setColor(0xffff00);
            g.fillRect(0, 0, Def.SYSTEM_SW, font.getHeight());
            g.setColor(0x000000);
            g.drawString(title + "  " + x + " , " + y + size, Def.SYSTEM_SW, 0, Def.TOP_RIGHT);
        }
    }

    private int GoSpeedX = 0;
    private int GoSpeedY = 0;
    private byte keyState = 0;
    private static final byte KEYSTATE_STOP = 0;
    private static final byte KEYSTATE_UP = 1;
    private static final byte KEYSTATE_DOWN = 2;
    private static final byte KEYSTATE_LEFT = 3;
    private static final byte KEYSTATE_RIGHT = 4;

    public void keyState()
    {
        switch (keyState)
        {
            case KEYSTATE_STOP:
                if (GoSpeedY > 0)
                {
                    GoSpeedY--;
                }
                if (GoSpeedY < 0)
                {
                    GoSpeedY++;
                }
                if (GoSpeedX > 0)
                {
                    GoSpeedX--;
                }
                if (GoSpeedX < 0)
                {
                    GoSpeedX++;
                }
                break;
            case KEYSTATE_UP:
                GoSpeedY = GoSpeedY - 2;
                break;
            case KEYSTATE_DOWN:
                GoSpeedY = GoSpeedY + 2;
                break;
            case KEYSTATE_LEFT:
                GoSpeedX = GoSpeedX - 2;
                break;
            case KEYSTATE_RIGHT:
                GoSpeedX = GoSpeedX + 2;
                break;
        }
        y = y + GoSpeedY;
        x = x + GoSpeedX;
    }

    public void keyPressed(int key)
    {
        switch (key)
        {
            case Def.KEY_UP:
                keyState = KEYSTATE_UP;
                break;
            case Def.KEY_DOWN:
                keyState = KEYSTATE_DOWN;
                break;
            case Def.KEY_LEFT:
                keyState = KEYSTATE_LEFT;
                break;
            case Def.KEY_RIGHT:
                keyState = KEYSTATE_RIGHT;
                break;
            case Def.KEY_MIDDLE:
                y = 0;
                GoSpeedY = 0;
                x = 0;
                GoSpeedX = 0;
                break;
        }
    }

    public void keyReleased(int key)
    {
        keyState = KEYSTATE_STOP;
    }
}
