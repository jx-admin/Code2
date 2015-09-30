package org.ikags.mreader;

import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;
import org.ikags.core.StateX;

public class SLogo extends StateX
{

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
        // TODO Auto-generated method stub
    }

    public void keyReleased(int key)
    {
        // TODO Auto-generated method stub
    }

    public void paint(Graphics g)
    {
        switch (framecount)
        {
            case 9:
                DrawCanvas.getInstance().logoinit();
                break;
            case 5:
                break;
            case 10:
                Define.mainState = Define.STATE_READING;
                break;
        }
        g.setColor(0x000000);
        g.fillRect(0, 0, Def.SYSTEM_SW, Def.SYSTEM_SH);
        g.setColor(0xff0000);
        g.fillRect(0, Def.SYSTEM_SH-10, framecount * (Def.SYSTEM_SW/10), 10);
        g.setColor(0xffffff);
        g.drawString("阅读器" + framecount * 10 + "%", Def.SYSTEM_SW / 2, 0, Def.TOP_CENTER);
        framecount++;
    }

    public int framecount = 0;
}
