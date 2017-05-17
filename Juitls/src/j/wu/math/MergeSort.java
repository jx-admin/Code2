package j.wu.math;

import j.wu.utils.Logger;

public class MergeSort {

	public static void main(String[] agrs) {
		int[] src = new int[] { 3, 3, 10, 7, 12, 90, 1, 4, 23, 90, 10, 10, 0 };
		Logger.d("src", Logger.toString(src).toString());
		mergeSort(src, 0, src.length - 1);
		Logger.d("src", Logger.toString(src).toString());
	}

	/**
	 * <pre>
	 * 归并排序 简介:将两个（或两个以上）有序表合并成一个新的有序表
	 * 即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列
	 * 时间复杂度为O(nlogn)
	 * 稳定排序方式
	 * </pre>
	 * 
	 * @param nums
	 *            待排序数组
	 * @return 输出有序数组
	 */
	public static void mergeSort(int[] a, int l, int r) {
		if (l < r) {
			int m = (l + r) / 2;
			mergeSort(a, l, m);
			mergeSort(a, m + 1, r);
			merge(a, l, m, r);
		}
	}

	private static void merge(int[] a, int l, int m, int r) {
		int[] temp = new int[r - l + 1];
		int i = l;// 左指针
		int j = m + 1;// 右指针
		int k = 0;
		// 把较小的数先移到新数组中
		while (i <= m && j <= r) {
			if (a[i] < a[j]) {
				temp[k++] = a[i++];
			} else {
				temp[k++] = a[j++];
			}
		}
		// 把左边边剩余的数移入数组
		while (i <= m) {
			temp[k++] = a[i++];
		}
		// 把右边边剩余的数移入数组
		while (j <= r) {
			temp[k++] = a[j++];
		}
		// 把新数组中的数覆盖nums数组
		for (i = l, k = 0; i <= r;k++, i++) {
			a[i] = temp[k];
		}
	}
}
