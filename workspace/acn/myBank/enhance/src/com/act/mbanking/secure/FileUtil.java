
package com.act.mbanking.secure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.act.mbanking.utils.LogManager;

import android.content.Context;
import android.util.Log;

public class FileUtil {

    public static final int AES_EncryptType = 0;

    Context context;

    public FileUtil(Context context) {

        this.context = context;
    }

    /**
     * @param fileName 保存的文件名
     * @param o 序列化的对象
     * @param encryptType 选择对象的加密类型
     * @throws Exception
     */
    public void save(String fileName, Object o, int encryptType, String key) throws Exception {

        String path = context.getFilesDir() + "/";

        File dir = new File(path);
        dir.mkdirs();

        File f = new File(dir, fileName);

        if (f.exists()) {
            f.delete();
        }

        if (encryptType < 0) {

        } else if (encryptType == AES_EncryptType) {
            ObjectEncryptAble aesEncryptImple = new AESEncryptImple(key);

            o = aesEncryptImple.encrypt((Serializable)o);
        }
        FileOutputStream os = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
        os.close();
    }

    public Object readObject(String fileName, int encryptType, String key) {
        try {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            File file = new File(dir, fileName);
            InputStream is = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(is);

            Object o = objectInputStream.readObject();
            objectInputStream.close();
            if (encryptType < 0) {

            } else if (encryptType == AES_EncryptType) {
                ObjectEncryptAble aesEncryptImple = new AESEncryptImple(key);
                o = aesEncryptImple.decrypt((Serializable)o);
            }
            return o;
        } catch (Exception e) {
            Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa", e.getLocalizedMessage());
        }
        return null;
    }
}
