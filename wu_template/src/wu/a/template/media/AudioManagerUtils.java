package wu.a.template.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.util.Log;

/**
 * Android AudioManager处理两个播放器同时有声音，停止其中一个播放的问题
 * 
 * 
 * 尽管某个时刻只有一个activity可以运行，Android却是一个多任务环境．这对使用音频的应用带来了特殊的挑战，
 * 因为只有一个音频输出而可能多个媒体都想用它．在Android2.2之前，没有内建的机制来处理这个问题，所以可能在某些情况下导致坏的用户体验．例如，
 * 当一个用户正在听音乐而另一个应用需要通知用户一些重要的事情时
 * ，用户可能由于音乐声音大而不能听的通知．从Android2.2开始，平台为应用提供了一个协商它们如何使用设备音频输出的途径
 * ，这个机制叫做音频焦点，AudioManager。
 * 当你的应用需要输出像乐音和通知之类的音频时，你应该总是请求音频焦点．一旦应用具有了焦点，它就可以自由的使用音频输出
 * ．但它总是应该监听焦点的变化．如果被通知丢失焦点，它应该立即杀死声音或降低到静音水平(有一个标志表明应选择哪一个)并且仅当重新获得焦点后才恢复大声播放。
 * 
 * 
 * 参数focusChange告诉你音频焦点如何发生了变化，它可以是以上几种值(它们都是定义在AudioManager中的常量)：
 * 
 * •AUDIOFOCUS_GAIN:你已获得了音频焦点．
 * 
 * •AUDIOFOCUS_LOSS:你已经丢失了音频焦点比较长的时间了．你必须停止所有的音频播放．因为预料到你可能很长时间也不能再获音频焦点，
 * 所以这里是清理你的资源的好地方．比如，你必须释放MediaPlayer．
 * 
 * •AUDIOFOCUS_LOSS_TRANSIENT:你临时性的丢掉了音频焦点，很快就会重新获得．你必须停止所有的音频播放，但是可以保留你的资源，
 * 因为你可能很快就能重新获得焦点．
 * 
 * •AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:你临时性的丢掉了音频焦点，但是你被允许继续以低音量播放，而不是完全停止．
 * 
 * 当第三方播放器也使用了这个机制的话，当你的应用获取声音焦点之后，第三方播放器失去焦点，做了暂停处理，即会停止播放。
 * 这样就不会出现两个播放器同时播放音乐的情况了。而如果第三方播放器没有经过处理，不管有没有焦点都一直播放，那就没有办法了
 * 
 * 
 * @author Administrator
 *
 */
public class AudioManagerUtils {
	private static final String TAG = "AudioFocusChangeListener";

	// * 首先，注册一个焦点监听器OnAudioFocusChangeListener 。
	private OnAudioFocusChangeListener mAudioFocusChangeListener = null;
	private AudioManager mAudioMgr;
	private boolean isNeedPlay;// 是否临时暂停
	private Context context;

	public AudioManagerUtils(Context context) {
		this.context = context;
	}

	public AudioManager getAudioManager() {
		if (mAudioMgr == null)
			mAudioMgr = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
		return mAudioMgr;
	}

	/**
	 * <pre>
	 * 返回当前音频模式，如 MODE_NORMAL（普通）, MODE_RINGTONE（铃声）, or MODE_IN_CALL（通话）
	 * @see AudioManager#getMode()
	 * @return
	 * </pre>
	 */
	public String getModelName() {
		switch (getAudioManager().getMode()) {
		case AudioManager.MODE_NORMAL:
			return "普通";
		case AudioManager.MODE_RINGTONE:
			return "铃声";
		case AudioManager.MODE_IN_CALL:
			return "通话";
		case AudioManager.MODE_CURRENT:
			return "现在";
		case AudioManager.MODE_INVALID:
			return "无效";
		case AudioManager.MODE_IN_COMMUNICATION:
			return "通信";
		}
		return "--";
	}

