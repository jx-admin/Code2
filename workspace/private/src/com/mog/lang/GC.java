package com.mog.lang;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class GC {

	public final static boolean DEBUG_DELETE = true;

	private final static Vector deletes = new Vector(1000);

	public static void __delete(Object o) {
		if (o == null) {
			return;
		}
		if (o instanceof IGCObject) {
			((IGCObject) o)._finalize();
		}
	}

	public static void deleteHashtable(Hashtable t) {
		if (t != null) {
			Enumeration keys = t.keys();
			deleteElements(keys);
			__delete(keys);

			Enumeration elements = t.elements();
			deleteElements(elements);
			__delete(elements);
		}
	}

	public static void deleteArray(Object array) {
		if (array != null && array.getClass().isArray()) {
			if (array instanceof Object[]) {
				Object[] arr = (Object[]) array;
				for (int i = 0; i < arr.length; i++) {
					GC.__delete(arr[i]);
				}
			}
		}
	}

	public static void deleteVector(Vector v) {
		if (v != null) {
			Enumeration enu = v.elements();
			GC.deleteElements(enu);
		}
	}

	public static void deleteElements(Enumeration enu) {
		while (enu != null && enu.hasMoreElements()) {
			GC.__delete(enu.nextElement());
		}
	}

	public void _finalize() {
	}

	public synchronized static void objref(Object o) {
		if (o == null) {
			return;
		}
		deletes.addElement(o);
	}

	public synchronized static void objRelease() {
		System.out.println("Deletes GC");
		deleteVector(deletes);
		deletes.removeAllElements();
	}
}
