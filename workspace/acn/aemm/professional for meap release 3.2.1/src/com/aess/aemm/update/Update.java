package com.aess.aemm.update;

import java.io.InputStream;
import java.util.ArrayList;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.aess.aemm.AEMMConfig;
import com.aess.aemm.apkmag.ApkInfoMag;
import com.aess.aemm.authenticator.Constants;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.gps.AEMMLocation;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.DomXmlBuilder.UserInfo;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.protocol.UpdateXmlParser.NotifyResult;
import com.aess.aemm.push.PushService;
import com.aess.aemm.receiver.AlarmReceiver;
import com.aess.aemm.setting.PwPolicySetup;
import com.aess.aemm.traffic.AemmTraffic;
import com.aess.aemm.view.HallMessagedb;
import com.aess.aemm.view.MainView;
import com.aess.aemm.view.NotificationUtils;
import com.aess.aemm.view.UpdateMessageHandler;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.data.MsgType;
import com.aess.aemm.view.data.User;

public class Update {
	public static final String TAG = "Update";
	public static final String DEBUGFILENAME = "Update.xml";

	public static final int CONNECT_TIMEOUT = 120000; // 2 minutes
	public static final int READ_TIMEOUT = 300000; // 5 minutes

	// public static final String MANUALUPDATE = "MANUAL";
	// public static final String AUTOUPDATE = "AUTO";

	public static final String STATE_OK = "ok";
	public static final String STATE_ERRID = "errid";
	public static final String STATE_ERRACCOUNT = "erraccount";
	public static final String STATE_ERRDEVICE = "errdevice";
	public static final String STATE_ERRUNKNOW = "errunknow";
	public static final String STATE_ERREQUIPMENT = "errequipment";

	public static final String SessionID = "session";

	public static final String UpdateType = "type";

	public static final int AUTO = 1;
	public static final int MANUAL = 2;
	public static final int LOGIN = 3;

	public Update(Context c) {
		if (null != c) {
			mContext = c;
		}
	}

	public static Account getAccount(Context cxt) {
		Log.i(TAG, "getAccount");
		Account account = null;
		if (null != cxt) {
			Account[] accounts = AccountManager.get(cxt).getAccountsByType(
					Constants.ACCOUNT_TYPE);
			if (accounts.length > 0) {
				account = accounts[0];
			}
		}
		return account;
	}

	public static final String ATAG = "AEMM";

	public static int checkCondition(Context cxt, int type, boolean login) {
		if (null == cxt) {
			return -1;
		}
		Log.d(TAG, "checkCondition");

		String url = AutoAdress.getInstance(cxt).updateUrl();
		String id = CommUtils.getSessionId(cxt);

		if (login) {
			id = "didn't check";
		}

		if (null == id || id.length() < 1 || null == url || url.length() < 1) {
			if (null == id || id.length() < 1) {
				Log.i(ATAG, "Session Id == null");
			}

			if (null == url || url.length() < 1) {
				Log.i(ATAG, "Update URL == null");
				AutoAdress.intentForNewUpdateURL(cxt);
			}

			String info = cxt
					.getString(com.aess.aemm.R.string.login_error_notlogin);

			broadcastMsg(cxt, info, type);

			return -2;
		}

		if (false == NetUtils.isNetOK(cxt)) {
			Log.i(ATAG, "Net is not ok");

			String info = cxt.getString(com.aess.aemm.R.string.network_erro);

			broadcastMsg(cxt, info, type);

			return -3;
		}

		// LOGIN_ERROR_TRFFIC_LIMIT
		int limit = SocketClient.isTrafficLimtOver(cxt);

		if (limit < 0 && type != MANUAL) {

			return NetUtils.LOGIN_ERROR_TRFFIC_LIMIT;
		}

		// LOGIN_ERROR_IMSI_WRONG
		if (1 == AEMMConfig.imsiCheck) {
			TelephonyManager tm = (TelephonyManager) cxt
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imsinow = tm.getSubscriberId();
			String imsiold = CommUtils.getIMSI(cxt);
			if (imsiold == null || !imsiold.equals(imsinow)) {
				com.aess.aemm.authenticator.Authenticator.delAemmUser(cxt);
				return NetUtils.LOGIN_ERROR_IMSI_WRONG;
			}
		}

		return 1;

	}

