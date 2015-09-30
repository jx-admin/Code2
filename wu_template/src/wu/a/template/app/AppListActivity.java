package wu.a.template.app;

import wu.a.template.R;
import wu.a.template.app.AppManager.OnAppInfoSelectedLinstener;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

public class AppListActivity extends Activity implements
		OnAppInfoSelectedLinstener {
	LayoutInflater lf;
	AppManager am;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lf = LayoutInflater.from(this);
		View contentView = lf.inflate(R.layout.app_layout, null);
		setContentView(contentView);
		am = new AppManager(this);
		GridView gv = (GridView) findViewById(R.id.app_gv);
		am.setGridView(gv, gv);
		am.setOnItemSelectedListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		am.startLoader();
	}

	@Override
	public void onSelected(AppInfo appInfo) {
		PackageInfoProvider.startApp(this, appInfo.getPackageName(),
				appInfo.getClassName());
	}
}
