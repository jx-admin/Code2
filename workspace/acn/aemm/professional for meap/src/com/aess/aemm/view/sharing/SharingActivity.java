package com.aess.aemm.view.sharing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.update.net.FileDownload;
import com.aess.aemm.update.net.NetExcutor;
import com.aess.aemm.update.net.NetFlag;
import com.aess.aemm.update.net.Params;
import com.aess.aemm.update.net.Result;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.msg.AccessoryActivity;
import com.aess.aemm.view.msg.MsgDetailActivity;
import com.aess.aemm.view.sharing.SharingActivity.SharingViewAdapter.GridHolder;

public class SharingActivity extends Activity {
	

	public static final String TAG = SharingActivity.class.getSimpleName();
	public static final int FLAG_DIALOG_NET_ZIRO=1;
	public static Boolean isVisiable;

	public static final int default_icon[] = new int[] {
			R.drawable.sharing_other, R.drawable.sharing_other,
			R.drawable.sharing_other, R.drawable.sharing_other,
			R.drawable.sharing_image, R.drawable.sharing_audio,
			R.drawable.sharing_video, R.drawable.sharing_other };

	private LayoutInflater mInflater;

	private String fileType[];
	private List<Sharing>[] sharingTypeList;

	HorizontalScrollView menu_sv;
	LinearLayout menu_content;
	MenuUtils mu;
	GridView content_gv;
	SharingViewAdapter sva;

	// private ViewPager myViewPager;
	// private MyPagerAdapter myAdapter;
	// private List<View> mListViews;

