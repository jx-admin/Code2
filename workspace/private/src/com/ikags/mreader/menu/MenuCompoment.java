package com.ikags.mreader.menu;

import java.util.Vector;

public class MenuCompoment
{

    public Vector vec = null;
    public String label = "未设置label";
    public String text = "未设置text";
    public int Height = 0;
    public int Width = 0;

    /**
     * 菜单元素
     * 
     * @param setlabel label设置
     * @param str Text设置
     */
    public MenuCompoment(String setlabel, String str)
    {
        if (setlabel != null)
        {
            label = setlabel;
        }
        if (str != null)
        {
            text = str;
            Width = org.ikags.core.Def.font.stringWidth(text);
        }
    }
}
