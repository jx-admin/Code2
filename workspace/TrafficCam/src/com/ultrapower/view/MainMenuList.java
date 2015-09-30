/**
//文档生成日期：2005.10.07
//
//(1)概述：
//类名称：MainMenuList
//类说明：
//提供菜单命令有：
 *   我要MobileWebCam
 *   设置MobileWebCam
 *   关于MobileWebCam
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
			3:关于MobileWebCam
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

import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

import com.ultrapower.common.CommandResources;
import com.ultrapower.control.GUIController;

/**********************************************************
//MainMenuList
//
//Class Description:
//	提供菜单命令有：
 *   我要MobileWebCam
 *   设置MobileWebCam
 *   关于MobileWebCam
//
//Author:
//zhengyun@ultrapower 2005.10.12
//
**********************************************************/
public class MainMenuList extends javax.microedition.lcdui.List{
    private GUIController 	 controller;
	public static MainMenuList currentList = null;
	
	private Command commandOK;
	private static Image m_imgCommand = CommandResources.getImage(
			CommandResources.IMG_COMMAND);
	
	private static final boolean m_bSelectCommands[] = {
        false, false, false, false
    };
	
	private Vector items;
		
    public MainMenuList(String title, GUIController control){
    	super(title, List.IMPLICIT);
    	
    	controller = control;
    	this.setCommandListener(new MainMenuListListener());
    	
		this.append(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_I_WANTWEBCAM))
						, m_imgCommand);
		this.append(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_I_SETTINGS))
				, m_imgCommand);
		this.append(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_I_ABOUT))
				, m_imgCommand);
		this.append(String.valueOf(CommandResources.getChars(
				CommandResources.TXT_I_EXIT))
				, m_imgCommand);
		
        commandOK = new Command(
				String.valueOf(CommandResources.getChars(
						CommandResources.TXT_C_OK)), 8, 10);
        addCommand(commandOK);
        setCommandListener(new MainMenuListListener());
        setSelectedFlags(m_bSelectCommands);
        setSelectCommand(null);
        setFitPolicy(0);
		currentList = this;
    }
    
	/*
     * 内部监听器，监听器监听所有Command事件，并把事件响应推出来让控制器处理
     */
    private class MainMenuListListener implements CommandListener{ 
    	public void commandAction(Command command, Displayable disp){
    			if(command == commandOK || (command == List.SELECT_COMMAND)){
					
					System.out.println("Enter MainMenuListListener");
    	            int ind = ((List)disp).getSelectedIndex();
    	            switch(ind)
    	            {
					case 0:
						controller.handleEvent(GUIController.EventID.EVENT_SHOWWEBCAM,null);
						break;
					case 1:
						System.out.println("EVENT_SETTINGS");
						controller.handleEvent(GUIController.EventID.EVENT_SETTINGS,null);
						break;
					case 2:
						controller.handleEvent(GUIController.EventID.EVENT_ABOUT,null);
						break;
					case 3:
						controller.handleEvent(GUIController.EventID.EVENT_EXIT,null);
						break;
					default:
						return;
    	            }
    			}
    	}
    }
}
