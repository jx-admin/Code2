
package com.act.mbanking;

import java.util.List;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.GetRecipientListModel;
import com.act.mbanking.utils.LogManager;

public class App extends Application {
	public String msgTitle=null;
	public String msgContext=null;

    /**
     * 测试时使用的变量打包的时候这个值应该为false
     */
    public boolean initValue = false;

    public static final boolean isNewPaymentsUpdate = true;

    /**
     * 版本名名称
     */
    String versionName;

    public static App app;

    /**
     * 版本号
     */
    int versionCode;

    private String userName;

    private String password;

    /**
     * 从登录进来
     */
    public boolean isFrist = false;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        LogManager.d(this + "onCreate");
        try {
            PackageManager pm = getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
            versionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * "003"
     */
    public List<AccountsForServiceModel> bankTransferAccounts;

    /**
     * 008
     */
    public List<AccountsForServiceModel> transferEntryAccounts;

    /**
     * 022
     */
    public List<AccountsForServiceModel> simTopUpAccounts;

    /**
     * 026
     */
    public List<AccountsForServiceModel> chargeAccounts;

    public GetRecipientListModel getRecipientListModel;

    /**
     * 注销
     */
    public void logOut() {

        if (bankTransferAccounts != null) {
            bankTransferAccounts.clear();
        }
        if (transferEntryAccounts != null) {
            transferEntryAccounts.clear();
        }
        if (simTopUpAccounts != null) {
            simTopUpAccounts.clear();
        }
        if (chargeAccounts != null) {
            chargeAccounts.clear();
        }
        bankTransferAccounts = null;
        transferEntryAccounts = null;
        simTopUpAccounts = null;
        chargeAccounts = null;
        isFrist = false;
        getRecipientListModel = null;

    }

    public static interface DataLoadListener {

        void onLoad(int progress);
    }
}
