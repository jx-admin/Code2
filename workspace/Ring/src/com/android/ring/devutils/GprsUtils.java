package com.android.ring.devutils;

import java.lang.reflect.Method;

import com.android.log.CLog;

import android.content.Context;
import android.net.ConnectivityManager;

public class GprsUtils {
	CLog cLog=new CLog(GprsUtils.class.getSimpleName());
	private static final boolean ENABLE=true;
	private Context context;
	private ConnectivityManager mCM; 
	private boolean oldEnable;
	private boolean enable;
	
	public GprsUtils(Context context){
		this.context=context;
	}
	
	public void onCreate(){
		cLog.println("onCreate");
		mCM = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		oldEnable=gprsIsOpenMethod("getMobileDataEnabled");
		cLog.println("old gprs "+oldEnable);
		if(oldEnable!=ENABLE) {
			setGprsEnabled("setMobileDataEnabled", ENABLE); 
		}
		enable=gprsIsOpenMethod("getMobileDataEnabled");
		cLog.println("cur gprs "+enable);
	}
	
	public void onDestroy(){
		cLog.println("onDestroy");
		if(oldEnable!=ENABLE){
			setGprsEnabled("setMobileDataEnabled", oldEnable); 
		}
		enable=gprsIsOpenMethod("getMobileDataEnabled");
		cLog.println("cur gprs "+enable);
	}
	
	/*
	接下来用到了三个自定义的方法
	 gprsIsOpenMethod  是从framework中取得getMobileDataEnabled这个方法, 主要用来检测GPRS是否打开
	 setGprsEnabled  取得setMobileDataEnabled方法, 用来打开或关闭GPRS
	 在这里我们只要调用grpsEnabled即可。*/
	//打开或关闭GPRS 
	private boolean gprsEnabled(boolean bEnable) 
	{ 
		Object[] argObjects = null; 
		
		boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled"); 
		if(isOpen == !bEnable) 
		{ 
			setGprsEnabled("setMobileDataEnabled", bEnable); 
		} 
		
		return isOpen;   
	} 
	
	//检测GPRS是否打开 
	private boolean gprsIsOpenMethod(String methodName) 
	{ 
		Class cmClass       = mCM.getClass(); 
		Class[] argClasses  = null; 
		Object[] argObject  = null; 
		
		Boolean isOpen = false; 
		try 
		{ 
			Method method = cmClass.getMethod(methodName, argClasses); 
			
			isOpen = (Boolean) method.invoke(mCM, argObject); 
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		} 
		
		return isOpen; 
	} 
	
	//开启/关闭GPRS 
	private void setGprsEnabled(String methodName, boolean isEnable) 
	{ 
		Class cmClass       = mCM.getClass(); 
		Class[] argClasses  = new Class[1]; 
		argClasses[0]       = boolean.class; 
		
		try 
		{ 
			Method method = cmClass.getMethod(methodName, argClasses); 
			method.invoke(mCM, isEnable); 
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		} 
	} 
//	 Class.getMethod   是从framework搜索指定的方法，  返回的Method就可以使用该方法, 第二个参数是该方法的参数类型。
//	 Method.invoke  使用从framework里搜索到的方法, 第二个是参数。
	
}
