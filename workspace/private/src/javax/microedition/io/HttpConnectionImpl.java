package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpConnectionImpl implements HttpConnection {
	
	String url;
	
	String protocol;
	
	public HttpConnectionImpl(String url)
	{ }

	public long getDate() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getExpiration() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeaderField(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeaderField(int n) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public long getHeaderFieldDate(String name, long def) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getHeaderFieldInt(String name, int def) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHeaderFieldKey(int n) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastModified() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRef() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getResponseCode() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getResponseMessage() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRequestMethod(String method) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setRequestProperty(String key, String value) throws IOException {
		// TODO Auto-generated method stub

	}

	public String getEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataInputStream openDataInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream openInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public DataOutputStream openDataOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream openOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
