package com.android.study.abc.examples.view.listview;

import java.util.Arrays;
import java.util.List;

import com.android.study.abc.examples.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DoubleColorListView extends Activity {

	ListView lv_DoubleColorListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doublecolorlistview);
		lv_DoubleColorListView=(ListView) findViewById(R.id.lv_doublecolorlistview);
		String[]strarr={"北京市(京)","天津市(津)","河北省(冀)","山西省(晋)","内蒙古自治区(内蒙古)","辽宁省(辽)","吉林省(吉)","黑龙江省(黑)","上海市(沪)","江苏省(苏)","浙江省(浙)","安徽省(皖)","福建省(闽)","江西省(赣)","山东省(鲁)","河南省(豫)","湖北省(鄂)","湖南省(湘)","广东省(粤)","广西壮族自治区(桂)","海南省(琼)","重庆市(渝)","四川省(川、蜀)","贵州省(黔、贵)","云南省(滇、云)","西藏自治区(藏)","陕西省(陕、秦)","甘肃省(甘、陇)","青海省(青)","宁夏回族自治区(宁)","台湾省(台)","新疆维吾尔自治区(新)","香港特别行政区(港)","澳门特别行政区(澳)"};
		List list=Arrays.asList(strarr);
		lv_DoubleColorListView.setAdapter(new DoubleColorListViewAdapter(this, list));
	}
}
/**
 * 技术通报标题列表适配器
 * 
 * @author Administrator
 * 
 */
class DoubleColorListViewAdapter extends BaseAdapter {
	private List list;
	LayoutInflater layoutInflater;
	Context context;
	
	public void setList(List l){
		list=l;
	}

	public DoubleColorListViewAdapter(Context context, List list) {
		this.list = list;
		this.context=context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String str = (String) list.get(position);
		LinearLayout linearLayout;
		if (convertView == null) {
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_doublecolorlistview, null);
		} else {
			linearLayout = (LinearLayout) convertView;
		}
		TextView tvListName= ((TextView) linearLayout.findViewById(R.id.TextView01));
		if (str != null)
			tvListName.setText(str);
		return linearLayout;
	}
}
