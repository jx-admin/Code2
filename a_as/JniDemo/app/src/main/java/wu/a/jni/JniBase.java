package wu.a.jni;

/**
 * Created by junxuwang on 16/11/17.
 * http://www.th7.cn/Program/Android/201509/550864.shtml
 * 静态注册
 */
public class JniBase {
    public static native String getName();
    public static native int getAge(String name);
}
