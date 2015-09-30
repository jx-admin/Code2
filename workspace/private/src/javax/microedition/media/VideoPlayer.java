package javax.microedition.media;

import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.media.control.FramePositioningControl;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;

public class VideoPlayer implements Player, VolumeControl, VideoControl,
		FramePositioningControl
{
	protected Hashtable listeners;

	protected VideoPlayer(String param)
	{
	}

	protected VideoPlayer(InputStream stream, java.lang.String type)
	{ }

	public void addPlayerListener(PlayerListener playerListener) {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void deallocate() {
		// TODO Auto-generated method stub

	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getMediaTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void prefetch() throws MediaException {
		// TODO Auto-generated method stub

	}

	public void realize() throws MediaException {
		// TODO Auto-generated method stub

	}

	public void removePlayerListener(PlayerListener playerListener) {
		// TODO Auto-generated method stub

	}

	public void setLoopCount(int count) {
		// TODO Auto-generated method stub

	}

	public long setMediaTime(long now) throws MediaException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void start() throws MediaException {
		// TODO Auto-generated method stub

	}

	public void stop() throws MediaException {
		// TODO Auto-generated method stub

	}

	public Control getControl(String controlType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Control[] getControls() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMuted() {
		// TODO Auto-generated method stub
		return false;
	}

	public int setLevel(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMute(boolean mute) {
		// TODO Auto-generated method stub

	}

	public int getDisplayHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDisplayWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDisplayX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDisplayY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSourceHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSourceWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object initDisplayMode(int mode, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDisplayLocation(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void setDisplaySize(int width, int height) throws MediaException {
		// TODO Auto-generated method stub

	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub

	}

	public int skip(int framesToSkip) {
		// TODO Auto-generated method stub
		return 0;
	}

    public byte[] getSnapshot(Object v)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