	public static int startUpdate(Context cxt, int type) {
		Log.i(TAG, "startUpdate");

		if (0 == AemmTraffic.isWork()) {
			new Thread(AemmTraffic.getInstance(cxt)).run();
		}
		
		if (Update.MANUAL != type) {
			String id = CommUtils.getSessionId(cxt);
			if(null == id || id.length() < 1){
				ViewUtils.finishUpdate(cxt);
				Log.i(TAG, "SessionID == null");
				return -4;
			}
		}

		if (Update.MANUAL == type) {
//			CommUtils.setTrafficLimit(cxt, (long) 0);
			AutoAdress ad = AutoAdress.getInstance(cxt);
			if (null == ad.getUpdateURL()) {
				int result = ad.init();
				if (result < 0) {
					ViewUtils.finishUpdate(cxt);
					Log.w(TAG, "Get address Err");
				}
			}
		}

		int ret = checkCondition(cxt, type, false);
		if (ret < 0) {
			Log.d(TAG, "checkCondition == error");
			ViewUtils.finishUpdate(cxt);
			return -2;
		}

		Account account = Update.getAccount(cxt);
		if (null == account) {
			Log.d(TAG, "Account == null");
			String msg = cxt.getString(com.aess.aemm.R.string.update_failed);
			broadcastMsg(cxt, msg, type);
			ViewUtils.finishUpdate(cxt);
			return -3;
		} else {
			ContentResolver.setSyncAutomatically(account,
					Constants.AUTHTOKEN_TYPE, true);
		}

		Log.i(TAG, "requestSync");

		Bundle bundle = new Bundle();
		bundle.putString(UpdateType, String.valueOf(type));
		ContentResolver.requestSync(account, Constants.AUTHTOKEN_TYPE, bundle);

		return 1;
	}

	// public static int startUpdate2(Context cxt, String type) {
	// Log.i(TAG, "startUpdate");
	// int ret = 0;
	//
	// if (null == cxt || false == NetUtils.isNetOK(cxt)) {
	// boolean manual = false;
	// if (null != type && type.equals(Update.MANUALUPDATE)) {
	// manual = true;
	// }
	// String msg = cxt.getString(com.aess.aemm.R.string.network_erro);
	// broadcastMsg(cxt, msg, manual);
	// ViewUtils.finishUpdate(cxt);
	// return -1;
	// }
	//
	// if (null != type && null != cxt) {
	// Account account = Update.getAccount(cxt);
	//
	// if (null != account) {
	// Log.i(TAG, "requestSync");
	// Bundle bundle = new Bundle();
	// bundle.putString(UpdateType, type);
	// ContentResolver.requestSync(account, Constants.AUTHTOKEN_TYPE,
	// bundle);
	// ret = 1;
	// } else {
	// Log.w(TAG, "didn't update, Account is null");
	// ViewUtils.finishUpdate(cxt);
	// }
	// } else {
	// ViewUtils.finishUpdate(cxt);
	// }
	// return ret;
	// }

	public static int doLogin(Context cxt, String user, String ps, int type) {
		Log.i(TAG, "doLogin");
		int ret = 0;

		if (null == cxt) {
			return -1;
		}

		if (null == user || null == ps) {
			return -1;
		}

		int x = Update.checkCondition(cxt, type, true);
		if (x < 0) {
			return x;
		}

		Update update = new Update(cxt);
		update.setManual(type);

		UpdateResult ur = update.sendLogin(cxt, user, ps);
		if (null == ur) {
			return -3;
		}

		if (0 == ur.mErrorMsg) {

			UpdateDetail.saveCycle(cxt, ur, UpdateDetail.NONE);

			Update.startUpdate(cxt, Update.AUTO);

			ApkInfoMag.sendIntentForApk(cxt);
			AEMMLocation.intentInitLocation(cxt);

			ret = 0;
		} else {
			try {
				ret = Integer.valueOf(ur.mErrorMsg);
			} catch (Exception e) {
				ret = NetUtils.F_UNKNOW;
			}
		}
		return ret;
	}

