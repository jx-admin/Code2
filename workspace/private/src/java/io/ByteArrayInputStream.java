package java.io;

public class ByteArrayInputStream extends InputStream
{
	
	public ByteArrayInputStream(byte[] buf) { }

	public ByteArrayInputStream(byte[] buf, int offset, int length) { }

	public synchronized int read() throws IOException {
		return 0;
	}

	public synchronized int read(byte[] b) throws IOException {
		return 0;
	}
	
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		return 0;
	}
	
	public long skip(long n) {
		return 0;
	}

	public int available() throws IOException {
		return 0;
	}

	public void close() throws IOException { }
}
