package cn.sdcet.tpod.chen.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.sdcet.tpod.chen.download.Service.DownloadFileService;

public class DownLoadActivity extends Activity implements
		OnMenuItemClickListener, OnClickListener {
	public void onBackPressed() {
		super.onBackPressed();
		// 淡入淡出效果
//		overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	}

	ListView listView = null;
	ArrayList itemList = null;
	// 用于更行ListView中进度条的大小
	private MyAdapet myAdapet;
	private DownloadHandler downloadHandler = new DownloadHandler();
	private DownloadFileService downloadFileService;

	protected void onStop() {
		super.onStop();
		System.out.println("停止");
		downloadFileService.removeDownloadCallBack();
	}

	/* 回调函数 */
	public interface DownloadCallBack {
		void execute();

		void execute(int position, int pro);

	}

	private ServiceConnection connection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			downloadFileService = ((DownloadFileService.MyBinder) service)
					.getService();
			if (downloadFileService != null) {
				System.out.println("下载页创建服务对象成功了。");
				Message msg = downloadHandler.obtainMessage();
				msg.what = 4;
				msg.sendToTarget();
			} else {
				System.out.println("创建服务对象失败了。");
			}

		}

		public void onServiceDisconnected(ComponentName name) {
			downloadFileService = null;
			Log.v("BindMusicButton",
					"in onServiceDisconnected(ComponentName name) ");
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloadactivity);
		connection();

	}

	private void connection() {
		Intent i = new Intent("cn.sdcet.sunshine.DownloadService");
		// connection 创建服务,并发送msg消息
		boolean d = this.getApplicationContext().bindService(i, connection,
				Context.BIND_AUTO_CREATE);
		System.out.println("正在创建服务对象！！" + d);

	}

	// 初始化activity
	private void initActivity() {
		// 获取列表的ID
		listView = (ListView) findViewById(R.id.downloadContent);
		// list集合
		itemList = PrepareData();
		myAdapet = new MyAdapet(DownLoadActivity.this, itemList);
		listView.setAdapter(myAdapet);

		// 创建长按事件
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {

				menu.setHeaderTitle("资源操作");
				MenuItem openfile = menu.add(0, 0, 0,
						"打开文件");
				MenuItem openfolder = menu.add(0, 1, 0,
						"打开文件夹");
				MenuItem delfile = menu.add(0, 2, 0,
						"删除文件");

				openfile.setOnMenuItemClickListener(DownLoadActivity.this);
				openfolder.setOnMenuItemClickListener(DownLoadActivity.this);
				delfile.setOnMenuItemClickListener(DownLoadActivity.this);

				// 震动
				Vibrator mVibrator = null;
				mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				mVibrator.vibrate(80);
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 震动
				Vibrator mVibrator = null;
				mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				mVibrator.vibrate(40);

				Toast.makeText(DownLoadActivity.this, "长按以操作选中项", 200).show();
			}

		});
		updateUI();
	}

	private ArrayList PrepareData() {
		ArrayList<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		Map<String, Object> tItem;
		if (downloadFileService == null) {
			System.out.println("服务为空");
			return listItem;
		}

		// downloadFileService.getDownList()返回一个下载文件的列表信息,存储到downloadList
		ArrayList<DownloadResourceItem> downloadList = downloadFileService
				.getDownList();

		if (downloadList == null) {
			System.out.println("返回空");
			return listItem;
		}
		// 获取downloadList中的信息
		for (int i = 0; i < downloadList.size(); i++) {
			DownloadResourceItem d = downloadList.get(i);
			tItem = new HashMap<String, Object>();
			tItem.put("title", d.getStrTitle());
			tItem.put("current", 0);
			tItem.put("img", R.drawable.logo_download);
			tItem.put("url", d.getStrUrl());
			tItem.put("path", d.getStrPath());
			tItem.put("complete", d.isComplete());
			listItem.add(tItem);

		}

		return listItem;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(DownLoadActivity.this, "单击事件", 4000).show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub

		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int selectPosition = menuInfo.position;
		HashMap<String, Object> dataTemp = (HashMap<String, Object>) itemList
				.get(selectPosition);
		String strPath = dataTemp.get("path").toString();
		System.out.println("打开文件，路径为：" + strPath);
		if (item.getItemId() == 0) {
			openFile(strPath);

		} else if (item.getItemId() == 1) {
			Toast.makeText(DownLoadActivity.this, "1菜单被选择:" + item.getTitle(),
					2000).show();

		} else if (item.getItemId() == 2) {
			delFile(strPath);
		} else {

		}

		// return false;
		return false;
	}

	// List的显示
	class MyAdapet extends BaseAdapter {

		List<HashMap<String, Object>> list;
		LayoutInflater infl = null;

		public MyAdapet(Context context, List<HashMap<String, Object>> list) {
			// this.infl =
			// (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.infl = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.list = list;

		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = infl.inflate(R.layout.download_items, null);
			HashMap<String, Object> detail = list.get(position);
			// 设置滚动条
			ProgressBar progressBar = (ProgressBar) convertView
					.findViewById(R.id.downloadBar);
			int progress = (Integer) detail.get("current");
			progressBar.setProgress(progress);
			// 设置标题
			TextView textView = (TextView) convertView
					.findViewById(R.id.downloadTitle);
			String title = (String) detail.get("title");
			textView.setText(title);

			// 设置图片logo
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.downloadimg);
			int imageid = (Integer) detail.get("img");
			imageView.setBackgroundResource(imageid);
			return convertView;
		}

		// 改变进度，postion就是要改的那个进度
		public void charged(int position, HashMap<String, Object> detail) {
			this.list.set(position, detail);
			notifyDataSetChanged();
		}

	}

	// 消息Handler用来更新UI
	public class DownloadHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 设置进度
				int id1 = msg.arg1;
				int current = msg.arg2;
				updateProgress(id1, current);
				break;
			case 2: // 成功
				int id2 = msg.arg1;
				updateImg(id2, 2);
				break;
			case 3: // 失败
				int id3 = msg.arg1;
				updateImg(id3, 1);
				break;
			case 4: // 服务创建完成
				System.out.println("ok");
				initActivity();
				break;
			default:

			}

			super.handleMessage(msg);
		}

		private void updateProgress(int id, int currentPos) {
			HashMap<String, Object> dataTemp = (HashMap<String, Object>) itemList
					.get(id);
			dataTemp.put("current", currentPos);
			myAdapet.charged(id, dataTemp);
		}

		private void updateImg(int id, int result) {
			HashMap<String, Object> dataTemp = (HashMap<String, Object>) itemList
					.get(id);
			if (result == 2) {
				dataTemp.put("img", R.drawable.download_success);
			} else if (result == 1) {
				dataTemp.put("img", R.drawable.download_failure);
			}
			myAdapet.charged(id, dataTemp);

		}
	}

	public void updateUI() {

		// 加入回调函数
		downloadFileService.addDownloadCallBack(new DownloadCallBack() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}

			@Override
			public void execute(int position, int pro) {
				// TODO Auto-generated method stub

				if (pro < 0) {// 下载失败
					Message msg = downloadHandler.obtainMessage();
					msg.what = 3;
					msg.arg1 = position;
					msg.arg2 = pro;
					msg.sendToTarget();
				} else if (pro <= 100) {// 正在下载
					System.out.println("在download中的回调函数执行" + pro);
					Message msg = downloadHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = position;
					msg.arg2 = pro;
					msg.sendToTarget();

				} else {// 下载成功
					Message msg = downloadHandler.obtainMessage();
					msg.what = 2;
					msg.arg1 = position;
					msg.sendToTarget();
				}
			}

		});

		for (int i = 0; i < itemList.size(); i++) {
			HashMap<String, Object> h = (HashMap<String, Object>) (itemList
					.get(i));
			String result = h.get("complete").toString();
			if (result.equals("true")) {
				Message msg = downloadHandler.obtainMessage();
				msg.what = 1;
				msg.arg1 = i;
				msg.arg2 = 100;
				msg.sendToTarget();

				Message msg1 = downloadHandler.obtainMessage();
				msg1.what = 2;
				msg1.arg1 = i;
				msg1.arg2 = 100;
				msg1.sendToTarget();
			}
		}

	}

	private void delFile(String strPath) {
		File file = new File(strPath);
		if (file.exists()) {
			if (file.delete()) {
				Toast.makeText(DownLoadActivity.this, "删除成功", 1000).show();

			} else {
				Toast.makeText(DownLoadActivity.this, "删除失败，可能文件被占用", 1000)
						.show();

			}
		} else {
			Toast.makeText(DownLoadActivity.this, "删除项不存在", 1000).show();
		}
	}

	private void openFile(String strPath) {

		File f = new File(strPath);
		if (f.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);

			String strType = getMIMEType(f);
			/* 设置intent的file与MimeType */
			intent.setDataAndType(Uri.fromFile(f), strType);
			startActivity(intent);
		} else {
			System.out.println("文件不存在");
		}

	}

	/* 判断文件MimeType的method */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/* 如果无法直接打开，就跳出软件列表给用户选择 */
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}
}
