package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.manager.contactus.ContactNewRequestLayout;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.view.MapLayout;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ContactsManagerHelp implements OnClickListener {
	public static final int SPACE = -40;

	static final String PHONE = "(2012)123-4251";

	public static final String TIME = "8:30 - 20:30 monday-friday";

	public static final String MAIL = "home.banking@mybank.it";

	public static final String WEB = "www.mybank.it";

	public static final String CALL = "(212)123-4254";

	public static final String OCALL = "17(212)026-0843";
	
	 private GoogleAnalytics mGaInstance;
	    
	  	private Tracker mGaTracker1;

	public ContactsManagerHelp(Activity act) {
		activity = act;
	}

	public void init(ViewGroup layout) {
		TextView tv = (TextView) layout.findViewById(R.id.time);
		String head = activity.getString(R.string.last_update_on);
		head = String.format(head, TimeUtil.getDateString(new Date().getTime(),
				TimeUtil.dateFormat1));
		tv.setText(head);

		vgContact = (ViewGroup) layout.findViewById(R.id.contactLayout);
		vgSearch = (ViewGroup) layout.findViewById(R.id.searchLayout);

		ImageView iv = (ImageView) vgContact.findViewById(R.id.imageContacts);
		iv.setOnClickListener(this);

		iv = (ImageView) vgSearch.findViewById(R.id.imageSearch);
		iv.setOnClickListener(this);

		scrollView = (ScrollView) layout.findViewById(R.id.scrollView1);
	}

	int selectid = -1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
		case 1:
		case 2:
			ViewHolder holder = null;
			if (selectid > -1 && v.getId() != selectid) {
				holder = (ViewHolder) lin.getChildAt(selectid).getTag();
				holder.detail.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.btnarrow_down);
			}
			holder = (ViewHolder) v.getTag();
			if (holder.detail.getVisibility() == View.VISIBLE) {
				holder.detail.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.btnarrow_down);
				selectid = -1;
			} else {
				holder = (ViewHolder) v.getTag();
				holder.detail.setVisibility(View.VISIBLE);
				holder.iv.setImageResource(R.drawable.btnarrow_up);
				selectid = v.getId();
			}

			return;
		}
		setOpen(v.getId());
	}

	private void setOpen(int x) {
		closeDetail();

		if (openItem == x) {
			openItem = -1;
			view = null;
			vg = null;
			return;
		}
		openItem = x;
		switch (x) {
		
      
		case R.id.imageContacts: {
			mGaInstance = GoogleAnalytics.getInstance(activity);
	        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
			 mGaTracker1.sendView("event.contact.us");
			showDetail(vgContact);
			break;
		}
		case R.id.imageSearch: {
			mGaInstance = GoogleAnalytics.getInstance(activity);
	        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
			 mGaTracker1.sendView("event.contact.map");
			 mGaTracker1.sendView("event.contact.map.search.branch");
			 
			showMap(vgSearch);
			break;
		}
		default: {
			break;
		}
		}
	}

	private void closeDetail() {
		if (null != view && null != vg) {
			vg.removeView(view);
		}
	}

	LinearLayout lin;

	private void showDetail(ViewGroup vgArug) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lin = (LinearLayout) inflater
				.inflate(R.layout.contact_us_details, null);

		ArrayList<HashMap<String, String>> list = getData(vgArug.getId());

		for (int i = 0; i < list.size(); i++) {
			View v = getChild(i, null, null, list);
			v.setId(i);
			v.setOnClickListener(this);
			lin.addView(v);
		}

		view = lin;

		vg = vgArug;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CLIP_VERTICAL;
		vg.addView(view, lp);

	}

	public static class ViewHolder {
		public TextView title = null;
		public View detail = null;
		public ImageView iv = null;
		public int postion = -1;
		public int expand = 0;
		public View ls = null;
	}

	private View getChild(int position, View convertView, ViewGroup parent,
			ArrayList<HashMap<String, String>> list) {
		ViewHolder holder = null;
		if (null == convertView) {

			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.show_list, null);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.detail = (TextView) convertView.findViewById(R.id.detail);
			if (position == 2) {
				holder.detail.setVisibility(View.GONE);
				ContactNewRequestLayout mContactNewRequestLayout = (ContactNewRequestLayout) inflater
						.inflate(R.layout.contact_new_request, null);

				holder.detail = mContactNewRequestLayout;
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				((ViewGroup) convertView).addView(holder.detail, lp);
			}
			holder.iv = (ImageView) convertView.findViewById(R.id.imagehint);

			convertView.setOnClickListener(this);
			convertView.setTag(holder);

		}
		holder = (ViewHolder) convertView.getTag();
		holder.postion = position;

		HashMap<String, String> item = list.get(position);
		String title = item.get("title");
		String detail = item.get("detail");
		String eValue = item.get("expand");

		holder.title.setText(title);
		if (holder.detail instanceof TextView) {
			((TextView) holder.detail).setText(detail);
		}
		if ("1".equals(eValue)) {
			holder.detail.setVisibility(View.VISIBLE);
			holder.iv.setImageResource(R.drawable.btnarrow_up);
		} else {
			holder.detail.setVisibility(View.GONE);
			holder.iv.setImageResource(R.drawable.btnarrow_down);
		}
		return convertView;
	}

	private void showMap(ViewGroup vgArug) {

		if (null == mapView) {
			MapLayout maplayout = (MapLayout) activity.getLayoutInflater()
					.inflate(R.layout.search_width_map, null);
			maplayout.init();
			// scrollView.requestDisallowInterceptTouchEvent(true);
			maplayout.parentScrollView = scrollView;

			mapView = maplayout;
		}
		view = mapView;
		vg = vgArug;

		addSubView(0);
	}

	private void addDetailStr(StringBuilder sb, int headStrId, String Value) {
		if (null == sb) {
			return;
		}

		if (headStrId < 0) {
			sb.append("\n");
			return;
		}

		String str = activity.getString(headStrId);
		if (null == str) {
			return;
		}

		if (!TextUtils.isEmpty(Value)) {
			int x = SPACE + str.length();
			String format = "%" + x + "s";

			str = String.format(format, str);
		}

		sb.append(str);
		if (!TextUtils.isEmpty(Value)) {
			sb.append(Value);
		}
		sb.append("\n");
	}

	private ArrayList<HashMap<String, String>> getData(int x) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> item = new HashMap<String, String>();
		String title = activity.getString(R.string.customer_service);
		item.put("title", title);
		StringBuilder sb = new StringBuilder();
		addDetailStr(sb, R.string.us, PHONE);
		addDetailStr(sb, R.string.business_hours2, TIME);
		addDetailStr(sb, R.string.e_mail, MAIL);
		addDetailStr(sb, R.string.website, WEB);
		item.put("detail", sb.toString());

		list.add(item);

		item = new HashMap<String, String>();
		title = activity.getString(R.string.theft_and_loss);
		item.put("title", title);
		sb = new StringBuilder();
		addDetailStr(sb, R.string.block_debit_and, null);
		addDetailStr(sb, R.string.us, CALL);
		addDetailStr(sb, R.string.other_country, OCALL);
		addDetailStr(sb, -1, null);
		addDetailStr(sb, R.string.block_credit_cards, null);
		addDetailStr(sb, R.string.us, CALL);
		addDetailStr(sb, R.string.other_country, OCALL);
		item.put("detail", sb.toString());
		list.add(item);

		// The link ("new request") to acces the section must be visible only
		// after login.
		if (!TextUtils.isEmpty(Contants.publicModel.getSessionId())) {
			item = new HashMap<String, String>();
			title = activity.getString(R.string.new_request);
			item.put("title", title);
			list.add(item);
		}

		return list;
	}

	private void addSubView(int height) {
		if (null != view && null != vg) {
			if (0 == height) {
				height = ViewGroup.LayoutParams.WRAP_CONTENT;
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, height);
			lp.gravity = Gravity.CLIP_VERTICAL;
			vg.addView(view, lp);
		}
	}

	private int openItem = -1;

	private View view;

	private View mapView;

	private ViewGroup vg;

	private ViewGroup vgContact;

	private ViewGroup vgSearch;

	private Activity activity;

	private ScrollView scrollView;
}
