
package com.accenture.mbank;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;

public class BaseActivity extends FragmentActivity {

    protected View back;

    /**
     * flag to indicate the state of current user, initialized to be false it
     * will be changed to true after user logined successfully
     */
    public static boolean isLogin = false;

    public static boolean isOffline = false;

    public static boolean initValue = false;

    public static boolean isNewVersion = true;

    public static final int ADV_NEWS = 0x00;

    public static final int ERROR_MESSAGE = 0x01;

    public static final int CLEAR_USERNAME_PASSWORD = 0x02;

    public Handler baseHandler;

    public static int screen_width = 0;

    public static int screen_height = 0;

    public static final double cycle_top_height_margin = 16d / 360d;

    /**
     * 上半圆占的高度比例
     */
    public static final double cycle_top_height = 65d / 360d;

    /**
     * 上半圆与下半圆的比例
     */
    public static final double cycle_bottom_height = 111d / 187;;

    /**
     * 内置ScrollView的最大高度
     */
    public static final double innerscroll_view_max_height = 116d / 360d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LogManager.d(this.getClass() + "oncreate");
        super.onCreate(savedInstanceState);
        baseHandler = new Handler();
        WindowManager windowManager = getWindowManager();

        screen_width = Math.min(windowManager.getDefaultDisplay().getWidth(), windowManager
                .getDefaultDisplay().getHeight());
        screen_height = Math.max(windowManager.getDefaultDisplay().getWidth(), windowManager
                .getDefaultDisplay().getHeight());
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);

        back = (View)findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    onBackClick();
                }
            });
        }

    }

    protected void onBackClick() {

    }

    protected String getResourceString(int id) {
        String str = getResources().getString(id);
        return str;

    }

    public boolean isFinished = false;

    public AlertDialog errorMessageDialog;

    public void displayErrorMessage(final String errordec) {
        LogManager.d("displayErrorMessage");
        if (isFinished) {
            return;
        }
        baseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (errorMessageDialog != null) {
                    errorMessageDialog.dismiss();
                    errorMessageDialog = null;
                }
                errorMessageDialog = DialogManager.createMessageDialog(errordec, BaseActivity.this);
                errorMessageDialog.show();

            }
        });
    }

    public void hideBack() {

        if (back != null) {
            back.setVisibility(View.GONE);
        }

    }

    public void showBack() {
        if (back != null) {
            back.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LogManager.d(this.getClass() + "onResume");
    }

    public void displaySuccessMessage(final String msg) {
        baseHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
