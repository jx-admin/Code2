package wu.a.lib.device;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

public class DeviceInfo {

	int mScreenWidth,mScreenHeight;
	public void getDisplay(Activity context){
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		mScreenWidth = mDisplayMetrics.widthPixels;
		mScreenHeight = mDisplayMetrics.heightPixels;
	}
	private DisplayMetrics dm;
	public String getDisplayInfo(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay(); 

        dm = new DisplayMetrics();
        display.getMetrics(dm);

		return "widthPixels:"+dm.widthPixels+" , "+"heightPixels:"+dm.heightPixels
				+"\ndensity:"+dm.density
				+"\ndensityDpi:"+dm.densityDpi
				+"\nscaledDensity:"+dm.scaledDensity
				+"\nxdpi:"+dm.xdpi+" , "+"\nydpi:"+dm.ydpi
				;
	}
	
	public String getInfo(Activity activity){
		return getDisplayInfo(activity)+"\nMemoryclass:"+getMemoryclass(activity)+"MB";
	}

	/**每个 android 平台内存限制不一样，从最开始的 16M 到 24M，以及后来的 32M，64M，或许以后会更大。
	那如何获取单个 app 内存限制大小呢？
	class : ActivityManager
	 * @param context
	 * @return n MB
	 */
	public int getMemoryclass(Context context){
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass(); 
	}
}
