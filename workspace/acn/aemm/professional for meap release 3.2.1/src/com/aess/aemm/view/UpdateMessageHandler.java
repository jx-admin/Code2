package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.aess.aemm.view.log.Logger;
import com.aess.aemm.view.log.LoggerFactory;

/**hall message manager
 * @author junxu.wang
 *
 */
public class UpdateMessageHandler {
	public static  Logger Log;
	/**清除所有消息*/
	public static final String CLEAR_MESSAGE="clear_message";
	public static final String ADD_MESSAGE="add_hall_message";
	public static final String UPDATE_FINISH_MESSAGE="update_finish_message";
	public static final String APP_LOCK_MESSAGE="app_lock_message";
//	public static final String NEW_APP_PUSH="NEW_APP_PUSH";
	public static final String UPDATE_NOT_FINISH_MESSAGE = "update_not_finish_message";
	public static final String USER="USER";
	
	private TextFlipper tFlipper;
	private MainView activity;
	public UpdateMessageHandler(MainView context){
		activity=context;
	}
	public void register(Context context){
		Log=LoggerFactory.getLogger(this.getClass());
		final IntentFilter filter = new IntentFilter();
		filter.addAction(UpdateMessageHandler.CLEAR_MESSAGE);
		filter.addAction(UpdateMessageHandler.ADD_MESSAGE);
		filter.addAction(UpdateMessageHandler.UPDATE_FINISH_MESSAGE);
		filter.addAction(UpdateMessageHandler.APP_LOCK_MESSAGE);
//		filter.addAction(UpdateMessageHandler.NEW_APP_PUSH);
		filter.addAction(UpdateMessageHandler.UPDATE_NOT_FINISH_MESSAGE);
		filter.addAction(UpdateMessageHandler.USER);
		context.registerReceiver(mReceiver, filter);
	}
    public void unregister(Context context){
    	ViewUtils.clearAllMessage(context);
    	context.unregisterReceiver(mReceiver);
    }
    public void setTextFlipper(TextFlipper tf){
    	tFlipper=tf;
    }
    public TextFlipper getTextFlipper(){
    	return tFlipper;
    }
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d("BroadcastReceiver="+ action);
            
