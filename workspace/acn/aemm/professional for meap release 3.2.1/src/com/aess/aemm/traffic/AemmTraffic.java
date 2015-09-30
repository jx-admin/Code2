package com.aess.aemm.traffic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.TrafficContent;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

public class AemmTraffic implements Runnable {

	public static AemmTraffic getInstance(Context cxt) {
		if (null == _traffic) {
			_traffic = new AemmTraffic(cxt);
		}
		return _traffic;
	}

	public static int isWork() {
		synchronized (loc) {
			return work;
		}
	}
	
	private boolean isNewMonth(Date today, long oldValue) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int newMonths = calendar.get(Calendar.MONTH) + 1;
		Date olddate = new Date(oldValue);
		calendar.setTime(olddate);
		int oldMonths = calendar.get(Calendar.MONTH) + 1;
		if (oldMonths != newMonths) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		setWork(1);
		
		int add = CommUtils.getPhoneReset(_cxt);
		CommUtils.setPhoneReset(_cxt, 0);
		
		List<ApplicationInfo> applist = _cxt.getPackageManager()
				.getInstalledApplications(
						PackageManager.GET_UNINSTALLED_PACKAGES);
		
		List<TrafficContent> tclist = TrafficContent.queryAllTraffics(_cxt);
		
		Date today = new Date();

		boolean newmonth = false;
		
		
		TrafficContent monbletc = getTCByUid(tclist, TrafficContent.Monble_UID);
		if (null == monbletc) {
			monbletc = new TrafficContent();
			monbletc.mUid = TrafficContent.Monble_UID;
			monbletc.mName = TrafficContent.Monble_NAME;
			monbletc.mOldFlow = 0;
			monbletc.mNewFlow = 0;
			monbletc.mTime = -1;
		} else {
			newmonth = isNewMonth(today, monbletc.mTime);
		}
		
		TrafficContent othertc = getTCByUid(tclist, TrafficContent.Other_UID);
		if (null == othertc) {
			othertc = new TrafficContent();
			othertc.mUid = TrafficContent.Other_UID;
			othertc.mName = TrafficContent.Other_NAME;
			othertc.mOldFlow = 0;
			othertc.mNewFlow = 0;
			othertc.mTime = -1;
		}
		
		if (true == newmonth) {
			monbletc.mOldFlow = 0;
			monbletc.mNewFlow = 0;
			monbletc.mTime = today.getTime();
		}
		
		if (true == newmonth) {
			othertc.mOldFlow = 0;
			othertc.mNewFlow = 0;
			othertc.mTime = today.getTime();
		}

		long other = 0;
		long totle = 0;
		if (null != applist) {
			for (ApplicationInfo ai : applist) {
				if (ApplicationInfo.FLAG_SYSTEM == (ai.flags & ApplicationInfo.FLAG_SYSTEM)) {
					other = other + TrafficStats.getUidRxBytes(ai.uid);
					continue;
				}
				
				TrafficContent tc = getTCByUid(tclist, ai.uid);
				if (null == tc) {
					tc = new TrafficContent();
					tc.mName = ai.packageName;
					tc.mUid = ai.uid;
					tc.mNewFlow = 0;
					tc.mOldFlow = 0;
					tc.mTime = -1;
				}
				
				if (true == newmonth) {
					tc.mNewFlow = 0;
					tc.mOldFlow = 0;
					tc.mTime = today.getTime();
				}
				
				long x = TrafficStats.getUidRxBytes(ai.uid);
				if (x > 0) {
					if (add > 1) {
						tc.mOldFlow = tc.mOldFlow + tc.mNewFlow;
					}
					tc.mNewFlow = x;
					totle = totle + x;
				}

				if (-1 == tc.mTime) {
					tc.mTime = today.getTime();
					tc.add(_cxt);
				} else {
					tc.update(_cxt);
				}
			}
		}
		
		totle = totle + other;
		if (add > 0) {
			monbletc.mOldFlow = monbletc.mOldFlow + monbletc.mNewFlow;
		}
	    monbletc.mNewFlow = totle;

		
		if (monbletc.mTime == -1) {
			monbletc.mTime = today.getTime();
			monbletc.add(_cxt);
		} else {
			monbletc.update(_cxt);
		}
		
		if (add > 0) {
			othertc.mOldFlow = othertc.mOldFlow + othertc.mNewFlow;
		}
		othertc.mNewFlow = other;
		
		if (othertc.mTime == -1) {
			othertc.mTime = today.getTime();
			othertc.add(_cxt);
		} else {
			othertc.update(_cxt);
		}
		
		setWork(0);
	}
	
	private TrafficContent getTCByUid(List<TrafficContent> tclist , long uid) {
		TrafficContent tcalt = null;
		if (null != tclist) {
			for (TrafficContent tc : tclist) {
				if (uid == tc.mUid) {
					tcalt = tc;
					break;
				}
			}
		}
		return tcalt;
	}

	private AemmTraffic(Context cxt) {
		_cxt = cxt;
	}

	private static void setWork(int x) {
		synchronized (loc) {
			work = x;
		}
	}

	private Context _cxt;
	private static AemmTraffic _traffic;
	private static Object loc = new Object();
	private static int work = 0;
}