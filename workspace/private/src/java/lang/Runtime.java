package java.lang;

public class Runtime
{
	private static Runtime runtime;

	private Runtime()
	{ }

	public static Runtime getRuntime()
	{
		return null;
	}

	public void exit(int status)
	{ }

	public void gc()
	{ }

	public long totalMemory()
	{
		return 0;
	}
	
	public long freeMemory()
	{
		return 0;
	}
	
}
