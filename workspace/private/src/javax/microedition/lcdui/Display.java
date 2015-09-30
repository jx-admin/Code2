package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;

public class Display
{

	private Displayable displayable;

	public static Display getDisplay(MIDlet m)
	{
		return null;
	}
	
	public void setCurrent(Displayable nextDisplayable)
	{
		
	}
	
	public Displayable getCurrent()
	{
		return displayable;
	}

	public void requestPaint(int x, int y, int width, int height, boolean wait)
	{ }
	
	public int getBestImageWidth(int arg0){
		return arg0;
	}

	public int getBestImageHeight(int arg0){
		return arg0;
	}
	
	public boolean isColor()
	{
		return true;
	}
	
	public int numColors()
	{
		return 65536;
	}
	
	public void callSerially(Runnable task)
	{
	}
	
	public boolean flashBacklight(int arg0)
	{
		return true;
	}
	
	public boolean vibrate(int arg0)
	{
		return true;
	}
	
	public int numAlphaLevels(){
		return 1;
	}
	}
