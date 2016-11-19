package j.wu.math;

import j.wu.utils.Logger;

public class RadixSort {
	public static void main(String[] args) {
		int[] srcAll = { 9, 8, 7, -6,-9, -8, -7, 6, 5, 4, 3, 2, 1, 0 ,-10,12,-8};
		int[] src = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 10 ,0};

		System.out.println("Before heap:");
		Logger.d("src", Logger.toString(src).toString());

		radixSort(src);

		System.out.println("After heap sort:");
		Logger.d("src", Logger.toString(src).toString());
	}

	/**
	 * 基数排序
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
	public static void radixSort(int[] a) {
		int k = 0;
		int n = 1;
		int[][] temp = new int[10][a.length];
		int[] count = new int[10];
		while (true) {
			boolean hasNum=false;
			for (int i = 0; i < a.length; i++) {
				int lsd = (a[i] / n) % 10;
				hasNum=hasNum|(a[i]>=n);
				temp[lsd][count[lsd]] = a[i];
				count[lsd]++;
			}
			if(!hasNum){
				break;
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
			Logger.d("d", n+"");
			n *= 10;
			k = 0;
		}
	}
	/**
	 * 支持负数
	 * @param a
	 */
	public static void radixSortAll(int[] a) {
		int k = 0;
		int n = 1;
		int[][] temp = new int[11][a.length];
		int[] count = new int[11];
		while (true) {
			boolean hasNum=false;
			for (int i = 0; i < a.length; i++) {
				int num=a[i];
				int lsd;
				if(num<0){
					if(-num>=n){
						lsd=(-num/n)%10;
					}else{
						lsd=-1;
					}
					hasNum=hasNum|((-num*10)>=n);
				}else{
					lsd = (a[i] / n) % 10;
					hasNum=hasNum|(num>=n);
				}
				lsd++;
				temp[lsd][count[lsd]] = a[i];
				count[lsd]++;
			}
			if(!hasNum){
				break;
			}
			for (int i = 0; i < 11; i++) {
				if (count[i] == 0) {
					continue;
				}
				if(i==50){
					for (int j = count[i]-1; j >=0; j--) {
						a[k++] = temp[i][j];
					}
				}else{
					for (int j = 0; j < count[i]; j++) {
						a[k++] = temp[i][j];
					}
				}
				count[i] = 0;
			}
			Logger.d("d", n+"");
			n *= 10;
			k = 0;
		}
	}
}
