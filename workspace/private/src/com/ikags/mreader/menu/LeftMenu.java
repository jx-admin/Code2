package com.ikags.mreader.menu;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;
import org.ikags.core.SystemCanvas;
import org.ikags.mreader.Define;
import org.ikags.mreader.SMenu;
import org.ikags.mreader.SReader;
import org.ikags.core.*;

/**
 * 菜单 支持三级菜单
 * 
 * @version 0.0.2
 * @author airzhangfish
 * 
 */
public class LeftMenu extends StateX
{

    Vector rootvec = null;

    public LeftMenu()
    {
        // 初始化，判断宽度
        Menuinit();
        resetsize(rootvec);
        // 获得初级菜单的宽度=w=
        MenuCompoment menucmp = ( MenuCompoment ) rootvec.elementAt(0);
        rootvecWdith = menucmp.Width;
    }

    public void init()
    {
    }

    private int rootvecWdith = 0;

    public void Logic()
    {
    }

    public void keyPressed(int key)
    {
        if (key == Def.KEY_2 || key == Def.KEY_UP)
        {
            switch (Vecdepth)
            {
                case 0:
                    select_index--;
                    if (select_index < 0)
                    {
                        select_index = 0;
                    }
                    break;
                case 1:
                    select2_index--;
                    if (select2_index < 0)
                    {
                        select2_index = 0;
                    }
                    break;
                case 2:
                    select3_index--;
                    if (select3_index < 0)
                    {
                        select3_index = 0;
                    }
                    break;
            }
        }
        if (key == Def.KEY_8 || key == Def.KEY_DOWN)
        {
            switch (Vecdepth)
            {
                case 0:
                    select_index++;
                    if (select_index > rootvec.size() - 1)
                    {
                        select_index = rootvec.size() - 1;
                    }
                    break;
                case 1:
                    MenuCompoment menucmp = ( MenuCompoment ) rootvec.elementAt(select_index);
                    select2_index++;
                    if (select2_index > menucmp.vec.size() - 1)
                    {
                        select2_index = menucmp.vec.size() - 1;
                    }
                    break;
                case 2:
                    MenuCompoment menucmp1 = ( MenuCompoment ) rootvec.elementAt(select_index);
                    MenuCompoment menucmp2 = ( MenuCompoment ) menucmp1.vec.elementAt(select2_index);
                    select3_index++;
                    if (select3_index > menucmp2.vec.size() - 1)
                    {
                        select3_index = menucmp2.vec.size() - 1;
                    }
                    break;
            }
        }
        if (key == Def.KEY_4 || key == Def.KEY_LEFT)
        {
            if (Vecdepth == 1)
            {
                Vecdepth = 0;
                select2_index = 0;
            }
            if (Vecdepth == 2)
            {
                Vecdepth = 1;
                select3_index = 0;
            }
        }
        if (key == Def.KEY_6 || key == Def.KEY_RIGHT)
        {
            MenuCompoment menucmp = ( MenuCompoment ) rootvec.elementAt(select_index);
            switch (Vecdepth)
            {// root目录
                case 0:
                    if (menucmp.vec == null)
                    {
                        // 执行命令
// MenuCommandAction(menucmp.label);
                    }
                    else
                    {
                        // 进入下层菜单
                        Vecdepth = 1;
                    }
                    break;
                case 1:
                    // son1目录
                    MenuCompoment menucmp2 = ( MenuCompoment ) menucmp.vec.elementAt(select2_index);
                    if (menucmp2.vec == null)
                    {
                        // 执行命令
// MenuCommandAction(menucmp2.label);
                    }
                    else
                    {
                        // 进入下层菜单
                        Vecdepth = 2;
                    }
                    break;
                case 2:
                    // son2目录
                    MenuCompoment menucmp2s = ( MenuCompoment ) menucmp.vec.elementAt(select2_index);
                    MenuCompoment menucmp3 = ( MenuCompoment ) menucmp2s.vec.elementAt(select3_index);
                    if (menucmp3.vec == null)
                    {
                        // 执行命令
// MenuCommandAction(menucmp3.label);
                    }
                    else
                    {
                        // 进入下层菜单(暂无四级菜单)
                    }
                    break;
            }
        }
        if (key == Def.KEY_5 || key == Def.KEY_MIDDLE || key == Def.SOFTKEY_LEFT)
        {
            MenuCompoment menucmp = ( MenuCompoment ) rootvec.elementAt(select_index);
            switch (Vecdepth)
            {// root目录
                case 0:
                    if (menucmp.vec == null)
                    {
                        // 执行命令
                        MenuCommandAction(menucmp.label);
                    }
                    else
                    {
                        // 进入下层菜单
                        Vecdepth = 1;
                    }
                    break;
                case 1:
                    // son1目录
                    MenuCompoment menucmp2 = ( MenuCompoment ) menucmp.vec.elementAt(select2_index);
                    if (menucmp2.vec == null)
                    {
                        // 执行命令
                        MenuCommandAction(menucmp2.label);
                    }
                    else
                    {
                        // 进入下层菜单
                        Vecdepth = 2;
                    }
                    break;
                case 2:
                    // son2目录
                    MenuCompoment menucmp2s = ( MenuCompoment ) menucmp.vec.elementAt(select2_index);
                    MenuCompoment menucmp3 = ( MenuCompoment ) menucmp2s.vec.elementAt(select3_index);
                    if (menucmp3.vec == null)
                    {
                        // 执行命令
                        MenuCommandAction(menucmp3.label);
                    }
                    else
                    {
                        // 进入下层菜单(暂无四级菜单)
                    }
                    break;
            }
        }
        if (key == Def.SOFTKEY_RIGHT)
        {
            if (Vecdepth == 0)
            {
                returnState();
            }
            if (Vecdepth == 1)
            {
                Vecdepth = 0;
                select2_index = 0;
            }
            if (Vecdepth == 2)
            {
                Vecdepth = 1;
                select2_index = 0;
            }
        }
    }

