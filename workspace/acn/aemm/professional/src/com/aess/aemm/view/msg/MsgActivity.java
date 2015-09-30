package com.aess.aemm.view.msg;

import java.util.HashSet;
import java.util.Set;

import com.aess.aemm.R;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.view.NotificationUtils;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class MsgActivity extends Activity implements OnGroupClickListener,
		MsgSelectListener, OnClickListener, OnChildClickListener {

	public static final String TAG = "MsgActivity";
	public static final int CHECKMARK_AREA = 40;

	private Cursor _cursor;
	private MsgAdapter _adapter;
	private ExpandableListView _elistview;
	private ViewGroup _selectionMenuView;
	private Set<Long> mItemSelectedIds = new HashSet<Long>();
	private Set<Integer> mGroupSelectedIds = new HashSet<Integer>();
	private ChangeObserver chgobserver = new ChangeObserver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	public void onItemsSelectedListener(long msgId, boolean selected) {
		if (selected) {
			mItemSelectedIds.add(msgId);
		} else {
			mItemSelectedIds.remove(msgId);
		}
		showOrHideSelectionMenu();
	}

	private void deleteMsg(long MsgId) {
		NewsContent.delContentById(this, MsgId);
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
			for (Long msdId : mItemSelectedIds) {
				deleteMsg(msdId);
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
	public boolean isMsgItemSelected(long id) {
		return mItemSelectedIds.contains(id);
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
			intent.setData(ContentUris.withAppendedId(NewsContent.CONTENT_URI,
					((MsgItem) (arg1)).getMsgId()));
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
