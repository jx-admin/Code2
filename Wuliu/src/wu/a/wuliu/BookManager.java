package wu.a.wuliu;

import java.util.ArrayList;
import java.util.List;

import wu.a.wuliu.model.Book;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import baidumapsdk.demo.R;

public class BookManager implements OnClickListener {
	
	private View view;
	private Context context;
	
	ListView bookList;
	private List<Book>datas;
	private LayoutInflater lif;
	private BookListAdapter mBookListAdapter;
	
	public BookManager(Context context){
		this.context=context;
		lif=LayoutInflater.from(context);
		view=LayoutInflater.from(context).inflate(R.layout.book_manager_main, null);
		
		bookList=(ListView) view.findViewById(R.id.book_list);
		mBookListAdapter=new BookListAdapter();
		loadData();
		bookList.setAdapter(mBookListAdapter);
	}
	
	private void loadData(){
		datas=new ArrayList<Book>();
		for(int i=0;i<10;i++){
			Book b=new Book();
			datas.add(b);
		}
		mBookListAdapter.notifyDataSetInvalidated();
	}
	
	public View getView(){
		return view;
	}
	
	public String getTitle(){
		return "下单";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.feedback_tv:
//			context.startActivity(new Intent(context,FeedBackActivity.class));
//			break;
		case R.id.cargo_goods:
			context.startActivity(new Intent(context,CargoGoodsActivity.class));
			break;
		case R.id.cargo_home:
			context.startActivity(new Intent(context,CargoHomeActivity.class));
			
			break;

		default:
			break;
		}
	}
	
	class BookListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return datas==null?0:datas.size();
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
			if(convertView==null){
				convertView=lif.inflate(R.layout.book_list_item, null,false);
			}
			return convertView;
		}
		
	}

}
