package wu.a.autolayout;

import java.io.InputStream;

import android.content.Context;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarParser extends ViewParser{
	

	public ProgressBarParser(Context context) {
		super(new ProgressBar(context));
	}
	
	public ProgressBarParser(Button view){
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
//		if("max".equals(an)){
//			int max=Integer.getInteger(av);
//		}
		super.parse(an, av);
	}

}
