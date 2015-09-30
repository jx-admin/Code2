package com.aess.aemm.installservice;

interface IRemoteServiceCallback {
    void installResult(String packageName, int error);
    void uninstallResult(String packageName, boolean succeeded);
}
