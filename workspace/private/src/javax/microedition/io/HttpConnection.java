package javax.microedition.io;

import java.io.IOException;

public interface HttpConnection extends ContentConnection
{

	public static final int HTTP_OK = 200;
	
	public static final int HTTP_BAD_GATEWAY = 502;
	
	public static final String GET = "GET";
	
	public static final String POST = "POST";

	public static final int HTTP_CONFLICT = 0;
	
	public String getURL();
	
	public String getProtocol();
	
	public String getHost();
	
	public String getFile();
	
	public String getRef();
	
	public String getQuery();
	
	public int getPort();
	
	public String getRequestMethod();
	
	public void setRequestMethod(String method) throws IOException;
	
	public String getRequestProperty(String key);
	
	public void setRequestProperty(String key, String value) throws IOException;
	
	public long getExpiration() throws IOException;
	
	public long getDate() throws IOException;
	
	public long getLastModified() throws IOException;
	
	public int getResponseCode() throws IOException;
	
	public String getResponseMessage() throws IOException;
	
	public String getHeaderField(String name) throws IOException;
	
	public int getHeaderFieldInt(String name, int def) throws IOException;
	
	public long getHeaderFieldDate(String name, long def) throws IOException;
	
	public String getHeaderField(int n) throws IOException;
	
	public String getHeaderFieldKey(int n) throws IOException;
}
