public class Object1 {
	private static Object1 mObject1;

	private Object1() {
	}

	public static Object1 getObject1() {
		if (mObject1 == null) {
			synchronized (Object1.class) {
				if (mObject1 == null) {
					mObject1 = new Object1();
				}
			}
		}
		return mObject1;
	}
}