    public void keyReleased(int key)
    {
    }
    public byte Vecdepth = 0;
    public int select_index = 0;
    public int select2_index = 0;
    public int select3_index = 0;
    // 左下起点的坐标
    public int start_x = 5;
    public int start_y = Def.SYSTEM_SH - 5;
    public int color_bg = 0xffffff;
    public int color_box = 0x000000;
    public int color_font = 0x000000;
    public int color_selected = 0x8888ff;
    // 向上偏移位置
    public int posion_off0 = 0;
    public int posion_off1 = 0;
    public int posion_off2 = 0;
Font font=Def.font;
    public void paint(Graphics g)
    {
        // 一级菜单
        g.setFont(font);
        g.setColor(color_bg);
        int rootvecHeight = rootvec.size() * (Def.fontHeight + 2);
        g.fillRect(start_x, start_y - rootvecHeight - 3, rootvecWdith + 5, rootvecHeight + 5);
        g.setColor(color_box);
        g.drawRect(start_x, start_y - rootvecHeight - 3, rootvecWdith + 5, rootvecHeight + 5);
        for (int i = 0; i < rootvec.size(); i++)
        {
            MenuCompoment menucmp = ( MenuCompoment ) rootvec.elementAt(i);
            g.setColor(color_selected);
            if (select_index == i)
            {
                g.fillRect(start_x + 2, start_y - rootvecHeight + i * (Def.fontHeight + 2), rootvecWdith, Def.fontHeight + 1);
            }
            g.setColor(color_font);
            if (menucmp.vec != null)
            {
                g.drawString(">", start_x + rootvecWdith, start_y - rootvecHeight + i * (Def.fontHeight + 2), 0);
            }
            g.drawString(menucmp.text, start_x + 3, start_y - rootvecHeight + i * (Def.fontHeight + 2), 0);
            // 二级菜单
            if (Vecdepth > 0)
            {
                MenuCompoment menucmp1 = ( MenuCompoment ) rootvec.elementAt(select_index);
                MenuCompoment menucmp2 = ( MenuCompoment ) menucmp1.vec.elementAt(0);
                g.setColor(color_bg);
                int vecHeight = menucmp1.vec.size() * (Def.fontHeight + 2);
                int pos_y = start_y - rootvecHeight + (select_index + posion_off1) * (Def.fontHeight + 2);
                if (pos_y + menucmp1.vec.size() * (Def.fontHeight + 2) > start_y)
                {
                    posion_off1 = (start_y - pos_y - menucmp1.vec.size() * (Def.fontHeight + 2)) / (Def.fontHeight + 2);
                    continue;
                }
                g.fillRect(start_x + 5 + menucmp1.Width, pos_y - 3, menucmp2.Width + 5, vecHeight + 5);
                g.setColor(color_box);
                g.drawRect(start_x + 5 + menucmp1.Width, pos_y - 3, menucmp2.Width + 5, vecHeight + 5);
                for (int j = 0; j < menucmp1.vec.size(); j++)
                {
                    MenuCompoment menucmpx = ( MenuCompoment ) menucmp1.vec.elementAt(j);
                    g.setColor(color_selected);
                    if (select2_index == j)
                    {
                        g.fillRect(start_x + 5 + menucmp1.Width + 2, pos_y + j * (Def.fontHeight + 2), menucmpx.Width, Def.fontHeight + 1);
                    }
                    g.setColor(color_font);
                    if (menucmpx.vec != null)
                    {
                        g.drawString(">", start_x + 5 + menucmp1.Width + menucmpx.Width - Def.font.stringWidth(">"), pos_y + j * (Def.fontHeight + 2), 0);
                    }
                    g.drawString(menucmpx.text, start_x + 5 + menucmp1.Width + 2, pos_y + j * (Def.fontHeight + 2), 0);
                }
                // 三级菜单
                if (Vecdepth > 1)
                {
                    MenuCompoment menucmp2s = ( MenuCompoment ) menucmp1.vec.elementAt(select2_index);
                    MenuCompoment menucmp3 = ( MenuCompoment ) menucmp2s.vec.elementAt(0);
                    g.setColor(color_bg);
                    int vec2Height = menucmp2s.vec.size() * (Def.fontHeight + 2);
                    int pos2_y = start_y - rootvecHeight + (select_index + posion_off1) * (Def.fontHeight + 2) + (select2_index + posion_off2) * (Def.fontHeight + 2);
                    if (pos2_y + menucmp2s.vec.size() * (Def.fontHeight + 2) > start_y)
                    {
                        posion_off2 = (start_y - pos2_y - menucmp2s.vec.size() * (Def.fontHeight + 2)) / (Def.fontHeight + 2);
                        continue;
                    }
                    g.fillRect(start_x + 5 + menucmp1.Width + 5 + menucmp2s.Width, pos2_y - 3, menucmp3.Width + 5, vec2Height + 5);
                    g.setColor(color_box);
                    g.drawRect(start_x + 5 + menucmp1.Width + 5 + menucmp2s.Width, pos2_y - 3, menucmp3.Width + 5, vec2Height + 5);
                    for (int j = 0; j < menucmp2s.vec.size(); j++)
                    {
                        MenuCompoment menucmpx = ( MenuCompoment ) menucmp2s.vec.elementAt(j);
                        g.setColor(color_selected);
                        if (select3_index == j)
                        {
                            g.fillRect(start_x + 5 + menucmp1.Width + 5 + menucmp2s.Width + 2, pos2_y + j * (Def.fontHeight + 2), menucmpx.Width, Def.fontHeight + 1);
                        }
                        g.setColor(color_font);
                        if (menucmpx.vec != null)
                        {
                            g.drawString(">", start_x + 5 + menucmp1.Width + 5 + menucmp2s.Width + menucmpx.Width - Def.font.stringWidth(">"), pos2_y + j * (Def.fontHeight + 2), 0);
                        }
                        g.drawString(menucmpx.text, start_x + 5 + menucmp1.Width + 5 + menucmp2s.Width + 2, pos2_y + j * (Def.fontHeight + 2), 0);
                    }
                }
            }
        }
    }

