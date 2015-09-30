package cn.oneMin.demo.slideListView;

//import java.util.List;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.wu.slidelistview.R;
import com.nineoldandroids.view.ViewHelper;

public class SlideAdapter extends BaseAdapter{
	private Context context=null;
	private LayoutInflater mInflater=null;
	private int count=0;
	private List<Info> list;	
	private Map<Integer,Boolean> mapViewDelete=new HashMap<Integer,Boolean>();
	
	public SlideAdapter(Context context){
		this.context=context;
		mInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		if(view==null)
		{			
		    view=mInflater.inflate(R.layout.slidelist_items, viewGroup, false);		   					
		}
		final View showView=view.findViewById(R.id.show_item);
		final View hideView=view.findViewById(R.id.hide_item);
		ViewHelper.setTranslationX(showView, 0);
		ViewHelper.setTranslationX(hideView, 0);
		holder.linkman=(TextView) view.findViewById(R.id.linkman);
		holder.linkman.setText(list.get(position).info1);
		holder.phone=(TextView) view.findViewById(R.id.phone);
		holder.phone.setText(list.get(position).info2);	
		view.setTag(holder);
		
		
		return view;
	}
	
	private class ViewHolder  
    {  
        public TextView linkman;  
        public TextView phone;
    } 
	
	public void setInfoList(List<Info> list){
		this.list=list;
		setInfoList();
	}
	private void setInfoList(){
		count=list.size();
		for(int i=0;i<count;i++){
			mapViewDelete.put(i,false);
		}
	}
	
	public List<Info> getInfoList(){
		return this.list;
	}

}
