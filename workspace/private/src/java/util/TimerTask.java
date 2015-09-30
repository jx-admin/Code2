package java.util;

public abstract class TimerTask {
	
	public Timer owner;
	
	public abstract void run();
	
	public boolean cancel()
	{
		if(owner!=null)
		{
			owner.timertasklist.removeElement(this);
			owner = null;
		}
		return true;
	}
	}
