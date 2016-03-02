package wu.a.lib.net;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;

public class GPSUtils {
	
	/**判断GPS是否打开
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {   
		LocationManager lm = ((LocationManager) context   
				.getSystemService(Context.LOCATION_SERVICE));   
		List<String> accessibleProviders = lm.getProviders(true);   
		return accessibleProviders != null && accessibleProviders.size() > 0;   
	}

}
