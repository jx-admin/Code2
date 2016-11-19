package wu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by jx on 2016/6/20.
 */
public class PermissionUtils {
    /**
     * 检查需要申请的权限
     *
     * @param context
     * @param sPermissionsNeedCheck
     * @return
     */
    public static ArrayList<String> checkPermission(Context context, String[] sPermissionsNeedCheck) {
        ArrayList<String> permissions = null;
        if (sPermissionsNeedCheck != null && sPermissionsNeedCheck.length > 0) {
            permissions = new ArrayList();
            for (String permission : sPermissionsNeedCheck) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }

    /**
     * 权限检查，并且尝试申请缺少的权限
     *
     * @param activity
     * @param permissions 需要的权限
     * @param requestCode
     * @return
     * @see #requestPermissions(Activity, String[], int)
     */
    public static boolean checkRequestPermissions(final @NonNull Activity activity,
                                                  final @NonNull String[] permissions, final int requestCode) {
        //检查需要申请的权限
        ArrayList<String> denicePermissions = PermissionUtils.checkPermission(activity, permissions);
        if (denicePermissions != null && denicePermissions.size() > 0) {
            //尝试申请权限
            PermissionUtils.requestPermissions(activity, denicePermissions.toArray(new String[denicePermissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 向用户生情所需权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     * @see ActivityCompat#requestPermissions(Activity, String[], int)
     */
    public static void requestPermissions(final @NonNull Activity activity,
                                          final @NonNull String[] permissions, final int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 权限申请回调
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param showDialog   没有获取到所有权限时，是否提示用户：页面缺少权限不可用
     * @return true以获取所需权限，否则返回false
     */
    public static boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, boolean showDialog) {
        boolean isOk = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isOk = false;
                    break;
                }
            }
        }
        if (!isOk && showDialog) {
            if (!isOk) {
                PermissionUtils.permissionDeniedDialog(activity, true);
            }
        }
        return isOk;
    }

    public static void permissionDeniedDialog(final Activity context, final boolean finish) {
//        CommonTextDialog commonTextDialog = new CommonTextDialog(context);
//        commonTextDialog.setConfig(null, context.getString(R.string.permission_denied), context.getString(R.string.settings), context.getString(R.string.exist), new CommonDialog.OnCommonDialogListener() {
//            @Override
//            public boolean onOk() {
//                startAppSettings(context);
//                return true;
//            }
//
//            @Override
//            public boolean onCancel() {
//                if (finish)
//                    context.finish();
//                return true;
//            }
//
//            @Override
//            public boolean onDismiss() {
//                return true;
//            }
//
//            @Override
//            public boolean interceptOk() {
//                return false;
//            }
//        });
    }

    // 权限参数
    private static final String PACKAGE_URL_SCHEME = "package:";

    // 启动应用的设置
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
