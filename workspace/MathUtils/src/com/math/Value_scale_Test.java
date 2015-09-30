package com.math;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Value_scale_Test {

	public static void main(String args[]){
		testgetValesWithZero();
		
	}
	public static void testgetValesWithZero(){
		long a,b;
		double datas[];


		a=-10;b=150;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();

		a=-100;b=2;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();

		a=-100;b=2;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();
		
		a=0;b=150;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();

		a=1;b=2;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();
		

		a=0;b=1;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();
		
		a=123;b=232323242;
		System.out.println("from "+a+" to "+b);
		datas=Number_scale.getValesWithZero(a,b,10,0);
		printArray(datas);System.out.println();
		
	}
	public static void testGetListTime_scale(){
		long a,b;
		Calendar fromCalendar=Calendar.getInstance();
		long datas[];
		
		a=123;b=232323242;
		System.out.println("from "+a+" to "+b);
		datas=Value_scale.getListTime_scale(a,b,10,false,7);
		printArray(datas);System.out.println();

		a=0;b=150;
		System.out.println("from "+a+" to "+b);
		datas=Value_scale.getListTime_scale(a,b,10,false,7);
		printArray(datas);System.out.println();

		a=1;b=2;
		System.out.println("from "+a+" to "+b);
		datas=Value_scale.getListTime_scale(a,b,10,false,7);
		printArray(datas);System.out.println();
		

		a=0;b=1;
		System.out.println("from "+a+" to "+b);
		datas=Value_scale.getListTime_scale(a,b,10,false,7);
		printArray(datas);System.out.println();
		
	}
	
	public static void printArray(double datas[]){
		if(datas==null){
			System.out.println("datas is null");
			return;
		}else if(datas.length==0){
			System.out.println("datas is empty");
			return;
		}
		for(int i=0;i<datas.length;i++){
			System.out.print(" , date:"+datas[i]);
		}
	}
	
	public static void printArray(long datas[]){
		if(datas==null){
			System.out.println("datas is null");
			return;
		}
		for(int i=0;i<datas.length;i++){
			System.out.println("date:"+datas[i]);
		}
	}
	

}
