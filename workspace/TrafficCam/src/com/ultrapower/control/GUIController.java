/*////////////////////////////////////////////////////////////////////
//文档生成日期：2005.10.12
//
//(1)概述：
//类名称：GUIController
//类说明：
//       MVC中的控制器部分，负责界面事件的处理，以及决定该显示哪一个Form。
//
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
//创建人: 郑昀(2005.10.07)
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
package com.ultrapower.control;

import java.io.InputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.ultrapower.common.CommandResources;
import com.ultrapower.facade.RmsFacade;
import com.ultrapower.midlet.TrafficView;
import com.ultrapower.model.VideoSettings;
import com.ultrapower.view.About;
import com.ultrapower.view.FormSetting;
import com.ultrapower.view.FormTrafficCam;
import com.ultrapower.view.MainMenuList;
import com.ultrapower.view.WaitFlash;

/**********************************************************
//	 GUIController
//
//	 Class Description:
//	 	MVC中的控制器部分，负责界面事件的处理，以及决定该显示哪一个Form。
//
//	 Author:
//	      zhengyun@ultrapower 2005.10.07
//
**********************************************************/
public class GUIController {
	public TrafficView	 		m_trafficcamMidlet;
	private VideoSettings 		m_Settings;
	private MainMenuList 		m_listMenu;
	private FormSetting 		m_settingForm;
	public  FormTrafficCam   	m_camForm;
	private WaitFlash 			m_waitFlash;
	private Alert 				m_alert;
	private About 				m_about;
	private VideoControl       m_video;
	
	//////////////////////////////////////////////////////////
	// 下面这4个参数是专门为了后台处理线程传递数据进来到控制器准备的
	// 所以保证这4个set函数为线程同步的
	/*
	 * 将要发送的图像数据的总字节数目
	 */
	private String m_sPostDataTotalLength;
	public synchronized void setPostDataTotalLength(String value)
	{
		m_sPostDataTotalLength = value;
	}
	/*
	 * 还剩余的图像字节数目
	 */
	private String m_sPostingDataLength;
	public synchronized void setPostingDataLength(String value)
	{
		m_sPostingDataLength = value;
	}
	/*
	 * 从远端服务器传回来的错误号
	 */
	private String m_sRemoteServerResponse;
	public synchronized void setRemoteServerResponse(String value)
	{
		m_sRemoteServerResponse = value;
	}
	/*
	 * 在下载并播放时发生的异常报告
	 */
	private String m_sPlayerException;
	public synchronized void setPlayerException(String value)
	{
		m_sPlayerException = value;
	}
	/*
	 * 通知下载数据的线程退出,
	 * 利用这种方式，使得后台线程和界面同步退出
	 */
	private boolean m_bStopDownloadData = false;
	public synchronized boolean getStopDownloadData()
	{
		return m_bStopDownloadData;
	}
	/*
	 * 在后台线程中New出来一个VideoControl，然后传出来到控制器上
	 * 这样，让对Video播放操作还在控制器中
	 */
	public synchronized void setVideoControl(VideoControl value)
	{
		m_video = value;
	}
	/////////////////////////////////////////////////////////////
	
	/** Nbr of keys */
	protected static final int	NBR_OF_KEYS			= 6;
	
	public GUIController(TrafficView midletMain){
		m_trafficcamMidlet = midletMain;
	}
	
