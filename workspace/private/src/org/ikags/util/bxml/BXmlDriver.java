package org.ikags.util.bxml;

import java.io.Reader;
import java.util.Vector;

/**
 * ika BXml解析器 <BR>
 * 此类为解析驱动类,使用方法如下:<BR>
 * 使用方法<BR>
 * BXmlElement rootElement = new BXmlElement();<BR>
 * rootElement = BXmlDriver.loadXML(new InputStreamReader(in, "UTF8"), rootElement);<BR>
 * 序列化方法<BR>
 * byte[] bytes = rootElement.serialize();<BR>
 * 反序列方法<BR>
 * BXmlElement serializeXML = BXmlElement.deserialize(bytes);<BR>
 * 打印<BR>
 * serializeXML.print(0);<BR>
 * 可用debug模式查看生成的rootElement结构.或者直接rootElement.print();查看打印结果<BR>
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2009.2.16 更新 2009.2.19
 * @version 0.2
 */
public class BXmlDriver
{

    /**
     * 初始化
     */
    public BXmlDriver()
    {
    }

    /**
     * 需要及时闭合的TAG标签String数组
     */
    public static String[] TAG_CLOSED = { "br", "input", "meta", "hr" };
    /**
     * 特殊处理的TAG(2种使用方式)
     */
    public static String TAG_IMG = "img";
    /**
     * Html解析修复是否开启
     */
    public static boolean HtmlFix = true;

    /**
     * 读取流文件,解析成以BXmlElement为节点的结构.
     * 
     * @param reader
     * @param rootelement
     * @return root级的BXmlElement
     */
    public static BXmlElement loadXML(Reader reader, BXmlElement rootelement)
    {
        if (rootelement == null)
        {
            System.out.println("element 必须实例化");
            return null;
        }
        try
        {
            Vector tagStack = new Vector();
            KXmlParser parser = new KXmlParser();
            parser.setInput(reader);
            BXmlElement rtelement = null;
            parser.setFeature(org.ikags.util.bxml.KXmlPullParser.FEATURE_RELAXED, true);
            int type = 0;
            String lastTag = "";
            int lastType = 0;
            System.out.println("=>start Of Document<=");
            while ((type = parser.next()) != KXmlParser.END_DOCUMENT)
            {
                // 排除设置,自动闭合
                if (HtmlFix == true)
                {
                    parser = IMGTag(TAG_IMG, lastTag, lastType, type, parser, tagStack);
                    type = thistype;
                    for (int i = 0; i < TAG_CLOSED.length; i++)
                    {
                        endTag(TAG_CLOSED[i], lastTag, lastType, type, parser, tagStack);
                    }
                }
                if (type == KXmlParser.START_TAG)
                {
                    String name = parser.getName().toLowerCase();
                    if (name != null)
                    {
                        try
                        {
                            Tag t = new Tag();
                            rtelement = t.handleTagStart(rootelement, parser, tagStack);
                            if (rtelement != null)
                            {
                                rootelement = rtelement;
                            }
                            pushTag(tagStack, t);
                            t = null;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    lastTag = name;
                    lastType = type;
                }
                else if (type == KXmlParser.END_TAG)
                {
                    String name = parser.getName().toLowerCase();
                    if (name != null)
                    {
                        Tag t = ( Tag ) topTag(tagStack);
                        if (t != null && name.equals(t.name))
                        {
                            removeTag(tagStack);
                        }
                        t = null;
                    }
                    lastTag = name;
                    lastType = type;
                }
                else if (type == KXmlParser.TEXT)
                {
                    String txt = parser.getText();
                    txt = nospace_str(txt);
                    if (txt != null && txt.length() > 0)
                    {
                        Tag t = new Tag();
                        t.handleTagTxt(txt, tagStack);
                        pushTag(tagStack, t);
                        removeTag(tagStack);
                        t = null;
                    }
                    lastTag = "";
                    lastType = type;
                }
            }
            tagStack.removeAllElements();
            System.out.println("=>End Of Document<=");
        }
        catch (Exception e)
        {
            System.out.println("ERROR:run KXmlParser error");
            e.printStackTrace();
        }
        // 清理
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
                System.gc();
            }
            catch (Exception e)
            {
                System.out.println("ERROR:run finally error");
                e.printStackTrace();
            }
        }
        resetElement(rootelement);
        return rootelement;
    }

