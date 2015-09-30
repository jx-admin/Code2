package j.wu.colection;

import java.lang.reflect.Array;

public class Main {

	public static void main(String[]args){
		int[]srcArray=new int[10];
		int[]desArray=new int[10];
		for(int i=0;i<10;i++){
			srcArray[i]=i;
			desArray[i]=10+i;
		}
		System.out.println(toString(srcArray));
		System.out.println(toString(desArray));
		System.arraycopy(srcArray, 2, desArray, 3, 4);
		System.out.println(toString(desArray));
	}
	
	public static String toString(int[]array){
		StringBuilder sb=new StringBuilder();
		for(int v:array){
			sb.append(v);
			sb.append(',');
		}
		return sb.toString();
	}
	
}
