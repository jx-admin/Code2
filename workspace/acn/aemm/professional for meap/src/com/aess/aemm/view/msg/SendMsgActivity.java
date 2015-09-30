package com.aess.aemm.view.msg;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.aess.aemm.R;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.view.util.DateSelect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;

public class SendMsgActivity extends Activity {
	public static final String TAG = "SendMsgActivity";
	public static final String DEBUGFILENAME = "msgsend.xml";
	public static final String ACTION = "android.intent.action.AEMMSEND";

	public static final int SET_TIME = 1;
	public static final int SET_INFO = 2;
	public static final int GET_SERVICE_INFO = 3;
	public static final int SEND_MSG_INFO = 4;
	public static final int END_ACTIVITY = 5;
	public static final int SEND_MSG_ERROE = 6;

	public static final int msgTimeBeginId = 1;
	public static final int msgTimeEndId = 2;
	public static final int validTimeBeginId = 3;
	public static final int validTimeEndId = 4;

	public static void dataPickEndMsg(int id, String dateString) {
		if (null != mHandler) {
			Message msg = new Message();
			msg.what = SET_TIME;
			msg.arg1 = id;
			msg.obj = dateString;
			mHandler.sendMessage(msg);
		}
	}

	public static void ServiceInfoFlashMsg(int x) {
		if (null != mHandler) {
			if (0 == x) {
				mHandler.sendEmptyMessage(SET_INFO);
			} else {
				Message msg = new Message();
				msg.what = SET_INFO;
				msg.arg1 = x;
				mHandler.sendMessage(msg);
			}

		}
	}

	public static void ServiceInfoGetMsg() {
		if (null != mHandler) {
			mHandler.sendEmptyMessage(GET_SERVICE_INFO);
		}
	}

	public static void SendMsg() {
		if (null != mHandler) {
			mHandler.sendEmptyMessage(SEND_MSG_INFO);
		}
	}

