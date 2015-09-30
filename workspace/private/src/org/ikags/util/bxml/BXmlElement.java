package org.ikags.util.bxml;

import java.util.Vector;
    /**
     * ika BXml解析器 <BR>
     * 此类为基本节点类,使用方法请参考:<BR>
     * BXmlDriver类的相关说明<BR>
     * 更新:增加序列化和反序列化方法,对部分方法的兼容性维护.允许一些错误输入的出现
     * 
     * @author http://airzhangfish.spaces.live.com
     * @since 2009.2.16 更新 2009.2.27
     * @version 0.3
     */
public class BXmlElement
{


    public BXmlElement()
    {
        children = new Vector();
    }
    private Vector children;
    private String[] attributesName;
    private String[] attributesValue;
    private String tagName;
    private String contents;
    /**
     * 此节点的父节点,如果是root节点则为null
     */
    public BXmlElement parent = null;
    /**
     * 此节点所在的父节点的上一个节点,如果是第一个节点则为null
     */
    public BXmlElement previous = null;
    /**
     * 此节点所在的父节点的下一个节点,如果是最后一个节点则为null
     */
    public BXmlElement next = null;

    /**
     * 初始化attributes的大小(生成此节点时调用,一般不需要使用此方法)
     * 
     * @param size
     */
    public void setNewAttribute(int size)
    {
        attributesName = new String[size];
        attributesValue = new String[size];
    }

    /**
     * 设置attributesName[index]的Name(生成此节点时调用,一般不需要使用此方法)
     * 
     * @param index
     * @param Name
     */
    public void setAttributeName(int index, String Name)
    {
        if (index < 0 || index > attributesName.length - 1)
        {
            return;
        }
        attributesName[index] = Name;
    }

    /**
     * 设置attributesValue[index]的name(生成此节点时调用,一般不需要使用此方法)
     * 
     * @param index
     * @param Value
     */
    public void setAttributeValue(int index, String Value)
    {
        if (index < 0 || index >= attributesValue.length)
        {
            return;
        }
        attributesValue[index] = Value;
    }

    /**
     * 获取attributes指定index的name
     * 
     * @param index
     * @return String
     */
    public String getAttributeName(int index)
    {
        if (attributesName == null)
        {
            return null;
        }
        if (index < 0 || index >= attributesName.length)
        {
            return null;
        }
        return attributesName[index];
    }

    /**
     * 获取attributes指定index的Value
     * 
     * @param index
     * @return String
     */
    public String getAttributeValue(int index)
    {
        if (attributesValue == null)
        {
            return null;
        }
        if (index < 0 || index >= attributesValue.length)
        {
            return null;
        }
        return attributesValue[index];
    }

    /**
     * 获取attributes的数目
     * 
     * @return int
     */
    public int getAttributeCounts()
    {
        if (attributesValue == null)
        {
            return 0;
        }
        return attributesValue.length;
    }

    /**
     * 获取attributes指定name的Value
     * 
     * @param abName
     * @return String
     */
    public String getAttributeValue(String abName)
    {
        if (abName == null)
        {
            return null;
        }
        if (attributesName == null)
        {
            return null;
        }
        if (attributesValue == null)
        {
            return null;
        }
        for (int i = 0; i < attributesName.length; i++)
        {
            if (attributesName[i].equals(abName))
            {
                return attributesValue[i];
            }
        }
        return null;
    }

    /**
     * 获取此节点的Vector
     * 
     * @return Vector
     */
    public Vector getChildren()
    {
        return this.children;
    }

    /**
     * 获取此节点下指定的子节点
     * 
     * @param index
     * @return BXmlElement
     */
    public BXmlElement getChildrenElement(int index)
    {
        if (index < 0 || index >= children.size())
        {
            return null;
        }
        return ( BXmlElement ) children.elementAt(index);
    }

    /**
     * 在此节点下增加一个子节点
     * 
     * @param el
     * @return boolean
     */
    public boolean addChildrenElement(BXmlElement el)
    {
        children.addElement(el);
        return true;
    }

    /**
     * 删除此节点下的指定子节点
     * 
     * @param index
     * @return boolean
     */
    public boolean removeChildrenElement(int index)
    {
        if (index < 0 || index >= children.size())
        {
            return false;
        }
        children.removeElementAt(index);
        return true;
    }

    /**
     * 删除此节点下的所有子节点
     * 
     * @return boolean
     */
    public boolean removeAllChildrenElements()
    {
        children.removeAllElements();
        return true;
    }

