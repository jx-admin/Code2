package gjz.bluetooth;

import gjz.bluetooth.R;
import gjz.bluetooth.AnimationTabHost;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class Bluetooth extends TabActivity {
	enum ServerOrCilent {
		NONE, SERVICE, CILENT
	};

	private Context mContext;
	static AnimationTabHost mTabHost;
	static String BlueToothAddress = "null";
	static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;
	static boolean isOpen = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.main);
		// 实例化
		mTabHost = (AnimationTabHost) getTabHost();
		mTabHost.addTab(mTabHost
				.newTabSpec("Tab1")
				.setIndicator(
						"设备列表",
						getResources().getDrawable(
								android.R.drawable.ic_menu_add))
				.setContent(new Intent(mContext, BtDiscoveryActivity.class)));
		mTabHost.addTab(mTabHost
				.newTabSpec("Tab2")
				.setIndicator(
						"对话列表",
						getResources().getDrawable(
								android.R.drawable.ic_menu_add))
				.setContent(new Intent(mContext, chatActivity.class)));
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (tabId.equals("Tab1")) {
				}
			}
		});
		mTabHost.setCurrentTab(0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(mContext, "address:", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		/* unbind from the service */
		super.onDestroy();
	}

	public class SiriListItem {
		String message;
		boolean isSiri;

		public SiriListItem(String msg, boolean siri) {
			message = msg;
			isSiri = siri;
		}
	}
}