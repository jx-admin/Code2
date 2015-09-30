package com.act.sctc.been;

import java.io.Serializable;

public class Goods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEF=0;
	public static final int WORK_OFF=1;
	public static final int SELLING=2;
	private int status;
	private long _id;
	private long user_id;
	private long product_id;
	private int product_type;
	private int count;
	private String mark;

	public Goods(){}
	
	public Goods(long _id,
	long user_id,
	long product_id,
	int product_type,
	int count,
	String mark){
		this._id=_id;
		this.user_id=user_id;
		this.product_id=product_id;
		this.product_type=product_type;
		this.count=count;
		this.mark=mark;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public int getProduct_type() {
		return product_type;
	}
	public void setProduct_type(int product_type) {
		this.product_type = product_type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}

	@Override
	public boolean equals(Object o) {
		return product_type==((Goods)o).getProduct_type()&&product_id==((Goods)o).getProduct_id();
	}
	

}
