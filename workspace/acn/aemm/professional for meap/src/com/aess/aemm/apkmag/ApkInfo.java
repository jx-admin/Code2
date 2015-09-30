package com.aess.aemm.apkmag;

import java.util.ArrayList;
import java.util.List;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.TrafficContent;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.view.data.Appdb;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApkInfo {
	
	public ApkInfo(Context cxt) {
		tclist = TrafficContent.queryAllTraffics(cxt);
	}
	
	public ArrayList<ApkData> getApkDataList(Context cxt, int type) {
		pm = cxt.getPackageManager();
		if (null == pm) {
			return null;
		}

		List<ApplicationInfo> applist = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

		ArrayList<ApkData> apklist = new ArrayList<ApkData>();

		for (int x = 0; x < applist.size(); x++) {
			ApplicationInfo appInfo = applist.get(x);

			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				continue;
			}
			
			ApkData data = new ApkData();

			ApkContent apkContent = ApkContent.queryContentBy_PKG_FLAG(
					cxt, appInfo.packageName, Appdb.INSTALLED);

			if (DomXmlBuilder.APKPSK == type) {
				if (null == apkContent) {
					data.type = DomXmlBuilder.APKPSK;
				}
			} else if (DomXmlBuilder.APKENT == type) {
				if (null != apkContent) {
					data.type = DomXmlBuilder.APKENT;
				}
			} else if (DomXmlBuilder.APK == type){
				data.type = DomXmlBuilder.APK;
			}

			int tt = data.setData(cxt, appInfo);
			if (tt > 0) {
				apklist.add(data);
			}
		}
		return apklist;
	}
	
	public long getTrafficTotle() {
		if (null != tclist) {
			for(TrafficContent tc : tclist) {
				if (TrafficContent.Monble_UID == tc.mUid) {
					return (tc.mOldFlow+ tc.mNewFlow)/1024;
				}
			}
		}

		return 0;
	}
	
	public long getTrafficOther() {
		if (null != tclist) {
			for(TrafficContent tc : tclist) {
				if (TrafficContent.Other_UID == tc.mUid) {
					return (tc.mOldFlow + tc.mNewFlow)/1024;
				}
			}
		}

		return 0;
	}
	
	public long getTrafficTotle(int uid) {
		if (null != tclist) {
			for(TrafficContent tc : tclist) {
				if (uid == tc.mUid) {
					return (tc.mOldFlow + tc.mNewFlow)/1024;
				}
			}
		}

		return 0;
	}
	
	
	public boolean isLimitOver(Context cxt) {
		long limit = CommUtils.getTrafficLimit(cxt);
		if (limit <= 0) {
			return false;
		}
		long totle = getTrafficTotle();
		
		return totle > limit;
	}
	

	public final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public class ApkData {

		public int setData(Context cxt, ApplicationInfo info) {
			if (null == info) {
				return -1;
			}

			if (null == pm) {
				return -1;
			}

			String name = info.loadLabel(pm).toString();
			if (null == name) {
				name = info.packageName;
			}
			disName = name;

			id = info.packageName;

			PackageInfo pkg = null;
			try {
				pkg = pm.getPackageInfo(info.packageName,
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (null != pkg) {
				version = pkg.versionName;
			}
			
			flow = getTrafficTotle(info.uid);

			ApkContent apkContent = ApkContent.queryContentBy_PKG_FLAG(
					cxt, info.packageName, Appdb.INSTALLED);

			if (null == apkContent) {
				enterprise = "0";

				disabled = "0";

				insTime = CommUtils.getTimeString(TIMEFORMAT);

				lastTime = "";

				exitTime = "";
			} else {

				disName = apkContent.mApkName;

				id = apkContent.mApkId;

				version = apkContent.mApkVersion;

				enterprise = "1";

				disabled = apkContent.mApkDisabled == 0 ? "0" : "1";

				insTime = apkContent.mApkInstalledTime == null ? ""
						: apkContent.mApkInstalledTime;

				lastTime = apkContent.mApkLastStartTime == null ? ""
						: apkContent.mApkLastStartTime;

				exitTime = apkContent.mApkLastExitTime == null ? ""
						: apkContent.mApkLastExitTime;
			}

			return 1;
		}

		public String toString() {
			StringBuilder str = new StringBuilder();

			str.append("<app ");

			str.append("name=\"");
			str.append(disName);
			str.append("\" ");

			str.append("app-id=\"");
			str.append(id);
			str.append("\" ");

			str.append("version=\"");
			str.append(version);
			str.append("\" ");

			str.append("enterprise=\"");
			str.append(enterprise);
			str.append("\" ");

			str.append("disabled=\"");
			str.append(disabled);
			str.append("\" ");

			str.append("install-time=\"");
			str.append(insTime);
			str.append("\" ");

			str.append("last-start-time=\"");
			str.append(lastTime);
			str.append("\" ");

			str.append("last-exit-time=\"");
			str.append(exitTime);
			str.append("\" ");
			
			str.append("flow=\"");
			str.append(String.valueOf(flow));
			str.append("\" ");

			str.append("/>\r\n");
			return str.toString();
		}

		public String disName = null;
		public String id = null;
		public long flow = 0;
		public int type = -1;
		public String version = null;
		public String enterprise = null;
		public String disabled = null;
		public String insTime = null;
		public String lastTime = null;
		public String exitTime = null;
	}

	private PackageManager pm = null;
	private List<TrafficContent> tclist = null;
}
