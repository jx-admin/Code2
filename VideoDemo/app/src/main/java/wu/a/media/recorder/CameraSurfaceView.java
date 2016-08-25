package wu.a.media.recorder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import wu.videodemo.Utils;

/**
 * Created by jx on 2016/5/14.
 */
public class CameraSurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = CameraSurfaceView.class.getSimpleName();
    private final static boolean DEBUG = true;
    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private static CameraSurfaceView mCameraRecorder;

    public static CameraSurfaceView getCameraRecorder() {
        if (mCameraRecorder == null) {
            synchronized (CameraSurfaceView.class) {
                if (mCameraRecorder == null) {
                    mCameraRecorder = new CameraSurfaceView();
                }
            }
        }
        return mCameraRecorder;
    }

    private CameraSurfaceView() {
        if (DEBUG) {
            Utils.d("CameraSurfaceView");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (DEBUG) {
            Utils.d("surfaceCreated");
        }
        updateCamera(getCamera(mCameraId), holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (DEBUG) {
            Utils.d("surfaceChanged");
        }
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (DEBUG) {
            Utils.d("surfaceDestroyed");
        }
        destroy();
    }

    public void start(SurfaceView surfaceView) {
        if (DEBUG) {
            Utils.d("start ");
        }
        this.mSurfaceView = surfaceView;
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
    }

    public void stop() {
        if (DEBUG) {
            Utils.d(" stop ");
        }
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
        }
    }

    public void destroy() {
        if (DEBUG) {
            Utils.d(" destroy ");
        }
        if (mCamera != null) {
            stop();
            mCamera.release();
            mCamera = null;
        }
    }

    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public Camera changeCamera() {
        Camera camera = getCamera((1 + mCameraId) % getNumberOfCameras());
        if (DEBUG) {
            Utils.d("changeCamera " + mCameraId);
        }
        updateCamera(camera, holder);
        return camera;
    }

    public Camera openCamera() {
        if (DEBUG) {
            Utils.d("openCamera");
        }
        Camera camera = getCamera(mCameraId);
        updateCamera(camera, holder);
        return camera;
    }

    private Camera getCamera(int cameraId) {
        Camera camera = null;
        if (this.mCameraId == cameraId && mCamera != null) {
            camera = mCamera;
        } else if (mCamera != null) {
            destroy();
        }
        this.mCameraId = cameraId;
        if (camera == null) {
            try {
                Utils.d("open camera with " + mCameraId);
                camera = Camera.open(mCameraId);
            } catch (Exception e) {
                if (DEBUG) {
                    Utils.d("getCamera " + mCameraId + " 摄像头被占用");
                }
            }
        }
        return camera;
    }

    private void updateCamera(Camera mCamera, SurfaceHolder holder) {
        if (mCamera == null) {
            if (DEBUG) {
                Utils.d("updateCamera 摄像机为空");
            }
            return;
        }

        if (this.mCamera == mCamera && holder == this.holder) {
            if (DEBUG) {
                Utils.d("updateCamera no changed");
            }
            return;
        }
        if (mCamera != null && holder != null) {
            try {
                mCamera.setDisplayOrientation(90);
                Camera.Size mPreviewSize = mCamera.getParameters().getPreviewSize();
                int with = mSurfaceView.getMeasuredWidth();
                int height = with * mPreviewSize.width / mPreviewSize.height;
                ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
                lp.width = with;
                lp.height = height;
                mSurfaceView.setLayoutParams(lp);
                mCamera.setPreviewDisplay(holder);//设置显示面板控制器
                priviewCallBack pre = new priviewCallBack();//建立预览回调对象
                mCamera.setPreviewCallback(pre); //设置预览回调对象
                //mCamera.getParameters().setPreviewFormat(ImageFormat.JPEG);
                mCamera.startPreview();//开始预览，这步操作很重要
                if (DEBUG) {
                    Utils.d("updateCamera preview");
                }
            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        }
        this.mCamera = mCamera;
        this.holder = holder;
    }

    /* 相机初始化的method */
    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                /*
                 * 设定相片大小为1024*768， 格式为JPG
                 */
                // parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(1024, 768);
                mCamera.setParameters(parameters);
                /* 打开预览画面 */
                mCamera.startPreview();
                if (DEBUG) {
                    Utils.d("initCamera preview");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPath(String captureFile) {
        this.captureFile = captureFile;
    }

    private String captureFile;

    // 每次cam采集到新图像时调用的回调方法，前提是必须开启预览
    class priviewCallBack implements Camera.PreviewCallback {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
//            Log.e(TAG, "onPreviewFrame " + (data == null ? 0 : data.length));
            if (data == null || data.length <= 0) {
                return;
            }
            if (captureFile != null && captureFile.length() > 0) {
                Log.e(TAG, "onPreviewFrame captureFile " + captureFile);
                String file = captureFile;
                captureFile = null;
//                decodeToBitMap(data, camera, file);
            }
//            Log.e("支持格式", mCamera.getParameters().getPreviewFormat() + "");
        }
    }

    /* 拍照的method */
    private void takePicture() {
        if (mCamera != null) {
        /* 自动对焦后拍照 */
            mCamera.autoFocus(mAutoFocusCallback);// 调用mCamera的
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            /* 按下快门瞬间会调用这里的程序 */
        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
            /* 要处理raw data?写?否 */
        }
    };

    //在takepicture中调用的回调方法之一，接收jpeg格式的图像
    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {

            /*
             * if (Environment.getExternalStorageState().equals(
             * Environment.MEDIA_MOUNTED)) // 判断SD卡是否存在，并且可以可以读写 {
             *
             * } else { Toast.makeText(EX07_16.this, "SD卡不存在或写保护",
             * Toast.LENGTH_LONG) .show(); }
             */
            // Log.w("============", _data[55] + "");

            try {
                /* 取得相片 */
                Bitmap bm = BitmapFactory.decodeByteArray(_data, 0,
                        _data.length);

                /* 创建文件 */
                File myCaptureFile = new File("strCaptureFilePath", "1.jpg");
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(myCaptureFile));
                /* 采用压缩转档方法 */
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                /* 调用flush()方法，更新BufferStream */
                bos.flush();

                /* 结束OutputStream */
                bos.close();

                /* 让相片显示3秒后圳重设相机 */
                // Thread.sleep(2000);
                /* 重新设定Camera */
                stop();
                initCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* 自定义class AutoFocusCallback */
    public final class AutoFocusCallback implements
            Camera.AutoFocusCallback {
        public void onAutoFocus(boolean focused, Camera camera) {

            /* 对到焦点拍照 */
            if (focused) {
                takePicture();
            }
        }
    }


    // 检测摄像头是否存在的私有方法
    private static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }

    public void decodeToBitMap(byte[] data, String captureFile) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
                    size.height, null);
            Log.w(TAG, size.width + " " + size.height);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height),
                        80, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(
                        stream.toByteArray(), 0, stream.size());
                Log.w(TAG, bmp.getWidth() + " " + bmp.getHeight());
                Log.w(TAG,
                        (bmp.getPixel(100, 100) & 0xff) + "  "
                                + ((bmp.getPixel(100, 100) >> 8) & 0xff) + "  "
                                + ((bmp.getPixel(100, 100) >> 16) & 0xff));

                stream.close();
                save(bmp, captureFile);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error:" + ex.getMessage());
        }
    }

    public void onPictureTaken(byte[] _data, Camera _camera, String strCaptureFilePath) {

            /*
             * if (Environment.getExternalStorageState().equals(
             * Environment.MEDIA_MOUNTED)) // 判断SD卡是否存在，并且可以可以读写 {
             *
             * } else { Toast.makeText(EX07_16.this, "SD卡不存在或写保护",
             * Toast.LENGTH_LONG) .show(); }
             */
        // Log.w("============", _data[55] + "");
        if (DEBUG) {
            Log.e(TAG, "onPictureTaken save to " + strCaptureFilePath + "/1.jpg");
        }
        try {
                /* 取得相片 */
            Bitmap bm = BitmapFactory.decodeByteArray(_data, 0,
                    _data.length);

                /* 创建文件 */
            File myCaptureFile = new File(strCaptureFilePath, "1.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
                /* 采用压缩转档方法 */
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                /* 调用flush()方法，更新BufferStream */
            bos.flush();

                /* 结束OutputStream */
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onPictureTaken error to " + strCaptureFilePath + "/1.jpg");
        }

        if (DEBUG) {
            Log.e(TAG, "onPictureTaken saveed to " + strCaptureFilePath + "/1.jpg");
        }
    }

    private void save(Bitmap bm, String strCaptureFilePath) throws IOException {
            /* 创建文件 */
        File myCaptureFile = new File(strCaptureFilePath, "1.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
                /* 采用压缩转档方法 */
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                /* 调用flush()方法，更新BufferStream */
        bos.flush();

                /* 结束OutputStream */
        bos.close();
    }
}
