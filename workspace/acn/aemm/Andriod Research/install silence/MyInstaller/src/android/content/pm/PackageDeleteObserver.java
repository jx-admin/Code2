package android.content.pm;

import android.content.pm.IPackageDeleteObserver;
import android.util.Log;

public class PackageDeleteObserver  extends IPackageDeleteObserver.Stub {
    public void packageDeleted(boolean succeeded) {
    	Log.v("VV","PackageDeleteObserver return :"+succeeded);
//        Message msg = mHandler.obtainMessage(UNINSTALL_COMPLETE);
//        msg.arg1 = succeeded?SUCCEEDED:FAILED;
//        mHandler.sendMessage(msg);
   }
}
