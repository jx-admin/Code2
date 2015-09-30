package wu.a.lib.view;

import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

public class ChartButtonActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_button_layout);
		testEditTextChanger(this);
		testProportionBar(this);
	}
	
	private void testEditTextChanger(Activity context){
		EditText amount_et=(EditText) context.findViewById(R.id.edit_text);
		new EditViewTextChanger(amount_et);
	}
	private void testProportionBar(Context context){
		ProportionBar pb=(ProportionBar) findViewById(R.id.pb);
		pb.setBackgroundResource(R.drawable.srect);
		pb.addItem("20%", 20, 0XFFFF0000);
		pb.addItem("30%", 40, 0XFF00FF00);
		pb.addItem("10%", 10, 0XFF0099FF);
		pb.setBackgroundDrawable(new ShadeDrawable(context));
		
		ProportionBar pb1=(ProportionBar) findViewById(R.id.pb2);
		pb1.setBackgroundResource(R.drawable.srect);
		pb1.addItem("70%", 70, 0XFFFF0000);
		pb1.addItemByRes("30%", 30, R.drawable.redpb);
	}
}
