package wu.a.jni;

/**
 * Created by junxuwang on 16/11/19.
 * http://blog.csdn.net/chenfeng0104/article/details/7088600
 * 动态JNI注册接口，JniDynamic.c
 */

public class JniDynamic {
    public static native String stringFromJNI();
    public static native void setData(byte str[],int type);
}
