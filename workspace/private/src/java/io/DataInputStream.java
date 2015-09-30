package java.io;

public class DataInputStream extends InputStream {
	
	private InputStream in;
	
	public DataInputStream(InputStream in) {

	}

	public void close() throws IOException {

	}
	
	public int read() throws IOException {
		return 0;
	}
	
	public int read(byte[] b) throws IOException {
		return 0;
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		return 0;
	}
	
//	public final int readInt() throws IOException {
//		return 0;
//	}
//
//	public final long readLong() throws IOException {
//		return 0;
//	}
//
//	public final String readUTF() throws IOException {
//		return null;
//	}
 
	public boolean readBoolean() throws IOException {
		return false;
	}

	public byte readByte() throws IOException {
		return 0;
	}
	
	public char readChar() throws IOException {
		return 0;
	}
	
	public double readDouble() throws IOException {
		return 0;
	}
	
	public float readFloat() throws IOException {
		return 0;
	}
	
	public void readFully(byte[] b) throws IOException {
	}
	
	public void readFully(byte[] b, int off, int len) throws IOException {
	}
	
	public int readInt() throws IOException {
		return 0;
	}
	
	public long readLong() throws IOException {
		return 0;
	}
	
	public short readShort() throws IOException {
		return 0;
	}
	
	public int readUnsignedByte() throws IOException {
		return 0;
	}
	
	public int readUnsignedShort() throws IOException {
		return 0;
	}
	
	public String readUTF() throws IOException {
		return null;
	}
	
	public int skipBytes(int n)throws IOException {
		return 0;
	}
	
	public void mark(int n) throws IOException
	{
	}
	
	public void reset() throws IOException
	{
	}
	
	public boolean markSupported() 
	{
		return false;
	}

}
