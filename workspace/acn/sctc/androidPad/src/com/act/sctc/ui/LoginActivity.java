package com.act.sctc.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.R;
import com.act.sctc.User;
import com.act.sctc.db.DBHelper;
import com.act.sctc.util.DbSync;
import com.act.sctc.util.HttpRequest;
import com.act.sctc.util.Logger;
import com.act.sctc.util.Utils;
import com.act.sctc.util.HttpRequest.HttpRequestListener;
import com.custom.view.CustomDialog;

public class LoginActivity extends BaseActivity implements OnClickListener, HttpRequestListener, OnLongClickListener {
	private EditText name_et, pass_word_et;
	private Button login_btn;
	private String username, password;
	private CustomDialog mDownloadDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);

		name_et = (EditText) findViewById(R.id.name_et);
		pass_word_et = (EditText) findViewById(R.id.pass_word_et);
		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);

		View logo = findViewById(R.id.imgLogo);
		logo.setLongClickable(true);
		logo.setOnLongClickListener(this);

		mDownloadDialog = new CustomDialog(this);
		mDownloadDialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		if (DbSync.USE_DB_SYNC) {
			username = name_et.getText().toString();
			if (username.length() == 0) {
				name_et.requestFocus();
				showDialog("工号不能为空！", true, false);
				return;
			}
			password = pass_word_et.getText().toString();
			if (pass_word_et.length() == 0) {
				pass_word_et.requestFocus();
				showDialog("密码不能为空！", true, false);
				return;
			}

			showDialog("正在登录。。。", false, false);

			password = Utils.md5(password);
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("eid", username));
			params.add(new BasicNameValuePair("password", password));
			new HttpRequest(this).post(0, DbSync.getServerUrl(this) + "/ws/user/login", params);
		} else {
			launchHomePage();
		}
	}

	@Override
	public void onHttpRequestFailed(int tag) {
		if (Logger.DEBUG) {
			Logger.warn("LoginActivity.onHttpRequestFailed");
		}
		// 本地登录
		try {
			Cursor cursor = DBHelper.getInstance(this).rawQuery(
					"select u._id,u.username,u.password,u.eid,u.token from user u where u.eid=? and u.password=?",
					new String[] { username, password });
			if (cursor != null && cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					User user = new User();
					user.userId = cursor.getInt(0);
					user.username = cursor.getString(1);
					user.password = cursor.getString(2);
					user.eid = cursor.getString(3);
					user.token = cursor.getString(4);
					App.updateCurrentUser(this, user, false);
					showDialog("进入离线模式！", true, true);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		showDialog("工号或密码错误！", true, false);
	}

	@Override
	public void onHttpRequestOK(int tag, byte[] data) {
		if (Logger.DEBUG) {
			Logger.debug("LoginActivity.onHttpRequestOK");
		}
		try {
			String response = new String(data, "utf-8");
			JSONObject jsonLogin = new JSONObject(response);
			int errCode = jsonLogin.getInt("errCode");
			if (errCode == 0) {
				User user = new User();
				user.userId = jsonLogin.getInt("id");
				user.username = jsonLogin.getString("nm");
				user.eid = jsonLogin.getString("eid");
				user.password = jsonLogin.getString("pwd");
				user.token = jsonLogin.getString("token");
				App.updateCurrentUser(this, user, true);
				launchHomePage();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		showDialog("工号或密码错误！", true, false);
	}

	private void showDialog(String msg, boolean done, final boolean launchHomePage) {
		if (!mDownloadDialog.isShowing()) {
			mDownloadDialog.show();
			mDownloadDialog.resetView();
		}
		mDownloadDialog.setMessage(msg);
		if (done) {
			mDownloadDialog.done();
			mDownloadDialog.addPositiveButton(R.string.back, new View.OnClickListener() {
				public void onClick(View v) {
					dismissDialog();
					if (launchHomePage) {
						launchHomePage();
					}
				}
			});
		}
	}

	private void dismissDialog() {
		if (mDownloadDialog != null) {
			mDownloadDialog.dismiss();
		}
	}

	private void launchHomePage() {
		dismissDialog();
		Intent intent = new Intent(this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onLongClick(View view) {
		if (view.getId() == R.id.imgLogo) {
			final EditText edit = new EditText(this);
			edit.setText(DbSync.getServerUrl(this));
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						DbSync.setServerUrl(LoginActivity.this, edit.getText().toString());
					}
				}
			};
			Builder builder = new Builder(this).setTitle("设置服务器URL").setView(edit).setCancelable(true)
					.setNegativeButton("取消", null).setPositiveButton("确定", listener);
			builder.show();
		}
		return true;
	}
}