    /**
     * 处理next()等方法
     * 
     * @param root
     */
    public static void resetElement(BXmlElement root)
    {
        int size = root.getChildren().size();
        for (int i = 0; i < size; i++)
        {
            BXmlElement element = ( BXmlElement ) root.getChildren().elementAt(i);
            element.parent = root;
            if (i < size - 1)
            {
                element.next = ( BXmlElement ) root.getChildren().elementAt(i + 1);
            }
            if (i > 0)
            {
                element.previous = ( BXmlElement ) root.getChildren().elementAt(i - 1);
            }
            resetElement(element);
        }
    }

    private static int thistype = 0;

    /**
     * 处理图片标签的2种可能
     * 
     * @param TAG
     * @param lastTag
     * @param lastType
     * @param type
     * @param parser
     * @param tagStack
     */
    private static KXmlParser IMGTag(String TAG, String lastTag, int lastType, int type, KXmlParser parser, Vector tagStack)
    {
        thistype = type;
        if (TAG == null)
        {
            return parser;
        }
        if (TAG.equals(lastTag) && lastType == KXmlParser.START_TAG)
        {
            if (type == KXmlParser.TEXT)
            {
                // IMG特殊处理
                try
                {
                    type = parser.next();
                    thistype = type;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            String thisname = null;
            if (type == KXmlParser.START_TAG)
            {
                thisname = parser.getName().toLowerCase();
            }
            if ("param".equals(thisname))
            {
                // IMG TAG开区间,特殊用法
            }
            else if (type == KXmlParser.END_TAG && TAG.equals(parser.getName().toLowerCase()))
            {
                // 正常
            }
            else
            {
                // 异常忘记删除标签
                removeTag(tagStack);
            }
        }
        return parser;
    }

    /**
     * 处理闭合TAG但是人为写成非闭合TAG的改正方法
     * 
     * @param TAG
     * @param lastTag
     * @param lastType
     * @param type
     * @param parser
     * @param tagStack
     */
    private static void endTag(String TAG, String lastTag, int lastType, int type, KXmlParser parser, Vector tagStack)
    {
        if (TAG == null)
        {
            return;
        }
        if (TAG.equals(lastTag) && lastType == KXmlParser.START_TAG)
        {
            if (type == KXmlParser.END_TAG && TAG.equals(parser.getName().toLowerCase()))
            {}
            else
            {
                removeTag(tagStack);
            }
        }
    }

    public static void pushTag(Vector tagStack, Tag node)
    {
        tagStack.addElement(node);
    }

    public static Tag topTag(Vector tagStack)
    {
        if (tagStack.size() > 0)
        {
            return ( Tag ) tagStack.lastElement();
        }
        return null;
    }

    public static Tag removeTag(Vector tagStack)
    {
        int size = tagStack.size();
        if (size > 0)
        {
            Tag tc = ( Tag ) tagStack.lastElement();
            tagStack.removeElementAt(size - 1);
            return tc;
        }
        return null;
    }

    /**
     * 清除空格.清除如:反斜杠+t,反斜杠+n,反斜杠+r,普通空格,TAB空格
     * 
     * @param str 处理之前的String
     * @return 处理之后的String
     */
    public static String nospace_str(String str)
    {
        if (str == null)
        {
            return null;
        }
        // 驱除句子中间的TAB空格,回车,
        StringBuffer strb = new StringBuffer();
        for (int i = 0; i < str.length(); i++)
        {
            String tmp_str = str.substring(i, i + 1);
            if (tmp_str.equals(" "))
            {}
            else if (tmp_str.equals("\t"))
            {}
            else if (tmp_str.equals("\n"))
            {}
            else if (tmp_str.equals("\r"))
            {}
            else if (tmp_str.equals("　"))
            {}
            else
            {
                strb.append(tmp_str);
            }
        }
        str = strb.toString();
        return str;
    }
}
