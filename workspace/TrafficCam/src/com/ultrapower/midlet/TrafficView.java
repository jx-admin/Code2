/**
//VideoIM文档生成日期：2005.10.12
//
//(1)概述：
//类名称：VideoCoolala
//类说明：
//	提供主界面
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

//(5)发现一个问题:
如果VideoIM.jad的版本设置的不合适，将会导致手机下载安装时提示“文件已损坏”!
只有设置为比较正常的比如“1.2.2”这种，才被手机下载了。

////////////////////////////////////////////////////////////////////*/

package com.ultrapower.midlet;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.ultrapower.control.GUIController;


/**********************************************************
//VideoCoolala
//
//Class Description:
//	实际上应该算作MVC中的View部分，是MIDlet的主界面。
//Author: 
//     zhengyun@ultrapower 2005.10.12
//
**********************************************************/
public class TrafficView extends MIDlet{
	private Display display;
	private static GUIController controller;
	
	/**
	 * default constructor
	 */
	public TrafficView() {
		super();
		display = Display.getDisplay(this);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() 
	throws MIDletStateChangeException{
		controller = new GUIController(this);
		try
		{
			controller.init();//初始化RMS,Menu,Forms
		}
		catch(Exception exc)
		{
			setCurrent(
				new Alert(
						"初始化错误", 
						"错误原因为:"
								+ exc.getMessage() + "/" + exc.getClass(),
								null, AlertType.ERROR));
		}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		this.notifyPaused();
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		controller = null;		
	}
	
	/**********************************************************
	//	 VideoCoolala::setCurrent()
	//
	//	 Description:
	//	  设置当前显示的界面
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.12
	//
	**********************************************************/
	public void setCurrent(Displayable disp){
		display.setCurrent(disp);
	}	
	public void setCurrent(Alert alert, Displayable disp){
		display.setCurrent(alert, disp);
    }
	
	public Displayable getCurrent(){
		return display.getCurrent();
    }
	
	/**********************************************************
	//	 VideoCoolala::getCurrentDisplay()
	//
	//	 Description:
	//	  获取当前的Display，这样可以让其他类能够控制要显示什么
	//
	//	 Parameters:
	//	 Return Values:
	//	 Author:
	//	      zhengyun@ultrapower 2005.10.12
	//
	**********************************************************/
	public Display getCurrentDisplay(){
		return display;
    }
	
	public void exit(boolean arg0){
		try{
			destroyApp(arg0);
			notifyDestroyed();
		}catch(MIDletStateChangeException e){
			//
		}
	}
}
