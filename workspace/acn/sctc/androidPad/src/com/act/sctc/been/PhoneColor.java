package com.act.sctc.been;


public class PhoneColor extends ProductColor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String picids[];
	public void setPicId(String picids[]){
		this.picids=picids;
	}
	public String[] getPicIds(){
		return picids;
	}
	
	public void writeObject(){
		
	}
	
}
