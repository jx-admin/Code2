package com.aess.aemm.view.msg;

public interface MsgSelectListener {
	void onItemsSelectedListener(long msgId, boolean selected);
	public boolean isMsgItemSelected(long id);
	public boolean isMsgGroupSelected(int type, int id);
	public final static int CHECKMARK_AREA = 50;
}
