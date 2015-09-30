package wu.a.lib.view;

import wu.a.lib.view.FaceView;
import android.app.Activity;
import android.os.Bundle;

public class FaceTest extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        FaceView faceView = new FaceView(this);
        setContentView(faceView);
    }
}
