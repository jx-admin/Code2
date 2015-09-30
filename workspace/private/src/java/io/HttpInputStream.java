package java.io;

public class HttpInputStream extends InputStream {
	public int read() throws IOException {
		return 0;
	}
	
	public int read(byte[] b) throws IOException {
		return 0;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return 0;
	}
	
	public long skip(long n) {
		return 0;
	}
	
	public int available() throws IOException
    {
		return 0;
    }

	public void close() throws IOException { }
}
