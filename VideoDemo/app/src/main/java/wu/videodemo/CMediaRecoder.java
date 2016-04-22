package wu.videodemo;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

/**
 * Created by jx on 2016/4/22.
 */
public class CMediaRecoder {
    private final static String TAG="dddd";
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    private MediaRecorder mediaRecorder;
    private final int maxDurationInMs = 20 * 1000;
    private final long maxFileSizeInBytes = 1024 * 1024;
    private final int videoFramesPerSecond = 20;
    private int BitRate = 5 * 1024 * 1024;
    private int vWidth = 640;
    private int vHeight = 480;
    private File tempFile;
    private String cacheFileName = "abc.mp4";
    private boolean isStart;

    public CMediaRecoder() {

    }

    public boolean isRecoding() {
        return mediaRecorder != null && isStart;
    }

    public void create(SurfaceHolder surfaceHolder, Camera camera) {
        this.surfaceHolder = surfaceHolder;
        this.camera = camera;

        mediaRecorder = new MediaRecorder();
    }

    public void destroy() {
        stop();
        mediaRecorder = null;
    }

    public void start() {
        if (isRecoding()) {
            return;
        }
        try {
            camera.unlock();
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 设置录制视频源为Camera(相机)

// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//            mediaRecorder.setMaxDuration(maxDurationInMs);

            tempFile = MediaUtils.createMediaFile(cacheFileName);
            Log.d("dddd", tempFile.getPath());
            mediaRecorder.setOutputFile(tempFile.getAbsolutePath());

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            // 设置录制的视频编码h263 h264
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);


            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoSize(vWidth, vHeight);

// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
            //设置编码比特率,不设置会使视频图像模糊
            mediaRecorder.setVideoEncodingBitRate(BitRate);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

//            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);

            mediaRecorder.prepare();

            mediaRecorder.start();

            isStart = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (isRecoding()) {
            isStart = false;
            mediaRecorder.stop();
            mediaRecorder.release();
            camera.lock();
        }
    }

    public void pause() {
        mediaRecorder.stop();
    }

    public void resume() {
        mediaRecorder.start();
    }
}
