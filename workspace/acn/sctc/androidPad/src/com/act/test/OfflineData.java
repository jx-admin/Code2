package com.act.test;

import java.util.ArrayList;



public class OfflineData {
	
	private static ArrayList<Goods> listGoods = null;
	
	public static ArrayList<Goods> getGoodsData() {
		if (listGoods == null) {
			listGoods = new ArrayList<Goods>();
			generateGoods();
		}
		return listGoods;
	}
	
	private static ArrayList<Goods> generateGoods() {
		listGoods.add(new Goods(1, "iPhone 5S", "5288.00 元", "现货", 1,  "香槟色"));

		listGoods.add(new Goods(2, "4M 宽带", "240.00 元", "光网小区", 1, ""));
		listGoods.add(new Goods(3, "SIM卡", "0.00 元", "", 1, "18912345138"));
		
		return listGoods;
	}
}