    /**
     * 获取Contents
     * 
     * @return String
     */
    public String getContents()
    {
        return contents;
    }

    /**
     * 对Contents赋值
     * 
     * @param contents
     */
    public void setContents(String contents)
    {
        this.contents = contents;
    }

    /**
     * 获取TagName
     * 
     * @return String
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * 对TagName赋值
     * 
     * @param tagName
     */
    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    /**
     * 打印以此element为root的整颗树
     */
    public void print(int indentNum)
    {
        for (int i = 0; i < indentNum; i++)
        {
            //System.out.print("____");
        }
        if ("".equals(tagName))
        {
            if (contents != null)
            {
                System.out.println(contents);
            }
        }
        else
        {
            //System.out.print("<" + tagName + " ");
            if (attributesName != null)
            {
                for (int i = 0; i < attributesName.length; i++)
                {
                    //System.out.print(attributesName[i] + "=\"" + attributesValue[i] + "\" ");
                }
            }
            if (children.size() == 0)
            {
                System.out.println("/>");
            }
            else
            {
                System.out.println(">");
                for (int i = 0; i < children.size(); i++)
                {
                    BXmlElement elt = ( BXmlElement ) children.elementAt(i);
                    elt.print(indentNum + 1);
                }
                for (int i = 0; i < indentNum; i++)
                {
                    //System.out.print("____");
                }
                System.out.println("</" + tagName + ">");
            }
        }
    }

    /**
     * 打印以此element为root的整颗树
     */
    public void printNode(int indentNum)
    {
        for (int i = 0; i < indentNum; i++)
        {
            //System.out.print("    ");
        }
        if ("".equals(tagName) && contents != null) {

            System.out.println(contents);            
        }
        else
        {
            //System.out.print("└──<" + tagName + "> ");
            if (attributesName != null)
            {
                for (int i = 0; i < attributesName.length; i++)
                {
                    //System.out.print(attributesName[i] + "=\"" + attributesValue[i] + "\", ");
                }
            }
            System.out.println(""); 
            
            if (children.size() > 0) {              
                for (int i = 0; i < children.size(); i++)
                {
                    BXmlElement element = ( BXmlElement ) children.elementAt(i);
                    element.printNode(indentNum + 1);
                }
            }
        }
    }
    
    
    /**
     * 序列化整颗树
     */
    public String elementToString()
    {
        StringBuffer sbu = new StringBuffer();
        if ("".equals(tagName))
        {
            if (contents != null)
            {
                sbu.append(contents);
            }
        }
        else
        {
            sbu.append("<" + tagName + " ");
            if (attributesName != null)
            {
                for (int i = 0; i < attributesName.length; i++)
                {
                    sbu.append(attributesName[i] + "=\"" + attributesValue[i] + "\" ");
                }
            }
            if (children.size() == 0)
            {
                sbu.append("/>");
            }
            else
            {
                sbu.append(">");
                for (int i = 0; i < children.size(); i++)
                {
                    BXmlElement elt = ( BXmlElement ) children.elementAt(i);
                    sbu.append(elt.elementToString());
                }
                sbu.append("</" + tagName + ">");
            }
        }
        return sbu.toString();
    }

    /**
     * 序列化 BXmlElement
     * 
     * @return byte[]
     */
    public byte[] serialize()
    {
        String str = this.elementToString();
        return str.getBytes();
    }

    /**
     * 反序列化
     * 
     * @param byteArray
     * @return BXmlElement
     */
    public static BXmlElement deserialize(byte[] byteArray)
    {
        BXmlElement bxmle = new BXmlElement();
        String str = new String(byteArray);
        StringReader reader = new StringReader(str);
        bxmle = BXmlDriver.loadXML(reader, bxmle);
        return bxmle;
    }

    /**
     * 判断以自己为root的树,是否有此节点
     * 
     * @param Element
     * @return boolean
     */
    public boolean isMyChildren(BXmlElement Element)
    {
        int size = this.children.size();
        for (int i = 0; i < size; i++)
        {
            BXmlElement ele = ( BXmlElement ) this.children.elementAt(i);
            if (ele.equals(Element))
            {
                return true;
            }
            int childrensize = ele.children.size();
            if (childrensize > 0)
            {
                for (int k = 0; k < childrensize; k++)
                {
                    BXmlElement elex = ( BXmlElement ) ele.children.elementAt(k);
                    boolean istrue = elex.isMyChildren(Element);
                    if (istrue == true)
                    {
                        return true;
                    }
                    else
                    {
                        // 继续运行
                    }
                }
            }
        }
        return false;
    }
    
}
