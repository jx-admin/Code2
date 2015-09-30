package java.io;

public abstract class Reader extends Object
{
	protected  Object   lock = new Object();
	public abstract void close() throws IOException;

	public int read(char[] cbuf) throws IOException
	{
		return 0;
	}

	public abstract int read(char[] cbuf, int off, int len) throws IOException;

	public int read() throws IOException
	{
		return -1;
	}

	public long skip(long n) throws IOException
	{
		return n;
	}

	public void reset() throws IOException
	{ }

	public void mark(int readAheadLimit) throws IOException
	{ }

	public boolean markSupported()
	{
		return false;
	}

	public abstract boolean ready() throws IOException;
}
