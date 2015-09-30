package j.wu.thread;

public class Thread_ {
	private int x;
	class Runner implements Runnable{

		@Override
		public void run() {
			int current=0;
			for(int i=0;i<4;i++){
				current=x;
				System.out.print(current+",");
				x+=2;
			}
			
		}
		
	}
	public static void main(String[]args){
		new Thread_().go();
	}
	
	public void go(){
		Runnable r=new Runner();
		new Thread(r).start();
		new Thread(r).start();
		
	}

}
