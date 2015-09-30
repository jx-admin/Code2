package cn.oneMin.demo.slideListView;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public abstract class SlideAdapter<T> extends BaseAdapter{
	Context context = null;
	LayoutInflater mInflater = null;
	List<T> list;
	SlideAdapterListener listener = null;

	public interface SlideAdapterListener {
		public void onItemDeleted(int position);
	}

	public void setListener(SlideAdapterListener listener) {
		this.listener = listener;
	}

	public SlideAdapter(Context context) {
		this.context = context;
		mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list == null ? 10 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup viewGroup) {
//		if (convertView == null) {
//			convertView = mInflater.inflate( R.layout.bper_recent_list_item_model, null);
//		}
//
//		return convertView;
//	}

	public void setDatas(List<T> list) {
		this.list = list;
	}

	public List<T> getDatas() {
		return this.list;
	}

//	@Override
//	public void onClick(View v) {
//		try {
//			EditText account_ev = (EditText) v.getTag();
//			int i = 0;
//			if (v.getId() == R.id._) {
//				try {
//					i = Integer.parseInt(account_ev.getText().toString());
//				} catch (Exception e) {
//				}
//				if (i > 0)
//					i--;
//				// account_ev.setText(Integer.toString(i));
//			} else if (v.getId() == R.id._p) {
//				try {
//					i = Integer.parseInt(account_ev.getText().toString());
//				} catch (Exception e) {
//				}
//				i++;
//				// account_ev.setText(Integer.toString(i));
//			}
//			int index = (Integer) account_ev.getTag();
//			this.notifyDataSetChanged();
//		} catch (Exception e) {
//			// do nothing
//		}
//	}

	public abstract void onHideClick(int position);
//	{
//		if (position >= 0 && list != null && list.size() >= position) {
//			list.remove(position);
//			if (listener != null) {
//				listener.onItemDeleted(position);
//			}
//		}
//	}
	public abstract void onItemClick(View view,int position);
//	{
//		Toast.makeText(view.getContext(), "clikItem"+position, 0).show();
//	}

}
