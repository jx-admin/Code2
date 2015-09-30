package org.ikags.util.bxml;

import java.util.Vector;

/**
 * ika BXml解析器 <BR>
 * TAG类<BR>
 * 解析BXMLElement的过程类<BR>
 * 更新:从内部类独立出来.
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2009.2.16 最后更新2009.2.19
 * @version 0.2
 */
public class Tag
{
    public static final String TAG_A = "a";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_ANCHOR = "anchor";
    public static final String TAG_ASPAPI = "aspapi";
    public static final String TAG_ASPCONTACT = "aspcontact";
    public static final String TAG_ASPDOWNLOAD = "aspdownload";
    public static final String TAG_ASPINSTALL = "aspinstall";
    public static final String TAG_ASPLEFTKEY = "aspleftkey";
    public static final String TAG_ASPLISTEXT = "asplistext";
    public static final String TAG_ASPLISTHIDE = "asplisthide";
    public static final String TAG_ASPPRELOAD = "asppreload";
    public static final String TAG_ASPSHORTCUT = "aspshortcut";
    public static final String TAG_ASPTAB = "asptab";
    public static final String TAG_B = "b";
    public static final String TAG_BASE = "base";
    public static final String TAG_BIG = "big";
    public static final String TAG_BOBY = "body";
    public static final String TAG_BR = "br";
    public static final String TAG_CARD = "card";
    public static final String TAG_CENTER = "center";
    public static final String TAG_DIV = "div";
    public static final String TAG_EM = "em";
    public static final String TAG_FORM = "form";
    public static final String TAG_FRAME = "frame";
    public static final String TAG_FRAMESET = "frameset";
    public static final String TAG_GO = "go";
    public static final String TAG_H1 = "h1";
    public static final String TAG_H2 = "h2";
    public static final String TAG_H3 = "h3";
    public static final String TAG_H4 = "h4";
    public static final String TAG_H5 = "h5";
    public static final String TAG_H6 = "h6";
    public static final String TAG_HEAD = "head";
    public static final String TAG_HR = "hr";
    public static final String TAG_HTML = "html";
    public static final String TAG_I = "i";
    public static final String TAG_IMG = "img";
    public static final String TAG_INPUT = "input";
    public static final String TAG_LI = "li";
    public static final String TAG_MENU = "menu";
    public static final String TAG_MENUITEM = "menuitem";
    public static final String TAG_MENUPOPUP = "menupopup";
    public static final String TAG_MENUSEPARATOR = "menuseparator";
    public static final String TAG_OL = "ol";
    public static final String TAG_OPTION = "option";
    public static final String TAG_P = "p";
    public static final String TAG_PARAM = "param";
    public static final String TAG_POSTFIELD = "postfield";
    public static final String TAG_PRE = "pre";
    public static final String TAG_SCRIPT = "script";
    public static final String TAG_SELECT = "select";
    public static final String TAG_SMALL = "small";
    public static final String TAG_SPAN = "span";
    public static final String TAG_STRONG = "strong";
    public static final String TAG_STYLE = "style";
    public static final String TAG_TABLE = "table";
    public static final String TAG_TD = "td";
    public static final String TAG_TH = "th";
    public static final String TAG_TITLE = "title";
    public static final String TAG_TR = "tr";
    public static final String TAG_UL = "ul";
    /**
     * TAG的名字
     */
    public String name = "TAG_NOTSET";
    private BXmlElement elementElement;
    private BXmlElement parentElement;

    /**
     * 初始化方法
     */
    public Tag()
    {
        elementElement = new BXmlElement();
    }

    /**
     * 链接父节点和子节点
     * 
     * @param parent
     */
    private void connectParent(Tag parent)
    {
        if (parent != null)
        {
            parentElement = parent.elementElement;
        }
    }

    /**
     * 提交 节点信息,当处于HTML节点时候,返回给ROOT节点
     * 
     * @param rootelement
     * @param parser
     * @param tagStack
     * @return  BXmlElement
     */
    public BXmlElement handleTagStart(BXmlElement rootelement, KXmlParser parser, Vector tagStack)
    {
        Tag parentTag = BXmlDriver.topTag(tagStack);
        connectParent(parentTag);
        String name = parser.getName().toLowerCase();
        this.name = name;
        elementElement.setTagName(name);
        if (TAG_HTML.equals(name))
        {
            rootelement = elementElement;
            return rootelement;
        }
        // parser 储存到elementElement 开始
        // 处理Attribute
        int AttributeCount = parser.getAttributeCount();
        if (AttributeCount > 0)
        {
            elementElement.setNewAttribute(AttributeCount);
            for (int i = 0; i < AttributeCount; i++)
            {
                elementElement.setAttributeName(i, parser.getAttributeName(i).toLowerCase());
                elementElement.setAttributeValue(i, parser.getAttributeValue(i));
            }
        }
        // parser 储存到elementElement 完毕
        if (parentElement != null)
        {
            parentElement.getChildren().addElement(elementElement);
        }
        return null;
    }

    /**
     * 提交TEXT文字信息
     * 
     * @param txt
     * @param tagStack
     */
    public void handleTagTxt(String txt, Vector tagStack)
    {
        Tag parentTag = BXmlDriver.topTag(tagStack);
        connectParent(parentTag);
        String name = "";
        this.name = name;
        elementElement.setTagName(name);
        elementElement.setContents(txt);
        if (parentElement != null)
        {
            parentElement.getChildren().addElement(elementElement);
        }
        txt = null;
    }
}
