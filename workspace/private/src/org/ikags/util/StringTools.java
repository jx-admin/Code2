package org.ikags.util;

//import java.io.DataInputStream;
//import java.io.IOException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
//import javax.microedition.io.Connector;
//import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;

/**
 * I.K.A Engine<BR>
 * StringTools类，主要用于读取txt和ini等文件,绘制String字符,以及对String进行切割使用<BR>
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2008.11.15 最后更新 2009.5.20
 * @version 0.2
 */
public class StringTools
{

    /**
     * drawWords调用的行距
     */
    public static int between_2line;
    private static Hashtable hash = new Hashtable();
    private static String[] WordsDrawing = null;

    /**
     * 整段文字绘画.根据传入数据来绘画,把String 自动转化为String[],然后显示出来,String[]存入一个hashtable中以便于下次继续使用
     * 
     * @param words
     * @param x
     * @param y
     * @param ach
     * @param fontcolor
     * @param bgcolor
     * @param use_bloom
     * @param g
     * @param lineWidth
     * @param fontHeight
     */
    public static final void drawWordswithEnter(String words, int x, int y, int ach, int fontcolor, int bgcolor, int use_bloom, Graphics g, int lineWidth, int fontHeight)
    {
        if (words == null)
        {
            return;
        }
        else
        {
            WordsDrawing = ( String[] ) hash.get(words);
        }
        if (WordsDrawing == null)
        {
            String[] strings = clipStringwithWidth2StringMatrix(words, lineWidth, lineWidth);
            hash.put(words, strings);
            WordsDrawing = ( String[] ) hash.get(words);
        }
        between_2line = fontHeight + 1;// 两行之间的距离
        for (int i = 0; i < WordsDrawing.length; i++)
        {
            int offy = y + i * between_2line;
            if (offy > g.getTranslateY() - fontHeight && offy < g.getClipHeight())
            {
                switch (use_bloom)
                {
                    case 0:
                        g.setColor(fontcolor);
                        g.drawString(WordsDrawing[i], x, offy, ach);
                        break;
                    case 1:
                        drawWord_half(WordsDrawing[i], x, offy, ach, fontcolor, bgcolor, g);
                        break;
                    case 2:
                        drawWord_all(WordsDrawing[i], x, offy, ach, fontcolor, bgcolor, g);
                        break;
                }
            }
        }
    }

    /**
     * 整段文字绘画.根据传入数据来绘画,
     * 
     * @param words
     * @param x
     * @param y
     * @param ach
     * @param fontcolor
     * @param bgcolor
     * @param use_bloom
     * @param g
     * @param fontHeight
     */
    public static final void drawWordswithEnter(String[] words, int x, int y, int ach, int fontcolor, int bgcolor, int use_bloom, Graphics g, int fontHeight)
    {
        if (words == null)
        {
            return;
        }
        between_2line = fontHeight + 1;// 两行之间的距离
        for (int i = 0; i < words.length; i++)
        {
            int offy = y + i * between_2line;
            if (offy > g.getTranslateY() - fontHeight && offy < g.getClipHeight() + fontHeight)
            {
                switch (use_bloom)
                {
                    case 0:
                        g.setColor(fontcolor);
                        g.drawString(words[i], x, offy, ach);
                        break;
                    case 1:
                        drawWord_half(words[i], x, offy, ach, fontcolor, bgcolor, g);
                        break;
                    case 2:
                        drawWord_all(words[i], x, offy, ach, fontcolor, bgcolor, g);
                        break;
                }
            }
        }
    }

    /**
     * 调用下面的泛光写字符函数（全包）
     */
    public static final void drawWord_all(String str, int x, int y, int ach, int font_color, int bg_color, Graphics g)
    {
        g.setColor(bg_color);
        g.drawString(str, x - 1, y, ach);
        g.drawString(str, x, y - 1, ach);
        g.drawString(str, x, y + 1, ach);
        g.drawString(str, x + 1, y, ach);
        g.setColor(font_color);
        g.drawString(str, x, y, ach);
    }

    /**
     * 调用下面的泛光写字符函数（半包）
     */
    public static final void drawWord_half(String str, int x, int y, int ach, int font_color, int bg_color, Graphics g)
    {
        g.setColor(bg_color);
        g.drawString(str, x, y + 1, ach);
        g.drawString(str, x + 1, y, ach);
        g.setColor(font_color);
        g.drawString(str, x, y, ach);
    }

