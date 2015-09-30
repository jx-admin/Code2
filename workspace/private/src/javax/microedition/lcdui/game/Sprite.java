package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Sprite extends Layer
{
	
//	public static final int TRANS_NONE = 0;
//	
//	public static final int TRANS_MIRROR = 2;
//	
//	public static final int TRANS_ROT180 = 3;
//	
//	public static final int TRANS_ROT90 = 5;
//	
//	public static final int TRANS_ROT270 = 6;
	
	public static final int TRANS_MIRROR = 2;
	public static final int TRANS_MIRROR_ROT180 = 1;
	public static final int TRANS_MIRROR_ROT270 = 4;
	public static final int TRANS_MIRROR_ROT90 = 7;
	public static final int TRANS_NONE = 0;
	public static final int TRANS_ROT180 = 3;
	public static final int TRANS_ROT270 = 6;
	public static final int TRANS_ROT90 = 5;

	int[] rgb;
	Image[] frames;
	int[] sequence;
	Image image;
	int transform, frame, frameWidth, frameHeight;
	
	public Sprite(Image image)
	{}
	
	public Sprite(Image image, int frameWidth, int frameHeight) 
	{}
	
	public Sprite(Sprite s)
	{}
	
	public void paint(Graphics g)
	{}
	
	public int getFrame()
	{
		return 0;
	}
	 
	public void setFrame(int sequenceIndex)
	{}
	
	public void setImage(Image img, int frameWidth, int frameHeight)
	{}
	
	public void nextFrame()
	{}
	
	public void prevFrame()
	{}
	
	public boolean collidesWith(Sprite s, boolean pixelLevel)
	{
		return false;
	}
	
	public boolean collidesWith(Image image, int x, int y, boolean pixelLevel) 
	{
		return false;
	}
	
	public int getRefPixelX()
	{
		return 0;
	}
	
	public int getRefPixelY()
	{
		return 0;
	}
	
	public void setTransform(int transform)
	{}
	
	public int getRawFrameCount()
	{
		return 0;
	}
	
	public void setRefPixelPosition(int x, int y)
	{}
	
	public void defineReferencePixel(int x, int y)
	{}
	
	public void defineCollisionRectangle(int x, int y, int width, int height)
	{}
	
	public int getFrameSequenceLength()
	{
		return 0;
	}
}
