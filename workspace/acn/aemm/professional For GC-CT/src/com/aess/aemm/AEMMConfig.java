package com.aess.aemm;

import com.aess.aemm.networkutils.AutoAdress;

public class AEMMConfig {
	
	public static final String TAG   = "AEMMConfig";
	public static final String HTTPS = "https://";
	public static final String HTTP  = "http://";
	public static final String TCP   = "tcp://";
	
	// auto address
	
	// test 1
    //public static final String GuideAddress   = "https://192.168.16.147:8000/urladapter.aspx";
	// debug 3.l
	//public static final String GuideAddress   = "https://111.207.222.194:9000/urladapter.aspx";
	// NLB 3.1
//	public static final String GuideAddress   = "https://111.207.222.195:9000/urladapter.aspx";
	// demo 3.1
//	public static final String GuideAddress = "https://111.207.222.194:8000/urladapter.aspx";
	// hof
	//public static final String GuideAddress = "https://170.252.34.79:8000/urladapter.aspx";
	// aws
	//public static final String GuideAddress = "https://50.112.130.13:8100/urladapter.aspx";
	// aws Singapore 3.0.2
	//public static final String GuideAddress = "https://50.112.130.13:8200/urladapter.aspx";
	// demo
	//public static final String GuideAddress = "https://124.205.250.21:8000/urladapter.aspx";
	// dianxin
//	public static final String GuideAddress = "https://10.3.88.93:8000/urladapter.aspx";
	// jx
//	public static final String GuideAddress = "https://192.168.16.143:8000/urladapter.aspx";
	// wfc
//	public static final String GuideAddress = "https://10.202.66.221:8000/urladapter.aspx";
	//3G
//	public static final String GuideAddress = "https://172.16.28.120:9000/urladapter.aspx";//3G
//	public static final String GuideAddress = "https://113.105.0.244:9000/urladapter.aspx";
	public static final String GuideAddress = "https://192.168.1.100:9000/urladapter.aspx";
	
	// SIGN == AUTO, get update address from service
	public static final String SIGN  =  AutoAdress.AUTO;
	
	/* if (VER >= NUM) {
    *    Aemm's version is 3.1
    *  } else {
    *  	 Aemm's version is 3.0.2
    *  }
    */
	public static final int    NUM          = 31;
	public static int          VER          = 31;
	
	public static int isPopu                = 1;
	public static int imsiCheck             = 0;
	
	public static boolean      v31          = true;
	
	// DEBUG == 2, write all protocol in file
	// didn't used "android.uid.system"
	public static final int    DEBUG        = 1;
}
