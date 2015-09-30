package com.aess.androidsso;

import com.aess.androidsso.IRemoteServiceCallback;

interface IRemoteService {
    void setLoginCallback(IRemoteServiceCallback callback);
	String login(String appid);
	int getLoginResult();
    String getToken();
	void logout(String appid);
}
