package java.io;

public abstract class Writer
{
    private char[] writeBuffer;
    private final int writeBufferSize = 1024;
    protected Object lock;
	
    public Writer()
    {
    	this.lock = this;
    }

    public abstract void close() throws IOException;

    public abstract void flush() throws IOException;

    public void write(int c) throws IOException
    { }

    public void write(char cbuf[]) throws IOException
    { }

    public abstract void write(char cbuf[], int off, int len) throws IOException;

	public void write(String str) throws IOException
	{
	}
}
