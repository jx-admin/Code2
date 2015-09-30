package java.io;

public class OutputStreamWriter extends Writer
{
	OutputStream stream;
	
	public OutputStreamWriter(OutputStream os) throws UnsupportedEncodingException
	{
	}

	public OutputStreamWriter(OutputStream os, String enc) throws UnsupportedEncodingException
	{
	}

	public void write(int c) throws IOException
    { }

	public void write(char cbuf[], int off, int len) throws IOException
	{
	}
	
	public void flush() throws IOException
	{
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
