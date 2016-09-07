package abc.soundplayer;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;


/**
 * SoundPool：
 * 此类特点就是低延迟播放，适合播放实时音实现同时播放多个声音，如游戏中炸弹的爆炸音等小资源文件，此类音频比较适合放到资源文件夹 res/raw下和程序一起打成APK文件。
 * 参数：1、Map中取值   2、当前音量     3、最大音量  4、优先级   5、重播次数   6、播放速度
 */
public class SoundPoolActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_load;
    private Button btn_play2;
    private Button btn_play3;
    private Button btn_play4;
    private Button btn_play5;
    private Button btn_release;
    private AssetManager aManager;
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundpool);
        aManager = getAssets();

        bindViews();
    }

    private void bindViews() {
        btn_load = (Button) findViewById(R.id.btn_load);
        btn_play2 = (Button) findViewById(R.id.btn_play2);
        btn_play3 = (Button) findViewById(R.id.btn_play3);
        btn_play4 = (Button) findViewById(R.id.btn_play4);
        btn_play5 = (Button) findViewById(R.id.btn_play5);
        btn_release = (Button) findViewById(R.id.btn_release);

        btn_load.setOnClickListener(this);
        btn_play2.setOnClickListener(this);
        btn_play3.setOnClickListener(this);
        btn_play4.setOnClickListener(this);
        btn_play5.setOnClickListener(this);
        btn_release.setOnClickListener(this);

    }

    private void initSP() throws Exception {
        //设置最多可容纳5个音频流，音频的品质为5
        SoundPool.Builder soundBuild = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            soundBuild = new SoundPool.Builder();
            soundBuild.setMaxStreams(4);
            mSoundPool = soundBuild.build();
        } else {
            mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 5);
        }

        soundID.put(1, mSoundPool.load(this, R.raw.duang, 1));
        soundID.put(2, mSoundPool.load(getAssets().openFd("biaobiao.mp3"), 1));  //需要捕获IO异常
        soundID.put(3, mSoundPool.load(this, R.raw.duang, 1));
        soundID.put(4, mSoundPool.load(this, R.raw.duang, 1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                try {
                    initSP();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_play2:
                mSoundPool.play(soundID.get(1), 0.2f, 0.2f, 0, 0, 1);
                break;
            case R.id.btn_play3:
                mSoundPool.play(soundID.get(2), 0.1f, 0.1f, 0, 0, 1);
                break;
            case R.id.btn_play4:
                mSoundPool.play(soundID.get(3), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play5:
                mSoundPool.play(soundID.get(4), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_release:
                mSoundPool.release();   //回收SoundPool资源
                break;
        }
    }
}
