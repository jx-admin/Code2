package wu.wubattery;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import wu.battery.BatteryActivity;
import wu.battery.ChargeLockAccessor;
import wu.battery.model.Battery;
import wu.utils.LogUtils;

public class MainActivity extends BatteryActivity {
    public static TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_test_main);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
    }

    @Override
    public void onBatteryChanged(final Battery battery) {
        super.onBatteryChanged(battery);
        tvMsg.post(new Runnable() {
            @Override
            public void run() {
                log(battery.toString());
            }
        });
    }

    @Override
    public void onPowerConnected() {
        super.onPowerConnected();
        tvMsg.post(new Runnable() {
            @Override
            public void run() {
                log("onPowerConnected");
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clean:
                tvMsg.setText("");
                break;
        }
    }

    public static void log(String msg) {
        if (tvMsg != null && msg != null) {
            tvMsg.setText(tvMsg.getText() + "\n" + msg);
        }
    }

    @Override
    protected ChargeLockAccessor.PowerLockCallback getPowerLockCallback(Context context) {
        return mPowerLockCallback;
    }

    ChargeLockAccessor.PowerLockCallback mPowerLockCallback = new ChargeLockAccessor.PowerLockCallback() {
        @Override
        public boolean disabled() {
            LogUtils.d("startView disabled");
            return false;
        }

        @Override
        public void startPowerLock() {
            LogUtils.d("startView startPowerLock");
            Intent i = new Intent(ChargeLockAccessor.HOLA_POWER_MANAGER_ACTION);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    };
}
