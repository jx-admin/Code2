package wu.a.wuliu;

import java.text.SimpleDateFormat;
import java.util.Date;

import wu.a.activity.TitleFooterActivity;
import wu.a.wuliu.model.Driver;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import baidumapsdk.demo.R;

import com.droid.City;
import com.droid.PoiSearchDemo;

public class BookToActivity extends TitleFooterActivity implements
		OnClickListener {
	private Driver driver;
	private EditText book_time;
	private EditText book_from, book_dest;
	private TextView driver_phone;
	private Button submit;
	
	private ViewGroup view;
	
	private TimeSelectManager timeSelectManager;

	public static void start(Activity activity, Driver driver) {
		Intent i = new Intent(activity, BookToActivity.class);
		i.putExtra("driver", driver);
		activity.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view=(ViewGroup) LayoutInflater.from(this).inflate(R.layout.book_to, null);
		setContentLayout(view);
		setTitleText(R.string.select_driver, true);
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);
		timeSelectManager=new TimeSelectManager(this);

		Intent i = getIntent();
		if (i != null) {
			driver = (Driver) i.getSerializableExtra("driver");
		}

		if (driver != null) {
			book_time = (EditText) findViewById(R.id.book_time);
			book_time.setOnClickListener(this);
			book_from = (EditText) findViewById(R.id.book_from);
			book_from.setOnClickListener(this);
			book_dest = (EditText) findViewById(R.id.book_dest);
			book_dest.setOnClickListener(this);
			driver_phone = (TextView) findViewById(R.id.driver_phone);
			driver_phone.setOnClickListener(this);
			// TextView driver_comment=(TextView)
			// findViewById(R.id.driver_comment);
			// TextView driver_service_times=(TextView)
			// findViewById(R.id.driver_service_times);
			// TextView car_number=(TextView) findViewById(R.id.car_number);
			// TextView driver_city=(TextView) findViewById(R.id.driver_city);
			driver_phone.setText(driver.getPhone());
			// driver_comment.setText(driver.getComment());
			// driver_service_times.setText(getString(R.string.service_times,
			// driver.getTimes()));
			// car_number.setText(driver.getCarNo());
			// driver_city.setText(getString(R.string.driver_city,driver.getCity()));
			// TextView
			// lisence_tv=((TextView)findViewById(R.id.driver_lisence));
			// lisence_tv.setText(driver.getLisence());
		}
		// comment_lv=(ListView) findViewById(R.id.comment_ls);
		// adapter=new DriverComentAdapter(BookToActivity.this);
		// comment_lv.setAdapter(adapter);
	}

	@Override
	public void onTitleLeftButtonClick(View v) {
		// super.onTitleLeftButtonClick(v);
		// Activity01.start(this, 12);
		super.onTitleLeftButtonClick(v);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1001) {
				book_from.setText(data.getStringExtra("address"));
			}else if(requestCode==1002){
				book_dest.setText(data.getStringExtra("address"));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.book_time:
			timeSelectManager.setOnTimeSelectedListener(new TimeSelectManager.OnTimeSelectedListener() {
				
				@Override
				public void onTimeSelected(Date time) {
					if(time!=null){
						book_time.setText(new SimpleDateFormat("yyyy-MM-dd (EEE) hh:mm").format(time));
					}else{
//						book_time.setText("");
					}
					view.removeView(timeSelectManager.getView());
				}
			});
			view.addView(timeSelectManager.getView());
			break;
		case R.id.book_from:
			startActivityForResult(new Intent(this,PoiSearchDemo.class),1001);
			break;
		case R.id.book_dest:
			startActivityForResult(new Intent(this,PoiSearchDemo.class),1002);
			break;
		case R.id.driver_phone:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);//Intent.ACTION_CALL;
			intent.setData(Uri.parse("tel:" + driver_phone.getText()));
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onLoader() {
		super.onLoader();
	}
}
