package wu.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wu.utils.PermissionUtils;

/**
 * Created by jx on 2016/8/10.
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
    IBaseFragmentActivity iActivity;

    public void setBaseFragmentActivity(IBaseFragmentActivity iActivity) {
        this.iActivity = iActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBindData();
    }

    protected <T extends View> T findView(int id) {
        return (T) view.findViewById(id);
    }

    protected void setContentView(View view) {
        this.view = view;
    }

    protected abstract void onBindData();

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @return 处理返回true，否则返回false
     */
    protected boolean onActivityResultIntercept(int requestCode, int resultCode, Intent data) {
        return false;
    }

    /**
     * activity返回键处理
     *
     * @return 被处理返回true，否则返回false
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 所需权限列表，需要重载
     *
     * @return 所需权限列表
     */
    public String[] getPermissionsNeedCheck() {
        return null;
    }

    /**
     * 检测是否拥有所需权限
     *
     * @param context
     * @return 没有授予的所需权限
     */
    public List<String> checkPermission(Context context) {
        String[] sPermissionsNeedCheck = getPermissionsNeedCheck();
        if (sPermissionsNeedCheck == null || sPermissionsNeedCheck.length == 0) {
            return null;
        }
        //检查需要申请的权限
        ArrayList<String> permissions = PermissionUtils.checkPermission(context, sPermissionsNeedCheck);
        return permissions;
    }

    /**
     * 检测授权结果
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return 完全授权返回true，否则返回false
     */
    public boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        return PermissionUtils.onRequestPermissionsResult(activity, requestCode, permissions, grantResults, true);
    }

    /**
     * 退出Fragment，回调通知Activity
     */
    protected void onFragmentExit() {
        if (iActivity != null) {
            iActivity.onFragmentExit(this);
        }
    }

    /**
     * 开始异步加载，回调通知Activity,可以在activity级别显示loading弹框
     */
    protected void onFragmentStartLoading() {
        if (iActivity != null) {
            iActivity.onFragmentStartLoading(this);
        }

    }

    /**
     * 异步加载结束，回调通知Activity,可以关闭在activity级别显示的loading弹框
     */
    protected void onFragmentFinishLoad() {
        if (iActivity != null) {
            iActivity.onFragmentFinishLoad(this);
        }
    }
}
