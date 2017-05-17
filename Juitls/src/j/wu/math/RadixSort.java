package j.wu.math;

import j.wu.utils.Logger;

/**
 * @author junxuwang
 *
 */
/**
 * @author junxuwang
 *
 */
public class RadixSort {
	public static void main(String[] args) {
		int[] src = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 0 };

		System.out.println("Before heap:");
		Logger.d("src", Logger.toString(src).toString());

		radixSort(src, 0, src.length - 1);

		System.out.println("After heap sort:");
		Logger.d("src", Logger.toString(src).toString());

		int[] srcAll = { 9, 8, 7, -6, -9, -8, -7, 6, 5, 4, 3, 2, 1, 0, -10, 12, -8 };

		System.out.println("Before heap:");
		Logger.d("src", Logger.toString(srcAll).toString());

		radixSortAll(srcAll);

		System.out.println("After heap sort:");
		Logger.d("src", Logger.toString(srcAll).toString());

	}

	/**
	 * 基数排序 地位开始，指定位数
	 * 
	 * @param a
	 *            待排序集合
	 * @param d
	 *            最大位数
	 */
	public static void radixSort(int[] a, int d) {
		int k = 0;
		int n = 1;
		int m = 1;
		int[][] temp = new int[10][a.length];
		int[] count = new int[10];
		while (m <= d) {
			for (int i = 0; i < a.length; i++) {
				int lsd = (a[i] / n) % 10;
				temp[lsd][count[lsd]] = a[i];
				count[lsd]++;
			}
			for (int i = 0; i < 10; i++) {
				if (count[i] == 0) {
					continue;
				}
				for (int j = 0; j < count[i]; j++) {
					a[k++] = temp[i][j];
				}
				count[i] = 0;
			}
			d *= 10;
			m++;
			k = 0;
		}
	}

	/**
	 * 地位开始，自动判断位数
	 * 
	 * @param a
	 */
	public static void radixSort(int[] a) {
		radixSort(a, 0, a.length - 1);
	}

	/**
	 * 桶排序，指定范围
	 * 
	 * @param a
	 *            集合
	 * @param l
	 *            起始位置（包含）
	 * @param r
	 *            结束位置（包含）
	 */
	public static void radixSort(int[] a, int l, int r) {
		if (a == null || a.length <= 1 || l < 0 || r > a.length || l >= r) {
			return;
		}
		int d = 1;
		int size = r - l + 1;
		int[][] temp = new int[10][size];
		int[] count = new int[10];
		int need = size;
		while (need > 0) {
			need = 0;
			for (int i = l; i <= r; i++) {
				if (a[i] >= d) {
					++need;
				}
				int lsd = (a[i] / d) % 10;
				temp[lsd][count[lsd]] = a[i];
				count[lsd]++;
			}
			if (need == 0) {
				break;
			}
			int k = l;
			for (int i = 0; i < 10; i++) {
				if (count[i] > 0) {
					for (int j = 0; j < count[i]; j++) {
						a[k++] = temp[i][j];
					}
					count[i] = 0;
				}
			}
			Logger.d("d", d + "");
			d *= 10;
		}
	}

	/**
	 * 支持负数,???Í
	 * 
	 * @param a
	 */
	public static void radixSortAll(int[] a) {
		int k = 0;
		int n = 1;
		int[][] temp = new int[11][a.length];
		int[] count = new int[11];
		while (true) {
			boolean hasNum = false;
			for (int i = 0; i < a.length; i++) {
				int num = a[i];
				int lsd;
				if (num < 0) {
					if (-num >= n) {
						lsd = (-num / n) % 10;
					} else {
						lsd = -1;
					}
					hasNum = hasNum | ((-num * 10) >= n);
				} else {
					lsd = (a[i] / n) % 10;
					hasNum = hasNum | (num >= n);
				}
				lsd++;
				temp[lsd][count[lsd]] = a[i];
				count[lsd]++;
			}
			if (!hasNum) {
				break;
			}
			for (int i = 0; i < 11; i++) {
				if (count[i] == 0) {
					continue;
				}
				if (i == 0) {
					for (int j = count[i] - 1; j >= 0; j--) {
						a[k++] = temp[i][j];
					}
				} else {
					for (int j = 0; j < count[i]; j++) {
						a[k++] = temp[i][j];
					}
				}
				count[i] = 0;
			}
			Logger.d("d", n + "");
			n *= 10;
			k = 0;
		}
	}
}
