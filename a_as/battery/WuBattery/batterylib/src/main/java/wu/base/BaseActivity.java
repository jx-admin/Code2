package wu.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by jx on 2015/12/29.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStart(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onStart(intent);
    }

    public void onStart(Intent intent) {
    }
}
