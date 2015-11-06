package com.aess.aemm.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.view.msg.SendMsgActivity;

public class DateSelect {
	
	public static final int IGNORE = 0;
	public static final int MORE = 1;
	public static final int LESS = 2;
	
	public DateSelect(Context cxt, int id, int type, String value) {
		mId = id;
		mType = type;
		mOldDate = value;
		mCxt = cxt;
	}
	
	public AlertDialog CreateDailog() {
		
		Builder builder = new AlertDialog.Builder(mCxt);

		LayoutInflater layoutInflater = (LayoutInflater) mCxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.date_time_select, null);

		mDatePick = (DatePicker) view.findViewById(R.id.date);
		if (SendMsgActivity.validTimeBeginId == mId
				|| SendMsgActivity.validTimeEndId == mId) {
			mDatePick.setEnabled(false);
		} else {
			mDatePick.setEnabled(true);
		}
		mTimePick = (TimePicker) view.findViewById(R.id.time);

		mTimePick.setIs24HourView(true);

		mBtnSure = (Button) view.findViewById(R.id.save);
		mBtnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int year = mDatePick.getYear();
				int month = mDatePick.getMonth()+1;
				int day = mDatePick.getDayOfMonth();
				int hour = mTimePick.getCurrentHour();
				int minute = mTimePick.getCurrentMinute();
				
				mNewDate = null;
				if (SendMsgActivity.validTimeBeginId == mId
						|| SendMsgActivity.validTimeEndId == mId) {
					mNewDate = String.format("%02d:%02d:00", hour, minute);
				} else {
					mNewDate = String.format("%04d-%02d-%02d %02d:%02d:00", year,
							month, day, hour, minute);
				}
				
				if (null != mOldDate && mOldDate.length() > 0) {

					if (mOldDate.length() > 8) {
						int x = dateCheck("yyyy-MM-dd HH:mm:ss");
						if ( x < 0) {
							return;
						}
					} else {
						int x = dateCheck("HH:mm:ss");
						if ( x < 0) {
							return;
						}
					}
				}
				SendMsgActivity.dataPickEndMsg(mId, mNewDate);
				mAlertDialog.cancel();
			}
		});

		mBtnCancel = (Button) view.findViewById(R.id.cancle);
		mBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAlertDialog.cancel();
			}
		});
		builder.setView(view);
		
		if(null != mOldDate && mOldDate.length() > 1) {
			String hint = null;
			if (MORE == mType) {
				hint = mCxt.getString(R.string.more);
			} else if (LESS == mType) {
				hint = mCxt.getString(R.string.less);
			}
			if (null != hint) {
				mHint = (TextView) view.findViewById(R.id.dtsText1);
				mHint.setText(hint + mOldDate);
				mHint.setTextColor(Color.WHITE);
			}
		}
		mAlertDialog = builder.show();
		return mAlertDialog;
	}
	
	private void inputHelp() {
		mHint.setTextColor(Color.RED);
		String head = mCxt.getString(R.string.msgTimeError);
		Toast.makeText(mCxt, head, Toast.LENGTH_SHORT).show();
	}
	
	private int dateCheck(String dataformat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataformat);
		Date oldDate = null;
		Date newDate = null;
		try {
			oldDate = sdf.parse(mOldDate);
			newDate = sdf.parse(mNewDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (null != oldDate && null != newDate) {
			if (MORE == mType) {
				if (newDate.before(oldDate)) {
					inputHelp();
					return -1;
				}
			} else if (LESS == mType) {
				if (newDate.after(oldDate)) {
					inputHelp();
					return -1;
				}
			}
		}
		return 1;
	}

	private Button mBtnSure;
	private Button mBtnCancel;
	private DatePicker mDatePick;
	private TimePicker mTimePick;
	private AlertDialog mAlertDialog;
	private TextView mHint;
	private int mId;
	private int mType;
	private String mOldDate;
	private Context mCxt;
	private String mNewDate;
}
