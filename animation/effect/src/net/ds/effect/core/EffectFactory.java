
package net.ds.effect.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ds.effect.R;
import net.ds.effect.framework.CellLayout;
import net.ds.effect.framework.ItemInfo;
import net.ds.effect.utils.Constants;
import net.ds.effect.utils.RuntimeConfig;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * <p>
 * Represents the factory which holds all the effects for the workspace.
 * </p>
 *
 * @author huangninghai
 * @version 1.0
 */
public class EffectFactory {
    private static List<EffectInfo> allEffects;

    private static Map<Integer, EffectInfo> allEffectsMap;

    private static Camera camera = new Camera();

    private static Matrix sMatrix = new Matrix();

    private static PaintFlagsDrawFilter sAntiAliesFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public static EffectInfo getEffectByType(int type) {
        Map<Integer, EffectInfo> effects = loadEffectsMap();

        return effects.get(type);
    }

    public static EffectInfo getNextEffectByType(int type, int effectOn) {
        List<EffectInfo> effects = getAllEffects(effectOn);

        int index = -1;

        for (int i = 0; i < effects.size(); i++) {
            EffectInfo effectInfo = effects.get(i);
            if (type == effectInfo.type) {
                index = i + 1;
            }
        }

        if (index < 0 || index >= effects.size()) {
            index = 0;
        }

        if (index >= 0 && index < effects.size()) {
            return effects.get(index);
        } else {
            return null;
        }
    }

    public static List<EffectInfo> getAllEffects(int effectOn) {
        List<EffectInfo> effects = loadEffectsList();
        List<EffectInfo> effectsList = new LinkedList<EffectInfo>();
        for (EffectInfo ef : effects) {
            if (ef.isEffectOn(effectOn)) {
                effectsList.add(ef);
            }
        }
        return effectsList;
    }

    private static Map<Integer, EffectInfo> loadEffectsMap() {
        if (allEffectsMap == null) {
            List<EffectInfo> effects = loadEffectsList();

            allEffectsMap = new HashMap<Integer, EffectInfo>();
            for (EffectInfo effect : effects) {
                allEffectsMap.put(effect.type, effect);
            }
        }
        return allEffectsMap;
    }

    public static final int TYPE_CLASSIC = 0;

    public static final int TYPE_CUSTOM_RANDOM = -2;

    public static final int TYPE_RANDOM = -1;

    public static final int TYPE_CUBE_OUTSIDE = 1;

    public static final int TYPE_CROSS_FADE = 2;

    public static final int TYPE_FALLDOWN = 3;

    public static final int TYPE_STACK = 4;

    public static final int TYPE_ROTATE = 5;

    public static final int TYPE_PAGE_SLIDE_DOWN = 6;

    public static final int TYPE_FLIPPY = 7;

    public static final int TYPE_ICON_COLLECTION = 8;

    public static final int TYPE_ROLL_AWAY = 9;

    public static final int TYPE_CHORD = 10;

    public static final int TYPE_SNAKE = 11;

    public static final int TYPE_SWIRL = 12;

    public static final int TYPE_CYLINDER = 13;

    public static final int TYPE_SPHERE = 14;

    public static final int TYPE_DISTORT = 15;

    public static final int TYPE_TURN = 16;

    public static final int TYPE_EXTRUSION = 17;

    /**
     * Load all effects the launcher have
     *
     * @return
     */
    private static List<EffectInfo> loadEffectsList() {
        if (allEffects != null) {
            return allEffects;
        } else {
            allEffects = new ArrayList<EffectInfo>();

            allEffects.add(new Classic());

            allEffects.add(new Sphere()); // 球

            allEffects.add(new Cylinder()); // 圆柱

            allEffects.add(new Swirl()); // 漩涡

            allEffects.add(new Snake()); // 贪吃蛇

            allEffects.add(new CubeOutside());     // 立方体

            allEffects.add(new CrossFade()); // 飘渺

            allEffects.add(new Falldown()); // 扇面

            allEffects.add(new Stack());    // 层叠

            allEffects.add(new Rotate());   // 旋转

            allEffects.add(new SlideDown()); // 忐忑

            allEffects.add(new Flippy());   // 追风

            allEffects.add(new IconCollection()); // 聚散

            allEffects.add(new RollAway()); // 车轮

            allEffects.add(new Chord());    // 琴弦

            allEffects.add(new Distort());  // 扭转

            allEffects.add(new Turn()); // 拨片

            allEffects.add(new Extrusion()); // 推拉
        }

        return allEffects;
    }

    private static final class Classic extends EffectInfo {

