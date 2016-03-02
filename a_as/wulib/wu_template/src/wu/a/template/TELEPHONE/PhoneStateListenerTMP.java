package wu.a.template.TELEPHONE;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Andoid Linstener获取信号强度</p>http://blog.csdn.net/nyjsl/article/details/7497452</p>
 * 
 * 在中国,联通3g为UMTS或者HSDPA,电信的3g为EVDO,移动和联通的2g为GPRS或者EGDE,电信的2G为CMDA
 * 
 * onSignalStrengthsChanged()方法,当信号改变时回调 </p>
 * 在manifest中获得权限:< uses-permission
 * android:name="android.permission.CHANGE_NEWWORK_STATE"/></p>
 * 注意：需要在AndroidManifest.xml文件中添加权限：<
 * uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 * 
 * @author Administrator
 *
 */
public class PhoneStateListenerTMP {
	private static final String TAG = "PhoneStateListenerTMP";
	private TelephonyManager telephoneManager;

	public void create(Context context) {
		telephoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	/**
	 * 获取Tel信息
	 */
	public void getTelInfo(){
		 //获取移动服务商名称  
		telephoneManager.getNetworkOperatorName();  
        //获取设备号码  
		telephoneManager.getDeviceId(); 
	}

	/**获取电信运营商类型
	 * @see #getTypeName(int)
	 * @return
	 */
	public int getType() {
		return telephoneManager.getNetworkType();
	}

	/**
	 * @param type
	 *            Sim信号类型
	 * @return 网络类型名称
	 */
	public String getTypeName(int type) {
		StringBuffer sb = new StringBuffer();
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_HSDPA: {
			sb.append("联通3g");
		}
			break;
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE: {
			sb.append("移动或者联通2g");
		}
			break;
		case TelephonyManager.NETWORK_TYPE_CDMA: {
			sb.append("电信2g");
		}
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A: {
			sb.append("电信3g");

		}
			break;
		default:
			sb.append("未知类型");
		}
		return sb.toString();
	}

	PhoneStateListener phoneStateListener = new PhoneStateListener() {

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);
			StringBuffer sb = new StringBuffer();
			String strength = String.valueOf(signalStrength
					.getGsmSignalStrength());
			sb.append(getTypeName(getType())).append("信号强度:").append(strength);

			Log.d(TAG, sb.toString());
		}

	};

	public void register() {
		telephoneManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	public void unregister() {
		telephoneManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_NONE);
	}

}
