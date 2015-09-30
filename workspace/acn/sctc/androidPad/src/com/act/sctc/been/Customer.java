package com.act.sctc.been;

public class Customer {

	private long _id;
	private String name;
	private String phone;
	private String licence;
	private String address;
	public Customer(){}
	public Customer(long _id,String name,String phone,String licence,String address){
		this._id=_id;
		this.name=name;
		this.phone=phone;
		this.licence=licence;
		this.address=address;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