    /**
     * 读取txt文件,返回存入String[]
     * 
     * @param path
     * @return String[]
     */
    public static String[] ReadTxt2String(String path, String encode)
    {
        try
        {
            Vector ini = readTxt2Vector(path, encode);
            String[] strs = new String[ini.size()];
            for (int i = 0; i < strs.length; i++)
            {
                strs[i] = ( String ) ini.elementAt(i);
            }
            return strs;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 读取txt UTF8文本数据,每行保存到Vector的String对象里面
     * 
     * @param path
     * @return Vector
     */
    public static Vector readTxt2Vector(String path, String encode)
    {
        Vector scripts = new Vector();
        try
        {
            InputStream is = null;
            byte[] bt = null;
            is = "i".getClass().getResourceAsStream(path);
            bt = new byte[is.available()];// 本地读取INI支持available
            int length = is.read(bt);
            is.close();
            is = null;
            String buf = new String(bt, 0, length, encode);
            // 存入数据
            int strstart = 0;
            int strend = 0;
            for (int i = 0;; i++)
            {
                strend = buf.indexOf("\n", strstart);
                if (strend == -1)
                {
                    return scripts;
                }
                scripts.addElement(buf.substring(strstart, strend - 1));
                strstart = strend + 1;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return scripts;
    }

    public static Vector readbytes2Vector(byte[] bt, String encode)
    {
        Vector scripts = new Vector();
        try
        {
            String buf = new String(bt, 0, bt.length, encode);
            // 存入数据
            int strstart = 0;
            int strend = 0;
            for (int i = 0;; i++)
            {
                strend = buf.indexOf("\n", strstart);
                if (strend == -1)
                {
                    return scripts;
                }
                scripts.addElement(buf.substring(strstart, strend - 1));
                strstart = strend + 1;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return scripts;
    }

    /**
     * 读取txt 指定encode文本数据，自动按照文本分行
     * 
     * @param path
     * @return String[]
     */
    public static final String[] readTxt2String(String path, String encode)
    {
        String[] str_info = null;
        try
        {
            InputStream is = null;
            byte[] bt = null;
            is = "i".getClass().getResourceAsStream(path);
            bt = new byte[is.available()];
            int length = is.read(bt);
            is.close();
            is = null;
            String buf = new String(bt, 0, length, encode);
            int strstart = 0;
            int strend = 0;
            int total = 0;
            // 统计多少行
            for (int i = 0;; i++)
            {
                strend = buf.indexOf("\n", strstart);
                if (strend == -1)
                {
                    total = i;
                    break;
                }
                else
                {
                    strstart = strend + 1;
                }
            }
            System.out.println("total:" + total);
            // 存入数据
            strstart = 0;
            strend = 0;
            str_info = new String[total];
            for (int i = 0; i < total; i++)
            {
                strend = buf.indexOf("\n", strstart);
                str_info[i] = buf.substring(strstart, strend - 1);
                strstart = strend + 1;
            }
        }
        catch (Exception ex)
        {
            System.out.println("load_script  script_load error");
            ex.printStackTrace();
        }
        return str_info;
    }

    /**
     * String 转 Int
     * 
     * @param s
     * @return int
     */
    public static int String2Int(String s)
    {
        int i = 0;
        try
        {
            i = Integer.parseInt(s);
        }
        catch (Exception ex)
        {
            System.out.println("ERROR:String2Int error:" + s);
            return 0;
        }
        return i;
    }

    /**
     * CVS格式读取,反馈给一个以Vector嵌套的Vector里
     * 
     * @param path
     * @param encode
     * @return Vector
     */
    public static Vector readCSV(String path, String encode)
    {
        String[] list;
        list = readTxt2String(path, encode);// "GB2312"
        Vector root = new Vector();
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < list.length; i++)
        {
            // str.substring(0, str.indexOf(";", 0))
            Vector child = new Vector();
            int start = 0;
            int end = 0;
            for (int j = 0;; j++)
            {
                sbu.delete(0, sbu.length());
                end = list[i].indexOf(",", start);
                if (end > 0)
                {
                    // 检测到分割
                    sbu.append(list[i].substring(start, end));
                    child.addElement(sbu.toString());
                    start = end + 1;
                }
                else
                {
                    // 后面没有检测到分割
                    sbu.append(list[i].substring(start, list[i].length()));
                    child.addElement(sbu.toString());
                    break;
                }
            }
            root.addElement(child);
        }
        return root;
    }

    /**
     * 按照给定的宽度划分Str数据.用于网页排版
     * 
     * @param str
     * @param startwidth
     * @param width
     * @return Vector反馈一个由String组成的Vector
     */
    public static Vector clipStringwithWidth(String str, int startwidth, int width)
    {
        char[] chars;
        chars = str.toCharArray();
        Vector vec = new Vector();
        boolean isfirstLine = true;
        int start = 0;
        int linew = 0;
        for (int i = 0; i < chars.length; i++)
        {
            linew++;
            int w = Def.font.charsWidth(chars, start, linew);
            if (isfirstLine)
            {
                if (w > startwidth)
                {
                    isfirstLine = false;
                    vec.addElement(String.valueOf(chars, start, linew - 1));
                    start = start + linew - 1;
                    i--;
                    linew = 0;
                }
            }
            else
            {
                if (w > width)
                {
                    vec.addElement(String.valueOf(chars, start, linew - 1));
                    start = start + linew - 1;
                    i--;
                    linew = 0;
                }
            }
            if (i == chars.length - 1)
            {
                vec.addElement(String.valueOf(chars, start, chars.length - start));
            }
        }
        return vec;
    }

    /**
     * 将以Vector保存的String集合,根据宽度再重新分行.便于显示和页面处理
     * 
     * @param rootvec
     * @param width
     * @return 反馈一个String[]
     */
    public static String[] clipVectorStringswithWidth2StringMatrix(Vector rootvec, int width)
    {
        if (rootvec == null)
        {
            return null;
        }
        Vector vec = new Vector();
        for (int i = 0; i < rootvec.size(); i++)
        {
            Vector temp = clipStringwithWidth(( String ) rootvec.elementAt(i), width, width);
            if (temp != null)
            {
                for (int j = 0; j < temp.size(); j++)
                {
                    vec.addElement(temp.elementAt(j));
                }
            }
        }
        String[] strs = null;
        if (vec != null)
        {
            strs = new String[vec.size()];
            for (int i = 0; i < vec.size(); i++)
            {
                strs[i] = ( String ) vec.elementAt(i);
            }
        }
        else
        {
            strs = new String[1];
            strs[0] = "";
        }
        return strs;
    }

    /**
     * 根据宽度将String转化为String[]
     * 
     * @param str
     * @param startwidth
     * @param width
     * @return 反馈一个String[]数组
     */
    public static String[] clipStringwithWidth2StringMatrix(String str, int startwidth, int width)
    {
        Vector vec = clipStringwithWidth(str, startwidth, width);
        String[] strs = null;
        if (vec != null)
        {
            strs = new String[vec.size()];
            for (int i = 0; i < vec.size(); i++)
            {
                strs[i] = ( String ) vec.elementAt(i);
            }
        }
        else
        {
            strs = new String[1];
            strs[0] = "";
        }
        return strs;
    }

    /**
     * FileConnection 读取数据返回byte数组
     * 
     * @param filepath
     * @return
     */
    public static byte[] readFile(String filepath)
    {
        byte data[] = null;
        try
        {
            FileConnection fc = ( FileConnection ) Connector.open(filepath);
            DataInputStream dis = fc.openDataInputStream();
            int ch;// 每次读出的数据
            int index = 0;// 读取的数据的总索引
            int len = 1024;// 放数据的空间不够时，每次扩充空间的大小为1024字节
            byte buf[];// 暂时存放从data[]拷贝出来的数据
            data = new byte[len];// 先初步设定一个1k的内存空间
            while ((ch = dis.read()) != -1)
            {
                data[index] = ( byte ) ch;
                index++;
                if (index >= len)
                {
                    len += 1024;
                    buf = new byte[len];
                    System.arraycopy(data, 0, buf, 0, index);
                    data = null;
                    data = buf;
                }
            }
            // 此时data[]的长度可能要比实际数据多，最后的一些字节可能是一些无效的数据，去掉无效数据
            if ((index % 1024) != 0)
            {
                buf = new byte[index];// index的值是数据的实际大小
                System.arraycopy(data, 0, buf, 0, index);
                System.out.println("index:" + index);
                data = null;
                data = buf;
            }
            if (dis != null)
            {
                dis.close();
                dis = null;
            }
            if (fc != null)
            {
                fc.close();
                fc = null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
