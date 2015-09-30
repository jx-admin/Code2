package java.util;

public class HashtableEnumeration implements Enumeration {
	private Hashtable table;

	public HashtableEnumeration(Hashtable table) {
	}

	public boolean hasMoreElements() {
		return false;
	}

	public Object nextElement() {
		return null;
	}
	}
