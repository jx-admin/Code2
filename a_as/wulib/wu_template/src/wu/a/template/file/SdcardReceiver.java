package wu.a.template.file;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class SdcardReceiver {
	/**
    <!-- 在SDCard中创建与删除文件权限 -->  
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />  
    <!-- 往SDCard写入数据权限 -->  
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />  
     */
	private static final BroadcastReceiver broadcastRec = new BroadcastReceiver() {
		File imagepath;

		public void registerReceiver(Context mContext){
			// 在IntentFilter中选择你要监听的行为  
	        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);// sd卡被插入，且已经挂载  
	        intentFilter.setPriority(1000);// 设置最高优先级  
	        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);// sd卡存在，但还没有挂载  
	        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);// sd卡被移除  
	        intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);// sd卡作为 USB大容量存储被共享，挂载被解除  
	        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);// sd卡已经从sd卡插槽拔出，但是挂载点还没解除  
	        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);// 开始扫描  
	        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);// 扫描完成  
	        intentFilter.addDataScheme("file");  
	        mContext.registerReceiver(this, intentFilter);// 注册监听函数  
		}
		public void unRegisterReceiver(Context mContext){
			mContext.unregisterReceiver(this);
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_MOUNTED.equals(action)){// SD
				// 卡已经成功挂载
				imagepath = android.os.Environment
						.getExternalStorageDirectory();// 你的SD卡路径
				Toast.makeText(context, "我的卡已经成功挂载"+"-imagepath-" + imagepath, 0).show();
			} else if (Intent.ACTION_MEDIA_REMOVED.equals(action)// 各种未挂载状态
					|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
					|| Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)) {
				imagepath = android.os.Environment.getDataDirectory();// 你的本地路径
				Toast.makeText(context, "我的各种未挂载状态", 0).show();
			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {// 开始扫描
				Toast.makeText(context, "开始扫描...", 0).show();
			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {// 扫描完成
				Toast.makeText(context, "扫描完成...", 0).show();
			} else if (action.equals(Intent.ACTION_MEDIA_SHARED)) {// 扩展介质的挂载被解除
																	// (unmount)。因为它已经作为
																	// USB
																	// 大容量存储被共享
				Toast.makeText(context, " USB 大容量存储被共享...", 0).show();
			} else {
				Toast.makeText(context, "其他状态...", 0).show();
			}
		}
	}; 
}
