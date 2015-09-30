import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestSingleThreadExecutor {
	    public static void main(String[] args) {
	        //创建一个可重用固定线程数的线程池
	        ExecutorService pool = Executors. newSingleThreadExecutor();

	        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
	        Thread t1 = new MyThread("1");
	        Thread t2 = new MyThread("2");
	        Thread t3 = new MyThread("3");
	        Thread t4 = new MyThread("4");
	        Thread t5 = new MyThread("5");
	        //将线程放入池中进行执行
	        pool.execute(t1);
	        t1.interrupt();
	        pool.execute(t2);
	        t2.interrupt();
	        pool.execute(t3);
	        t3.interrupt();
	        pool.execute(t4);
	        pool.execute(t5);
	        //关闭线程池
	        pool.shutdown();
	    }
	}
class MyThread extends Thread {
	private String name;
	public MyThread(String name){
		this.name=name;
	}
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "正在执行。。。"+name);
        try{
        	Thread.sleep(1000);
        }catch(Exception e){
        	e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束。。。"+name);
    }
}

