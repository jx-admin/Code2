package com.act.sctc.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.act.sctc.R;
import com.act.sctc.been.SetMeal;

public class SetmealPageUtils {
	private Context context;
	private View view;
	SetMealItem siu_tl, siu_tr, siu_bl, siu_br;
	public SetMealItem[] ppItems;

	public SetmealPageUtils(List<SetMeal> datas, int type, int position, Context context) {
		this.context = context;
		iniController();
		setData(datas, type, position);
	}

	public SetmealPageUtils(int type, Context context) {
		this.context = context;
		iniController();
	}

	private void iniController() {
		view = LayoutInflater.from(context).inflate(R.layout.setmeal_page_item, null);
		view.setTag(this);
		siu_tl = (SetMealItem) view.findViewById(R.id.tl);
		siu_tl.onCreate();
		siu_tr = (SetMealItem) view.findViewById(R.id.tr);
		siu_tr.onCreate();
		siu_bl = (SetMealItem) view.findViewById(R.id.bl);
		siu_bl.onCreate();
		siu_br = (SetMealItem) view.findViewById(R.id.br);
		siu_br.onCreate();
		ppItems = new SetMealItem[] { siu_tl, siu_tr, siu_bl, siu_br };
	}

	public void setOnCheckedChangeListener(SetMealItem.OnCheckedChangeListener onCheckedChangeListener) {
		// this.onCheckedChangeListener=onCheckedChangeListener;
		siu_tl.setOnCheckedChangeListener(onCheckedChangeListener);
		siu_tr.setOnCheckedChangeListener(onCheckedChangeListener);
		siu_bl.setOnCheckedChangeListener(onCheckedChangeListener);
		siu_br.setOnCheckedChangeListener(onCheckedChangeListener);
	}

	public void setData(List<SetMeal> ls, int type, int position) {
		if (position < ls.size()) {
			siu_tl.setData(type, ls.get(position));
			siu_tl.setVisibility(View.VISIBLE);
		} else {
			siu_tl.setVisibility(View.GONE);
		}
		++position;
		if (position < ls.size()) {
			siu_tr.setData(type, ls.get(position));
			siu_tr.setVisibility(View.VISIBLE);
		} else {
			siu_tr.setVisibility(View.GONE);
		}
		++position;
		if (position < ls.size()) {
			siu_bl.setData(type, ls.get(position));
			siu_bl.setVisibility(View.VISIBLE);
		} else {
			siu_bl.setVisibility(View.GONE);
		}
		++position;
		if (position < ls.size()) {
			siu_br.setData(type, ls.get(position));
			siu_br.setVisibility(View.VISIBLE);
		} else {
			siu_br.setVisibility(View.GONE);
		}
	}

	private void iniListener() {

	}

	private void iniVariable() {

	}

	public View getView() {
		return view;
	}

}
