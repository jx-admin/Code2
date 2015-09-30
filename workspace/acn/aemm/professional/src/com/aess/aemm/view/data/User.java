package com.aess.aemm.view.data;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.update.UpdateExecutor;
import com.aess.aemm.view.InfoMainView;
import com.aess.aemm.view.ViewUtils;

public class User {
	public static final String LOGCAT = "user";
	public static final String XMLBODY = "<info operate=\"%d\" name=\"%s\" mail=\"%s\" im=\"%s\" addr=\"%s\" oldpwd=\"%s\" newpwd=\"%s\"/>\r\n";
	public final static Map<String,String> mErrorInfo=new HashMap<String,String>();
	public static final String SUCCESSED = "修改成功";
	static{
		mErrorInfo.put("500","服务器错误");
		mErrorInfo.put("401","客户端用户名不符合策略");
		mErrorInfo.put("402","新密码不符合密码策略");
		mErrorInfo.put("403","原密码不正确");
	}
	public final class Setting {
		public static final String ELEMENT_USER = "user",
				ELEMENT_INFO = "info", ELEMENT_ERORR = "error",
				ELEMENT_ERORR_NUMBER = "number", OPERATE = "operate",
				NAME = "name", MAIL = "mail", IM = "im", ADDR = "addr",
				OLDPWD = "oldpwd", NEWPWD = "newpwd", GET = "0", SET = "1";
		public static final String ACCOUNT = "thirdinfo";
	}

	private String operator, name, mail, im, addr, oldpwd, newpwd;

	public User() {
	}

	public User(String pwd,String name, String mail, String im, String addr) {
		set(pwd,name, mail, im, addr);
	}

	public User(String oldPwd, String newPwd) {
		set(oldPwd, newPwd);
	}

	public void set(String pwd,String name, String mail, String im, String addr) {
		this.oldpwd=pwd;
		this.name = name;
		this.mail = mail;
		this.im = im;
		this.addr = addr;
	}

	public void set(String oldPwd, String newPwd) {
		oldpwd = oldPwd;
		newpwd = newPwd;
	}

	/**
	 * <!—用户更新信息 --> <info operate="操作（0：获取，1：更新）" name="用户名" mail="邮箱"
	 * im="即时通讯" addr="通讯地址" oldpwd="原密码" newpwd="新密码" />
	 * 
	 * @param cxt
	 * @param xmlBuf
	 * @return
	 */
	public static int buildUserInfo(Context cxt, StringBuilder xmlbody) {
		Log.d(LOGCAT, "build xml ...");
		SharedPreferences settings = cxt.getSharedPreferences(
				InfoMainView.USERINFODATA_NEW, 0);
		StringBuffer xmlBuf = new StringBuffer();
		SharedPreferences.Editor editor = settings.edit();
		String operate = settings.getString(User.Setting.OPERATE, null);
		if (operate == null) {
			Log.d(LOGCAT, "operate=-1 needn't updata ...");
			return -1;
		}
		xmlBuf.append("<user>\r\n<info ");
		xmlBuf.append(String.format(" operate=\"%s\"", operate));
		String mUserName = settings.getString(User.Setting.NAME, null);
		if (!TextUtils.isEmpty(mUserName)) {
			xmlBuf.append(String.format(" name=\"%s\"", mUserName));
		}
		String mUserMail = settings.getString(User.Setting.MAIL, null);
		if (!TextUtils.isEmpty(mUserMail)) {
			xmlBuf.append(String.format(" mail=\"%s\"", mUserMail));
		}
		String mUserIM = settings.getString(User.Setting.IM, null);
		if (!TextUtils.isEmpty(mUserIM)) {
			xmlBuf.append(String.format(" im=\"%s\"", mUserIM));
		}
		String mUserAddress = settings.getString(User.Setting.ADDR, null);
		if (!TextUtils.isEmpty(mUserAddress)) {
			xmlBuf.append(String.format(" addr=\"%s\"", mUserAddress));
		}
		String oldpwd = settings.getString(User.Setting.OLDPWD, null);
		if (!TextUtils.isEmpty(oldpwd)) {
			xmlBuf.append(String.format(" oldpwd=\"%s\"", oldpwd));
		}
		String newpwd = settings.getString(User.Setting.NEWPWD, null);
		if (!TextUtils.isEmpty(newpwd)) {
			xmlBuf.append(String.format(" newpwd=\"%s\"", newpwd));
		}
		xmlBuf.append(" />\r\n</user>\r\n");
		xmlbody.append(xmlBuf);
		// editor.putInt(User.Setting.OPERATE, User.Setting.NULL);
		editor.clear();
		if (operate.equals(User.Setting.SET)) {
			editor.remove(User.Setting.OPERATE);
		} else if (operate.equals(User.Setting.GET)) {
			editor.putString(User.Setting.OPERATE, User.Setting.GET);
		}
		editor.commit();
		Log.d(LOGCAT, "send user xml:" + xmlBuf);

		return 1;
	}

