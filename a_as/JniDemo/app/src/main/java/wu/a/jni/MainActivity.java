package wu.a.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("MyJni");//导入生成的链接库文件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("dddd",JniBase.getName()+"--"+JniBase.getAge("wjx"));
        Log.e("dddd",JniDynamic.stringFromJNI()+" dd ");
        JniDynamic.setData(new byte[]{1,2,3},3);
    }
}
