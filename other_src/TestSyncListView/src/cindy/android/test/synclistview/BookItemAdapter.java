package cindy.android.test.synclistview;

import java.util.Vector;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BookItemAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private Context mContext;
	private Vector<BookModel> mModels = new Vector<BookModel>();
	private ListView mListView;
	SyncImageLoader syncImageLoader;
	
	public BookItemAdapter(Context context,ListView listView){
		mInflater = LayoutInflater.from(context);
		syncImageLoader = new SyncImageLoader();
		mContext = context;
		mListView = listView;
		
		mListView.setOnScrollListener(onScrollListener);
	}

	
	public void addBook(String book_name,String out_book_url,String out_book_pic){
		BookModel model = new BookModel();
		model.book_name =book_name;
		model.out_book_url = out_book_url;
		model.out_book_pic = out_book_pic;
		mModels.add(model);
	}
	
	public void clean(){
		mModels.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mModels.size();
	}

	@Override
	public Object getItem(int position) {
		if(position >= getCount()){
			return null;
		}
		return mModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.book_item_adapter, null);
		}
		BookModel model = mModels.get(position);
		convertView.setTag(position);
		ImageView iv = (ImageView) convertView.findViewById(R.id.sItemIcon);
		TextView sItemTitle =  (TextView) convertView.findViewById(R.id.sItemTitle);
		TextView sItemInfo =  (TextView) convertView.findViewById(R.id.sItemInfo);
		sItemTitle.setText(model.book_name);
		sItemInfo.setText(model.out_book_url);
		iv.setBackgroundResource(R.drawable.rc_item_bg);
		syncImageLoader.loadImage(position,model.out_book_pic,imageLoadListener);
		return  convertView;
	}

	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener(){

		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			//BookModel model = (BookModel) getItem(t);
			View view = mListView.findViewWithTag(t);
			if(view != null){
				ImageView iv = (ImageView) view.findViewById(R.id.sItemIcon);
				iv.setBackgroundDrawable(drawable);
			}
		}
		@Override
		public void onError(Integer t) {
			BookModel model = (BookModel) getItem(t);
			View view = mListView.findViewWithTag(model);
			if(view != null){
				ImageView iv = (ImageView) view.findViewById(R.id.sItemIcon);
				iv.setBackgroundResource(R.drawable.rc_item_bg);
			}
		}
		
	};
	
	public void loadImage(){
		int start = mListView.getFirstVisiblePosition();
		int end =mListView.getLastVisiblePosition();
		if(end >= getCount()){
			end = getCount() -1;
		}
		syncImageLoader.setLoadLimit(start, end);
		syncImageLoader.unlock();
	}
	
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					DebugUtil.debug("SCROLL_STATE_FLING");
					syncImageLoader.lock();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					DebugUtil.debug("SCROLL_STATE_IDLE");
					loadImage();
					//loadImage();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					syncImageLoader.lock();
					break;
	
				default:
					break;
			}
			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
	};
}
