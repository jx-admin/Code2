package wu.a.lib.view;

import wu.a.lib.view.PathShapeView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;

public class PathShapeActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testPathShapeView(this);
	}
	

	private void testPathShapeView(Context context){
		PathShapeView psv;
		psv=new PathShapeView(context);
		android.widget.LinearLayout.LayoutParams lp=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		psv.setLayoutParams(lp);
		setContentView(psv);
	}

}
