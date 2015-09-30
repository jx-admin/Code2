package wu.a.wuliu;

import java.util.ArrayList;
import java.util.List;

import wu.a.activity.TitleFooterActivity;
import wu.a.wuliu.model.Book;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import baidumapsdk.demo.R;

import com.droid.Activity01;
import com.droid.City;

public class BookManagerActivity extends TitleFooterActivity implements OnClickListener {
	ListView bookList;
	private List<Book>datas;
	private LayoutInflater lif;
	private BookListAdapter mBookListAdapter;
	public static void start(Activity activity){
		activity.startActivity(new Intent(activity,BookManagerActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lif=LayoutInflater.from(this);
		setContentLayout(R.layout.book_manager_main);
//		setMenuStatus(this,MENU_BOOK_MANAGER, false);
		setTitleText("订单");
		bookList=(ListView) findViewById(R.id.book_list);
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
	
	@Override
	public void onTitleLeftButtonClick(View v) {
//		super.onTitleLeftButtonClick(v);
		Activity01.start(this, 12);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			if(requestCode==12){
				City city=(City) data.getSerializableExtra("city");
				setTitleLeftButtonText(city.name);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		}
		// TODO Auto-generated method stub
		
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
