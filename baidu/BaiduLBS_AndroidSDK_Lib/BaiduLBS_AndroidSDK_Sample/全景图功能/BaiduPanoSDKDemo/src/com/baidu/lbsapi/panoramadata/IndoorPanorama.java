package com.baidu.lbsapi.panoramadata;

/**
 * 内景数据结构
 */
public class IndoorPanorama {


	protected String mIId;//内景ID
	
	protected String mName;//内景名称
	
	protected int mMcLocationX;
	
	protected int mMcLocationY;
	
	public IndoorPanorama() {
		
	}
	
	/**
	 * 获取内景ID
	 * @return
	 */
	public String getIId() {
		return mIId;
	}
	
	/**
	 * 获取内景名称
	 * @return
	 */
	public String getIndoorName() {
		return mName;
	}
	
	/**
	 * 获取x坐标
	 * @return
	 */
	public int getLocationX() {
		return mMcLocationX;
	}
	
	/**
	 * 获取Y坐标
	 * @return
	 */
	public int getLocationY() {
		return mMcLocationY;
	}


}
