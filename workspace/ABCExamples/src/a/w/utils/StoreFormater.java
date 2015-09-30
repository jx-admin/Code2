package a.w.utils;

import java.text.DecimalFormat;

public class StoreFormater {

	static char[] name = { 'T', 'G', 'M', 'K', 'B' };

	public static void main(String[] args) {
		System.out.println(getStoreSize(-100 - 1024 - 1024 * 1024));
		System.out.println(getStoreSize(-100 - 1024));
		System.out.println(getStoreSize(-100));
		System.out.println(getStoreSize(0));
		System.out.println(getStoreSize(1));
		System.out.println(getStoreSize(100 + 1024));
		System.out.println(getStoreSize(100 + 1024 + 1024 * 1024));

		System.out.println(getStoreSize(-100 - 1024 - 1024 * 1024, ' '));
		System.out.println(getStoreSize(-100 - 1024, ' '));
		System.out.println(getStoreSize(-100, ' '));
		System.out.println(getStoreSize(0, ' '));
		System.out.println(getStoreSize(1, ' '));
		System.out.println(getStoreSize(1 << 10, ' '));
		System.out.println(getStoreSize(1 << 20, ' '));
		System.out.println(getStoreSize(1 << 30, ' '));
		System.out.println(getStoreSize((1l << 50) + 1, ' '));
	}

	private static long getStep(char name) {
		long step = 1l;
		switch (name) {
		case 'T':
			step <<= 40;
			break;
		case 'G':
			step <<= 30;
			break;
		case 'M':
			step <<= 20;
			break;
		case 'K':
			step <<= 10;
			break;
		case 'B':
			step = 1;
			break;
		default:
			step = 0;
			break;
		}
		return step;
	}
	static DecimalFormat df = new DecimalFormat("0.00");
	/**
	 * @param size
	 * @param unit
	 *            T or G or M or K or B
	 * @return xxT(or G or M or K or B)
	 */
	public static String getStoreSize(long size, char unit) {
		if (size == 0) {
			return "0" + unit;
		}

		long step = getStep(unit);
		if (step != 0) {
			return String.valueOf((double) size / step) + unit;
		}

		long value = Math.abs(size);
		for (int i = 0; i < name.length; i++) {
			step = getStep(name[i]);
			if (value >= step) {
				return df.format(((double) size / step)) + name[i];
			}
		}

		return String.valueOf(size);
	}

	/**
	 * @param size
	 * @return xxTxxGxxMxxKxxB
	 */
	public static String getStoreSize(long size) {
		if (size == 0) {
			return "0";
		}
		StringBuilder sb = new StringBuilder();
		int i = name.length - 1;
		long value = Math.abs(size);
		for (; i >= 1 && value != 0; i--) {
			long cv = value & 0x01ff;
			if (cv != 0) {
				sb.insert(0, name[i]).insert(0, cv);
			}
			value >>= 10;
		}
		if (size < 0) {
			sb.insert(0, '-');
		}
		return sb.toString();
	}

}