        Classic() {
            super(0, TYPE_CLASSIC, "pref_key_transformation_classic", "@string/transformation_type_classic", R.drawable.effect_overview_simple_classic);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            Matrix matrix = childTransformation.getMatrix();

            if (offset != 0) {
                if (isPortrait) {
                    matrix.postTranslate(offset, 0);
                } else {
                    matrix.postTranslate(0, offset);
                }

                childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

                return true;
            }

            childTransformation.setTransformationType(Transformation.TYPE_IDENTITY);
            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class CubeOutside extends EffectInfo {
        CubeOutside() {
            super(3, TYPE_CUBE_OUTSIDE, "pref_key_transformation_cube_outside", "@string/transformation_type_cube_outside", R.drawable.effect_overview_simple_cube_outside);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            final Matrix matrix = childTransformation.getMatrix();

            if (radio > 0) {
                applyLeft(1.0f - radio, childMeasuredWidth, childMeasuredHeight, matrix);
            } else {
                applyRight(1.0f + radio, childMeasuredWidth, childMeasuredHeight, matrix);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }

        public void applyLeft(float ratio, int w, int h, Matrix matrix) {
            // 倾斜角度 = 倾斜比例 * 最大倾斜角度
            final float deg = (1.0f - ratio) * -75.0f;
            camera.save();
            camera.rotateY(deg);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-w, -h / 2);
            matrix.postTranslate(w, h / 2);
        }

        public void applyRight(float ratio, int w, int h, Matrix matrix) {
            // 倾斜角度 = 倾斜比例 * 最大倾斜角度
            final float deg = (1.0f - ratio) * 75.0f;
            camera.save();
            camera.rotateY(deg);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(0.0f, -h / 2);
            matrix.postTranslate(0.0f, h / 2);
        }

    }

    private static final class CrossFade extends EffectInfo {
        CrossFade() {
            super(4, TYPE_CROSS_FADE, "pref_key_transformation_corss_fade", "@string/transformation_type_crossfade", R.drawable.effect_overview_simple_crossfade);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            childTransformation.setAlpha(1.0F - Math.abs(radio));

            Matrix matrix = childTransformation.getMatrix();

            if (offset != 0) {
                if (isPortrait) {
                    matrix.postTranslate(offset, 0);
                } else {
                    matrix.postTranslate(0, offset);
                }
                childTransformation.setTransformationType(Transformation.TYPE_BOTH);
            } else {
                childTransformation.setTransformationType(Transformation.TYPE_ALPHA);
            }

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class Falldown extends EffectInfo {
        Falldown() {
            super(5, TYPE_FALLDOWN, "pref_key_transformation_fall_down", "@string/transformation_type_falldown", R.drawable.effect_overview_simple_fall_down);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.setRotate(-radio * 45.0F, childMeasuredWidth / 2.0F, childMeasuredHeight);
            } else {
                matrix.setRotate(radio * 45.0F, childMeasuredWidth, childMeasuredHeight / 2.0F);
            }

            if (offset != 0) {
                if (isPortrait) {
                    matrix.postTranslate(offset, 0);
                } else {
                    matrix.postTranslate(0, offset);
                }
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class Stack extends EffectInfo {
        Stack() {
            super(7, TYPE_STACK, "pref_key_transformation_stack", "@string/transformation_type_stack", R.drawable.effect_overview_simple_stack);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (radio <= 0.0F) {
                return false;
            }

            childTransformation.setAlpha(1.0F - radio);

            float scale = 0.4F * (1.0F - radio) + 0.6F;
            matrix.setScale(scale, scale);
            if (isPortrait) {
                matrix.postTranslate((1.0F - scale) * childMeasuredWidth * 3.0F + offset, (1.0F - scale) * childMeasuredHeight * 0.5F);
            } else {
                matrix.postTranslate((1.0F - scale) * childMeasuredWidth * 0.5F, (1.0F - scale) * childMeasuredHeight * 3.0F + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_BOTH);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class Rotate extends EffectInfo {
        Rotate() {
            super(8, TYPE_ROTATE, "pref_key_transformation_rotate", "@string/transformation_type_rotate", R.drawable.effect_overview_simple_rotate);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (radio == 0.0F) {
                return false;
            }

            if (radio > 0) {
                applyLeft(radio, childMeasuredWidth, childMeasuredHeight, matrix);
            } else {
                applyRight(1.0f + radio, childMeasuredWidth, childMeasuredHeight, matrix);
            }

            matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        public void applyLeft(float radio, int w, int h, Matrix matrix) {
            if (radio > 0.5f) {
                camera.save();
                camera.rotateY(90);
                camera.getMatrix(matrix);
                camera.restore();
                return;
            }

            final float deg = -180.0F * radio;
            camera.save();
            final float zt = w * radio * 1.5f;
            camera.translate(0, 0, zt);
            camera.rotateY(deg);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(- w / 2, -h / 2);
            matrix.postTranslate(w / 2, h / 2);
        }

        public void applyRight(float radio, int w, int h, Matrix matrix) {
            if (radio < 0.5f) {
                camera.save();
                camera.rotateY(90);
                camera.getMatrix(matrix);
                camera.restore();
                return;
            }

            final float deg = 180.0F * (1f - radio);
            camera.save();
            final float zt = w * (1 - radio) * 1.5f;
            camera.translate(0, 0, zt);
            camera.rotateY(deg);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(- w / 2, -h / 2);
            matrix.postTranslate(w / 2, h / 2);
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class SlideDown extends EffectInfo {
        SlideDown() {
            super(9, TYPE_PAGE_SLIDE_DOWN, "pref_key_transformation_page_slide_down", "@string/transformation_type_page_slide_down", R.drawable.effect_overview_simple_page_slide_down);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            childTransformation.setAlpha(1.0F - Math.abs(radio));

            if (isPortrait) {
                matrix.postTranslate(offset, Math.abs(radio) * childMeasuredHeight);
            } else {
                matrix.postTranslate(Math.abs(radio) * childMeasuredWidth, offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_BOTH);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class Flippy extends EffectInfo {
        Flippy() {
            super(14, TYPE_FLIPPY, "pref_key_transformation_flippy", "@string/transformation_type_flippy", R.drawable.effect_overview_simple_flippy);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            camera.save();
            camera.translate(0, 0, parentView.getMeasuredWidth() * Math.abs(radio) / 4.0f);
            if (isPortrait) {
                camera.rotateY(30.0F * radio);
            } else {
                camera.rotateX(-30.0F * radio);
            }
            camera.getMatrix(matrix);
            camera.restore();

            float scale = Math.abs(radio) / 5.0f;
            if (radio < 0) {
                if (isPortrait) {
                    matrix.preTranslate(-childMeasuredWidth / 2.0f, -childMeasuredHeight / 2.0F);
                    matrix.postTranslate(offset + (1 + scale) * childMeasuredWidth / 2.0f, childMeasuredHeight / 2.0F);
                } else {
                    matrix.preTranslate(-childMeasuredHeight / 2.0F, -childMeasuredWidth / 2.0f);
                    matrix.postTranslate(childMeasuredHeight / 2.0F, offset + (1 + scale) * childMeasuredWidth / 2.0F);
                }
            } else {
                if (isPortrait) {
                    matrix.preTranslate(-childMeasuredWidth / 2.0f, -childMeasuredHeight / 2.0F);
                    matrix.postTranslate(offset + (1 + scale) * childMeasuredWidth / 2.0f, childMeasuredHeight / 2.0F);
                } else {
                    matrix.preTranslate(-childMeasuredHeight / 2.0F, -childMeasuredWidth / 2.0f);
                    matrix.postTranslate(childMeasuredHeight / 2.0F, offset + (1 + scale) * childMeasuredWidth / 2.0F);
                }
            }

            float alpha = (1.0f - Math.abs(radio)) / 2 + 0.5F;
            childTransformation.setAlpha(alpha);
            childTransformation.setTransformationType(Transformation.TYPE_BOTH);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class IconCollection extends EffectInfo {

        IconCollection() {
            super(16, TYPE_ICON_COLLECTION, "pref_key_transformation_icon_collection", "@string/transformation_type_icon_collection", R.drawable.effect_overview_simple_collection);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            Matrix matrix = childTransformation.getMatrix();

            if (offset != 0) {
                if (isPortrait) {
                    matrix.postTranslate(offset, 0);
                } else {
                    matrix.postTranslate(0, offset);
                }
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            float parentMeasuredWidth = parentView.getMeasuredWidth();
            float parentMeasuredHeight = parentView.getMeasuredHeight();

            float scale = 0.0F;
            float distanceWidth;
            float distanceHeight;

            sMatrix.reset();
            Matrix matrix = sMatrix;

            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            distanceWidth = parentView.getLeft() + parentMeasuredWidth / 2.0F - childView.getLeft() - childMeasuredWidth / 2.0F;
            distanceHeight = parentView.getTop() + parentMeasuredHeight / 2.0F - childView.getTop() - childMeasuredHeight / 2.0F;

            if (isPortrait) {
                distanceWidth -= itemInfo.screen * parentMeasuredWidth;
            } else {
                distanceHeight -= itemInfo.screen * parentMeasuredHeight;
            }

            if (itemInfo.spanX > 1 || itemInfo.spanY > 1) {
                scale = 1.0F / Math.max(itemInfo.spanX, itemInfo.spanY);
            }

            if (Math.abs(radioX) <= 0.5) {
                float extraWidth = 0.0f;
                float extraHeight = 0.0f;
                if (scale != 0.0F) {
                    scale = (scale - 1.0F) * Math.abs(radioX) * 2.0F + 1.0F;
                    matrix.preScale(scale, scale);
                    matrix.preTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                    extraWidth = childMeasuredWidth / 2.0F;
                    extraHeight = childMeasuredHeight / 2.0F;
                }
                matrix.postTranslate(extraWidth + Math.abs(radioX) * 2.0F * distanceWidth, extraHeight + Math.abs(radioX) * 2.0F * distanceHeight);
            } else {
                float extraWidth = 0.0f;
                float extraHeight = 0.0f;
                if (scale != 0.0F) {
                    scale = (scale - 1.0F) * (1 - Math.abs(radioX)) * 2.0F + 1.0F;
                    matrix.preScale(scale, scale);
                    matrix.preTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                    extraWidth = childMeasuredWidth / 2.0F;
                    extraHeight = childMeasuredHeight / 2.0F;
                }
                if (isPortrait) {
                    matrix.postTranslate(extraWidth + Math.abs(radioX) * 2.0F * distanceWidth, extraHeight + (1 - Math.abs(radioX)) * 2.0F * distanceHeight);
                } else {
                    matrix.postTranslate(extraWidth + (1 - Math.abs(radioX)) * 2.0F * distanceWidth, extraHeight + Math.abs(radioX) * 2.0F * distanceHeight);
                }
            }

            return apply(canvas, childView, drawingTime, isCellLayoutNeedAntiAlias() ? sAntiAliesFilter : null, matrix, 1.0F, callback);
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }
    }

    private static final class RollAway extends EffectInfo {

        RollAway() {
            super(18, TYPE_ROLL_AWAY, "pref_key_transformation_roll_away", "@string/transformation_type_roll_away", R.drawable.effect_overview_simple_roll_away);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (radio <= -0.5F) {
                matrix.postRotate((1 + radio) * 2.0F * -90.0F, childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
            } else if (radio >= 0.5F) {
                matrix.postRotate((1 - radio) * 2.0F * 90.0F, childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
            }

            if (offset != 0) {
                if (isPortrait) {
                    matrix.postTranslate(offset, 0);
                } else {
                    matrix.postTranslate(0, offset);
                }
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            int count = parentView.getChildCount();

            if (count == 0) {
                return null;
            }

            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            float parentMeasuredWidth = parentView.getMeasuredWidth();
            float parentMeasuredHeight = parentView.getMeasuredHeight();

            float scale = 0.0F;
            float distanceWidth;
            float distanceHeight;
            float distanceDegrees;

            sMatrix.reset();
            Matrix matrix = sMatrix;

            int cellX = itemInfo.cellX;
            int cellY = itemInfo.cellY;

            int index = 0;

            for (int i = 0; i < count; i++) {
                View view = parentView.getChildAt(i);

                if (view.getTag() instanceof ItemInfo) {
                    int otherCellX = ((ItemInfo) view.getTag()).cellX;
                    int otherCellY = ((ItemInfo) view.getTag()).cellY;

                    if (otherCellY < cellY) {
                        index++;
                    } else if (otherCellY == cellY && otherCellX < cellX) {
                        index++;
                    }
                }
            }

            float angle = 360.0F / count * index;
            CellLayout cellLayout = (CellLayout) parentView;
            float r = cellLayout.getCellShortAxisDistance() * 1.25F;
            distanceDegrees = 90.0F + angle;

            if (radioX >= 0.5F) {
                distanceDegrees += 90.0F;
                angle += 90.0F;
                if (angle >= 360.0F) {
                    angle -= 360.0F;
                }
            } else if (radioX <= -0.5f) {
                distanceDegrees -= 90.0f;
                angle -= 90.0f;
                if (angle < 0f) {
                    angle += 360.0f;
                }
            }

            if (itemInfo.spanX > 1 || itemInfo.spanY > 1) {
                scale = 1.0F / Math.max(itemInfo.spanX, itemInfo.spanY);
            }

            distanceWidth = parentView.getLeft() + parentMeasuredWidth / 2.0F - childView.getLeft() - childMeasuredWidth / 2.0F
                    + (float) (r * Math.cos((angle <= 180.0F ? angle : 360.0F - angle) * Math.PI / 180.0));
            distanceHeight = parentView.getTop() + parentMeasuredHeight / 2.0F - childView.getTop() - childMeasuredHeight / 2.0F
                    - (float) (r * Math.sin((angle <= 180.0F ? angle : angle - 360.0F) * Math.PI / 180.0));

            if (isPortrait) {
                distanceWidth -= itemInfo.screen * parentMeasuredWidth;
            } else {
                distanceHeight -= itemInfo.screen * parentMeasuredHeight;
            }

            if (Math.abs(radioX) <= 0.5) {
                if (scale != 0.0F) {
                    if (Math.abs(radioX) <= 0.25) {
                        scale = (scale - 1.0F) * Math.abs(radioX) * 4.0F + 1.0F;
                    }
                    matrix.postTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                    matrix.postScale(scale, scale);
                    matrix.postTranslate(childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
                }
                matrix.postRotate(-Math.abs(radioX) * 2.0F * distanceDegrees, childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
                matrix.postTranslate(Math.abs(radioX) * 2.0F * distanceWidth, Math.abs(radioX) * 2.0F * distanceHeight);
            } else {
                if (scale != 0.0F) {
                    matrix.postTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                    matrix.postScale(scale, scale);
                    matrix.postTranslate(childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
                }
                matrix.postRotate(-distanceDegrees, childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
                matrix.postTranslate(distanceWidth, distanceHeight);
            }

            return apply(canvas, childView, drawingTime, isCellLayoutNeedAntiAlias() ? sAntiAliesFilter : null, matrix, 1.0F, callback);
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }
    }

    private static final class Chord extends EffectInfoWithWidgetCache {

        Chord() {
            super(20, TYPE_CHORD, "pref_key_transformation_chord", "@string/transformation_type_chord", R.drawable.effect_overview_simple_chord);
            if (Build.VERSION.SDK_INT >= 18) {
                //mEffcetOn = EFFECT_ON_DRAWER;
                mIsUsingWidgetCache = true;
            }
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            boolean ret = true;
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            if (itemInfo.spanX <= 1) {
                beforeTransformChild(canvas, childView.getContext(), childView.getLeft(), childView.getTop());
                transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX, canvas);
                ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft(), -childView.getTop(), null, callback);
                return ret;
            } else {

                if (mIsUsingWidgetCache) {
                    cacheBitmap = getWidgetBitmap(childView, itemInfo.screen, childView.getWidth(), childView.getHeight(), 0, 0);
                }

                if (radioX >= 0) {
                    for (int i = 0; i < itemInfo.spanX; i++) {
                        beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + childView.getWidth() / itemInfo.spanX * i, childView.getTop());
                        transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, canvas);
                        ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop(), new Rect(childView.getLeft()
                                + childView.getWidth() / itemInfo.spanX * i, childView.getTop(), childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth()
                                / itemInfo.spanX, childView.getTop() + childView.getHeight()), callback);
                    }
                } else {
                    for (int i = itemInfo.spanX - 1; i >= 0; i--) {
                        beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + childView.getWidth() / itemInfo.spanX * i, childView.getTop());
                        transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, canvas);
                        ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop(), new Rect(childView.getLeft()
                                + childView.getWidth() / itemInfo.spanX * i, childView.getTop(), childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth()
                                / itemInfo.spanX, childView.getTop() + childView.getHeight()), callback);
                    }
                }
            }
            return ret;
        }

        private void beforeTransformChild(Canvas canvas, Context context, float translateX, float translateY) {
            canvas.save();
            if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
                canvas.setDrawFilter(sAntiAliesFilter);
            }
            canvas.translate(translateX, translateY);
        }

        private boolean afterTransformChild(Canvas canvas, View childView, long drawingTime, float translateX, float translateY, Rect r, Callback callback) {
            boolean ret = true;
            canvas.translate(translateX, translateY);

            if (mIsUsingWidgetCache && r != null && cacheBitmap != null) {
                Rect src = new Rect(r);
                src.offset(-childView.getLeft(), -childView.getTop());
                canvas.drawBitmap(cacheBitmap, src, r, null);
            } else {
                if (r != null) {
                    canvas.clipRect(r, Op.REPLACE);
                }
                ret = callback.onEffectApplied(canvas, childView, drawingTime);
            }

            canvas.restore();

            return ret;
        }

        private float transformChild(ViewGroup parentView, View childView, float radio, int offset, int currentScreen, int index, Canvas canvas) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            sMatrix.reset();
            Matrix matrix = sMatrix;

            ItemInfo itemInfo = (ItemInfo) childView.getTag();
            childMeasuredWidth = childMeasuredWidth / itemInfo.spanX;
            childMeasuredHeight = childMeasuredHeight / itemInfo.spanY;

            boolean tranformation = false;

            float degree = 0f;
            float begin = 0f;
            float end = 0f;
            float mid = 0f;

            CellLayout cellLayout = (CellLayout) parentView;
            if (itemInfo.screen == currentScreen) {
                if (radio >= 0) {
                    begin = (cellLayout.getCountX() - index - 1.0f) / cellLayout.getCountX();
                    end = (cellLayout.getCountX() - index - 0f) / cellLayout.getCountX();
                    mid = (end - begin) / 2.0f + begin;
                    if (radio >= begin && radio < mid) {
                        tranformation = true;
                        degree = -90f * (radio - begin) / (mid - begin);
                    } else if (radio < begin) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    begin = -(index + 0f) / cellLayout.getCountX();
                    end = -(index + 1.0f) / cellLayout.getCountX();
                    mid = (end - begin) / 2.0f + begin;
                    if (radio > mid && radio <= begin) {
                        tranformation = true;
                        degree = 90f * (radio - begin) / (mid - begin);
                    } else if (radio > begin) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                }
            } else {
                if (radio >= 0) {
                    begin = (cellLayout.getCountX() - index - 0.0f) / cellLayout.getCountX();
                    end = (cellLayout.getCountX() - index - 1.0f) / cellLayout.getCountX();
                    mid = (begin - end) / 2.0f + end;
                    if (radio <= mid && radio > end) {
                        tranformation = true;
                        degree = -90f + 90f * (radio - mid) / (end - mid);
                    } else if (radio < mid) {
                        degree = 0f;
                        tranformation = true;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    begin = -(index + 1.0f) / cellLayout.getCountX();
                    end = -(index + 0.0f) / cellLayout.getCountX();
                    mid = (begin - end) / 2.0f + end;
                    if (radio < end && radio >= mid) {
                        tranformation = true;
                        degree = 90f - 90f * (radio - mid) / (end - mid);
                    } else if (radio < end) {
                        tranformation = true;
                        degree = 90f;
                    } else {
                        tranformation = true;
                        degree = 0f;
                    }
                }
            }

            if (tranformation) {
                camera.save();
                camera.rotateY(degree);
                camera.getMatrix(matrix);
                camera.restore();

                matrix.preTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                matrix.postTranslate(childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);

                canvas.concat(matrix);
            }
            return degree;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }
    }

    private static final class Distort extends EffectInfoWithWidgetCache {

        Distort() {
            super(28, TYPE_DISTORT, "pref_key_transformation_distort", "@string/transformation_type_distort", R.drawable.effect_overview_simple_distort);
            if (Build.VERSION.SDK_INT >= 18) {
//                mEffcetOn = EFFECT_ON_DRAWER;
                mIsUsingWidgetCache = true;
            }
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            boolean ret = true;
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            if (itemInfo.spanX <= 1) {
                beforeTransformChild(canvas, childView.getContext(), childView.getLeft(), childView.getTop());
                float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX, itemInfo.cellY, canvas);
                ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft(), -childView.getTop(), null, degree, callback);
                return ret;
            } else {
                if (mIsUsingWidgetCache) {
                    cacheBitmap = getWidgetBitmap(childView, itemInfo.screen, childView.getWidth(), childView.getHeight(), 0, 0);
                }

                Rect dstRect = new Rect(0, 0, childView.getWidth() / itemInfo.spanX, childView.getHeight() / itemInfo.spanY);
                if (radioX >= 0) {

                    for (int i = 0; i < itemInfo.spanX; i++) {
                        for (int j = 0; j < itemInfo.spanY; j++) {
                            Rect srcRect = new Rect(dstRect.width() * i, dstRect.height() * j, dstRect.width() * (i + 1), dstRect.height() * (j + 1));
                            beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + srcRect.left, childView.getTop() + srcRect.top);
                            float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, itemInfo.cellY + j, canvas);
                            if (degree != 90) {
                                if (mIsUsingWidgetCache) {
                                    canvas.drawBitmap(cacheBitmap, srcRect, dstRect, null);
                                    canvas.restore();
                                } else {
                                    ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop() - childView.getHeight() / itemInfo.spanY * j,
                                            new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                                    childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * (j + 1)), degree, callback);
                                }
                            } else {
                                canvas.restore();
                            }
                        }
                    }
                } else {
                    for (int i = itemInfo.spanX - 1; i >= 0; i--) {
                        for (int j = itemInfo.spanY - 1; j >= 0; j--) {
                            Rect srcRect = new Rect(dstRect.width() * i, dstRect.height() * j, dstRect.width() * (i + 1), dstRect.height() * (j + 1));
                            beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + srcRect.left, childView.getTop() + srcRect.top);
                            float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, itemInfo.cellY + j, canvas);
                            if (degree != 90) {
                                if (mIsUsingWidgetCache) {
                                    canvas.drawBitmap(cacheBitmap, srcRect, dstRect, null);
                                    canvas.restore();
                                } else {
                                    ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop() - childView.getHeight() / itemInfo.spanY * j,
                                            new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                                    childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * (j + 1)), degree, callback);
                                }
                            } else {
                                canvas.restore();
                            }
                        }
                    }
                }
            }
            return ret;
        }

        private void beforeTransformChild(Canvas canvas, Context context, float translateX, float translateY) {
            canvas.save();
            if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
                canvas.setDrawFilter(sAntiAliesFilter);
            }
            canvas.translate(translateX, translateY);
        }

        private boolean afterTransformChild(Canvas canvas, View childView, long drawingTime, float translateX, float translateY, Rect r, float degree, Callback callback) {
            boolean ret = true;
            canvas.translate(translateX, translateY);

            if (r != null) {
                canvas.clipRect(r, Op.REPLACE);
            }
            ret = callback.onEffectApplied(canvas, childView, drawingTime);
            canvas.restore();

            return ret;
        }

        private float transformChild(ViewGroup parentView, View childView, float radio, int offset, int currentScreen, int indexX, int indexY, Canvas canvas) {
            if (radio == 0) {
                return 0;
            }

            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            sMatrix.reset();
            Matrix matrix = sMatrix;

            ItemInfo itemInfo = (ItemInfo) childView.getTag();
            childMeasuredWidth = childMeasuredWidth / itemInfo.spanX;
            childMeasuredHeight = childMeasuredHeight / itemInfo.spanY;

            boolean tranformation = false;

            float degree = 0f;
            float begin = 0f;
            float end = 0f;
            float mid = 0f;

            CellLayout cellLayout = (CellLayout) parentView;

            int cellLayoutCountX = cellLayout.getCountX();
            radio = radio / (cellLayoutCountX / (cellLayoutCountX + 1.0f));

            final float interval = 2.0f;
            float offsetY = interval * indexY / (cellLayout.getCountY() - 1.0f);

            if (itemInfo.screen == currentScreen) {
                if (radio > 0) {
                    begin = (cellLayoutCountX - indexX - 1.0f) / cellLayoutCountX;
                    end = (cellLayoutCountX - indexX - 0f + offsetY) / cellLayoutCountX;
                    mid = (end - begin) / 2.0f + begin;
                    if (radio >= begin && radio < mid) {
                        tranformation = true;
                        degree = -90f * (radio - begin) / (mid - begin);
                    } else if (radio < begin) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    begin = -(indexX + 0f) / cellLayoutCountX;
                    end = -(indexX + 1.0f + offsetY) / cellLayoutCountX;
                    mid = (end - begin) / 2.0f + begin;
                    if (radio > mid && radio <= begin) {
                        tranformation = true;
                        degree = 90f * (radio - begin) / (mid - begin);
                    } else if (radio > begin) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                }
            } else {
                offsetY = interval - offsetY;
                if (radio > 0) {
                    begin = (cellLayoutCountX - indexX - 0.0f + offsetY) / cellLayoutCountX;
                    end = (cellLayoutCountX - indexX - 1.0f) / cellLayoutCountX;
                    mid = (begin - end) / 2.0f + end;
                    if (radio <= mid && radio > end) {
                        tranformation = true;
                        degree = -90f + 90f * (radio - mid) / (end - mid);
                    } else if (radio < mid) {
                        degree = 0f;
                        tranformation = true;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    begin = -(indexX + 1.0f + offsetY) / cellLayoutCountX;
                    end = -(indexX + 0.0f) / cellLayoutCountX;
                    mid = (begin - end) / 2.0f + end;
                    if (radio < end && radio >= mid) {
                        tranformation = true;
                        degree = 90f - 90f * (radio - mid) / (end - mid);
                    } else if (radio < end) {
                        tranformation = true;
                        degree = 90f;
                    } else {
                        tranformation = true;
                        degree = 0f;
                    }
                }
            }

            if (tranformation) {
                camera.save();
                camera.rotateY(degree);
                camera.getMatrix(matrix);
                camera.restore();

                matrix.preTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                matrix.postTranslate(childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);

                canvas.concat(matrix);
            }
            return degree;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }
    }

    private static final class Turn extends EffectInfoWithWidgetCache {

        Turn() {
            super(29, TYPE_TURN, "pref_key_transformation_turn", "@string/transformation_type_turn", R.drawable.effect_overview_simple_turn);
            if (Build.VERSION.SDK_INT >= 14) {
                //mEffcetOn = EFFECT_ON_DRAWER;
                mIsUsingWidgetCache = true;
            }
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            boolean ret = true;
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            if (itemInfo.spanX <= 1) {
                beforeTransformChild(canvas, childView.getContext(), childView.getLeft(), childView.getTop());
                float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX, itemInfo.cellY, canvas);
                ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft(), -childView.getTop(), null, degree, callback);
                return ret;
            } else {
                if (mIsUsingWidgetCache) {
                    cacheBitmap = getWidgetBitmap(childView, itemInfo.screen, childView.getWidth(), childView.getHeight(), 0, 0);
                }

                Rect dstRect = new Rect(0, 0, childView.getWidth() / itemInfo.spanX, childView.getHeight() / itemInfo.spanY);
                if (radioX >= 0) {

                    for (int i = 0; i < itemInfo.spanX; i++) {
                        for (int j = 0; j < itemInfo.spanY; j++) {
                            Rect srcRect = new Rect(dstRect.width() * i, dstRect.height() * j, dstRect.width() * (i + 1), dstRect.height() * (j + 1));
                            beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + srcRect.left, childView.getTop() + srcRect.top);
                            float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, itemInfo.cellY + j, canvas);
                            if (degree != 90) {
                                if (mIsUsingWidgetCache) {
                                    canvas.drawBitmap(cacheBitmap, srcRect, dstRect, null);
                                    canvas.restore();
                                } else {
                                    ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop() - childView.getHeight() / itemInfo.spanY * j,
                                            new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                                    childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * (j + 1)), degree, callback);
                                }
                            } else {
                                canvas.restore();
                            }
                        }
                    }
                } else {
                    for (int i = itemInfo.spanX - 1; i >= 0; i--) {
                        for (int j = itemInfo.spanY - 1; j >= 0; j--) {
                            Rect srcRect = new Rect(dstRect.width() * i, dstRect.height() * j, dstRect.width() * (i + 1), dstRect.height() * (j + 1));
                            beforeTransformChild(canvas, childView.getContext(), childView.getLeft() + srcRect.left, childView.getTop() + srcRect.top);
                            float degree = transformChild(parentView, childView, radioX, offset, currentScreen, itemInfo.cellX + i, itemInfo.cellY + j, canvas);
                            if (degree != 90) {
                                if (mIsUsingWidgetCache) {
                                    canvas.drawBitmap(cacheBitmap, srcRect, dstRect, null);
                                    canvas.restore();
                                } else {
                                    ret = afterTransformChild(canvas, childView, drawingTime, -childView.getLeft() - childView.getWidth() / itemInfo.spanX * i, -childView.getTop() - childView.getHeight() / itemInfo.spanY * j,
                                            new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                                    childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                                    childView.getTop() + childView.getHeight() / itemInfo.spanY * (j + 1)), degree, callback);
                                }
                            } else {
                                canvas.restore();
                            }
                        }
                    }
                }
            }
            return ret;
        }

