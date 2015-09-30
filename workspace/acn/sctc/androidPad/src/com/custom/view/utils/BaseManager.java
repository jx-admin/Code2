package com.custom.view.utils;

import android.content.Context;
import android.view.View;

public class BaseManager {
	protected Context context;
	protected View layout;

	public BaseManager(View layout) {
		this.layout = layout;
		context = layout.getContext();
	}

	public View getLayout() {
		return layout;
	}

}
