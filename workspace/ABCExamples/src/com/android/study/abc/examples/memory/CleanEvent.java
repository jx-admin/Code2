package com.android.study.abc.examples.memory;

import java.util.HashMap;
import java.util.Map;

import a.w.utils.Logger;
import android.content.Context;
import android.widget.Toast;

public class CleanEvent {

	private static final String TAG = "CleanEvent";
	private volatile static CleanEvent sCleanEvent = null;
	private Map<String, CleanEventListener> mCleanEventListeners = new HashMap<String, CleanEventListener>();

	private CleanEvent() {

	}

	public static CleanEvent getInstance() {
		if (sCleanEvent == null) {
			synchronized (CleanEvent.class) {
				if (sCleanEvent == null) {
					sCleanEvent = new CleanEvent();
				}
			}
		}
		return sCleanEvent;
	}

	public void addCleanEventListener(String key,
			CleanEventListener cleanEventListener) {
		mCleanEventListeners.put(key, cleanEventListener);
	}

	public void fireOnStartClean(String comName) {
		Logger.i(TAG, "fireOnStartClean");
		// if (!BubbleTextViewFactory.KEY_OF_CLEAN.equals(comName)) {
		// return;
		// }
		for (CleanEventListener cleanEventListener : mCleanEventListeners
				.values()) {
			cleanEventListener.onStartClean();
		}
	}

	public void fireOnProgressChange(int current, int max, String percentage,
			String processName, String comName) {
		Logger.i(TAG, "fireOnProgressChange");
		// if (!BubbleTextViewFactory.KEY_OF_CLEAN.equals(comName)) {
		// return;
		// }
		for (CleanEventListener cleanEventListener : mCleanEventListeners
				.values()) {
			cleanEventListener
					.onCleaning(current, max, percentage, processName);
		}
	}

	public void fireOnCleanDone(String comName, Context context,
			String cleanMb, long afterMb, long beforeMb) {
		Logger.i(TAG, "fireOnCleanDone from " + comName);
		for (CleanEventListener cleanEventListener : mCleanEventListeners
				.values()) {
			cleanEventListener.onCleanDone(cleanMb, afterMb, beforeMb);
		}

		String cleanDoneMb;
		if (cleanMb.contains("0.0") || cleanMb.contains("-")) {
			cleanDoneMb = ("不需清楚内存");
		} else {
			cleanDoneMb = ("清除了") + cleanMb.trim() + ("大小内存");
		}
		Toast.makeText(context, cleanDoneMb, Toast.LENGTH_SHORT).show();
	}

	public interface CleanEventListener {
		public void onStartClean();

		public void onCleaning(int current, int max, String percentage,
				String processName);

		public void onCleanDone(String cleanMb, long afterMb, long beforeMb);

		public void onPercentageChange(String percentage);
	}
}
