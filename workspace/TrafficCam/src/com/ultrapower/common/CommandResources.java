// Copyright (c) 2005 Sony Ericsson Mobile Communications AB
//
// This software is provided "AS IS," without a warranty of any kind. 
// ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, 
// INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A 
// PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. 
//
// THIS SOFTWARE IS COMPLEMENTARY OF JAYWAY AB (www.jayway.se)

/**
//VideoIM文档生成日期：2005.10.12
//
//(1)概述：
//类名称：CommandResources
//类说明：
//	提供本应用所需要的各种资源的Mapper的功能，仿照BlueGammon程序的做法。
* 
//所在子系统：VideoIM
//
//系统总描述:
	    我们提供的VideoIM手机自动拍照上传器J2ME版本[开源]是
	    一个可以下载到手机(例如Nokia7610已经确实可以下载安装并正常运行)的应用程序，
	    用来自动驱动手机摄像头定时拍摄,并后台将JPEG图像(很小，大约几KB)上传到服务器上，
	    这样就可以帮助其他系统工作，比如PC机上的MSN Messenger可以和你的移动MSN Messenger
	    通过这种方式视频聊天，对方可以每隔十几秒钟看到你的手机所看到的画面了。

	 子系统描述：
		VideoIM的功能列表：
			1:我要MobileWebCam
				启动MobileWebCam
				停止MobileWebCam
			2:MobileWebCam设置
			3:关于我
			4:退出


//(2)历史记录:
//修改人: 郑昀(2005.10.12)
//联系我: Google Talk >> zhengyun@gmail.com
//Blogs:    http://blog.csdn.net/zhengyun_ustc/以及http://www.cnblogs.com/zhengyun_ustc

//(3)版权声明:
//由于我这个版本的VideoIM手机自动拍照上传器也是基于Mowecam的设计理念基础上改编而来的，
//所以决定遵照GPL协议的大意开放源代码，您可以自由传播和修改，在遵照GPL协议的约束条件的前提下。

//(4)相关资源:
1：《[J2ME]VideoIM手机自动拍照上传器开源说明》
2：《[J2ME]VideoIM手机自动拍照上传器设计说明》
3：下载源代码：

////////////////////////////////////////////////////////////////////*/
package com.ultrapower.common;

import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.lcdui.Image;

/**
 * Primitive resource mapper.
 * 
 * @author Peter Andersson
 */
public class CommandResources
{
  // Text keys
  private static int TXTID = 0;
  // text / page titles
  public static final int TXT_T_APP            = TXTID++;
  // RMS Name
  public static final int TXT_RMS_STORENAME    = TXTID++;
  
  public static final int TXT_T_STOPWEBCAM     = TXTID++;
  public static final int TXT_T_STARTWEBCAM    = TXTID++;
  public static final int TXT_T_SETTINGS       = TXTID++;
  public static final int TXT_T_GETSTREAM      = TXTID++;
  public static final int TXT_T_ABOUT          = TXTID++;
  // text / item labels
  public static final int TXT_I_WANTWEBCAM     = TXTID++;
  public static final int TXT_I_SETTINGS       = TXTID++;
  public static final int TXT_I_ABOUT          = TXTID++;
  public static final int TXT_I_EXIT           = TXTID++;
  // text / commands
  public static final int TXT_C_START_WEBCAM   = TXTID++;
  public static final int TXT_C_STOP_WEBCAM    = TXTID++;
  public static final int TXT_C_OK             = TXTID++;
  public static final int TXT_C_CANCEL         = TXTID++;
  public static final int TXT_C_BACK           = TXTID++;
  public static final int TXT_C_SAVESETTINGS   = TXTID++;
  public static final int TXT_C_QUIT           = TXTID++;
  
  // text / other stuff
  public static final int TXT_ABOUT            = TXTID++;
  public static final int TXT_WAITTITLE        = TXTID++;
  public static final int TXT_WAITMESSAGE        = TXTID++;
  
  public static final int TXT_WEBCAMTIPS       = TXTID++;
  public static final int TXT_POSTERROR_TITLE  = TXTID++;
  public static final int TXT_POSTERROR_MSG    = TXTID++;
  public static final int TXT_POSTSUCC_TITLE   = TXTID++;
  public static final int TXT_POSTSUCC_MSG     = TXTID++;
  
  /*
   * 我的转换服务器和下载服务器地址
   */
  public static final int TXT_MY_3GP_SERVER_URL  		 = TXTID++;
  public static final int TXT_DOWNLOAD3GPFILE_SERVER_URL  = TXTID++;
  
