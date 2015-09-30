package com.aess.aemm.view.data;

import android.graphics.Bitmap;

public class AppItem {
	public AppItem(Bitmap bmp,String name,String ctg){
		icon=bmp;this.name=name;categery=ctg;
	}
	
	public String name;
	public String categery;
	public Bitmap icon;
}
