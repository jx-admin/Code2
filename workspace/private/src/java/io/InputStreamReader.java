package java.io;

public class InputStreamReader extends Reader
{
	InputStream stream;
	
	public InputStreamReader(InputStream is) throws UnsupportedEncodingException
	{
	}

	public InputStreamReader(InputStream is, String enc) throws UnsupportedEncodingException
    {
    }

	public void close() throws IOException
	{
	}

	public boolean ready() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public int read() throws IOException
	{
		return -1;
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
