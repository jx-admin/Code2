package com.act.mbanking.utils;

public class Number_scale {
	
	public static double[] getVales(double start,double end,int maxStep,double minValue){
		int fixIndex=1;
		if(start!=0){
			fixIndex=3;
		}
		double values[];
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
			values=new double[]{start};
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
		values=new double[size];
		for(int i=0;i<size;i++){
			System.out.println(i+" "+value);
			values[i]=value;
			value+=step;
		}
		
		return values;
		
	}

}
