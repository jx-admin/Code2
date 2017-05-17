package j.wu.math;

import j.wu.utils.Logger;

/**
 * @author junxuwang
 *
 */
public class SelectSort {

	public static void main(String[] agrs) {
		int[] src = new int[] { 3, 3, 10, 7, 12, 90, 1, 4, 23, 90, 10, 10, 0 };
		Logger.d("src", Logger.toString(src).toString());
		selectSort(src);
		Logger.d("src", Logger.toString(src).toString());
	}

	public static void selectSort(int[] a) {
		int min, max, temp, n = a.length;
		for (int i = 0, times = n / 2; i < times; i++) {
			min = i;
			max = i;
			for (int j = i + 1; j < n - i; j++) {
				if (a[j] < a[min]) {
					min = j;
				} else if (a[j] > a[max]) {
					max = j;
				}
			}
			temp = a[i];
			a[i] = a[min];
			a[min] = temp;
			if (max == i) {
				max = min;
			}
			temp = a[n - i - 1];
			a[n - i - 1] = a[max];
			a[max] = temp;
		}
	}

}
