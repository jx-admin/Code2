package java.io;

public class ByteArrayOutputStream extends OutputStream {
	
	public ByteArrayOutputStream(int size) { }
	
	public ByteArrayOutputStream() { }

	public synchronized int size() {
		return 0;
	}

	public synchronized byte[] toByteArray() {
		return null;
	}
	
	public synchronized void write(int b) throws IOException { }

	public synchronized void write(byte[] bytes) throws IOException { }

	public synchronized void write(byte[] b, int off, int len) throws IOException { }
	
	public void reset()	{ }

	public void close() throws IOException { }
}
