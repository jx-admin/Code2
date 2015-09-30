/*////////////////////////////////////////////////////////////////////
//文档生成日期：2005.10.12
//
//(1)概述：
//类名称：FormWebcam
//类说明：
//     MVC中的View部分，负责启动/停止自动拍照和上传照片的主力Form。
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
package com.ultrapower.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
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
import com.ultrapower.model.ThreadGetVideo;
import com.ultrapower.model.VideoSettings;

/**********************************************************
//FormWebcam
//
//Class Description:
//	MVC中的View部分，负责启动/停止自动拍照和上传照片的主力Form。
//
//Author:
//zhengyun@ultrapower 2005.10.12
//
**********************************************************/
public class FormTrafficCam extends Form
{
	private GUIController 	 controller;
    private static final Command m_cmdBack = new Command("BackMainMenu", 3, 0);
    private static final Command m_cmdStart = new Command("Start", "Download&Play", 4, 1);
    private static final Command m_cmdStop = new Command("Stop", "StopDownload&Play", 6, 2);
	private static final Command m_cmdPlayAgain
		= new Command("Replay", "ReplayCurrent", 6, 2);
	
    private ThreadGetVideo m_threadStreaming;
    public static FormTrafficCam m_formWebcam;
	private WaitFlash 		 m_WaitFlash;
	private Display     	 m_display;          // The display for this MIDlet
	
	// 保留当前Tips的提示资源的ID号:
	private int m_nItemTipsAppendId = -1;

    public FormTrafficCam(String title, GUIController control)
    {
        super(title);
		controller = control;

        addCommand(m_cmdBack);
        addCommand(m_cmdStart);
		//addCommand(m_cmdPlayDemo);
        setCommandListener(new FormTrafficCamListener());
		m_formWebcam = this;
		
		m_display = controller.m_trafficcamMidlet.getCurrentDisplay();
    }

	/*
     * 内部监听器，监听器监听所有Command事件，并把事件响应推出来让控制器处理
     */
    private class FormTrafficCamListener implements CommandListener{ 
		public void commandAction(Command command, Displayable disp){
			if(command == m_cmdBack){
				controller.handleEvent(GUIController.EventID.EVENT_WEBCAM_BACK, null);
	        }
			else if(command == m_cmdStart){
				controller.handleEvent(GUIController.EventID.EVENT_STARTWEBCAM, null);    	                    	            
			}
			else if(command == m_cmdStop){
				controller.handleEvent(GUIController.EventID.EVENT_STOPWEBCAM, null);    	                    	            
			}
			else if(command == m_cmdPlayAgain){
				controller.handleEvent(GUIController.EventID.EVENT_PLAYAGAIN, null);    	                    	            
			}
			//end else
		}
    }//end inner class

    public final void StopWebcamDownload()
    {
		System.out.println("/** Enter StopWebcamDownload!");
		m_threadStreaming.setDownloadVideo(false);
		m_threadStreaming.StopPlayer();
        removeCommand(m_cmdStop);
        addCommand(m_cmdPlayAgain);
        setTitle(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_T_STOPWEBCAM)));
    }
	
	public final void ShowWebcamDownload()
    {		
		System.out.println("/** Enter ShowWebcamDownload!");
		removeCommand(m_cmdStop);
	    addCommand(m_cmdStart);
		m_threadStreaming = new ThreadGetVideo(controller);
	    Thread thread;
	    (thread = new Thread(m_threadStreaming)).start();
	    setTitle(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_T_STOPWEBCAM)));
    }
	
    public final void StartWebcamDownload()
    {	
		System.out.println("/** Enter StartWebcamDownload!");
		
        removeCommand(m_cmdPlayAgain);
        addCommand(m_cmdStop);
        setTitle(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_T_STARTWEBCAM)));
		
		/*if(m_WaitFlash == null)
		{
			// 当前没有等候动画时,我们需要创建一个
			m_WaitFlash = new WaitFlash(controller);
			m_WaitFlash.setMessage("下载中",
					"正在下载媒体文件，请稍等...");
			m_display.setCurrent(m_WaitFlash);			
		}
		else
		{
			m_display.setCurrent(m_WaitFlash);
		}
		System.out.println("设置了等待画面!");
		*/
		
		m_threadStreaming.DeleteVideoControl();
		m_threadStreaming.setDownloadVideo(true);		
    }
	
	public void PlayAgain()
	{
		removeCommand(m_cmdPlayAgain);
        addCommand(m_cmdStop);
		m_threadStreaming.PlayAgain();
	}
	
	public final synchronized void setProgress(
			String strProgressTitle,
			String strMessage)
    {
		if(m_nItemTipsAppendId < 0)
		{
			m_nItemTipsAppendId = append(new StringItem(strProgressTitle, strMessage));
		}
		else
		{
			set(m_nItemTipsAppendId, new StringItem(strProgressTitle, strMessage));
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
	
	/* Nokia S60 Exception
	 * Exception in thread "JMF thread: com.sun.media.ProcessEngine@11d2572[ com.sun.media.ProcessEngine@11d2572 ] 
	 * ( configureThread)" java.lang.Error: Invalid media file: File Type Box not found
	at com.nokia.phone.sdk.concept.util.mmedia.video.v3gpp.ISOMedia.readStream(ISOMedia.java)
	at com.nokia.phone.sdk.concept.util.mmedia.video.v3gpp.ISOMedia.<init>(ISOMedia.java)
	at com.nokia.phone.sdk.concept.util.mmedia.video.v3gpp.ThreeGPDeMux.getTracks(ThreeGPDeMux.java)
	at com.sun.media.BasicSourceModule.doRealize(BasicSourceModule.java:180)
	 */
}


/*
IOException: SymbianOS error = -28
?

*/