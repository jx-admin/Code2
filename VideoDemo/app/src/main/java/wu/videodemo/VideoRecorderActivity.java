package wu.videodemo;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import wu.a.media.recorder.CameraRecorder;

/**
 * Created by jx on 2016/3/30.
 */
public class VideoRecorderActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "CAMERA_TUTORIAL";

    private SurfaceView surfaceView;
    private Button io_btn, play_btn;

    private CameraRecorder mCMediaRecoder;
    private String path = "/storage/emulated/0/Pictures/FunnyVideo/recorder_demo.mp4";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_surface);
        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        io_btn = (Button) findViewById(R.id.record_btn);
        io_btn.setOnClickListener(this);
        findViewById(R.id.switch_btn).setOnClickListener(this);
        findViewById(R.id.play_btn).setOnClickListener(this);
        mCMediaRecoder = new CameraRecorder();

        mCMediaRecoder.create(surfaceView);
    }

    private MediaPlayer mPlayer;

    public void play(String fileName, SurfaceView view) {
        Log.d("dddd", "play " + fileName);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setDisplay(view.getHolder()); // 定义一个SurfaceView播放它
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                stop();
// canvas.drawColor(Color.TRANSPARENT,
// PorterDuff.Mode.CLEAR);
            }
        });
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        mPlayer.start();
    }


    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.switch_btn) {
            mCMediaRecoder.changeCamera();
        } else if (id == R.id.play_btn) {
//            play(getSdcardForWrite().getPath() + File.separator + cacheFileName, (SurfaceView) findViewById(R.id.surface_play));
        } else if (id == R.id.resume_btn) {
            mCMediaRecoder.startRecorder(path);
        } else if (id == R.id.pause_btn) {
            mCMediaRecoder.stopRecorder();
        } else {
            if (mCMediaRecoder.isRecording()) {
                mCMediaRecoder.stopRecorder();
            } else {
                mCMediaRecoder.startRecorder(path);
            }
        }
    }

    public static File getSdcardForWrite() {
        String mExternalStorageState = Environment.getExternalStorageState();
        // 拥有可读写权限
        if (Environment.MEDIA_MOUNTED.equals(mExternalStorageState)) {
            // 获取扩展存储设备的文件目录
            return android.os.Environment.getExternalStorageDirectory();
            // 打开文件
            // Stirng sdcardPath=SDFile.getAbsolutePath();
        } else if (mExternalStorageState.endsWith(Environment.MEDIA_MOUNTED_READ_ONLY)) {// 拥有只读权限

        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCMediaRecoder.destroy();
    }
}
