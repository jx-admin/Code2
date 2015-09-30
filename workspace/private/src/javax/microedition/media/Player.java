package javax.microedition.media;

public interface Player extends Controllable
{
        public static final int CLOSED = 0;
	public static final long TIME_UNKNOWN = -1;
	
	static final int REALIZED = 200;
	
	static final int PREFETCHED = 300;
	
	static final int STARTED = 400;
	
	void addPlayerListener(PlayerListener playerListener);
	
	void realize() throws MediaException;
	
	void prefetch() throws MediaException;
	
	String getContentType();
	
	int getState();
	
	long getDuration();
	
	long getMediaTime();
	
	long setMediaTime(long now) throws MediaException;
	
	void start() throws MediaException;
	
	void stop() throws MediaException;
	
	void removePlayerListener(PlayerListener playerListener);
	
	void deallocate();
	
	void close();
	
	void setLoopCount(int count);
}
