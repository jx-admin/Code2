package com.aess.aemm.view.msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.view.NotificationUtils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgDetailActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ly = (LinearLayout) getLayoutInflater().inflate(
				R.layout.activity_msg_detail2, null);
		initWidget(ly);
		Intent i = getIntent();
		setActivity2(i);
		setContentView(ly);
		setTitle(R.string.title_activity_msg_detail);
	}

	@Override
	protected void onResume() {
		NotificationUtils.cancelNotification(this);
		super.onResume();
	}
	
	private void setActivity2(Intent intent) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Uri uri = intent.getData();
		Cursor cursor1 = NewsContent.queryMsgByURI(this, uri);
		NewsContent nc = NewsContent.getContentByCursor(cursor1);
		
		if (null != nc) {

			isread = nc.mIsRead;

			int type = nc.mType;
			if (null != nc.mTypeName) {
				tvType.setText(nc.mTypeName);
			} else {
				tvType.setText(getTypeLable(type));
			}

			String info = nc.mTitile;
			if (null != info) {
				tvtle.setText(info);
				if (isread == 1) {
					tvtle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
				} else {
					tvtle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				}
			}
			
			if (null != nc.mBusName) {
				tvbus.setText(nc.mBusName);
			} else {
				tvbus.setText(getBusLable(nc.mBusType));
			}

			info = nc.mContent;
			if (null != info) {
				tvCnt.setText(info);
			}

			long infovalue = nc.mPData;
			if (0 != infovalue) {
				Date date = new Date(infovalue);
				tvDate.setText(sdf2.format(date));
			}

			if (MessageType.MSG_INFORM == type || type > MessageType.MSG_EVENT) {
				pub.setVisibility(View.GONE);
				lev.setVisibility(View.GONE);
				rag.setVisibility(View.GONE);
				rag1.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}

			if (MessageType.MSG_POST == type) {
				info = nc.mPublish;
				if (null != info) {
					tvPub.setText(info);
				}
				lev.setVisibility(View.GONE);
				rag.setVisibility(View.GONE);
				rag1.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}

			if (MessageType.MSG_PLAN == type || MessageType.MSG_EVENT == type) {
				tvRange.setText(nc.mBegin);
				tvRange1.setText(nc.mEnd);
			}

			if (MessageType.MSG_PLAN == type) {
				int value = -1;
				value = nc.mPlanState;
				if (-1 != value) {
					tvState.setText(getStateLable(value));
				}
				pub.setVisibility(View.GONE);
				lev.setVisibility(View.GONE);
			}

			if (MessageType.MSG_EVENT == type) {
				int value2 = -1;
				value2 = nc.mLevel;
				if (null != info) {
					tvLevel.setText(getLevLable(value2));
				}
				pub.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}
		}
		nc.mIsRead = 1;
		nc.update(this);
	}

	interface EventState {
		public static final int stUnbegin = 0;
		public static final int stDoing = 1;
		public static final int stFinish = 2;
		public static final int stDefer = 3;
	}

	private String getStateLable(int type) {
		String alt = null;
		switch (type) {
		case EventState.stUnbegin: {
			alt = this.getString(R.string.mtenvents0);
			break;
		}
		case EventState.stDoing: {
			alt = this.getString(R.string.mtenvents1);
			break;
		}
		case EventState.stFinish: {
			alt = this.getString(R.string.mtenvents2);
			break;
		}
		case EventState.stDefer: {
			alt = this.getString(R.string.mtenvents3);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}

	private String getTypeLable(int type) {
		String alt = null;
		switch (type) {
		case MessageType.MSG_POST: {
			alt = this.getString(R.string.mtpost);
			break;
		}
		case MessageType.MSG_INFORM: {
			alt = this.getString(R.string.mtinform);
			break;
		}
		case MessageType.MSG_PLAN: {
			alt = this.getString(R.string.mtplan);
			break;
		}
		case MessageType.MSG_EVENT: {
			alt = this.getString(R.string.mtenvent);
			break;
		}
		case MessageType.MSG_CUSTOM: {
			alt = this.getString(R.string.mtother);
			break;
		}
		default: {
			alt = this.getString(R.string.mtother);
		}
		}
		return alt;
	}
	
	
	interface LevState {
		public static final int stHigh = 0;
		public static final int stNormal = 1;
		public static final int stLow = 2;
	}
	
	interface BusType {
		public static final int btNormal = 0;
		public static final int btOper = 1;
		public static final int btRes = 2;
	}
	
	private String getLevLable(int type) {
		String alt = null;
		switch (type) {
		case LevState.stHigh: {
			alt = this.getString(R.string.mtlev1);
			break;
		}
		case LevState.stNormal: {
			alt = this.getString(R.string.mtlev2);
			break;
		}
		case LevState.stLow: {
			alt = this.getString(R.string.mtlev3);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}
	
	private String getBusLable(int type) {
		String alt = null;
		switch (type) {
		case BusType.btNormal: {
			alt = this.getString(R.string.btNor);
			break;
		}
		case BusType.btOper: {
			alt = this.getString(R.string.btOpe);
			break;
		}
		case BusType.btRes: {
			alt = this.getString(R.string.btRes);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}

	private void initWidget(View view) {
		tvtle = (TextView)view.findViewById(R.id.tmdtitle);
		tvCnt = (TextView)view.findViewById(R.id.tmdtnt);
		tvPub = (TextView)view.findViewById(R.id.tmdpub);
		tvType = (TextView)view.findViewById(R.id.tmdtype);
		tvDate = (TextView)view.findViewById(R.id.tmddate);
		tvRange = (TextView)view.findViewById(R.id.tmdtrange);
		tvRange1 = (TextView)view.findViewById(R.id.tmdtrange1);
		tvState = (TextView)view.findViewById(R.id.tmdtstate);
		tvLevel = (TextView)view.findViewById(R.id.tmdlevel);
		tvbus = (TextView)view.findViewById(R.id.tmdbus);

		pub = view.findViewById(R.id.mdlpub);
		lev = view.findViewById(R.id.mdllev);
		rag = view.findViewById(R.id.mdlrag);
		rag1 = view.findViewById(R.id.mdlrag1);
		sta = view.findViewById(R.id.mdlsta);
		bus = view.findViewById(R.id.mdlbus);
	}

	TextView tvtle;
	TextView tvState;
	TextView tvCnt;
	TextView tvType;
	TextView tvDate;
	TextView tvPub;
	TextView tvRange;
	TextView tvRange1;
	TextView tvLevel;
	TextView tvbus;
	View pub = null;
	View lev = null;
	View rag = null;
	View rag1 = null;
	View sta = null;
	View bus = null;
	int isread = 1;
}
