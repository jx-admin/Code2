package java.io;

public abstract class OutputStream {
	public void close() throws IOException { }

	public abstract void write(int b) throws IOException;
	
	public void write(byte[] bytes) throws IOException { }
	
	public void write(byte[] b, int off, int len) throws IOException { }

	public void flush() throws IOException
	{ }
}
