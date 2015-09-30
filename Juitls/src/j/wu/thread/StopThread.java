package j.wu.thread;

public class StopThread {
	
	public static void main(String[]args){
		
		InterupThread d=new InterupThread();
		d.start();
		System.out.println(" thread is Interrupted = "+d.isInterrupted());
		d.interrupt();
//		d.stop();
		System.out.println(" thread is Interrupted = "+d.isInterrupted());
	}
	
	static class InterupThread extends Thread{
		int i;
		public void run(){
			while(true){
				i++;
				System.out.println("do "+i);
				try{
					Thread.sleep(1000);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
