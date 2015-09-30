package java.lang;

public class Double {
	
	public static final double MAX_VALUE = 0x7fefffffffffffffL;
	
	public static final double MIN_VALUE = 0x1L;//TODO
	
	
	
	private double value;

	/**
	 * A constant holding a Not-a-Number (NaN) value of type <code>double</code>.
	 * It is equivalent to the value returned by
	 * <code>Double.longBitsToDouble(0x7ff8000000000000L)</code>.
	 */
	public static final double NaN = 0.0d / 0.0;

	/**
	 * A constant holding the positive infinity of type <code>double</code>.
	 * It is equal to the value returned by
	 * <code>Double.longBitsToDouble(0x7ff0000000000000L)</code>.
	 */
	public static final double POSITIVE_INFINITY = 1.0 / 0.0;

	/**
	 * A constant holding the negative infinity of type <code>double</code>.
	 * It is equal to the value returned by
	 * <code>Double.longBitsToDouble(0xfff0000000000000L)</code>.
	 */
	public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

	public Double(double d) {

	}

	public boolean isInfinite() {
		return false;
	}
	
	static public boolean isInfinite(double v) {
		return (v == POSITIVE_INFINITY) || (v == NEGATIVE_INFINITY);
	}

	public static double longBitsToDouble(long bits) {
		return 0.0;
	}

	public long longValue() {
		return 0;
	}

	public int intValue() {
		return 0;
	}

	public double doubleValue() {
		return 0;
	}

	public boolean isNaN() {
		return false;
	}

	public static boolean isNaN(double d) {
		return false;
	}

	public static Double valueOf(String s) throws NumberFormatException {
		return null;
	}
	
	public static double parseDouble(String s) throws NumberFormatException
	{
		return 0;
	}
    
	public static String toString(double d)
	{
		return null;
	}
	
	public byte byteValue()
	{
		return 0;
	}
	
	public short shortValue()
	{
		return 0;
	}
	
	public static long doubleToLongBits(double value)
	{
		return 0;
	}
	
	public int hashCode() {
		return (int)value;
	}

	public String toString() {
		return ""+value;
	}
	
	public boolean equals(Object o) {
		return false;
	}
	
}
