package java.lang;

public class Long {
	
    public static final long MIN_VALUE = 0x8000000000000000L;
    public static final long MAX_VALUE = 0x7fffffffffffffffL;

	private long value;
	
	public Long(long value) {
	}

	public long longValue() {
		return 0;
	}

	public int hashCode() {
		return(int) value;
	}

	public String toString() {
		return ""+value;
	}

	public static String toString(long i, int radix) {
		return null;
	}

	public static String toString(long i) {
		return null;
	}
	
	public static long parseLong(String s) throws NumberFormatException
    {
		return 0;
    }
	
	public boolean equals(Object o) {
		return false;
	}
}
