package cn.oneMin.demo.slideListView;

//import java.util.List;

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

import com.act.sctc.R;
import com.act.sctc.been.Goods;
import com.act.sctc.db.SqlUtils;

public class SlideAdapter<T> extends BaseAdapter implements OnClickListener {
	private Context context = null;
	private LayoutInflater mInflater = null;
	private List<T> list;
//	Cursor mCursor;

	public SlideAdapter(Context context) {
		this.context = context;
		mInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.shopping_cart_item, null);
		}
		TextView textView1 = (TextView) convertView
				.findViewById(R.id.textView1);
		TextView textView2 = (TextView) convertView
				.findViewById(R.id.textView2);
		TextView textView3 = (TextView) convertView
				.findViewById(R.id.textView3);
		EditText editTextView = (EditText) convertView
				.findViewById(R.id.account_ev);
		TextView _P = (TextView) convertView.findViewById(R.id._p);
		_P.setOnClickListener(this);
		_P.setTag(editTextView);
		_P = (TextView) convertView.findViewById(R.id._);
		_P.setOnClickListener(this);
		_P.setTag(editTextView);
		TextView textView7 = (TextView) convertView
				.findViewById(R.id.textView7);

		Goods goods = (Goods) list.get(position);
		
//		if(mCursor!=null&&mCursor.moveToPosition(position)){
//			int index = mGoodsData.get(position).getIndex();
			textView1.setText(Integer.toString(position));
			Cursor nameCursor=SqlUtils.getProductNameandPrice(context, goods.getProduct_type(),goods.getProduct_id());
			if(nameCursor!=null){
				if(nameCursor.moveToFirst()){
					textView2.setText(nameCursor.getString(1));
					textView3.setText(nameCursor.getString(2));
				}
				nameCursor.close();
			}
			editTextView.setText(Integer.toString(goods.getCount()));
			textView7.setText(goods.getMark());
//			}
			
		return convertView;
	}

	public void setDatas(List<T> list) {
		this.list = list;
	}

	public List<T>  getDatas() {
		return this.list;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id._) {

			EditText account_ev = (EditText) v.getTag();
			int i = 0;
			try {
				i = Integer.parseInt(account_ev.getText().toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (i > 0)
				i--;
			account_ev.setText(Integer.toString(i));
		} else if (v.getId() == R.id._p) {

			EditText account_ev = (EditText) v.getTag();
			int i = 0;
			try {
				i = Integer.parseInt(account_ev.getText().toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			i++;
			account_ev.setText(Integer.toString(i));
		}
	}

	public void onDelete(int position) {
		if(position>=0&&list!=null&&list.size()>=position){
			list.remove(position);
		}
	}

}
