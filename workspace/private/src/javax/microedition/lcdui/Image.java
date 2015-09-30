package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;

public class Image {

	private Image() {
	}

	public static Image createImage(String path) throws java.io.IOException {
		return null;
	}

	public int getHeight() {
		return 0;
	}

	public int getWidth() {
		return 0;
	}

	public Graphics getGraphics() {
		return null;
	}

	public static Image createImage(int width, int height) {
		return null;
	}

	public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
		return null;
	}
	
	public static Image createImage(byte[] imageData, int imageOffset, int imageLength)
	{
		return null;
	}
	
	public static Image createImage(InputStream stream) throws IOException
    {
		return null;
    }
	
	public static Image createImage(Image image, int x, int y, int width, int height, int transform)
	{
		return null;
	}
	
	public static Image createImage(Image image)
	{
		return null;
	}
	
	public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height)
	{
		
	}
	
	public boolean isMutable()
	{
		return false;
	}
}
