package net.ds.effect.utils;

/**
 * 桌面运行时配置
 * @author songzhaochun
 *
 */
public class RuntimeConfig {

    /**
     * 对于WVGA一下机型，不会启用抗锯齿（反走样）
     * 对于一些特殊机型，考虑到效果和效率，可以开启抗锯齿，这个是适配的值
     * 特效：扇面
     */
    public static boolean sEnableCellLayoutFanEffectAntiAlias = false;

    /**
     * 桌面主界面正处于touch状态，建议一切非UI任务暂停
     */
    public static boolean sLauncherInTouching = false;

    /**
     * 桌面主界面正处于滑屏动画状态，建议一切非UI任务暂停
     */
    public static boolean sLauncherInScrolling = false;

    /**
     * 如果发生横竖屏切换，则刷新CellLayout的DrawingCache
     */
    public static boolean sDirtyDrawingCacheByOritationChange = false;
    /**
     * 当使用智能整理后的第一次打开抽屉文件夹，刷新DrawingCache避免背景文件夹上显示数字
     */
    public static boolean sDirtyDrawingCacheByResettleDrawer = false;
    /**
     * 当icon角标有变化，刷新DrawingCache避免icon显示角标
     */
    public static boolean sDirtyDrawingCacheByTipUpdated = false;

    public static boolean sLowerPerformance = false;

    public static boolean isPortrait = true;
}