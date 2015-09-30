package org.ikags.mreader.reader;

import java.util.Vector;
import javax.microedition.lcdui.Image;

public class HTMLItem
{

    public static final byte TYPE_TEXT = 0;
    public static final byte TYPE_IMAGE = 1;
    public static final byte TYPE_BR = 2;
    public static final  int defualtFontColor=0x000000;
    public static final  int SelectableFontColor=0x0000ff;
    
    
    public static final byte POS_LEFT = 0;
    public static final byte POS_CENTER = 1;    
    public static final byte POS_RIGHT = 2;

    public byte pos=0;
    public int x = 0;
    public int y = 0;
    public int width = 1;
    public int height = 1;
    public int FontColor=defualtFontColor;
    public boolean isSelectable = false;
    public boolean isSelected = false;
    public String Href = null;
    public byte Type = 0;
    public String str = null;
    public Image img = null;
    public Vector vec;

    public HTMLItem()
    {
        vec = new Vector();
    }
}