	public int doUpate() {
		Log.i(TAG, "doUpate");
		int rlt = 0;

		CancelUpdateTimer(mContext);

		UpdateResult ur = sendUpdate();

		dealWtihUpdate(ur);

		responseCommandId(mContext, ur);

		sendHint(ur);

		wipeData(ur);

		restart(ur);

		User.saveUserInfomation(mContext, ur);

		SetUpdateTimer(mContext);

		return rlt;
	}

	public void wipeData(UpdateResult updateRlt) {
		if (null != updateRlt && true == updateRlt.mWipeDevice) {
			PwPolicySetup pwp = PwPolicySetup.getInstance(mContext);
			pwp.wipeData();
		}
	}

	public void restart(UpdateResult updateRlt) {
		if (Process.myUid() == Process.SYSTEM_UID) {
			if (null != updateRlt && true == updateRlt.mRestart) {
				Intent i = new Intent(Intent.ACTION_REBOOT);
				i.putExtra("nowait", 1);
				i.putExtra("interval", 1);
				i.putExtra("window", 0);
				mContext.sendBroadcast(i);
			}
		}
	}

	public void sendHint(UpdateResult ur) {
		String info = null;
		if (null != ur) {
			if (NetUtils.E_SERVER_LIMIT == ur.mErrorMsg) {
				CommUtils.setServiceLimit(mContext, -1);
				PushService.readServiceLimit();
			} else {
				CommUtils.setServiceLimit(mContext, 1);
				PushService.readServiceLimit();
			}
			
			if (0 == ur.mErrorMsg) {
				info = mContext.getString(com.aess.aemm.R.string.update_over);
			} else {
				Log.d(TAG, info + ur.mErrorMsg);
				info = NetUtils.getErrorString(mContext, ur.mErrorMsg);
			}

		} else {
			info = mContext.getString(com.aess.aemm.R.string.update_over);
			Log.d(TAG, info);
		}

		broadcastMsg(mContext, info, upType);
	}

//	public int dealLoginErr(UpdateResult updateRlt) {
//		int rlt = 1;
//		if (null != updateRlt && 0 != updateRlt.mErrorMsg) {
//			Log.d(TAG, "dealUpdateErr");
//
//			String info = mContext
//					.getString(com.aess.aemm.R.string.update_failed);
//
//			broadcastMsg(mContext, info, upType);
//
//			rlt = -1;
//		}
//		return rlt;
//	}

	// public static void responseWipeDevice(Context cxt, UpdateResult
	// updaterlt) {
	// if (null == cxt || null == updaterlt) {
	// return;
	// }
	// if (true == updaterlt.mWipeDevice) {
	// if (null != updaterlt.mWipeId && updaterlt.mWipeId.length() > 0) {
	// updaterlt.mWipeId = updaterlt.mWipeId + ":1";
	// responseCommandId(cxt, updaterlt);
	// }
	// }
	// }

	public static void responseCommandId(Context cxt, UpdateResult updaterlt) {
		if (null == cxt) {
			return;
		}
		if (AEMMConfig.VER < AEMMConfig.NUM) {
			return;
		}
		Log.d(TAG, "responseCommandId");

		AutoAdress ad = AutoAdress.getInstance(cxt);
		String url = ad.getUpdateURL();

		if (null != url) {
			String data = DomXmlBuilder.buildInfo(cxt, false,
					DomXmlBuilder.COMMANDS, updaterlt);
			HttpHelp.aemmHttpPost(cxt, url, data, "UpCommand.xml");
		}
	}

	public static void responseCommandId(Context cxt, String commandId) {
		if (null == cxt) {
			return;
		}
		if (AEMMConfig.VER < AEMMConfig.NUM) {
			return;
		}
		AutoAdress ad = AutoAdress.getInstance(cxt);
		String url = ad.getUpdateURL();

		if (null != url) {

			if (null == commandId) {
				return;
			}
			String data = DomXmlBuilder.buildInfo(cxt, false,
					DomXmlBuilder.COMMAND, commandId);
			HttpHelp.aemmHttpPost(cxt, url, data, "UpCommand.xml");
		}
	}

	public void setManual(int value) {
		upType = value;
	}

