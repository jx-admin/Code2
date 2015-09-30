package com.act.sctc.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;

import com.act.sctc.R;
import com.act.sctc.db.PhoneColumn;
import com.act.sctc.util.Logger;
import com.custom.view.utils.BaseManager;

public class PhoneParameterManager extends BaseManager {
	private int phoneId;
	private PhoneParameterItemView entertainment_function, transfer_function, personal_assistant, main_body, display,
			dv, other, network;

	public PhoneParameterManager(View layout) {
		super(layout);
		iniController();
	}

	private final static String[] projection = new String[] { PhoneColumn.series, PhoneColumn.brand, PhoneColumn.type,
			PhoneColumn.start_time, PhoneColumn.look_design, PhoneColumn.os, PhoneColumn.smartphone,
			PhoneColumn.cpu_core, PhoneColumn.cpu_rate, PhoneColumn.keyborad, PhoneColumn.input, PhoneColumn.op_sign,
			PhoneColumn.net_standard, PhoneColumn.net_rate, PhoneColumn.browser, PhoneColumn.device_mem,
			PhoneColumn.run_mem, PhoneColumn.card_mem, PhoneColumn.extend_mem, PhoneColumn.screen_szie,
			PhoneColumn.screen_color, PhoneColumn.screen_resolution, PhoneColumn.gravity, PhoneColumn.touch,
			PhoneColumn.music, PhoneColumn.video, PhoneColumn.ebook, PhoneColumn.camera, PhoneColumn.sensor,
			PhoneColumn.video_maker, PhoneColumn.photo_mode, PhoneColumn.continue_photo, PhoneColumn.resolution_photo,
			PhoneColumn.camera_other, PhoneColumn.sub_camera, PhoneColumn.auto_focus, PhoneColumn.gps,
			PhoneColumn.wifi, PhoneColumn.bluetooth, PhoneColumn.office, PhoneColumn.email, PhoneColumn.calculator,
			PhoneColumn.device_size, PhoneColumn.device_quality, PhoneColumn.device_matierial,
			PhoneColumn.battery_category, PhoneColumn.battery_capacity, PhoneColumn.speek_time, PhoneColumn.idle_time,
			PhoneColumn.headset };

	public void setPhoneId(int id) {
		phoneId = id;
		new UpdateTask().execute();
	}

	private class UpdateTask extends AsyncTask<Object, Object, Object> {

		protected Object doInBackground(Object... params) {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			try {
				Thread.sleep(700);
			} catch (Exception e) {
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			Cursor phonePramsCursor = context.getContentResolver().query(PhoneColumn.CONTENT_URI, projection,
					"_id=" + phoneId, null, null);
			if (phonePramsCursor != null) {
				if (phonePramsCursor.moveToFirst()) {
					int size = phonePramsCursor.getColumnCount();
					StringBuilder sb = new StringBuilder(1024);
					for (int i = 0, g = 0, j = 0; i < size; j++, i++) {
						if (j >= tiles[g].length) {
							gropView[g].setContext(sb.toString());
							sb.setLength(0);
							g++;
							if (g >= gropView.length) {
								break;
							}
							j = 0;
							gropView[g].setTitle(groupTitles[g]);
						}
						String string = phonePramsCursor.getString(i);
						if (string == null) {
							string = "未知";
						}
						if (j > 0) {
							sb.append('\n');
						}
						sb.append(tiles[g][j] + "：" + string);
					}
				}
				phonePramsCursor.close();
			}
		}
	}

