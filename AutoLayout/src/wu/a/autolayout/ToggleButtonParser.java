package wu.a.autolayout;

import java.io.InputStream;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ToggleButtonParser extends TextViewParser{
	

	public ToggleButtonParser(Context context) {
		super(new ToggleButton(context));
	}
	
	public ToggleButtonParser(Button view){
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
