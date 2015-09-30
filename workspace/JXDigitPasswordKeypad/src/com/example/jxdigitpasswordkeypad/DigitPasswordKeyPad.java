package com.example.jxdigitpasswordkeypad;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
/**
 * 系统定制的数字密码键盘
 *
 * @author twf
 *
 */
public class DigitPasswordKeyPad extends View {
    private Context ctx = null;
    private View v;
    private String digitnum = "";
    private int length = 6;
 
    private Button digitkeypad_1;
    private Button digitkeypad_2;
    private Button digitkeypad_3;
    private Button digitkeypad_4;
    private Button digitkeypad_5;
    private Button digitkeypad_6;
    private Button digitkeypad_7;
    private Button digitkeypad_8;
    private Button digitkeypad_9;
    private Button digitkeypad_0;
    private Button digitkeypad_c;
    private Button digitkeypad_ok;
    private EditText digitkeypad_edittext;
 
    private boolean isPwd;
 
    public DigitPasswordKeyPad(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }
 
    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }
 
    public void setEditTextIsPwd(boolean ispwd) {
        if (ispwd) {
            digitkeypad_edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            digitkeypad_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        this.isPwd = ispwd;
    }
 
    public View setup() {
        LayoutInflater lif = LayoutInflater.from(ctx);
        v = lif.inflate(R.layout.control_digitpasswordkeypad, null);
 
        // 初始化 对象
        digitkeypad_1 = (Button) v.findViewById(R.id.digitkeypad_1);
        digitkeypad_2 = (Button) v.findViewById(R.id.digitkeypad_2);
        digitkeypad_3 = (Button) v.findViewById(R.id.digitkeypad_3);
        digitkeypad_4 = (Button) v.findViewById(R.id.digitkeypad_4);
        digitkeypad_5 = (Button) v.findViewById(R.id.digitkeypad_5);
        digitkeypad_6 = (Button) v.findViewById(R.id.digitkeypad_6);
        digitkeypad_7 = (Button) v.findViewById(R.id.digitkeypad_7);
        digitkeypad_8 = (Button) v.findViewById(R.id.digitkeypad_8);
        digitkeypad_9 = (Button) v.findViewById(R.id.digitkeypad_9);
        digitkeypad_0 = (Button) v.findViewById(R.id.digitkeypad_0);
        digitkeypad_c = (Button) v.findViewById(R.id.digitkeypad_c);
        digitkeypad_ok = (Button) v.findViewById(R.id.digitkeypad_ok);
        digitkeypad_edittext = (EditText) v.findViewById(R.id.digitpadedittext);
 
        // 添加点击事件
        DigitPasswordKeypadOnClickListener dkol = new DigitPasswordKeypadOnClickListener();
        digitkeypad_1.setOnClickListener(dkol);
        digitkeypad_2.setOnClickListener(dkol);
        digitkeypad_3.setOnClickListener(dkol);
        digitkeypad_4.setOnClickListener(dkol);
        digitkeypad_5.setOnClickListener(dkol);
        digitkeypad_6.setOnClickListener(dkol);
        digitkeypad_7.setOnClickListener(dkol);
        digitkeypad_8.setOnClickListener(dkol);
        digitkeypad_9.setOnClickListener(dkol);
        digitkeypad_0.setOnClickListener(dkol);
        digitkeypad_c.setOnClickListener(dkol);
        digitkeypad_ok.setOnClickListener(new DigitPasswordKeypadFinshOnClikcListener());
 
        return v;
    }
 
    private class DigitPasswordKeypadFinshOnClikcListener implements OnClickListener {
 
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
 
            if (viewId == R.id.digitkeypad_ok) {
                // 点击完成
                // 设置值回页面
                // 隐藏自己View
                // if (isPwd) {
                // jsimpl.hidePasswdPad(digitkeypad_edittext.getText().toString());
                // } else {
                // jsimpl.hideCallNumPad(digitkeypad_edittext.getText().toString());
                // }
                DigitPasswordKeyPad.this.setVisibility(View.GONE);
            }
        }
    }
 
    public void initInputLable(String str, int length) {
        str = str.trim();
        digitnum = str;
        this.length = length;
        digitkeypad_edittext.setText(digitnum);
        digitkeypad_edittext.setSelection(digitnum.length());
    }
 
    private class DigitPasswordKeypadOnClickListener implements OnClickListener {
 
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
 
            switch (viewId) {
            case R.id.digitkeypad_1:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 1;
                }
                break;
            case R.id.digitkeypad_2:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 2;
                }
                break;
            case R.id.digitkeypad_3:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 3;
                }
                break;
            case R.id.digitkeypad_4:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 4;
                }
                break;
            case R.id.digitkeypad_5:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 5;
                }
                break;
            case R.id.digitkeypad_6:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 6;
                }
                break;
            case R.id.digitkeypad_7:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 7;
                }
                break;
            case R.id.digitkeypad_8:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 8;
                }
                break;
            case R.id.digitkeypad_9:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 9;
                }
                break;
            case R.id.digitkeypad_0:
                if (digitnum.length() == length) {
                    return;
                } else {
                    digitnum += 0;
                }
 
                break;
            case R.id.digitkeypad_c:// 后退
                if (digitnum.length() > 0) {
                    digitnum = digitnum.substring(0, digitnum.length() - 1);
                }
                break;
            }
            // 格式化 数据
            digitkeypad_edittext.setText(digitnum);
            digitkeypad_edittext.setSelection(null != digitnum ? digitnum.length() : 0);
        }
 
    }
}