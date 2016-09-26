package wu.a.lib.view;

import android.graphics.Matrix;
import android.view.TextureView;
import android.view.View;

/**
 * Created by jx on 2016/8/2.
 */
public abstract class MatrixAble {

    /**
     * 已经起作用的设置
     */
    protected Matrix oldMatrix = new Matrix();
    protected View destView;
    /**
     * 原始（没有变换前的）宽度
     */
    private float originalWidth;

    /**
     * 原始（没有变换前的）高度
     */
    private float originalHeight;

    public MatrixAble(View view) {
        this.destView = view;
    }

    public Matrix getMatrix() {
        return oldMatrix;
    }

    protected abstract void onMatrixChanged(Matrix matrix);

    public void setMatrix(Matrix matrix) {
        oldMatrix.set(matrix);
        originalWidth = 0;
        originalHeight = 0;
        onMatrixChanged(oldMatrix);
    }


    public int getWidth() {
        return destView.getWidth();
    }

    public int getHeight() {
        return destView.getHeight();
    }

    /**
     * 其原始宽度为屏幕宽度除缩放倍数
     *
     * @return
     */
    public float getOriginalWidth() {
        if (originalWidth == 0) {
            float[] values = new float[9];
            getMatrix().getValues(values);
            originalWidth = getWidth() / values[Matrix.MSCALE_X];
        }
        return originalWidth;
    }

    /**
     * 其原始长度为屏幕宽度除缩放倍数
     *
     * @return
     */
    public float getOriginalHeight() {
        if (originalHeight == 0) {
            float[] values = new float[9];
            getMatrix().getValues(values);
            originalHeight = getHeight() / values[Matrix.MSCALE_Y];
        }
        return originalHeight;
    }
}
