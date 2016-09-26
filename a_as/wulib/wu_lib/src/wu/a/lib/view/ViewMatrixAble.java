package wu.a.lib.view;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import wu.a.lib.utils.Logger;

/**
 * Created by jx on 2016/9/12.
 */
public class ViewMatrixAble extends MatrixAble {
    float[] values = new float[9];

    public ViewMatrixAble(View destView) {
        super(destView);
        this.destView = destView;
    }

    public Matrix getMatrix() {
        return oldMatrix;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onMatrixChanged(Matrix matrix) {
        oldMatrix.getValues(values);
        Log.d("dddd", "tx " + values[Matrix.MTRANS_X] + " ty " + values[Matrix.MTRANS_Y]
                + " scx " + values[Matrix.MSCALE_X] + " scy " + values[Matrix.MSCALE_Y]);
        Log.e("dddd", "tx " + values[Matrix.MTRANS_X] + " ty " + values[Matrix.MTRANS_Y]
                + " scx " + values[Matrix.MSCALE_X] + " scy " + values[Matrix.MSCALE_Y]);
        destView.setTranslationX(values[Matrix.MTRANS_X]);
        destView.setTranslationY(values[Matrix.MTRANS_Y]);
        destView.setScaleX(values[Matrix.MSCALE_X]);
        destView.setScaleY(values[Matrix.MSCALE_Y]);
    }
}
