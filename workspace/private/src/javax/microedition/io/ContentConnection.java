package javax.microedition.io;

public interface ContentConnection extends StreamConnection
{
	public String getType();
	
	public String getEncoding();
	
	public long getLength();
	
}
