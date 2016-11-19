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
		for (int i = 1; i < a.length; i++) {
			if (a[i - 1] > a[i]) {
				int temp = a[i];
				int j = i;
				while (j > 0 && a[j - 1] > temp) {
					a[j] = a[j - 1];
					j--;
				}
				a[j] = temp;
			}
		}
	}
}