	public static void EndActivity() {
		if (null != mHandler) {
			mHandler.sendEmptyMessage(END_ACTIVITY);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_send);
		setTitle(R.string.msgSend);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SET_TIME: {
					setDate(msg.arg1, (String) msg.obj);
					break;
				}
				case SET_INFO: {
					work = setView();
					if (work == 2) {
						hintView.setVisibility(View.GONE);
					} else {
						Log.w(TAG, "Error is " + msg.arg1);
						String info = SendMsgActivity.this
								.getString(R.string.msgFormatFail);
						txHint.setText(info);
						txHint.setTextColor(Color.RED);
						pbHint.setVisibility(View.GONE);
						bHint.setVisibility(View.VISIBLE);
					}
					break;
				}
				case GET_SERVICE_INFO: {
					if (work < 1) {
						work = 1;
						hintView.setVisibility(View.VISIBLE);
						new Thread() {
							public void run() {
								int x = getResFromService();
								ServiceInfoFlashMsg(x);
							}
						}.start();

					}
					break;
				}
				case SEND_MSG_INFO: {
					new Thread() {
						public void run() {
							int x = sendMsgToService();
							if (x == 0) {
								EndActivity();
							} else {
								Log.w(TAG, "Send Error is " + x);
								mHandler.sendEmptyMessage(SEND_MSG_ERROE);
							}
						}
					}.start();
					break;
				}
				
				case END_ACTIVITY: {
					dismissDialog(0);
					SendMsgActivity.this.finish();
					break;
				}
				case SEND_MSG_ERROE : {
					dismissDialog(0);
					hintView.setVisibility(View.VISIBLE);
					String info = SendMsgActivity.this
							.getString(R.string.msgSendFail);
					txHint.setText(info);
					txHint.setTextColor(Color.YELLOW);
					pbHint.setVisibility(View.GONE);
					bHint.setVisibility(View.VISIBLE);
				}
				}
				super.handleMessage(msg);
			}

		};

		init();

		ServiceInfoGetMsg();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (msgRes.busTypeMap.size() > 0) {
			hintView.setVisibility(View.GONE);
		} else {
			hintView.setVisibility(View.VISIBLE);
		}
		super.onConfigurationChanged(newConfig);
	}

	private void ValidViewVisible(Boolean valid, Boolean revTime) {
		if (valid) {
			metValidInput.setVisibility(View.VISIBLE);
		} else {
			metValidInput.setVisibility(View.GONE);
		}
		
		if (revTime) {
			mtvValidBegin.setVisibility(View.VISIBLE);
			metValidBegin.setVisibility(View.VISIBLE);

			mtvValidEnd.setVisibility(View.VISIBLE);
			metValidEnd.setVisibility(View.VISIBLE);
		} else {
			mtvValidBegin.setVisibility(View.GONE);
			metValidBegin.setVisibility(View.GONE);

			mtvValidEnd.setVisibility(View.GONE);
			metValidEnd.setVisibility(View.GONE);
		}
	}

	private void MsgViewVisible(int MsgType) {
		switch (MsgType) {
		case MessageType.MSG_PLAN:
		case MessageType.MSG_EVENT: {
			metBegin.setVisibility(View.VISIBLE);
			mtvBegin.setVisibility(View.VISIBLE);
			metEnd.setVisibility(View.VISIBLE);
			mtvEnd.setVisibility(View.VISIBLE);

			metPublish.setVisibility(View.GONE);
			mtvPublish.setVisibility(View.GONE);

			if (MsgType == MessageType.MSG_PLAN) {
				mtvState.setVisibility(View.VISIBLE);
				msState.setVisibility(View.VISIBLE);
				mtvLevel.setVisibility(View.GONE);
				msLevel.setVisibility(View.GONE);
			} else {
				mtvState.setVisibility(View.GONE);
				msState.setVisibility(View.GONE);
				mtvLevel.setVisibility(View.VISIBLE);
				msLevel.setVisibility(View.VISIBLE);
			}
			break;
		}
		default: {
			metBegin.setVisibility(View.GONE);
			mtvBegin.setVisibility(View.GONE);
			metEnd.setVisibility(View.GONE);
			mtvEnd.setVisibility(View.GONE);
			if (MsgType == MessageType.MSG_POST) {
				metPublish.setVisibility(View.VISIBLE);
				mtvPublish.setVisibility(View.VISIBLE);
			} else {
				metPublish.setVisibility(View.GONE);
				mtvPublish.setVisibility(View.GONE);
			}
			mtvState.setVisibility(View.GONE);
			msState.setVisibility(View.GONE);
			mtvLevel.setVisibility(View.GONE);
			msLevel.setVisibility(View.GONE);
		}
		}
	}

	private void init() {

		ArrayAdapter<CharSequence> noDateAdapter = ArrayAdapter
				.createFromResource(this, R.array.msgNoDate,
						android.R.layout.simple_spinner_item);
		noDateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		mcbValid = (CheckBox) findViewById(R.id.msgValid);
		metValidInput = (EditText) findViewById(R.id.etValidInput);
		
		mcbRevTime = (CheckBox) findViewById(R.id.msgRecTime);

		metValidEnd = (EditText) findViewById(R.id.etValidEnd);

		mtvValidBegin = (TextView) findViewById(R.id.tvValidBegin);
		metValidBegin = (EditText) findViewById(R.id.etValidBegin);
		imm.hideSoftInputFromWindow(metValidBegin.getWindowToken(), 0);
		DateListener vblistener = new DateListener(validTimeBeginId,
				DateSelect.LESS, metValidEnd);
		metValidBegin.setOnFocusChangeListener(vblistener);
		metValidBegin.setOnClickListener(vblistener);

		mtvValidEnd = (TextView) findViewById(R.id.tvValidEnd);
		DateListener belistener = new DateListener(validTimeEndId,
				DateSelect.MORE, metValidBegin);
		imm.hideSoftInputFromWindow(metValidEnd.getWindowToken(), 0);
		metValidEnd.setOnFocusChangeListener(belistener);
		metValidEnd.setOnClickListener(belistener);

		ValidViewVisible(mcbValid.isChecked(), mcbRevTime.isChecked());
		mcbValid.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				ValidViewVisible(mcbValid.isChecked(), mcbRevTime.isChecked());
			}
		});
		
		mcbRevTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				ValidViewVisible(mcbValid.isChecked(), mcbRevTime.isChecked());
			}
		});

		sMsgType = (Spinner) findViewById(R.id.sMsgType);
		sMsgType.setAdapter(noDateAdapter);
		sMsgType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int type = MessageType.MSG_INFORM;
				if (msgRes.typeMap.size() > 0) {
					Object[] typeKeyArray = msgRes.typeMap.keySet().toArray();
					if (id >= 0 || id < typeKeyArray.length) {
						type = Integer.valueOf((String) typeKeyArray[(int) id]);
					}
				}
				MsgViewVisible(type);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				;
			}
		});

		mtvState = (TextView) findViewById(R.id.tvState);
		msState = (Spinner) findViewById(R.id.sState);
		ArrayAdapter<CharSequence> stateadapter = ArrayAdapter
				.createFromResource(this, R.array.msgPlanState,
						android.R.layout.simple_spinner_item);
		stateadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		msState.setAdapter(stateadapter);

		mtvLevel = (TextView) findViewById(R.id.tvLevel);
		msLevel = (Spinner) findViewById(R.id.slevel);
		ArrayAdapter<CharSequence> leveladapter = ArrayAdapter
				.createFromResource(this, R.array.msgEventLevel,
						android.R.layout.simple_spinner_item);
		leveladapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		msLevel.setAdapter(leveladapter);

		// mtvBusType = (TextView) findViewById(R.id.tvBusType);
		msBusType = (Spinner) findViewById(R.id.sBusType);
		msBusType.setAdapter(noDateAdapter);

		sMsgAgrUser = (Spinner) findViewById(R.id.sAgrUser);
		sMsgAgrUser.setAdapter(noDateAdapter);

		sMsgSenUser = (Spinner) findViewById(R.id.sMsgSenUser);
		sMsgSenUser.setAdapter(noDateAdapter);

		metEnd = (EditText) findViewById(R.id.etEnd);

		metBegin = (EditText) findViewById(R.id.etBegin);
		DateListener mblistener = new DateListener(msgTimeBeginId,
				DateSelect.LESS, metEnd);
		imm.hideSoftInputFromWindow(metBegin.getWindowToken(), 0);
		metBegin.setOnFocusChangeListener(mblistener);
		metBegin.setOnClickListener(mblistener);
		mtvBegin = (TextView) findViewById(R.id.tvBegin);

		DateListener melistener = new DateListener(msgTimeEndId,
				DateSelect.MORE, metBegin);
		imm.hideSoftInputFromWindow(metEnd.getWindowToken(), 0);
		metEnd.setOnFocusChangeListener(melistener);
		metEnd.setOnClickListener(melistener);
		mtvEnd = (TextView) findViewById(R.id.tvEnd);

		metPublish = (EditText) findViewById(R.id.etPublish);
		mtvPublish = (TextView) findViewById(R.id.tvPublish);

		hintView = findViewById(R.id.msgHint);
		txHint = (TextView) findViewById(R.id.msgHintTx);
		bHint = (Button) findViewById(R.id.msgHintBtn);
		bHint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				txHint.setText(R.string.sendMessageHint);
				txHint.setTextColor(Color.WHITE);
				pbHint.setVisibility(View.VISIBLE);
				bHint.setVisibility(View.GONE);
				ServiceInfoGetMsg();
			}

		});
		pbHint = (ProgressBar) findViewById(R.id.msgHintPb);

		btnSend = (Button) findViewById(R.id.sendSure);
		btnSend.setOnClickListener(new SendClickListener());

		btnCancel = (Button) findViewById(R.id.sendCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SendMsgActivity.this.finish();
			}
		});

		metTitle = (TextView) findViewById(R.id.etTitle);
		metContent = (TextView) findViewById(R.id.etContent);

		msSendType = (Spinner) findViewById(R.id.sSendType);
		ArrayAdapter<CharSequence> sendTypeadapter = ArrayAdapter
				.createFromResource(this, R.array.msgSendType,
						android.R.layout.simple_spinner_item);
		sendTypeadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		msSendType.setAdapter(sendTypeadapter);

		metStarUrl = (EditText) findViewById(R.id.etStarUrl);

		MsgViewVisible(0);
	}

	class DateListener implements OnClickListener, OnFocusChangeListener {
		DateListener(int id, int type, TextView view) {
			mId = id;
			mType = type;
			mView = view;
		}

		@Override
		public void onClick(View view) {
			OpenDialog();
		}

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			if (arg1) {
				OpenDialog();
			}
		}

		private void OpenDialog() {
			String date = null;
			if (null != mView) {
				date = mView.getText().toString();
			}
			DateSelect ds = new DateSelect(SendMsgActivity.this, mId, mType, date);
			ds.CreateDailog();
		}

		int mId;
		int mType;
		TextView mView;
	}

	class SendClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			if (msgRes.typeMap.isEmpty() || msgRes.userMap.isEmpty()
					|| msgRes.busTypeMap.isEmpty()) {
				Toast.makeText(SendMsgActivity.this,
						getString(R.string.msgSendErrorHint2),
						Toast.LENGTH_SHORT).show();
				return;
			}

			String errString = getString(R.string.msgSendErrorHint);

			info.mTitile = metTitle.getText().toString();
			StringBuffer sb = new StringBuffer();
			if (null == info.mTitile || info.mTitile.length() < 1) {
				sb.append(getString(R.string.msgdtitle));
			}

			info.mContent = metContent.getText().toString();
			if (null == info.mContent || info.mContent.length() < 1) {
				sb.append(getString(R.string.msgContent));
			}

			long id = sMsgType.getSelectedItemId();
			long count = 0;
			Set<String> keys = msgRes.typeMap.keySet();
			for (String key : keys) {
				if (count == id) {
					info.mTypeKey = key;
					info.mTypeValue = msgRes.typeMap.get(key);
					break;
				}
				count++;
			}
			int type = Integer.valueOf(info.mTypeKey);

			if (MessageType.MSG_EVENT == type || MessageType.MSG_PLAN == type) {
				info.mBegin = metBegin.getText().toString();
				if (null == info.mBegin || info.mBegin.length() < 1) {
					sb.append(mtvBegin.getText());
				}

				info.mEnd = metEnd.getText().toString();
				if (null == info.mEnd || info.mEnd.length() < 1) {
					sb.append(mtvEnd.getText());
				}
			}
			if (MessageType.MSG_POST == type) {
				info.mOrg = metPublish.getText().toString();
				if (null == info.mOrg || info.mOrg.length() < 1) {
					sb.append(mtvPublish.getText());
				}
			}
			if (MessageType.MSG_EVENT == type) {
				info.mEventLevel = (int) msLevel.getSelectedItemId();
			}
			if (MessageType.MSG_PLAN == type) {
				info.mPlanState = (int) msState.getSelectedItemId();
			}
			if (mcbValid.isChecked()) {
				info.mIsValid = true;
				info.mValidNum = metValidInput.getText().toString();
				if (null == info.mValidNum || info.mValidNum.length() < 1) {
					sb.append(getString(R.string.msgValid));
				}
			} else {
				info.mIsValid = false;
			}
			
			if (mcbRevTime.isChecked()) {
				info.mIsRevTime = true;
				
				info.mValidBegin = metValidBegin.getText().toString();
				if (null == info.mValidBegin || info.mValidBegin.length() < 1) {
					sb.append(mtvValidBegin.getText());
				}

				info.mValidEnd = metValidEnd.getText().toString();
				if (null == info.mValidEnd || info.mValidEnd.length() < 1) {
					sb.append(mtvValidEnd.getText());
				}
			} else {
				info.mIsRevTime = false;
			}

			id = sMsgSenUser.getSelectedItemId();
			keys = msgRes.userMap.keySet();
			count = 0;
			for (String key : keys) {
				if (count == id) {
					info.mAcceptUser.put(key, msgRes.userMap.get(key));
					break;
				}
				count++;
			}

			id = sMsgAgrUser.getSelectedItemId();
			keys = msgRes.userMap.keySet();
			count = 0;
			for (String key : keys) {
				if (count == id) {
					info.mAgreeUserKey = key;
					info.mAgreeUserName = msgRes.userMap.get(key);
					break;
				}
				count++;
			}

			id = msBusType.getSelectedItemId();
			keys = msgRes.busTypeMap.keySet();
			count = 0;
			for (String key : keys) {
				if (count == id) {
					info.mBusType = key;
					info.mBusName = msgRes.busTypeMap.get(key);
					break;
				}
				count++;
			}

			info.mSentType = String.valueOf(msSendType.getSelectedItemId());

			info.mPublishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());

			info.mStartURI = metStarUrl.getText().toString();

			if (sb.length() > 1) {
				sb.append(errString);
				Toast.makeText(SendMsgActivity.this, sb.toString(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			showDialog(0);
			SendMsgActivity.SendMsg();
		}
	}

	private void setDate(int id, String date) {
		switch (id) {
		case msgTimeBeginId: {
			metBegin.setText(date);
			break;
		}
		case msgTimeEndId: {
			metEnd.setText(date);
			break;
		}
		case validTimeBeginId: {
			metValidBegin.setText(date);
			break;
		}
		case validTimeEndId: {
			metValidEnd.setText(date);
			break;
		}
		}
	}

	private int setView() {
		int x = 1;
		if (msgRes.typeMap.size() > 0) {
			x = 2;
			List<String> msgTypeList = new ArrayList<String>();
			Set<String> keys = msgRes.typeMap.keySet();
			for (String key : keys) {
				msgTypeList.add(msgRes.typeMap.get(key));
			}
			ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(
					SendMsgActivity.this, android.R.layout.simple_spinner_item,
					android.R.id.text1, msgTypeList);
			typeadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sMsgType.setAdapter(typeadapter);
		}
		if (msgRes.userMap.size() > 0) {
			x = 2;
			List<String> msgUserList = new ArrayList<String>();
			Set<String> keys = msgRes.userMap.keySet();
			for (String key : keys) {
				msgUserList.add(msgRes.userMap.get(key));
			}
			ArrayAdapter<String> agreeAdapter = new ArrayAdapter<String>(
					SendMsgActivity.this, android.R.layout.simple_spinner_item,
					android.R.id.text1, msgUserList);
			agreeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sMsgAgrUser.setAdapter(agreeAdapter);

			// MsgSpinnerAdapter msa = new
			// MsgSpinnerAdapter(SendMsgActivity.this, msgUserList);
			sMsgSenUser.setAdapter(agreeAdapter);

		}
		if (msgRes.busTypeMap.size() > 0) {
			x = 2;
			List<String> msgBTypeList = new ArrayList<String>();
			Set<String> keys = msgRes.busTypeMap.keySet();
			for (String key : keys) {
				msgBTypeList.add(msgRes.busTypeMap.get(key));
			}
			ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(
					SendMsgActivity.this, android.R.layout.simple_spinner_item,
					android.R.id.text1, msgBTypeList);
			typeadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			msBusType.setAdapter(typeadapter);
		}
		return x;
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	private int getResFromService() {
		AutoAdress ad = AutoAdress.getInstance(this);
		String url = ad.getUpdateURL();
		if (null == url) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		url = ad.getUpdateURL();
		if (null != url) {
			commandId++;
			String resBuf = DomXmlBuilder.buildInfo(this, false,
					DomXmlBuilder.MSGRES, String.valueOf(commandId));

			InputStream is = HttpHelp.aemmHttpPost(this, url, resBuf,
					DEBUGFILENAME);
			int x = UpdateXmlParser.parseUpdateXmlForMsg(is, msgRes);
			return x;
		} else {
			return NetUtils.F_LOGIN_ADDRESS_ERROR;
		}
	}

	private int sendMsgToService() {
		AutoAdress ad = AutoAdress.getInstance(this);
		String url = ad.getUpdateURL();
		if (null != url) {
			commandId++;
			info.mCommandId = String.valueOf(commandId);
			String infoBuf = DomXmlBuilder.buildInfo(this, false,
					DomXmlBuilder.MSGINFO, info);

			InputStream is = HttpHelp.aemmHttpPost(this, url, infoBuf,
					DEBUGFILENAME);
			return UpdateXmlParser.parseUpdateXmlForError(is);
		}
		return NetUtils.F_LOGIN_ADDRESS_ERROR;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.msgDoSend));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {

			}
		});
		return dialog;
	}

	private MsgRes msgRes = new MsgRes();
	private MsgRes.MsgInfo info = new MsgRes.MsgInfo();
	private static Handler mHandler;
	private static int commandId = 0;

	private CheckBox mcbValid;
	private EditText metValidInput;
	
	private CheckBox mcbRevTime;
	private TextView mtvValidBegin;
	private EditText metValidBegin;
	private TextView mtvValidEnd;
	private EditText metValidEnd;

	private Spinner sMsgType;

	private Spinner sMsgAgrUser;

	private Spinner sMsgSenUser;

	private TextView mtvState;
	private Spinner msState;

	private TextView mtvLevel;
	private Spinner msLevel;

	private EditText metBegin;
	private TextView mtvBegin;
	private EditText metEnd;
	private TextView mtvEnd;

	private EditText metPublish;
	private TextView mtvPublish;

	private Spinner msBusType;

	private int work = 0;

	private View hintView;
	private TextView txHint;
	private Button bHint;
	private ProgressBar pbHint;

	private Button btnSend;
	private Button btnCancel;

	private TextView metTitle;
	private TextView metContent;

	private Spinner msSendType;

	private EditText metStarUrl;
}
