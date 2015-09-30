
package net.ds.effect.framework;

import net.ds.effect.R;
import net.ds.effect.core.EffectCellLayout;
import net.ds.effect.core.EffectSlideView;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class PreviewActivity extends Activity {
    
    public static final String SELECTED_EFFECT_ID = "selected_effect_key";
    private static final int MAX_SCREEN_SIZE = 3;
    private static final int ROW_COUNT = 4;
    private static final int COLUMN_COUNT = 4;
    
    private int mCurrentEffect;
    
    private EffectSlideView mSlideView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mSlideView = (EffectSlideView) findViewById(R.id.slide_view);
        mCurrentEffect = this.getIntent().getIntExtra(SELECTED_EFFECT_ID, -1);
        mSlideView.setCurrentEffect(mCurrentEffect);
        initIcons();
    }
    
    private void initIcons() {
        for (int i = 0; i < MAX_SCREEN_SIZE; i++) {
            EffectCellLayout cellLayout = new EffectCellLayout(this);
            cellLayout.setDimension(COLUMN_COUNT, ROW_COUNT);

            for (int j = 0; j < COLUMN_COUNT; j++) {
                for (int k = 0; k < ROW_COUNT; k++) {
                    TextView textView = new TextView(this);
                    Drawable drawable = this.getResources().getDrawable(getIconResId(k));
                    drawable = new BitmapDrawable(this.getResources(), ((BitmapDrawable) drawable).getBitmap());
                    drawable.setBounds(0, 0, this.getResources().getDimensionPixelSize(R.dimen.app_icon_size), this.getResources().getDimensionPixelSize(R.dimen.app_icon_size));
                    textView.setCompoundDrawables(null, drawable, null, null);
                    
                    ItemInfo itemInfo = new ItemInfo();
                    itemInfo.cellX = k;
                    itemInfo.cellY = j;
                    itemInfo.spanX = 1;
                    itemInfo.spanY = 1;
                    itemInfo.screen = i;
                    textView.setTag(itemInfo);
                    
                    cellLayout.addView(textView);
                }
            }
            mSlideView.addView(cellLayout);
        }
    }
    
    private int getIconResId(int j) {
        if (j == 0) {
            return R.drawable.app_0;
        } else if (j == 1) {
            return R.drawable.app_1;
        } else if (j == 2) {
            return R.drawable.app_2;
        } else {
            return R.drawable.app_3;
        }
    }
}
