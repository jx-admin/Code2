package wu.a.media.recorder;

import android.view.SurfaceView;

/**
 * Created by jx on 2016/5/26.
 */
public class CameraRecorder implements IVideoRecorder {

    public void create(SurfaceView surfaceView) {
        CameraSurfaceView.getCameraRecorder().start(surfaceView);
    }

    @Override
    public void destroy() {
        VideoRecorder.getVideoRecorder().destroy();
        CameraSurfaceView.getCameraRecorder().destroy();
    }

    @Override
    public void startRecorder(String path) {
        VideoRecorder.getVideoRecorder().start(CameraSurfaceView.getCameraRecorder().openCamera(), path);
    }

    @Override
    public void stopRecorder() {
        VideoRecorder.getVideoRecorder().stop();
    }

    @Override
    public boolean isRecording() {
        return false;
    }

    public void changeCamera() {
        VideoRecorder.getVideoRecorder().stop();
        CameraSurfaceView.getCameraRecorder().changeCamera();
    }
}
