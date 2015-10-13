package wu.a.lib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public final class Storage {
    public static boolean isSdcardMounted() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    public static boolean isOnSdcard(String path) {
        return path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            // If the external storage is mounted but we get null cache dir, return cache dir instead
            File f = context.getExternalCacheDir();
            return null != f ? f : context.getCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Get the app cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @return The path to the internal/external cache dir
     */
    public static String getCacheDir(Context context) {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                        || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath()
                        : context.getCacheDir().getPath();
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
