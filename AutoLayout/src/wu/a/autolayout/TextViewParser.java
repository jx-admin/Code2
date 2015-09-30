package wu.a.autolayout;

import java.io.InputStream;

import android.content.Context;
import android.widget.TextView;

public class TextViewParser extends ViewParser<TextView> {
	

	public TextViewParser(Context context) {
		super(new TextView(context));
	}

	public TextViewParser(TextView view) {
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

	void parse(String an,String av){
		if(ATT_TEXT.equals(an)){
			view.setText(av);
		}else 
			super.parse(an,av);
	}

}
