package org.ikags.mreader.reader;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import org.ikags.core.Def;
import org.ikags.util.RmsTools;

public class MReader implements Runnable
{

    public MReader(String str)
    {
        if (str != null)
        {
            openUrl(str,true,0);
        }
    }
    private  Vector SavedVector = new Vector();
    private Vector HistoryVector = new Vector();
    String Url = null;
    int indexlength = 0;
    public static byte loadState = 0;
    public static final byte LOADED = 0;
    public static final byte LOADING = 1;
    public static byte NetType = 1;
    public static final byte ONLINE = 0;
    public static final byte OFFLINE = 1;
    HtmlReader htmlr = new HtmlReader();
    TxtReader txtr = new TxtReader();
    ImageReader imgr = new ImageReader();
    Thread th = null;
    boolean saveHistory=true;
    public void openUrl(String url,boolean sHistory,int indelength)
    {  
        this.indexlength=indelength;
        Url = url;
        saveHistory=sHistory;
        th = new Thread(this);
        th.start();
    }
    
    public void run()
    {
        System.out.println("new thread:"+Url);
        loadState = LOADING;
        txtr.clean();
        imgr.clean();
        htmlr.clean();
        // 读取数据
        if (Url == null)
        {
            return;
        }
        // 添加历史记录
        if (Url != null&&saveHistory)
        {
            HistoryVector.addElement(Url);
        }
     
        switch (NetType)
        {
            case ONLINE:
                // 联网处理,读取网页,网络浏览器
                break;
            case OFFLINE:
                // 本地html,TXT文件浏览器
            	loadingLocalPage(Url);
                break;
        }
        switch (FileType)
        {
            case FILE_TXT:
                txtr.indexLegth=indexlength;
                break;
            case FILE_HTML:
                htmlr.indexLegth=indexlength;
                break;
            case FILE_IMG:
                break;
        }
        loadState = LOADED;
    }

    
    RmsTools rms=new RmsTools("ika_mreader");
    public void saveMark(){
        SavedVector.removeAllElements();
        SavedVector.addElement(Url);
        int length=0;
        switch (FileType)
        {
            case FILE_TXT:
                length=txtr.indexLegth;
                break;
            case FILE_HTML:
                length=htmlr.indexLegth;
                break;
            case FILE_IMG:
                break;
        }
        SavedVector.addElement(""+length);
        rms.saveRecord(SavedVector);
    }
    
    public void loadMark(){
        Vector defvec=new Vector();
        defvec.removeAllElements();
        defvec.addElement("/index.html");
        defvec.addElement(""+0);
        SavedVector=rms.loadRecord(defvec);
        String url=(String)SavedVector.elementAt(0);
        int index=Integer.parseInt((String)SavedVector.elementAt(1));
        openUrl(url,true,index);
    }
    
    public void openLastHistory()
    {
        if (HistoryVector.size() > 1&& loadState != LOADING)
        {
           HistoryVector.removeElement(HistoryVector.lastElement());
            String url = ( String ) HistoryVector.lastElement();
            openUrl(url,false,0);
        }
    }

    
    public void frashPage()
    {
        if (HistoryVector.size() > 0&& loadState != LOADING)
        {
           String url = ( String ) HistoryVector.lastElement();
            openUrl(url,false,0);
        }
    }
    
    
    /**
     * 读取本地网页
     * 
     * @param url
     */
    public static byte FileType = 0;
    public static final byte FILE_TXT = 0;
    public static final byte FILE_HTML = 1;
    public static final byte FILE_IMG = 2;

    public void loadingLocalPage(String url)
    {
        // 确认文件格式
        FileType = checkFileType(url);
        switch (FileType)
        {
            case FILE_TXT:
                txtr.loadingTXT(url);
                break;
            case FILE_HTML:
                htmlr.loadingHTML(url);
                break;
            case FILE_IMG:
                imgr.loadingIMG(url);
                break;
        }
    }

    public byte checkFileType(String url)
    {
        // 确认文件格式
        if (url.length() > 4)
        {
            String u = url.substring(url.length() - 4, url.length()).toLowerCase();
            if (u.equals("html") || u.equals(".htm") || u.equals(".jsp") || u.equals(".asp") || u.equals(".php"))
            {
                return FILE_HTML;
            }
            if (u.equals(".png") || u.equals(".jpg") || u.equals(".bmp") || u.equals(".gif"))
            {
                return FILE_IMG;
            }
            if (u.equals(".txt"))
            {
                return FILE_TXT;
            }
        }
        return FILE_TXT;
    }

    public void paint(Graphics g)
    {
        switch (FileType)
        {
            case FILE_TXT:
                txtr.paint(g);
                break;
            case FILE_HTML:
                htmlr.paint(g);
                break;
            case FILE_IMG:
                imgr.paint(g);
                break;
        }
        if (loadState == LOADING)
        {
            g.setColor(0xffff00);
            g.fillRect(0, Def.SYSTEM_SH / 2 - Def.font.getHeight(), Def.SYSTEM_SW, Def.font.getHeight());
            g.setColor(0x000000);
            g.drawString(loadingStr[frameCount%3], Def.SYSTEM_SW / 2, Def.SYSTEM_SH / 2, Def.BOTTOM_CENTER);
            frameCount++;
        }
    }
    int frameCount=0;
    String[] loadingStr={"读取中.  ","读取中.. ","读取中..."};

    public void keyPressed(int key)
    {
        if (key == Def.SOFTKEY_RIGHT)
        {
            openLastHistory();
            return;
        }
        switch (FileType)
        {
            case FILE_TXT:
                txtr.keyPressed(key);
                break;
            case FILE_HTML:
                htmlr.keyPressed(key);
                break;
            case FILE_IMG:
                imgr.keyPressed(key);
                break;
        }
    }

    public void keyReleased(int key)
    {
        switch (FileType)
        {
            case FILE_TXT:
                txtr.keyReleased(key);
                break;
            case FILE_HTML:
                htmlr.keyReleased(key);
                break;
            case FILE_IMG:
                imgr.keyReleased(key);
                break;
        }
    }
}
