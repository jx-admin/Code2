package wu.a.lib.net;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimCard {

	private static TelephonyManager manager;

	public static boolean hasSimcard(Context context) {
		manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

//	public static boolean hasSimcard(Context context) {
//		boolean result = false;
//		StringBuilder sb = new StringBuilder();
//		manager = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//
//		sb.append("SIM卡状态");
//		switch (manager.getSimState()) {
//		case TelephonyManager.SIM_STATE_READY:
//			sb.append("良好");
//			sb.append('\n');
//			result = true;
//			break;
//		case TelephonyManager.SIM_STATE_ABSENT:
//			sb.append("无SIM卡");
//			sb.append('\n');
//			break;
//		default:
//			sb.append("SIM卡被锁定或未知状态");
//			sb.append('\n');
//			break;
//		}
//		Log.d("MainActivity", sb.toString());
//		return result;
//	}

}
