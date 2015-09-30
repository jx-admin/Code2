package wu.a.lib.device;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import wu.a.lib.file.FileUtils;
import wu.a.template.R;

public class DisplayActivity extends Activity {
	private TextView display_info;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_info);
		display_info=(TextView) findViewById(R.id.display_info);
		display_info.setText(getDisplay()+"\n"+new FileUtils().getFileInfo(this));
	}
	
	private String getDisplay(){
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        StringBuilder sb=new StringBuilder();
     // 屏幕宽度（像素）
        sb.append("metric.widthPixels=");
        sb.append(metric.widthPixels);
        sb.append('\n');
        // 屏幕高度（像素）
        sb.append("metric.heightPixels=");
        sb.append(metric.heightPixels);
        sb.append('\n');
     // 屏幕密度（0.75 / 1.0 / 1.5）
        sb.append("metric.density=");
        sb.append(metric.density);
        sb.append('\n');
     // 屏幕密度DPI（120 / 160 / 240）
        sb.append("metric.densityDpi=");
        sb.append(metric.densityDpi);
        sb.append('\n');
        sb.append("metric.scaledDensity=");
        sb.append(metric.scaledDensity);
        sb.append('\n');
        sb.append("metric.xdpi=");
        sb.append(metric.xdpi);
        sb.append('\n');
        sb.append("metric.ydpi=");
        sb.append(metric.ydpi);
        sb.append('\n');
        return sb.toString();
	}

}
