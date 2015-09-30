package java.io;

public abstract class InputStream {
	
	private byte[] skipBuffer;

	public void close() throws IOException {

	}

	public int read(byte[] b) throws IOException {
		return 0;
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		return 0;
	}
	
	public abstract int read() throws IOException;

	public long skip(long n) {
		return 0;
	}
	
	public int available() throws IOException
    {
		return 0;
    }
}
