package org.ikags.mreader.reader;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;
import org.ikags.util.StringTools;

public class TxtReader
{


    Font font = Def.font;
    int pageLength = Def.SYSTEM_SH;
    int indexLegth = 0;
    private int GoSpeed = 0;
    private byte keyState = 0;
    private static final byte KEYSTATE_STOP = 0;
    private static final byte KEYSTATE_UP = 1;
    private static final byte KEYSTATE_DOWN = 2;
     public static final String[] Encode={"UTF-8", "GB2312","ISO-8891"};
    public void clean()
    {
        // 清空正在使用的内存
        font = Def.font;
        if (painttxtVector != null)
        {
            painttxtVector.removeAllElements();
            painttxtVector = null;
        }
    }

    /**
     * 读取TXT
     * 
     * @param url
     */
    Vector painttxtVector = null;
    Vector oldtxtVector = null;

    public void loadingTXT(String url)
    {
        if (url.length() > 5 && url.substring(0, 5).equals("file:"))
        {
//            // JSR75
            byte[] bt = StringTools.readFile(url);
            
            oldtxtVector = StringTools.readbytes2Vector(bt,Encode[0]);
            if (oldtxtVector.size()==0)
            {
                oldtxtVector = StringTools.readbytes2Vector(bt, Encode[1]);
            }
            if (oldtxtVector.size()==0)
            {
                oldtxtVector = StringTools.readbytes2Vector(bt, Encode[2]);
            }
        }
        else
        {
            
            if(!url.substring(0, 1).equals("/")){
                url="/"+url;
            }
            
            // 资源包内
            oldtxtVector = StringTools.readTxt2Vector(url, Encode[0]);
            if (oldtxtVector.size()==0)
            {
                oldtxtVector = StringTools.readTxt2Vector(url, Encode[1]);
            }
            if (oldtxtVector.size()==0)
            {
                oldtxtVector = StringTools.readTxt2Vector(url,Encode[2]);
            }
        }
        // 读取
        if (oldtxtVector.size()==0)
        {
            oldtxtVector.addElement("读取失败:" + url);
        }
        // 转换成屏幕宽度的
        if (painttxtVector == null)
        {
            painttxtVector = new Vector();
        }
        else
        {
            painttxtVector.removeAllElements();
        }
        for (int i = 0; i < oldtxtVector.size(); i++)
        {
            String str = ( String ) oldtxtVector.elementAt(i);
            Vector vec = StringTools.clipStringwithWidth(str, Def.SYSTEM_SW - 3, Def.SYSTEM_SW - 3);
            if (vec != null)
            {
                for (int j = 0; j < vec.size(); j++)
                {
                    painttxtVector.addElement(vec.elementAt(j));
                }
            }
        }
        oldtxtVector.removeAllElements();
        oldtxtVector = null;
        pageLength = Math.max(Def.SYSTEM_SH, painttxtVector.size() * font.getHeight());
        indexLegth = 0;
        System.gc();
        System.out.println("生成txt绘画节点后的内存:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }
    public void paint(Graphics g)
    {
        keyState();
        g.setColor(0x000000);
        if (painttxtVector != null)
        {
            for (int i = 0; i < painttxtVector.size(); i++)
            {
                String str = ( String ) painttxtVector.elementAt(i);
                int yline = i * font.getHeight() - indexLegth;
                if (yline > -30 && yline < Def.SYSTEM_SH + 30)
                {
                    g.drawString(str, 0, yline, 0);
                }
            }
        }
        if (GoSpeed != 0 || keyState != KEYSTATE_STOP)
        {
//            g.setColor(0xffff00);
//            g.fillRect(0, 0, Def.SYSTEM_SW, font.getHeight());
//            g.setColor(0x000000);
//            g.drawString(title + "  " + (indexLegth / font.getHeight()) + "/" + (pageLength / font.getHeight()) + "行", Def.SYSTEM_SW, 0, Def.TOP_RIGHT);
       
            g.setColor(0xaa0000);
            int yindex=Def.SYSTEM_SH*indexLegth/pageLength;
            int scrollLength=2+Def.SYSTEM_SH*Def.SYSTEM_SH/pageLength;
            
             g.fillRect(Def.SYSTEM_SW-3, yindex, 3, scrollLength);
        }
    }



    public void keyState()
    {
        switch (keyState)
        {
            case KEYSTATE_STOP:
                if (GoSpeed > 0)
                {
                    GoSpeed--;
                }
                if (GoSpeed < 0)
                {
                    GoSpeed++;
                }
                break;
            case KEYSTATE_UP:
                GoSpeed = GoSpeed - 2;
                break;
            case KEYSTATE_DOWN:
                GoSpeed = GoSpeed + 2;
                break;
        }
        indexLegth = indexLegth + GoSpeed;
        if (indexLegth < 0)
        {
            indexLegth = 0;
            GoSpeed = 0;
        }
        else if (indexLegth > pageLength - Def.SYSTEM_SH)
        {
            indexLegth = pageLength - Def.SYSTEM_SH;
            GoSpeed = 0;
        };
    }

    public void keyPressed(int key)
    {
        switch (key)
        {
            case Def.KEY_UP:
            case Def.KEY_2:
                keyState = KEYSTATE_UP;
                break;
            case Def.KEY_DOWN:
            case Def.KEY_8:
                keyState = KEYSTATE_DOWN;
                break;
            case Def.KEY_1://最顶
                GoSpeed=-3000;
                break;
            case Def.KEY_7://最底
                GoSpeed=3000;
                break;
            case Def.KEY_3://翻页1/5
                if(indexLegth<pageLength/5){
                    indexLegth=0;
                }else{
                    indexLegth =indexLegth- pageLength/5;  
                }
                break;
            case Def.KEY_9://翻页-1/5
                if(indexLegth>pageLength*4/5){
                    indexLegth = pageLength - Def.SYSTEM_SH;
                }else{
                    indexLegth =indexLegth+ pageLength/5;  
                }
                break;
            case Def.KEY_4://上翻页
            case Def.KEY_LEFT:
                if(indexLegth>Def.SYSTEM_SH){
                    indexLegth =indexLegth-Def.SYSTEM_SH;  
                }else{
                    indexLegth = 0; 
                } 
                break;
            case Def.KEY_6://下翻页
            case Def.KEY_RIGHT:
                if(indexLegth<pageLength-2*Def.SYSTEM_SH){
                    indexLegth =indexLegth+Def.SYSTEM_SH;  
                }else{
                    indexLegth = pageLength - Def.SYSTEM_SH; 
                } 
                break;
        }
    }

    public void keyReleased(int key)
    {
        keyState = KEYSTATE_STOP;
    }
}
