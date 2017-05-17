package j.wu.math;

import j.wu.utils.Logger;

public class InsertionSort {
	public static void main(String[] agrs) {
		int[] src = new int[] { 3, 10, 7, 12, 90, 1, 4, 23, 90 };
		Logger.d("src", Logger.toString(src).toString());
		instertionSort(src);
		Logger.d("src", Logger.toString(src).toString());
	}

	public static void instertionSort(int[] a) {
		if (a == null || a.length == 0) {
			return;
		}
		int n = a.length;
		int temp;
		for (int i = 1; i < n; i++) {
			if (a[i] < a[i - 1]) {
				temp = a[i];
				int j = i - 1;
				while (j >= 0 && temp < a[j]) {
					a[j + 1] = a[j];
					j--;
				}
				a[j + 1] = temp;
			}
		}
	}
}
