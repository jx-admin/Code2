package org.ikags.util.bxml;

import java.io.IOException;
import java.io.Reader;

/**
 * StringReader<BR>
 * JAVA SE源代码实现
 * 
 * @author SUN
 * 
 */
public class StringReader extends Reader
{

    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;

    public StringReader(String s)
    {
        this.str = s;
        this.length = s.length();
    }

    private void ensureOpen() throws IOException
    {
        if (str == null)
            throw new IOException("Stream closed");
    }

    public int read() throws IOException
    {
        synchronized (lock)
        {
            ensureOpen();
            if (next >= length)
                return -1;
            return str.charAt(next++);
        }
    }

    public int read(char cbuf[], int off, int len) throws IOException
    {
        synchronized (lock)
        {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0))
            {
                throw new IndexOutOfBoundsException();
            }
            else if (len == 0)
            {
                return 0;
            }
            if (next >= length)
                return -1;
            int n = Math.min(length - next, len);
            str.getChars(next, next + n, cbuf, off);
            next += n;
            return n;
        }
    }

    public long skip(long ns) throws IOException
    {
        synchronized (lock)
        {
            ensureOpen();
            if (next >= length)
                return 0;
            // Bound skip by beginning and end of the source
            long n = Math.min(length - next, (int)ns);
            n = Math.max(0-next, (int)n);
            next += n;
            return n;
        }
    }

    public boolean ready() throws IOException
    {
        synchronized (lock)
        {
            ensureOpen();
            return true;
        }
    }

    public boolean markSupported()
    {
        return true;
    }

    public void mark(int readAheadLimit) throws IOException
    {
        if (readAheadLimit < 0)
        {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock)
        {
            ensureOpen();
            mark = next;
        }
    }

    public void reset() throws IOException
    {
        synchronized (lock)
        {
            ensureOpen();
            next = mark;
        }
    }

    public void close()
    {
        str = null;
    }
}
