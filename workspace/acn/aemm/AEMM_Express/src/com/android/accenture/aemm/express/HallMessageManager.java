package com.android.accenture.aemm.express;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.android.accenture.aemm.express.updataservice.ListenerService;

public class HallMessageManager extends ViewFlipper{
	public static final String CLEAR_MESSAGE="clear_message";
	public static final String ADD_MESSAGE="add_hall_message";
	public static final String UPDATE_FINISH_MESSAGE="update_finish_message";
	public static final String HALL_ENABLE_MESSAGE="hall_enable_message";
	public static final String APP_LOCK_MESSAGE="app_lock_message";
	private Context context;
	
	public HallMessageManager(Context context) {
        super(context);
        this.context=context;
        setKind(context,0);
	}
	
    public HallMessageManager(Context context, AttributeSet attrs) {
        super(context, attrs);
		setKind(context,0);
        this.context=context;
    }
    public void onCreate(){
    	receiver();
    }
    public void onDestroyed(){
    	clearMessage(context);
    	getContext().unregisterReceiver(mReceiver);
    }

	public void setKind(Context context,int position){
		   switch (position) {
	        case 0:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_up_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_up_out));
	            break;
	        case 1:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_left_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_left_out));
	            break;
	        case 2:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    android.R.anim.fade_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    android.R.anim.fade_out));
	            break;
	        default:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.hyperspace_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.hyperspace_out));
	            break;
	        }
	}
	/**
	 * <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:gravity="center_horizontal"
                        android:textSize="26sp"
                        android:text="@string/animation_2_text_3"/>
	 */
	public void addMessage(HallMessagedb msg){
		MessageView mv;
		for(int i=0;i<getChildCount();i++){
			Log.v("VV","i:"+i+msg.getMessage());
			mv=(MessageView) getChildAt(i);
			if(mv.message.getKind()==msg.getKind()){
				mv.setMessage(msg);
				msg=null;
				break;
			}
		}
		if (msg != null) {
			mv = new MessageView(context,msg);
			mv.setSingleLine();
//			LayoutParams lp = new LayoutParams((int) (context.getResources()
//					.getDimension(R.dimen.message_w)), (int) (context
//					.getResources().getDimension(R.dimen.message_h)));
//			lp.gravity = Gravity.CENTER_VERTICAL;
			mv.setId(msg.getKind());
			mv.setBackgroundColor(0xff);
			this.addView(mv);
		}
	}
	public void receiver(){
		final IntentFilter filter = new IntentFilter();
		filter.addAction(HallMessageManager.CLEAR_MESSAGE);
        filter.addAction(HallMessageManager.ADD_MESSAGE);
        filter.addAction(HallMessageManager.UPDATE_FINISH_MESSAGE);
        filter.addAction(ListenerService.NEW_APP_PUSH);
        filter.addAction(HallMessageManager.APP_LOCK_MESSAGE);
        filter.addAction(HallMessageManager.HALL_ENABLE_MESSAGE);
        getContext().registerReceiver(mReceiver, filter);
	}
	
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            
            Log.v("VV","action"+ action);
            if(CLEAR_MESSAGE.equals(action)){
            	removeAllViews();
            }else
            if (ADD_MESSAGE.equals(action)) {
            	Log.v("VV","i:"+ ADD_MESSAGE);
            	String msg=intent.getStringExtra("msg");
            	int startTime=intent.getIntExtra("start", 0);
            	int maxTime=intent.getIntExtra("max",HallMessagedb.EVER);
            	int delayTime=intent.getIntExtra("delay",ViewFlipper.DEFAULT_INTERVAL);
            	int kind=intent.getIntExtra("kind", Integer.MAX_VALUE);
            	boolean isClear=intent.getBooleanExtra("clear", false);
            	if(isClear){
            		removeAllViews();
            	}
            	
            	
            	addMessage(new HallMessagedb(msg, startTime, maxTime, delayTime, kind));
            } 
            else if(UPDATE_FINISH_MESSAGE.equals(action)){
            	
            	Log.v("VV","i:"+ UPDATE_FINISH_MESSAGE);
            	Main.finishUpdata();
            }
            else if(intent.getAction().equals(ListenerService.NEW_APP_PUSH)){
    			Log.v("VV","message receve NEW_APP_PUSH");
    			
    			if(Main.mHall!=null){
    				Log.v("VV","NEW_APP_PUSH hall");
    				long ids[]=intent.getLongArrayExtra("appids");
    				Message message = new Message();  
    	            message.what =Main.NEWAPPPUSH;
    	            if(Main.isVersionOn){
    	            	Main.mHall.handler.sendMessage(message);
    	            }else{
    	            	Main.mHall.readUpdata(ids);
    	            }
    			}else{
    				Log.v("VV","hall is null");
    			}
    		}
            else if(intent.getAction().equals(HALL_ENABLE_MESSAGE)){
                boolean flag=intent.getBooleanExtra("enabled", false);
                if(Main.mHall!=null){
    				Log.v("VV","HALL_ENABLE_MESSAGE");
    				Message message = new Message();  
    	            message.what =Main.HALL_ENABLE_MESSAGE;
    	            message.obj=new Boolean(flag);
    	            Main.mHall.handler.sendMessage(message);
    			}else{
    				Log.v("VV","hall is null");
    			}
            }
            else if(intent.getAction().equals(APP_LOCK_MESSAGE)){
            	Log.v(ScrollAdapter.APPLOCK,"APP_LOCK_MESSAGE");
            	
            	String ids[]=intent.getStringArrayExtra("appids");
            	boolean flags[]=intent.getBooleanArrayExtra("flags");
            	if (ids == null && flags == null){
            		Log.v(ScrollAdapter.APPLOCK,"APP_LOCK_MESSAGE but is null" );
            		return;
            	}
            	 if(Main.mHall!=null){
            		 Log.i(ScrollAdapter.APPLOCK,"HallMessageManager receve broadcast appLock."+ String.valueOf(ids.length) + String .valueOf(flags.length));
            		 if(Main.debugView){
            		 for(int i=0;i<ids.length&&i<flags.length;i++){
            			 Log.i(ScrollAdapter.APPLOCK,"HallMessageManager appLock "+ids[i]+"->"+flags[i]);
            		 }
            		 }
            		 Main.mHall.setAppEnables(ids, flags);
     			}
            }
        }
    };
    
    /**发送消息
     * @param context
     * @param msg
     */
    public static void sendMessage(Context context,String msg,int sTime,int totleTime,int delayTime,int kind){
    	Intent message=new Intent(HallMessageManager.ADD_MESSAGE);
    	message.putExtra("msg",msg);
    	message.putExtra("start",sTime);
    	message.putExtra("max",totleTime);
    	message.putExtra("delay",delayTime);
    	message.putExtra("kind",kind);
    	context.sendBroadcast(message);
    }
    public static void addMessage(Context context,HallMessagedb msg){
    	sendMessage(context,msg.getMessage(),msg.getStartTime(),msg.getMaxTime(),msg.getDelayTime(),msg.getKind());
    }
    public static void clearMessage(Context context){
    	Intent message=new Intent(HallMessageManager.CLEAR_MESSAGE);
    	context.sendBroadcast(message);
    }
    //告知更新结束
    public static void finishUpdate(Context context){
    	Log.v("finishUpdate","i:"+ UPDATE_FINISH_MESSAGE);
    	Intent message= new Intent(HallMessageManager.UPDATE_FINISH_MESSAGE);
    	context.sendBroadcast(message);
    }
    public static void cancelMessage(Context context,int kind){
    	sendMessage(context,"", 0,0,0,kind);
    }
    public static void sendMessage(Context context,String msg,int kind){
    	sendMessage(context,msg, 0,HallMessagedb.result_maxTime,
				HallMessagedb.status_delayTime,kind);
    }
    public static void startUpdata(Context context){
    	clearMessage(context);
    	sendMessage(context,(String) context.getText(R.string.updata_executing), 0,HallMessagedb.EVER,
				HallMessagedb.status_delayTime, HallMessagedb.STATUSMSG);
    }
    public static void updataFinish(Context context,String msg){
    	sendMessage(context,msg, 0,HallMessagedb.result_maxTime,
				HallMessagedb.status_delayTime, HallMessagedb.STATUSMSG);
    	HallMessageManager.finishUpdate(context);
    }
       public static void newAppLoader(Context context){
    	  Log.i("VV","APP_UPDATE ");
    	Intent message=new Intent(ListenerService.NEW_APP_PUSH);
    	context.sendBroadcast(message);
    }
       
       public static void newAppLoader(Context context,long ids[]){
     	  Log.i("VV","APP_UPDATE ");
     	Intent intent=new Intent(ListenerService.NEW_APP_PUSH);
     	intent.putExtra("appids",ids);
     	context.sendBroadcast(intent);
     }
       
      public static void HallAppInstallEnabled(Context context,boolean flag){
     	  Log.i("newAppLoader","HallAppInstallEnabled ");
     	Intent message=new Intent(HALL_ENABLE_MESSAGE);
     	message.putExtra("enabled", flag);
     	context.sendBroadcast(message);
     }
       
     public static void HallAppLock(Context context,String[] ids, boolean []flags){
      	Log.i(ScrollAdapter.APPLOCK,"HallMessageManager send broadcast appLock.");
      	Intent intent=new Intent(APP_LOCK_MESSAGE);
      	intent.putExtra("appids",ids);
		intent.putExtra("flags",flags);
		
      	context.sendBroadcast(intent);
    }
       
	public void test() {
		addMessage(new HallMessagedb("常显示信息测试2s", 1000, -1, 2000, 1));
		addMessage(new HallMessagedb("dfsfsdfsfasdfsafasfasfsafsfsafsfdfadsfa测试信息2秒停留", 1000, 10000, 2000, 2));
		addMessage(new HallMessagedb("10秒停留信息测试3", 1000, 1000, 10000, 3));
		addMessage(new HallMessagedb("常显示信息测试4s", 1000, -1, 4000, 1));
	}
	public void test2(){
		removeAllViews();
		sendMessage(context, "remoall", 11);
	}

}
