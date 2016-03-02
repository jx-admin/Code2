package wu.a.lib.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

@SuppressLint("NewApi")
public class PlateformUtils {

	/**
	 * <pre>
	 *
	 * @param context
	 * @param keyEvent KeyEvent.KEYCODE_MEDIA_PREVIOUS,KeyEvent.KEYCODE_MEDIA_NEXT,KeyEvent.KEYCODE_HEADSETHOOK,KeyEvent.KEYCODE_MEDIA_STOP,KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
	 * </pre>
	 */
	public static void sendActionMediaButton(Context context, int keyEvent,String pkg) {
		Intent i;
		KeyEvent ke;
		
		i = new Intent();
		if(!TextUtils.isEmpty(pkg)){
			i.setPackage(pkg);
		}
		i.setAction(Intent.ACTION_MEDIA_BUTTON);
		ke=new KeyEvent(System.currentTimeMillis()-10, System.currentTimeMillis()-5, KeyEvent.ACTION_DOWN, keyEvent, 0, 0, KeyCharacterMap.PREDICTIVE, /*scancode Raw device scan code of the event.
*/226, KeyEvent.FLAG_FROM_SYSTEM, InputDevice.SOURCE_KEYBOARD);
		i.putExtra(Intent.EXTRA_KEY_EVENT, ke);
		context.sendBroadcast(i);

		i = new Intent();
		if(!TextUtils.isEmpty(pkg)){
			i.setPackage(pkg);
		}
		i.setAction(Intent.ACTION_MEDIA_BUTTON);
		ke=new KeyEvent(android.os.SystemClock.uptimeMillis()-4, android.os.SystemClock.uptimeMillis()-2, KeyEvent.ACTION_UP, keyEvent, 0, 0, KeyCharacterMap.PREDICTIVE, /*scancode Raw device scan code of the event.
*/226, KeyEvent.FLAG_FROM_SYSTEM, InputDevice.SOURCE_KEYBOARD);
//		ke = new KeyEvent(System.currentTimeMillis() - 5,
//				System.currentTimeMillis(), KeyEvent.ACTION_UP, keyEvent, 0);
		// ke = new KeyEvent(KeyEvent.ACTION_UP,
		// KeyEvent.KEYCODE_MEDIA_PREVIOUS);
		i.putExtra(Intent.EXTRA_KEY_EVENT, ke);
		context.sendBroadcast(i);
	}
	public static void sendActionMediaButtonPress(Context context, int keyEvent,String pkg) {
		Intent i;
		KeyEvent ke;
		i = new Intent();
		if(!TextUtils.isEmpty(pkg)){
			i.setPackage(pkg);
		}
		i.setAction(Intent.ACTION_MEDIA_BUTTON);
		ke=new KeyEvent(android.os.SystemClock.uptimeMillis()-2, android.os.SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, keyEvent, 0, 0, KeyCharacterMap.PREDICTIVE, /*scancode Raw device scan code of the event.
*/226, KeyEvent.FLAG_FROM_SYSTEM, InputDevice.SOURCE_KEYBOARD);
		i.putExtra(Intent.EXTRA_KEY_EVENT, ke);
		context.sendBroadcast(i);
	}

}
