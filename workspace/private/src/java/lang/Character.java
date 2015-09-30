package java.lang;

public class Character {

    public static final int MIN_RADIX = 2;
    public static final int MAX_RADIX = 36;
    
	char value;
	
	public Character(char value)
	{
		
	}
	
	public static boolean isDigit(char ch) {
		return false;
	}

	public static boolean isUpperCase(char ch) {
		return false;
	}

	public static boolean isLowerCase(char ch) {
		return false;
	}

	public static char toLowerCase(char ch) {
		return 0;
	}

	public char charValue()
	{
		return 0;
	}
	
	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		return ""+value;
	}
	
	public boolean equals(Object o) {
		return false;
	}
}
