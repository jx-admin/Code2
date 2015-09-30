package java.lang.ref;

public class WeakTable
{
	public WeakTable()
	{ }

	public static WeakTable currentWeakTable()
	{
		return null;
	}

	public void gc()
	{ }

	public void register(WeakReference ref)
	{ }

	public void unregister(WeakReference ref)
	{ }
}