package com.math.sort;

import java.util.Arrays;

public class MergeSort {

	public static void mergeSort(int[] data) {
		System.out.println("开始排序：");
		sort(data, 0, data.length - 1);
	}

	/**
	 * 将索引从left到right范围的数组元素进行归并排序 data 待排序数组 left 待排序数组的第一个元素索引 right
	 * 待排序数组的最后一个元素索引
	 */
	private static void sort(int[] data, int left, int right) {
		System.out.println("left "+left+" right "+right);
		// TODO Auto-generated method stub
		if (left < right) {
			// 找出中间索引
			int center = (left + right)>>1;
			// 对左边数组进行递归
			sort(data, left, center);
			// 对右边数组进行递归
			sort(data, center + 1, right);
			// 合并
			merge(data, left, center, right);

		}
	}

	/**
	 * <pre>
	 * 将两个数组进行归并，归并前两个数组已经有序，归并后依然有序
	 *  data 数组对象
	 *  	left 左数组的第一个元素的索引
	 *  	center左数组的最后一个元素的索引，center+1是右数组第一个元素的索引 
	 *  	right 右数组的最后一个元素的索引
	 */
	private static void merge(int[] data, int left, int center, int right) {
		// TODO Auto-generated method stub
		int[] tmpArr = new int[data.length];
		int mid = center + 1;
		// third记录中间数组的索引
		int third = left;
		int tmp = left;
		while (left <= center && mid <= right) {
			// 从两个数组中取出最小的放入中间数组
			if (data[left] <= data[mid]) {
				tmpArr[third++] = data[left++];
			} else {
				tmpArr[third++] = data[mid++];
			}
		}
		// 剩余部分依次放入中间数组
		while (mid <= right) {
			tmpArr[third++] = data[mid++];
		}
		while (left <= center) {
			tmpArr[third++] = data[left++];
		}
		// 将中间数组中的内容复制回原数组
		while (tmp <= right) {
			data[tmp] = tmpArr[tmp++];
		}
		System.out.println(Arrays.toString(data));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] data = { 21, 30, 49, 30, 16, 9, -16, 10, 25, 18 };
		System.out.println("排序之前：\n" + Arrays.toString(data));
		mergeSort(data);
		System.out.println("排序之后：\n" + Arrays.toString(data));
	}

}
