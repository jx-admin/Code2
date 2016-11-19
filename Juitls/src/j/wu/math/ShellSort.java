package j.wu.math;

import j.wu.utils.Logger;

public class ShellSort {

	public static void main(String[] agrs) {
		int[] src = new int[] { 3, 10, 7, 12, 90, 1, 4, 23, 90 };
		Logger.d("src", Logger.toString(src).toString());
		shellSort(src);
		Logger.d("src", Logger.toString(src).toString());
	}

	public static void shellSort(int[] a) {
		int d = a.length;
		while (true) {
			d /= 2;
			for (int x = 0; x < d; x++) {
				for (int i = x + d; i < a.length; i += d) {
					if (a[i - d] > a[i]) {
						int temp = a[i];
						int j = i - d;
						for (; j >= 0 && a[j] > temp; j -= d) {
							a[j + d] = a[j];
						}
						a[j + d] = temp;
					}
				}

			}
			if (d == 1) {
				break;
			}
		}
	}
}
