package com.aess.aemm.view.msg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.DownloadHandler;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.protocol.UpdateXmlParser;
import com.aess.aemm.update.net.NetExcutor;
import com.aess.aemm.update.net.Params;
import com.aess.aemm.update.net.Result;
import com.aess.aemm.view.NotificationUtils;
import com.aess.aemm.view.sharing.Sharing;
import com.aess.aemm.view.sharing.SharingActivity;

public class MsgDetailActivity extends Activity implements OnClickListener {
	public final String TAG="msgDetail";
	Attachment curAttach;
	NewsContent nc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ly = (LinearLayout) getLayoutInflater().inflate(
				R.layout.activity_msg_detail2, null);
		initWidget(ly);
		Intent i = getIntent();
		setActivity2(i);
		setContentView(ly);
		setTitle(R.string.title_activity_msg_detail);
	}

	@Override
	protected void onResume() {
		NotificationUtils.cancelNotification(this);
		super.onResume();
	}  
	
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		   if(arg0==KeyEvent.KEYCODE_BACK){
			   checkMsgStateShow();
			   return true;
		   }
		// TODO Auto-generated method stub
		return super.onKeyDown(arg0, arg1);
	}
	
	protected void onDestroy(){
		super.onDestroy();
	}

	private void setActivity2(Intent intent) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Uri uri = intent.getData();
		String ConmandId=intent.getStringExtra("ConmandId");
		Cursor cursor1 = NewsContent.queryMsgByConmandId(this, uri,ConmandId);
		nc = NewsContent.getContentByCursor(cursor1);
		
		if (null != nc) {
			if(nc.mHasAttachment>0){
				Attachment attach;
				if(nc.mAttachments==null){
					nc.mAttachments=new ArrayList<Attachment>();
					Cursor attachCursor = Attachment.getAttachments(this,nc.mCommandId);
					if (attachCursor != null && attachCursor.moveToFirst()) {
						do{
							attach = new Attachment();
							attach.restore(attachCursor);
							nc.mAttachments.add(attach);
						}while(attachCursor.moveToNext());
					}
				}
				if(nc.mAttachments.size()==1){
					attach=nc.mAttachments.get(0);
					Bitmap icon = attach.getIcon();
					if (icon != null) {
						imAccessory.setImageBitmap(icon);
					}
					if (attach.mName != null) {
						tvAccessory.setVisibility(View.VISIBLE);
						tvAccessory.setText(attach.mName);
					}
					
				}
				imAccessory.setVisibility(View.VISIBLE);
				imAccessory.setTag(nc);
				imAccessory.setOnClickListener(this);
			}

			isread = nc.mIsRead;

			int type = nc.mType;
			if (null != nc.mTypeName) {
				tvType.setText(nc.mTypeName);
			} else {
				tvType.setText(getTypeLable(type));
			}

			String info = nc.mTitile;
			if (null != info) {
				tvtle.setText(info);
				if (isread == 1) {
					tvtle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
				} else {
					tvtle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				}
			}
			
			if (null != nc.mBusName) {
				tvbus.setText(nc.mBusName);
			} else {
				tvbus.setText(getBusLable(nc.mBusType));
			}

			info = nc.mContent;
			if (null != info) {
				tvCnt.setText(info);
			}

			long infovalue = nc.mPData;
			if (0 != infovalue) {
				Date date = new Date(infovalue);
				tvDate.setText(sdf2.format(date));
			}

			if (MessageType.MSG_INFORM == type || type > MessageType.MSG_EVENT) {
				pub.setVisibility(View.GONE);
				lev.setVisibility(View.GONE);
				rag.setVisibility(View.GONE);
				rag1.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}

			if (MessageType.MSG_POST == type) {
				info = nc.mPublish;
				if (null != info) {
					tvPub.setText(info);
				}
				lev.setVisibility(View.GONE);
				rag.setVisibility(View.GONE);
				rag1.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}

			if (MessageType.MSG_PLAN == type || MessageType.MSG_EVENT == type) {
				tvRange.setText(nc.mBegin);
				tvRange1.setText(nc.mEnd);
			}

			if (MessageType.MSG_PLAN == type) {
				int value = -1;
				value = nc.mPlanState;
				if (-1 != value) {
					tvState.setText(getStateLable(value));
				}
				pub.setVisibility(View.GONE);
				lev.setVisibility(View.GONE);
			}

			if (MessageType.MSG_EVENT == type) {
				int value2 = -1;
				value2 = nc.mLevel;
				if (null != info) {
					tvLevel.setText(getLevLable(value2));
				}
				pub.setVisibility(View.GONE);
				sta.setVisibility(View.GONE);
			}
		}
