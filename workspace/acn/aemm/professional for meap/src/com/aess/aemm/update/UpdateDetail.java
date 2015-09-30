package com.aess.aemm.update;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.commonutils.HelpUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.gps.AEMMLocation;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.protocol.ProfileXmlParser;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.protocol.UpdateXmlParser.ProfileResult;
import com.aess.aemm.protocol.UpdateXmlParser.RemoveResult;
import com.aess.aemm.push.PushService;
import com.aess.aemm.receiver.AlarmReceiver;
import com.aess.aemm.view.MainView;
import com.aess.aemm.view.NotificationUtils;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.data.MsgType;
import com.aess.aemm.view.msg.CalendarAdd;
import com.aess.aemm.view.msg.MessageType;
import com.aess.aemm.view.sso.SSO;

public class UpdateDetail {

	public static final String TAG = "UpdateDetail";

	public static final String INSTALL = String.valueOf(Appdb.INSTALLED);
	public static final String REMOTE = String.valueOf(Appdb.REMOTE_UNINSTALL);
	public static final String NEWAPP = String.valueOf(Appdb.NEWAPP);
	public static final String UNINSTALLED = String.valueOf(Appdb.UNINSTALLED);
	public static final String SVERSION = "0";

	public static final int NONE = 0;
	public static final int RESETGPS = 1;

	public static int saveCycle(Context cxt, UpdateResult ur, int type) {
		int rlt = 0;
		if (null == ur) {
			return rlt;
		}

		HelpUtils.saveSession(cxt, ur.mSessionId);

		if (0 != ur.mCheckCycle) {
			String value = String.valueOf(ur.mCheckCycle);
			String old = CommUtils.getCheckCycle(cxt);
			if (!value.equals(old)) {
				CommUtils.setCheckCycle(cxt, value);
			}
			rlt = 1;
		}

		boolean send = false;
		if (null != ur.mLocationRange) {
			String value = ur.mLocationRange;
			String old = CommUtils.getLocRange(cxt);
			if (!value.equals(old)) {
				CommUtils.setLocRange(cxt, value);
				send = true;
			}
			rlt = 1;
		}

		if (0 != ur.mLocationCycle) {
			String value = String.valueOf(ur.mLocationCycle);
			String oldvalue = CommUtils.getLocCycle(cxt);
			if (null == oldvalue || oldvalue.length() < 1
					|| !oldvalue.equals(value)) {
				CommUtils.setLocCycle(cxt, value);
				send = true;
			}
			rlt = 1;
		}

		if (send && RESETGPS == type) {
			AEMMLocation.intentResetLocation(cxt);
			AlarmReceiver.SendIntentForTime(cxt, AlarmReceiver.LOCTIME);
		}

		if (0 != ur.mAppCycle) {
			String value = String.valueOf(ur.mAppCycle);
			String oldvalue = CommUtils.getAppCycle(cxt);
			if (null == oldvalue || oldvalue.length() < 1
					|| !oldvalue.equals(value)) {
				CommUtils.setAppCycle(cxt, value);
				AlarmReceiver.SendIntentForTime(cxt, AlarmReceiver.APKTIME);
			}
			rlt = 1;
		}

		if (ur.mSilentInstall > -1) {
			CommUtils.setSilInstall(cxt, ur.mSilentInstall);
		}

		if (ur.mTraffic > -1) {
			CommUtils.setTrafficLimit(cxt, ur.mTraffic);
			PushService.readPushLimitMsg();
		}

		return rlt;
	}

	public String updateProfile(Context context, UpdateResult result) {
		Log.i(TAG, "CONFIG_UPDATE");
		String StateMsg = null;
		for (ProfileResult config : result.mProfileList) {

			String url = config.mConfigUrl;

			InputStream configXml = HttpHelp.aemmHttpGet(context, url,
					"configxml.xml");

			if (null != configXml) {
				ProfileXmlParser parser = new ProfileXmlParser(context);
				StateMsg = parser.parseProfileXml(configXml, result);
			}
		}

		if (null != result.mProfileVersion) {
			CommUtils.setProfileVersion(context, result.mProfileVersion);
		}

		if (null != result.mVpnProfileVersion) {
			CommUtils.setVpnProfileVersion(context, result.mVpnProfileVersion);
		}
		return StateMsg;
	}

