package com.aemm.config_demo;

import java.util.ArrayList;

public class ItemBO {
	private String name;
	private String description;
	public String getName() { return name;	}
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	// / --------------------
	public static ArrayList<ItemBO> getItems() {
		ArrayList<ItemBO> list = new ArrayList<ItemBO>();
		ItemBO item;
		
		item = new ItemBO();
		item.setName("APN Demo");
		item.setDescription("desc 1");
		list.add(item);

		item = new ItemBO();
		item.setName("VPN Demo");
		item.setDescription("desc 2");
		list.add(item);

		item = new ItemBO();
		item.setName("AppList Demo");
		item.setDescription("desc 3");
		list.add(item);

		return list;
	}
}
