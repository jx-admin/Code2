package wu.a.template.app;

/**
 * Created by Administrator on 2014/12/26.
 */
public class AppInfo {
	private String appName;
	// private String appVersion;
	// private Bitmap drawable;
	// private boolean isUserApp;
	private String packageName;
	private String className;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	// public String getAppVersion() {
	// return appVersion;
	// }

	// public void setAppVersion(String appVersion) {
	// this.appVersion = appVersion;
	// }

	// public Bitmap getDrawable() {
	// return drawable;
	// }

	// public void setDrawable(Bitmap drawable) {
	// this.drawable = drawable;
	// }

	// public boolean isUserApp() {
	// return isUserApp;
	// }

	// public void setUserApp(boolean isUserApp) {
	// this.isUserApp = isUserApp;
	// }

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String toString() {
		return appName + "," + /*
								 * appVersion + "," + drawable + "," + isUserApp
								 * + "," +
								 */packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
