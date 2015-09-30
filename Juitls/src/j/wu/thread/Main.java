package j.wu.thread;

import j.wu.utils.Log;

public class Main {
	public static final String TAG = Main.class.getSimpleName();
	public static void main(String[]args){
//		ThreadFlagFor.test();
		ThreadFlag.test();
	}
}

class ThreadFlag extends Thread{
	public static final String TAG = ThreadFlag.class.getSimpleName();
	public volatile boolean exit=false;
	
	public static void test(){
		ThreadFlag mThreadFlag=new ThreadFlag();
		mThreadFlag.interrupt();
//		try{
//			Thread.sleep(1000);
//		}catch(InterruptedException e){
//			Log.d(TAG,e.getMessage());
//		}catch(Exception e){
//			Log.d(TAG,e.getMessage());
//		}
//		mThreadFlag.stop();
		mThreadFlag.start();
	}
	
	@Override
	public void interrupt() {
		exit=true;
		// TODO Auto-generated method stub
		super.interrupt();
	}
	
	public void run(){
		Log.d(TAG,"start");
		while(!exit)
//			;
		{
			try{
				Thread.sleep(1000);
				Log.d(TAG, ".");
			}catch(InterruptedException e){
				Log.d(TAG,e.getMessage());
			}catch(Exception e){
				Log.d(TAG,e.getMessage());
			}
		}
		Log.d(TAG, "end");
	}
}
class ThreadFlagFor extends Thread{
	public static final String TAG = ThreadFlagFor.class.getSimpleName();
	public volatile boolean exit=false;
	
	public static void test(){
		ThreadFlagFor mThreadFlag=new ThreadFlagFor();
		mThreadFlag.start();
		try{
			Thread.sleep(4000);
		}catch(InterruptedException e){
			Log.d(TAG,e.getMessage());
		}catch(Exception e){
			Log.d(TAG,e.getMessage());
		}
		mThreadFlag.interrupt();
//		mThreadFlag.stop();
	}
	
	public void run(){
		Log.d(TAG,"start");
		int i=0;
		while(!isInterrupted()){
			i++;
			if(i==Integer.MAX_VALUE/4){
				Log.d(TAG, ".");
			}
		}
		Log.d(TAG, "end");
	}
}


class ThreadIO extends Thread{
	public static final String TAG = ThreadIO.class.getSimpleName();
	public volatile boolean exit=false;
	
	public static void test(){
		ThreadFlagFor mThreadFlag=new ThreadFlagFor();
		mThreadFlag.start();
		try{
			Thread.sleep(4000);
		}catch(InterruptedException e){
			Log.d(TAG,e.getMessage());
		}catch(Exception e){
			Log.d(TAG,e.getMessage());
		}
		mThreadFlag.interrupt();
//		mThreadFlag.stop();
	}
	
	public void run(){
		Log.d(TAG,"start");
		int i=0;
		while(!isInterrupted()){
			i++;
			if(i==Integer.MAX_VALUE/4){
				Log.d(TAG, ".");
			}
		}
		Log.d(TAG, "end");
	}
}