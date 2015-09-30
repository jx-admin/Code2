package com.baidu.lbsapi.album.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class StorageCheck {
	//private static final String TAG = "StorageCheck";

	public static final int SDCARD_NORMAL = 0;
	public static final int SDCARD_FULL = 1;
	public static final int SDCARD_ERROR = 2;
	public static final int SDCARD_NOTFOUND = 3;

	public static final int MIN_FREE_SIZE = 1024 * 1024 * 15;

	// SD卡可用空间小于此值时,提示用户
	public static final int MIN_CACHE_FREE_SIZE = 1024 * 1024 * 20;

	/**
	 * 检查SD状态
	 * @param sizeInByte 需要检查的所需空间大小
	 * @param bBuffer 是否预留buffer( 15M )
	 * @return SD卡正常时，检查是否有sizeInByte的可用空间，否则返回SD卡状态
	 */
	public static int handleSdcardError(int sizeInByte,boolean bBuffer) {
		int state = getSdcardState();

		if (state == SDCARD_NORMAL) {
			StatFs sfs = getSdcardSize();
			long freeSize = sfs.getBlockSize();
			long freeBlock = sfs.getFreeBlocks();
			freeSize = freeSize * freeBlock;
			if (freeSize < ( bBuffer ? MIN_FREE_SIZE : 0 ) + sizeInByte) {
				//Log.e(TAG, "SDCARD_FULL");
				return SDCARD_FULL;
			}
		}
		return state;
	}


	public static int getSdcardState() {
		//Log.e(TAG, "getSdcardState");
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_BAD_REMOVAL)) {
			//Log.e(TAG, "MEDIA_BAD_REMOVAL");
			return SDCARD_ERROR;
		} else if (status.equals(Environment.MEDIA_CHECKING)) {
			//Log.e(TAG, "MEDIA_CHECKING");
		} else if (status.equals(Environment.MEDIA_MOUNTED)) {
			// OK
			// Windows Media Sync
			// Motorola Phone Portal
			//Log.e(TAG, "MEDIA_MOUNTED");
			return SDCARD_NORMAL;
		} else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			//Log.e(TAG, "MEDIA_MOUNTED_READ_ONLY");
			return SDCARD_ERROR;
		} else if (status.equals(Environment.MEDIA_NOFS)) {
			//Log.e(TAG, "MEDIA_NOFS");
			return SDCARD_ERROR;
		} else if (status.equals(Environment.MEDIA_REMOVED)) {
			// Remove Sdcard
			//Log.e(TAG, "MEDIA_REMOVED");
			return SDCARD_NOTFOUND;
		} else if (status.equals(Environment.MEDIA_SHARED)) {
			// USB Mass Storage
			//Log.e(TAG, "MEDIA_SHARED");
			return SDCARD_NOTFOUND;
		} else if (status.equals(Environment.MEDIA_UNMOUNTABLE)) {
			//Log.e(TAG, "MEDIA_UNMOUNTABLE");
			return SDCARD_ERROR;
		} else if (status.equals(Environment.MEDIA_UNMOUNTED)) {
			//Log.e(TAG, "MEDIA_UNMOUNTED");
			return SDCARD_NOTFOUND;
		}
		return SDCARD_NORMAL;
	}

	public static StatFs getSdcardSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		return stat;
	}


}
