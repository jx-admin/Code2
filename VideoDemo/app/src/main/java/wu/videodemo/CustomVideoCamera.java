package wu.videodemo;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
    private boolean previewRunning;
    private Button io_btn, play_btn;

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
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
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
        if (previewRunning) {
            camera.stopPreview();
        }
        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(width, height);
        p.setPreviewFormat(PixelFormat.JPEG);
//        camera.setParameters(p);

        try {
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
//                    Log.d("dddd","onPrebac "+data.length);
                }
            });
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        previewRunning = false;
        camera.release();
    }

    private MediaRecorder mediaRecorder;
    private final int maxDurationInMs = 20000;
    private final long maxFileSizeInBytes = 500000;
    private final int videoFramesPerSecond = 20;
    private int BitRate = 5 * 1024 * 1024;
    private File tempFile;
    private String cacheFileName = "abc.mp4";
    private boolean isStart;

    public boolean startRecording() {
        try {
            camera.unlock();

            mediaRecorder = new MediaRecorder();

            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 设置录制视频源为Camera(相机)

// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//            mediaRecorder.setMaxDuration(maxDurationInMs);

            tempFile = new File(getSdcardForWrite(), cacheFileName);
            Log.d("dddd", tempFile.getPath());
            mediaRecorder.setOutputFile(tempFile.getAbsolutePath());

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            // 设置录制的视频编码h263 h264
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);


            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoSize(640, 480);

// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
            //设置编码比特率,不设置会使视频图像模糊
            mediaRecorder.setVideoEncodingBitRate(BitRate);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);

            mediaRecorder.prepare();

            mediaRecorder.start();

            isStart = true;
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {
        isStart = false;
        mediaRecorder.stop();
        mediaRecorder.release();
        camera.lock();
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
        if (v.getId() == R.id.play_btn) {
            play(getSdcardForWrite().getPath() + File.separator + cacheFileName, (SurfaceView) findViewById(R.id.surface_play));
        } else {
            if (isStart) {
                stopRecording();
            } else {
                startRecording();
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
}
