package com.aess.aemm.view.msg;

public interface MsgSelectListener {
	void onItemsSelectedListener(String conmandId, boolean selected);
	public boolean isMsgItemSelected(String conmandId);
	public boolean isMsgGroupSelected(int type, int id);
	public final static int CHECKMARK_AREA = 50;
}
