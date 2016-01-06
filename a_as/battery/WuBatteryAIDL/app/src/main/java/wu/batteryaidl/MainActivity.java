package wu.batteryaidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import java.util.List;

import wu.battery.model.Battery;
import wu.batteryaidl.help.Utils;
import wu.batteryaidl.view.ChargingView;
import wu.utils.LogUtils;

public class MainActivity extends Activity implements ChargingView.IDelegate {


    private IBatteryService mBattery;
    private IOnBatteryChangedListener mOnBatteryChangedListener = new IOnBatteryChangedListener() {
        @Override
        public void onBatteryChanged(final Battery battery) throws RemoteException {
            if (battery != null) {
                chargingView.onBatteryChanged(battery);
            }
            LogUtils.d("OnBatteryChaned in MainAcitivity is " + battery);
        }

        public void onPowerConnected() {
            LogUtils.d("onPowerConnected in MainAcitivity is ");
            chargingView.onPowerConnected();
        }

        public void onPowerDisConnected() {
            LogUtils.d("onPowerDisConnected in MainAcitivity is ");
            chargingView.onPowerDisConnected();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("onServiceConnected in MainAcitivity ");
            mBattery = IBatteryService.Stub.asInterface(service);
            try {
                mBattery.addOnBatteryChangedListener(mOnBatteryChangedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    ChargingView chargingView;

    public static void start(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chargingView = (ChargingView) findViewById(R.id.chargeview);
        chargingView.setDelegate(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.unlockAndWakeLockScreen(MainActivity.this, 3000);
            }
        }, 10000);
        querySysAppInfo();
    }

    public void querySysAppInfo() {
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
// 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
//            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标

            LogUtils.d(appLabel + " activityName---" + activityName
                    + " pkgName---" + pkgName);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, MainBatteryService.class);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
        chargingView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBattery != null) {
            try {
                mBattery.removeOnBatteryChangedListener(mOnBatteryChangedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBattery = null;
        }
        unbindService(mServiceConnection);
        chargingView.onStop();
    }

    @Override
    public void onClose(View view) {
        finish();
    }
}