//		nc.mIsRead = 1;
		nc.update(this);
	}

	interface EventState {
		public static final int stUnbegin = 0;
		public static final int stDoing = 1;
		public static final int stFinish = 2;
		public static final int stDefer = 3;
	}

	private String getStateLable(int type) {
		String alt = null;
		switch (type) {
		case EventState.stUnbegin: {
			alt = this.getString(R.string.mtenvents0);
			break;
		}
		case EventState.stDoing: {
			alt = this.getString(R.string.mtenvents1);
			break;
		}
		case EventState.stFinish: {
			alt = this.getString(R.string.mtenvents2);
			break;
		}
		case EventState.stDefer: {
			alt = this.getString(R.string.mtenvents3);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}

	private String getTypeLable(int type) {
		String alt = null;
		switch (type) {
		case MessageType.MSG_POST: {
			alt = this.getString(R.string.mtpost);
			break;
		}
		case MessageType.MSG_INFORM: {
			alt = this.getString(R.string.mtinform);
			break;
		}
		case MessageType.MSG_PLAN: {
			alt = this.getString(R.string.mtplan);
			break;
		}
		case MessageType.MSG_EVENT: {
			alt = this.getString(R.string.mtenvent);
			break;
		}
		case MessageType.MSG_CUSTOM: {
			alt = this.getString(R.string.mtother);
			break;
		}
		default: {
			alt = this.getString(R.string.mtother);
		}
		}
		return alt;
	}
	
	
	interface LevState {
		public static final int stHigh = 0;
		public static final int stNormal = 1;
		public static final int stLow = 2;
	}
	
	interface BusType {
		public static final int btNormal = 0;
		public static final int btOper = 1;
		public static final int btRes = 2;
	}
	
	private String getLevLable(int type) {
		String alt = null;
		switch (type) {
		case LevState.stHigh: {
			alt = this.getString(R.string.mtlev1);
			break;
		}
		case LevState.stNormal: {
			alt = this.getString(R.string.mtlev2);
			break;
		}
		case LevState.stLow: {
			alt = this.getString(R.string.mtlev3);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}
	
	private String getBusLable(int type) {
		String alt = null;
		switch (type) {
		case BusType.btNormal: {
			alt = this.getString(R.string.btNor);
			break;
		}
		case BusType.btOper: {
			alt = this.getString(R.string.btOpe);
			break;
		}
		case BusType.btRes: {
			alt = this.getString(R.string.btRes);
			break;
		}
		default: {
			alt = this.getString(R.string.mtunknow);
		}
		}
		return alt;
	}

	private void initWidget(View view) {
		pub = view.findViewById(R.id.mdlpub);
		lev = view.findViewById(R.id.mdllev);
		rag = view.findViewById(R.id.mdlrag);
		rag1 = view.findViewById(R.id.mdlrag1);
		sta = view.findViewById(R.id.mdlsta);
		bus = view.findViewById(R.id.mdlbus);
		
		tvtle = (TextView)view.findViewById(R.id.tmdtitle);
		tvCnt = (TextView)view.findViewById(R.id.tmdtnt);
		tvPub = (TextView)pub.findViewById(R.id.tmdpub);
		tvType = (TextView)view.findViewById(R.id.tmdtype);
		tvDate = (TextView)view.findViewById(R.id.tmddate);
		tvRange = (TextView)view.findViewById(R.id.tmdtrange);
		tvRange1 = (TextView)view.findViewById(R.id.tmdtrange1);
		tvState = (TextView)view.findViewById(R.id.tmdtstate);
		tvLevel = (TextView)view.findViewById(R.id.tmdlevel);
		tvbus = (TextView)bus.findViewById(R.id.tmdbus);
		tvAccessory=(TextView) view.findViewById(R.id.msg_tvAccessory);
		imAccessory=(ImageView) view.findViewById(R.id.msg_accessory);
		imAccessory.setOnClickListener(this);
	}

	TextView tvtle;
	TextView tvState;
	TextView tvCnt;
	TextView tvType;
	TextView tvDate;
	TextView tvPub;
	TextView tvRange;
	TextView tvRange1;
	TextView tvLevel;
	TextView tvbus;
	ImageView imAccessory;
	TextView tvAccessory;
	View pub = null;
	View lev = null;
	View rag = null;
	View rag1 = null;
	View sta = null;
	View bus = null;
	int isread = 1;
	ProgressDialog progressDialog;
	
	DownloadHandler attachHandler=new DownloadHandler(){

		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		}

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG,"handleMessage "+msg.what+":"+msg.arg2+"/"+msg.arg1);
			switch(msg.what){
			case READ_WHAT:
				if(UpdateXmlParser.parseResult((InputStream)((Result)msg.obj).getData())==1){
					nc.update(MsgDetailActivity.this);
				}else{
					nc.mState = NewsContent.MSG_UNREADED;
				}
				exit();
			break;
			case FLAG_DOWNLOAD_SUCCESS:
				AttachmentDownload ad=(AttachmentDownload)msg.obj;
				boolean fileExist=false;
				try {
					FileInputStream  fis=MsgDetailActivity.this.openFileInput(curAttach.mFile);
					if(fis!=null&&fis.available()>0){
						fileExist=true;
					}
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(fileExist){
					curAttach.mFile=ad.fileName;
					curAttach.update(MsgDetailActivity.this);
					openfile();
					if(progressDialog!=null){
						progressDialog.dismiss();
					}
				}else{
					Toast.makeText(MsgDetailActivity.this,R.string.download_fail, Toast.LENGTH_SHORT).show();
				}
			break;
			case FLAG_DOWNLOAD_ING:
				break;
			default:
				Toast.makeText(MsgDetailActivity.this, R.string.attachment_load_fail, Toast.LENGTH_SHORT).show();
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==NewsContent.MSG_SUCCESSFUL){
			curAttach.mState=NewsContent.MSG_SUCCESSFUL;
			curAttach.update(this);
		}else if(resultCode==-100){
			curAttach.mState=NewsContent.MSG_OPENFAIL;
			curAttach.update(this);
			new Thread(){
				public void run(){
					AutoAdress address = AutoAdress.getInstance(MsgDetailActivity.this);
					String addr = address.getUpdateURL();
					Log.d(TAG, "submit conment to -> " + addr);
					if (null != addr) {
						String lgInfo = DomXmlBuilder.buildInfo(MsgDetailActivity.this,false, DomXmlBuilder.ATTACHMENTRES,curAttach);
						Log.d(TAG, "sendAttachment XmlBuilder.buildInfo == " + lgInfo);
						InputStream upResult = HttpHelp.aemmHttpPost(MsgDetailActivity.this, addr, lgInfo,"/sdcard/comment.txt");
					}
				}
			}.start();
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View view) {
		if (nc.mAttachments.size() == 1) {
			loadAttachment(nc.mAttachments.get(0));
		} else {
			createDialog(this, nc.mAttachments);
		}
	}
	
	private void exit(){
		Attachment.deleteAllFiles(MsgDetailActivity.this);
		finish();
	}
	
	private void downloadAttachment(){
		if(curAttach.mFile==null){
			curAttach.mFile=curAttach.mCommandId +curAttach.mName;
		}
		AttachmentDownload ad=new AttachmentDownload(this,curAttach.mDownloadUri.toString(),curAttach.mFile,attachHandler);
		ad.start();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.downloading));
		progressDialog.show();
	}

	private void checkMsgStateShow(){
		int unReadcount=0;
		if(nc.mAttachments!=null){
			for(int i=nc.mAttachments.size()-1;i>=0;i--){
				if(nc.mAttachments.get(i).mState!=NewsContent.MSG_SUCCESSFUL){
					unReadcount++;
				}
			}
		}
		if (unReadcount > 0) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage(this.getString(R.string.has_unread_attachment,unReadcount));
			builder.setTitle(android.R.string.dialog_alert_title);
			builder.setNegativeButton(android.R.string.cancel, null);
			builder.setPositiveButton(R.string.ignore,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					exit();
				}
			});
			builder.create().show();
		} else if(nc.mState!=NewsContent.MSG_SUCCESSFUL){
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage(R.string.is_set_read);
			builder.setTitle(android.R.string.dialog_alert_title);
			builder.setNegativeButton(R.string.read,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							AutoAdress address = AutoAdress.getInstance(MsgDetailActivity.this);
							String url = address.getUpdateURL();
							nc.mState = NewsContent.MSG_SUCCESSFUL;
							String upInfo = DomXmlBuilder.buildInfo(MsgDetailActivity.this,false, DomXmlBuilder.ATTACHMENTRES,nc);
							
							Log.i(TAG, "send Attachment status change ...");
							Params param=new Params();
							param.setUrl(url);
							param.setData(upInfo);
							param.setContext(MsgDetailActivity.this);
							param.setMessage(attachHandler.obtainMessage(DownloadHandler.READ_WHAT));
							new NetExcutor(MsgDetailActivity.this,null).execute(param);
							
							
//							finish();
//							new Thread(){
//								public void run(){
//									AutoAdress address = AutoAdress.getInstance(MsgDetailActivity.this);
//									String addr = address.getUpdateURL();
//									Log.d(TAG, "submit conment to -> " + addr);
//									if (null != addr) {
//										nc.mState = NewsContent.MSG_SUCCESSFUL;
//										String lgInfo = DomXmlBuilder.buildInfo(MsgDetailActivity.this,false, DomXmlBuilder.ATTACHMENTRES,nc);
//										Log.d(TAG, "sendAttachment XmlBuilder.buildInfo == " + lgInfo);
//										InputStream upResult = HttpHelp.aemmHttpPost(MsgDetailActivity.this, addr, lgInfo,"/sdcard/comment.txt");
//										if(UpdateXmlParser.parseResult(upResult)==1){
//											nc.update(MsgDetailActivity.this);
//										}else{
//											nc.mState = NewsContent.MSG_UNREADED;
//										}
//									}
//									Message msg=attachHandler.obtainMessage(attachHandler.READ_WHAT);
//									msg.arg1=attachHandler.DOWNLOAD_ERROR_SUCCESS;
//									attachHandler.sendMessage(msg);
//								}
//							}.start();
						}
					});
			builder.setPositiveButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
