package wu.videodemo;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jx on 2016/4/22.
 */
public class MediaUtils {

    /**
     * Create an default file for save image from camera.
     *
     * @return
     * @throws IOException
     */
    public static File createMediaFile(String fileNmae) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_",
                Locale.getDefault()).format(new Date());
        String imageFileName = /*timeStamp+*/fileNmae;
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return new File(storageDir, imageFileName);
    }
}
