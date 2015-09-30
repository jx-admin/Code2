package com.aess.aemm.view.msg;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.view.NotificationUtils;

public class MsgActivity extends Activity implements OnGroupClickListener,
		MsgSelectListener, OnClickListener, OnChildClickListener {

	public static final String TAG = "MsgActivity";
	public static final int CHECKMARK_AREA = 40;

	private Cursor _cursor;
	private MsgAdapter _adapter;
	private ExpandableListView _elistview;
	private ViewGroup _selectionMenuView;
	private Set<String> mItemSelectedIds = new HashSet<String>();
	private Set<Integer> mGroupSelectedIds = new HashSet<Integer>();
	private ChangeObserver chgobserver = new ChangeObserver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		addMsg(this,"commandId",0,System.currentTimeMillis(),"pdf","text","/sdcard/pdf.pdf",NewsContent.DOC_PDF);
//		addMsg(this,"commandId",0,System.currentTimeMillis(),"3gp","3gp","/sdcard/3gp.3gp",NewsContent.VIDEO_UNSPECIFIED);
//		addMsg(this,"commandId",0,System.currentTimeMillis(),"jpg.jpg","jpg.jpg","/sdcard/jpg.jpg",NewsContent.IMAGE_UNSPECIFIED);
//		addMsg(this,"commandId",0,System.currentTimeMillis(),"jpg.jpg","jpg.jpg",null,null);
		setContentView(R.layout.activity_msg);
		setTitle(R.string.msg);
		_elistview = (ExpandableListView) findViewById(R.id.elist);

		_elistview.setOnGroupClickListener(this);
		_elistview.setOnChildClickListener(this);

		_selectionMenuView = (ViewGroup) findViewById(R.id.selection_menu);

		Button btn = (Button) findViewById(R.id.msgundo);
		btn.setOnClickListener(this);
		btn = (Button) findViewById(R.id.msgdel);
		btn.setOnClickListener(this);

	}
	private void addMsg(Context context,String commandId,int  type,long date,String title,String content){
		NewsContent item = new NewsContent();
		item.mCommandId = commandId;
			item.mType = type;
			item.mPData = date;
			item.mTitile = title;
			item.mContent = content;
//			if (AEMMConfig.v31) {
//				item.mStartUri = person.getString(MsgName.MSG_START_URI);
//				item.mBusType = person.getInt(MsgName.MSG_BUS_TYPE);
//				item.mBusName = person.getString(MsgName.MSG_BUS_TYPE_N);
//				item.mTypeName = person.getString(MsgName.MSG_TYPE_NAME);
//			}

		if (MessageType.MSG_POST == item.mType) {
				item.mPublish ="organization";
		}


		item.mIsRead = 0;
		item.mEventId = -1;
		item.add(context);
	}
	

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_send_msg, menu);
//        return true;
//    }

	@Override
	protected void onResume() {
		NotificationUtils.cancelNotification(this);
        getContentResolver().registerContentObserver(NewsContent.CONTENT_URI,
		true, chgobserver);
		super.onResume();

		refreshData();
	}

	@Override
	protected void onPause() {
		 getContentResolver().unregisterContentObserver(chgobserver);
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		if (null != _cursor) {
			_cursor.close();
		}
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		return super.onKeyDown(arg0, arg1);
	}
	

	private void refreshNoRec() {
		if (_cursor.getCount() < 1) {
			TextView tv = (TextView) findViewById(R.id.nomsg);
			tv.setVisibility(View.VISIBLE);
		} else {
			TextView tv = (TextView) findViewById(R.id.nomsg);
			tv.setVisibility(View.INVISIBLE);
		}
	}

	private boolean haveCursors() {
		return null != _cursor;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if (parent.isGroupExpanded(groupPosition)) {
			parent.collapseGroup(groupPosition);
		} else {
			parent.expandGroup(groupPosition);
		}

		return true;
	}

	private void showOrHideSelectionMenu() {
		boolean shouldBeVisible = !mItemSelectedIds.isEmpty();

		boolean isVisible = _selectionMenuView.getVisibility() == View.VISIBLE;
		if (shouldBeVisible) {
			if (!isVisible) {
				_selectionMenuView.setVisibility(View.VISIBLE);
				_selectionMenuView.startAnimation(AnimationUtils.loadAnimation(
						this, R.anim.footer_appear));
			}
		} else if (!shouldBeVisible && isVisible) {
			_selectionMenuView.setVisibility(View.GONE);
			_selectionMenuView.startAnimation(AnimationUtils.loadAnimation(
					this, R.anim.footer_disappear));
		}
	}

	@Override
	public void onItemsSelectedListener(String commandId, boolean selected) {
		if (selected) {
			mItemSelectedIds.add(commandId);
		} else {
			mItemSelectedIds.remove(commandId);
		}
		showOrHideSelectionMenu();
	}

	private void deleteMsg(String conmandId) {
		NewsContent.delContentById(this,conmandId);
	}
	
	private void refreshData() {
		if (null != _cursor) {
			_cursor.close();
		}

		_cursor = NewsContent.queryContentByPDate(this);
		
		if (null == _cursor) {
			return;
		}
		mGroupSelectedIds.add(0);
		
		_adapter = new MsgAdapter(this, _cursor, this);
		
		_elistview.setAdapter(_adapter);
		
		int x = _adapter.getGroupCount();
		for (int msdId : mGroupSelectedIds) {
			if (x>=0 && msdId < x) {
				_elistview.expandGroup(msdId);
			}
		}
		
		if (haveCursors()) {
			refreshNoRec();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msgdel: {
			for (String commandId : mItemSelectedIds) {
				deleteMsg(commandId);
			}
			clearSelection();
			refreshData();
			break;
		}
		case R.id.msgundo: {
			clearSelection();
			break;
		}
		}
	}

	private void clearSelection() {
		mItemSelectedIds.clear();
		showOrHideSelectionMenu();
	}

	@Override
	public boolean isMsgItemSelected(String conmandId) {
		return mItemSelectedIds.contains(conmandId);
	}
	
	@Override
	public boolean isMsgGroupSelected(int type, int id) {
		if (type == 0) {
			return mGroupSelectedIds.remove(id);
		} else if (type == 1) {
			return mGroupSelectedIds.add(id);
		}
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		if (arg1 instanceof MsgItem) {
			Intent intent = new Intent("android.intent.action.AEMMMSGDETIAL");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra("ConmandId", ((MsgItem) (arg1)).getConmandId());
			intent.setData(NewsContent.CONTENT_URI);
			this.startActivity(intent);
		}
		return true;
	}
	
    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
        	refreshData();
//			_adapter = new MsgAdapter(MsgActivity.this, _cursor, MsgActivity.this);
//			_elistview.setAdapter(_adapter);
//            _elistview.invalidate();
        }
    }
}