  public static final int TXT_MY_3GP_SERVER_METHOD  	 = TXTID++;
  public static final int TXT_MY_3GP_SERVER_METHOD_RETURN  	 = TXTID++;
  public static final int TXT_MY_3GP_SERVER_METHOD_RESPONSE  	 = TXTID++;
  
  public static final int TXT_DEMO_3GPFILE  	 = TXTID++;
  
  public static final int TXT_URL_MMS_SERVER_1  	 = TXTID++;
  
  
  public static final int RMS_KEY_DOWN3GP_SERVER_URL  	 = 0;
  public static final int RMS_KEY_MMS_TIMELIMIT  			 = 1;
  public static final int RMS_KEY_MY_3GP_SERVER_URL  		 = 2;

  // Image keys
  private static final int OFFSET_IMG          = 100;
  public static final int IMG_COMMAND   	      = 100;
  public static final int IMG_ABOUT	          = 101;
  
  // Text data
  protected static final char[][] TEXTBUF = {
    "交通路况".toCharArray(),
    "TrafficViewer".toCharArray(),
    
    "交通路况停止".toCharArray(),
	"交通路况启动".toCharArray(),
    "设置".toCharArray(),
	"获取实时录像中".toCharArray(),
    "关于".toCharArray(),
    
	"TrafficView".toCharArray(),
    "Settings".toCharArray(),
    "About".toCharArray(),
    "Exit".toCharArray(),

	"StartMonitor".toCharArray(),
    "StopMonitor".toCharArray(),
    "OK".toCharArray(),
	"Cancel".toCharArray(),
    "Back".toCharArray(),
    "Save Current".toCharArray(),
	"Exit".toCharArray(),
    
    ("交通路况监视器-实时获取服务由郑昀编写,你可以自由改写并传播,但请尽量保留原作者信息。\n" +
		"应用于支持播放3gp媒体格式的手机。获取北京各个交通要道监视器的实时录像并播放。  \n" +
        "GoogleTalk>> zhengyun@gmail.com  \n" +
        "Blog>> http://zhengyun_ustc.cnblogs.com/ \n" +
        "Version>> 1.1.1 \n" +
        "Date>> 2005.10.21").toCharArray(),
        
     "正在下载录像".toCharArray(),
	 "下载录像...".toCharArray(),
     ("Tips:您可以选择菜单上的“启动监视器”来播放交通要道监视器的实时录像。").toCharArray(),
     
     
     "下载录像时发生了错误".toCharArray(),
     "下载实况录像时发生异常:".toCharArray(),
     
     "下载录像成功".toCharArray(),
     "准备播放实况录像!".toCharArray(),
     
	 "http://219.238.168.183:8080/TrafficStream/services/Streaming".toCharArray(),
	 "http://219.238.168.183/traffic/".toCharArray(),
	 "getstream".toCharArray(),
	 "getstreamReturn".toCharArray(),
	 "getstreamResponse".toCharArray(),
	 
	 "/res/3gp/demo.3gp".toCharArray(),
	 
	 "mms://220.194.63.17/cebeijing10".toCharArray(),
  };
  
  /** Image cache */
  protected static Hashtable m_images = new Hashtable();
  
  // Image resource names
  protected static final String[] IMGNAME_MAP ={
    "command.png",
    "about.png",
  };
  
  /**
   * Returns specified text as character array.
   * @param id  The id of the text.
   * @return    A text as char array.
   */
  public static char[] getChars(int id)
  {
    return TEXTBUF[id];
  }
  
  /**
   * Returns specified text as string.
   * @param id  The id of the text.
   * @return    A text as String.
   */  
  public static String getString(int id)
  {
    return new String(getChars(id));
  }
  
  /**
   * Returns specified image.
   * @param id  The id of the image.
   * @return    An image.
   */
  public static synchronized Image getImage(int id)
  {
    id -= OFFSET_IMG;
    Image img = (Image)m_images.get(new Integer(id));
    if (img == null)
    {
      try
      {
		  /*
		   * 注意原来这里写的是"/" +，始终无法加载图像；
		   * 所以我改为了"/res/" + 
		   */
        img = Image.createImage("/res/icons/" + IMGNAME_MAP[id]);
		System.out.println("get image:/res/icons/" + IMGNAME_MAP[id]);
        m_images.put(new Integer(id), img);
      }
      catch (Exception e)
      {
        System.out.println("Error getting resource img " + IMGNAME_MAP[id]
           + ">>" + e.getMessage());
        e.printStackTrace();
      }
    }
    return img;
  }
}
