package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;
import com.android.email.provider.EmailContent.Account;
import android.content.Context;
import android.util.Log;

class EmailProfile extends Profile {
	public final static String TAG = "EmailProfile";

	public class EmailArg {
		public String emailAddress = null;
		public String incomingPassword = null;
		public String outgoingPassword = null;
		public String incomingServer = null;
		public String outgoingServer = null;
		public String incomingServerPort = null;
		public String outgoingServerPort = null;
		public boolean incomingSsl = false;
		public boolean outgoingSsl = false;
		public String emailAccountType = null;
		public String emailAccountName = null;
	}

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));

		if (key.equals("EmailAccountName")) {
//			emailarg.emailAccountName = value;
		} else if (key.equals("EmailAccountType")) {
			emailarg.emailAccountType = value;
		} else if (key.equals("EmailAddress")) {
			emailarg.emailAddress = value;
		} else if (key.equals("IncomingMailServerHostName")) {
			emailarg.incomingServer = value;
		} else if (key.equals("IncomingMailServerPortNumber")) {
			emailarg.incomingServerPort = value;
		} else if (key.equals("IncomingMailServerUseSSL")) {
			emailarg.incomingSsl = value.equals("true") ? true : false;
		} else if (key.equals("IncomingPassword")) {
			emailarg.incomingPassword = value;
		} else if (key.equals("OutgoingMailServerHostName")) {
			emailarg.outgoingServer = value;
		} else if (key.equals("OutgoingMailServerPortNumber")) {
			emailarg.outgoingServerPort = value;
		} else if (key.equals("OutgoingMailServerUseSSL")) {
			emailarg.outgoingSsl = value.equals("true") ? true : false;
		} else if (key.equals("OutgoingPassword")) {
			emailarg.outgoingPassword = value;
		} else if (key.equals("OutgoingMailServerUsername")) {
			
		} else if (key.equals("IncomingMailServerUsername")) {
			
		} else if (key.equals("EmailAccountDescription")) {
			emailarg.emailAccountName = value;
		} else {
			super.setValue(key, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");

		int ret = -1;

		clearProfile(context);
		
		if (null != emailarg.emailAddress) {

			if (emailarg.emailAccountName == null) {
				emailarg.emailAccountName = emailarg.emailAddress;
			}

			Account account = EmailSetup.systemHaveSameConfig(context,
					emailarg.emailAddress);

			EmailSetup setup = new EmailSetup();

			setup.setEmailAccount(context, emailarg, account);

			ret = setup.addEmailAccount(context);

			if (ret > 0) {
				String pType = ProfileType.Profile_Email.toString();
				long cid = setup.getEmailAccountId();
				SettingsContent pc = dbHaveSameConfig(context, pType, cid);
				if ( pc == null ) {
					pc = new SettingsContent();
					pc.setProfileArg(emailarg.emailAddress, uuid, cid, version,
							pType);
					pc.add(context);
				} else {
					pc.setProfileArg(null, null, -1, version,
							pType);
					pc.update(context);
				}

				ret = 1;
			}
		}
		return ret;
	}

	@Override
	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");

		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			ver = VERSION;
		}

		String type = ProfileType.Profile_Email.toString();

		ArrayList<SettingsContent> dblist = SettingsContent.queryContentBy_Type_Version(context, type, ver);

		for (SettingsContent olddb : dblist) {
			boolean delete = true;
			if (null != emailarg.emailAddress) {
				if (emailarg.emailAddress.equals(olddb.getName())) {
					delete = false;
				}
			}
			if (delete) {
				Long cid = Long.valueOf(olddb.getCid());
				EmailSetup.delEmailAccountById(context, cid);
				SettingsContent.delContentById(context, olddb.getId());
			}
		}

		return 0;
	}

	private static SettingsContent dbHaveSameConfig(Context context,
			String ptype, long cid) {
		SettingsContent value = null;

		value = SettingsContent.queryContentBy_Type_Cid(context, ptype, String
				.valueOf(cid));

		return value;
	}

	EmailArg emailarg = new EmailArg();
}