	/**
	 * <!—用户更新信息 --> <info operate="操作（0：获取，1：更新）" name="用户名" mail="邮箱"
	 * im="即时通讯" addr="通讯地址" oldpwd="原密码" newpwd="新密码" />
	 * 
	 * @param cxt
	 * @param xmlBuf
	 * @return
	 */
	public static int buildUserInfo(Context cxt, Document document, Element root) {
		Log.d(LOGCAT, "build xml ...");
		SharedPreferences settings = cxt.getSharedPreferences(
				InfoMainView.USERINFODATA_NEW, 0);
		SharedPreferences.Editor editor = settings.edit();
		String operate = settings.getString(User.Setting.OPERATE, null);
		if (operate == null) {
			Log.d(LOGCAT, "operate=null needn't updata ...");
			return -1;
		}
		Element user = document.createElement(User.Setting.ELEMENT_USER);
		Element info = document.createElement(User.Setting.ELEMENT_INFO);
		user.appendChild(info);
		info.setAttribute(User.Setting.OPERATE, operate);

		String mUserName = settings.getString(User.Setting.NAME, null);
		if (!TextUtils.isEmpty(mUserName)) {
			info.setAttribute(User.Setting.NAME, mUserName);
		}
		String mUserMail = settings.getString(User.Setting.MAIL, null);
		if (!TextUtils.isEmpty(mUserMail)) {
			info.setAttribute(User.Setting.MAIL, mUserMail);
		}
		String mUserIM = settings.getString(User.Setting.IM, null);
		if (!TextUtils.isEmpty(mUserIM)) {
			info.setAttribute(User.Setting.IM, mUserIM);
		}
		String mUserAddress = settings.getString(User.Setting.ADDR, null);
		if (!TextUtils.isEmpty(mUserAddress)) {
			info.setAttribute(User.Setting.ADDR, mUserAddress);
		}
		String oldpwd = settings.getString(User.Setting.OLDPWD, null);
		if (!TextUtils.isEmpty(oldpwd)) {
			info.setAttribute(User.Setting.OLDPWD, oldpwd);
		}
		String newpwd = settings.getString(User.Setting.NEWPWD, null);
		if (!TextUtils.isEmpty(newpwd)) {
			info.setAttribute(User.Setting.NEWPWD, newpwd);
		}
		root.appendChild(user);

		editor.clear();
		if (User.Setting.SET.equals(operate)) {
			editor.remove(User.Setting.OPERATE);
		} else if (User.Setting.GET.equals(operate)) {
			editor.putString(User.Setting.OPERATE, User.Setting.GET);
		}
		editor.commit();
		Log.d(LOGCAT, "send user xml:" + user.toString());

		return 1;
	}