	/**********************************************************
	//	 GUIController::init()
	//
	//	 Description:
	//	  初始化控制器：
	 * 		初始化各个Form界面
	 * 		设定先显示MainMenu列表界面
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.12
	//
	**********************************************************/
	public void init(){
		
		// Initialize the RMS persistence facade
	    RmsFacade.init(NBR_OF_KEYS);
		
		m_Settings = VideoSettings.getInstance();
		// init ui
		m_listMenu		=	new MainMenuList(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_T_APP)), this);
		setCurrent(m_listMenu);
		
		m_about			=	new About(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_T_ABOUT)));
		m_settingForm	=	new FormSetting(this);	
		m_camForm		=	new FormTrafficCam(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_T_STOPWEBCAM))
						,this);
	}


	/**********************************************************
	//	 GUIController::getSettings()
	//
	//	 Description:
	//	  返回当前封装了RMS操作的Settings的类实例
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.07
	//
	**********************************************************/
	public VideoSettings getSettings(){
		return m_Settings;
	}
	
	/**********************************************************
	//	 GUIController::setCurrent()
	//
	//	 Description:
	//	  设置当前显示的界面
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.07
	//
	**********************************************************/
	public void setCurrent(Displayable disp){
		m_trafficcamMidlet.setCurrent(disp);
    }
	public void setCurrent(Alert alert, Displayable disp){
		m_trafficcamMidlet.setCurrent(alert, disp);
    }
	
	/**********************************************************
	//	 GUIController::setProgress/setTitle
	//
	//	 Description:
	//	  设置FormProgress界面上的显示文字/标题的
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.07
	//
	**********************************************************/
	/*public void setProgress(String title, String message){
		m_progressForm.setProgress(title, message);
	}
	public void setTitle(String title){
		m_progressForm.setTitle(title);
	}*/
	
	/**********************************************************
	//	 GUIController::EventID
	//
	//	 Description:
	//	  定义事件ID内部类
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.07
	//
	**********************************************************/
    public static class EventID{
        private EventID(){
        }
        
        public static final byte EVENT_EXIT				= 0;//退出
		public static final byte EVENT_SHOWWEBCAM        = 1;//进入webcam界面
        public static final byte EVENT_STARTWEBCAM 		= 2;//TrafficCam界面的"启动"按钮
		public static final byte EVENT_STOPWEBCAM 		= 3;//TrafficCam界面的"停止"按钮
		public static final byte EVENT_WEBCAM_BACK 		= 4;//TrafficCam界面的"返回"按钮
        public static final byte EVENT_SETTINGS_SAVE		= 5;//设置界面的“保存”按钮
        public static final byte EVENT_SETTINGS_BACK	    = 6;//设置界面的“返回”按钮
        public static final byte EVENT_ABOUT				= 7;//About界面
		public static final byte EVENT_ABOUT_BACK		= 8;//About界面的“返回”按钮
		public static final byte EVENT_SETTINGS		    = 9;//进入设置界面
		public static final byte EVENT_DOWNLOADDATA		= 10;//说明当前开始下载数据
		public static final byte EVENT_DOWNLOADING	    = 11;//说明当前正在下载数据
		public static final byte EVENT_DOWNLOADDATA_ERROR    = 12;//说明当前正在下载数据时发生了错误
		public static final byte EVENT_DOWNLOADDATA_SUCC     = 13;//说明当前成功下载数据
		public static final byte EVENT_START_PLAY_3GP     = 14;//开始播放3gp媒体文件
		public static final byte EVENT_PLAYAGAIN    		 = 15;//重新播放刚才的3gp媒体文件
    }
	
    
	/**********************************************************
	//	 GUIController::handleEvent
	//
	//	 Description:
	//	  对传入的事件进行处理
	//
	//	 Parameters:
	//	 Return Values:
	//   Remark:
	 * 
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.07
	//
	**********************************************************/
	public void handleEventNoThrows( int eventID,Object[] args){   
		try{
			handleEvent(eventID, args);
		}
		catch(Exception exc){}
	}
    public void handleEvent( int eventID,Object[] args){   
		System.out.println("Controller::handleEvent eventID>>" + eventID);
		
    	switch (eventID)
        {   
    	    case EventID.EVENT_EXIT:
    	    {
				m_bStopDownloadData = true;
				// 清理RMS
				RmsFacade.shutdown();
				System.gc();     // 通知进行垃圾收集
				Thread.yield();  // 本线程暂停一下，使得GC可以马上获得机会运行
				m_trafficcamMidlet.exit(false);
    	    	break;
    	    }
    	    case EventID.EVENT_SHOWWEBCAM:
    	    {
				try
				{
					m_camForm.ShowWebcamDownload();
				}
				catch(Exception exc)
				{
				
				}
				setCurrent(m_camForm);
    	        break;
    	    }
			case EventID.EVENT_SETTINGS:
    	    {
				m_settingForm.initSettingsTextFields();
				setCurrent(m_settingForm);
    	        break;
    	    }
    	    case EventID.EVENT_STARTWEBCAM:
    	    {
				m_bStopDownloadData = false;
				m_camForm.StartWebcamDownload();
				//setCurrent(m_camForm);
    	    	break;
    	    }
			case EventID.EVENT_STOPWEBCAM:
    	    {
				m_bStopDownloadData = true;
				m_camForm.StopWebcamDownload();
				setCurrent(m_camForm);
    	    	break;
    	    }
			case EventID.EVENT_WEBCAM_BACK:
			{
				m_bStopDownloadData = true;
				// 必须先保证摄像头已经关闭的，而且不再抓取数据
				m_camForm.StopWebcamDownload();
			}
    	    case EventID.EVENT_ABOUT_BACK:
    	    case EventID.EVENT_SETTINGS_BACK: 
    	    {
    	    	setCurrent(m_listMenu);
    	    	break;
    	    }
    	    case EventID.EVENT_SETTINGS_SAVE:
    	    {
				/*
				 * 存储当前设置页面上的所有参数进入RMS
				 */
				String mmsurl = (String)args[0];
				String converturl = (String)args[1];
				String timelimit = (String)args[2];
				
    	        m_Settings.save(mmsurl,
						timelimit,
						converturl);
				setCurrent(m_settingForm);
				
    	        break;
    	    }
    	    case EventID.EVENT_ABOUT:
    	    {
    	        setCurrent(m_about);
    	        break;
    	    }
    	    case EventID.EVENT_DOWNLOADDATA:
			{
				/*
				 * 设置FormWebcam界面下方的文字，表明当前传输图像的总字节数:
				 */
				try{
					// 告知用户总字节数目:				
					String sProgress = String.valueOf(
							CommandResources.getChars(CommandResources.TXT_WAITTITLE));
					m_camForm.setProgress(sProgress 
								+ m_sPostDataTotalLength, "");
				}
				catch(Exception exc)
				{
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_WAITTITLE)), 
											exc.getMessage() + "/" + exc.getClass(), null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				break;
			}
			case EventID.EVENT_DOWNLOADING:
			{
				/*
				 * 设置FormWebcam界面下方的文字，表明当前传输图像的进度
				 * 告知用户剩余字节数目
				 */
				try{
					String sProgress = String.valueOf(
							CommandResources.getChars(CommandResources.TXT_WAITMESSAGE));
					String sProgressTitle = String.valueOf(
							CommandResources.getChars(CommandResources.TXT_WAITTITLE));
					m_camForm.setProgress(sProgressTitle + m_sPostDataTotalLength,
							sProgress + m_sPostingDataLength);
				}
				catch(Exception exc)
				{
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_WAITTITLE)), 
											exc.getMessage() + "/" + exc.getClass(), null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				break;
			}
			case EventID.EVENT_DOWNLOADDATA_ERROR:
			{
				try{
					m_camForm.StopWebcamDownload();
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_POSTERROR_TITLE)), 
											String.valueOf(
													CommandResources.getChars(CommandResources.TXT_POSTERROR_MSG))
													+ m_sPlayerException, null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				catch(Exception exc)
				{
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_WAITTITLE)), 
											exc.getMessage() + "/" + exc.getClass(), null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				break;
			}
			case EventID.EVENT_DOWNLOADDATA_SUCC:
			{
				try{
					m_camForm.StopWebcamDownload();
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_POSTSUCC_TITLE)), 
											String.valueOf(
													CommandResources.getChars(CommandResources.TXT_POSTSUCC_MSG))
													, null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				catch(Exception exc)
				{
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_WAITTITLE)), 
											exc.getMessage() + "/" + exc.getClass(), null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
				break;
			}
			case EventID.EVENT_PLAYAGAIN:
			{
				try
				{
					/*InputStream is = 
						getClass().getResourceAsStream
						(String.valueOf(
								CommandResources.getChars(CommandResources.TXT_DEMO_3GPFILE)));

					Player player = (Player)Manager.createPlayer(is, "video/3gpp");
					player.realize();
					System.out.println("3");
					m_video = (VideoControl)player.getControl("VideoControl");
					System.out.println("4");
					if(m_video != null)
			        {
						// the player can start with the smallest latency
						player.prefetch();
						
						System.out.println("/** 已经得到了VideoControl!");
					
						// Display Video as a Form item
						//
						Item itemVideoWindow;
						// 在USE_GUI_PRIMITIVE模式下，默认是可见的，而在USE_DIRECT_VIDEO模式下，
						// 默认是不可见的，需要通过方法setVisible(boolean visible)来设置
						(itemVideoWindow = (Item)m_video.initDisplayMode
							(VideoControl.USE_GUI_PRIMITIVE, null)).setLayout(3);
						m_camForm.append(itemVideoWindow);
						player.start();
						System.out.println("/** 启动Player!");
	
			        }
			        */
					m_camForm.PlayAgain();
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
					setCurrent(
							new Alert(
									String.valueOf(
											CommandResources.getChars(CommandResources.TXT_WAITTITLE)), 
											exc.getMessage() + "/" + exc.getClass(), null, AlertType.ERROR),
							(Displayable)m_camForm);
				}
			}
         	default:
         	    break;
        }
    }
    
}
