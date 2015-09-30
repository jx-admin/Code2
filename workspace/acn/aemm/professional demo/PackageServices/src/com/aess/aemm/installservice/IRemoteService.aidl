package com.aess.aemm.installservice;

import com.aess.aemm.installservice.IRemoteServiceCallback;

interface IRemoteService {
	void installApplication(String appPath, IRemoteServiceCallback callback);
	void uninstallApplication(String packageName, IRemoteServiceCallback callback);
}