	/**
	 * info name="用户名" mail="邮箱" im="即时通讯" addr="通讯地址" />
	 * <!—更新失败返回错误节点,成功则不存在该节点 --> <error number="错误号"
	 * 
	 * @param result
	 * @param root
	 */
	public static void parseUserInfo(UpdateResult result, Element root) {
		Log.d(LOGCAT, "parse User xml ...");
		// authenticate
		NodeList nl = root.getElementsByTagName(User.Setting.ELEMENT_USER);
		if (nl != null && nl.getLength() > 0) {
			NodeList sub_nl = ((Element) nl.item(0))
					.getElementsByTagName(User.Setting.ELEMENT_INFO);
			Log.d(LOGCAT, "info notelist size:" + sub_nl.getLength());
			if (sub_nl != null && sub_nl.getLength() > 0) {
				if (result.mUser == null) {
					result.mUser = new User();
				}
				result.mUser.setName(((Element) sub_nl.item(0))
						.getAttribute(User.Setting.NAME));
				result.mUser.setMail(((Element) sub_nl.item(0))
						.getAttribute(User.Setting.MAIL));
				result.mUser.setIm(((Element) sub_nl.item(0))
						.getAttribute(User.Setting.IM));
				result.mUser.setAddr(((Element) sub_nl.item(0))
						.getAttribute(User.Setting.ADDR));
				Log.d("parse user->", result.mUser.getInfo());
				result.mErrorMsg=0;
			} else {
				sub_nl = ((Element) nl.item(0))
						.getElementsByTagName(User.Setting.ELEMENT_ERORR);
				if (sub_nl != null && sub_nl.getLength() > 0) {
					String errorMsg = ((Element) sub_nl.item(0))
							.getAttribute(User.Setting.ELEMENT_ERORR_NUMBER);
					result.mErrorMsg = Integer.valueOf(errorMsg);
				}
				Log.i(LOGCAT, "error:"+result.mErrorMsg);
			}

			NodeList acc_nl = ((Element) nl.item(0))
					.getElementsByTagName(User.Setting.ACCOUNT);
			if (null != acc_nl) {
				for (int x = 0; x < acc_nl.getLength(); x++) {
					Element info = (Element) acc_nl.item(x);
					result.mAppAccount.put(info.getAttribute("appid"),
							info.getAttribute("name"));
				}
			}
		}
	}

	public static void loadUser(Context cxt) {
		SharedPreferences settings = cxt.getSharedPreferences(
				InfoMainView.USERINFODATA_NEW, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(User.Setting.OPERATE, User.Setting.GET);
		editor.commit();
	    new UpdateExecutor(cxt,UpdateExecutor.GETUSER).execute();
	}

	public void saveUser(SharedPreferences.Editor editor) {
		editor.clear();
		if (name != null)
			editor.putString(User.Setting.NAME, name);
		if (mail != null)
			editor.putString(User.Setting.MAIL, mail);
		if (im != null)
			editor.putString(User.Setting.IM, im);
		if (addr != null)
			editor.putString(User.Setting.ADDR, addr);
		editor.commit();
	}

	public static void saveUserInfomation(Context context, UpdateResult ur) {
		SharedPreferences settings = context.getSharedPreferences(
				InfoMainView.USERINFODATA, 0);
		SharedPreferences.Editor editor = settings.edit();
		if (ur == null || ur.mUser == null) {
			return;
		}
		ur.mUser.saveUser(editor);
		settings = context.getSharedPreferences(InfoMainView.USERINFODATA_NEW,
				0);
		String operate = settings.getString(User.Setting.OPERATE, null);
		if (User.Setting.GET.equals(operate)) {
			ViewUtils.startUser(context);
			editor = settings.edit();
			editor.remove(User.Setting.OPERATE);
			editor.commit();
		}
	}

	public void saveCreate(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				InfoMainView.USERINFODATA_NEW, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(User.Setting.OPERATE, User.Setting.SET);
		if (!TextUtils.isEmpty(name))
			editor.putString(User.Setting.NAME, name);
		if (!TextUtils.isEmpty(mail))
			editor.putString(User.Setting.MAIL, mail);
		if (!TextUtils.isEmpty(im))
			editor.putString(User.Setting.IM, im);
		if (!TextUtils.isEmpty(addr))
			editor.putString(User.Setting.ADDR, addr);
		if (!TextUtils.isEmpty(oldpwd))
			editor.putString(User.Setting.OLDPWD, oldpwd);
		if (!TextUtils.isEmpty(newpwd))
			editor.putString(User.Setting.NEWPWD, newpwd);
		editor.commit();
	}

	private String getInfo() {
		return String.format("name=%s mail=%s im=%s addr=%s", name, mail, im,
				addr);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

}
