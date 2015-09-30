package android.content.pm;

import android.content.pm.IPackageInstallObserver;
import android.util.Log;

public class PackageInstallObserver extends IPackageInstallObserver.Stub {
    public void packageInstalled(String packageName, int returnCode) {
    	Log.v("VV",packageName+" return :"+returnCode);
//        Message msg = mHandler.obtainMessage(INSTALL_COMPLETE);
//        msg.arg1 = returnCode;
//        mHandler.sendMessage(msg);
    }
}
