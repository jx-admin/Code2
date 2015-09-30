package wu.a.wuliu;

import java.util.ArrayList;
import java.util.List;

import wu.a.wuliu.model.SaleAct;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import baidumapsdk.demo.R;

/**
 * <pre>
 * 优惠活动
 * @author junxu.wang
 * @d2015年7月16日
 * </pre>
 *
 */
public class SaleManager implements OnClickListener {

	private View view;
	private Context context;

	ListView bookList;
	private List<SaleAct> datas;
	private LayoutInflater lif;
	private BookListAdapter mBookListAdapter;

	public SaleManager(Context context) {
		this.context = context;
		lif = LayoutInflater.from(context);
		view = LayoutInflater.from(context).inflate(R.layout.book_manager_main,
				null);

		bookList = (ListView) view.findViewById(R.id.book_list);
		mBookListAdapter = new BookListAdapter();
		loadData();
		bookList.setAdapter(mBookListAdapter);
	}

	private void loadData() {
		datas = new ArrayList<SaleAct>();
		for (int i = 0; i < 2; i++) {
			SaleAct b = new SaleAct();
			datas.add(b);
		}
		mBookListAdapter.notifyDataSetInvalidated();
	}

	public View getView() {
		return view;
	}

	public String getTitle() {
		return "优惠活动";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.feedback_tv:
		// context.startActivity(new Intent(context,FeedBackActivity.class));
		// break;
		case R.id.cargo_goods:
			context.startActivity(new Intent(context, CargoGoodsActivity.class));
			break;
		case R.id.cargo_home:
			context.startActivity(new Intent(context, CargoHomeActivity.class));

			break;

		default:
			break;
		}
	}

	class BookListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = lif.inflate(R.layout.sale_act_list_item, null,
						false);
			}
			return convertView;
		}

	}

}