    public void resetsize(Vector vec)
    {
        // 获取最大值
        int MaxWidth = 0;
        for (int i = 0; i < vec.size(); i++)
        {
            MenuCompoment menucmp = ( MenuCompoment ) vec.elementAt(i);
            MaxWidth = Math.max(MaxWidth, menucmp.Width);
            if (menucmp.vec != null)
            {
                resetsize(menucmp.vec);
            }
        }
        // 设置最大值
        for (int i = 0; i < vec.size(); i++)
        {
            MenuCompoment menucmp = ( MenuCompoment ) vec.elementAt(i);
            menucmp.Width = MaxWidth;
        }
    }

    /**
     * 载入菜单
     */
    public void Menuinit()
    {
        rootvec = new Vector();
      rootvec.addElement(new MenuCompoment("menu1读取本地文件", "读取本地文件"));
        rootvec.addElement(new MenuCompoment("menu1目录", " 目录"));
        rootvec.addElement(new MenuCompoment("menu1读取书签", "读取书签"));
        rootvec.addElement(new MenuCompoment("menu1保存书签", "保存书签"));
        rootvec.addElement(new MenuCompoment("menu1设置", " 设置"));
        rootvec.addElement(new MenuCompoment("menu1帮助", " 帮助"));
        rootvec.addElement(new MenuCompoment("menu1关于", " 关于"));
        rootvec.addElement(new MenuCompoment("menu1退出", " 退出"));
    }

    public void MenuCommandAction(String label)
    {
        System.out.println("MenuCommandAction:" + label);
        if (label == "menu1读取本地文件")
        {
//            SReader.fb.startApp();
        }
        if (label == "menu1目录")
        {
            SReader.reader.openUrl("/index.html",true,0);
            returnState();
        }
        if (label == "menu1设置")
        {
        SMenu.menuState=SMenu.SETTING;
        }
        if (label == "menu1读取书签")
        {
            SReader.reader.loadMark();
            returnState();
        }
        if (label == "menu1保存书签")
        {
            SReader.reader.saveMark();
            returnState();
        }
        if (label == "menu1帮助")
        {
            SMenu.menuState=SMenu.HELP;
        }
        if (label == "menu1关于")
        {
            SMenu.menuState=SMenu.ABOUT;
        }
        if (label == "menu1退出")
        {
            SystemCanvas.exit();
        }
    }

    public void returnState()
    {
// MyDef.BrowerState = MyDef.STATE_BROWER;//返回原来的状态
        Define.mainState = Define.STATE_READING;
    }
}
