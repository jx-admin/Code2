package com.android.moshineifo;

/**如果我们想要在Android手机操作系统中查看具体的系统信息，比如内存大小，系统性能等等。具体的操作方法应该是怎样的呢?在这里大家就可以充分的掌握这一操作方法，方便我们对这一手机系统的了解。
 今天来说说如何查看Android系统信息中的手机属性、内存使用情况等信息，这些在J2me上已经家喻户晓了，在Android上面呢?其实也很简单，直接看下面的代码就ok啦。
 */
import android.app.ActivityManager;
import android.content.Context;

public class SystemProperty {
	/**
	 * 显示数据存库
	 */
	private StringBuffer buffer;

	public String getInfo(Context context) {
		initProperty();
		getMemoryInfo(context);
		return buffer.toString();
	}

	// Android系统信息具体查看方法
	private void initProperty() {
		initProperty("java.vendor.url", "java.vendor.url");
		initProperty("java.class.path", "java.class.path");
		initProperty("user.home", "user.home");
		initProperty("java.class.version", "java.class.version");
		initProperty("os.version", "os.version");
		initProperty("java.vendor", "java.vendor");
		initProperty("user.dir", "user.dir");
		initProperty("user.timezone", "user.timezone");
		initProperty("path.separator", "path.separator");
		initProperty(" os.name", " os.name");
		initProperty("os.arch", "os.arch");
		initProperty("line.separator", "line.separator");
		initProperty("file.separator", "file.separator");
		initProperty("user.name", "user.name");
		initProperty("java.version", "java.version");

		initProperty("java.home", "java.home");
	}

	private void initProperty(String description, String propertyStr) {
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		buffer.append("\n" + description).append("=:");
		buffer.append(System.getProperty(propertyStr)).append(" ");
	}

	// Android系统信息中内存情况查看
	private void getMemoryInfo(Context context) {
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		buffer.append(" 剩余内存:").append(outInfo.availMem >> 10).append("k");
		buffer.append(" 剩余内存:").append(outInfo.availMem >> 20).append("M");
		buffer.append(" 是否处于低内存状态:").append(outInfo.lowMemory);
	}
}
// 顺便提示一下，Android系统也提供了，Runtime类，但是该类获取到的信息Java虚拟机的使用情况信息。以上就是我们对Android
// 系统信息的具体查看方法教程。
