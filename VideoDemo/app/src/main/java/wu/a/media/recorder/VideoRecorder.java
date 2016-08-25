package wu.a.media.recorder;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;

import wu.videodemo.BuildConfig;
import wu.videodemo.Utils;

/**
 * Created by jx on 2016/5/13.
 */
public class VideoRecorder implements MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {
    private static final String TAG = "VideoRecorder";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static VideoRecorder mVideoRecorder;
    private MediaRecorder mMediaRecorder;
    private CamcorderProfile profile;
    private String recordFilePath;
    private boolean isRecording;
    private Camera mCamera;

    public static VideoRecorder getVideoRecorder() {
        if (mVideoRecorder == null) {
            synchronized (VideoRecorder.class) {
                if (mVideoRecorder == null) {
                    mVideoRecorder = new VideoRecorder();
                }
            }
        }
        return mVideoRecorder;
    }

    private VideoRecorder() {
        if (DEBUG) {
            Utils.d(TAG, "create VideoRecorder");
        }
    }

    private void createRecorder() {
        if (DEBUG) {
            Utils.d(TAG, "create mideaRecorder");
        }
        mMediaRecorder = new MediaRecorder();
        profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//        profile.videoFrameWidth = optimalSize.width;
//        profile.videoFrameHeight = optimalSize.height;
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnInfoListener(this);
    }

    public boolean start(Camera mCamera, String filePath) {
        if (DEBUG) {
            Utils.d(TAG, "start record with " + filePath);
        }
        if (isRecording) {
            return isRecording;
        }
        boolean isStart = false;
        if (filePath == null || filePath.length() <= 0) {
            Log.e(TAG, "[-] start error with null file");
            return isStart;
        }
        // 开始录制
        try {
            if (mMediaRecorder == null) {
                createRecorder();
            }
            recordFilePath = filePath;
            this.mCamera = mCamera;
            mMediaRecorder.reset();
            // 锁定Camera，开始录制
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);

            // 设置录取源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // 设置一个CamcorderProfile
            mMediaRecorder.setProfile(profile);
            // 设置输出文件
            mMediaRecorder.setOutputFile(recordFilePath);

            //内容也旋转90度
            mMediaRecorder.setOrientationHint(90);

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            isStart = true;
        } catch (IllegalStateException e) {
            Log.e(TAG, "[-] preparing MediaRecorder: " + e.getMessage());
            stop();
        } catch (Exception e) {
            Log.e(TAG, "[-] preparing MediaRecorder: " + e.getMessage());
            stop();
        }
        return isStart;
    }

    public void stop() {
        if (DEBUG) {
            Utils.d(TAG, "stop record ");
        }
        if (mMediaRecorder != null && isRecording) {
            isRecording = false;
            try {
                mMediaRecorder.stop();
                if (DEBUG) {
                    Log.e(TAG, "stop with " + recordFilePath);
                }
            } catch (Exception e) {
                Log.e(TAG, "[-] stop MediaRecorder: " + e.getMessage());
            }
            if (mCamera != null) {
                // 锁定
                mCamera.lock();
            }
        }
    }

//    public void reStart() {
//        try {
//            mMediaRecorder.stop();
//            mMediaRecorder.prepare();
//            mMediaRecorder.start();
//        } catch (Exception e) {
//            Log.e(TAG, "[-] reStart MediaRecorder: " + e.getMessage());
//        }
//    }

//    public void pause() {
//        mMediaRecorder.pause();
//    }
//
//    public void resume() {
//        mMediaRecorder.start();
//    }

    public boolean isRecording() {
        return isRecording;
    }

    public void destroy() {
        if (DEBUG) {
            Utils.d(TAG, "destroy record ");
        }
        stop();
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        isRecording = false;
        if (DEBUG) {
            Utils.d(TAG, "onError record ");
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (DEBUG) {
            Utils.d(TAG, "onInfo record ");
        }
    }
}
