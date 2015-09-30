package org.ikags.util.bxml;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class DOMMIDlet extends MIDlet
{

    public DOMMIDlet()
    {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException
    {
    }

    protected void pauseApp()
    {
    }

    protected void startApp() throws MIDletStateChangeException
    {
        System.out.println("midlet run");
        for (int i = 0; i < paths.length; i++)
        {
            printhtml(paths[i]);
        }
        System.out.println("midlet run over");
    }

// public String[] paths = { "/frame_a1.htm", "/frame_a2.htm", "/frame_a3.htm", "/frame_a4.htm", "/frame_a5.htm", "/frame.html", "/frame1.htm", "/frame2.htm", "/frame3.htm",
// "/list.html", "/test1.htm", "/test2.htm", "/test3.htm", "/test4.htm", "/test5.htm", "/test6.htm", "/test7.htm", "/ol.html" };
    public String[] paths = { "/frame_a1.htm" };

    public void printhtml(String path)
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(path);
            if (in != null)
            {
                // 使用方法
                BXmlElement rootElement = new BXmlElement();
                rootElement = BXmlDriver.loadXML(new InputStreamReader(in, "UTF8"), rootElement);
                // 序列化方法
                byte[] bytes = rootElement.serialize();
                // 反序列方法
                BXmlElement serializeXML = BXmlElement.deserialize(bytes);
                // 打印
                serializeXML.print(0);
                serializeXML.isMyChildren(serializeXML);
            }
            else
            {
                System.out.println("ERROR:InputStream==null,path=" + path);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