	public static String updateMessage(Context context, UpdateResult ur) {
		Log.i(TAG, "updateMessage");
		int msgCount = 0;
		int addCount = 0;
		Uri uri = null;
		ArrayList<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		if (null != ur.mMessagesList) {
			for (NewsContent nsNew : ur.mMessagesList) {
				msgCount++;
				if (MessageType.MSG_PLAN == nsNew.mType
						|| MessageType.MSG_EVENT == nsNew.mType) {
					addCount++;
				}

				String selfpname = context.getPackageName();
				if (null == selfpname) {
					selfpname = "com.aess.aemm";
				}

				uri = nsNew.add(context);
				boolean appflag = false;
				if (nsNew.mStartUri != null && nsNew.mStartUri.length() > 0) {
					appflag = true;
				}

				String flag = "";
				StringBuffer limhit = new StringBuffer();
				if (true == appflag) {
					if (!selfpname.equals(nsNew.mStartUri)) {
						msgCount--;

						ArrayList<ApkContent> apklist = ApkContent
								.queryAllContents(context);
						for (ApkContent ac : apklist) {
							if (nsNew.mStartUri.equals(ac.mApkPackageName)) {
								if (1 == ac.mApkDisabled) {
									limhit.append(flag + nsNew.mStartUri);
									flag = ", ";
									break;
								}
							}
						}

						if (SSO.FRIEND.equals(nsNew.mStartUri)) {
							HashMap<String, String> msg = new HashMap<String, String>();
							msg.put("title", nsNew.mTitile);
							msg.put("state", nsNew.mTypeName);
							msg.put("content", nsNew.mContent);
							msgList.add(msg);
						} else {
							NotificationUtils.sentNotificationForApp2(context,
									1, nsNew.mStartUri);
						}
					}
				}

				if (limhit.length() > 0) {
					limhit.append(" ");
					String info = context.getString(R.string.app_disable);
					limhit.append(info);
					Toast.makeText(context, limhit, Toast.LENGTH_SHORT).show();
				}

				if (null != nsNew.mCommandId && nsNew.mCommandId.length() > 0) {
					nsNew.mCommandId = String.format("%s:1", nsNew.mCommandId);
				}
			}
			
			if (msgList.size() > 0) {
				NotificationUtils.sentNotificationForApp2(context,
						2, msgList);
			}
			
			if (msgCount > 0) {
				NotificationUtils
						.sentNotificationForMsg(context, msgCount, uri);
				MainView.sendMsgCheck();
			}
			
			if (addCount > 0) {
				new CalendarAdd(context).addCalendar();
			}
		}

		return null;
	}

	public String updateApp(Context context, UpdateResult result) {
		if (null == result.mAppListVersion) {
			return null;
		}

		Log.i(TAG, "APP_UPDATE");

		String msg = null;

		msg = UpdatePushAppList(context, result);

		return msg;
	}

	public String updateAccount(Context context, UpdateResult result) {

		Log.i(TAG, "update App Account");

		String msg = null;

		ArrayList<ApkContent> apklist = ApkContent.queryAllContents(context);
		Set<String> apkIdSet = result.mAppAccount.keySet();
		if (null != apklist) {
			for (int y = 0; y < apklist.size(); y++) {
				ApkContent apkc = apklist.get(y);
				msg = null;
				for (String id : apkIdSet) {
					if (apkc.mApkId.equals(id)) {
						String name = result.mAppAccount.get(id);
						apkc.mSSOAccount = name;
						apkc.update(context);
						msg = "1";
						break;
					}
				}
				if (TextUtils.isEmpty(msg)) {
					if (!TextUtils.isEmpty(apkc.mSSOAccount)) {
						apkc.mSSOAccount = "";
						apkc.update(context);
						msg = "2";
					}
				}
			}
		}

		return msg;
	}

	private ComponentName getComponentName(Context cxt, String packageName) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(packageName);

