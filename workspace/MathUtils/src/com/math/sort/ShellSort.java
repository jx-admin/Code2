package com.math.sort;

/**
 * 希尔排序的思想我就不叙述了，这里，我用Java实现，并与插入排序做比较。因为希尔排序是基于插入排序，由于增加了新的特性，大大提高了插入排序的执行效率，
 * 完整代码如下，并有两者之间时间的比较
 * 
 * @author junxu.wang
 * 
 */
public class ShellSort {

	public static void main(String[] args) {
		int[] a = new int[100000];
		for (int i = 0; i < 100000; i++) {
			a[i] = (int) (Math.random() * 1000000);
		}

		long begin = System.currentTimeMillis();
		shellSort(a);
		long end = System.currentTimeMillis();
		System.out.println("希尔排序，对100000个数排列，耗费时间为：" + (end - begin) + "s");
		// 插入排序
		int[] b = new int[100000];
		for (int i = 0; i < 100000; i++) {
			b[i] = (int) (Math.random() * 1000000);
		}
		begin = System.currentTimeMillis();
		insertSort(b);
		end = System.currentTimeMillis();
		System.out.println("插入排序，对100000个数排列，耗费时间为：" + (end - begin) + "s");
		// for(int i=0;i<a.length;i++){
		// System.out.println(a[i]);
		// }
	}

	/**
	 * 希尔排序是基于插入排序，由于增加了新的特性，大大提高了插入排序的执行效率
	 * 
	 * @param a
	 * @return
	 */
	private static void shellSort3(int[] a) {
		int h = 1;
		int temp;
		int inner, outer;
		while (h <= a.length / 3)
			h = h * 3 + 1;
		while (h > 0) {
			for (outer = h; outer < a.length; outer++) {
				temp = a[outer];
				inner = outer;
				while (inner > h - 1 && a[inner - h] >= temp) {
					a[inner] = a[inner - h];
					inner -= h;
				}
				a[inner] = temp;
			}
			h = (h - 1) / 3;
		}
	}

	/**
	 * insert sort
	 * 
	 * @param b
	 */
	public static void insertSort(int b[]) {
		int temp, outer, inner;
		for (outer = 1; outer < b.length; outer++) {
			temp = b[outer];
			inner = outer;
			while (inner > 0 && b[inner - 1] > temp) {
				b[inner] = b[inner - 1];
				inner--;
			}
			b[inner] = temp;
		}
	}

	/**
	 * 插入法insertion sort 插入法较为复杂，它的基本工作原理是抽出牌，在前面的牌中寻找相应的位置插入，然后继续下一张
	 * 
	 * @param pData
	 * @param Count
	 */
	void InsertSort(int[] pData, int Count) {
		int iTemp;
		int iPos;
		for (int i = 1; i < Count; i++) {
			iTemp = pData[i];// 保存要插入的数
			iPos = i - 1;// 被插入的数组数字个数
			while ((iPos >= 0) && (iTemp < pData[iPos])) {// 从最后一个（最大数字）开始对比，大于它的数字往后移位
				pData[iPos + 1] = pData[iPos];
				iPos--;
			}
			pData[iPos + 1] = iTemp;// 插入数字的位置
		}
	}

	public static void shellSort(int[] a) {
		// 希尔排序
		int d = a.length;
		while (true) {
			d = d / 2;
			for (int x = 0; x < d; x++) {
				for (int i = x + d; i < a.length; i = i + d) {
					int temp = a[i];
					int j;
					for (j = i - d; j >= 0 && a[j] > temp; j = j - d) {
						a[j + d] = a[j];
					}
					a[j + d] = temp;
				}
			}
			if (d == 1) {
				break;
			}
		}
		System.out.println();
		System.out.println("排序之后：");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + "");
		}
	}
}
