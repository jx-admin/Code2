package com.aess.aemm.view.msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.aess.aemm.db.NewsTableColumns;
import com.aess.aemm.R;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DateSorter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgAdapter implements ExpandableListAdapter {
	public static final String TAG = "MsgAdapter";
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		if (moveCursorToChildPosition(groupPosition, childPosition)) {
			return getLong(NewsTableColumns.ID_COLUMN);
		}
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		Log.d(TAG, "getChildView");
		MsgItem view = null;
		if (null == convertView) {
			view = (MsgItem)LayoutInflater.from(_cxt).inflate(R.layout.msg_item, null);
	        view.setOnMsgSelectListener(_listener);
		} else {
			view = (MsgItem)convertView;
		}
		
		if (!moveCursorToChildPosition(groupPosition, childPosition)) {
			return convertView;
		}
		
		setMsgItemView(view);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int x = gItem[groupPositionToBin(groupPosition)];
		Log.d(TAG, "msg : " + x);
		return x;
	}

	@Override
	public long getCombinedChildId(long groupPosition, long childPosition) {
		return childPosition;
	}

	@Override
	public long getCombinedGroupId(long groupPosition) {
		return groupPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return gItemCount;
	}

	@Override
	public long getGroupId(int groupPosition) {
//		groupid = groupPosition;
		return groupPosition;
	}

	boolean moveCursorToChildPosition(int groupPosition, int childPosition) {
		if (_cursor.isClosed())
			return false;
		groupPosition = groupPositionToBin(groupPosition);

		int index = childPosition;
		for (int i = 0; i < groupPosition; i++) {
			index += gItem[i];
		}
		return _cursor.moveToPosition(index);
	}

	private int groupPositionToBin(int groupPosition) {
		if (groupPosition < 0 || groupPosition >= DateSorter.DAY_COUNT) {
			throw new AssertionError("group position out of range");
		}
		if (DateSorter.DAY_COUNT == gItemCount || 0 == gItemCount) {
			return groupPosition;
		}
		int arrayPosition = -1;
		while (groupPosition > -1) {
			arrayPosition++;
			if (gItem[arrayPosition] != 0) {
				groupPosition--;
			}
		}
		return arrayPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MsgGroup view = null;
		if (null == convertView) {
			view = (MsgGroup)LayoutInflater.from(_cxt).inflate(R.layout.msg_group, null);
		} else {
			view = (MsgGroup)convertView;
		}
		
		TextView tv = (TextView)view.findViewById(R.id.msggtitle);
		tv.setText(_dateSorter.getLabel(groupPositionToBin(groupPosition)));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return _cursor.isClosed() || _cursor.getCount() == 0;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		_listener.isMsgGroupSelected(0, groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		_listener.isMsgGroupSelected(1, groupPosition);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
//		_observers.add(observer);

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
//		_observers.remove(observer);

	}

	public MsgAdapter(Context cxt, Cursor cursor, MsgSelectListener listener) {
		_cxt = cxt;
		_cursor = cursor;
		_dateSorter = new DateSorter(cxt);
		_listener = listener;

		initGroup();
	}

	private void initGroup() {
		gItem = new int[DateSorter.DAY_COUNT];

		for (int x = 0; x < gItem.length; x++) {
			gItem[x] = 0;
		}

		if (!hasCursor()) {
			return;
		}

		gItemCount = 0;
		int index = -1;
		if (_cursor.moveToFirst() && _cursor.getCount() > 0) {
			while (!_cursor.isAfterLast()) {
				long date = getLong(NewsTableColumns.PDATE_COLUMN);
				int in = _dateSorter.getIndex(date);
				if (in > index) {
					gItemCount++;
					if (index == DateSorter.DAY_COUNT - 1) {
						gItem[index] = _cursor.getCount()
								- _cursor.getPosition();
						break;
					}
					index = in;
				}
				gItem[index]++;
				_cursor.moveToNext();
			}
		}
	}
	
	public void setMsgItemView(View view) {
		ImageView iv = (ImageView)view.findViewById(R.id.msgtype);
		TextView tvContent = (TextView)view.findViewById(R.id.msgcnt);
		TextView tvTime = (TextView)view.findViewById(R.id.msgtime);
		TextView tvTitle = (TextView)view.findViewById(R.id.msgtitle);
		CheckBox cb = (CheckBox)view.findViewById(R.id.msgisel);

		int isread = _cursor.getInt(NewsTableColumns.ISREAD_COLUMN);
		
		setImage(iv, _cursor.getInt(NewsTableColumns.TYPE_COLUMN), isread);

		tvTitle.setText(_cursor.getString(NewsTableColumns.TITLE_COLUMN));

		if (1 == isread) {
			tvTitle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
			tvTitle.setTextColor(Color.LTGRAY);
		} else {
			tvTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tvTitle.setTextColor(Color.WHITE);
		}
		tvContent.setText(_cursor.getString(NewsTableColumns.CONTENT_COLUMN));
		long dateValue = _cursor.getLong(NewsTableColumns.PDATE_COLUMN);

		Date date = new Date(dateValue);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		tvTime.setText(sdf.format(date));

		long msgId = _cursor.getLong(NewsTableColumns.ID_COLUMN);
		((MsgItem) view).setMsdId(msgId);
        cb.setChecked(_listener.isMsgItemSelected(msgId));
	}
	
	private void setImage(ImageView iv, int msgType, int imageType) {
		switch (msgType) {
		case MessageType.MSG_INFORM: {
			if (imageType == 1) {
				iv.setImageResource(R.drawable.attachment2);
			} else {
				iv.setImageResource(R.drawable.attachment);
			}
			break;
		}
		case MessageType.MSG_POST: {
			if (imageType == 1) {
				iv.setImageResource(R.drawable.message2);
			} else {
				iv.setImageResource(R.drawable.message);
			}
			break;
		}
		case MessageType.MSG_PLAN: {
			if (imageType == 1) {
				iv.setImageResource(R.drawable.alarms2);
			} else {
				iv.setImageResource(R.drawable.alarms);
			}
			break;
		}
		case MessageType.MSG_EVENT: {
			if (imageType == 1) {
				iv.setImageResource(R.drawable.queue2);
			} else {
				iv.setImageResource(R.drawable.queue);
			}
			break;
		}
		default: {
			if (imageType == 1) {
				iv.setImageResource(R.drawable.zidingyi2);
			} else {
				iv.setImageResource(R.drawable.zidingyi);
			}
		}
		}
	}

	private long getLong(int column) {
		return _cursor.getLong(column);
	}

	private boolean hasCursor() {
		return null != _cursor;
	}

	private int[] gItem;
	private int gItemCount;
	private Context _cxt;
	private Cursor _cursor;
	private DateSorter _dateSorter;
	private MsgSelectListener _listener;
//	private int groupid = -1;
//    private Vector<DataSetObserver> _observers;
}