            if(tFlipper==null){
            	return;
            }
            if(CLEAR_MESSAGE.equals(action)){
            	tFlipper.removeAllViews();
            }else
            if (ADD_MESSAGE.equals(action)) {
            	boolean isClear=intent.getBooleanExtra("clear", false);
            	HallMessagedb mHallMessagedb=(HallMessagedb) intent.getSerializableExtra("obj");
            	if(isClear){
            		tFlipper.removeAllViews();
            	}
            	Log.d("add message:"+mHallMessagedb.getInfo());
            	tFlipper.addMessage(mHallMessagedb);
            } 
            else if(UPDATE_FINISH_MESSAGE.equals(action)){

            	Log.d("recieve finishUpdateBroadcast");
            	if(activity!=null){
            		activity.finishUpdata();
            		tFlipper.removeAllViews();
            		for(HallMessagedb msg:resultMsgLs){
            			tFlipper.addMessage(msg);
            		}
            		resultMsgLs.clear();
            	}
            }
//            else if(USER.equals(action)){
//            	activity.startInfoMainView();
//            }
            else if(UPDATE_NOT_FINISH_MESSAGE.equals(action)){
            	if(activity!=null){
            		activity.setBusy(false);
            		tFlipper.removeAllViews();
            		for(HallMessagedb msg:resultMsgLs){
            			tFlipper.addMessage(msg);
            		}
            		resultMsgLs.clear();
            	}
            }
//            else if(intent.getAction().equals(UpdateMessageHandler.NEW_APP_PUSH)){
//    			if(activity!=null){
//    				Log.d("NEW_APP_PUSH hall");
//    				Message message = new Message();  
//    	            message.what =MainView.NEWAPPPUSH;
//    	            MainView.mHall.handler.sendMessage(message);
//    			}else{
//    				Log.d( "hall view is null or stop");
//    			}
//    		}
            else if(intent.getAction().equals(APP_LOCK_MESSAGE)){
            	
            	String ids[]=intent.getStringArrayExtra("appids");
            	boolean flags[]=intent.getBooleanArrayExtra("flags");
            	if (ids == null || flags == null){
            		ScrollAdapter.Log.d("APP_LOCK_MESSAGE but ids == null || flags == null" );
            		return;
            	}
            	 if(activity!=null){
            		 ScrollAdapter.Log.d("HallMessageManager receve broadcast appLock."+ String.valueOf(ids.length) + String .valueOf(flags.length));
            		 if(MainView.VIEWDEBUG){
	            		 for(int i=0;i<ids.length&&i<flags.length;i++){
	            			 ScrollAdapter.Log.d("HallMessageManager appLock "+ids[i]+"->"+flags[i]);
	            		 }
            		 }
            		 activity.setAppEnables(ids, flags);
     			}
            }
        }
    };
    
    /**Send a message to UI
     * @param context
     * @param msg
     */
    public static void addMessage(Context context,HallMessagedb mHallMessagedb){
    	Intent message=new Intent(UpdateMessageHandler.ADD_MESSAGE);
    	message.putExtra("obj", mHallMessagedb);
    	context.sendBroadcast(message);
    }
    
    /**Removal of an update message
     * @param context
     * @param kind
     */
    static void removeMessage(Context context,int kind){
    	UpdateMessageHandler.addMessage(context,new HallMessagedb(kind));
    }
    
    /**remove all the update message from hall ui.
     * @param context
     */
    static void clearAllMessage(Context context){
    	Intent message=new Intent(UpdateMessageHandler.CLEAR_MESSAGE);
    	context.sendBroadcast(message);
    }
    
    /**Notification to the UI to application update
     * @param context
     */
//    @Deprecated
//    public static void newAppLoader(Context context){
//    	ScrollAdapter.Log.d("APP_UPDATE ");
//    	Intent message=new Intent(UpdateMessageHandler.NEW_APP_PUSH);
//    	context.sendBroadcast(message);
//    }
       
    /**告知大厅可用性更新
     * @param context
     * @param flag
     */
    @Deprecated
    static void HallAppLock(Context context,String[] ids, boolean []flags){
      	ScrollAdapter.Log.d("HallMessageManager send broadcast appLock.");
      	Intent intent=new Intent(UpdateMessageHandler.APP_LOCK_MESSAGE);
      	intent.putExtra("appids",ids);
		intent.putExtra("flags",flags);
      	context.sendBroadcast(intent);
    }
       
	public void test(Context context) {
//		addMessage(context,new HallMessagedb("addMessage broadcast",0,50000,10000,11));
//		tFlipper.addMessage(new HallMessagedb("常显示信息测试2s", 1000, -1, 2000, 1));
		tFlipper.addMessage(new HallMessagedb("dfsfsdfsfasdfsafasfasfsafsfsafsfdfadsfa测试信息2秒停留dfsfsdfsfasdfsafasfasfsafsfsafsfdfadsfadadfsfsdfsfasdfsaffadfsfsdfsfasdfsafasfasfsafsfsaf", 1000, 20000, 5000, 2));
		tFlipper.addMessage(new HallMessagedb("息2秒停留息2秒停留测试信息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留息2秒停留", 1000, -1, 1000, 23));
//		tFlipper.addMessage(new HallMessagedb("10秒停留信息测试3", 1000, 1000, 10000, 3));
//		tFlipper.addMessage(new HallMessagedb("常显示信息测试4s", 1000, -1, 4000, 1));
	}
	private static List <HallMessagedb>resultMsgLs=new ArrayList <HallMessagedb>();
	static void addResultMessage(HallMessagedb message){
		resultMsgLs.add(message);
	}
	
}
