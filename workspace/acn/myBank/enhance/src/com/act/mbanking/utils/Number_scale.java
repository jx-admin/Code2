package com.act.mbanking.utils;

import java.util.ArrayList;
import java.util.List;

public class Number_scale {
	
	public static List<Double> getVales(double start,double end,int maxStep,double minValue){
		int fixIndex=1;
		if(start!=0){
			fixIndex=2;
		}
		List<Double> values;
		double total=end-start;
		double step=total/(maxStep-fixIndex);
//		System.out.println("start "+start+" end "+end+" total "+total+" "+" step "+step);
		if(step!=0&&step<minValue){
			step=minValue;
		}
//		System.out.println(" step "+step);
		double pow=0;
		double value=0;
		if(step==0){
			values=new ArrayList<Double>(1);
			values.add(start);
//			System.out.println("s "+start);
			return values;
		
		}else if(step>1){
			pow=(int) Math.pow(10,String.valueOf((int)step).length()-1);
//			System.out.println(" pow "+pow);
			step+=pow-step%pow;
		}else if(step<1){
			pow=0.1;
			while(step/pow<1){
				pow*=0.1d;
			}
//			System.out.println(" pow "+pow);
			step+=pow-step%pow;
		}
		if(start!=0){
			value=start-step;
			value-=end%step;
		}
		value-=value%pow;
		int size=(int) ((end-start)/step)+fixIndex+1;
//		System.out.println("size "+size+" step "+step);
			values=new ArrayList<Double>(size+1);
		boolean zero=false;
		for(int i=0;i<size;i++){
			System.out.println(i+" "+value);
			if(value==0){
				zero=true;
			}else if(!zero&&value>0){
				values.add(0d);
			}
			values.add(value);
			value+=step;
		}
		
		return values;
		
	}
	public static double[] getValesWithZero(double start,double end,int maxStep,double minValue){
		if(start>0){
			start=0;
		}
		if(end<0){
			end=0;
		}
		int fixIndex=0;
		if(start<0){
			++fixIndex;
		}
		if(end>0){
			++fixIndex;
		}
		double values[];
		double total=end-start;
		double step=total/(maxStep-fixIndex);
		System.out.println("total: "+total+" ["+start+" , "+end+"]\n"+maxStep+" * "+step);
		
		if(step==0){
			values=new double[]{start};
			System.out.println("s "+start);
			return values;
		
		}
		
		if(step<minValue){
			step=minValue;
		}
		System.out.println("step "+step);
		
		double pow=0;
		double value=0;
		if(step>1){
			pow=(int) Math.pow(10,String.valueOf((int)step).length()-1);
		}else if(step<1){
			pow=0.1;
			while(step/pow<1){
				pow*=0.1d;
			}
			
		}
		System.out.println(" pow "+pow);
		double excess=step%pow;
		if(excess!=0){
			double lack=pow-excess;
			step+=lack;
		}
		int left=(int)Math.ceil(Math.abs(start)/step);
		int right=(int)Math.ceil(end/step);
		System.out.println("left "+left+" right "+right+" size "+(right+left)+" step "+step);
		values=new double[right+left+1];
		for(int i=0;i<=left||i<=right;i++){
			if(i>0&&i<=left){
				values[left-i]=-value;
			}
			if(i<=right){
				values[left+i]=value;
			}
			value+=step;
		}
		return values;
	}

}
