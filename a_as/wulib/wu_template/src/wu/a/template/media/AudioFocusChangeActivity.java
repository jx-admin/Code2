package wu.a.template.media;

import wu.a.template.R;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AudioFocusChangeActivity extends Activity {
	private final static String TAG=AudioFocusChangeActivity.class.getSimpleName();
	ToggleButton audio_foucs_tbtn;
	TextView info_tv;
	Button model_btn;
	private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener ;
	private AudioManagerUtils mAudioManagerUtils;
	private boolean audioRequest;
	private StringBuilder sb=new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiofocus_layout);
		mAudioManagerUtils=new AudioManagerUtils(this);
		createAudioFocusChangeListener();
		audio_foucs_tbtn=(ToggleButton) findViewById(R.id.audio_foucs_tbtn);
		audio_foucs_tbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(audioRequest){
					audioRequest=!mAudioManagerUtils.abandonAudioFocus(mAudioFocusChangeListener);
				}else{
					audioRequest=mAudioManagerUtils.requestAudioFocus(mAudioFocusChangeListener);
				}
				sb.append("audioRequest=");
				sb.append(audioRequest);
				sb.append('\n');
				info_tv.setText(sb.toString());
			}
		});
		info_tv=(TextView) findViewById(R.id.info_tv);
		model_btn=(Button) findViewById(R.id.model_btn);
		model_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model_btn.setText(mAudioManagerUtils.getModelName());
			}
		});
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	/**
	 * <pre>
	 * Build.VERSION.SDK_INT表示当前SDK的版本，Build.VERSION_CODES.ECLAIR_MR1为SDK 7版本 ，
	 * 因为AudioManager.OnAudioFocusChangeListener在SDK8版本开始才有。
	 * </pre>
	 */
	private void createAudioFocusChangeListener() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
			mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
				@Override
				public void onAudioFocusChange(int focusChange) {

					sb.append("focusChange=");
					sb.append(focusChange);

					Log.d(TAG, "other audioFocus: " + focusChange);
					switch (focusChange) {
					case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
						sb.append(" AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 降低音量");
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 降低音量");
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
						sb.append(" AUDIOFOCUS_LOSS_TRANSIENT 长期，被其他播放器抢走等，不需要恢复");
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_LOSS_TRANSIENT 长期，被其他播放器抢走等，不需要恢复");
						// request=false;
						// isNeedPlay = isPlaying();
//						if (isNeedPlay) {
							// stop();
//						}
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
						sb.append(" AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK 暂时失去，需要恢复");
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK 暂时失去，需要恢复");
						// isNeedPlay=isPlaying();
//						if (isNeedPlay) {
							// pause();
//						}
						break;
					case AudioManager.AUDIOFOCUS_LOSS:
						sb.append(" AUDIOFOCUS_LOSS 失去焦点");
						// 失去焦点之后的操作
						Log.d(TAG, "lose audioFocus");
						// request=false;//暂存当前播放状态，待恢复
//						isNeedPlay = false;
						// stop();//如果正在播放则停止
						break;
					case AudioManager.AUDIOFOCUS_GAIN:
						// 获得焦点之后的操作
						sb.append(" AUDIOFOCUS_GAIN 获得焦点");
						Log.d(TAG, "get audioFocus");
						// if (isNeedPlay && !isPlaying()) {//判断是否需要回复
						// start();//恢复播放
						// }
						break;
					default:
						sb.append(" AUDIOFOCUS default");
						Log.d(TAG, "unknown audioFocus");
						break;
					}
					sb.append('\n');
					info_tv.setText(sb.toString());
				}
			};
		}
	}
}
