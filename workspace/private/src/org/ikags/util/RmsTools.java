package org.ikags.util;

import javax.microedition.rms.RecordStore;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Vector;

/**
 * I.K.A Engine<BR>
 * 储存游戏的相关数据,
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.1.19
 * @version 0.4
 */
public class RmsTools
{

    private static String savename = "IKA_Engine_Rms";

    public RmsTools(String savename)
    {
        RmsTools.savename = savename;
        System.out.println("初始化RMS:" + savename);
    }

    /**
     * 读取储存的数据,储存于Vector,<BR>
     * 原理为将将读出的数据转化为String格式存于Vector中.
     * 
     * @param defvec 默认第一次储存时候所占空间大小
     * @return RMS取出的String数据组成的Vector
     */
    public Vector loadRecord(Vector defvec)
    {
        Vector vec = new Vector();
        System.out.println("loadRecord rms");
        try
        {
            RecordStore rs;
            rs = RecordStore.openRecordStore(savename, true);
            int nr = rs.getNumRecords();
            // 如果数据为0写入数据
            if (nr <= 0)
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                // 数据内容（写入）
                dos.writeUTF("" + defvec.size());
                for (int i = 0; i < defvec.size(); i++)
                {
                    dos.writeUTF(( String ) defvec.elementAt(i));
                }
                dos.flush();
                byte[] data = bos.toByteArray();
                rs.addRecord(data, 0, data.length);
                dos.close();
                rs.closeRecordStore();
                return defvec;
            }
            // 读取数据
            byte[] data = rs.getRecord(1);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bis);
            // 数据内容（读取）
            int total = Integer.parseInt(dis.readUTF());
            String str = "";
            for (int i = 0; i < total; i++)
            {
                str = dis.readUTF();
                vec.addElement(str);
            }
            dis.close();
            rs.closeRecordStore();
        }
        catch (Exception ex2)
        {
            System.out.println("load rms error");
            ex2.printStackTrace();
            return null;
        }
        return vec;
    }

    /**
     * 储存数据,来自Vector<BR>
     * 原理为将Vector中的数据全部转化为String格式保存于RMS
     * 
     * @param vec
     */
    public void saveRecord(Vector vec)
    {
        if (vec == null)
        {
            System.out.println("saveRecord:vec is null");
            return;
        }
        System.out.println("saveRecord rms");
        RecordStore rs;
        try
        {
            rs = RecordStore.openRecordStore(savename, true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            // 数据内容（写入）
            int total = vec.size();
            dos.writeUTF("" + total);
            for (int i = 0; i < total; i++)
            {
                dos.writeUTF(( String ) vec.elementAt(i));
            }
            dos.flush();
            byte[] data = bos.toByteArray();
            rs.setRecord(1, data, 0, data.length);
            dos.close();
            rs.closeRecordStore();
        }
        catch (Exception ex2)
        {
            System.out.println("save rms error");
            ex2.printStackTrace();
        }
    }
}
