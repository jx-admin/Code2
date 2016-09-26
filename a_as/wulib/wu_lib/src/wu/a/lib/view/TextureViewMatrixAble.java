package wu.a.lib.view;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.os.Build;
import android.view.TextureView;

/**
 * Created by jx on 2016/9/12.
 */
public class TextureViewMatrixAble extends MatrixAble {

    public TextureViewMatrixAble(TextureView destView) {
        super(destView);
        this.destView = destView;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Matrix getMatrix() {
        return ((TextureView) destView).getTransform(oldMatrix);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onMatrixChanged(Matrix matrix) {
        ((TextureView) destView).setTransform(oldMatrix);
    }
}
