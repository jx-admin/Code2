package com.baidu.lbsapi.panoramadata;

import java.util.List;

/**
 * 全景元数据结构封装
 */
public class Panorama {
	
	public static final String PANO_TYPE_STREET = "street";
	public static final String PANO_TYPE_INTER = "inter";

	//全景pid
	protected String mPId;
	//所在道路名称
	protected String mStreetName;
	//内景点名称
	protected String mName;
	//车头朝向
	protected double mMoveDir;
	//全景所在的位置(墨卡托坐标)
	protected int mMcLocationX;
	protected int mMcLocationY;
	//全景类型 街景：street 内景：inter
	protected String mType;

	
	//全景附近内景信息列表
	protected List<IndoorPanorama> mNearIndoorScapeList;

	/**
	 * default access constructor, the developer can not create an instance.
	 */
	Panorama() {

	}

	/**
	 * 获取全景的唯一标识 ID
	 * 
	 * @return
	 */
	public String getPId() {
		return mPId;
	}

	/**
	 * 获取该全景所在街道名
	 * 
	 * @return
	 */
	public String getStreetName() {
		return mStreetName;
	}
	
	/**
	 *  返回内景点名称
	 * @return
	 */
	public String getName() {
		return mName;
	}

	/**
	 * 获取车头朝向信息
	 * 
	 * @return
	 */
	public double getMoveDir() {
		return mMoveDir;
	}
	
	/**
	 * 获取全景位置的墨卡托坐标
	 * @return
	 */
	public int getLocationX() {
		return mMcLocationX;
	}
	
	/**
	 * 获取全景位置的墨卡托坐标
	 * @return
	 */
	public int getLocationY() {
		return mMcLocationY;
	}
	
	/**
	 * 返回全景类型，是街景还是内景
	 * @return
	 */
	public String getPanoType() {
		return mType;
	}
	
	/**
	 * 获取内景列表点信息列表
	 * @return
	 */
	public List<IndoorPanorama> getNearIndoorScapeList() {
		return mNearIndoorScapeList;
	}

}
