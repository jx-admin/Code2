package org.ikags.mreader.reader;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.ikags.core.Def;
import org.ikags.util.ImageTools;
import org.ikags.util.StringTools;
import org.ikags.util.bxml.*;

import com.mog.lang.GC;

public class HtmlReader
{

    /**
     * 读取HTML
     * 
     * @param url
     */
    Vector paintHtmlVector = new Vector();
    Font font = Def.font;
    Image defualtImage = null;
    String pageTitle = "";
    Vector ImagePools = new Vector();
    public static final String[] Encode = { "UTF-8", "GB2312", "ISO-8891" };

    public HtmlReader()
    {
        defualtImage = ImageTools.creatImage("/noimg.png");
    }

    public void clean()
    {
        font = Def.font;
        paintHtmlVector.removeAllElements();
        ImagePools.removeAllElements();
        SelectedIndex = 0;
        SelectedItem = null;
    }

    // **********************************************
    // **********************************************
    // **********************************************
    // 主要流程部分
    // **********************************************
    // **********************************************
    // **********************************************
    public void loadingHTML(String url)
    {
        System.gc();
        System.out.println("url:" + url);
        System.out.println("载入网页前内存使用:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        BXmlElement rootElement = null;
        try
        {
            byte[] bt = null;
            if (url.length() > 5 && url.substring(0, 5).equals("file:"))
            {// JSR75
                bt = StringTools.readFile(url);
            }
            else
            {
                // 本地资源
                InputStream is = null;
                is = "i".getClass().getResourceAsStream(url);
                ByteArrayOutputStream barr = new ByteArrayOutputStream();
                
                byte[] buf = new byte[512];
                int len = 0;
                while( (len = is.read(buf)) >0 )
                {
                	barr.write(buf, 0, len);
                }
                
                /*bt = new byte[is.available()];// 本地读取INI支持available
                is.read(bt);*/
                GC.__delete(buf);
                
                bt = barr.toByteArray();
                
                barr.close();
                GC.__delete(barr);
                
                is.close();
                is = null;
            }
            rootElement = new BXmlElement();
            String Str = null;
            try
            {
                System.out.println("Reading use:" + Encode[0]);
                Str = new String(bt, Encode[0]);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                try
                {
                    System.out.println("Reading use:" + Encode[1]);
                    Str = new String(bt, Encode[0]);
                }
                catch (Exception ex1)
                {
                    ex1.printStackTrace();
                    System.out.println("Reading use:" + Encode[2]);
                    Str = new String(bt, Encode[0]);
                }
            }
            rootElement = BXmlDriver.loadXML(new StringReader(Str), rootElement);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
        System.out.println("载入网页后内存使用:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        makePage(rootElement);
        rootElement = null;
        System.gc();
        System.out.println("生成页面:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }

    public void makePage(BXmlElement rootElement)
    {
        paintHtmlVector.removeAllElements();
        trivseser(rootElement);
        layoutPage(paintHtmlVector);
    }

    // 遍历方法
    public void trivseser(BXmlElement Element)
    {
        if (Element.getChildren().size() > 0)
        {
            bulidHTMLItemsStart(Element);
            for (int i = 0; i < Element.getChildren().size(); i++)
            {
                BXmlElement element = ( BXmlElement ) Element.getChildren().elementAt(i);
                trivseser(element);
            }
            bulidHTMLItemsEnd(Element);
        }
        else
        {
            // 最末端节点
            bulidHTMLItemsStart(Element);
        }
    }

    public boolean bulidHTMLItemsStart(BXmlElement Element)
    {
        boolean isEnd = false;
        String name = Element.getTagName();
        if (name.equals(""))// 文本
        {
            // 判断是否为标题文本
            if (Element.parent.getTagName().equals(Tag.TAG_TITLE))
            {
                pageTitle = "" + Element.getContents();
                return isEnd;
            }
            HTMLItem item = new HTMLItem();
            item.Type = HTMLItem.TYPE_TEXT;
            item.str = Element.getContents();
            item.Href = getAtribInfo(Element, Tag.TAG_A, "href");
            if (item.Href != null)
            {
                item.isSelectable = true;
                item.FontColor = HTMLItem.SelectableFontColor;
                item.str = item.str + " ";
            }
            String pos = getAtribInfo(Element, Tag.TAG_P, "align");
            if (pos != null)
            {
                if (pos.equals("center"))
                {
                    item.pos = HTMLItem.POS_CENTER;
                }
                else if (pos.equals("right"))
                {
                    item.pos = HTMLItem.POS_RIGHT;
                }
            }
            paintHtmlVector.addElement(item);
        }
        if (name.equals(Tag.TAG_IMG))
        {
            HTMLItem item = new HTMLItem();
            item.Type = HTMLItem.TYPE_IMAGE;
            item.img = createPageImage(Element.getAttributeValue("src"));
            item.Href = getAtribInfo(Element, Tag.TAG_A, "href");
            if (item.Href != null)
            {
                item.isSelectable = true;
            }
            String pos = getAtribInfo(Element, Tag.TAG_P, "align");
            if (pos != null)
            {
                if (pos.equals("center"))
                {
                    item.pos = HTMLItem.POS_CENTER;
                }
                else if (pos.equals("right"))
                {
                    item.pos = HTMLItem.POS_RIGHT;
                }
            }
            paintHtmlVector.addElement(item);
        }
        if (name.equals(Tag.TAG_BR))// 文本
        {
            HTMLItem item = new HTMLItem();
            item.Type = HTMLItem.TYPE_BR;
            item.str = "";
            paintHtmlVector.addElement(item);
        }
        if (name.equals(Tag.TAG_P))
        {
            HTMLItem item = new HTMLItem();
            item.Type = HTMLItem.TYPE_BR;
            item.str = "";
            paintHtmlVector.addElement(item);
        }
        return isEnd;
    }

    // 获取属性
    public String getAtribInfo(BXmlElement Element, String Tag, String Attrib)
    {
        if (Element.parent != null)
        {
            if (Element.parent.getTagName().equals(Tag))
            {
                return Element.parent.getAttributeValue(Attrib);
            }
            else
            {
                return getAtribInfo(Element.parent, Tag, Attrib);
            }
        }
        return null;
    }

    public boolean bulidHTMLItemsEnd(BXmlElement Element)
    {
        boolean isEnd = false;
        String name = Element.getTagName();
        if (name.equals(Tag.TAG_P))
        {
            HTMLItem item = new HTMLItem();
            item.Type = HTMLItem.TYPE_BR;
            item.str = "";
            paintHtmlVector.addElement(item);
        }
        return isEnd;
    }

    public void layoutPage(Vector vec)
    {
        // 计算宽高
        for (int i = 0; i < vec.size(); i++)
        {
            HTMLItem item = ( HTMLItem ) vec.elementAt(i);
            switch (item.Type)
            {
                case HTMLItem.TYPE_TEXT:
                    item.width = font.stringWidth(item.str);
                    item.height = font.getHeight();
                    break;
                case HTMLItem.TYPE_IMAGE:
                    item.width = item.img.getWidth();
                    item.height = item.img.getHeight();
                    break;
                case HTMLItem.TYPE_BR:
                    item.width = 1;
                    item.height = font.getHeight();
                    break;
            }
        }
        // 计算换行坐标
        int lineX = 0;
        int lineY = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < vec.size(); i++)
        {
            HTMLItem item = ( HTMLItem ) vec.elementAt(i);
            // 处理过界换行
            lineWidth = lineWidth + item.width;
            if (lineWidth > Def.SYSTEM_SW && item.Type != HTMLItem.TYPE_BR)
            {
                if (lineWidth > item.width && item.Type != HTMLItem.TYPE_TEXT)
                {
                    HTMLItem britem = new HTMLItem();
                    britem.Type = HTMLItem.TYPE_BR;
                    britem.width = 1;
                    britem.height = font.getHeight();
                    paintHtmlVector.insertElementAt(britem, i);
                    lineWidth = lineWidth - item.width;
                    i--;
                    continue;
                }
                if (item.Type == HTMLItem.TYPE_TEXT)
                {
                    System.out.println("处理转换ing..." + ((lineWidth - item.width)));
                    // 文本分割,过界换行
                    Vector strvec = StringTools.clipStringwithWidth(item.str, Def.SYSTEM_SW - (lineWidth - item.width), Def.SYSTEM_SW);
                    for (int j = strvec.size() - 1; j >= 0; j--)
                    {
                        String newstr = ( String ) strvec.elementAt(j);
                        HTMLItem newitem = new HTMLItem();
                        newitem.Type = HTMLItem.TYPE_TEXT;
                        newitem.str = newstr;
                        newitem.x = 0;
                        newitem.y = 0;
                        newitem.width = font.stringWidth(newitem.str);
                        newitem.height = font.getHeight();
                        paintHtmlVector.insertElementAt(newitem, i);
                        // 添加换行回车
                        if (j != 0)
                        {
                            HTMLItem britem = new HTMLItem();
                            britem.Type = HTMLItem.TYPE_BR;
                            britem.width = 1;
                            britem.height = font.getHeight();
                            paintHtmlVector.insertElementAt(britem, i);
                        }
                    }
                    lineWidth = lineWidth - item.width;
                    paintHtmlVector.removeElement(item);
                    i--;
                    continue;
                }
            }
            // 处理排版
            switch (item.Type)
            {
                case HTMLItem.TYPE_TEXT:
                    item.x = lineX;
                    item.y = lineY;
                    if (item.pos == HTMLItem.POS_CENTER)
                    {
                        item.x = (Def.SYSTEM_SW + lineX - item.width) / 2;
                    }
                    else if (item.pos == HTMLItem.POS_RIGHT)
                    {
                        item.x = Def.SYSTEM_SW - item.width;
                    }
                    lineX = lineX + item.width;
                    lineHeight = Math.max(lineHeight, item.height);
                    break;
                case HTMLItem.TYPE_BR:
                    item.x = lineX;
                    item.y = lineY;
                    lineX = 0;
                    lineWidth = 0;
                    lineHeight = Math.max(lineHeight, item.height);
                    lineY = lineY + lineHeight;
                    lineHeight = 0;
                    vec.removeElement(item);
                    i--;
                    break;
                case HTMLItem.TYPE_IMAGE:
                    item.x = lineX;
                    item.y = lineY;
                    if (item.pos == HTMLItem.POS_CENTER)
                    {
                        item.x = (Def.SYSTEM_SW + lineX - item.width) / 2;
                    }
                    else if (item.pos == HTMLItem.POS_RIGHT)
                    {
                        item.x = Def.SYSTEM_SW - item.width;
                    }
                    lineX = lineX + item.width;
                    lineHeight = Math.max(lineHeight, item.height);
                    break;
            }
        }
        pageLength = Math.max(Def.SYSTEM_SH, lineY + lineHeight);
        indexLegth = 0;
    }

    // **********************************************
    // **********************************************
    // **********************************************
    // 图片cache部分
    // **********************************************
    // **********************************************
    // **********************************************
    public class ImageCache
    {

        public Image img;
        public String url;

        public ImageCache(String url, Image img)
        {
            this.img = img;
            this.url = url;
        }
    }

    public Image createPageImage(String url)
    {
        Image img = null;
        for (int i = 0; i < ImagePools.size(); i++)
        {
            ImageCache ic = ( ImageCache ) ImagePools.elementAt(i);
            if (ic.url.equals(url))
            {
                img = ic.img;
                return img;
            }
        }
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
            img = ImageTools.creatImage("/" + url);
        }
        if (img == null)
        {
            img = defualtImage;
        }
        else
        {
            ImagePools.addElement(new ImageCache(url, img));
        }
        return img;
    }

    // **********************************************
    // **********************************************
    // **********************************************
    // 绘画处理部分
    // **********************************************
    // **********************************************
    // **********************************************
    public void paint(Graphics g)
    {
        keyState();
        if (paintHtmlVector != null)
        {
            for (int i = 0; i < paintHtmlVector.size(); i++)
            {
                HTMLItem item = ( HTMLItem ) paintHtmlVector.elementAt(i);
                int paintX = item.x;
                int paintY = item.y - indexLegth;
                if (paintY + item.width > -30 && paintY < Def.SYSTEM_SH + 30)
                {
                    switch (item.Type)
                    {
                        case HTMLItem.TYPE_TEXT:
                            if (!item.isSelected)
                            {
                                g.setColor(item.FontColor);
                                g.drawString(item.str, paintX, paintY, 0);
                            }
                            else
                            {
                                g.setColor(item.FontColor);
                                g.fillRect(paintX, paintY, item.width, item.height);
                                g.setColor(0xffffff);
                                g.drawString(item.str, paintX, paintY, 0);
                            }
                            break;
                        case HTMLItem.TYPE_IMAGE:
                            if (!item.isSelected)
                            {
                                g.drawImage(item.img, paintX, paintY, 0);
                            }
                            else
                            {
                                g.drawImage(item.img, paintX, paintY, 0);
                                g.setColor(item.FontColor);
                                g.drawRect(paintX, paintY, item.width - 1, item.height - 1);
                                g.drawRect(paintX + 1, paintY + 1, item.width - 3, item.height - 3);
                          
                            }
                            break;
                    }
                }
            }
        }
        if (GoSpeed != 0 || keyState != KEYSTATE_STOP)
        {
//            g.setColor(0xffff00);
//            g.fillRect(0, 0, Def.SYSTEM_SW, font.getHeight());
//            g.setColor(0x000000);
//            g.drawString(pageTitle + "  " + (indexLegth / font.getHeight()) + "/" + (pageLength / font.getHeight()) + "行", Def.SYSTEM_SW, 0, Def.TOP_RIGHT);
       
            g.setColor(0xaa0000);
            int yindex=Def.SYSTEM_SH*indexLegth/pageLength;
            int scrollLength=2+Def.SYSTEM_SH*Def.SYSTEM_SH/pageLength;
            
             g.fillRect(Def.SYSTEM_SW-3, yindex, 3, scrollLength);
        
        }
    }

    // **********************************************
    // **********************************************
    // **********************************************
    // 按键处理部分,选中处理
    // **********************************************
    // **********************************************
    // **********************************************
    int SelectedIndex = 0;
    HTMLItem SelectedItem = null;
    public static final byte SelectedUP = 0;
    public static final byte SelectedDOWN = 1;

    public void setSelected(byte up)
    {
        switch (up)
        {
            case SelectedUP:
                for (int i = SelectedIndex - 1; i >= 0; i--)
                {
                    HTMLItem item = ( HTMLItem ) paintHtmlVector.elementAt(i);
                    if (item.isSelectable)
                    {
                        if (SelectedItem != null)
                        {
                            SelectedItem.isSelected = false;
                        }
                        SelectedItem = item;
                        SelectedItem.isSelected = true;
                        SelectedIndex = i;
                        break;
                    }
                }
                break;
            case SelectedDOWN:
                for (int i = SelectedIndex + 1; i < paintHtmlVector.size(); i++)
                {
                    HTMLItem item = ( HTMLItem ) paintHtmlVector.elementAt(i);
                    if (item.isSelectable)
                    {
                        if (SelectedItem != null)
                        {
                            SelectedItem.isSelected = false;
                        }
                        SelectedItem = item;
                        SelectedItem.isSelected = true;
                        SelectedIndex = i;
                        break;
                    }
                }
                break;
        }
    }

    public void commandAction()
    {
        if (SelectedItem != null)
        {
            String uurl = SelectedItem.Href;
            System.out.println("commandAction:" + uurl);
            org.ikags.mreader.SReader.reader.openUrl(uurl,true,0);
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
                setSelected(SelectedUP);
                keyState = KEYSTATE_UP;
                break;
            case Def.KEY_DOWN:
            case Def.KEY_8:
                setSelected(SelectedDOWN);
                keyState = KEYSTATE_DOWN;
                break;
            case Def.KEY_MIDDLE:
            case Def.KEY_5:
                commandAction();
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

    int pageLength = Def.SYSTEM_SH;
    public int indexLegth = 0;
    private int GoSpeed = 0;
    private byte keyState = 0;
    private static final byte KEYSTATE_STOP = 0;
    private static final byte KEYSTATE_UP = 1;
    private static final byte KEYSTATE_DOWN = 2;
}
