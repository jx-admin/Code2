package org.accenture.product.lemonade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Lemonade extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button zhushaohu = (Button) findViewById(R.id.zhushaohu);
		Button zhangxingting = (Button) findViewById(R.id.zhangxingting);
		Button juhuizhe = (Button) findViewById(R.id.juhuizhe);
		Button gaozhenqiang = (Button) findViewById(R.id.gaozhenqiang);

		zhushaohu.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
//				Intent intent = new Intent(Lemonade.this, OphoneMiniTest.class);
//				startActivity(intent);
			}
		});

		zhangxingting.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// ViewGroup viewGroup = (ViewGroup) v.getParent();
				// viewGroup.removeViewAt(0);
				// Intent intent = new Intent(Lemonade.this,
				// WidgetActivity.class);
				// startActivity(intent);

			}
		});

		juhuizhe.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});

		gaozhenqiang.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(Lemonade.this, PersonalizationActivity.class);
				startActivity(intent);
			}
		});
	}
}