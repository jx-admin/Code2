package com.act.sctc.been;

import java.io.Serializable;
import java.util.List;


public class Phone implements Serializable {
	
	public static final int BRAND_IPHONE=1;
	public static final int BRAND_SANG=2;
	public static final int BRAND_XIAOMI=3;
	public static final int BRAND_HUAWEI=4;
	public static final int BRAND_HTC=5;
	public static final int BRAND_AILIXIN=6;
	public static final int BRAND_UNKWON=0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long 	id					;
	private double	localPrice			;
	private boolean visibility			;
	
	public  String 	name				;
	public String 	icon				;
	public  String 	introduce			;
	public  int 	price				;
	public 	int 	originalPrice		;
	public  String 	amount				;
	private final 	static String category="1";
	private String 	producer;
	private int 	screenWith;
	private int 	screenHeight;
	private List<String>specials;
	private List<PhoneColor> colors;
	private int brandCategory;
	
	public Phone(){}
	
	public Phone(String name,String iconUri,String introduce,int price,int originalPrice,int brandCategory){
		this.name=name;
		this.icon=iconUri;
		this.introduce=introduce;
		this.price=price;
		this.originalPrice=originalPrice;
		this.brandCategory=brandCategory;
	}
//	public ProductPhone(String name,Uri iconUri,String introduce,int price,int originalPrice,int brandCategory){
//		this.name=name;
//		this.introduce=introduce;
//		this.price=price;
//		this.originalPrice=originalPrice;
//		this.brandCategory=brandCategory;
//	}

	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public int getScreenWith() {
		return screenWith;
	}
	public void setScreenWith(int screenWith) {
		this.screenWith = screenWith;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getOriginalPrice(){
		return originalPrice;
	}
	public void setOriginalPrice(int originalPrice){
		this.originalPrice=originalPrice;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public static String getCategory() {
		return category;
	}
	public List<String>getSpecials(){
		return specials;
	}
	public void setSpecials(List<String> specials){
		this.specials=specials;
	}
	
	public void setColor(List<PhoneColor> colors){
		this.colors=colors;
	}
	public List<PhoneColor>getColors(){
		return colors;
	}
	public void setIcon(String iconUri){
		this.icon=iconUri;
	}
	public String getIcon() {
		// TODO Auto-generated method stub
		return icon;
	}
	public int getBrandCategory() {
		return brandCategory;
	}
	public void setBrandCategory(int brandCategory) {
		this.brandCategory = brandCategory;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLocalPrice() {
		return localPrice;
	}

	public void setLocalPrice(double localPrice) {
		this.localPrice = localPrice;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
}
