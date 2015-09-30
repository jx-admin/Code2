package cn.sdcet.tpod.chen.download.Service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import cn.sdcet.tpod.chen.download.DownLoadActivity.DownloadCallBack;
import cn.sdcet.tpod.chen.download.DownloadFile;
import cn.sdcet.tpod.chen.download.DownloadResourceItem;

public class DownloadFileService extends Service {
	private final IBinder binder = new MyBinder();
	ArrayList<DownloadResourceItem> downloadList = new ArrayList<DownloadResourceItem>();
	
	DownloadCallBack downloadCallBack = null;
	boolean downloadCallBackSwich = false;

	// 创建回调函数体
	public interface CallBack {
		void execute();
		void execute(int position, int pro);
	}

	public class MyBinder extends Binder {
		public DownloadFileService getService() {
			return DownloadFileService.this;
		}
	}

	/**
	 * @return 返回下载列表信息
	 */
	public ArrayList<DownloadResourceItem> getDownList() {
		return downloadList;
	}

	public void addAndStart(String strUrl, String strPath, String strTitle) {
		DownloadResourceItem d = new DownloadResourceItem();
		d.setStrTitle(strTitle);
		d.setStrUrl(strUrl);
		d.setStrPath(strPath);
		d.setComplete(false);
		downloadList.add(d);
		downloadStart(downloadList.size() - 1);
	}

	public boolean downloadStart(int position) {
		DownloadResourceItem d = downloadList.get(position);
		// 创建下载线程
		new DownloadFile(d.getStrUrl(), d.getStrPath(), position,
				new CallBack() {
					public void execute() {
					}

					public void execute(int position, int pro) {
						setUIPro(position, pro);
					}

				}).start();
		return false;

	}

	/**
	 * @param position
	 *            下载序列的位置
	 * @param pro
	 *            下载的进度
	 */
	private void setUIPro(int position, int pro) {
		if (downloadCallBackSwich == true) {
			downloadCallBack.execute(position, pro);
		}
		if (pro == 100) {// 下载完成，更新下载列表。
			DownloadResourceItem d = downloadList.get(position);
			d.setComplete(true);
			downloadList.set(position, d);
		}
		if (pro > 100) {
			DownloadResourceItem d = downloadList.get(position);
			Looper.prepare();
			Toast.makeText(this, "资源[" + d.getStrTitle() + "]下载完成", 1000)
					.show();
			Looper.loop();
		}
	}

	public void addDownloadCallBack(DownloadCallBack downloadCallBack) {
		this.downloadCallBack = downloadCallBack;
		downloadCallBackSwich = true;
	}

	public void removeDownloadCallBack() {
		downloadCallBackSwich = false;
		System.out.println("服务检测到主动回调停止");
	}

	public void onCreate() {
		System.out.println("创建服务");
		super.onCreate();
	}

	public void onDestroy() {
		System.out.println("服务被销毁");
		super.onDestroy();
	}

	public void onStart(Intent intent, int startId) {
		System.out.println("启动服务");
		super.onStart(intent, startId);

	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public IBinder onBind(Intent intent) {
		return binder;
	}

}
