package javax.microedition.lcdui;

public class Graphics {

	public static final int TOP = 16;

	public static final int BOTTOM = 32;

	public static final int LEFT = 4;

	public static final int RIGHT = 8;

	public static final int HCENTER = 64;

	public static final int VCENTER = 128;
	
	public static final int SOLID = 0;
	public static final int DOTTED = 1;
	
	public static final int BASELINE = 64;
	
	private Graphics() {
	}

	public void setColor(int red, int green, int blue) {
	}

	public void setColor(int c) {
	}

	public void drawRect(int x, int y, int width, int height) {
	}

	public void setClip(int x, int y, int width, int height) {
	}

	public void fillRect(int x, int y, int width, int height) {
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
	}

	public void drawImage(Image img, int x, int y, int anchor) {
	}

	public void drawString(String str, int x, int y, int anchor) {
	}

	public void drawChar(char c, int x, int y, int anchor) {
	}

	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
	}

	public void setFont(Font font) {
	}

	public Font getFont() {
		return null;
	}

	public void translate(int xo, int yo) {

	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {

	}

	public int getClipHeight() {
		return 0;
	}

	public int getClipWidth() {
		return 0;
	}

	public int getClipX() {
		return 0;
	}

	public int getClipY() {
		return 0;
	}

	public int getTranslateX() {
		return 0;
	}

	public int getTranslateY() {
		return 0;
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
	{
		
	}
	
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
	{
		
	}
	
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
	{
		
	}
	
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
	{
		
	}
	
	public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor)
	{
		
	}
	
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha)
	{
		
	}
	
	public int getColor()
	{
		return 0;
	}
	
	public int getGreenComponent() 
	{
		return 0;
	}
	
	public int getRedComponent() 
	{
		return 0;
	}
	
	public int getBlueComponent() 
	{
		return 0;
	}
	
	public int getGrayScale() 
	{
		return 0;
	}

	public void setGrayScale(int value)
	{
	}
	
	public void setStrokeStyle(int style)
	{}
	
	public int getStrokeStyle()
	{
		return 0;
	}
	
	public void clipRect(int x, int y, int w, int h)
	{}
	
	public void drawChars(char[] data, int offset, int length, int x, int y, int anchor)
	{}
	
	public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor)
	{}
	
	public int getDisplayColor(int color)
	{
		return 0;
	}
}
