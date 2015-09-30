package com.math.sort;

public class RecursiveSort {
		public static void main(String[] args) {
			String[] a= {"9","8","7","6","5","4","3","2","1"};
			Object[] aux = (Object[])a.clone();
			mergeSort(aux, a);
			for(int i=0;i<a.length;i++){
				System.out.println(a[i]);
			}
		}

	    //每个拆分的列表元素个数<=3
	    private static final int INSERTIONSORT_THRESHOLD = 3;
	    /**
	     * 
	     * 归并排序(非递归实现)
	     */
	    public static void mergeSort(Object[] src,Object[] dest){
	    	int spreCount = INSERTIONSORT_THRESHOLD;	 
	    	int low,mid,high;
	    	int len = src.length;
			if(len <= INSERTIONSORT_THRESHOLD*2){		//如果只能划分为两组，直接排序
				sort(dest,0,len);
				return;
			}
	    	while(spreCount < len){
		    	for(int i=0;i<len;i=high){	//依次排序归并相邻两个列表
		    		low = i;	
		    		high = low + spreCount * 2 ;
		    		mid = low + spreCount;
		    		if(high >= len){
		    			high = len;
		    		}
		    		int l = high - low;
		    		if(l <= INSERTIONSORT_THRESHOLD){
		    			sort(src,low,high);
		    			break;
		    		}

		    		if(spreCount == 3){		//所有拆分的列表只进行一次排序
		    			sort(dest,low,mid);
		    			sort(dest,mid,high);
		    		}
		    		if(l == len)	//最后一次归并把src有次序的赋给dest
		    			Merge(src,dest,low,mid,high);
		    		else
		    			Merge(dest,src,low,mid,high);

		    	}
		    	spreCount *= 2;
	    	}
	    	
	    }
	    //对拆分的每个列表进行排序
	    private static void sort(Object[] dest,int low,int high){
			for (int i = low; i < high; i++){
				for (int j = i; j > low ; j--){
					if(((Comparable) dest[j - 1]).compareTo(dest[j]) > 0){
						swap(dest, j-1, j); 
					}
				}
			}
	    }
	    
	    //归并相邻两个列表并保存在dest中
		private static void Merge(Object[] src, Object[] dest, int low, int mid,
				int high) {
	    	int i = low;
	    	int p = low;	//第一个列表指针
	    	int q = mid;    //第二个列表指针
	    	while(p < mid && q <high){
	    		if(((Comparable) src[p]).compareTo(src[q]) <= 0){
	    			dest[i++] = src[p++];
	    		}else{
	    			dest[i++] = src[q++];
	    		}
	    	}
	    	//添加剩余的值
	    	while(p < mid && i<high){
	    		dest[i++] = src[p++];
	    	}
	    	while(q < high && i<high){
	    		dest[i++] = src[q++];
	    	}
			
		}
		
	    private static void swap(Object[] x, int a, int b) {
	    	Object t = x[a];
	    	x[a] = x[b];
	    	x[b] = t;
	    }
	 

	}