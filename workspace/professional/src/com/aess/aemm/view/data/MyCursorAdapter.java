package com.aess.aemm.view.data;

import com.aess.aemm.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class MyCursorAdapter extends CursorAdapter{

	@SuppressWarnings("unused")
	private Cursor cursor;    
    private LayoutInflater mInflater;
    
	public MyCursorAdapter(Context context, Cursor c) {
		super(context, c);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor=c;
	}

	public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor=c;
    }

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.apk_item, parent, false);
	}

}
