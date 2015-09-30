package java.lang;

public class Integer {

	private int value;
	
	public static final int MAX_VALUE = 2147483647;

	public static final int MIN_VALUE = -2147483648;

	public Integer(int value) {
	}

	public int intValue() {
		return 0;
	}

	public int hashCode() {
		return value;
	}

	public String toString() {
		return ""+value;
	}

	public static String toString(int i) {
		return null;
	}
	
	public static String toString(int i, int radix) {
		return null;
	}

	public static int parseInt(String s) throws NumberFormatException {
		return 0;
	}

	public static int parseInt(String s, int radix) throws NumberFormatException {
		return 0;
	}
	
	public static Integer valueOf(String s) throws NumberFormatException
	{
		return null;
	}
	
	public static Integer valueOf(String s, int radix) throws NumberFormatException
	{
		return null;
	}
	
	public long longValue()
	{
		return 0;
	}
	
	public byte byteValue()
	{
		return 0;
	}
	public static String toHexString(int i)
	{
	    return null;
	}
	
	public static String toBinaryString(int i)
	{
		return null;
	}
	
	public boolean equals(Object o) {
		return false;
	}
}
