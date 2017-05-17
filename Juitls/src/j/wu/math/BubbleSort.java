package j.wu.math;

import j.wu.utils.Logger;

public class BubbleSort {

	public static void main(String[]args){
		int[] src = new int[] { 3, 10, 7, 12, 90, 1, 4, 23, 90,-1 };
		Logger.d("src",Logger.toString(src).toString());
		bubbleSort(src);
		Logger.d("src",Logger.toString(src).toString());
	}
	/**下沉*/
	public static void bubbleSort2(int[]a){
		boolean sort=true;
		for(int i=a.length-1;sort&&i>0;i--){
			sort=false;
			for(int j=1;j<=i;j++){
				if(a[j]<a[j-1]){
					Logger.d("swap", a[j]+" "+j+" -- "+a[j-1]+" "+(j-1));
					a[j]+=a[j-1];
					a[j-1]=a[j]-a[j-1];
					a[j]-=a[j-1];
					sort=true;
				}else{
					Logger.d("not", a[j]+" "+j+" -- "+a[j-1]+" "+(j-1));
				}
			}
			if(!sort){
				Logger.d("end", "sort ="+sort);
			}
		}
	}
	public static void bubbleSort(int[]a){
		int n=a.length;
		boolean sort=true;
		for(int i=1;sort&&i<n;i++){
			sort=false;
			for(int j=n-1;j>=i;j--){
				if(a[j]<a[j-1]){
					a[j]+=a[j-1];
					a[j-1]=a[j]-a[j-1];
					a[j]-=a[j-1];
					sort=true;
					Logger.d("cha",Logger.toString(a).toString());
				}else{
					Logger.d("not",Logger.toString(a).toString());
				}
			}
			if(!sort){
				Logger.d("end", "sort ="+sort);
			}
		}
	}
}