	public static final String LOGINFILENAME = "authenticate.xml";

	public UpdateResult sendLogin(Context cxt, String user, String ps) {
		Log.i(TAG, "sendLogin");

		String url = AutoAdress.getInstance(cxt).updateUrl();

		if (null == url) {
			Log.d(TAG, "Upate Url == null");
			return null;
		}

		UserInfo ui = new UserInfo(user, ps);
		String lgInfo = DomXmlBuilder.buildInfo(cxt, true, DomXmlBuilder.LOGIN,
				ui);

		if (null == lgInfo || lgInfo.length() < 1) {
			Log.d(TAG, "XmlBuilder.buildInfo == null");
			return null;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(cxt, url, lgInfo,
				LOGINFILENAME);

		if (null == upResult) {
			Log.d(TAG, "login respons == null");
			return null;
		}

		UpdateResult urlt = new UpdateResult();

		urlt = new UpdateXmlParser(cxt).parseUpdateXml(upResult, 0);

		return urlt;
	}

	private UpdateResult sendUpdate() {
		Log.i(TAG, "sendUpdate");

		String url = AutoAdress.getInstance(mContext).updateUrl();

		if (null == url) {
			Log.d(TAG, "Upate Url == null");
			return null;
		}

		String upInfo = DomXmlBuilder.buildInfo(mContext, false,
				DomXmlBuilder.UPDATE, null);

		if (null == upInfo || upInfo.length() < 1) {
			Log.d(TAG, "XmlBuilder.buildInfo == null");
			return null;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(mContext, url, upInfo,
				DEBUGFILENAME);

		if (null == upResult) {
			Log.d(TAG, "aemmHttpPost == null");
			return null;
		}

		UpdateResult urlt = new UpdateResult();

		urlt = new UpdateXmlParser(mContext).parseUpdateXml(upResult, 1);

		return urlt;
	}
	
	public static UpdateResult sendUser(Context mContext){
		final String TAG="sendUser Method"; 
		Log.i(TAG, "start ...");

		String url = AutoAdress.getInstance(mContext).updateUrl();

		if (null == url) {
			Log.d(TAG, "Url == null");
			return null;
		}

		String upInfo = DomXmlBuilder.buildInfo(mContext, false,DomXmlBuilder.USER, null);

		Log.d(TAG, "XmlBuilder.buildInfo->"+upInfo);
		if (TextUtils.isEmpty(upInfo)) {
			return null;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(mContext, url, upInfo,DEBUGFILENAME);

		if (null == upResult) {
			Log.d(TAG, "aemmHttpPost == null");
			return null;
		}

		UpdateResult urlt = UpdateXmlParser.getUpdateResultByErr();

		urlt = new UpdateXmlParser(mContext).parseXml(mContext,upResult,null,DomXmlBuilder.USER);//parseUpdateXml(upResult, 1);
		User.saveUserInfomation(mContext, urlt);

		User.saveUserInfomation(mContext, urlt);
		return urlt;
	}

	private int dealWtihUpdate(UpdateResult ur) {

		UpdateDetail ud = new UpdateDetail();
		String msg = null;

		Log.i(TAG, "Update Detail Begin");

		if (null == ur) {
			return -1;
		}

		UpdateDetail.saveCycle(mContext, ur, UpdateDetail.RESETGPS);

		if (null != ur.mProfileVersion || null != ur.mVpnProfileVersion) {
			msg = ud.updateProfile(mContext, ur);
			if (msg != null) {
				broadcastMsg(mContext, msg, upType);
				msg = null;
				if (null != ur.mProfileId && ur.mProfileId.length() > 0) {
					ur.mProfileId = String.format("%s:1", ur.mProfileId);
				}
			}
		}

		try {
			boolean send = false;
			if (null != ur.mAppListVersion) {
				msg = ud.updateApp(mContext, ur);

				PushService.autoinstall = CommUtils.getSilInstall(mContext);
				if (1 == PushService.autoinstall) {
					ArrayList<ApkContent> newapklist = ApkContent
							.queryAllContents(mContext);
					if (null != newapklist && newapklist.size() > 0) {
						send = true;
					}
				}
				hintMessage(msg, send);
				if (null != ur.mAppListId && ur.mAppListId.length() > 0) {
					ur.mAppListId = String.format("%s:1", ur.mAppListId);
				}
			}

			msg = ud.updateAccount(mContext, ur);
			if (msg != null) {
				hintMessage(null, false);
				msg = null;
			}

			if (null != ur.mBlackListVersion) {
				msg = ud.updateBlackApp(mContext, ur);
				if (msg != null) {
					broadcastMsg(mContext, msg, upType);
					msg = null;
				}
				if (null != ur.mBlackListId && ur.mBlackListId.length() > 0) {
					ur.mBlackListId = String.format("%s:1", ur.mBlackListId);
				}
			}

			if (null != ur.mRemoveList) {
				msg = ud.remoteDelApk(mContext, ur);
				if (msg != null) {
					broadcastMsg(mContext, null, upType);
					msg = null;
				}
			}

			if (null != ur.mClientVersion) {
				msg = ud.updateClient(mContext, ur);
				if (msg != null) {
					broadcastMsg(mContext, msg, upType);
					msg = null;
				}
			}
			
			UpdateDetail.updateMessage(mContext, ur);

			if (null != ur.mNotifyList) {
				for (NotifyResult nr : ur.mNotifyList) {
					NotificationUtils.sentNotificationForNotity(mContext, nr);
					if (null != nr.mCommandId && nr.mCommandId.length() > 0) {
						nr.mCommandId = String.format("%s:1", nr.mCommandId);
					}
				}
			}

			if (send) {
				PushService.sendAutoInsBeginMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != ur.mStartAppPName) {
			ud.startApp(mContext, ur.mStartAppPName);
			if (null != ur.mStartAppId && ur.mStartAppId.length() > 0) {
				ur.mStartAppId = String.format("%s:1", ur.mStartAppId);
			}
		}

		if (true == ur.mCleanLock) {
			PwPolicySetup.clearDevicePs(mContext);
			if (null != ur.mCleanId && ur.mCleanId.length() > 0) {
				ur.mCleanId = String.format("%s:1", ur.mCleanId);
			}
		}
		boolean showhit = true;
		if (true == ur.mCleanLock) {
			showhit = !ur.mCleanLock;
		}

		if (true == ur.mLockDevice) {
			int ret = PwPolicySetup.lockDevice(mContext, ur.mLockPassword,
					showhit);
			if (null != ur.mLockId && ur.mLockId.length() > 0) {
				if (ret > 0) {
					ur.mLockId = String.format("%s:1", ur.mLockId);
				} else {
					ur.mLockId = String.format("%s:2", ur.mLockId);
				}
			}
		}

		if (true == ur.mDevice) {
			if (null != ur.mDeviceId && ur.mDeviceId.length() > 0) {
				ur.mDeviceId = String.format("%s:1", ur.mDeviceId);
			}
		}

		if (true == ur.mWipeDevice) {
			if (null != ur.mWipeId && ur.mWipeId.length() > 0) {
				ur.mWipeId = String.format("%s:1", ur.mWipeId);
			}
		}

		if (true == ur.mRestart) {
			if (null != ur.mRestartId && ur.mRestartId.length() > 0) {
				ur.mRestartId = String.format("%s:1", ur.mRestartId);
			}
		}

		Log.i(TAG, "Update Detail End");

		return 0;
	}

	public void hintMessage(String msg, boolean autoIns) {
		int type = MsgType.APP_UPATE_SILLENCE;
		if (null != msg) {
			broadcastMsg(mContext, msg, upType);
			type = MsgType.APP_UPDATE;
			msg = null;
		}
		if (LOGIN == upType) {
			type = MsgType.APP_UPATE_SILLENCE;
		}

		if (autoIns) {
			MainView.sendAutoInsBegMessage();
		}

		ViewUtils.update(mContext, type, null);
	}

	public static void broadcastMsg(Context context, String info) {

		HallMessagedb hmsg = new HallMessagedb(info, 0, 10000, 10000,
				HallMessagedb.RESULTMSG);
		UpdateMessageHandler.addMessage(context, hmsg);
	}

	public static void hitFlowLimit(Context context) {
		String info = context
				.getString(com.aess.aemm.R.string.trafficlimitover);
		if (null != MainView.mHall && MainView.mHall.atvState < MainView.STOP) {
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
	}
	
	public static void hitServiceLimit(Context context) {
		String info = context
				.getString(com.aess.aemm.R.string.server_limit_hint);
		if (null != MainView.mHall && MainView.mHall.atvState < MainView.STOP) {
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
	}

	public static void broadcastMsg(Context context, String info, int type) {
		if (MANUAL == type) {
			HallMessagedb msg = new HallMessagedb(info, 0, 2000, 2000,
					HallMessagedb.UPDATEMSG);
			ViewUtils.addResultMessage(msg);
		}
	}

	public static void CancelUpdateTimer(Context context) {
		Log.i(TAG, "CancelUpdateTimer");

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.UPDATE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		alarmManager.cancel(pendingIntent);
	}

	public static void SetUpdateTimer(Context context) {

		long timelong = CommUtils.getCheckTime(context);

		if (timelong < 1) {
			return;
		}

		Log.i(TAG,
				String.format(" SetUpdateTimer : %s", String.valueOf(timelong)));

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.UPDATE);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);

		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ timelong, pendingIntent);
	}

	// private static String getLanuage() {
	// // "en-us"
	// return "zh-cn";
	// }

	// private InputStream httpGet(String urlStr) {
	// Log.i(TAG, "httpGet");
	//
	// InputStream input = null;
	// HttpURLConnection conn = null;
	// try {
	// URL url = new URL(urlStr);
	//
	// conn = (HttpURLConnection) url.openConnection();
	//
	// // 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该ε铰ッ徦喢ニ喡好柯�
	// int responseCode = conn.getResponseCode();
	// if (HttpURLConnection.HTTP_OK == responseCode) {
	// Log.i(TAG, "HTTP OK");
	//
	// StringBuffer buffer = new StringBuffer("");
	// InputStream httpis = conn.getInputStream();
	//
	// if (null != httpis) {
	// BufferedReader bf = new BufferedReader(
	// new InputStreamReader(httpis));
	// String line = "";
	// while ((line = bf.readLine()) != null) {
	// buffer.append(line);
	// }
	// }
	// input = new ByteArrayInputStream(buffer.toString().getBytes());
	// } else {
	// StateMsg = "";
	// Log.i(TAG, "HTTP Err : " + String.valueOf(responseCode));
	// }
	// } catch (Exception e) {
	// StateMsg = "";
	// e.printStackTrace();
	// } finally {
	// if (conn != null)
	// conn.disconnect();
	// }
	// return input;
	// }
	//
	// private void setHttp(HttpURLConnection con, String Content_length) {
	// Log.i(TAG, "setHttp");
	// // 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路
	// con.setDoOutput(true); // 盲陆驴茅鈥澟该︹�陇忙鈥孤�URL
	// 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙沤楼忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该柯�
	// con.setDoInput(true); // 盲陆驴茅鈥澟该︹�陇忙鈥孤�URL
	// 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙沤楼忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤�
	// con.setUseCaches(false); // 茅鈥澟该︹�陇忙鈥孤访┾�鸥莽拧鈥犆ぢ尖劉忙鈥孤访┾�鸥忙鈥撀っ︹�路
	//
	// // 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路URL茅鈥澟该︹�陇忙鈥孤访┾�鸥猫楼鸥忙鈥撀姑モ�陇忙鈥孤�
	// try {
	// con.setRequestMethod("POST");
	// } catch (ProtocolException e) {
	// e.printStackTrace();
	// }
	//
	// // 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路
	// //
	// 茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥猫隆鈥斆︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路猫锟铰┟┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该ぢ韭ッ︹�鈥姑︹�路茅鈥澟该寂捗┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该モ�驴猫戮戮忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤访┾�鸥盲戮楼忙鈥犫�忙鈥孤访┾�鸥忙鈥撀っ︹�路盲赂鈧┾�鸥忙鈥撀っ︹�路
	//
	// con.setRequestProperty("Content-length", Content_length);
	// con.setRequestProperty("Content-Type", "application/octet-stream");
	// con.setRequestProperty("Connection", "Keep-Alive");//
	// 莽禄麓茅鈥澟该♀�莽颅鹿忙鈥孤访┾�鸥忙鈥撀っ︹�路茅鈥澟该︹�陇忙鈥孤�
	// con.setRequestProperty("Charset", "UTF-8");
	// con.setRequestProperty("Language", getLanuage());
	// con.setConnectTimeout(CONNECT_TIMEOUT);
	// con.setReadTimeout(READ_TIMEOUT);
	// }
	//
	// private InputStream HttpWrite(byte[] postBody) {
	// Log.i(TAG, "HttpWrite");
	//
	// InputStream is = null;
	// if (null != postBody && postBody.length > 0) {
	// HttpURLConnection httpConn = null;
	// try {
	// // connect
	// URL url = new URL(UPDATE_URL);
	// httpConn = (HttpURLConnection) url.openConnection();
	//
	// // set property
	// setHttp(httpConn, String.valueOf(postBody.length));
	//
	// // write
	// OutputStream outputStream = httpConn.getOutputStream();
	// outputStream.write(postBody);
	// outputStream.close();
	//
	// Log.i(TAG, "http.write ok");
	//
	// // get response
	// int responseCode = httpConn.getResponseCode();
	//
	// if (HttpURLConnection.HTTP_OK == responseCode) {
	// // update OK
	// Log.i(TAG, "http.read ok");
	//
	// InputStream httpis = httpConn.getInputStream();
	// BufferedReader reader = new BufferedReader(
	// new InputStreamReader(httpis));
	//
	// StringBuffer buffer = new StringBuffer("");
	// String line = "";
	// while ((line = reader.readLine()) != null) {
	// buffer.append(line);
	// }
	// if (buffer.toString().length() > 0) {
	// is = new ByteArrayInputStream(buffer.toString()
	// .getBytes());
	// }
	//
	// } else {
	// // update fail
	// Log.i(TAG, "HTTP Response Fail, Code : "
	// + String.valueOf(responseCode));
	// StateMsg = "";
	// }
	// } catch (Exception ex) {
	// // update err
	// if (null != ex) {
	// Log.i(TAG, "HTTP Response Error, Code : "
	// + String.valueOf(ex.toString()));
	// }
	// StateMsg = "";
	// ex.printStackTrace();
	// } finally {
	// // need to close connection
	// if (httpConn != null) {
	// httpConn.disconnect();
	// }
	// }
	// }
	//
	// return is;
	// }

	// ViewUtils

	// private int noneUpdate(UpdateResult rlt) {
	// int x = 0;
	// if (null != rlt) {
	// if (null != rlt.mProfileList) {
	// x = x | 1;
	// }
	//
	// if (null != rlt.mProfileVersion) {
	// x = x | 1;
	// }
	//
	// if (null != rlt.mAppList) {
	// x = x | 2;
	// }
	//
	// if (null != rlt.mAppListVersion) {
	// x = x | 2;
	// }
	//
	// if (null != rlt.mBlackList) {
	// x = x | 4;
	// }
	//
	// if (null != rlt.mBlackListVersion) {
	// x = x | 4;
	// }
	//
	// if (null != rlt.mClientUpdateUrl) {
	// x = x | 8;
	// }
	//
	// if (null != rlt.mClientVersion) {
	// x = x | 8;
	// }
	//
	// if (true == rlt.mWipeDevice) {
	// x = x | 16;
	// }
	//
	// if (true == rlt.mLockDevice) {
	// x = x | 32;
	// }
	//
	// if (null != rlt.mRemoteUninstallAppList
	// && false == rlt.mRemoteUninstallAppList.isEmpty()) {
	// x = x | 64;
	// }
	// }
	//
	// return x;
	// }

	// private void errInfo(String err) {
	// Log.i(TAG, "dealWithErr");
	// if (err.equals(STATE_ERRACCOUNT) || err.equals(STATE_ERRID)
	// || err.equals(STATE_ERREQUIPMENT)
	// || err.equals(STATE_ERRUNKNOW) || err.equals(STATE_ERRDEVICE)) {
	//
	// }
	// }

	private Context mContext = null;
	private int upType = 0;
}