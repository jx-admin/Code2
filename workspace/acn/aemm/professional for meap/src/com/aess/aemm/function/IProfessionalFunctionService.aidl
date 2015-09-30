package com.aess.aemm.function;

interface IProfessionalFunctionService{
	void addProtectedAEMMComponent(String component);
	void deleteProtectedAEMMComponent(String component);
	void addProhibitedApk(String apkName);
	void deleteProhibitedApk(String apkName);
	void enableDial(boolean enabled);
	void enableCamera(boolean enabled);
	void enableAudio(boolean enabled);
	void enableVideo(boolean enabled);
	void enableSMS(boolean enabled);
	void enableMMS(boolean enabled);
	void addProhibitedApplication(String application);
	void deleteProhibitedApplication(String application);
	void addProhibitedActivity(String actyName);
	void deleteProhibitedActivity(String actyName);
	void addAemmWifi(String key);
	void deleteAemmWifi(String key);
	void addAemmVPN(String key);
	void deleteAemmVPN(String key);
	void addAemmAPN(String key);
	void deleteAemmAPN(String key);
	void addAemmEmail(String key);
	void deleteAemmEmail(String key);	
	void enableAllApkInstall(boolean enabled);
}