	/**
	 * <pre>
	 * 设置声音模式，可取值MODE_NORMAL（普通）, MODE_RINGTONE（铃声）, or MODE_IN_CALL（通话）
	 * @see AudioManager#setMode(int)
	 * @param mode
	 * </pre>
	 */
	public void setMode(int mode) {
		getAudioManager().setMode(mode);
	}
	
	/**
	 * <pre>
	 * 如RINGER_MODE_NORMAL（普通）、RINGER_MODE_SILENT（静音）、RINGER_MODE_VIBRATE（震动）
	 * @return 
	 * </pre>
	 */
	public String getRingerModeName(){
		switch(getAudioManager().getRingerMode()){
		case AudioManager.RINGER_MODE_NORMAL:
			return "普通";
		case AudioManager.RINGER_MODE_SILENT:
			return "静音";
		case AudioManager.RINGER_MODE_VIBRATE:
			return "震动";
		}
		return "--";
	}
	
	/**
	 * <pre>
	 * 改变铃声模式 
	 * @see AudioManager#setRingerMode(int)
	 * @param ringerMode  如RINGER_MODE_NORMAL（普通）、RINGER_MODE_SILENT（静音）、RINGER_MODE_VIBRATE（震动）
	 * </pre>
	 */
	public void setRingerMode(int ringerMode){
		getAudioManager().setRingerMode(ringerMode);
	}
	
	/**
	 * <pre>
	 * 取得当前手机的音量，最大值为7，最小值为0，当为0时，手机自动将模式调整为“震动模式”
	 * @param streamType
	 * @return
	 * </pre>
	 */
	public int getStreamVolume(int streamType){
		return getAudioManager().getStreamVolume(streamType);
	}
	
	/**
	 * <pre>
	 * 获得当前手机最大铃声
	 * @param streamType
	 * @return
	 * </pre>
	 */
	public int getStreamMaxVolume(int streamType){
		return getAudioManager().getStreamMaxVolume(streamType);
	}

	/**
	 * <pre>
	 * 用来控制手机音量大小
	 * @see AudioManager#adjustVolume(int, int)
	 * @param direction 参数为AudioManager.ADJUST_LOWER 时，可将音量调小一个单位，传入AudioManager.ADJUST_RAISE时，则可以将音量调大一个单位
	 * @param flags
	 * </pre>
	 */
	public void adjustVolume(int direction, int flags) {
		getAudioManager().adjustVolume(direction, flags);
	}

	/**
	 * <pre>
	 * （以步长）调节手机音量大小
	 * @see AudioManager#adjustStreamVolume(int, int, int)
	 * @param streamType 声音类型，可取为STREAM_VOICE_CALL（通话）、STREAM_SYSTEM（系统声音）、STREAM_RING（铃声）、STREAM_MUSIC（音乐）、STREAM_ALARM（闹铃声）
	 * @param direction 调整音量的方向，可取ADJUST_LOWER（降低）、ADJUST_RAISE（升高）、ADJUST_SAME
	 * @param flags 可选的标志位 ,调出系统音量控制AudioManager.FX_FOCUS_NAVIGATION_UP
	 * </pre>
	 */
	public void adjustStreamVolume(int streamType, int direction, int flags) {
		getAudioManager().adjustStreamVolume(streamType, direction, flags);
	}

	/**
	 * <pre>
	 * 直接设置音量大小
	 * @see AudioManager#setStreamVolume(int, int, int)
	 * @param streamType 声音类型，可取为STREAM_VOICE_CALL（通话）、STREAM_SYSTEM（系统声音）、STREAM_RING（铃声）、STREAM_MUSIC（音乐）、STREAM_ALARM（闹铃声）
	 * @param direction 调整音量的方向，可取ADJUST_LOWER（降低）、ADJUST_RAISE（升高）、ADJUST_SAME
	 * @param flags 可选的标志位 ,调出系统音量控制AudioManager.FX_FOCUS_NAVIGATION_UP
	 * </pre>
	 */
	public void setStreamVolume(int streamType, int index, int flags) {
		getAudioManager().setStreamVolume(streamType, index, flags);
	}

