package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface VolumeControl extends Control
{
	int setLevel(int level);
	
	boolean isMuted();
	
	void setMute(boolean mute);
	
	
}
