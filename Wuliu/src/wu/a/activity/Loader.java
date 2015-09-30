package wu.a.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class Loader extends Thread {
	private static final String TAG = "Loader";
	Context context;
	Handler mHandler;
	int what;
	String request;
	Parcel p;

	public static void start(Context context, Handler mHandler, int what,
			String request, Parcel p) {
		// ProgressDialogUtils.showProgressDialog(context, R.string.LOADING);
		Loader l = new Loader(context, mHandler, what, request, p);
		l.start();
	}

	public Loader(Context context, Handler mHandler, int what, String request,
			Parcel p) {
		this.context = context;
		this.mHandler = mHandler;
		this.what = what;
		this.request = request;
		this.p = p;
	}

	public void run() {
		SocialConstants.log("Loader request=" + request);
		String result = null;
		try {
			if (!SocialConstants.OUTLINE) {
				result =null;// URLConnectionUtil.queryStringForGet(request);
				SocialConstants.log("Loader result: " + result);
				if (result != null) {
					p.parcel(result);
				}
			} else {
				Thread.sleep(1000);
				SocialConstants.log("Loader result=" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SocialConstants.log("Loader done");
		Message msg = mHandler.obtainMessage(what, p);
		msg.sendToTarget();
	}
}

class LoaderHandler extends Handler {
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		// ProgressDialogUtils.dismissProgressDialog();
	}
}
