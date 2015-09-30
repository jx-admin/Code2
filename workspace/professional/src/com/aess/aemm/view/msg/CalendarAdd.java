package com.aess.aemm.view.msg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Calendar.Calendars;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.view.HallMessagedb;
import com.aess.aemm.view.UpdateMessageHandler;

public class CalendarAdd implements Runnable {
	public static final String TAG = "CalendarAdd";

	Uri events = Uri.parse("content://com.android.calendar/events");
	Uri reminders = Uri.parse("content://com.android.calendar/reminders");
	Uri calendars = Uri.parse("content://com.android.calendar/calendars");

	String[] CalendarsPro = { Calendars._ID, Calendars._SYNC_ACCOUNT,
			Calendars._SYNC_ACCOUNT_TYPE, Calendars.SELECTED};

	public CalendarAdd(Context cxt) {
		_cxt = cxt;
	}

	public void addCalendar() {
		int x = getState();
		if (0 == x) {
			new Thread(this).start();
		}
	}

	@Override
	public void run() {
		int id = getAccount();
		if (id > 0) {
			List<NewsContent> nclist = NewsContent.getContentByType(_cxt);

			for (NewsContent nc : nclist) {

				try{
					if (nc.mEventId <= 0) {
						add(nc);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void add(NewsContent nc) throws ParseException {
		ContentValues cvevent = new ContentValues();
		cvevent.put(android.provider.Calendar.Events.TITLE, nc.mTitile);
		cvevent.put(android.provider.Calendar.Events.DESCRIPTION, nc.mContent);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(nc.mBegin);
		cvevent.put(android.provider.Calendar.Events.DTSTART, date.getTime());
		date = sdf.parse(nc.mEnd);
		cvevent.put(android.provider.Calendar.Events.DTEND, date.getTime());
		cvevent.put(android.provider.Calendar.Events.CALENDAR_ID, people.id);
		cvevent.put(android.provider.Calendar.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

		cvevent.put(android.provider.Calendar.Events._SYNC_ACCOUNT, people.name);
		cvevent.put(android.provider.Calendar.Events._SYNC_ACCOUNT_TYPE, people.type);			
		cvevent.put(android.provider.Calendar.Events.HAS_ALARM, "1");
		ContentResolver cr = _cxt.getContentResolver();
		Uri newEvent = cr.insert(events, cvevent);;
		
		long recid = Long.parseLong(newEvent.getLastPathSegment());
		
    	ContentValues cvreminder = new ContentValues();
    	cvreminder.put(android.provider.Calendar.Reminders.EVENT_ID, recid );
    	cvreminder.put(android.provider.Calendar.Reminders.MINUTES, 10);
    	cvreminder.put(android.provider.Calendar.Reminders.METHOD, 1);
        
        cr.insert(reminders, cvreminder);
        
        nc.mEventId = (int)recid;
        nc.update(_cxt);
	}
	
//	public void update(NewsContent nc) throws ParseException {
//		ContentValues cvevent = new ContentValues();
//		cvevent.put(android.provider.Calendar.Events.TITLE, nc.mTitile);
//		cvevent.put(android.provider.Calendar.Events.DESCRIPTION, nc.mContent);
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = sdf.parse(nc.mBegin);
//		cvevent.put(android.provider.Calendar.Events.DTSTART, date.getTime());
//		date = sdf.parse(nc.mEnd);
//		cvevent.put(android.provider.Calendar.Events.DTEND, date.getTime());
//		cvevent.put(android.provider.Calendar.Events.CALENDAR_ID, people.id);
//		cvevent.put(android.provider.Calendar.Events.TIMEZONE, timezone);
//		cvevent.put(android.provider.Calendar.Events.ORGANIZER, nc.mPublish);
//		
//		cvevent.put(android.provider.Calendar.Events._SYNC_ACCOUNT, people.name);
//		cvevent.put(android.provider.Calendar.Events._SYNC_ACCOUNT_TYPE, people.type);			
//		cvevent.put(android.provider.Calendar.Events.HAS_ALARM, "1");
//		ContentResolver cr = _cxt.getContentResolver();
//		Uri newEvent = cr.insert(events, cvevent);;
//		
//		long recid = Long.parseLong(newEvent.getLastPathSegment());
//		
//    	ContentValues cvreminder = new ContentValues();
//    	cvreminder.put(android.provider.Calendar.Reminders.EVENT_ID, recid );
//    	cvreminder.put(android.provider.Calendar.Reminders.MINUTES, 10);
//    	cvreminder.put(android.provider.Calendar.Reminders.METHOD, 1);
//        
//        cr.insert(reminders, cvreminder);
//        
//        nc.mEventId = (int)recid;
//        nc.update(_cxt);
//	}

	public static int getState() {
		synchronized (loc) {
			return state;
		}
	}

	public static void setState(int value) {
		synchronized (loc) {
			state = value;
		}
	}
	
	private int getAccount() {
		Cursor c = _cxt.getContentResolver().query(calendars, CalendarsPro,
				null, null, null);
		try {
			if (null != c && c.moveToFirst()) {
				people.id = c.getInt(0);
				people.name = c.getString(1);
				people.type = c.getString(2);
				people.select = c.getInt(3);
				if (people.select > 0) {
					return 1;
				}
				while(c.moveToNext()) {
					people.id = c.getInt(0);
					people.name = c.getString(1);
					people.type = c.getString(2);
					people.select = c.getInt(3);
					if (people.select > 0) {
						return 1;
					}
				}
			}
		} finally {
			if (null != c) {
				c.close();
			}
		}


		hitAccountLimit(_cxt);
		return 0;
	}
	
	public static void hitAccountLimit(Context context) {
		String info = context.getString(com.aess.aemm.R.string.accountlimitover);
		HallMessagedb hmsg = new HallMessagedb(info, 0, 2000, 2000,
				HallMessagedb.RESULTMSG);
		UpdateMessageHandler.addMessage(context, hmsg);
	}
	
	private class Account {
		public int id = -1;
		public String name = null;
		public String type = null;
		public int select = 0;
//		public int sync = 0;
	}

	private Context _cxt;
	private static Object loc = new Object();
	private static int state = 0;
	private Account people = new Account();
}
