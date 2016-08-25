package wu.a.media.recorder;

import android.view.SurfaceView;

/**
 * Created by jx on 2016/5/26.
 */
public interface IVideoRecorder {
    public void create(SurfaceView surfaceView);
    void destroy();
    void startRecorder(String path);
    void stopRecorder();
    boolean isRecording();

}
