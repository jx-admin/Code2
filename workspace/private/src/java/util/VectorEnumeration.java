package java.util;

public class VectorEnumeration implements Enumeration
{
	private Vector vector;

	public VectorEnumeration(Vector vector) {
	}

	public boolean hasMoreElements() {
		return false;
	}

	public Object nextElement() {
		return null;
	}
	}
