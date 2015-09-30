/**
//文档生成日期：2005.10.12
//
//(1)概述：
//类名称： WaitFlash
//类说明：
//	 “动画等待画面”的Canvas界面
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
//	创建人: 张文杰
//	修改者: 郑昀(2005.10.12)
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

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.ultrapower.common.CommandResources;
import com.ultrapower.control.GUIController;

/*
 * Title: WaitingWebService
 * Description:此类可用于长时间操作时的等待画面
 * Copyright: Copyright (c) 2005
 * Company: ultrapower
 * 
 * @author 张文杰
 * @version 1.0
 */
public class WaitFlash extends Canvas {

	private static WaitFlash m_flash;
	/**
     * Singleton pattern is used to return 
     * only one instance of VideoSettings
     */
    public static synchronized WaitFlash getInstance(GUIController controller){
        if( m_flash == null ) {
			System.out.println("!-- WaitFlash::getInstance --!\n");
			m_flash = new WaitFlash(controller);
        }
		return m_flash;
    }
	
	private int mCount, mMaxinum;

	private int mInterval;

	private int mWidth, mHeight, mx, my, mRadius;

	private String m_sTitle = "";
	private String m_sMsg = "";
	
	private GUIController m_controller;
	
	private static final Command m_cmdBack = 
		new Command(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_C_BACK)), 3, 0);

	protected void paint(Graphics g) {
		int theta = -(mCount * 360 / mMaxinum);

		///////////////
		// 以下setColor我们是要定义动画的背景色彩		
		//g.setColor(255, 255, 255); // 这是白色
		//g.setColor(8,65,99); // 这是深蓝色
		g.setColor(222,235,198); // 这是浅绿色
		g.fillRect(0, 0, mWidth, mHeight);
		///////////////

		///////////////
		// 以下setColor我们是要定义动画中圆形物体的边缘色彩
		//g.setColor(0x00ff0000); // 这是红色
		g.setColor(173,195,41); // 这是绿色
		///////////////		
		g.drawArc(mx, my, mRadius, mRadius, 0, 360);
		
		///////////////
		// 以下setColor我们是要定义动画中圆形物体的填充色彩
		//g.setColor(255,154,49);// 橙色
		g.setColor(115,166,49);// 深绿色
		///////////////
		g.fillArc(mx, my, mRadius, mRadius, theta + 90, 90);
		g.fillArc(mx, my, mRadius, mRadius, theta + 270, 90);

		/*
		 * 这里写的是提示信息的标题
		 */
		if (m_sTitle != null) {
			g.drawString(m_sTitle, mWidth / 2, my+mRadius+20, Graphics.BOTTOM
					| Graphics.HCENTER);
		}
		
		/*
		 * 这里写的是提示信息的详细内容
		 */
		if (m_sMsg != null) {
			g.drawString(m_sMsg, mWidth / 2, my+mRadius+30, Graphics.BOTTOM
					| Graphics.HCENTER);
		}
	}

	/*
     * 内部监听器，监听器监听所有Command事件，并把事件响应推出来让控制器处理
     */
    private class FormPostProgressListener implements CommandListener{ 
		public void commandAction(Command command, Displayable disp){
			if(command == m_cmdBack){
				m_controller.handleEvent(GUIController.EventID.EVENT_STOPWEBCAM, null);
	        }
		}
    }
	
	public WaitFlash(GUIController control) {
		m_controller = control;
		this.setCommandListener(new FormPostProgressListener());
        
		if(m_cmdBack != null)
			addCommand(m_cmdBack);
		
		mCount = 0;
		mMaxinum = 36;
		mInterval = 50;
		mWidth = getWidth();
		mHeight = getHeight();

		int halfWidth = (mWidth - mRadius) / 2;
		int halfHeight = (mHeight - mRadius) / 2;
		mRadius = Math.min(halfWidth, halfWidth)-10;

		mx = halfWidth - mRadius / 2;
		my = halfHeight - mRadius / 2;

		TimerTask task = new TimerTask() {
			public void run() {
				mCount = (mCount + 1) % mMaxinum;
				repaint();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 0, mInterval);
		
		m_flash = this;
	}

	public final void setMessage(String title, String message) {
		//System.out.println("Enter setMessage>>" + title + "/" + message);
		m_sTitle = title;
		m_sMsg = message;
		repaint();
	}
	
	public final void setTitle(String title) {
		//System.out.println("Enter setTitle>>" + title);
		m_sTitle = title;
		repaint();
	}
}
