package org.ikags.core;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

/**
 * I.K.A Engine<BR>
 * Def类，基本设置,定义设置。<BR>
 * 更新:删除与config.ini的关联,键值统一处理成虚拟键值
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class Def
{

    // *************************基本设置
    /**
     * 屏幕主线程运行sleep时间
     */
    public static byte SYSTEM_DELAY = 80;
    /**
     * 系统屏幕宽度Width
     */
    public static int SYSTEM_SW = 240;
    /**
     * 系统屏幕高度Height
     */
    public static int SYSTEM_SH = 320;
    /**
     * 版本号码默认0.0.1,调试版本增加最后位,发布版本增加中间位,正式版本增加第一位
     */
    public static String Version = "0.0.1";
    /**
     * 是否处于Debug状态,0为不处于,1为处于
     */
    public static int DebugMode = 0;
    // 默认为NOKIA手机按键
    /**
     * 按键 上 的键值,虚拟键值
     */
    public static final byte KEY_UP = -1;
    /**
     * 按键 下 的键值,虚拟键值
     */
    public static final byte KEY_DOWN = -2;
    /**
     * 按键 左 的键值,虚拟键值
     */
    public static final byte KEY_LEFT = -3;
    /**
     * 按键 右 的键值,虚拟键值
     */
    public static final byte KEY_RIGHT = -4;
    /**
     * 按键 中 的键值,虚拟键值
     */
    public static final byte KEY_MIDDLE = -5;
    /**
     * 按键 左软键 的键值,虚拟键值
     */
    public static final byte SOFTKEY_LEFT = -6;
    /**
     * 按键 右软键 的键值,虚拟键值
     */
    public static final byte SOFTKEY_RIGHT = -7;
    /**
     * 数字按键0的键值,一般不变
     */
    public static final byte KEY_0 = 48;
    /**
     * 数字按键1的键值,一般不变
     */
    public static final byte KEY_1 = 49;
    /**
     * 数字按键2的键值,一般不变
     */
    public static final byte KEY_2 = 50;
    /**
     * 数字按键3的键值,一般不变
     */
    public static final byte KEY_3 = 51;
    /**
     * 数字按键4的键值,一般不变
     */
    public static final byte KEY_4 = 52;
    /**
     * 数字按键5的键值,一般不变
     */
    public static final byte KEY_5 = 53;
    /**
     * 数字按键6的键值,一般不变
     */
    public static final byte KEY_6 = 54;
    /**
     * 数字按键7的键值,一般不变
     */
    public static final byte KEY_7 = 55;
    /**
     * 数字按键8的键值,一般不变
     */
    public static final byte KEY_8 = 56;
    /**
     * 数字按键9的键值,一般不变
     */
    public static final byte KEY_9 = 57;
    /**
     * 数字按键*的键值,一般不变
     */
    public static final byte KEY_STAR = 42;// "*" 键
    /**
     * 数字按键#的键值,一般不变
     */
    public static final byte KEY_POUND = 35;// "#" 键
    /**
     * 数字按键clear的键值,一般不变
     */
    public static final byte KEY_CLEAR = -8;// "clear" 键
    /**
     * 数字按键CLOSE的键值,一般不变
     */
    public static final byte KEY_CLOSE = -11;// "CLOSE" 键
    /**
     * 按键状态值,用于按键延迟判定.
     */
    public static int keyState = 0;// 按键延迟判定
    /**
     * 系统默认字体初始化设定
     */
    public static Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public static Font font1 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public static Font font2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    public static Font font3 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
    /**
     * 系统默认字体高度
     */
    public static final int fontHeight = font.getHeight();
    /**
     * 系统默认字体宽度
     */
    public static final int fontWidth = font.charWidth('坦');
    // 字符方位设置
    /**
     * 字符(图片)方位位置设定,左上
     */
    public static final byte TOP_LEFT = 20; // 左上
    /**
     * 字符(图片)方位位置设定,右上
     */
    public static final byte TOP_RIGHT = 24; // 右上
    /**
     * 字符(图片)方位位置设定,中上
     */
    public static final byte TOP_CENTER = 17; // 中上
    /**
     * 字符(图片)方位位置设定,正中心
     */
    public static final byte CENTER = 3; // 正中心
    /**
     * 字符(图片)方位位置设定,中下
     */
    public static final byte BOTTOM_CENTER = 33; // 中下
    /**
     * 字符(图片)方位位置设定,左下
     */
    public static final byte BOTTOM_LEFT = 36; // 左下
    /**
     * 字符(图片)方位位置设定,右下
     */
    public static final byte BOTTOM_RIGHT = 40; // 右下
    // 游戏声音设置
    /**
     * 系统声音开关,默认开
     */
    public static boolean SoundOn = true;
    // 状态设置
    /**
     * 缓存上一级状态参数
     */
    public static byte LastState = -1;
    /**
     * 程序主状态参数
     */
    public static byte MainState = -1;
    /**
     * 程序LOGO,主界面状态(非菜单)状态(默认状态,不推荐使用)
     */
    public static final byte STATE_LOGO = -1;
    /**
     * 程序声音选择界面状态(默认状态,不推荐使用)
     */
    public static final byte STATE_SOUND_SECLET = 1;
    /**
     * 程序主选单界面状态(默认状态,不推荐使用)
     */
    public static final byte STATE_MAINMENU = 2; // 游戏主菜单
    /**
     * 程序运行界面状态(默认状态,不推荐使用)
     */
    public static final byte STATE_RUN = 3; // 游戏运行画面
    /**
     * 程序运行界面弹出菜单状态(默认状态,不推荐使用)
     */
    public static final byte STATE_RUNMENU = 4; // 游戏运行中的菜单画面
    /**
     * 程序设置界面状态(默认状态,不推荐使用)
     */
    public static final byte STATE_SET = 5; // 游戏设置
    /**
     * 程序帮助界面状态(默认状态,不推荐使用)
     */
    public static final byte STATE_HELP = 6; // 游戏帮助
    /**
     * 程序双缓冲图片声明,用于屏幕旋转等
     */
    public static Image bufferImage = null;// 双缓冲旋转
    public static int DEFAULT_SYSTEM_SW = 0;
    public static int DEFAULT_SYSTEM_SH = 0;
    // 屏幕旋转后的方向
    /**
     * 屏幕旋转参数
     */
    public static byte TheWay = 0;
    /**
     * 屏幕旋转方向:顶部在上方
     */
    public static final byte TOPinNORMAL = 0;// 顶部在上方
    /**
     * 屏幕旋转方向:顶部在左边
     */
    public static final byte TOPinLEFT = 1;// 顶部在左边
    /**
     * 屏幕旋转方向:顶部在右边
     */
    public static final byte TOPinRIGHT = 2;// 顶部在右边
    /**
     * 屏幕旋转方向:顶部在底部
     */
    public static final byte TOPinBOTTOM = 3;// 顶部在底部
    /**
     * PSP 开发PSPKVM用程序开发部分参数 <BR>
     * 可用总共内存7.9M,可用内存6.4M。 <BR>
     * 屏幕宽高480*272 <BR>
     * 大中小字体16*16 <BR>
     */
    public static final byte PSP_KEY_UP = 50;
    public static final byte PSP_KEY_DOWN = 56;
    public static final byte PSP_KEY_LEFT = 52;
    public static final byte PSP_KEY_RIGHT = 54;
    public static final byte PSP_ANALOG_UP = -1;
    public static final byte PSP_ANALOG_DOWN = -2;
    public static final byte PSP_ANALOG_LEFT = -3;
    public static final byte PSP_ANALOG_RIGHT = -4;
    public static final byte PSP_KEY_SQUARE = 49;
    public static final byte PSP_KEY_TRIANGLE = 51;
    public static final byte PSP_KEY_CIRCLE = -5;
    public static final byte PSP_KEY_CROSS = 48;
    public static final byte PSP_SELECT = 21;
    public static final byte PSP_START = 22;
    public static final byte PSP_L = 0;// 尚未实现
    public static final byte PSP_R = 0;// 尚未实现
    // MOTO
    // public static final byte KEY_UP = -1;
    // public static final byte KEY_DOWN = -6;
    // public static final byte KEY_LEFT = -2;
    // public static final byte KEY_RIGHT = -5;
    // public static final byte KEY_MIDDLE = -20;
    // public static final byte SOFTKEY_LEFT = -21;
    // public static final byte SOFTKEY_RIGHT = -22;
}
