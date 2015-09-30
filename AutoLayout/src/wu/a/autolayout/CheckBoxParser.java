package wu.a.autolayout;

import java.io.InputStream;

import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CheckBoxParser extends TextViewParser{
	

	public CheckBoxParser(Context context) {
		super(new CheckBox(context));
	}
	
	public CheckBoxParser(Button view){
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
