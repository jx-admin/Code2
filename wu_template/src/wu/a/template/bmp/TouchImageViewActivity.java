package wu.a.template.bmp;


	import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

	public class TouchImageViewActivity extends Activity {
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FrameLayout contnet=new FrameLayout(this);
	        TouchImageView img = new TouchImageView(this);
	        img.setBackgroundColor(0xff0000ff);
	        contnet.addView(img, 500, 500);
	        setContentView(contnet);
	    }
}
