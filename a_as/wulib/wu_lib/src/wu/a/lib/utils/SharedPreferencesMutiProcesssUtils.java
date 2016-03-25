package wu.a.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by jx on 2015/12/25.
 */
public class SharedPreferencesMutiProcesssUtils {
    private static final String SP_FILE_NAME_DEFAULT = "sp_muti_process";
    private SharedPreferences sp;

    private SharedPreferencesMutiProcesssUtils(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * @param context
     * @param name    文件名
     * @return
     * @see SharedPreferencesMutiProcesssUtils#getSharedPreferencesUtils(Context, String, String)
     */
    public static SharedPreferencesMutiProcesssUtils getSharedPreferencesUtils(Context context, String name) {
        return new SharedPreferencesMutiProcesssUtils(context, name);
    }

    /**
     * @param context
     * @param packageName 文件所在包名
     * @param name        文件名
     * @return SharedPreferences
     * @throws PackageManager.NameNotFoundException
     */
    public static SharedPreferencesMutiProcesssUtils getSharedPreferencesUtils(Context context, String packageName, String name) throws PackageManager.NameNotFoundException {
        Context targetContext = getTargetContext(context, packageName);
        return new SharedPreferencesMutiProcesssUtils(targetContext, name);
    }

    public <T extends Serializable> boolean put(String key, T value) {
        if (key != null && key.length() > 0) {
            return put(sp, key, value);
        }
        return false;
    }

    public <T extends Serializable> T get(String key, T defValue) {
        if (key != null && key.length() > 0) {
            return get(sp, key, defValue);

        }
        return defValue;
    }

    public static <T extends Serializable> boolean put(Context context, String key, T value) {
        if (key != null && key.length() > 0) {
            SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME_DEFAULT, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            return put(sp, key, value);
        }
        return false;
    }

    public static <T extends Serializable> T get(Context context, String key, T defValue) {
        if (key != null && key.length() > 0) {
            SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME_DEFAULT, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            return get(sp, key, defValue);
        }
        return defValue;
    }

    public static <T extends Serializable> boolean put(SharedPreferences sp, String key, T value) {
        if (key != null && key.length() > 0) {
            SharedPreferences.Editor edit = sp.edit();
            if (value == null) {
                edit.remove(key);
                edit.commit();
                return true;
            } else {
                byte[] byte64 = ObjectToByte(value);
                if (byte64 != null) {
                    String str64 = new String(Base64.encode(byte64, Base64.DEFAULT));
                    edit.putString(key, str64);
                    edit.commit();
                    return true;
                }
            }
        }
        return false;
    }

    public static <T extends Serializable> T get(SharedPreferences sp, String key, T defValue) {
        if (key != null && key.length() > 0) {
            String str64 = sp.getString(key, null);
            if (str64 != null && str64.length() > 0) {
                byte[] bytes = Base64.decode(str64, Base64.DEFAULT);
                return (T) ByteToObject(bytes);
            }
        }
        return defValue;
    }

    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 根据目标程序的包名来获取其程序的上下文
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static Context getTargetContext(Context context, String packageNmae) throws PackageManager.NameNotFoundException {
        return context.createPackageContext(packageNmae, Context.CONTEXT_IGNORE_SECURITY);
    }
}
