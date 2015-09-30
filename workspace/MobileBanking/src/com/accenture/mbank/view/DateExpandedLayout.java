
package com.accenture.mbank.view;

import java.text.ParseException;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.DatePickerDialog.DatePickListener;

public class DateExpandedLayout extends ExpandedContainer implements OnClickListener,
        DatePickListener {

    TextView date;

    long time;

    public DateExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (date == null) {

            date = (TextView)findViewById(R.id.date_text);
            date.setOnClickListener(this);

            setNowTime();
            String date = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
            expandFocusResultChange(date);
            this.date.setText(date);
        }
    }

    boolean editAble = true;

    @Override
    public void onClick(View v) {
        if (v == date) {
            if (editAble) {
                if (Utils.isFastDoubleClick()) {
                    return;
                } else {
                    DatePickerDialog.getInstance().showDateDialog(getContext(), this);
                }
            }
        }
    }

    @Override
    public void onDatePick(CharSequence year, CharSequence month, CharSequence day, int yearint,
            int monthint, int dayint) {

//        String result = day.toString() + " " + month + " " + year.toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearint, monthint, dayint);
        time = calendar.getTimeInMillis();
        String result = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
        date.setText(result);
        
        expandFocusResultChange(result);

        long now = System.currentTimeMillis();
        if (time < now) {
            setNowTime();
            result = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
            date.setText(result);
            expandFocusResultChange(result);
        }

    }

    private void setNowTime() {
        Calendar beginTime = Calendar.getInstance();
        int year = beginTime.get(Calendar.YEAR);
        int month = beginTime.get(Calendar.MONTH);
        int day = beginTime.get(Calendar.DAY_OF_MONTH);
        beginTime.set(year, month, day, 0, 0);
        time = beginTime.getTimeInMillis();
    }

    @Override
    protected void resetResult() {
        super.resetResult();
        init = false;
        setNowTime();
        String date = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
        expandFocusResultChange(date);
        this.date.setText(date);
        
        ViewGroup v = (ViewGroup)this.expandBarResultListener;
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEditable(boolean flag) {
        // TODO Auto-generated method stub
        super.setEditable(flag);
        editAble = flag;
    }

    @Override
    protected void onRecover(String text) {

        if (text == null || text.equals("")) {
            return;
        }
        try {
            time = TimeUtil.getTimeByString(text, TimeUtil.dateFormat2);
            String date = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
            expandResultChange(date);
            this.date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Object getValue() {
        return time;
    }

    public void showType(final String typeCode) {
        ViewGroup v = (ViewGroup)this.expandBarResultListener;

        resetResult();
        if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            v.setVisibility(View.VISIBLE);
        } else if (typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            v.setVisibility(View.VISIBLE);
        } else if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {
            v.setVisibility(View.GONE);
        } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            v.setVisibility(View.GONE);
        }
    }
}
