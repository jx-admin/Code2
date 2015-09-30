package com.aess.aemm.view.quit;

import java.io.InputStream;
import java.util.ArrayList;

import com.aess.aemm.R;
import com.aess.aemm.appmanager.ApkInstall;
import com.aess.aemm.authenticator.AuthenticatorActivity;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.db.TrafficContent;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.push.PushService;
import com.aess.aemm.setting.Profile;
import com.aess.aemm.setting.Profile.ProfileType;
import com.aess.aemm.view.MainView;
import com.aess.aemm.view.ToastHelp;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.data.MsgType;
import com.aess.aemm.view.menu.MenuDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserQuit {
	public static final String TAG = "UserQuit";
	public static final String DEBUGFILENAME = "quit.xml";

	public UserQuit(Context cxt) {
		mCxt = cxt;
	}

	public AlertDialog CreateDailog() {
		Builder builder = new AlertDialog.Builder(mCxt);

		builder.setTitle(R.string.userquit);
		LayoutInflater layoutInflater = (LayoutInflater) mCxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.userquit, null);
		mBtnSure = (Button) view.findViewById(R.id.btnQuit);
		mBtnSure.setOnClickListener(mSure);

		mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
		mBtnCancel.setOnClickListener(mCancel);

		mHint = (TextView) view.findViewById(R.id.quitHint);
		mPr = (ProgressBar) view.findViewById(R.id.progressBar1);
		builder.setView(view);

		mAlertDialog = builder.show();
		return mAlertDialog;
	}

	private void cleanUser() {
		mBtnCancel.setEnabled(false);
		mBtnSure.setEnabled(false);
		mHint.setVisibility(View.INVISIBLE);
		mPr.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				int x = 0;
				try {
					x = askService();

					if ( 0 == x){
						deleteUserInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				MainView.sendDelUserEnd();
				if ( x > 0) {
					ToastHelp.SendUserQuiteFaile(x);
				}
			}
		}.start();
	}

	private void deleteUserInfo() {
		Log.d(TAG, "deleteUserInfo");
		ClearDownList();

		AuthenticatorActivity.clearConfigFile(mCxt);
		Log.d(TAG, "clearConfigFile");
		
		
		CommUtils.setAppListVersion(mCxt, "0");
		CommUtils.setBlackListVersion(mCxt, "0");
		CommUtils.setProfileVersion(mCxt, "0");
		CommUtils.setServiceLimit(mCxt, 0);
		CommUtils.setTrafficLimit(mCxt, (long)0);
		Log.d(TAG, "clear ALL Version");
		
		PushService.resetPushManagerMsg();
		Log.d(TAG, "resetPushManagerMsg");
		
		ViewUtils.cancelNotification(mCxt);
		Log.d(TAG, "cancelNotification");
		
		com.aess.aemm.authenticator.Authenticator.delAemmUser(mCxt);
		Log.d(TAG, "Del Aemm User");
		
		ProfileType[] ptList = ProfileType.values();
		for (int x = 0; x < ptList.length; x++) {
			Profile profile = Profile.createProfile(ptList[x]);
			if (null != profile) {
				profile.clearProfile(mCxt);
			}
		}
		Log.d(TAG, "Del User Profile");

		ArrayList<ApkContent> acList = ApkContent.queryAllContents(mCxt);
		if (null != acList) {
			for (ApkContent ac : acList) {
				ac.delete(mCxt);
			}
		}
		Log.d(TAG, "Del User APK");
		
		TrafficContent.deleteAllTrafficContent(mCxt);
		
		NewsContent.deleteAllContent(mCxt);
		MainView.sendMsgCheck();
		
		MenuDialog.delMenuItem(mCxt);
		
		
		ViewUtils.update(mCxt, MsgType.APP_UPATE_SILLENCE, null);
	}

	private int askService() {
		Log.d(TAG, "askService");
		AutoAdress ad = AutoAdress.getInstance(mCxt);
		String url = ad.getUpdateURL();
		if (null != url) {
			String logout = DomXmlBuilder.buildInfo(mCxt, false,
					DomXmlBuilder.LOGOUT, null);
			InputStream is = HttpHelp.aemmHttpPost(mCxt, url, logout,
					DEBUGFILENAME);
			return UpdateXmlParser.parseUpdateXmlForError(is);
		}
		return NetUtils.F_LOGIN_ADDRESS_ERROR;
	}

	OnClickListener mSure = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			cleanUser();
		}
	};

	OnClickListener mCancel = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mAlertDialog.cancel();
		}
	};

	public void ClearDownList() {
		ApkInstall ai = new ApkInstall(mCxt);
		int count = 0;
		while (!ai.serviceIsOk()) {
			try {
				Thread.sleep(500);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count > 19) {
				Log.w(TAG, "bindService(ApkInstallService.class) fail");
				return;
			}
		}
		ai.clearDownList();
	};

	private Button mBtnSure;
	private Button mBtnCancel;
	private TextView mHint;
	private ProgressBar mPr;
	private AlertDialog mAlertDialog;
	private Context mCxt;
}
