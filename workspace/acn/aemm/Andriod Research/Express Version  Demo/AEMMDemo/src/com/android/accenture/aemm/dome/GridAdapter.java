package com.android.accenture.aemm.dome;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class GridAdapter extends BaseAdapter{
	class GridHolder {
		int index=-1;
		ImageView appImage;
		ImageView editImage;
		ProgressBar progressBar;
		TextView appName;
	}
}
