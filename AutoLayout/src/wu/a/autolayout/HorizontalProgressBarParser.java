package wu.a.autolayout;

import java.io.InputStream;

import android.content.Context;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HorizontalProgressBarParser extends ViewParser{
	

	public HorizontalProgressBarParser(Context context) {
		super(new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal));
	}
	
	public HorizontalProgressBarParser(Button view){
		super(view);
	}

	@Override
	public TextView parse(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void parse(String an, String av) {
		super.parse(an, av);
	}

}
