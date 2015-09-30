package wu.a.wuliu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import baidumapsdk.demo.R;

/**
 * <pre>
 * 优惠活动
 * @author junxu.wang
 * @d2015年7月16日
 * </pre>
 *
 */
public class InfoManager implements OnClickListener {

	private View view;
	private Context context;

	private LayoutInflater lif;

	public InfoManager(Context context) {
		this.context = context;
		lif = LayoutInflater.from(context);
		view = LayoutInflater.from(context).inflate(R.layout.info_layout,
				null);

		view.findViewById(R.id.share_item).setOnClickListener(this);
		view.findViewById(R.id.daijinquan_item).setOnClickListener(this);
		view.findViewById(R.id.help_item).setOnClickListener(this);
		view.findViewById(R.id.about_item).setOnClickListener(this);
		view.findViewById(R.id.feedback_item).setOnClickListener(this);
		view.findViewById(R.id.call_service_item).setOnClickListener(this);
	}

	private void loadData() {
	}

	public View getView() {
		return view;
	}

	public String getTitle() {
		return "优惠活动";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.feedback_tv:
		// context.startActivity(new Intent(context,FeedBackActivity.class));
		// break;
		case R.id.share_item:
			break;
		case R.id.daijinquan_item:
			break;
		case R.id.help_item:
			break;
		case R.id.about_item:
			AboutActivity.start((Activity)context);
			break;
		case R.id.feedback_item:
			FeedBackActivity.start(context);
			break;
		case R.id.call_service_item:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);//Intent.ACTION_CALL;
			intent.setData(Uri.parse("tel:" + "40088888888"));
			context.startActivity(intent);
			break;

		default:
			break;
		}
	}


}
