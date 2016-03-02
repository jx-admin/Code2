package wu.a.template.media;

import java.io.FileNotFoundException;
import java.io.FileReader;

import wu.a.lib.utils.Logger;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * <pre>
 * @author junxu.wang
 * 
 * @date : 2015年3月17日 下午4:43:28
 * 
 *       耳机：select from： http://blog.sina.com.cn/s/blog_6ca570ed0101bjll.html
 *       meidaButton select from：http://www.linuxidc.com/Linux/2012-02/53075.htm
 * </pre>
 */
public class MediaButtonReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		StringBuilder sb = new StringBuilder();
		String actoin = intent.getAction();
		sb.append(MediaButtonReceiver.class.getSimpleName());
		sb.append("onReceiver = ");
		sb.append(actoin);
		if (Intent.ACTION_HEADSET_PLUG.equals(actoin)) {
			/*
			 * state —— 0代表拔出，1代表插入 name——字符串，代表headset的类型 microphone ——
			 * 1代表插入的headset有麦克风，0表示没有麦克风
			 */
			int state = intent.getIntExtra("state", -1);
			// 大多数手机这个值是1或者2，但是也有不少手机是100多，
			// 分别表示耳机连接或者耳机和麦克风都连接
			if (state > 0) {
				sb.append(" 耳机已插入");
			} else if (state == 0) {
				sb.append(" 耳机已拔出");
			} else {
				sb.append(" 未知耳机状态");
			}
		} else if (Intent.ACTION_MEDIA_BUTTON.equals(actoin)) {
			KeyEvent keyEvent = (KeyEvent) intent
					.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

			// 按下 / 松开 按钮
			int keyAction = keyEvent.getAction();
			// 获得按键字节码
			int keyCode = keyEvent.getKeyCode();
			// 获得事件的时间
			long downtime = keyEvent.getEventTime();
			sb.append(" keyAction=");
			sb.append(keyAction);
			sb.append(" keyCode=");
			sb.append(keyCode);
			sb.append(" doownTime=");
			sb.append(downtime);
			switch (keyAction) {
			case KeyEvent.ACTION_DOWN:
				sb.append(" ACTION_DOWN");
				break;
			case KeyEvent.ACTION_UP:
				sb.append(" ACTION_UP");
				switch (keyEvent.getKeyCode()) {
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					sb.append("--KEYCODE_MEDIA_PLAY_PAUSE");
					break;
				case KeyEvent.KEYCODE_HEADSETHOOK:
					sb.append("--KEYCODE_HEADSETHOOK");
					break;
				case KeyEvent.KEYCODE_MEDIA_STOP:
					sb.append("--KEYCODE_MEDIA_STOP");
					break;
				case KeyEvent.KEYCODE_MEDIA_NEXT:
					sb.append("--KEYCODE_MEDIA_NEXT");
					break;
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					sb.append("--KEYCODE_MEDIA_PREVIOUS");
					break;
				default:
					sb.append("--default");
					break;
				}
				break;
			case KeyEvent.ACTION_MULTIPLE:
				sb.append(" ACTION_MULTIPLE");
				break;
			}

		}
		Logger.log(sb.toString());
		MediaAcitivity.start(context, sb.toString());
	}

	/**
	 * <pre>
	 * 注册耳机状态Receiver
	 * @param context
	 *
	 * </pre>
	 */
	public void registerHeadsetPlugReceiver(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(2147483647);
		intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(this, intentFilter);
	}

	/**
	 * <pre>
	 * 注销耳机状态Receiver
	 * @param context
	 *
	 * </pre>
	 */
	public void unregisterHeadsetPlugReceiver(Context context) {
		context.unregisterReceiver(this);
	}

	/**
	 * <pre>
	 * 注册 Meida Button Receiver
	 * 该广播必须在AndroidManifest.xml文件中进行声明，否则就监听不到该MEDIA_BUTTON广播了
	 * <receiver android:name="xx.xx.MediaButtonReceiver" >
	 *             <intent-filter>
	 *                 <action android:name="android.intent.action.MEDIA_BUTTON" >
	 *                 </action>
	 *             </intent-filter>
	 *         </receiver>
	 * @param context
	 * </pre>
	 */
	public static void registerMediaButtonReceiver(Context context) {
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		ComponentName mbCN = new ComponentName(context.getPackageName(),
				MediaButtonReceiver.class.getName());
		mAudioManager.registerMediaButtonEventReceiver(mbCN);
	}

	/**
	 * <pre>
	 * 注销Media Button Receiver
	 * @param context
	 * </pre>
	 */
	public static void unregisterMediaButtonReceiver(Context context) {
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		ComponentName mbCN = new ComponentName(context.getPackageName(),
				MediaButtonReceiver.class.getName());
		mAudioManager.unregisterMediaButtonEventReceiver(mbCN);
	}

	/**
	 * <pre>
	 * isHeadsetOn这即是插入状态，看起来非常简单。但是你会发现你的程序得到的总是false，这是因为调用系统服务都是需要权限的。
	 * 而android应用的结构使你无法在代码中来更改权限，只能在AndroidManifest.xml中进行修改，所以这就为如果要开发API的人留下一个小遗憾。
	 * 权限是：
	 * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
	 * @param context
	 * @return
	 * </pre>
	 */
	public static boolean isHeadsetOn(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		boolean isHeadsetOn = audioManager.isWiredHeadsetOn();
		return isHeadsetOn;
	}

	/**
	 * <pre>
	 * android底层有很多个接口来反应耳机插入状态的，其根本原理是耳机插入和拔出状态的电平是不同的。
	 * 因为android是开源的，而且版本很多，所以未必每个手机厂家都会去实现同一个接口。
	 * 实际上，上面的isWiredHeadsetOn()方法就是这些接口在上层的体现。
	 * 我们可以绕过中间层，直接去读取底层接口的值，来判断。
	 * 但是这样的方法不是对每个手机都有效，比如未实现那个接口的。在2.3之后的手机可以这样做：
	 * p.s.没有提到权限，测试没有效果
	 * @return
	 * </pre>
	 * @deprecated
	 */
	public static boolean isHeadsetOn() {
		boolean isHeadsetOn = false;
		String HEADSET_STATE_PATH = "/sys/class/switch/h2w/state";// 这就是其中一个接口
		int headsetState = 0;
		int len = 0;
		try {
			FileReader file = new FileReader(HEADSET_STATE_PATH);
			char[] buffer = new char[1024];
			len = file.read(buffer, 0, 1024);
			headsetState = Integer.valueOf(new String(buffer, 0, len).trim());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 大多数手机这个值是1或者2，但是也有不少手机是100多，
		// 分别表示耳机连接或者耳机和麦克风都连接
		// -1或者其他负数表示拔出
		isHeadsetOn = headsetState > 0;
		return isHeadsetOn;
	}
}