	private PhoneParameterItemView[] gropView;
	private final static String[] groupTitles = new String[] { "主体", "网络", "存储", "显示", "娱乐功能", "摄像功能", "GPS模块", "个人助理",
			"其他" };
	private final static String[][] tiles = new String[][] {
			{ "系列", "品牌", "型号", "上市时间", "外观设计", "操作系统", "智能机", "CPU核数", "CPU频率", "键盘类型", "输入方式", "运营商标志内容" },
			{ "网络制式", "网络频率", "浏览器" }, { "机身内存", "运行内存", "存储卡类型", "最大存储扩展" }, { "屏幕尺寸", "屏幕色彩", "分辨率", "重力感应", "触摸屏" },
			{ "音乐播放", "视频播放", "电子书" }, { "摄像头", "传感器类型", "视频拍摄", "拍摄模式", "连拍功能", "照片分辨率", "其他性能", "副摄像头", "自动对焦" },
			{ "GPS模块", "Wi-Fi", "蓝牙" }, { "Office", "电子邮件", "计算器" },
			{ "机身尺寸", "机身重量", "机身材质", "电池类型", "电池容量", "理论通话时间", "理论待机时间", "耳机" } };

	private void iniController() {
		gropView = new PhoneParameterItemView[] { (PhoneParameterItemView) layout.findViewById(R.id.main_body),
				(PhoneParameterItemView) layout.findViewById(R.id.network),
				(PhoneParameterItemView) layout.findViewById(R.id.memery),
				(PhoneParameterItemView) layout.findViewById(R.id.display),
				(PhoneParameterItemView) layout.findViewById(R.id.entertainment_function),
				(PhoneParameterItemView) layout.findViewById(R.id.dv),
				(PhoneParameterItemView) layout.findViewById(R.id.transfer_function),
				(PhoneParameterItemView) layout.findViewById(R.id.personal_assistant),
				(PhoneParameterItemView) layout.findViewById(R.id.other) };
		// entertainment_function=(PhoneParameterItemView)
		// layout.findViewById(R.id.entertainment_function);
		// entertainment_function.addString("音乐播放：支持");
		// entertainment_function.addString("视频播放：支持");
		//
		// transfer_function=(PhoneParameterItemView)
		// layout.findViewById(R.id.transfer_function);
		// transfer_function.addString("GPS模块（硬件）：支持");
		// transfer_function.addString("WIfI：支持");
		// transfer_function.addString("蓝牙：支持");
		//
		// personal_assistant=(PhoneParameterItemView)
		// layout.findViewById(R.id.personal_assistant);
		// personal_assistant.addString("Office：支持");
		// personal_assistant.addString("电子邮箱：支持");
		// personal_assistant.addString("计算器：支持");
		// personal_assistant.addString("闹钟：支持");
		//
		// main_body=(PhoneParameterItemView)
		// layout.findViewById(R.id.main_body);
		// main_body.addString("品牌：三星（SAMSUNG）");
		// main_body.addString("型号：GALAXY NOTEE3");
		// main_body.addString("颜色：白色 黑色");
		// main_body.addString("上市时间：2013年");
		// main_body.addString("外观设计：直板");
		// main_body.addString("3G视频通话：支持");
		// main_body.addString("操作系统：ANDROID4.3");
		// main_body.addString("智能机：是");
		// main_body.addString("CPU核数：4核");
		// main_body.addString("CPU频率：2.3GHZ");
		// main_body.addString("键盘类型：虚拟QWERTY键盘");
		// main_body.addString("输入方式：触控");
		// main_body.addString("运营商标志内容：带外包装、带机身、带开机画面、带内置应用");
		//
		// display=(PhoneParameterItemView) layout.findViewById(R.id.display);
		// display=(PhoneParameterItemView) layout.findViewById(R.id.display);
		// display.addString("屏幕尺寸：5.7英寸");
		// display.addString("屏幕材质：superAMOLED");
		// display.addString("分辨率：1920X1080");
		//
		// dv=(PhoneParameterItemView) layout.findViewById(R.id.dv);
		// dv.addString("摄像头：1300像素");
		// dv.addString("副摄像头：200像素");
		//
		// other=(PhoneParameterItemView) layout.findViewById(R.id.other);
		// other.addString("NFC(近场通讯）：支持");
		// other.addString("电池容量：3200mah");
		// other.addString("机身尺寸：151.2X79.2X8.3mm");
		//
		// network=(PhoneParameterItemView) layout.findViewById(R.id.network);
		// network.addString("....");
	}

	private void iniListener() {

	}

	private void iniVariable() {

	}

}
