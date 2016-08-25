package wu.a.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wu.videodemo.Utils;

/**
 * Created by jx on 2016/6/14.
 */
public class CameraParams implements Camera.AutoFocusCallback {

    private Camera mCamera;

    public boolean setFocusMode(String focusMode) {
        boolean result = false;
        Camera.Parameters params = mCamera.getParameters();
        List<String> FocusModes = params.getSupportedFocusModes();
        if (FocusModes.contains(focusMode)) {
            params.setFocusMode(focusMode);
            mCamera.setParameters(params);
            result = true;
        } else {
            Utils.w("Focus Mode not supported: " + focusMode);
        }
        return result;
    }

    private void getSupportedFocusModes() {
        Utils.d("getSupportedFocusModes:");
        Camera.Parameters params = mCamera.getParameters();
        List<String> FocusModes = params.getSupportedFocusModes();
        for (String item : FocusModes) {
            Utils.d(item);
        }
    }

    public boolean setFlashMode(String flashMode) {
        boolean result = false;
        Camera.Parameters params = mCamera.getParameters();
        List<String> FlashModes = params.getSupportedFlashModes();
        if (FlashModes.contains(flashMode)) {
            params.setFocusMode(flashMode);
            mCamera.setParameters(params);
            result = true;
        } else {
            Utils.w("Flash Mode not supported: " + flashMode);
        }
        return result;
    }

    private void getSupportedFlashModes() {
        Utils.d("getSupportedFocusModes:");
        Camera.Parameters params = mCamera.getParameters();
        List<String> FlashModes = params.getSupportedFlashModes();
        for (String item : FlashModes) {
            Utils.d(item);
        }
    }

    public void focusOnTouch(View view, MotionEvent event) {
        float x = event.getRawX(), y = event.getRawY();
        setFocusArea(x / view.getWidth(), y / view.getHeight());
    }

    /**
     * @param x position scale, value range[0-1]
     * @param y position scale, value range[0-1]
     */
    public void setFocusArea(float x, float y) {
        Rect focusRect = calculateTapArea(x, y, 1f);
        Rect meteringRect = calculateTapArea(x, y, 1.5f);
//        mCamera.cancelAutoFocus();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));

            parameters.setMeteringAreas(meteringAreas);
        }

        mCamera.setParameters(parameters);
        mCamera.autoFocus(this);
    }

    /**
     * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
     *
     * @param x           locale[0-1]
     * @param y           locale[0-1]
     * @param coefficient areaScale
     * @return
     */
    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int centerX = (int) (x * 2000 - 1000);
        int centerY = (int) (y * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    public void getFocusAreaInfo() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        Utils.d("getMaxNumFocusAreas=" + parameters.getMaxNumFocusAreas());
        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = parameters.getFocusAreas();
            for (Camera.Area item : focusAreas) {
                Utils.d("focusArea:" + item.rect);
            }
        }

        Utils.d("getMaxNumMeteringAreas=" + parameters.getMaxNumMeteringAreas());
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meterings = parameters.getMeteringAreas();
            for (Camera.Area area : meterings) {
                Utils.d("metering area:" + area.rect);
            }
        }

        mCamera.setParameters(parameters);
        mCamera.autoFocus(this);
    }

    public Camera.Size getResolution() {
        Camera.Parameters params = mCamera.getParameters();
        Camera.Size s = params.getPreviewSize();
        Utils.d("PreviewSize: " + s.width + "," + s.height);
        return s;
    }

    public Camera.CameraInfo getCameraInfo(int mCameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        Utils.d("CameraInfo:facing=" + info.facing + ", orientation=" + info.orientation + ", canDisableShutterSound=" + info.canDisableShutterSound);
        return info;
    }

    //获得摄像头的旋转角度
    public int getRotation(Activity activity, int mCameraId) {
        int mOrientation = getCameraDisplayOrientation(activity, mCameraId, true);
        Utils.d("[+] 计算摄像头旋转：" + mCameraId + " -> " + mOrientation);
        return mOrientation;
    }

    /**
     * This method will be useless when using CameraGLSurfaceView to display camera preview
     *
     * @param activity
     * @param cameraId
     */
    public static int getCameraDisplayOrientation(Activity activity, int cameraId, boolean mirror) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            // compensate the mirror
            if (mirror) {
                result = (360 - result) % 360;
            }
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private static final int PIXWIDTH = 640;
    private static final int PIXHEIGHT = 480;
    private static final int FPS = 20;
    private void startCameraPreview(Camera camera, Camera.PreviewCallback previewCallback, SurfaceView surfaceView, SurfaceHolder holder, int rotation) {
        Utils.d("[+] 初始化相机并开始预览");
        if (null != camera) {
            Camera.Parameters parameters = camera.getParameters();
            //针对小手机，如果屏幕小于640，就要缩小previewSize否则按照640X480录制的buffer会溢出。
            //TODO 血的教训呀!!!
            int width = Math.min(PIXWIDTH, parameters.getPreviewSize().width), height = Math.min(PIXHEIGHT, parameters.getPreviewSize().height);
            DisplayMetrics mDisplayMetrics=surfaceView.getContext().getResources().getDisplayMetrics();
            int screenWidth = mDisplayMetrics.widthPixels;
            int screenHeight = mDisplayMetrics.heightPixels;
            Camera.Size size = getOptimalPreviewSize(camera.getParameters().getSupportedPreviewSizes(), width, height);

            Utils.d(String.format("[+] 预览：[%d/%d] 屏幕：[%d,%d] 最合适：[%dx%d] / [%dx%d]， ", parameters.getPreviewSize().width, parameters.getPreviewSize().height, screenHeight, screenWidth, size.width, size.height, width, height));
//            sConfig.put("width", size.width);
//            sConfig.put("height", size.height);
//            Utils.set(Constants.CAMERA_PREVIEW_SIZE, sConfig);
            parameters.setFlashMode("off");
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setPreviewFormat(ImageFormat.NV21);
            if (Build.VERSION.SDK_INT < 9) {
                parameters.setPreviewFrameRate(FPS);
            }
            parameters.setPictureSize(size.width, size.height);
            parameters.setPreviewSize(size.width, size.height);
            parameters.set("orientation", "portrait");
//            parameters.set("rotation", rotation);
            parameters.setRotation(rotation);
            camera.setDisplayOrientation(rotation);
            camera.setParameters(parameters);
            Camera.Size mPreviewSize = camera.getParameters().getPreviewSize();
            int pwith = screenWidth;
            int pheight = pwith * mPreviewSize.width / mPreviewSize.height;
            ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
            lp.width = pwith;
            lp.height = pheight;
            surfaceView.setLayoutParams(lp);
            camera.autoFocus(mCamerafocus);
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.setPreviewCallback(previewCallback);
            camera.startPreview();
        }
    }

    //计算最适合的preview size
    private static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
//            Utils.e("[$] " + size.width + " / " + size.height);
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
