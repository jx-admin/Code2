package com.act.test;

public class Goods {
	public static enum user_role {
		NURSE, DOCTOR_INCHARGE, DOCTOR_ONDUTY, PHARMACIST
	}

	public int index;
	public String name;
	public String price;
	public String addr;
	public int count;
	public String note;
	public String mark;
	public Goods(int index, String name, String price, String addr, int count, String mark) {
		this.index = index;
		this.name = name;
		this.price = price;
		this.addr = addr;
		this.count = count;
		this.note = note;
		this.mark=mark;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int count() {
		// TODO Auto-generated method stub
		return count;
	}

	public String mark() {
		// TODO Auto-generated method stub
		return mark;
	}

	
}
