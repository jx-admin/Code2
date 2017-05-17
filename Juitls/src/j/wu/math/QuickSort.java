package j.wu.math;

import j.wu.utils.Logger;

public class QuickSort {
	public static void main(String[] agrs) {
		int[] src = new int[] { 3, 10, 7, 12, 90, 1, 4, 23, 90 };
		Logger.d("src", Logger.toString(src).toString());
		quickSort(src, 0, src.length - 1);
		Logger.d("src", Logger.toString(src).toString());
	}

	public static void quickSort(int[] a, int l, int r) {
		if (l < r) {
			int middle = getMiddle(a, l, r);
			quickSort(a, l, middle - 1);
			quickSort(a, middle + 1, r);
		}
	}

	private static int getMiddle(int[] a, int l, int r) {
		if (l < r) {
			int n = a[l];
			while (l < r) {
				while (l < r && a[r] > n) {
					r--;
				}
				if (l < r) {
					a[l++] = a[r];
				}
				while (l < r && a[l] < n) {
					l++;
				}
				if (l < r) {
					a[r--] = a[l];
				}
			}
			a[l] = n;
		}
		return l;
	}
}
