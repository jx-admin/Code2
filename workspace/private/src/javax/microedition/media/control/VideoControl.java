package javax.microedition.media.control;

import javax.microedition.media.MediaException;

public interface VideoControl extends GUIControl
{
	static final int USE_DIRECT_VIDEO = 1;
	
	Object initDisplayMode(int mode, Object arg);
	
	int getDisplayHeight();
	
	int getDisplayWidth();
	
	int getSourceHeight();
	
	int getSourceWidth();
	
	void setDisplayLocation(int x, int y);
	
	void setDisplaySize(int width, int height) throws MediaException;
	
	int getDisplayX();
	
	int getDisplayY();
	
	void setVisible(boolean visible);
	byte[] getSnapshot(Object v);
}
