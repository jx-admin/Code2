package wu.a.lib.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.Iterator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public final class NetUtils {
    private final static String TAG = "NetUtils";

    public static String getNetworkType(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null ? info.getTypeName() : null;
    }

    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = manager.getConnectionInfo();
            return null != wifiInfo ? wifiInfo.getMacAddress() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }

    public static boolean isMobileDataConnected(NetworkInfo activeNetwork) {
        return isNetworkConnected(activeNetwork)
                && ConnectivityManager.TYPE_MOBILE == activeNetwork.getType();
    }

    public static boolean isMobileDataConnected(Context context) {
        return isMobileDataConnected(getActiveNetworkInfo(context));
    }

    public static boolean isWifiConnected(NetworkInfo activeNetwork) {
        return isNetworkConnected(activeNetwork)
                && ConnectivityManager.TYPE_WIFI == activeNetwork.getType();
    }

    public static boolean isWifiConnected(Context context) {
        return isWifiConnected(getActiveNetworkInfo(context));
    }

    public static boolean isNetworkConnected(NetworkInfo activeNetwork) {
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isNetworkConnected(Context context) {
        final NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Returns the default link's IP addresses, if any, taking into account IPv4 and IPv6 style
     * addresses.
     * @param context the application context
     * @return the formatted and newline-separated IP addresses, or null if none.
     *
     *         192.168.0.2
     *         fe80:d222:beff:fe2b:a9fb
     */
    public static String getDefaultIpAddresses(Context context) {
        return formatIpAddresses(getActiveLinkProperties(context));
    }

    public static boolean isWifiAvailable(Context context) {
        return isWifiAvailable(getActiveNetworkInfo(context));
    }

    public static boolean isWifiAvailable(NetworkInfo activeNetwork) {
        return isNetworkAvailable(activeNetwork)
                && ConnectivityManager.TYPE_WIFI == activeNetwork.getType();
    }

    /**
     * Check whether network is available.
     *
     * <p>
     * 不同于{@link #isNetworkConnected}, 网络正在连接和连接上都会返回<code>true</code>
     * <p>
     *
     * @param context Application context
     * @return <code>true</code> if network available, <code>false</code> otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(getActiveNetworkInfo(context));
    }

    public static boolean isNetworkAvailable(NetworkInfo activeNetwork) {
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Returns the default link's IPv4 addresse
     * @param context the application context
     * @return the formatted IPv4 address, or null if none.
     */
    public static String getDefaultIpv4Address(Context context) {
        return formatIpv4Address(getActiveLinkProperties(context));
    }

    private static LinkProperties getActiveLinkProperties(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Method m = ConnectivityManager.class
                    .getDeclaredMethod("getActiveLinkProperties", (Class<?>[]) null);
            m.setAccessible(true);
            return (LinkProperties) m.invoke(cm, (Object[]) null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String formatIpv4Address(LinkProperties prop) {
        if (prop == null) return null;
        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
        // If there are no entries, return null
        if (!iter.hasNext()) return null;
        return iter.next().getHostAddress();
    }

    private static String formatIpAddresses(LinkProperties prop) {
        if (prop == null) return null;
        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
        // If there are no entries, return null
        if (!iter.hasNext()) return null;
        // Concatenate all available addresses, comma separated
        String addresses = "";
        while (iter.hasNext()) {
            addresses += iter.next().getHostAddress();
            if (iter.hasNext()) addresses += "\n";
        }
        return addresses;
    }

    public static String decodeUrl(final String encodedUrl) {
        return decodeUrl(encodedUrl, false);
    }

    public static String decodeUrl(
            final String encodedUrl, final boolean successIfEncodingUnsupported) {
        try {
            return URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            if (successIfEncodingUnsupported) {
                return null;
            } else {
                throw new IllegalStateException(ignored);
            }
        }
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wfm.isWifiEnabled();
    }

    public static boolean toggleWifiEnabled(Context context) {
        try {
            WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wfm.setWifiEnabled(!wfm.isWifiEnabled());
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean setWifiEnabled(Context context, boolean enabled) {
        try {
            WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wfm.setWifiEnabled(enabled);
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isMobileDataEnabled(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return (Boolean) cm.getClass()
                    .getMethod("getMobileDataEnabled", (Class[]) null)
                    .invoke(cm, (Object[]) null);
        } catch (Throwable e) {
            Trace.w(TAG, "Failed to execute getMobileDataEnabled", e);
        }
        return false;
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Field mServiceFiled = cm.getClass().getDeclaredField("mService");
            mServiceFiled.setAccessible(true);
            Object service = mServiceFiled.get(cm);

            Method setMobileDataEnabled = service.getClass()
                    .getDeclaredMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabled.setAccessible(true);
            setMobileDataEnabled.invoke(service, enabled);
        } catch (Throwable e) {
            Trace.w(TAG, "Failed to execute setMobileDataEnabled", e);
        }
    }

    public static char getKeyD() {
        return 'z';
    }
}