//							Toast.makeText(getApplicationContext(), "cancel", 0).show();
							exit();
						}
					});
			builder.create().show();
		}else{
			exit();
		}
	}
	
	private void openfile(){
		AccessoryActivity.start(MsgDetailActivity.this, curAttach.mMimeType, Uri.parse( getFilesDir() + "/" +curAttach.mFile),curAttach.mName, 1,MsgDetailActivity.class);
	}
	
	private void loadAttachment(Attachment attach){
		curAttach=attach;
		if(curAttach.mFile==null){
			curAttach.mFile=curAttach.mCommandId+curAttach.mName;
		}
		boolean fileExist=false;
		try {
			FileInputStream  fis=this.openFileInput(curAttach.mFile);
			if(fis!=null&&fis.available()>0){
				fileExist=true;
			}
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(fileExist){
			openfile();
			/*AlertDialog.Builder builder = new Builder(this);
			builder.setMessage(R.string.attachment_file_exist);
			builder.setTitle(android.R.string.dialog_alert_title);
			builder.setNegativeButton(R.string.yes ,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							downloadAttachment();
						}
					});
			builder.setPositiveButton(android.R.string.no,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							openfile();
							dialog.dismiss();
						}
					});
			builder.create().show();*/
		}else{
			downloadAttachment();
		}

	}
	
	ListView list;
	private void createDialog(final Context mContext, final List<Attachment> mAttachments) {

		list = new ListView(mContext);//(ListView) contentView.findViewById(R.id.envaluate_lv);
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		list.setFadingEdgeLength(0);
		MenuAdapter mMenuAdapter = new MenuAdapter(mContext,mAttachments);
		list.setAdapter(mMenuAdapter);
		list.setScrollingCacheEnabled(false);
		list.setBackgroundResource(android.R.color.white);
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
				.setNegativeButton(android.R.string.cancel, null)
				.setCustomTitle(((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_dialog_title, null)).setView(list).create();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int location,
					long arg3) {
				if(mAttachments!=null&&mAttachments.size()>location){
					loadAttachment(nc.mAttachments.get(location));
		        }
				alertDialog.dismiss();
			}
		});
	}
	
	class MenuAdapter extends BaseAdapter  {
		Attachment item;
		List<Attachment> mAttachments;
		LayoutInflater inflater;
		
		public MenuAdapter(Context mContext,List<Attachment> mAttachments){
			this.mAttachments=mAttachments;
			inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if(mAttachments==null){
				return 0;
			}
			return mAttachments.size();
		}

		@Override
		public Attachment getItem(int index) {
			if(mAttachments!=null&&mAttachments.size()>index){
				return mAttachments.get(index);
			}
			return null;//mDatals.get(index);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int i, View v, ViewGroup g) {
			if (v == null) {
					v = inflater.inflate(R.layout.app_list_item, null);
			}
			item=getItem(i);
			if(item.mIcon64!=null){
				((ImageView) v.findViewById(R.id.icon_iv)).setImageBitmap(item.getIcon());
			}
			if(item.mName!=null){
				((TextView) v.findViewById(R.id.name_tv)).setText(item.mName);
			}
			((TextView) v.findViewById(R.id.categery_tv)).setText(item.getState());
			return v;
		}
	}
}
