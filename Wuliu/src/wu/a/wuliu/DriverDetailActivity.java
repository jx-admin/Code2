package wu.a.wuliu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wu.a.activity.TitleFooterActivity;
import wu.a.wuliu.model.Driver;
import wu.a.wuliu.model.UserComent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import baidumapsdk.demo.R;

import com.droid.City;

public class DriverDetailActivity extends TitleFooterActivity implements OnClickListener {
	private Driver driver;
	private List<UserComent>userComentList;
	private ListView comment_lv;
	private DriverComentAdapter adapter;
	private Button submit;
	
	public static void start(Activity activity,Driver driver){
		Intent i=new Intent(activity,DriverDetailActivity.class);
		i.putExtra("driver", driver);
		activity.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.driver_detail);
		setTitleText(R.string.select_driver,true);
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);

		Intent i=getIntent();
		if(i!=null){
			driver=(Driver) i.getSerializableExtra("driver");
		}
		
		if(driver!=null){
			ImageView icon=(ImageView) findViewById(R.id.driver_icon);
			TextView driver_name=(TextView) findViewById(R.id.driver_name);
			TextView driver_comment=(TextView) findViewById(R.id.driver_comment);
			TextView driver_service_times=(TextView) findViewById(R.id.driver_service_times);
			TextView car_number=(TextView) findViewById(R.id.car_number);
			TextView driver_city=(TextView) findViewById(R.id.driver_city);
			driver_name.setText(driver.getName());
			driver_comment.setText(driver.getComment());
			driver_service_times.setText(getString(R.string.service_times, driver.getTimes()));
			car_number.setText(driver.getCarNo());
			driver_city.setText(getString(R.string.driver_city,driver.getCity()));
			TextView lisence_tv=((TextView)findViewById(R.id.driver_lisence));
			lisence_tv.setText(driver.getLisence());
		}
		comment_lv=(ListView) findViewById(R.id.comment_ls);
		adapter=new DriverComentAdapter(DriverDetailActivity.this);
		comment_lv.setAdapter(adapter);
		findViewById(R.id.submit).setOnClickListener(this);
	}
	
	@Override
	public void onTitleLeftButtonClick(View v) {
//		super.onTitleLeftButtonClick(v);
//		Activity01.start(this, 12);
		super.onTitleLeftButtonClick(v);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			if(requestCode==12){
				City city=(City) data.getSerializableExtra("city");
				setTitleLeftButtonText(city.name);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submit:
			BookToActivity.start(this, driver);
			break;
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onLoader() {
		super.onLoader();
		if(userComentList==null){
			userComentList=new ArrayList<UserComent>();
		for(int i=0;i<8;i++){
			UserComent userComent=new UserComent();
			userComent.setTime(System.currentTimeMillis()-1000*60*60*24);
			userComent.setCommentContent("特别赞");
			userComent.setCommentScore(i%10);
			userComent.setPhone("1312158101"+i);
			userComentList.add(userComent);
		}
		comment_lv.post(new Runnable() {
			
			@Override
			public void run() {
				adapter.notifyDataSetInvalidated();
			}
		});
		}
	}
	class DriverComentAdapter extends BaseAdapter{
		public DriverComentAdapter(Context context){
			
		}

		@Override
		public int getCount() {
			return userComentList==null?0:userComentList.size();
		}

		@Override
		public Object getItem(int position) {
			return userComentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=lf.inflate(R.layout.comment_list_item, null);
			}
			TextView driver_name=(TextView) convertView.findViewById(R.id.user);
			TextView driver_comment=(TextView) convertView.findViewById(R.id.comment);
			TextView time=(TextView) convertView.findViewById(R.id.time);
			RatingBar ratingBar=(RatingBar) convertView.findViewById(R.id.ratingba);
			UserComent userComment=(UserComent) getItem(position);
			if(userComment!=null){
			driver_name.setText(userComment.getPhone());
			driver_comment.setText(userComment.getCommentContent());
			ratingBar.setMax(10);
			ratingBar.setProgress(userComment.getCommentScore());
			time.setText(formatter.format(userComment.getTime()));
			}
			return convertView;
		}
		
	}

}
