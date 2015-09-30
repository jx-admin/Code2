package java.util;

public class Timer {
	
	public Vector timertasklist = new Vector();
	
	//startOnce
	public void schedule(TimerTask task, long delay) 
	{
		synchronized (this)
		{
			task.owner = this;
			if(!timertasklist.contains(task))
				timertasklist.addElement(task);
		}
	}
	
	//startPeriodFixDelay
	public void schedule(TimerTask task, long delay, long period)
	{
		synchronized (this)
		{
			task.owner = this;
			if(!timertasklist.contains(task))
				timertasklist.addElement(task);	
		}
	}
	
	//startPeriodFixRate
	public void scheduleAtFixedRate(TimerTask task, long delay, long period)
	{
		synchronized (this)
		{
			task.owner = this;
			if(!timertasklist.contains(task))
				timertasklist.addElement(task);		
		}
	}
	
	public void cancel()
	{
		synchronized (this)
		{
			while(timertasklist.size() >0)
			{
				TimerTask task = (TimerTask)timertasklist.elementAt(0);
				task.cancel();
				timertasklist.removeElementAt(0);
			}
		}
	}
 
	}
     
