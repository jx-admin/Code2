
package com.accenture.mbank.view;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.wheel.widget.OnWheelChangedListener;
import com.accenture.mbank.wheel.widget.WheelView;
import com.accenture.mbank.wheel.widget.adapters.ArrayWheelAdapter;
import com.accenture.mbank.wheel.widget.adapters.NumericWheelAdapter;

public class DatePickerDialog {

    Context context;

    public DateNumericAdapter dayAdapter;

    public DateNumericAdapter yearAdapter;

    public DateArrayAdapter monthAdapter;

    private static DatePickerDialog datePickerDialog;

    public static DatePickerDialog getInstance() {

        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog();
        }
        return datePickerDialog;

    }

    private DatePickerDialog() {

    };

    public void showDateDialog(Context context, final DatePickListener datePickListener) {

        this.context = context;
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.date_picker_dialog,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();
        initWheel(linearLahyout, datePickListener, alertDialog);

        ImageView cancel = (ImageView)linearLahyout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()) * 3 / 4; // 设置宽度
        alertDialog.getWindow().setAttributes(lp);

    }

    public void initWheel(View root, final DatePickListener datePickListener,
            final AlertDialog alertDialog) {
        Calendar calendar = Calendar.getInstance();

        final WheelView month = (WheelView)root.findViewById(R.id.month);
        final WheelView year = (WheelView)root.findViewById(R.id.year);
        final WheelView day = (WheelView)root.findViewById(R.id.day);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"
        };

        monthAdapter = new DateArrayAdapter(context, months, curMonth);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        yearAdapter = new DateNumericAdapter(context, curYear, curYear + 10, 0);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);

        // day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

        ImageView confirm = (ImageView)root.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CharSequence dayText = dayAdapter.getItemText(day.getCurrentItem());
                CharSequence yearText = yearAdapter.getItemText(year.getCurrentItem());
                CharSequence monthText = monthAdapter.getItemText(month.getCurrentItem());

                datePickListener.onDatePick(yearText, monthText, dayText,
                        Integer.parseInt(yearText.toString()), month.getCurrentItem(),
                        Integer.parseInt(dayText.toString()));
                alertDialog.dismiss();

            }
        });

    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(context, 1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;

        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;

        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    static public interface DatePickListener {

        void onDatePick(CharSequence year, CharSequence month, CharSequence day, int yearint,
                int monthint, int dayint);

    }

}
