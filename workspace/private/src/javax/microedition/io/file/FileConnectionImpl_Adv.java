package javax.microedition.io.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class FileConnectionImpl_Adv implements FileConnection {
	
	FileInputStream input;
	
	FileOutputStream output;
	
	public FileConnectionImpl_Adv(String url)
	{ }

	public long availableSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean canRead() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canWrite() {
		// TODO Auto-generated method stub
		return false;
	}

	public void create() throws IOException {
		// TODO Auto-generated method stub

	}

	public void delete() throws IOException {
		// TODO Auto-generated method stub

	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public long fileSize() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isHidden() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	public long lastModified() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Enumeration list() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration list(String filter, boolean includeHidden)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void mkdir() throws IOException {
		// TODO Auto-generated method stub

	}

	public DataInputStream openDataInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public DataOutputStream openDataOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream openInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream openOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream openOutputStream(long byteOffset) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void rename(String newName) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setFileConnection(String fileName) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setHidden(boolean hidden) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setWritable(boolean writable) {
		// TODO Auto-generated method stub

	}

	public long totalSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
