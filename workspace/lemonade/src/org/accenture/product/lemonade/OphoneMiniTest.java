package org.accenture.product.lemonade;

import org.accenture.product.lemonade.view.ScreenSwitcherFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ViewSwitcher;

/**
 * 
 * @author shaohu.zhu 
 * 
 */
public class OphoneMiniTest extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homemain);

		// Button phone=(Button)findViewById(R.id.Button01);
		// Button application=(Button)findViewById(R.id.Button02);
		// Button message=(Button)findViewById(R.id.Button03);

		// phone.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent=new Intent();
		// intent.setAction(Intent.ACTION_DIAL);
		// startActivity(intent);
		// }
		// });
		//
		// application.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // Intent intent=new Intent();
		// // intent.setAction(Intent.ACTION_ALL_APPS);
		// // startActivity(intent);
		// }
		// });
		//
		// message.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Uri smsToUri = Uri.parse("smsto:");
		// Intent intent=new Intent();
		// intent.setData(smsToUri);
		// intent.setAction(Intent.ACTION_SENDTO);
		// startActivity(intent);
		// }
		// });

		Gallery gallery = (Gallery) findViewById(R.id.Gallery01);
		gallery.setAdapter(new ScreenAdapter(this));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_HOME:

				Intent intent = new Intent(OphoneMiniTest.this, ScreenSwitcherActivity.class);
				startActivityForResult(intent, 0);

				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow()
	{
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	class ScreenAdapter extends BaseAdapter
	{

		Context context;
		ScreenSwitcherFactory ssf;

		private int[] imageList = new int[]
		{ };

		public ScreenAdapter(Context context)
		{
			// TODO Auto-generated constructor stub
			this.context = context;
			ssf = new ScreenSwitcherFactory(context);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return imageList.length;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return imageList[position];
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			// ImageView imageView=new ImageView(context);
			// imageView.setMaxHeight(400);
			// imageView.setAdjustViewBounds(true);
			// imageView.setImageResource(imageList[position]);
			// return imageView;

			ViewSwitcher sSwitcher = new ViewSwitcher(context);
			sSwitcher.setFactory(ssf);
			// mSwitcher.setImageResource(imageList[position]);
			sSwitcher.setBackgroundResource(imageList[position]);
			sSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
			sSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));
			return sSwitcher;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0)
		{
			// Bundle bundle=data.getExtras();
			int i = data.getIntExtra("screenNum", 0);
			// int screenNum=bundle.getInt("screenNum");
			Log.e("screenNum----------:", "" + i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		menu.add(0, Menu.FIRST, 0, "add");
		menu.add(0, Menu.FIRST + 1, 0, "Personalization");
		menu.add(0, Menu.FIRST + 2, 0, "Apps Manager");
		menu.add(0, Menu.FIRST + 3, 0, "Notifications");
		menu.add(0, Menu.FIRST + 4, 0, "Settings");

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{

			case (Menu.FIRST + 1):
				Intent intent = new Intent(this, PersonalizationActivity.class);
				startActivity(intent);
				break;

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}
}
