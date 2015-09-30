package com.aess.aemm.view;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class GridAdapter extends BaseAdapter{
	public class GridHolder {
		public int index=-1;
		public ImageView appImage;
		public ImageView editImage;
		public ProgressBar progressBar;
		public TextView appName;
		public TextView appDecrip;
		public AppItem app;
	}
}
