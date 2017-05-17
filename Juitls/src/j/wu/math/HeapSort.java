package j.wu.math;

import j.wu.utils.Logger;

public class HeapSort {
	public static void main(String[] args) {
		int[] src = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, 1, -4 };

		System.out.println("Before heap:");
		Logger.d("src", Logger.toString(src).toString());

		heapMaxSort(src);

		System.out.println("After heap sort:");
		Logger.d("src", Logger.toString(src).toString());
	}

	public static void heapMaxSort(int[] array) {
		if (array == null || array.length <= 1) {
			return;
		}

		buildMaxHeap(array);

		for (int i = array.length - 1; i >= 1; i--) {
			int temp = array[0];
			array[0] = array[i];
			array[i] = temp;

			maxHeap(array, i, 0);
		}
	}

	private static void buildMaxHeap(int[] array) {
		int half = array.length / 2;
		for (int i = half; i >= 0; i--) {
			maxHeap(array, array.length, i);
		}
	}

	private static void maxHeap(int[] array, int heapSize, int index) {
		int left = index * 2 + 1;
		int right = index * 2 + 2;

		int largest = index;
		if (left < heapSize && array[left] > array[index]) {
			largest = left;
		}

		if (right < heapSize && array[right] > array[largest]) {
			largest = right;
		}

		if (index != largest) {
			int temp = array[index];
			array[index] = array[largest];
			array[largest] = temp;

			maxHeap(array, heapSize, largest);
		}
	}

	public static void heapMinSort(int[] array) {
		if (array == null || array.length <= 1) {
			return;
		}

		buildMinHeap(array);

		for (int i = array.length - 1; i >= 1; i--) {
			int temp = array[0];
			array[0] = array[i];
			array[i] = temp;

			minHeap(array, i, 0);
		}
	}

	private static void buildMinHeap(int[] array) {
		if (array == null || array.length <= 1) {
			return;
		}

		int half = array.length / 2;
		for (int i = half; i >= 0; i--) {
			minHeap(array, array.length, i);
		}
	}

	private static void minHeap(int[] array, int heapSize, int index) {
		int left = index * 2 + 1;
		int right = index * 2 + 2;

		int largest = index;
		if (left < heapSize && array[left] < array[index]) {
			largest = left;
		}

		if (right < heapSize && array[right] < array[largest]) {
			largest = right;
		}

		if (index != largest) {
			int temp = array[index];
			array[index] = array[largest];
			array[largest] = temp;

			minHeap(array, heapSize, largest);
		}
	}
}