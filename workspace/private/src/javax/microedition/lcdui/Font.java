package javax.microedition.lcdui;

public class Font {

	public static final int FACE_PROPORTIONAL = 64;
	public static final int FACE_MONOSPACE = 32;
	public static final int FACE_SYSTEM = 0;

	public static final int SIZE_LARGE = 16;
	public static final int SIZE_MEDIUM = 0;
	public static final int SIZE_SMALL = 8;

	public static final int STYLE_PLAIN = 0;
	public static final int STYLE_BOLD = 1;
//	public static final int STYLE_ITALIC = 2;
	public static final int STYLE_UNDERLINED = 4;

	public static final int FONT_INPUT_TEXT = 1;
	public static final int FONT_STATIC_TEXT = 0;
	public static final int STYLE_ITALIC = 2;
	
	private static final Font defaultFont = null;
	
	private Font() {
	}

	public static Font getDefaultFont() {
		if(defaultFont != null)
			return defaultFont;
		return null;
	}

	public static Font getFont(int face, int style, int size) {
		return null;
	}
	
	public static Font getFont(int i)
	{
		return null;
	}

	public int stringWidth(String str) {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public int charWidth(char ch) {
		return 0;
	}
	
	public int getSize()
	{
		return 0;
	}
	
	public int getFace()
	{
		return 0;
	}
	
	public int charsWidth(char[] ch, int offset, int length)
	{
		return 0;
	}
	
	public int getStyle()
	{
		return 0;
	}
	
	public int getBaselinePosition()
	{
		return 0;
	}
	
	public int substringWidth(String s, int i, int j)
	{
		return 0;
	}

}
