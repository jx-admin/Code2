package wu.a.wuliu;

import java.util.ArrayList;
import java.util.List;

import wu.a.activity.TitleFooterActivity;
import wu.a.wuliu.model.Driver;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import baidumapsdk.demo.R;

import com.droid.City;

public class DriverListActivity extends TitleFooterActivity implements OnClickListener {
	private ListView driverList;
	private Button submit;
	private List<Driver> drivers;
	private DriverAdapter adapter;
	
	public static void start(Activity activity){
		activity.startActivity(new Intent(activity,DriverListActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.driver_list);
//		setMenuStatus(this,MENU_BOOK, false);
		setTitleText(R.string.select_driver,true);
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);
		driverList=(ListView) findViewById(R.id.driver_ls);
		driverList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DriverDetailActivity.start(DriverListActivity.this, drivers.get(position));
			}
			
		});

		adapter=new DriverAdapter(DriverListActivity.this);
		driverList.setAdapter(adapter);
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
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onLoader() {
		super.onLoader();
		if(drivers==null){
		drivers=new ArrayList<Driver>();
		for(int i=0;i<10;i++){
			Driver driver=new Driver();
			driver.setCarNo("M667"+i*2+""+i*4+""+i);
			driver.setCity("山东");
			driver.setComment("暂无评价");
			driver.setTimes(i*3);
			driver.setName("赵飞");
			driver.setPhone("13121581070");
			drivers.add(driver);
		}
		driverList.post(new Runnable() {
			
			@Override
			public void run() {
				adapter.notifyDataSetInvalidated();
			}
		});
		}
	}
	class DriverAdapter extends BaseAdapter{
		public DriverAdapter(Context context){
			
		}

		@Override
		public int getCount() {
			return drivers==null?0:drivers.size();
		}

		@Override
		public Object getItem(int position) {
			return drivers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=lf.inflate(R.layout.driver_list_item, null);
			}
			ImageView icon=(ImageView) convertView.findViewById(R.id.driver_icon);
			TextView driver_name=(TextView) convertView.findViewById(R.id.driver_name);
			TextView driver_comment=(TextView) convertView.findViewById(R.id.driver_comment);
			TextView driver_service_times=(TextView) convertView.findViewById(R.id.driver_service_times);
			TextView car_number=(TextView) convertView.findViewById(R.id.car_number);
			TextView driver_city=(TextView) convertView.findViewById(R.id.driver_city);
			Driver driver=(Driver) getItem(position);
			driver_name.setText(driver.getName());
			driver_comment.setText(driver.getComment());
			driver_service_times.setText(getString(R.string.service_times, driver.getTimes()));
			car_number.setText(driver.getCarNo());
			driver_city.setText(getString(R.string.driver_city,driver.getCity()));
			return convertView;
		}
		
	}

}
