package wu.a.lib.view;

import wu.a.lib.view.ShadeDrawable;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**�Զ��� Drawable���� 
 * @author junxu.wang
 *
 */
public class DrawableActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		init();
		setTitle("Drawble");
	}
	
	private void init(){
		ScrollView sl=new ScrollView(this);
		LinearLayout contentView=new LinearLayout(this);
		contentView.setOrientation(LinearLayout.VERTICAL);
		sl.addView(contentView);
		TextView tv=new TextView(this);
		tv.setText("TextView�ؼ�fa;ldkfjalksfjalskdfjaklsdfjaklsdfjkalsdfjaslkfdfjalksdfjalsfkjalskfjlasfjasl");
		Button btn=new Button(this);
		btn.setText("Button��ťTextView�ؼ�fa;ldkfjalksfjalskdfjaklsdfjaklsdfjkalsdfjaslkfdfjalksdfjalsfkjalskfjlasfjasl");
		EditText et=new EditText(this);
		et.setHint("inputText");
		EditText et1=new EditText(this);
		et1.setText("hello");
		
		contentView.addView(tv);
		contentView.addView(btn);
		contentView.addView(et);
		contentView.addView(et1);
		setContentView(sl);
		
		btn.setBackgroundDrawable(new ShadeDrawable(this));
		
		
	}

}