	/**
	 * <pre>
	 * Build.VERSION.SDK_INT表示当前SDK的版本，Build.VERSION_CODES.ECLAIR_MR1为SDK 7版本 ，
	 * 因为AudioManager.OnAudioFocusChangeListener在SDK8版本开始才有。
	 * </pre>
	 */
	private void createAudioFocusChangeListener() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
			mAudioFocusChangeListener = new OnAudioFocusChangeListener() {
				@Override
				public void onAudioFocusChange(int focusChange) {

					Log.d(TAG, "other audioFocus: " + focusChange);
					switch (focusChange) {
					case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 降低音量");
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_LOSS_TRANSIENT 长期，被其他播放器抢走等，不需要恢复");
						// request=false;
						// isNeedPlay = isPlaying();
						if (isNeedPlay) {
							// stop();
						}
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
						Log.d(TAG,
								"OnAudioFocusChangeListener AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK 暂时失去，需要恢复");
						// isNeedPlay=isPlaying();
						if (isNeedPlay) {
							// pause();
						}
						break;
					case AudioManager.AUDIOFOCUS_LOSS:
						// 失去焦点之后的操作
						Log.d(TAG, "lose audioFocus");
						// request=false;//暂存当前播放状态，待恢复
						isNeedPlay = false;
						// stop();//如果正在播放则停止
						break;
					case AudioManager.AUDIOFOCUS_GAIN:
						// 获得焦点之后的操作
						Log.d(TAG, "get audioFocus");
						// if (isNeedPlay && !isPlaying()) {//判断是否需要回复
						// start();//恢复播放
						// }
						break;
					default:
						Log.d(TAG, "unknown audioFocus");
						break;
					}
				}
			};
		}
	}

	/**
	 * 要请求音频焦点，你必须从AudioManager mAudioMgr调用requestAudioFocus()
	 * 
	 * @param context
	 */
	public boolean requestAudioFocus(
			OnAudioFocusChangeListener mAudioFocusChangeListener) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
			if (getAudioManager() != null) {
				Log.i(TAG, "Request audio focus");
				int ret = mAudioMgr.requestAudioFocus(
						mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN);
				if (ret != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					Log.i(TAG, "request audio focus fail. " + ret);
				}
				return ret == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
			}
		}
		return false;
	}

	/**
	 * 放弃焦点
	 */
	public boolean abandonAudioFocus(
			OnAudioFocusChangeListener mAudioFocusChangeListener) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
			if (mAudioMgr != null) {
				Log.i(TAG, "Abandon audio focus");
				int result = mAudioMgr
						.abandonAudioFocus(mAudioFocusChangeListener);
				mAudioMgr = null;
				return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
			}
		}
		return true;
	}

	int currVolume;

	/**
	 * <pre>
	 * 打开扬声器
	 * @param mContext
	 * </pre>
	 */
	public void OpenSpeaker(Context mContext) {

		try {
			AudioManager audioManager = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setMode(AudioManager.ROUTE_SPEAKER);
			currVolume = audioManager
					.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

			if (!audioManager.isSpeakerphoneOn()) {
				audioManager.setSpeakerphoneOn(true);

				audioManager
						.setStreamVolume(
								AudioManager.STREAM_VOICE_CALL,
								audioManager
										.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
								AudioManager.STREAM_VOICE_CALL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 关闭扬声器
	 * @param mContext
	 * </pre>
	 */
	public void CloseSpeaker(Context mContext) {

		try {
			AudioManager audioManager = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager != null) {
				if (audioManager.isSpeakerphoneOn()) {
					audioManager.setSpeakerphoneOn(false);
					audioManager.setStreamVolume(
							AudioManager.STREAM_VOICE_CALL, currVolume,
							AudioManager.STREAM_VOICE_CALL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Toast.makeText(context,"揚聲器已經關閉",Toast.LENGTH_SHORT).show();
	}

}
