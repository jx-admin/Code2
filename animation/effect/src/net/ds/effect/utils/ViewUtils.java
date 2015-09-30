
package net.ds.effect.utils;

import net.ds.effect.BuildConfig;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class ViewUtils {
    
    private final static Paint sPaint = new Paint();
    private static final boolean LOGE_ENABLED = BuildConfig.DEBUG;
    private static final String TAG = Constants.TAG;

    /**
     * 是否硬件加速
     * @param view
     * @return
     */
    public static boolean isHardwareAccelerated(View view) {
        if (!CommonUtils.isEnable()) {
            return false;
        }

        try {
            return view.isHardwareAccelerated();
        } catch (Throwable e) {
            if (LOGE_ENABLED) {
                Log.e(TAG, "Failed to invoke isHardwareAccelerated", e);
            }

            return false;
        }
    }

    /**
     * 是否在硬件层绘制,只开启{@linkplain #isHardwareAccelerated(View)}}}会用Display List绘制，而这里true会用HardwareRender绘制（我们现在4.0上采用这种机制）
     * @param view
     * @return
     */
    public static boolean isViewUseHardwareLayer(View view) {
        if (!CommonUtils.isEnable()) {
            return false;
        }

        try {
            return view.getLayerType() == View.LAYER_TYPE_HARDWARE;
        } catch (Throwable e) {
            if (LOGE_ENABLED) {
                Log.e(TAG, "Failed to invoke isHardwareAccelerated", e);
            }

            return false;
        }
    }

    public static boolean canEnableHardwareLayer(View view) {
        if (Build.VERSION.SDK_INT < 17) { // before 4.2
            return false;
        }

        try {
            return view.isHardwareAccelerated();
        } catch (Throwable e) {
            if (LOGE_ENABLED) {
                Log.e(TAG, "Failed to invoke isHardwareAccelerated", e);
            }

            return false;
        }
    }

    public static void enableHardwareLayer(View view, boolean hasLayer) {
        if (!CommonUtils.isEnable()) {
            return;
        }

        try {
            if (view.isHardwareAccelerated()) {
                view.setLayerType(hasLayer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE, sPaint);
            }
        } catch (Throwable e) {
            if (LOGE_ENABLED) {
                Log.e(TAG, "Failed to invoke enableHardwareLayer", e);
            }
        }
    }

    public static void destroyHardwareLayer(View view) {
        if (!CommonUtils.isEnable()) {
            return;
        }

        try {
            try {
                CommonUtils.invokeMethod(View.class, view, "destroyLayer", new Class[] {
                    boolean.class
                }, false);
            } catch (Throwable e) {
                CommonUtils.invokeMethod(View.class, view, "destroyLayer", null);
            }

            if (LOGE_ENABLED) {
                Log.d(TAG, "After destroyHardwareLayer, mHardwareLayer = " + CommonUtils.invokeField(View.class, view, "mHardwareLayer"));
            }
        } catch (Throwable e) {
            if (LOGE_ENABLED) {
                Log.e(TAG, "Failed to invoke destroyHardwareLayer", e);
            }
        }
    }

    public static void setLayerType(boolean layer, Handler handler, final View mainView, final View... views) {
        if (Build.VERSION.SDK_INT < 11) return;

        final int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;

        if (layerType != mainView.getLayerType() && views != null) {
            handler.post(new Runnable() {
                public void run() {
                    Log.v(TAG, "changing layerType. hardware? " + (layerType == View.LAYER_TYPE_HARDWARE));
                    mainView.setLayerType(layerType, null);
                    for (View view : views) {
                        if (view != null) {
                            view.setLayerType(layerType, null);
                        }
                    }
                }
            });
        }
    }

    public static final int LAYER_TYPE_NONE = View.LAYER_TYPE_NONE;
    public static final int LAYER_TYPE_SOFTWARE = View.LAYER_TYPE_SOFTWARE;
    public static final int LAYER_TYPE_HARDWARE = View.LAYER_TYPE_HARDWARE;

    public static void setLayerType(View v, int layer) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        v.setLayerType(layer, sPaint);
    }

    /**
     * {@link View#setSystemUiVisibility(int)}}
     * @param view
     * @param visibility
     */
    public static boolean setSystemUiVisibility(View view, int visibility) {
        if (Build.VERSION.SDK_INT < 11) return false;

        try {
            view.setSystemUiVisibility(visibility);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @SuppressLint("NewApi")
    public static void postOnAnimation(View view, Runnable r) {
        if (Build.VERSION.SDK_INT < 16) {
            view.post(r);
        } else {
            view.postOnAnimation(r);
        }
    }

    @SuppressLint("NewApi")
	public static void postOnAnimationDelayed(View view, Runnable r, long delay) {
        if (Build.VERSION.SDK_INT < 16) {
            view.postDelayed(r, delay);
        } else {
            view.postOnAnimationDelayed(r, delay);
        }
    }
}
