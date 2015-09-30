/**
//VideoIM文档生成日期：2005.10.12
//
//(1)概述：
//类名称：VideoSettings
//类说明：
//	提供存储和读取RMS中关于本应用所需要的几个参数的功能
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
//创建人: 郑昀(2005.10.12)
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
package com.ultrapower.model;

import javax.microedition.rms.RecordStoreException;

import com.ultrapower.common.CommandResources;
import com.ultrapower.facade.RmsFacade;

/**********************************************************
//VideoSettings
//
//Class Description:
//	提供存储和读取RMS中关于本应用所需要的几个参数的功能
//
//Author:
//   zhengyun@ultrapower 2005.10.12
//
**********************************************************/
public class VideoSettings {

	private static VideoSettings 	 m_settings;
    /**
     * Singleton pattern is used to return 
     * only one instance of VideoSettings
     */
    public static synchronized VideoSettings getInstance(){
        if( m_settings == null ) {
			System.out.println("!-- VideoSettings::getInstance --!\n");
			m_settings = new VideoSettings();
			try
	        {			
				m_settings.LoadSettings();
				System.out.println("选择从RMS中读取设置");
	        }
	        catch(Exception exc)
	        {	
				m_settings.InitDefaultSettings();
				System.out.println("从RMS中无法读取,改为默认数值");
	        }
        }
        return m_settings;
    }
	
  /** 下载流媒体文件的地址,这是转换服务之后提供的文件下载地址，和转换3gp的服务器url不一样 */
  protected char[] m_sDownload3gpFileSiteURL;
  /** "获取流媒体实况录像的秒数" */
  protected int m_nLiveMediaTimeLimit;
  /** "我的转换mms为3gp的服务器的地址" */
  protected char[] m_sMyConvertServerURL;
  
  ///////////////////////////////////////////////////////////////////
  
  /**
   * Returns "下载流媒体文件的地址,这是转换服务之后提供的文件下载地址，和转换3gp的服务器url不一样".
   * @return	下载流媒体文件的地址.
   */
  public char[] getDownload3gpFileSiteURL()
  {
    return m_sDownload3gpFileSiteURL;
  }
  
  /**
   * Returns "获取流媒体实况录像的秒数".
   * @return	获取流媒体实况录像的秒数.
   */
  public int getLiveMediaTimeLimit()
  {
    return m_nLiveMediaTimeLimit;
  }
  
  /**
   * Returns "我的转换mms为3gp的服务器的地址".
   * @return	我的转换mms为3gp的服务器的地址.
   */
  public char[] getMyConvertServerURL()
  {
    return m_sMyConvertServerURL;
  }
  
  ///////////////////////////////////////////////////////////////////
  
  /**
   * set "下载流媒体文件的地址,这是转换服务之后提供的文件下载地址，和转换3gp的服务器url不一样".
   * @param 下载流媒体文件的地址.
   */
  public void setDownload3gpFileSiteURL(char[] value)
  {
    m_sDownload3gpFileSiteURL = value;
  }
  
  /**
   * set "获取流媒体实况录像的秒数".
   * @param	 获取流媒体实况录像的秒数.
   */
  public void setLiveMediaTimeLimit(int value)
  {
    m_nLiveMediaTimeLimit = value;
  }
  
  /**
   * set "我的转换mms为3gp的服务器的地址".
   * @param	 我的转换mms为3gp的服务器的地址.
   */
  public void setMyConvertServerURL(char[] value)
  {
	  m_sMyConvertServerURL = value;
  }
  
  /////////////////////////////////////////////////////////////
  
  /**
   * 将当前用户设置的各项参数写入到RMS中.
   */
  public void save()
  {
    RmsFacade.setChars(
			CommandResources.RMS_KEY_DOWN3GP_SERVER_URL,
			m_sDownload3gpFileSiteURL);
    RmsFacade.setInt(CommandResources.RMS_KEY_MMS_TIMELIMIT,
			m_nLiveMediaTimeLimit);
    RmsFacade.setChars(CommandResources.RMS_KEY_MY_3GP_SERVER_URL,
			m_sMyConvertServerURL);
  }
  
  /**
   * 将当前用户设置的各项参数写入到RMS中.
   */
  public void save(String sDownload3gpFileSiteURL,
		  String sLiveMediaTimeLimit,
		  String sMyConvertServerURL)
  {
	System.out.println("Enter VideoSettings::save");
	m_sDownload3gpFileSiteURL = sDownload3gpFileSiteURL.toCharArray();
    RmsFacade.setChars(CommandResources.RMS_KEY_DOWN3GP_SERVER_URL,
			m_sDownload3gpFileSiteURL);	
	
	m_nLiveMediaTimeLimit = Integer.valueOf(sLiveMediaTimeLimit).intValue();
    RmsFacade.setInt(CommandResources.RMS_KEY_MMS_TIMELIMIT,
			m_nLiveMediaTimeLimit);
		
	m_sMyConvertServerURL = sMyConvertServerURL.toCharArray();
    RmsFacade.setChars(CommandResources.RMS_KEY_MY_3GP_SERVER_URL,
			m_sMyConvertServerURL);
  }
  
  /**
   * 直接从RMS将各项参数提取出来
   */
  public void LoadSettings()
  throws RecordStoreException
  {
	  m_sDownload3gpFileSiteURL = RmsFacade.getChars(CommandResources.RMS_KEY_DOWN3GP_SERVER_URL);
	  if(m_sDownload3gpFileSiteURL.length > 0)
		  System.out.println("有数据:" + m_sDownload3gpFileSiteURL);
	  else
	  {
		  // 说明没有RMS记录,可能是第一次运行本应用
		  throw new RecordStoreException();
	  }
		 
	  m_nLiveMediaTimeLimit = RmsFacade.getInt(CommandResources.RMS_KEY_MMS_TIMELIMIT);
	  
	  m_sMyConvertServerURL = RmsFacade.getChars(CommandResources.RMS_KEY_MY_3GP_SERVER_URL);
	  
  }
  
  /**
   * Initializes this videoim
   */
  public void InitDefaultSettings()
  {
	  m_sDownload3gpFileSiteURL = CommandResources.getChars(CommandResources.TXT_DOWNLOAD3GPFILE_SERVER_URL);
	  m_sMyConvertServerURL = CommandResources.getChars(CommandResources.TXT_MY_3GP_SERVER_URL);
	  m_nLiveMediaTimeLimit = 3;
	  
	  // 将默认的数值写入RMS
	  save();
  }
}
