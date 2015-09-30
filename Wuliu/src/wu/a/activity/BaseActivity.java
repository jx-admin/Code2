package wu.a.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import baidumapsdk.demo.R;

/**
 * 我的Activity基类
 * 
 * @author junxu.wang
 *
 */
public class BaseActivity extends Activity {
	public static final int login_need = 101;
	public static final int load_done = 102;
	Handler mHandler = new LoaderHandler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			onDispatchMessage(msg);
		}
	};

	/**
	 * <pre>
	 * 主线程Handler消息分发回调方法
	 * @param msg
	 * @return 是否被处理
	 * </pre>
	 */
	boolean onDispatchMessage(android.os.Message msg) {
		boolean result = true;
		switch (msg.what) {
		case load_done:
			ProgressDialogUtils.dismissProgressDialog();
			break;
		case login_need:// 提示用户登录账号
			ProgressDialogUtils.dismissProgressDialog();
			// TODO Login Page
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	@Override
	protected void onStart() {
		super.onStart();
		startLoader();
	}

	/**
	 * <pre>
	 * 启动后台加载初始化数据
	 * </pre>
	 */
	protected void startLoader() {
		ProgressDialogUtils.showProgressDialog(this, R.string.LOADING);
		new doLoaderThreador(this).start();
	}

	/**
	 * <pre>
	 * 后台加载初始化数据回调方法
	 * </pre>
	 */
	protected void onLoader() {
		Message msg1 = mHandler.obtainMessage(load_done, null);
		msg1.sendToTarget();
	}

	/**
	 * 后台加载数据
	 * 
	 * @author junxu.wang
	 *
	 */
	class doLoaderThreador extends Thread {
		Context context;

		public doLoaderThreador(Context context) {
			this.context = context;
		}

		public void run() {
			onLoader();
		}
	}
}
