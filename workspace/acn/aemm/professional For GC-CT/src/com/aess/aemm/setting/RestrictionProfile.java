package com.aess.aemm.setting;

import com.aess.aemm.function.ProfessionalFunction;
import com.aess.aemm.function.SingleProfessionalFunction;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

public class RestrictionProfile extends Profile {
	public static final String TAG = "RestrictionProfile";
	
	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));
		
		if ("allowAppInstallation".equals(key)) {
			allowAppInstallation = "false".equals(value) ? false : true;
		} else if ("allowCamera".equals(key)) {
			allowCamera = "false".equals(value) ? false : true;
		} else if ("allowScreenShot".equals(key)) {
//			allowScreenShot = "false".equals(value) ? false : true;
		} else if ("allowVoiceDialing".equals(key)) {
			allowVoiceDialing = "false".equals(value) ? false : true;
		} else if ("allowRecording".equals(key)) {
			allowRecording = "false".equals(value) ? false : true;
		} else if ("allowSMS".equals(key)) {
			allowSMS = "false".equals(value) ? false : true;
		} else if ("allowMMS".equals(key)) {
			allowMMS = "false".equals(value) ? false : true;
		} else if ("allowTalk".equals(key)) {
			allowTalk = "false".equals(value) ? false : true;
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");
		ProfessionalFunction pf = SingleProfessionalFunction.GetProfessionalFunction(context);
		
		try {
			pf.enableAllApkInstalled(allowAppInstallation);
			
			pf.enableCamera(allowCamera);
			
			pf.enableDial(allowTalk);
			
			pf.enableMMS(allowMMS);
			
			pf.enableSMS(allowSMS);
			
			pf.enableAudio(allowRecording);

			ProfessionalFunction.enableVoiceDialer(context, true);
			if(!allowVoiceDialing) {
				ProfessionalFunction.enableVoiceDialer(context, false);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");
		allowAppInstallation = true;
		allowCamera = true;
//		allowScreenShot = true;
		allowVoiceDialing = true;
		allowRecording = true;
		allowSMS = true;
		allowMMS = true;
		allowTalk = true;
		
		saveProfile(context);
		return 0;
	}
	
	private boolean allowAppInstallation = true;
	private boolean allowCamera = true;
//	private boolean allowScreenShot = true;
	private boolean allowVoiceDialing = true;
	private boolean allowRecording = true;
	private boolean allowSMS = true;
	private boolean allowMMS = true;
	private boolean allowTalk = true;
}
