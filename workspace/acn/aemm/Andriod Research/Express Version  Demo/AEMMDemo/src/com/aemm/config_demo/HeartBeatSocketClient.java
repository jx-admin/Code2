package com.aemm.config_demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Message;

import com.android.accenture.aemm.dome.Logger;
import com.android.accenture.aemm.dome.LoggerFactory;

public class HeartBeatSocketClient extends Thread{
	static int iid=0;
	int id=0;
	private static int heartTimes=0;
	private int heartDelay=10000;
	private long receveTime;
	private boolean isRun;
	private Socket socket;
	private InetSocketAddress address;
	private BufferedWriter bw;
	private BufferedReader br;
	private Handler mHandler;
	private Logger logger;
	
	public HeartBeatSocketClient(Handler mHandler,String host,int port) {
		logger=LoggerFactory.getLogger(this.getClass());
		logger.i("cretae HeartBeatSocketClient->"+toString());
		this.mHandler=mHandler;
		address = new InetSocketAddress(host, port);
	}
	
	class HeartBeatThread extends Thread{
		long lastTime;
		public void run(){
			logger.i(id+" HeartBeat to run");
			bw=null;
			while(isRun){
				lastTime=System.currentTimeMillis();
				sendMessage("Q"+(heartTimes++)+":heart"+"\n");
				try {
					Thread.sleep(heartDelay);
					logger.i(id+" HeartBeat to sleep:"+heartDelay);
				} catch (InterruptedException e) {
					logger.e(id+" HeartBeat to sleep erro:"+e.getMessage());
				}
				logger.i("HeartBeat send time cost:"+(receveTime-lastTime));  
				if(receveTime<lastTime){
					logger.e("HeartBeat receve timeout");
					sentError();
					break;
				}
			}
			close();
		}
	}
	
	void sendMessage(String msg){
		try {
			if(bw==null){
				logger.i(id+" to create bufferedWriter");
				bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			}
			bw.write(msg);
			bw.flush();
			logger.i(id+" sent:" +msg);
		}catch(Exception e){
			logger.e(msg+" sent error",e);
			sentError();
	    }
	}
	private void sentError(){
		isRun=false;
		if(mHandler!=null){
			Message msg = mHandler
			.obtainMessage(ListenerService.READ_OUTTIME);
			mHandler.sendMessage(msg);
			logger.i("send error to handler");  
		}
	}
	public void run() {
		id=iid++;
		logger.i("Threed to run...");
		isRun = true;
		String pushCommand = null;
		do {
			try {
				close();
				socket = new Socket();
				logger.i(id+" Socket to connhost : " + address.toString()+" timeout is:"+30000);
				socket.connect(address,30000);
				// mSocket.setSoTimeout(60000);
	
				logger.i(id+" get input stream from socket...");
				br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
				bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				new HeartBeatThread().start();
	
				while (isRun) {
					logger.i(id+" waiting command...");
					pushCommand = br.readLine();
					logger.i(id+" receve:" + pushCommand);
					receveTime=System.currentTimeMillis();
					if(pushCommand == null){
						break;
					}
					logger.i(" will push command to handler "+pushCommand+" "+pushCommand.indexOf('Q'));
					if(pushCommand.indexOf('Q')!=0){
						if(mHandler!=null){
							logger.i(" push command to handler "+pushCommand);
							Message msg = mHandler.obtainMessage(ListenerService.TIMER_MESSAGE);
							msg.obj = pushCommand.substring(pushCommand.lastIndexOf(':')+1);
							mHandler.sendMessage(msg);
						}else{
							sentError();
						}
					}else{
						sendMessage('R'+pushCommand.substring(1)+id+'\n');
					}
				}
			} catch (SocketTimeoutException e) {
				logger.e(id+" socket connect TIME_OUT", e);
			} catch (Exception e) {
				logger.e(id+" socket connect Exception:", e);
			} 
			
			close();
			
			try {
				logger.i(id+" to sleep 1000");
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (isRun);
		close();
		logger.close();
	}
	
	public void onStop(){
		isRun=false;
	}
	
	public int close() {
		logger.i(id+" socke to close this = " + this.toString());
		try {
			if(socket != null&&socket.isConnected()){
				socket.close();
				socket = null;
			}
			if(bw!=null){
				bw.close();
				bw=null;
			}
			if(br!=null){
				br.close();
				br=null;
			}
		} catch (Exception e) {
			logger.e(id+" close :", e);
			return 0;
		}
		return 1;
	}
}
