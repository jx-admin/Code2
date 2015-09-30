package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

public abstract class Layer
{
	public int getHeight()
	{
		return 0;
	}
	
	public int getWidth()
	{
		return 0;
	}
	
	public int getX()
	{
		return 0;
	}
	
	public int getY()
	{
		return 0;
	}
	
	public boolean isVisible()
	{
		return true;
	}
	
	public void move(int dx, int dy)
	{
	}
	
	public abstract void paint(Graphics g);
	
	public void setPosition(int x, int y)
	{
	}
	
	public void setVisible(boolean visible)
	{
	}

	 
}
