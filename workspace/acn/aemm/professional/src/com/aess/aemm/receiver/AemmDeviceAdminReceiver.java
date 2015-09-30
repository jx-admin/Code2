package com.aess.aemm.receiver;

import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.setting.PwPolicySetup;
import android.app.admin.DeviceAdminReceiver;
public class AemmDeviceAdminReceiver extends DeviceAdminReceiver {
	
	public final static String PSWORD   = "com.aess.aemm.PSWORD";
	public final static String SETTING = "com.android.settings";
    
    @Override
	public void onPasswordExpiring(android.content.Context context,
			android.content.Intent intent) {
		int x = CommUtils.getPasswordRequire(context);
		if (x < 1) {
			CommUtils.setPasswordRequire(context, 1);
			PwPolicySetup.clearDevicePs(context);
		}
		return;
	}
}