package wu.a.template.media;

import wu.a.lib.media.PlateformUtils;
import wu.a.template.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class MediaButtonSenderActivity extends Activity {
	
	String pkg=
//			null;
			//"fm.xiami.main";
//			"com.tencent.mobileqq";
//			"com.ting.mp3.qianqian.android";
//			"cn.kuwo.player";
			"com.ting.mp3.android";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediabuttonsender_layout);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pre_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_PREVIOUS,pkg);
			break;
		case R.id.next_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_NEXT,pkg);
			break;
		case R.id.play_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_PLAY,pkg);
			break;
		case R.id.play_pause_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,pkg);
			break;
		case R.id.headsethook_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_HEADSETHOOK,pkg);
			break;
		case R.id.pause_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_PAUSE,pkg);
			break;
		case R.id.stop_btn:
			PlateformUtils.sendActionMediaButton(this,KeyEvent.KEYCODE_MEDIA_STOP,pkg);
			break;
		}
	}
}
