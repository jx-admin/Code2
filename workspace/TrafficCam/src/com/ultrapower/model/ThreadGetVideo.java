/**
//VideoIM文档生成日期：2005.10.12
//
//(1)概述：
//类名称：ThreadPostVideo
//类说明：
//	提供抓拍照片并发送的功能
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

import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Item;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;

import com.ultrapower.common.CommandResources;
import com.ultrapower.control.GUIController;

/**********************************************************
//ThreadPostVideo
//
//Class Description:
//	提供抓拍照片并发送的功能
//
//Author:
// zhengyun@ultrapower 2005.10.12
//
**********************************************************/
public class ThreadGetVideo
	implements Runnable {
	
	private GUIController 	m_controller = null;
	private VideoControl 	m_video = null;
	private Player         m_player = null;
	private boolean 		m_bDownloadVideo;
	private VideoSettings 	m_Settings = VideoSettings.getInstance();
	private InputStream    m_isInputMedia; 
		
	public ThreadGetVideo(GUIController controll)
    {
		System.out.println("/** Enter ThreadPostVideo Constractor!");
		m_controller 	 = controll;
		m_bDownloadVideo = false;
    }
	
	public synchronized void setDownloadVideo(boolean value)
	{
		m_bDownloadVideo = value;
	}
	
	public final void run()
    {
        /* Use networking if necessary */
        long lngStart;
        long lngTimeTaken;
		String sProgressWAITTITLE = String.valueOf(
				CommandResources.getChars(CommandResources.TXT_WAITTITLE));
        
		while(true)
		{
	        try
	        {
				//  我们是依靠m_bDownloadVideo来判断是否去得到3gp并下载的.
				//  如果当前m_bDownloadVideo是false,那么本线程就只能先睡眠一段时间了.
	            if( m_bDownloadVideo ) 
				{
					System.out.println("/* StartWebcamDownload::开始下载实况录像!");
		            
		            /*
		             *  Change to run locally on developer machine
		             */
					String serviceNamespace = 
						String.valueOf(m_controller.getSettings().getMyConvertServerURL());
							//String.valueOf(
							//		CommandResources.getChars(
							//				CommandResources.TXT_MY_3GP_SERVER_URL));
					String methodName = 
						String.valueOf(
								CommandResources.getChars(
										CommandResources.TXT_MY_3GP_SERVER_METHOD));
					String methodReturnName = 
						String.valueOf(
								CommandResources.getChars(
										CommandResources.TXT_MY_3GP_SERVER_METHOD_RETURN));
					String methodResponseName = 
						String.valueOf(
								CommandResources.getChars(
										CommandResources.TXT_MY_3GP_SERVER_METHOD_RESPONSE));
					String downloadSiteURL = 
						String.valueOf(m_controller.getSettings().getDownload3gpFileSiteURL());
						//String.valueOf(
						//		CommandResources.getChars(
						//				CommandResources.TXT_DOWNLOAD3GPFILE_SERVER_URL));
					String serviceUrl = 
						serviceNamespace + "?wsdl";
		
					System.out.println("Try add ClassMap.....");
		            //ClassMap classMap = new ClassMap(Soap.VER11);
					//classMap.prefixMap = Soap.prefixMap[2];

					System.out.println("/* Try new SoapObject */\n" + 
							"	serviceNamespace:" + serviceNamespace);
		            SoapObject request = new SoapObject(serviceNamespace, methodName );
					SoapSerializationEnvelope envelope =
						new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.bodyOut = request;
					/*envelope.addMapping(serviceNamespace, 
							methodResponseName, 
							PropertyInfo.VECTOR_CLASS);*/
					//(new MarshalBase64()).register(envelope);
					/*envelope.addMapping(
							"http://xml.apache.org/xml-soap",
							"Vector",
							PropertyInfo.VECTOR_CLASS); 
					envelope.addMapping(
							"http://schemas.xmlsoap.org/soap/encoding/",
							"string",
							PropertyInfo.STRING_CLASS); 
					envelope.addMapping(
							"http://schemas.xmlsoap.org/soap/encoding/",
							"int",
							PropertyInfo.INTEGER_CLASS);*/
					
					/*
					 * 添加 web service的输入参数
					 */
					String strMMSURL = 
						String.valueOf(m_controller.getSettings().getMyConvertServerURL());
						//String.valueOf(
						//		CommandResources.getChars(
						//				CommandResources.TXT_URL_MMS_SERVER_1));
					request.addProperty("url", 
							strMMSURL);
					System.out.println("/* 流媒体服务器:" + strMMSURL);
					String strTimeLimit = 
						String.valueOf(m_controller.getSettings().getLiveMediaTimeLimit());
						//String.valueOf(VideoSettings.getInstance().getLiveMediaTimeLimit());
					request.addProperty("timeVideo", 
							strTimeLimit);
					System.out.println("/* 时间秒数:" + strTimeLimit);
					String str3gpFileName = 						
						String.valueOf(
							BuildLongFromString(
									GetCID() + "thisisasalt1234")) + ".3gp";
					request.addProperty("FileName", 
							str3gpFileName);
					System.out.println("/* 文件名:" + str3gpFileName);
					
		
					System.out.println("/* Try new HttpTransport \n" + 
							"	serviceUrl:" + serviceUrl + "	;method:" + methodName);
		            //HttpTransport tx = new HttpTransport(serviceUrl, methodName);
		            //tx.setClassMap( classMap );
		            
					HttpTransport tx = 
						new HttpTransport(serviceUrl);
					tx.debug = true;
							            
					System.out.println("Try call Streaming web service");	
					tx.call(serviceNamespace + "#" + methodName,
							envelope);
					//Object Response = (Object)tx.call(request);
					System.out.println("End call Streaming web service");				
					
					KvmSerializable o3gpResponse = (KvmSerializable)envelope.bodyIn;
					if(o3gpResponse != null)
					{
						PropertyInfo info = new PropertyInfo();
						Hashtable properties = new Hashtable();
						//String str3gpResponse;
						
				        int cnt = o3gpResponse.getPropertyCount();
						for (int i = 0; i < cnt; i++) {
							o3gpResponse.getPropertyInfo(i, properties, info);
							System.out.println("info name:" + info.name);
							if(methodReturnName.equalsIgnoreCase(info.name))
							{
								System.out.println("这正是我们要找的返回值!" );
								//str3gpResponse = (String)o3gpResponse.getProperty(i);
								//System.out.println("服务器告诉的URL为:" + str3gpResponse.getStringAt(0));
							}
						}
						
						downloadSiteURL = downloadSiteURL + str3gpFileName;
						System.out.println("服务器告诉的URL为:" + downloadSiteURL);
						
						HttpConnection conn = 
							(HttpConnection)Connector.open(downloadSiteURL);
						conn.setRequestMethod(HttpConnection.GET);
						conn.setRequestProperty("Content-Length", "0");
						
						conn.setRequestProperty("Accept-Language", "zh-cn");
						conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
						
						conn.setRequestProperty("Connection", "close"); 
						System.out.println("1");
						m_isInputMedia = 
							(InputStream)conn.openInputStream();
						System.out.println("2");
						m_player = (Player)Manager.createPlayer(m_isInputMedia, "video/3gpp");
						m_player.realize();
						System.out.println("3");
						m_video = (VideoControl)m_player.getControl("VideoControl");
						System.out.println("4");
						if(m_video != null)
				        {
							// the player can start with the smallest latency
							m_player.prefetch();
							
							System.out.println("/** 已经得到了VideoControl!");
						
							// Display Video as a Form item
							//
							Item itemVideoWindow;
							// 在USE_GUI_PRIMITIVE模式下，默认是可见的，而在USE_DIRECT_VIDEO模式下，
							// 默认是不可见的，需要通过方法setVisible(boolean visible)来设置
							(itemVideoWindow = (Item)m_video.initDisplayMode
								(VideoControl.USE_GUI_PRIMITIVE, null)).setLayout(3);
							m_controller.m_camForm.append(itemVideoWindow);
							m_player.start();
							System.out.println("/** 启动Player!");
		
				        }
				        
					}
					
					m_bDownloadVideo = false;
				}
				
				lngStart = System.currentTimeMillis();
                lngTimeTaken = System.currentTimeMillis() - lngStart;
                if(lngTimeTaken < 100)
                    Thread.sleep(75 - lngTimeTaken);
	        
	        }
			catch(Exception exc)
	        {
				System.out.println("/** 下载实况录像时发生异常!");
				m_bDownloadVideo = false;
				exc.printStackTrace();
				////////////////////////////////////////////
				// 告诉控制器,不能够下载录像并播放
				m_controller.setPlayerException(exc.getMessage()
						 + "/" + exc.getClass());
				m_controller.handleEventNoThrows(GUIController.EventID.EVENT_DOWNLOADDATA_ERROR, 
						null);
				////////////////////////////////////////////
	        }
		}
    }
	
	private String GetCID()
    {
        return "cid" + System.currentTimeMillis();
    }
	
	private long BuildLongFromString(String strMagic)
    {
        int j = 0;
        for(int k = 0; k < strMagic.length(); k++)
            if((k & 0x1) == 0)
                j ^= j << 7 ^ strMagic.charAt(k) ^ j >> 3;
            else
                j ^= ~(j << 11 ^ strMagic.charAt(k) ^ j >> 5);

        return (long)(j & 0x7fffffff);
    }
	
	public synchronized void PlayAgain()
	{
		System.out.println("/** PlayAgain!");
		try
		{
			if(m_player != null)
			{
				//if(m_player.getState() == m_player.CLOSED)
				{
					m_player.start();
				}
			}
		}
		catch(Exception exc)
		{
			
		}
		finally
		{
		}
	}
	
	public synchronized void StopPlayer()
	{
		System.out.println("/** StopPlayer!");
		try
		{
			if(m_player != null)
			{
				//if(m_player.getState() == m_player.STARTED)
				{
					m_player.stop();
				}
			}
		}
		catch(Exception exc)
		{
			
		}
		finally
		{
		}
	}
	
	public synchronized void DeleteVideoControl()
	{
		System.out.println("/** DeleteVideoControl!");
		try
		{
			if(m_player != null)
			{
				//if(m_player.getState() == m_player.STARTED)
				{
					m_player.stop();
				}
				m_player.deallocate();
				m_player = null;
			}
		}
		catch(Exception exc)
		{
			
		}
		finally
		{
			if(m_video != null)
			{
				m_video.setVisible(false);
				m_video = null;
			}
		}
	}

}
