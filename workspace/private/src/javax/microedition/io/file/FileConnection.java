package javax.microedition.io.file;

import javax.microedition.io.StreamConnection;

public interface FileConnection extends StreamConnection
{
	public String getURL();
	
	public boolean canRead();
	
	public boolean canWrite();
	
	public long totalSize();
	
	public long availableSize();
	
	public long lastModified();
	
	public void delete() throws java.io.IOException;

	public java.io.InputStream openInputStream() throws java.io.IOException;
	
	public java.io.OutputStream openOutputStream() throws java.io.IOException;
	
	public java.io.OutputStream openOutputStream(long byteOffset) throws java.io.IOException;
	
	public java.io.DataInputStream openDataInputStream() throws java.io.IOException;
	
	public java.io.DataOutputStream openDataOutputStream() throws java.io.IOException;
	
	public boolean isDirectory();
	
	public boolean isHidden();
	
	public boolean isOpen();
	
	public java.util.Enumeration list() throws java.io.IOException;
	
	public java.util.Enumeration list(java.lang.String filter, boolean includeHidden)
     throws java.io.IOException;
	
	public void mkdir() throws java.io.IOException;
    
	public boolean exists();
	
	public void rename(java.lang.String newName) throws java.io.IOException;
	
	public void setFileConnection(java.lang.String fileName) throws java.io.IOException;
	
	public void create() throws java.io.IOException;
	
	public long fileSize() throws java.io.IOException;
	
	public void setHidden(boolean hidden) throws java.io.IOException;
	
	public void setWritable(boolean writable);
}
