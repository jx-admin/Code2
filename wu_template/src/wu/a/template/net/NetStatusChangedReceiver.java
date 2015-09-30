package wu.a.template.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * <pre>
 * 网络状态发生变化的时候，系统会发出 android.net.conn.CONNECTIVITY_CHANGE
 * 在 manifest 文件中需要加上一条权限：<
 * uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
 * />
 * 可以静态、动态注册Receiver
 * 
 * 通过 intent 可以获取一些 EXTRA，如 EXTRA_NO_CONNECTIVITY。
 * boolean b = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
 * 
 * @author junxu.wang
 *
 */
public class NetStatusChangedReceiver {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				Log.d("mark", "网络状态已经改变 "+intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true));
				connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
				} else {
					Log.d("mark", "没有可用网络");
				}
			}
		}
	};

	private Context context;

	public void register(Context context) {
		this.context = context;
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(mReceiver, mFilter);
	}

	public void unregisterReceiver() {
		context.unregisterReceiver(mReceiver);
	}

}