		List<ResolveInfo> infoList = cxt.getPackageManager()
				.queryIntentActivities(intent, 0);
		if (infoList != null && infoList.size() > 0) {
			ResolveInfo info = infoList.get(0);
			if (info != null) {
				String pkgName = info.activityInfo.packageName;
				String clsName = info.activityInfo.name;
				return new ComponentName(pkgName, clsName);
			}
		}
		return null;
	}

	private void forceStopPackage(Context cxt, String pkgName) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.forceStopPackage(pkgName);
	}

	public String stopApp(Context cxt, UpdateResult result) {
		Log.i(TAG, "APP_START");

		String msg = null;

		if (null == result || null == result.mRemoveList
				|| result.mRemoveList.size() < 1) {
			return msg;
		}

		String id = "com.sds.android.ttpod";
		forceStopPackage(cxt, id);
		return null;
	}

	public String startApp(Context cxt, String packageId) {
		Log.i(TAG, "APP_START");

		String msg = null;

		if (null == packageId || packageId.length() < 0) {
			return msg;
		}

		ApkContent oldapk = ApkContent.queryContentBy_PKG_FLAG(cxt, packageId,
				Appdb.INSTALLED);

		if (null != oldapk && oldapk.mApkDisabled > 0) {
			return msg;
		}

		List<PackageInfo> packageList = cxt.getPackageManager()
				.getInstalledPackages(0);

		if (null == packageList) {
			return msg;
		}

		for (PackageInfo info : packageList) {
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				continue;
			}

			if (packageId.equals(info.packageName)) {
				ComponentName comp = getComponentName(cxt, info.packageName);
				if (null == comp) {
					return msg;
				}
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClassName(comp.getPackageName(), comp.getClassName());
				cxt.startActivity(intent);
			}
		}
		return msg;
	}

	public String updateBlackApp(Context context, UpdateResult result) {

		if (null == result.mBlackListVersion) {
			return null;
		}

		Log.i(TAG, "BLACK_APP_UPDATE");

		String msg = UpdateBlackApplist(context, result);

		ViewUtils.update(context, MsgType.APP_UPATE_SILLENCE, null);

		return msg;
	}

	public String remoteDelApk(Context context, UpdateResult result) {
		String StateMsg = null;

		if (null == result.mRemoveList) {
			return null;
		}

		if (result.mRemoveList.isEmpty()) {
			return null;
		}

		Log.i(TAG, "REMOTE_DELETE");

		ArrayList<ApkContent> oldapklist = ApkContent.queryAllContents(context);
		int state = 0;
		for (RemoveResult removeApk : result.mRemoveList) {
			for (ApkContent oldapk : oldapklist) {
				if (true == sampleApk(removeApk, oldapk)) {
					if (INSTALL.equals(oldapk.mApkFlag)
							|| REMOTE.equals(oldapk.mApkFlag)) {
						oldapk.mApkFlag = REMOTE;
						oldapk.update(context);
						removeApk.mPackageName = oldapk.mApkPackageName;
						state = 1;
						break;
					}
				}
			}
		}

		if (state > 0) {
			ViewUtils.update(context, MsgType.APP_UPATE_SILLENCE, null);
		}

		for (RemoveResult remapk : result.mRemoveList) {
			if (null != remapk.mPackageName) {
				if (null != remapk.mCommandId && remapk.mCommandId.length() > 0) {
					remapk.mCommandId = String
							.format("%s:1", remapk.mCommandId);
				}
				ViewUtils.unInstallApplicationSilence(context,
						remapk.mPackageName);
			}
		}

		return StateMsg;
	}

	public String updateClient(Context context, UpdateResult result) {

		if (null == result.mClientUpdateUrl) {
			return null;
		}

		if (null == result.mClientVersion) {
			return null;
		}

		Log.i(TAG, "CLIENT_UPDATE");

		String StateMsg = null;
		if (null != result.mClientVersion && null != result.mClientUpdateUrl) {
			CommUtils.setNewClientURL(context, result.mClientUpdateUrl);
			CommUtils.setNewClientVersion(context, result.mClientVersion);
			CommUtils.setCurrentClientName(context, result.mClientName);
			ViewUtils.update(context, MsgType.CLIENT_UPDATE, null);
		}
		return StateMsg;
	}

	private String UpdatePushAppList(Context context, UpdateResult result) {
		Log.i(TAG, "APP_UPDATE");
		int count = 0;
		String StateMsg = null;

		ArrayList<ApkContent> oldapklist = ApkContent.queryAllContents(context);

		if (null != oldapklist) {
			for (ApkContent oldapk : oldapklist) {
				for (ApkContent newapk : result.mAppList) {
					if (true == sampleApk(oldapk, newapk)) {
						if (null == oldapk.mApkUrl
								|| oldapk.mApkUrl.length() < 2) {
							if (oldapk.mApkDisabled > 0) {
								newapk.mApkDisabled = 1;
							}
						} else {
							oldapk.mIsDeal = true;
							newapk.mIsDeal = true;
						}
						updateApkProfileContent(context, oldapk, newapk);
						break;
					}
				}

				if (oldapk.mIsDeal == false) {
					int flag = -1;
					if (null != oldapk.mApkFlag) {
						flag = Integer.parseInt(oldapk.mApkFlag);
					}
					if (Appdb.INSTALLED == flag || Appdb.DOWNLOAD == flag) {
						oldapk.mApkPublished = 0;
						oldapk.update(context);
					} else {
						oldapk.delete(context);
					}
				}
			}
		}

		for (ApkContent newapk : result.mAppList) {
			if (newapk.mIsDeal == false) {
				newapk.add(context);
				count++;
			}
		}

		result.mAppList = null;
		CommUtils.setAppListVersion(context, result.mAppListVersion);
		if (count > 0) {
			StateMsg = String.valueOf(count);
		}
		return StateMsg;
	}

	public void updateApkProfileContent(Context context, ApkContent oldapk,
			ApkContent newapk) {
		boolean update = false;
		if (null == oldapk.mApkDesc || !oldapk.mApkDesc.equals(newapk.mApkDesc)) {
			oldapk.mApkDesc = newapk.mApkDesc;
			update = true;
		}
		if (null == oldapk.mApkUrl || !oldapk.mApkUrl.equals(newapk.mApkUrl)) {
			oldapk.mApkUrl = newapk.mApkUrl;
			update = true;
		}
		if (oldapk.mApkPublished < 1) {
			oldapk.mApkPublished = 1;
		}
		if (true == update) {
			oldapk.update(context);
		}
	}

	private String UpdateBlackApplist(Context context, UpdateResult result) {
		Log.i(TAG, "BLACK APP_UPDATE");
		String StateMsg = null;

		ArrayList<ApkContent> oldapklist = ApkContent.queryAllContents(context);

		clearBlackList(context, oldapklist);

		dealBlackList(context, result, oldapklist);

		CommUtils.setBlackListVersion(context, result.mBlackListVersion);

		result.mBlackList = null;

		return StateMsg;
	}

	private String dealBlackList(Context context, UpdateResult result,
			ArrayList<ApkContent> oldapklist) {
		String msg = null;
		if (null != oldapklist) {
			for (ApkContent oldapk : oldapklist) {
				for (ApkContent blackapk : result.mBlackList) {
					// if(!TextUtils.isEmpty(oldapk.mApkId)
					// &&oldapk.mApkId.equals(blackapk.mApkId)
					// &&!TextUtils.isEmpty(oldapk.mApkVersion)
					// &&(oldapk.mApkVersion.equals(blackapk.mApkVersion)||ApkProfileContent.ALLVERSION.equals(blackapk.mApkVersion))){
					if (true == sampleApk(oldapk, blackapk)) {
						if (0 == oldapk.mApkDisabled) {
							oldapk.mApkDisabled = 1;
							oldapk.update(context);
						}
						blackapk.mIsDeal = true;
						break;
					}
				}
			}
		}

		for (ApkContent blackapk : result.mBlackList) {
			if (false == blackapk.mIsDeal) {
				if (0 == blackapk.mApkDisabled) {
					blackapk.mApkDisabled = 1;
				}
				blackapk.add(context);
			}
		}
		return msg;
	}

	private void clearBlackList(Context context,
			ArrayList<ApkContent> oldapklist) {

		if (null != oldapklist) {
			for (ApkContent oldapk : oldapklist) {
				int flag = -1;
				if (null != oldapk.mApkFlag) {
					flag = Integer.parseInt(oldapk.mApkFlag);
				}
				if (0 != oldapk.mApkDisabled) {
					if (1 == oldapk.mApkPublished || Appdb.INSTALLED == flag
							|| Appdb.REMOTE_UNINSTALL == flag) {
						oldapk.mApkDisabled = 0;
						oldapk.update(context);
					} else {
						oldapk.delete(context);
					}
				}
			}
		}
	}

	private boolean sampleApk(RemoveResult one, ApkContent two) {
		boolean rlt = false;
		if (null != one) {
			if (one.mAppId.equals(two.mApkId)
					&& one.mAppVer.equals(two.mApkVersion)) {
				rlt = true;
			}
		}
		return rlt;
	}

	private boolean sampleApk(ApkContent one, ApkContent two) {
		boolean rlt = false;
		if (null != one) {
			if ((null != one.mApkId) && (null != one.mApkVersion)) {
				if (one.mApkId.equals(two.mApkId)
						&& one.mApkVersion.equals(two.mApkVersion)) {
					rlt = true;
				}
			}
		}
		return rlt;
	}

	// private boolean sampleApk2(ApkProfileContent one, ApkProfileContent two)
	// {
	// boolean rlt = false;
	// if (null != one) {
	// if ((null != one.mApkId) && (null != one.mApkVersion)) {
	// if (one.mApkId.equals(two.mApkId)) {
	// //&& one.mApkVersion.equals(two.mApkVersion)) {
	// if (SVERSION.equals(one.mApkVersion)) {
	// rlt = true;
	// } else if (SVERSION.equals(two.mApkVersion)) {
	// rlt = true;
	// } else if (one.mApkVersion.equals(two.mApkVersion)) {
	// rlt = true;
	// }
	// }
	// }
	// }
	// return rlt;
	// }

	static class GetResult {
		public GetResult(long id) {
			dbid = id;
		}

		public void setUpdate() {
			rltType = 1;
		}

		public void setDelete() {
			rltType = 2;
		}

		public void setNone() {
			rltType = 0;
		}

		public int getType() {
			return rltType;
		}

		public long getDBID() {
			return dbid;
		}

		private int rltType = 0;
		private long dbid = -1;
	}

	AppStat appStat = new AppStat();

	// Context mContext = null;

	@SuppressWarnings("unused")
	static class AppStat {
		// nowAPK = mPushAPK + mOldAPK - mRepeatAPK - mCancelAPK;
		private int mPushAPK = 0;
		private int mOldAPK = 0;
		private int mRepeatAPK = 0;
		private int mCancelAPK = 0;

		private int mBlackAPK = 0;
	}

	// private String UpdatePushAppList(Context context, UpdateResult result) {
	// Log.i(TAG, "APP_UPDATE");
	// int count = 0;
	// String StateMsg = null;
	//
	// ArrayList<ApkProfileContent> oldapklist = ApkProfileContent
	// .queryAllApkContents(context);
	//
	// if (null != oldapklist) {
	// for(ApkProfileContent oldapk: oldapklist) {
	// for (ApkProfileContent newapk: result.mAppList) {
	// if (true == sampleApk(oldapk, newapk)) {
	//
	// if (oldapk.mApkDisabled != 0) {
	// if (oldapk.mApkPublished == 0) {
	// oldapk.delete(context);
	// newapk.mApkDisabled = 1;
	// } else {
	// newapk.mIsDeal = true;
	// }
	// } else {
	// newapk.mIsDeal = true;
	// // if (oldapk.mApkPublished == 0) {
	// // oldapk.mApkPublished =1;
	// // oldapk.update(context);
	// // }
	// }
	// oldapk.mIsDeal = true;
	// break;
	// }
	// }
	//
	// if (oldapk.mIsDeal == false) {
	//
	// int flag = -1;
	// if (null != oldapk.mApkFlag) {
	// flag = Integer.parseInt(oldapk.mApkFlag);
	// }
	//
	// if (oldapk.mApkDisabled != 0 ||
	// Appdb.INSTALLED == flag ) {
	// oldapk.mApkPublished = 0;
	// oldapk.update(context);
	// } else {
	// oldapk.delete(context);
	// }
	// // count++;
	// }
	// }
	// }
	//
	// for (ApkProfileContent newapk : result.mAppList) {
	// if (newapk.mIsDeal == false) {
	//
	// newapk.save(context);
	// count++;
	// }
	// }
	//
	// result.mAppList = null;
	// CommUtils.setAppListVersion(context, result.mAppListVersion);
	// if (count > 0) {
	// StateMsg = String.valueOf(count);
	// }
	// return StateMsg;
	// }
}
