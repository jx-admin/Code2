package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface RateControl extends Control
{
	
	int getRate();
	
	int setRate(int millirate);
	
}
