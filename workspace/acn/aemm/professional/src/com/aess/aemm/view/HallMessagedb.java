package com.aess.aemm.view;

import java.io.Serializable;

public class HallMessagedb implements Serializable{
	private static final long serialVersionUID = -4660385453075520752L;
	public static final int UPDATEMSG=188;
	public static final int APPMSG = 21;
	public static final int STATUSMSG = 1;
	public static final int RESULTMSG = 2;
	
	public static final int status_maxTime = 3*1000;
	public static final int status_delayTime = 10000;
	public static final int result_maxTime = 60*1000;
	
	public static final int result_delayTime = 10*1000;
	
	public static final int EVER=-1;
	private int startTime;
	private int maxTime=EVER;
	private int delayTime;
	private String message;
	private int kind;
	public String getInfo(){
		if(message==null){
			return "";
		}
		return "kind:"+kind+" maxTime:"+maxTime+" startTime:"+startTime+" delayTime:"+delayTime+" message:"+message;
	}
	public HallMessagedb(String msg,int startTime,int maxTime,int delayTime,int kind){
		this.message=msg;
		this.startTime=0;
		this.maxTime=maxTime;
		this.delayTime=delayTime;
		this.kind=kind;
	}
	public HallMessagedb(String msg,int kind){
		this(msg,0,HallMessagedb.result_maxTime,HallMessagedb.status_delayTime,kind);
	}
	/**将要删除的消息
	 * @param kind
	 */
	public HallMessagedb(int kind){
		this(null,0,0,0,kind);
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public boolean isOutTime(){
		return (maxTime!=EVER&&startTime>=maxTime);
	}
	public void nextTime() {
		startTime+=delayTime;
	}
}
