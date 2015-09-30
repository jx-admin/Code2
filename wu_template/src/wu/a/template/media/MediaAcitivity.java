package wu.a.template.media;

import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MediaAcitivity extends Activity implements OnClickListener {
	MediaButtonReceiver mMediaButtonReceiver;
	private TextView info_tv;
	public static void start(Context context,String data){
		Intent i=new Intent(context,MediaAcitivity.class);
		i.putExtra("data", data);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_layout);
		info_tv=(TextView) findViewById(R.id.info_tv);
		info_tv.setMovementMethod(ScrollingMovementMethod.getInstance());
		mMediaButtonReceiver=new MediaButtonReceiver();
		initDate(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initDate(intent);
	}
	
	private void initDate(Intent intent){
		if(intent!=null){
			String data=intent.getStringExtra("data");
			info_tv.setText(info_tv.getText()+"\n"+data);
		}
	}
	
	public void onClick(View v){
		int id=v.getId();
		if(id==R.id.media_button_reg){
			MediaButtonReceiver.registerMediaButtonReceiver(this);
		}else if(id==R.id.media_button_unreg){
			MediaButtonReceiver.unregisterMediaButtonReceiver(this);
		}else if(id==R.id.headset_reg){
			boolean isHeadon=MediaButtonReceiver.isHeadsetOn();
			boolean isHeadon2=MediaButtonReceiver.isHeadsetOn(this);
			info_tv.setText(info_tv.getText()+"\n"+" isHeadon f="+isHeadon+" manager="+isHeadon2);
			mMediaButtonReceiver.registerHeadsetPlugReceiver(this);
		}else if(id==R.id.headset_unreg){
			mMediaButtonReceiver.unregisterHeadsetPlugReceiver(this);
		}
	}
	

}
