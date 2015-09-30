package com.aess.aemm.update.net;


public class Result {
	
	private int resultCode;
	private Object data;
	private Object flag;
	
	public void setResultCode(int resultCode){
		this.resultCode=resultCode;
	}

	public int getResultCode(){
		return resultCode;
	}
	
	public void setData(Object response){
		this.data=response;
	}
	
	public Object getData(){
		return data;
	}

	public Object getFlag() {
		return flag;
	}

	protected void setFlag(Object flag) {
		this.flag = flag;
	}
}