        private void beforeTransformChild(Canvas canvas, Context context, float translateX, float translateY) {
            canvas.save();
            if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
                canvas.setDrawFilter(sAntiAliesFilter);
            }
            canvas.translate(translateX, translateY);
        }

        private boolean afterTransformChild(Canvas canvas, View childView, long drawingTime, float translateX, float translateY, Rect r, float degree, Callback callback) {
            boolean ret = true;
            canvas.translate(translateX, translateY);

            if (r != null) {
                canvas.clipRect(r, Op.REPLACE);
            }
            if (degree != 90) {
                ret = callback.onEffectApplied(canvas, childView, drawingTime);
            }
            canvas.restore();

            return ret;
        }

        private float transformChild(ViewGroup parentView, View childView, float radio, int offset, int currentScreen, int indexX, int indexY, Canvas canvas) {
            if (radio == 0) { // || radio == 1 || radio == -1) {
                return 0;
            }

            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            sMatrix.reset();
            Matrix matrix = sMatrix;

            ItemInfo itemInfo = (ItemInfo) childView.getTag();
            childMeasuredWidth = childMeasuredWidth / itemInfo.spanX;
            childMeasuredHeight = childMeasuredHeight / itemInfo.spanY;

            boolean tranformation = false;

            float degree = 0f;

            CellLayout cellLayout = (CellLayout) parentView;

            final int cellLayoutCountX = cellLayout.getCountX();
            final int cellLayoutCountY = cellLayout.getCountY();

            final int count = cellLayoutCountX + cellLayoutCountY - 1;

            radio = radio / (count / (count + 1.0f));

            if (itemInfo.screen == currentScreen) {
                if (radio > 0) {
                    final int itemLine = cellLayoutCountX - indexX + indexY - 1;
                    float remainder = (count + 0) * radio - itemLine;
                    if (remainder > 0 && remainder < 1) {
                        tranformation = true;
                        degree = 90f * remainder;
                    } else if (remainder <= 0) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    final int itemLine = indexX + cellLayoutCountY - indexY - 1;
                    float remainder = -(count - 0) * radio - itemLine;
                    if (remainder > 0 && remainder < 1) {
                        tranformation = true;
                        degree = 90f * remainder;
                    } else if (remainder <= 0) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                }
            } else {
                if (radio > 0) {
                    final int itemLine = cellLayoutCountX - indexX + indexY - 1;
                    float remainder = (count + 0) * radio - itemLine;
                    if (remainder > 0f && remainder < 1f) {
                        tranformation = true;
                        degree = -90f * remainder;
                    } else if (remainder <= 0f) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                } else {
                    final int itemLine = indexX + cellLayoutCountY - indexY - 1;
                    float remainder = -(count + 0) * radio - itemLine;
                    if (remainder > 0f && remainder < 1f) {
                        tranformation = true;
                        degree = -90f * remainder;
                    } else if (remainder <= 0f) {
                        tranformation = true;
                        degree = 0f;
                    } else {
                        tranformation = true;
                        degree = 90f;
                    }
                }
            }

            if (tranformation) {

                float degreeZ = (float)Math.atan((double)childMeasuredWidth / (double)childMeasuredHeight) * 180 / (float)Math.PI;
                camera.save();
                camera.rotateZ(degreeZ);
                camera.rotateY(degree);
                camera.getMatrix(matrix);
                camera.restore();

                matrix.preTranslate(-childMeasuredWidth / 2.0F, -childMeasuredHeight / 2.0F);
                matrix.postTranslate(childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);

                canvas.concat(matrix);
                canvas.rotate(degreeZ, childMeasuredWidth / 2.0F, childMeasuredHeight / 2.0F);
            }
            return degree;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }
    }

    private static final class Extrusion extends EffectInfo {

        Extrusion() {
            super(30, TYPE_EXTRUSION, "pref_key_transformation_extrusion", "@string/transformation_type_extrusion", R.drawable.effect_overview_extrusion);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            final Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            if (radio > 0) { // left
                radio = 1.0f - radio;
                matrix.setScale(radio, 1, childMeasuredWidth, childMeasuredHeight / 2);
            } else { // right
                radio = 1.0f + radio;
                matrix.setScale(radio, 1, 0, childMeasuredHeight / 2);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            return null;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return true;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return false;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return true;
        }
    }

    private static final class Snake extends EffectInfo {

        Snake() {
            super(24, TYPE_SNAKE, "pref_key_transformation_snake", "@string/transformation_type_snake", R.drawable.effect_overview_simple_snake);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            int count = parentView.getChildCount();

            if (count == 0) {
                return null;
            }

            int cellX = itemInfo.cellX;
            int cellY = itemInfo.cellY;

            CellLayout cellLayout = (CellLayout) parentView;
            int countX = cellLayout.getCountX();
            int countY = cellLayout.getCountY();

            if (cellX < 0 || cellX >= countX || cellY < 0 || cellY >= countY) {
                return null;
            }

            // TODO isPortrait
            float distanceWidth = 0;
            float distanceHeight = 0;
            float alpha = 1.0f;

            sMatrix.reset();
            Matrix matrix = sMatrix;

            int countAll = countX * countY;
            int cellLongAxisDistance = cellLayout.getCellLongAxisDistance();
            int cellShortAxisDistance = cellLayout.getCellShortAxisDistance();

            boolean flag = true;
            int xSteps = 0;
            int ySteps = 0;
            if (radioX > 0) {
                ySteps += countY;
                radioX -= 1;
                flag = false;
            }

            int[][] snakeSteps = getSnakeSteps(countX, countY)[cellX][cellY];

            int index = (int) (Math.abs(radioX) * countAll);
            for (int i = 0; i < index; i++) {
                xSteps += snakeSteps[i][0];
                ySteps += snakeSteps[i][1];
            }

            if (!flag && countY % 2 == 1) {
                for (int i = 0; i < countX; i++) {
                    xSteps += snakeSteps[index + i][0];
                }
            }

            int position = (countY - 1 - cellY) * countX + (cellY % 2 == 0 ? cellX : (countX - 1 - cellX)) + 1;
            int offsetX;
            int offsetY;
            if (position + index == countAll) {
                if (flag) {
                    offsetX = 1;
                    offsetY = 0;
                } else {
                    if (countY % 2 == 0) {
                        xSteps++;
                    } else {
                        xSteps--;
                    }
                    ySteps--;
                    if (countY % 2 == 0) {
                        offsetX = -1;
                    } else {
                        offsetX = 1;
                    }
                    offsetY = 0;
                }
            } else {
                if (!flag && countY % 2 == 1) {
                    offsetX = snakeSteps[index + countX][0];
                } else {
                    offsetX = snakeSteps[index][0];
                }
                offsetY = snakeSteps[index][1];
            }

            if (cellY + ySteps >= countY || cellY + ySteps < 0) {
                alpha = 0f;
            }

            distanceWidth = xSteps * cellShortAxisDistance;
            distanceHeight = ySteps * cellLongAxisDistance;

            distanceWidth += offsetX * cellShortAxisDistance * (Math.abs(radioX) * countAll - index);
            distanceHeight += offsetY * cellLongAxisDistance * (Math.abs(radioX) * countAll - index);

            matrix.postTranslate(distanceWidth, distanceHeight);

            return apply(canvas, childView, drawingTime, isCellLayoutNeedAntiAlias() ? sAntiAliesFilter : null, matrix, alpha, callback);
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }

        private static int[][][][] sSnakeSteps;

        private static int[][][][] getSnakeSteps(int countX, int countY) {
            if (sSnakeSteps == null || sSnakeSteps.length != countX || sSnakeSteps[0].length != countY) {
                sSnakeSteps = new int[countX][countY][countX * countY + countX + 1][2];
                for (int i = 0; i < countY; i++) {
                    for (int j = 0; j < countX; j++) {
                        int index = 0;
                        if (i % 2 == 0) {
                            for (; index < countX - 1 - j; index++) {
                                sSnakeSteps[j][i][index][0] = 1;
                                sSnakeSteps[j][i][index][1] = 0;
                            }
                        } else {
                            for (; index < j; index++) {
                                sSnakeSteps[j][i][index][0] = -1;
                                sSnakeSteps[j][i][index][1] = 0;
                            }
                        }
                        for (int k = 0; k < sSnakeSteps[j][i].length - index; k++) {
                            if (k % countX == 0) {
                                sSnakeSteps[j][i][index + k][0] = 0;
                                sSnakeSteps[j][i][index + k][1] = -1;
                            } else {
                                if (i % 2 == 0) {
                                    sSnakeSteps[j][i][index + k][0] = (k / countX % 2 == 0) ? -1 : 1;
                                } else {
                                    sSnakeSteps[j][i][index + k][0] = (k / countX % 2 == 0) ? 1 : -1;
                                }
                                sSnakeSteps[j][i][index + k][1] = 0;
                            }
                        }
                    }
                }
            }
            return sSnakeSteps;
        }
    }

    private static final class Swirl extends EffectInfo {

        Swirl() {
            super(25, TYPE_SWIRL, "pref_key_transformation_swirl", "@string/transformation_type_swirl", R.drawable.effect_overview_simple_swirl);
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            int count = parentView.getChildCount();

            if (count == 0) {
                return null;
            }

            int cellX = itemInfo.cellX;
            int cellY = itemInfo.cellY;

            CellLayout cellLayout = (CellLayout) parentView;
            int countX = cellLayout.getCountX();
            int countY = cellLayout.getCountY();

            if (cellX < 0 || cellX >= countX || cellY < 0 || cellY >= countY) {
                return null;
            }

            // TODO isPortrait
            float scale = 0.0F;
            float distanceWidth = 0;
            float distanceHeight = 0;
            float alpha = 1.0f;

            sMatrix.reset();
            Matrix matrix = sMatrix;

            if (itemInfo.spanX > 1 || itemInfo.spanY > 1) {
                scale = 1.0F / Math.max(itemInfo.spanX, itemInfo.spanY);
            }

            int countAll = countX * countY;
            int cellLongAxisDistance = cellLayout.getCellLongAxisDistance();
            int cellShortAxisDistance = cellLayout.getCellShortAxisDistance();

            boolean flag = true;
            int xSteps = 0;
            int ySteps = 0;
            if (radioX > 0) {
                // ySteps += countY;
                radioX -= 1;
                flag = false;
            }

            int[][] swirlSteps = getSwirlSteps(countX, countY);
            int position = sSwirlIndex[cellX][cellY];

            int index = (int) (Math.abs(radioX) * countAll);
            for (int i = 0; i < index; i++) {
                xSteps += swirlSteps[(i + position) % countAll][0];
                ySteps += swirlSteps[(i + position) % countAll][1];
            }

            int offsetX;
            int offsetY;
            if (index + position == countAll - 1) {
                if (!flag) {
                    xSteps += swirlSteps[index + position][0] - 1;
                    ySteps += swirlSteps[index + position][1];
                    offsetX = 1;
                    offsetY = 0;
                } else {
                    alpha = 1.0F - (Math.abs(radioX) * countAll - index);
                    offsetX = 0;
                    offsetY = 0;
                }
            } else {
                offsetX = swirlSteps[(index + position) % countAll][0];
                offsetY = swirlSteps[(index + position) % countAll][1];
            }

            if (flag) {
                if (index + position >= countAll) {
                    alpha = 0f;
                }
            } else {
                if (index + position <= countAll - 2) {
                    alpha = 0f;
                }
            }

            if (scale != 0.0F) {
                if (index == 0) {
                    scale = (scale - 1.0F) * (Math.abs(radioX) * countAll - index) + 1.0F;
                }
                matrix.preScale(scale, scale);
            }

            distanceWidth = xSteps * cellShortAxisDistance;
            distanceHeight = ySteps * cellLongAxisDistance;

            distanceWidth += offsetX * cellShortAxisDistance * (Math.abs(radioX) * countAll - index);
            distanceHeight += offsetY * cellLongAxisDistance * (Math.abs(radioX) * countAll - index);

            matrix.postTranslate(distanceWidth, distanceHeight);

            return apply(canvas, childView, drawingTime, isCellLayoutNeedAntiAlias() ? sAntiAliesFilter : null, matrix, alpha, callback);
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }

        private static int[][] sSwirlSteps;

        private static int[][] sSwirlIndex;

        private static int[][] getSwirlSteps(int countX, int countY) {
            if (sSwirlSteps == null || sSwirlIndex.length != countX || sSwirlIndex[0].length != countY) {
                sSwirlSteps = new int[countX * countY][2];
                sSwirlIndex = new int[countX][countY];
                boolean[] reached = new boolean[countX * countY];
                int[][] dir = new int[][] {
                    {
                        1, 0
                    }, {
                        0, 1
                    }, {
                        -1, 0
                    }, {
                        0, -1
                    }
                };
                int currentDir = 0;
                int x = 0;
                int y = 0;
                reached[0] = true;
                int index = 0;
                sSwirlIndex[0][0] = 0;
                while (true) {
                    boolean found = false;
                    for (int i = 0; i < 4; i++) {
                        int xOffset = dir[(i + currentDir) % 4][0];
                        int yOffset = dir[(i + currentDir) % 4][1];
                        int xx = x + xOffset;
                        int yy = y + yOffset;
                        int position = yy * countX + xx;
                        if (xx < countX && xx >= 0 && yy < countY && yy >= 0 && !reached[position]) {
                            x = xx;
                            y = yy;
                            currentDir = (i + currentDir) % 4;
                            reached[position] = true;
                            sSwirlSteps[index][0] = xOffset;
                            sSwirlSteps[index][1] = yOffset;
                            sSwirlIndex[x][y] = ++index;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        sSwirlSteps[index][0] = -x;
                        sSwirlSteps[index][1] = -y;
                        index++;
                        break;
                    }
                }
            }
            return sSwirlSteps;
        }

    }

    private static final class Cylinder extends EffectInfoWithWidgetCache {

        private static final float MIN_ALPHA = 0.2f;

        // 动画开始时，渐变区域
        private static final float TRANSITION_POSITIVE = 0.01f;

        // 动画结束时，渐变区域
        private static final float TRANSITION_NEGATIVE = 0.5f;

        Cylinder() {
            super(27, TYPE_CYLINDER, "pref_key_transformation_cylinder", "@string/transformation_type_cylinder", R.drawable.effect_overview_cylinder);
            if (Build.VERSION.SDK_INT >= 18) {
                mIsUsingWidgetCache = true;
            }
            if (interpolator == null) {
                interpolator = new DecelerateInterpolator();
            }
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            boolean ret = true;
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
                canvas.setDrawFilter(sAntiAliesFilter);
            }

            if (itemInfo.spanX <= 1) {
                ret = transformChild(parentView, childView, radioX, itemInfo.cellX, 0, 1, canvas, drawingTime, callback, null, isPortrait, itemInfo.screen, currentScreen);
            } else {

                if (mIsUsingWidgetCache) {
                    cacheBitmap = getWidgetBitmap(childView, itemInfo.screen, childView.getWidth(), childView.getHeight(), 0, 0);
                }

                if (radioX >= 0) {
                    for (int i = 0; i < itemInfo.spanX; i++) {
                        ret = transformChild(parentView, childView, radioX, itemInfo.cellX + i, i, itemInfo.spanX, canvas, drawingTime, callback,
                                new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                        childView.getTop(),
                                        childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                        childView.getTop() + childView.getHeight()),
                                        isPortrait,
                                        itemInfo.screen,
                                        currentScreen);
                    }
                } else {
                    for (int i = itemInfo.spanX - 1; i >= 0; i--) {
                        ret = transformChild(parentView, childView, radioX, itemInfo.cellX + i, i, itemInfo.spanX, canvas, drawingTime, callback,
                                new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                        childView.getTop(),
                                        childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                        childView.getTop() + childView.getHeight()),
                                        isPortrait,
                                        itemInfo.screen,
                                        currentScreen);
                    }
                }
            }

            return ret;
        }

        private boolean mCurrentGradualed = false;

        private boolean mCurrentGradualing = false;

        private boolean mNextGradualing = false;

        private boolean mNextGradualed = false;

        // 用户手指是否在按下
        private boolean mTouching = false;

        // 用户手指按下动作被忽略了
        private boolean mTouchDownIgnored = false;

        private int mTouchDownScreenIndex = -1;

        @Override
        public void onTouchDown(boolean isScrolling) {
            super.onTouchDown(isScrolling);

            mTouching = true;

            if (isScrolling) {
                mTouchDownIgnored = true;
                return;
            }

            mCurrentGradualed = false;
            mCurrentGradualing = false;

            mNextGradualed = false;
            mNextGradualing = false;

            mTouchDownScreenIndex = -1;
        }

        @Override
        public void onTouchUpCancel(boolean isScrolling) {
            mTouching = false;

            if (mTouchDownIgnored && isScrolling) {
                mTouchDownIgnored = false;
                return;
            }

            mCurrentGradualed = false;
            mNextGradualed = false;
        }

        // 圆柱是否形成
        private boolean mCylinderform;

        private boolean transformChild(ViewGroup parentView, View childView, float radio, int cellX, int index, int spanX, Canvas canvas, long drawingTime, Callback callback, Rect r,
                boolean isPortrait, int screenIndex, int currentScreen) {
            final float parentMeasuredWidth = parentView.getMeasuredWidth();
            final float parentMeasuredHeight = parentView.getMeasuredHeight();
            final CellLayout cellLayout = (CellLayout) parentView;
            final Context context = cellLayout.getContext();
            int countX = cellLayout.getCountX();
            float gradualFactor = 1F;
            float alphalFactor = 1F;
            float t = mTouching ? TRANSITION_POSITIVE: TRANSITION_NEGATIVE;
            float radioABS = Math.abs(radio);

            if (mTouchDownScreenIndex == -1 && radioABS < t && screenIndex == currentScreen) {
                mTouchDownScreenIndex = screenIndex;
            }

            if (radioABS < t) {
//                float cellFactor = (mTouching || screenIndex == mTouchDownScreenIndex) ? 1 : (radio > 0 ? (countX - cellX) * 1f / countX : (cellX + 1f) / countX);

                if (!mCurrentGradualed) {
//                    gradualFactor = (float) (1 - Math.pow(1 - radioABS / t, 2)) * cellFactor * 0.9f;
                    mCurrentGradualing = true;
                } else if (!mNextGradualed) {
                    if (screenIndex != currentScreen) {
//                        gradualFactor = (float) (1 - Math.pow(1 - radioABS / t, 2)) * cellFactor * 0.9f;
                        mNextGradualing = true;
                    }
                }
            } else {
                if (radioABS < (1 - t)) {
                    if (mCurrentGradualing) {
                        mCurrentGradualed = true;
                        mCurrentGradualing = false;
                    } else if (mNextGradualing) {
                        mNextGradualed = true;
                        mNextGradualing = false;
                    }
                }

                if (radioABS > (1 - t) && (mCurrentGradualing || mNextGradualing)) {
//                    gradualFactor = 0.9f;
                    alphalFactor = -radioABS / t + 1 / t;
                }
            }

            sMatrix.reset();
            final Matrix matrix = sMatrix;
			float yDegree = -90 + 180f / (countX * 2) + cellX * 180f / countX
					- 180f * radio;
            float alpha = (float) (1 + Math.cos(ONE_DEGREE * yDegree));
            if (alpha > 1) {
                alpha = 1;
                if (radioABS > 0.9f) {
                    alpha = 10 * (1.0f - radioABS);
                }
            } else if (alpha < MIN_ALPHA * 2) {
                alpha = MIN_ALPHA * 2;
            }

            if (radioABS < 0.1f) {
                if (!RuntimeConfig.sLauncherInTouching || RuntimeConfig.sLauncherInTouching && !mCylinderform) {
                    gradualFactor = interpolator.getInterpolation(radioABS / 0.1f);
                }
                if (!RuntimeConfig.sLauncherInTouching)
                    mCylinderform = false;
            } else {
                if (alpha == 1.0f)
                    mCylinderform = true;
            }

//            float transFactor = Utils.isPortrait(context) ? (useHighScreenDensity() ? 3.74f : 2.5f) : (useHighScreenDensity() ? 6.65f : 4.5f);
            camera.save();
            camera.rotateY(yDegree * gradualFactor);
//            camera.translate(0, 0, -parentMeasuredWidth / transFactor);
            camera.translate(0, 0, -288f);

            camera.getMatrix(matrix);
            camera.restore();

            float scale = 5f / (context.getResources().getDisplayMetrics().widthPixels / 80f);
            matrix.preScale(scale, scale);

            canvas.save();
            canvas.scale(1 / scale / 2f, 1 / scale / 2f, parentMeasuredWidth / 2, parentMeasuredHeight / 2);

            canvas.translate(parentMeasuredWidth / 2, parentMeasuredHeight / 2);
            canvas.concat(matrix);
            canvas.translate(-parentMeasuredWidth / 2, -parentMeasuredHeight / 2);

            final float transX = parentMeasuredWidth / 2 - childView.getLeft() - childView.getMeasuredWidth() * (index * 2 + 1) / (2 * spanX);
            canvas.translate(transX * gradualFactor, 0);

//            float alpha = Math.abs(yDegree) > 90 ? (1f - Math.abs(((Math.abs(yDegree) % 180) - 90)) * (1f - MIN_ALPHA) / 90f) : 1f;

            if (alpha < 1.0F && !mIsUsingWidgetCache) {
                final int cl = childView.getLeft();
                final int ct = childView.getTop();
                final int cr = childView.getRight();
                final int cb = childView.getBottom();
                //TODO 4.3中此方法导致速度巨慢，暂时屏蔽
                canvas.saveLayerAlpha(cl, ct, cr, cb, (int) (255 * alpha * gradualFactor * alphalFactor), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            }

            boolean ret = true;

            if (mIsUsingWidgetCache && r != null && cacheBitmap != null) {
                Rect src = new Rect(r);
                src.offset(-childView.getLeft(), -childView.getTop());
                canvas.drawBitmap(cacheBitmap, src, r, null);
            } else {
                if (r != null) {
                    canvas.clipRect(r, Op.REPLACE);
                }
                ret = callback.onEffectApplied(canvas, childView, drawingTime);
            }

            if (alpha < 1.0F && !mIsUsingWidgetCache) {
                canvas.restore();
            }
            canvas.restore();
            return ret;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean drawChildrenOrderByMoveDirection() {
            return true;
        }

        public boolean useDefaultScroller() {
            return true;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }
    }
    private static final float ONE_DEGREE = 0.01745329F;

    private static final class Sphere extends EffectInfoWithWidgetCache {

        private static final float MAX_X_DEGREE = 50f;

        private static final float MIN_ALPHA = 0.05f;

        // 动画开始时，渐变区域
        private static final float TRANSITION_POSITIVE = 0.01f;

        // 动画结束时，渐变区域
        private static final float TRANSITION_NEGATIVE = 0.2f;

        // Y方向滑动比例是否需要重置
        private boolean mRadioYNeedInit;

        // Y方向之前的滑动比例
        private float mLastRadioY;

        private int mTouchDownScreenIndex = -1;

        private boolean mCurrentGradualed = false;

        private boolean mCurrentGradualing = false;

        private boolean mNextGradualing = false;

        private boolean mNextGradualed = false;

        // 用户手指是否在按下
        private boolean mTouching = false;

        // 用户手指按下动作被忽略了
        private boolean mTouchDownIgnored = false;

        // 球是否形成
        private boolean mSphereform;

        Sphere() {
            super(27, TYPE_SPHERE, "pref_key_transformation_sphere", "@string/transformation_type_sphere", R.drawable.effect_overview_sphere);
            if (Build.VERSION.SDK_INT >= 18) {
                //mEffcetOn = EFFECT_ON_DRAWER;
                mIsUsingWidgetCache = true;
            }
            if (interpolator == null) {
                interpolator = new DecelerateInterpolator();
            }
        }

        @Override
        public boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation, float radio, int offset, int currentScreen, boolean isPortrait) {
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            Matrix matrix = childTransformation.getMatrix();

            if (isPortrait) {
                matrix.postTranslate(childMeasuredWidth * radio + offset, 0.0F);
            } else {
                matrix.postTranslate(0.0F, childMeasuredHeight * radio + offset);
            }

            childTransformation.setTransformationType(Transformation.TYPE_MATRIX);

            return true;
        }

        @Override
        public Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime, Callback callback, float radioX, int offset, float radioY,
                int currentScreen, boolean isPortrait) {
            boolean ret = true;
            ItemInfo itemInfo = (ItemInfo) childView.getTag();

            if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
                canvas.setDrawFilter(sAntiAliesFilter);
            }

            if (itemInfo.spanX <= 1) {
                ret = transformChild(parentView, childView, radioX, radioY,
                        itemInfo.cellX, 0, 1,
                        itemInfo.cellY, 0, 1,
                        canvas, drawingTime, callback, null, isPortrait, itemInfo.screen, currentScreen);
            } else {

                if (mIsUsingWidgetCache) {
                    cacheBitmap = getWidgetBitmap(childView, itemInfo.screen, childView.getWidth(), childView.getHeight(), 0, 0);
                }

                if (radioX >= 0) {
                    for (int i = 0; i < itemInfo.spanX; i++) {
                        for (int j = 0; j < itemInfo.spanY; j++) {
                            ret = transformChild(parentView, childView, radioX, radioY,
                                    itemInfo.cellX + i, i, itemInfo.spanX,
                                    itemInfo.cellY + j, j, itemInfo.spanY,
                                    canvas, drawingTime, callback,
                                    new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                            childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                            childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                            childView.getTop() + childView.getHeight() / itemInfo.spanY * j + childView.getHeight() / itemInfo.spanY),
                                    isPortrait, itemInfo.screen, currentScreen);
                        }
                    }
                } else {
                    for (int i = itemInfo.spanX - 1; i >= 0; i--) {
                        for (int j = itemInfo.spanY - 1; j >= 0; j--) {
                            ret = transformChild(parentView, childView, radioX, radioY,
                                    itemInfo.cellX + i, i, itemInfo.spanX,
                                    itemInfo.cellY + j, j, itemInfo.spanY,
                                    canvas, drawingTime, callback,
                                    new Rect(childView.getLeft() + childView.getWidth() / itemInfo.spanX * i,
                                            childView.getTop() + childView.getHeight() / itemInfo.spanY * j,
                                            childView.getLeft() + childView.getWidth() / itemInfo.spanX * i + childView.getWidth() / itemInfo.spanX,
                                            childView.getTop() + childView.getHeight() / itemInfo.spanY * j + childView.getHeight() / itemInfo.spanY),
                                    isPortrait, itemInfo.screen, currentScreen);
                        }
                    }
                }
            }

            return ret;
        }

        @Override
        public void onTouchDown(boolean isScrolling) {
            super.onTouchDown(isScrolling);

            mTouching = true;
            if (isScrolling) {
                mTouchDownIgnored = true;
                return;
            }

            mCurrentGradualed = false;
            mCurrentGradualing = false;

            mNextGradualed = false;
            mNextGradualing = false;

            mRadioYNeedInit = true;
            mTouchDownScreenIndex = -1;
        }

        @Override
        public void onTouchUpCancel(boolean isScrolling) {
            mTouching = false;
            if (mTouchDownIgnored && isScrolling) {
                mTouchDownIgnored = false;
                return;
            }

            mRadioYNeedInit = false;

            mCurrentGradualed = false;
            mNextGradualed = false;

            mTouchDownIgnored = false;
        }

        private boolean transformChild(ViewGroup parentView, View childView, float radio, float radioY, int cellX, int indexX, int spanX, int cellY, int indexY, int spanY, Canvas canvas, long drawingTime,
                Callback callback, Rect r, boolean isPortrait, int screenIndex, int currentScreen) {
            final float parentMeasuredWidth = parentView.getMeasuredWidth();
            final float parentMeasuredHeight = parentView.getMeasuredHeight();
            final CellLayout cellLayout = (CellLayout) parentView;
            final Context context = cellLayout.getContext();
            sMatrix.reset();
            final Matrix matrix = sMatrix;

            int countX = cellLayout.getCountX();
            int countY = cellLayout.getCountY();
            cellY = countY - 1 - cellY;

            float gradualFactor = 1F;
            float alphalFactor = 1F;
            float t = mTouching ? TRANSITION_POSITIVE: TRANSITION_NEGATIVE;
            float radioAbs = Math.abs(radio);

            float radioYDiff = 0f;
            if (mTouchDownScreenIndex == -1 && radioAbs < t && screenIndex == currentScreen) {
                mTouchDownScreenIndex = screenIndex;
            }

            if (mRadioYNeedInit) {
                mLastRadioY = radioY;
                mRadioYNeedInit = false;
            } else {
                radioYDiff = radioY - mLastRadioY;
            }

            if (radioAbs < t) {

                if (!mCurrentGradualed) {
                    mCurrentGradualing = true;
                } else if (!mNextGradualed) {
                    if (screenIndex != currentScreen) {
                        mNextGradualing = true;
                    }
                }
            } else {
                if (radioAbs < (1 - t)) {
                    if (mCurrentGradualing) {
                        mCurrentGradualed = true;
                        mCurrentGradualing = false;
                    } else if (mNextGradualing) {
                        mNextGradualed = true;
                        mNextGradualing = false;
                    }
                }

                if (radioAbs > (1 - t) && (mCurrentGradualing || mNextGradualing)) {
                    alphalFactor = -radioAbs / t + 1 / t;
                }
            }

            float yDegree = -90 + 180f / (countX * 2) + cellX * 180f / countX - 180f * radio;
            float xDegree = MAX_X_DEGREE - 90 + (180F - MAX_X_DEGREE * 2) * cellY / (countY - 1);
            float alpha = (float) (1 + Math.cos(ONE_DEGREE * yDegree));
            float preXDegree = radioYDiff * 360;

            if (alpha > 1) {
                alpha = 1;
                if (radioAbs > 0.9f) {
                    alpha = 10 * (1.0f - radioAbs);
                }
            } else if (alpha < MIN_ALPHA * 2) {
                alpha = MIN_ALPHA * 2;
            }

            if (radioAbs < 0.1f) {
                if (!RuntimeConfig.sLauncherInTouching || RuntimeConfig.sLauncherInTouching && !mSphereform) {
                    gradualFactor = interpolator.getInterpolation(radioAbs / 0.1f);
                }
                if (!RuntimeConfig.sLauncherInTouching)
                    mSphereform = false;
            } else {
                if (alpha == 1.0f)
                    mSphereform = true;
            }

            if (Math.abs(preXDegree) > 90F) {
                preXDegree = preXDegree > 0 ? 90f : -90f;
			}

            camera.save();
            camera.rotateX(-preXDegree * gradualFactor);
            camera.rotateY(yDegree * gradualFactor);
            camera.rotateX(xDegree * gradualFactor);
            camera.translate(0, 0, -288);
            camera.getMatrix(matrix);
            camera.restore();

            float scale = 5f / (context.getResources().getDisplayMetrics().widthPixels / 80f);
            matrix.preScale(scale, scale);

            canvas.save();
            canvas.scale(1 / scale / 2f, 1 / scale / 2f, parentMeasuredWidth / 2, parentMeasuredHeight / 2);
            canvas.translate(parentMeasuredWidth / 2, parentMeasuredHeight / 2);
            canvas.concat(matrix);
            canvas.translate(-parentMeasuredWidth / 2, -parentMeasuredHeight / 2);

            final float transX = parentMeasuredWidth / 2 - childView.getLeft() - childView.getMeasuredWidth() * (indexX * 2 + 1) / (2 * spanX);
            final float transY = parentMeasuredHeight / 2 - childView.getTop() - childView.getMeasuredHeight() * (indexY * 2 + 1) / (2 * spanY);
            canvas.translate(transX * gradualFactor, transY * gradualFactor);

            if (alpha < 1.0F && !mIsUsingWidgetCache) {
                if (screenIndex != currentScreen) {
                    float n = (float) (Math.sin(Math.toRadians(180f * Math.abs(radio))));
                    alphalFactor *= n;
                }

                final int cl = childView.getLeft();
                final int ct = childView.getTop();
                final int cr = childView.getRight();
                final int cb = childView.getBottom();

                canvas.saveLayerAlpha(cl, ct, cr, cb, (int) (255 * alpha * gradualFactor * alphalFactor), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG); //TODO 4.3中此方法导致速度巨慢，暂时屏蔽
            }

            boolean ret = true;

            if (mIsUsingWidgetCache && r != null && cacheBitmap != null) {
                Rect src = new Rect(r);
                src.offset(-childView.getLeft(), -childView.getTop());
                canvas.drawBitmap(cacheBitmap, src, r, null);
            } else {
                if (r != null) {
                    canvas.clipRect(r, Op.REPLACE);
                }
                ret = callback.onEffectApplied(canvas, childView, drawingTime);
            }

            if (alpha < 1.0F && !mIsUsingWidgetCache) {
                canvas.restore();
            }
            canvas.restore();
            return ret;
        }

        @Override
        public boolean isWorkspaceNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean isCellLayoutNeedAntiAlias() {
            return false;
        }

        @Override
        public boolean needInvalidateHardwareAccelerated() {
            return true;
        }

        @Override
        public boolean canEnableWholePageDrawingCache() {
            return false;
        }

        @Override
        public boolean drawChildrenOrderByMoveDirection() {
            return true;
        }

        public boolean useDefaultScroller() {
            return true;
        }

        @Override
        public boolean isScreenFused() {
            return true;
        }

        @Override
        public void onRefresh() {
            onTouchDown(false);
        }
    }

    /**********************
     * 一些以前写好的特效但是没有使用的
     ***************/

    /*
     * allEffects.add(new EffectInfo(2, 6,
     * "pref_key_transformation_cube_inside",
     * "@string/transformation_type_cube_inside") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix(); camera.save(); if
     * (isPortrait) { camera.rotateY(60.0F * radio); } else {
     * camera.rotateX(-60.0F * radio); } camera.getMatrix(matrix);
     * camera.restore(); matrix.preTranslate(-childMeasuredWidth / 2.0F,
     * -childMeasuredHeight / 2.0F);
     * matrix.postTranslate(childMeasuredWidth / 2.0F,
     * childMeasuredHeight / 2.0F); if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_MATRIX); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(6, 3,
     * "pref_key_transformation_left_page",
     * "@string/transformation_type_leftpage") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio));
     * camera.save(); if (isPortrait) {
     * camera.translate(-childMeasuredWidth / 2.0F * Math.abs(radio) /
     * 3.0F, childMeasuredHeight / 2.0F, -childMeasuredWidth / 2.0F *
     * radio); camera.rotateY(-30.0F * radio); } else {
     * camera.translate(-childMeasuredWidth / 2.0F, childMeasuredHeight
     * / 2.0F Math.abs(radio) / 3.0F, -childMeasuredHeight / 2.0F *
     * radio); camera.rotateX(30.0F * radio); }
     * camera.getMatrix(matrix); camera.restore(); if (isPortrait) {
     * matrix.postTranslate(childMeasuredWidth * radio,
     * childMeasuredHeight / 2.0F); } else {
     * matrix.postTranslate(childMeasuredWidth / 2.0F,
     * childMeasuredHeight * radio); } if (offset != 0) { if
     * (isPortrait) { matrix.postTranslate(offset, 0); } else {
     * matrix.postTranslate(0, offset); } }
     * childTransformation.setTransformationType
     * (Transformation.TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(10, 9,
     * "pref_key_transformation_page_slide_up",
     * "@string/transformation_type_page_slide_up") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio)); if
     * (isPortrait) { matrix.postTranslate(0.0F, -Math.abs(radio) *
     * childMeasuredHeight); } else {
     * matrix.postTranslate(-Math.abs(radio) * childMeasuredWidth,
     * 0.0F); } if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(11, 10,
     * "pref_key_transformation_vertical_scrolling",
     * "@string/transformation_type_vertical_scrolling") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio));
     * matrix.postTranslate(childMeasuredWidth * radio, radio *
     * childMeasuredHeight); if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(12, 11,
     * "pref_key_transformation_page_fade",
     * "@string/transformation_type_page_fade") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio)); if
     * (isPortrait) { matrix.postTranslate(childMeasuredWidth * radio,
     * 0.0F); } else { matrix.postTranslate(0.0F, childMeasuredHeight *
     * radio); } if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(13, 12,
     * "pref_key_transformation_page_zoom",
     * "@string/transformation_type_page_zoom") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio));
     * matrix.preScale(1 + radio, 1 + radio);
     * matrix.preTranslate(-childMeasuredWidth / 2.0F,
     * -childMeasuredHeight / 2.0F); if (isPortrait) {
     * matrix.postTranslate( childMeasuredWidth / 2.0F +
     * childMeasuredWidth * radio, childMeasuredHeight / 2.0F); } else {
     * matrix.postTranslate(childMeasuredWidth / 2.0F,
     * childMeasuredHeight / 2.0F + childMeasuredHeight * radio); } if
     * (offset != 0) { if (isPortrait) { matrix.postTranslate(offset,
     * 0); } else { matrix.postTranslate(0, offset); } }
     * childTransformation
     * .setTransformationType(Transformation.TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(15, 14,
     * "pref_key_transformation_carousel",
     * "@string/transformation_type_carousel") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio)); if (radio
     * <= 0) { float scale = 0.5F * (1.0F + radio) + 0.5F;
     * matrix.preScale(scale, scale);
     * matrix.preTranslate(-childMeasuredWidth / 2.0F,
     * -childMeasuredHeight / 2.0F); if (isPortrait) {
     * matrix.postTranslate(childMeasuredWidth / 2.0F +
     * childMeasuredWidth radio / 2.0F, childMeasuredHeight / 2.0F); }
     * else { matrix.postTranslate(childMeasuredWidth / 2.0F,
     * childMeasuredHeight / 2.0F + childMeasuredHeight * radio / 2.0F);
     * } } else { float scale = 1.0F + 3.0F * radio;
     * matrix.preScale(scale, scale); if (isPortrait) {
     * matrix.preTranslate(0.0F, -childMeasuredHeight / 2.0F);
     * matrix.postTranslate(2 * radio * childMeasuredWidth,
     * childMeasuredHeight / 2.0F); } else {
     * matrix.preTranslate(-childMeasuredWidth / 2.0F, 0.0F);
     * matrix.postTranslate(childMeasuredWidth / 2.0F, 2 * radio
     * childMeasuredHeight); } } if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(17, 16,
     * "pref_key_transformation_icon_scatter",
     * "@string/transformation_type_icon_scatter") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { Matrix matrix =
     * childTransformation.getMatrix(); if (offset != 0) { if
     * (isPortrait) { matrix.postTranslate(offset, 0); } else {
     * matrix.postTranslate(0, offset); } }
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredHeight = childView.getMeasuredHeight(); float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * parentMeasuredWidth = parentView.getMeasuredWidth(); float
     * parentMeasuredHeight = parentView.getMeasuredHeight(); float
     * distanceWidth; float distanceHeight; Matrix matrix =
     * childTransformation.getMatrix(); ItemInfo itemInfo = (ItemInfo)
     * childView.getTag(); if (isPortrait) { if (radio <= 0.0F) {
     * distanceWidth = parentView.getLeft() - childView.getLeft(); }
     * else { distanceWidth = parentView.getRight() -
     * childView.getRight(); } distanceHeight = parentView.getTop() +
     * parentMeasuredHeight / 2.0F - childView.getTop() -
     * childMeasuredHeight / 2.0F; } else { if (radio <= 0.0F) {
     * distanceHeight = parentView.getTop() - childView.getTop(); } else
     * { distanceHeight = parentView.getBottom() -
     * childView.getBottom(); } distanceWidth = parentView.getLeft() +
     * parentMeasuredWidth / 2.0F - childView.getLeft() -
     * childMeasuredWidth / 2.0F; } if (isPortrait) { distanceWidth -=
     * itemInfo.screen * parentMeasuredWidth; } else { distanceHeight -=
     * itemInfo.screen * parentMeasuredHeight; }
     * matrix.postTranslate(-Math.abs(radio) * distanceWidth,
     * -Math.abs(radio) distanceHeight);
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; } });
     */

    /*
     * allEffects.add(new EffectInfo(19, 18,
     * "pref_key_transformation_wave",
     * "@string/transformation_type_wave") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix();
     * childTransformation.setAlpha(1.0F - Math.abs(radio)); if
     * (isPortrait) { matrix.postTranslate(childMeasuredWidth * radio,
     * 0.0F); } else { matrix.postTranslate(0.0F, childMeasuredHeight *
     * radio); } if (offset != 0) { if (isPortrait) {
     * matrix.postTranslate(offset, 0); } else { matrix.postTranslate(0,
     * offset); } }
     * childTransformation.setTransformationType(Transformation
     * .TYPE_BOTH); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix(); camera.save(); if
     * (isPortrait) { camera.rotateY(180.0F * radio); } else {
     * camera.rotateX(-180.0F * radio); } camera.getMatrix(matrix);
     * camera.restore(); matrix.preTranslate(-childMeasuredWidth / 2.0F,
     * -childMeasuredHeight / 2.0F);
     * matrix.postTranslate(childMeasuredWidth / 2.0F,
     * childMeasuredHeight / 2.0F);
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; } });
     */

    /*
     * allEffects.add(new EffectInfo(21, 20,
     * "pref_key_transformation_stairs_down_right",
     * "@string/transformation_type_stairs_down_right") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix(); if (radio < 0) { float
     * scale = 0.4F * (1.0F + radio) + 0.6F; matrix.preScale(scale,
     * scale); if (isPortrait) { matrix.preTranslate(0,
     * -childMeasuredHeight / 2.0F); matrix.postTranslate(0,
     * childMeasuredHeight / 2.0F); } else {
     * matrix.preTranslate(-childMeasuredWidth / 2.0F, 0);
     * matrix.postTranslate(childMeasuredWidth / 2.0F, 0); } } else {
     * float scale = 0.2F * radio + 1.0F; matrix.preScale(scale, scale);
     * matrix.preTranslate(-childMeasuredWidth, -childMeasuredHeight /
     * 2.0F); matrix.postTranslate(childMeasuredWidth,
     * childMeasuredHeight / 2.0F); } if (offset != 0) { if (isPortrait)
     * { matrix.postTranslate(offset, 0); } else {
     * matrix.postTranslate(0, offset); } }
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(22, 21,
     * "pref_key_transformation_stairs_down_left",
     * "@string/transformation_type_stairs_down_left") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix(); if (radio < 0) { float
     * scale = -0.2F * radio + 1.0F; matrix.preScale(scale, scale); if
     * (isPortrait) { matrix.preTranslate(0, -childMeasuredHeight /
     * 2.0F); matrix.postTranslate(0, childMeasuredHeight / 2.0F); }
     * else { matrix.preTranslate(-childMeasuredWidth / 2.0F, 0);
     * matrix.postTranslate(childMeasuredWidth / 2.0F, 0); } } else {
     * float scale = 0.4F * (1.0F - radio) + 0.6F;
     * matrix.preScale(scale, scale);
     * matrix.preTranslate(-childMeasuredWidth, -childMeasuredHeight /
     * 2.0F); matrix.postTranslate(childMeasuredWidth,
     * childMeasuredHeight / 2.0F); } if (offset != 0) { if (isPortrait)
     * { matrix.postTranslate(offset, 0); } else {
     * matrix.postTranslate(0, offset); } }
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

    /*
     * allEffects.add(new EffectInfo(23, 22,
     * "pref_key_transformation_squash",
     * "string/transformation_type_squash") {
     * @Override public boolean
     * getWorkspaceChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { float
     * childMeasuredWidth = childView.getMeasuredWidth(); float
     * childMeasuredHeight = childView.getMeasuredHeight(); Matrix
     * matrix = childTransformation.getMatrix(); if (radio < 0) {
     * matrix.preScale(1 + radio, 1); } else if (radio > 0) { if
     * (isPortrait) { matrix.preTranslate(childMeasuredWidth * radio,
     * 0.0F); } else { matrix.preTranslate(0.0F, childMeasuredHeight *
     * radio); } matrix.preScale(1 - radio, 1); } if (offset != 0) { if
     * (isPortrait) { matrix.postTranslate(offset, 0); } else {
     * matrix.postTranslate(0, offset); } }
     * childTransformation.setTransformationType
     * (Transformation.TYPE_MATRIX); return true; }
     * @Override public boolean
     * getCellLayoutChildStaticTransformation(ViewGroup parentView, View
     * childView, Transformation childTransformation, float radio, int
     * offset, int currentScreen, boolean isPortrait) { return false; }
     * });
     */

}
