package wu.a.lib.data.json;

import wu.a.template.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class JsonActivity extends Activity implements OnClickListener{
	Button btn1,btn2;
	EditText et1,et2;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_main);
		btn1=(Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		btn2=(Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(this);

		et1=(EditText) findViewById(R.id.et1);
		et1.setOnClickListener(this);
		et2=(EditText) findViewById(R.id.et2);
		et2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			JsonlibTest jsonlibTest=new JsonlibTest();
			jsonlibTest.parse();
			break;
		case R.id.btn2:
			break;

		default:
			break;
		}
		
	}

}
