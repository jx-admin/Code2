package cindy.android.test.synclistview;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class TestListViewActivity extends AbstructCommonActivity 
	implements AdapterView.OnItemClickListener{
	
	ListView viewBookList;
	
	BookItemAdapter adapter;
	
	//ViewGroup listFolder;
	
	LoadStateView loadStateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		viewBookList = (ListView) findViewById(R.id.viewBookList);
		adapter = new BookItemAdapter(this,viewBookList);
		loadStateView = (LoadStateView) findViewById(R.id.downloadStatusBox);
		
		loadStateView.setOnReloadClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reload();
			}
		});
		//listFolder = (ViewGroup) getLayoutInflater().inflate(R.layout.load_more, null);
		//viewBookList.addFooterView(listFolder);
		viewBookList.setAdapter(adapter);
		viewBookList.setOnItemClickListener(this);
		reload();
	}
	
	private void reload(){
		adapter.clean();
		loadStateView.startLoad();
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(2*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loadDate();
				sendMessage(REFRESH_LIST);
				//sendMessageDely(LOAD_IMAGE, 500);
			}
		}).start();
	}
	
	public void loadDate(){
		for(int i=0;i<10;i++){
			adapter.addBook("ÍÌÊÉÐÇ¿Õ"+i, 
			"http://www.pfwx.com/bookinfo/11/11000.html", 
			"http://www.pfwx.com/files/article/image/11/11000/11000s.jpg");
			
			adapter.addBook("ÏÉÄæ"+i, 
			"http://www.pfwx.com/bookinfo/9/9760.html", 
			"http://www.pfwx.com/files/article/image/9/9760/9760s.jpg");
			
			adapter.addBook("Îä¶¯Ç¬À¤"+i, 
			"http://www.pfwx.com/bookinfo/13/13939.html", 
			"http://www.pfwx.com/files/article/image/13/13939/13939s.jpg");
			
			adapter.addBook("·²ÈËÐÞÏÉ´«"+i, 
			"http://www.pfwx.com/bookinfo/3/3237.html", 
			"http://www.pfwx.com/files/article/image/3/3237/3237s.jpg");
			
			adapter.addBook("ÕÚÌì"+i, 
			"http://www.pfwx.com/bookinfo/11/11381.html", 
			"http://www.pfwx.com/files/article/image/11/11381/11381s.jpg");		
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	private static final int REFRESH_LIST = 0x10001;
	private static final int SHOW_LOAD_STATE_VIEW = 0x10003;
	private static final int HIDE_LOAD_STATE_VIEW = 0x10004;

	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case REFRESH_LIST:
			adapter.notifyDataSetChanged();
			loadStateView.stopLoad();
			if(adapter.getCount() == 0){
				loadStateView.showEmpty();
			}
			break;
		case SHOW_LOAD_STATE_VIEW:
			loadStateView.startLoad();
			break;
		case HIDE_LOAD_STATE_VIEW:
			loadStateView.stopLoad();
			break;

		default:
			break;
		}
	}

}
