package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;

import android.content.Context;
import android.security.Credentials;
import android.util.Log;

public class CertificateProfile extends Profile {

	public static final String TAG = "CertificateProfile";
	public static final String ROOTCERTIFICATE = "com.apple.security.root";
	public static final String PSKCERTIFICATE = "com.apple.security.pkcs12";

	public static final String node_value_content = "PayloadContent";
	public static final String node_value_type = "PayloadType";
	public static final String node_value_password = "Password";
	public static final String node_value_displayname = "PayloadDisplayName";

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));
		
		if (key.equals(node_value_content)) {
			data = value;
		} else if (key.equals(node_value_type)) {
			Certificatetype = value;
		} else if (key.equals(node_value_password)) {
			Password = value;
		} else if (key.equals(node_value_displayname)) {
			displayName = value;
		} else {
			return super.setValue(key, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");

		int ret = -1;
		String type = null;
		
		type = getCredType();
		
		clearProfile(context);
		
		CertificateSetup ss = new CertificateSetup(context);
		
		ss.setValues(data, displayName, Password, type);

		if (ss.AddCert() >= 0)
		{
			SettingsContent pc = new SettingsContent();
			pc.setProfileArg(displayName, uuid, -1, version, type);
			pc.add(context);
			ret = 1;
		}
		return ret;
	}

	private String getCredType() {
		String type;
		type = Credentials.PKCS12;
		if (Certificatetype != null) {
			if (Certificatetype.equals(ROOTCERTIFICATE)) {
				type = Credentials.CERTIFICATE;
			} else if (Certificatetype.equals(PSKCERTIFICATE)) {
				type = Credentials.PKCS12;
			}
		}
		return type;
	}

	@Override
	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");
		
		String type = getCredType();
		
		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			// delete all Profile
			ver = "-1";
		}
		
		ArrayList<SettingsContent> list = SettingsContent.queryContentBy_Type_Version(
				context, type, ver);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				SettingsContent pc = list.get(i);
				if (pc != null) {
					Log.i(TAG, "delete");
					String certname = pc.getName();
					// the id in local setting db
					CertificateSetup.deleteCert(certname);
					int num = SettingsContent.delContentById(context, pc.getId());
					Log.i(TAG, String.valueOf(num)
							+ "cert record has been deleted");
				}
			}
		}
		return 0;
	}
	
	private String data = null;
	private String Password = null;
	private String Certificatetype = null;
	private String displayName = null;
}
