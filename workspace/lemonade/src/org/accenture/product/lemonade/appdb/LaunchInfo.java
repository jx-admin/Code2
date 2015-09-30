package org.accenture.product.lemonade.appdb;

final class LaunchInfo {

	int mLaunchCount = -1;
	long mLastLaunched = 0;

	public LaunchInfo(int launchCount, long lastLaunched) {
		mLaunchCount = launchCount;
	}

	public int getCount() {
		return mLaunchCount;
	}

	public long getLastLaunched() {
		return mLastLaunched;
	}


	public void launched() {
		mLastLaunched = System.currentTimeMillis() / 1000L;
		mLaunchCount++;
	}
}
