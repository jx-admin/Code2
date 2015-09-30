package cn.sdcet.tpod.chen.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.sdcet.tpod.chen.download.Service.DownloadFileService.CallBack;

public class DownloadFile extends Thread{

	String strDownLoadUrl = null;
	String strPathName = null;
	int position = 0;
	CallBack callBack = null;
	
	public DownloadFile(String strDownLoadUrl,String strPathName,int position,CallBack callBack){
		this.strDownLoadUrl = strDownLoadUrl;
		this.strPathName = strPathName;
		this.position = position;
		this.callBack = callBack;
	}
	

	public void run() {

		int count;

		int index = strDownLoadUrl.lastIndexOf("/");
		System.out.println("转换之前路径"+strDownLoadUrl);
		strDownLoadUrl = strDownLoadUrl.substring(0, index)
				+ "/"
				+ java.net.URLEncoder.encode(strDownLoadUrl
						.substring(index + 1));
		// strDownLoadUrl = java.net.URLEncoder.encode(strDownLoadUrl);
		strDownLoadUrl = strDownLoadUrl.replace("+", "%20");
		System.out.println("换换之后路径：" + strDownLoadUrl);

		try {
			URL url = new URL(strDownLoadUrl);
			URLConnection conexion = url.openConnection();
			conexion.connect();
//			String SDPATH = Environment.getExternalStorageDirectory() + "/";
//			System.out.println("sd卡的位置：" + SDPATH);
//			File file = new File(SDPATH + strPathName);
			
			File file = new File(strPathName.substring(0,strPathName.lastIndexOf("/")));
			if(!file.exists()){
				file.mkdirs();
			}
			
			file = new File(strPathName);
			if (!file.exists()) {
				file.createNewFile();
			}

			InputStream input = new BufferedInputStream(url.openStream());
//			OutputStream output = new FileOutputStream(SDPATH + strPathName);
			OutputStream output = new FileOutputStream(strPathName);
			// OutputStream output = new
			// FileOutputStream("/sdcard/picture.jpg");

			int lengthOfFile = conexion.getContentLength();
			byte data[] = new byte[1024];
			int total = 0;
			int progress = 0;
			int lastProgress = 0;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
				total += count;

				progress = (int) (((total * 1.0) / lengthOfFile) * 100);
//				System.out.println("已完成" + progress + "%");
				if(lastProgress+5<=progress){
					callBack.execute(position, progress);
					lastProgress = progress;
					
				}else if(progress==100){
					callBack.execute(position, progress);
					
				}else{
//					System.out.println("跳过去");
					
				}
				
				
			}

			output.flush();
			output.close();
			input.close();
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage().toString());
			callBack.execute(position, -1);
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			callBack.execute(position, -1);
			return;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			callBack.execute(position, -1);
			e.printStackTrace();
			return;
		}catch(Exception e){
			callBack.execute(position, -1);
			e.printStackTrace();
			return;
		}
		callBack.execute(position, 150);

	}
}
