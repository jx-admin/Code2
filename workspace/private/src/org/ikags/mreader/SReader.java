package org.ikags.mreader;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.ikags.core.Def;
import org.ikags.core.StateX;
//import org.ikags.mreader.reader.FileBrowser;
import org.ikags.mreader.reader.MReader;
import org.ikags.util.ImageTools;

public class SReader extends StateX
{

    public static MReader reader = new MReader("/index.html");//index.html
    Image bgbar = null;
    Image leftbar = null;
    Image rightbar = null;
//    public static FileBrowser fb = new FileBrowser(mreaderMIDlet.getDisplay());

    public SReader()
    {
        if (bgbar == null)
        {
            bgbar = ImageTools.creatImage("/bgbar.png");
            leftbar = ImageTools.creatImage("/left.png");
            rightbar = ImageTools.creatImage("/right.png");
        }
    }

    public void Logic()
    {
        // TODO Auto-generated method stub
    }

    public void init()
    {
        // TODO Auto-generated method stub
    }

    public void keyPressed(int key)
    {
        if (key == Def.SOFTKEY_LEFT || key == Def.SOFTKEY_LEFT)
        {
            if (Define.mainState != Define.STATE_MENU)
            {
                Define.mainState = Define.STATE_MENU;
            }
        }
        else
        {
            reader.keyPressed(key);
        }
    }

    public void keyReleased(int key)
    {
        reader.keyReleased(key);
    }

    public void paint(Graphics g)
    {
        paintTheme(g);
        reader.paint(g);
        g.drawImage(leftbar, 2, Def.SYSTEM_SH, Def.BOTTOM_LEFT);
        g.drawImage(rightbar, Def.SYSTEM_SW-2, Def.SYSTEM_SH, Def.BOTTOM_RIGHT);
    }

    public void paintTheme(Graphics g)
    {
        int size = Def.SYSTEM_SW / bgbar.getWidth();
        for (int i = 0; i < size; i++)
        {
            g.drawImage(bgbar, i * bgbar.getWidth(), -i * 6, 0);
        }
    }
}
