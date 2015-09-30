
package com.seekting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.seekting.util.LOGManager;

public class BaseActivity extends Activity {

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        LOGManager.d(this.getClass().getSimpleName() + " onResume() invoked!!");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LOGManager.d(this.getClass().getSimpleName() + " onPause() invoked!!");
        super.onPause();
    }

    protected void showLongToast(final String pMsg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, pMsg, Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void showShortToast(final String pMsg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, pMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void handleOutmemoryError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "内存空间不足！", Toast.LENGTH_SHORT).show();
                // finish();
            }
        });
    }

    private int network_err_count = 0;

    protected void handleNetworkError() {
        network_err_count++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (network_err_count < 3) {
                    Toast.makeText(BaseActivity.this, "网速好像不怎么给力啊！", Toast.LENGTH_SHORT).show();
                } else if (network_err_count < 5) {
                    Toast.makeText(BaseActivity.this, "网速真的不给力！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseActivity.this, "唉，今天的网络怎么这么差劲！", Toast.LENGTH_SHORT).show();
                }
                // finish();

            }
        });
    }

    protected void handleMalformError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "数据格式错误！", Toast.LENGTH_SHORT).show();
                // finish();
            }
        });
    }

    protected void handleFatalError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "发生了一点意外，程序终止！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
