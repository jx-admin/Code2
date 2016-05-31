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

/**
 * Created by jx on 2016/3/30.
 */
public class CustomVideoCamera extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "CAMERA_TUTORIAL";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean isPreview;
    private Button io_btn, play_btn;

    private int screenWidth = 640;
    private int screenHeight = 480;

    private CMediaRecoder mCMediaRecoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_surface);
        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        io_btn = (Button) findViewById(R.id.io_btn);
        io_btn.setOnClickListener(this);
        findViewById(R.id.play_btn).setOnClickListener(this);
        mCMediaRecoder = new CMediaRecoder();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        mCMediaRecoder.create(surfaceHolder, camera);
        camera.setDisplayOrientation(90);
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        isPreview = false;
        camera.release();
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
        if (id == R.id.play_btn) {
//            play(getSdcardForWrite().getPath() + File.separator + cacheFileName, (SurfaceView) findViewById(R.id.surface_play));
        } else if (id == R.id.resume_btn) {
            mCMediaRecoder.resume();
        } else if (id == R.id.pause_btn) {
            mCMediaRecoder.pause();
        } else {
            if (mCMediaRecoder.isRecoding()) {
                mCMediaRecoder.stop();
            } else {
                mCMediaRecoder.start();
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

    private void initCamera(SurfaceHolder holder) {
        if (!isPreview) {
            camera = Camera.open();
        }
        if (camera != null && !isPreview) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(screenWidth, screenHeight);    // 设置预览照片的大小
                parameters.setPreviewFpsRange(20, 30);                    // 每秒显示20~30帧
                parameters.setPictureFormat(ImageFormat.NV21);           // 设置图片格式
                parameters.setPictureSize(screenWidth, screenHeight);    // 设置照片的大小
                //camera.setParameters(parameters);                      // android2.3.3以后不需要此行代码
                camera.setPreviewDisplay(surfaceHolder);                 // 通过SurfaceView显示取景画面
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
//                    Log.d("dddd","onPrebac "+data.length);
                    }
                });         // 设置回调的类
                camera.setPreviewDisplay(holder);
                camera.startPreview();                                   // 开始预览
                camera.autoFocus(null);
                isPreview = true;                                 // 自动对焦
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
