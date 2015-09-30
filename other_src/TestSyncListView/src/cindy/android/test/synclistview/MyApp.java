package cindy.android.test.synclistview;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class MyApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		File f = new File(Environment.getExternalStorageDirectory()+"/TestSyncListView/");
		if(!f.exists()){
			f.mkdir();
		}
	}

}