	private Sharing currSharing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		loadView();
		// test();
	}

	AlertDialog alertDialog = null;
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case 0:
			alertDialog = new AlertDialog.Builder(this)
					.setNegativeButton(android.R.string.ok, null)
					.setTitle(args.getString("title"))
					.setMessage(args.getShort("message")).create();
			return alertDialog;
		}
		return super.onCreateDialog(id, args);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
			case FLAG_DIALOG_NET_ZIRO:
				if(alertDialog==null){
				alertDialog = new AlertDialog.Builder(this)
				.setNegativeButton(android.R.string.ok, null)
//				.setTitle(android.R.string.dialog_alert_title)
//				.setMessage(R.string.download_fail)
				.create();
				}
				alertDialog.setTitle(android.R.string.dialog_alert_title);
				alertDialog.setMessage(this.getText(R.string.download_fail));
			return alertDialog;
		}
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		exit();
		super.onBackPressed();
	}
	
	private void exit(){
		for (int i = 0; i < Sharing.Sharings.size(); i++) {
			if (!TextUtils.isEmpty(Sharing.Sharings.get(i).mFile))
				this.deleteFile(Sharing.Sharings.get(i).mFile);
		}
		Sharing.Sharings.clear();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean sent = false;
		if (resultCode == NewsContent.MSG_SUCCESSFUL) {
			if (currSharing.mStatus != Sharing.READ) {
				currSharing.mStatus = Sharing.READ;
				sent = true;
			}
		} else if (resultCode == -3) {
			if (currSharing.mStatus != Sharing.UNPAR) {
				currSharing.mStatus = Sharing.UNPAR;
				sent = true;
			}
		}
		if (sent) {
			AutoAdress address = AutoAdress.getInstance(SharingActivity.this);
			String addr = address.getUpdateURL();
			Log.d(TAG, "sharing status change submit-> " + addr);
			if (null != addr) {
				String lgInfo = DomXmlBuilder.buildInfo(SharingActivity.this,
						false, DomXmlBuilder.SHARINGSTATUS, currSharing);
				Params param = new Params();
				param.setUrl(addr);
				param.setData(lgInfo);
				param.setFlag(currSharing);
				param.setContext(SharingActivity.this);
				param.setMessage(mHandler.obtainMessage(SHARINGSTATUS));
				new NetExcutor(SharingActivity.this, null).execute(param);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	@Override
	protected void onResume() {
		isVisiable=true;
		ViewUtils.cancelNotification(this);
		super.onResume();
	}
	
	

	@Override
	protected void onPause() {
		isVisiable=false;
		super.onPause();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		fileType = this.getResources().getStringArray(R.array.file_type);
		if (fileType.length > 0) {
			sharingTypeList = new ArrayList[fileType.length];
			loadDate();
		}
	}

	private void loadDate() {
		for (int i = sharingTypeList.length - 1; i >= 0; i--) {
			if (sharingTypeList[i] == null) {
				sharingTypeList[i] = new ArrayList<Sharing>();
			} else {
				sharingTypeList[i].clear();
			}
		}
		sharingsRequest(this);
	}

	private void invalueDate() {
		content_gv.invalidateViews();
		// for(int i=0;i<fileType.length;i++){
		// ((GridView)mListViews.get(i)).invalidateViews();
		// }
	}

	private void sharingsRequest(Context mContext) {
		Log.i(TAG, "start sharingsRequest ...");
		String url = AutoAdress.getInstance(mContext).updateUrl();
		String upInfo = DomXmlBuilder.buildInfo(mContext, false,
				DomXmlBuilder.SHARINGSREQUEST, null);
		Params param = new Params();
		param.setUrl(url);
		param.setData(upInfo);
		param.setContext(mContext);
		param.setMessage(mHandler.obtainMessage(SHARINGLIST_REQUEST));
		new NetExcutor(this, null).execute(param);
	}

	public void downLoadSharing(Context mContext, Sharing sharing) {
		final String TAG = "downLoadSharing Method";
		Log.i(TAG, "start downLoadSharing ...");
		Params param = new Params();
		param.setUrl(sharing.mDownload_url);
		param.setData(null);
		param.setContext(mContext);
		param.setMessage(mHandler.obtainMessage(SHARING_GET_URL));
		param.setFlag(sharing);
		new NetExcutor(mContext, null).execute(param);
	}

	private void loadView() {
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.sharing_layout);

		findViewById(R.id.update_btn).setOnClickListener(onClickListener);
		menu_sv = (HorizontalScrollView) findViewById(R.id.menu_sv);
		menu_content = (LinearLayout) findViewById(R.id.menu_lin);
		content_gv = (GridView) findViewById(R.id.content_gv);

		// myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
		// myViewPager.setHorizontalFadingEdgeEnabled(false);

		mu = new MenuUtils();
		mu.initItem(this, menu_content, fileType, onClickListener);
		mu.setCurrent(0);
		sva = new SharingViewAdapter(this, sharingTypeList[0]);
		content_gv.setAdapter(sva);

		// myAdapter = new MyPagerAdapter();
		// mListViews = new ArrayList<View>();
		// for(int i=0;i<fileType.length;i++){
		// GridView gv=(GridView) mInflater.inflate(R.layout.sharing_view,
		// null);
		// SharingViewAdapter sva=new
		// SharingViewAdapter(this,sharingTypeList[i]);
		// gv.setAdapter(sva);
		// mListViews.add(gv);
		// }
		// myViewPager.setAdapter(myAdapter);
		// //初始化当前显示的view
		// myViewPager.setCurrentItem(0);
		// myViewPager.setOnPageChangeListener(mPageChangeListener);

	}

	private void readFile(Sharing sharing) {
		currSharing = sharing;
		AccessoryActivity.start(SharingActivity.this, sharing.mMime,
				Uri.parse(getFilesDir() + "/" + sharing.mFile), sharing.mName,
				1,SharingActivity.class);
	}


	public void testNet() {
		Params mParams = new Params();
		NetExcutor mNetExcutor = new NetExcutor(this, null);
		mNetExcutor.execute(mParams);
	}

	public static final int SHARINGLIST_REQUEST = 1, SHARING_GET_URL = 2,
			SHARING_DOWNLOAD = 3, SHARINGSTATUS = 4;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHARINGLIST_REQUEST:
				new UpdateXmlParser(SharingActivity.this).parseXml(
						SharingActivity.this,
						(InputStream) ((Result) msg.obj).getData(), null,
						DomXmlBuilder.SHARINGSREQUEST);
				Sharing sharing;

				// test
//				for (int i = 0; i < 200; i++) {
//					Sharing s = new Sharing();
//					s.mName = "loadDate" + i;
//					s.mCommandId = "123";
//					s.mFileDownload_url = "http://www.baidu.com";
//					s.mStatus = i % 2;
//					s.mFileType = i % 7;
//					s.mFile="test123.pdf";
//					s.mMime=AccessoryActivity.DOC_PDF;
//					Sharing.Sharings.add(s);
//				}// end

				for (int i = 0; i < Sharing.Sharings.size(); i++) {
					sharing = Sharing.Sharings.get(i);
					if (sharing.mFileType < 0
							|| sharing.mFileType > sharingTypeList.length - 1) {
						sharing.mFileType = sharingTypeList.length - 1;
					}
					sharingTypeList[sharing.mFileType].add(sharing);
				}
				invalueDate();
				break;
			case SHARING_GET_URL:
				sharing = (Sharing) ((Result) msg.obj).getFlag();
				new UpdateXmlParser(SharingActivity.this).parseXml(
						SharingActivity.this,
						(InputStream) ((Result) msg.obj).getData(), sharing,
						DomXmlBuilder.SHARINGDOWNLOADREQUEST);
				if (TextUtils.isEmpty(sharing.mCommandId)) {// old
					Sharing.Sharings.remove(sharing);
					sharingTypeList[sharing.mFileType].remove(sharing);
					AlertDialog alertDialog = new AlertDialog.Builder(
							SharingActivity.this)
							.setNegativeButton(android.R.string.ok, null)
							.setTitle(android.R.string.dialog_alert_title)
							.setMessage(
									String.format(SharingActivity.this
											.getString(R.string.sharing_old),
											sharing.mName)).create();
					alertDialog.show();

					sharing.isDownloading = false;
				} else if (TextUtils.isEmpty(sharing.mFileDownload_url)) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							SharingActivity.this)
							.setNegativeButton(android.R.string.ok, null)
							.setTitle(android.R.string.dialog_alert_title)
							.setMessage(R.string.address_parse_error).create();
					alertDialog.show();

					sharing.isDownloading = false;
				} else {
					if (sharing.tag.mDownload_pb.getVisibility() != View.VISIBLE) {
						sharing.tag.mDownload_pb.setVisibility(View.VISIBLE);
						sharing.tag.mDownload_pb.setProgress(0);
						Log.d(TAG, "pb is show");
					}
					if (sharing.mFile == null) {
						sharing.mFile = sharing.mName + sharing.mCommandId;
					}
					FileDownload fd = new FileDownload(SharingActivity.this,
							sharing.mFileDownload_url, sharing.mFile,
							obtainMessage(SHARING_DOWNLOAD),
							sharing.tag.mDownload_pb);
					fd.flag = sharing;
					fd.start();
				}
				break;
			case SHARING_DOWNLOAD:
				FileDownload fd = (FileDownload) msg.obj;
				sharing = (Sharing) fd.flag;
				if (msg.arg1 == NetFlag.FLAG_DOWNLOAD_ING) {
					sharing.tag.mDownload_pb.setMax(fd.getContentLength());
					sharing.tag.mDownload_pb.setProgress(fd.getReceivedBytes());
					// ((ProgressBar)fd.vFlag).setMax(fd.getContentLength());
					// ((ProgressBar)fd.vFlag).setProgress(fd.getReceivedBytes());
					Log.d(TAG,
							fd.getContentLength() + "downloading:"
									+ fd.getReceivedBytes());
				} else if (msg.arg1 == NetFlag.FLAG_DOWNLOAD_SUCCESS) {
					sharing.tag.mDownload_pb.setVisibility(View.GONE);
					readFile(sharing);

					sharing.isDownloading = false;
				} else if(msg.arg1==NetFlag.FLAG_DOWNLOAD_ZERO){
					sharing.isDownloading = false;
					SharingActivity.this.showDialog(SharingActivity.FLAG_DIALOG_NET_ZIRO);
				}else {
					sharing.isDownloading = false;
					sharing.tag.mDownload_pb.setVisibility(View.GONE);
					Toast.makeText(SharingActivity.this, "doload failed",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case SHARINGSTATUS:
				if (new UpdateXmlParser(SharingActivity.this)
						.parseResult((InputStream) ((Result) msg.obj).getData()) == 1) {
					invalueDate();
				} else {
					currSharing.mStatus = Sharing.DEFAULT;
				}

				break;
			}
			super.handleMessage(msg);
		}

	};
	// 单个菜单事件
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.update_btn) {
				loadDate();
			} else if (v instanceof TextView) {
				mu.setCurrent(v);
				// myViewPager.setCurrentItem((Integer)v.getTag(),true);
				sva.setDate(sharingTypeList[(Integer) v.getTag()]);
//				invalueDate();
				content_gv.setAdapter(sva);
				Log.i("SlideMenu", "width：" + v.getWidth() + "height："
						+ v.getHeight() + "sx:" + v.getScrollX() + "sy:"
						+ v.getScrollY());

				// 点击菜单时改变内容
				// Toast.makeText(v.getContext(), "select"+v.getTag(),
				// 0).show();
			} else if (v instanceof ImageView) {
				Sharing sharing = (Sharing) v.getTag();
				Toast.makeText(v.getContext(), sharing.mName, 0).show();
				if (sharing.isDownloading) {
					return;
				} else {
					sharing.isDownloading = true;
				}
				boolean load = false;
				if (!TextUtils.isEmpty(sharing.mFile)) {
					try {
						FileInputStream fis = SharingActivity.this
								.openFileInput(sharing.mFile);
						if (fis != null&&fis.available()>0) {
							load = true;
							fis.close();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (load) {
					readFile(sharing);
					sharing.isDownloading = false;
				} else {
					downLoadSharing(SharingActivity.this, sharing);
					sharing.isDownloading = true;
				}
			}
		}
	};

	// OnPageChangeListener mPageChangeListener=new OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// // Toast.makeText(SharingActivity.this, "onPageSelected - " +
	// arg0,Toast.LENGTH_SHORT).show();
	// mu.setCurrent(arg0);
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// // Toast.makeText(SharingActivity.this, "onPageScrolled - " +
	// arg0,Toast.LENGTH_SHORT).show();
	// Log.d("k", "onPageScrolled - " + arg0);
	// //从1到2滑动，在1滑动前调用
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// // Toast.makeText(SharingActivity.this, "onPageScrollStateChanged - " +
	// arg0,Toast.LENGTH_SHORT).show();
	// Log.d("k", "onPageScrollStateChanged - " + arg0);
	// //状态有三个0空闲，1是增在滑行中，2目标加载完毕
	// /**
	// * Indicates that the pager is in an idle, settled state. The current page
	// * is fully in view and no animation is in progress.
	// */
	// //public static final int SCROLL_STATE_IDLE = 0;
	// /**
	// * Indicates that the pager is currently being dragged by the user.
	// */
	// //public static final int SCROLL_STATE_DRAGGING = 1;
	// /**
	// * Indicates that the pager is in the process of settling to a final
	// position.
	// */
	// //public static final int SCROLL_STATE_SETTLING = 2;
	//
	// }
	// };

	public static UpdateResult sharingResponse(Context mContext, Sharing sharing) {
		final String TAG = "sharingResponse Method";
		Log.i(TAG, "start sharingResponse ...");

		String url = AutoAdress.getInstance(mContext).updateUrl();

		if (null == url) {
			Log.d(TAG, "Url == null");
			return null;
		}

		String upInfo = DomXmlBuilder.buildInfo(mContext, false,
				DomXmlBuilder.SHARINGRESPONSE, sharing);

		Log.d(TAG, "XmlBuilder.buildInfo->" + upInfo);
		if (TextUtils.isEmpty(upInfo)) {
			return null;
		}

		InputStream upResult = HttpHelp.aemmHttpPost(mContext, url, upInfo,
				"sharing.txt");

		if (null == upResult) {// response failed
			Log.d(TAG, "aemmHttpPost == null");
			return null;
		}
		try {
			upResult.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public class SharingViewAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private List<Sharing> list;
		protected LayoutInflater mInflater;

		public SharingViewAdapter(Context c, List<Sharing> list) {
			mContext = c;
			this.list = list;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setDate(List<Sharing> list) {
			this.list = list;
		}

		// 获取图片的个数
		public int getCount() {
			return list.size();
		}

		// 获取图片在库中的位置
		public Object getItem(int position) {
			return list.get(position);
		}

		// 获取图片ID
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// if (convertView == null){
			// convertView=mInflater.inflate(R.layout.sharing_item, null);
			// }
			Sharing sharing = list.get(position);
			if (sharing.tag == null) {
				GridHolder tag = new GridHolder();
				sharing.tag = tag;
				sharing.tag.view = mInflater.inflate(R.layout.sharing_item,
						null);
				////////////////////////////////////
				convertView = sharing.tag.view;
				sharing.tag.index = position;
				sharing.tag.mIcon_iv = (ImageView) convertView
						.findViewById(R.id.icon_iv);
				sharing.tag.mName_tv = (TextView) convertView
						.findViewById(R.id.name_tv);
				sharing.tag.mDownload_pb = (ProgressBar) convertView
						.findViewById(R.id.load_pb);
				sharing.tag.mStatusIcon_iv = (ImageView) convertView
						.findViewById(R.id.state_iv);
				sharing.tag.mIcon_iv.setTag(sharing);
				// sharing.tag.mIcon_iv.setOnClickListener(sharingLinstener);
				sharing.tag.mIcon_iv.setOnClickListener(onClickListener);
				// sharing.tag.data=sharing;
			}
			
			if (sharing.mIcon != null) {
				sharing.tag.mIcon_iv.setImageBitmap(sharing.mIcon);
			} else {
				sharing.tag.mIcon_iv
				.setImageResource(default_icon[sharing.mFileType]);
			}
			sharing.tag.mName_tv.setText(sharing.mName);
			if (sharing.mStatus == Sharing.READ) {
				sharing.tag.mStatusIcon_iv.setVisibility(View.VISIBLE);
				// tag.mStatusIcon_iv.setImageResource(R.drawable.sharing_state_saw);
			} else {
				sharing.tag.mStatusIcon_iv.setVisibility(View.GONE);
			}
			
			return sharing.tag.view;
		}

		public class GridHolder {
			public int index = -1;
			public ImageView mIcon_iv;
			public ImageView mStatusIcon_iv;
			public ProgressBar mDownload_pb;
			public TextView mName_tv;
			public Sharing data;
			public View view;
		}
	}

	// private class MyPagerAdapter extends PagerAdapter{
	//
	// @Override
	// public void destroyItem(View arg0, int arg1, Object arg2) {
	// Log.d("k", "destroyItem");
	// ((ViewPager) arg0).removeView(mListViews.get(arg1));
	// }
	//
	// @Override
	// public void finishUpdate(View arg0) {
	// Log.d("k", "finishUpdate");
	// }
	//
	// @Override
	// public int getCount() {
	// Log.d("k", "getCount");
	// return mListViews.size();
	// }
	//
	// @Override
	// public Object instantiateItem(View arg0, int arg1) {
	// Log.d("k", "instantiateItem");
	// ((ViewPager) arg0).addView(mListViews.get(arg1),0);
	// return mListViews.get(arg1);
	// }
	//
	// @Override
	// public boolean isViewFromObject(View arg0, Object arg1) {
	// Log.d("k", "isViewFromObject");
	// return arg0==(arg1);
	// }
	//
	// @Override
	// public void restoreState(Parcelable arg0, ClassLoader arg1) {
	// Log.d("k", "restoreState");
	// }
	//
	// @Override
	// public Parcelable saveState() {
	// Log.d("k", "saveState");
	// return null;
	// }
	//
	// @Override
	// public void startUpdate(View arg0) {
	// Log.d("k", "startUpdate");
	// }
	//
	// }

}
