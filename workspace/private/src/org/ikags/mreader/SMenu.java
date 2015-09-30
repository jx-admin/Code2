package org.ikags.mreader;

import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;
import org.ikags.core.StateX;
import com.ikags.mreader.menu.LeftMenu;

public class SMenu extends StateX
{

    LeftMenu lm = new LeftMenu();
    public static byte menuState = 0;
    public static final byte MENU = 0;
    public static final byte SETTING = 1;
    public static final byte HELP = 2;
    public static final byte ABOUT = 3;

    public void Logic()
    {
        // TODO Auto-generated method stub
    }

    public void init()
    {
    }

    public void keyPressed(int key)
    {
        switch (menuState)
        {
            case MENU:
                lm.keyPressed(key);
                break;
            case SETTING:
                 switch(key){
                     case Def.KEY_LEFT:
                         settingIndex--;
                         break;
                     case Def.KEY_RIGHT:
                         settingIndex++;
                         break;
                     case Def.SOFTKEY_RIGHT:
                         menuState=MENU;
                         break;
                     case Def.SOFTKEY_LEFT:
                          switch(settingIndex%3){
                              case 0:
                                  Def.font=Def.font1;
                                  break;
                              case 1:
                                  Def.font=Def.font2;
                                  break;
                              case 2:
                                  Def.font=Def.font3;
                                  break;
                          }
                         menuState=MENU;
                         Define.mainState = Define.STATE_READING;
                         SReader.reader.frashPage();
                         break;
                 }
                break;
            case HELP:
                switch(key){
                    case Def.SOFTKEY_LEFT:
                    case Def.SOFTKEY_RIGHT:
                        menuState=MENU;
                        break;
                }
                break;
            case ABOUT:
                switch(key){
                    case Def.SOFTKEY_LEFT:
                    case Def.SOFTKEY_RIGHT:
                        menuState=MENU;
                        break;
                }
                break;
        }
    }

    public void keyReleased(int key)
    {

    }

    public void paint(Graphics g)
    {
        switch (menuState)
        {
            case MENU:
                lm.paint(g);
                break;
            case SETTING:
                paintBg(g);
                g.drawString(setting[settingIndex%3], Def.SYSTEM_SW/2, 60, Def.TOP_CENTER);
                break;
            case HELP:
                paintBg(g);
                break;
            case ABOUT:
                paintBg(g);
                g.drawString(name, Def.SYSTEM_SW/2, 60, Def.TOP_CENTER);
                g.drawString(acher, Def.SYSTEM_SW/2, 60+Def.font.getHeight(), Def.TOP_CENTER);
                break;
        }
    }
    
    int settingIndex=99999;
    String[] setting={"字体 <小>","字体 <中>","字体 <大>"};
    String name="IKA 阅读器";
    String acher="airzhangfish@yahoo.com";
    int boxSize=0;
    public void paintBg(Graphics g){
        boxSize=Def.SYSTEM_SH-100;
        g.setColor(0xffffff);
        g.fillRect(0, 50, Def.SYSTEM_SW, Def.SYSTEM_SH-100);
        g.setColor(0x000000);
        g.drawRect(0, 50, Def.SYSTEM_SW-1, boxSize-1);
    }
    
    
}
