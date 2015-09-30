package com.baidu.lbsapi.panoramadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 内景数据类
 * @author lvjingxiao
 *
 */
public class PanoramaParser  {

	public static final String KEY_NAME = "title";
	public static final String KEY_PID = "pid";
	public static final String KEY_URL = "url";	

	/**
	 * 解析sdata, qsdata请求的数据
	 * @param json
	 * @return
	 */
	public static Panorama parseSdataJson(String json) {
		Panorama panorama = new Panorama();
		if(TextUtils.isEmpty(json)) {
			return null;
		}
		JSONObject jo = null;
		try {
			jo = new JSONObject(json);
			int errno = getErrorNo(jo);
			if(errno == 0) {
				JSONArray contentArray = jo.getJSONArray("content");
				JSONObject contentJo = null;
				if(contentArray != null) {
					contentJo = contentArray.getJSONObject(0);
				}
				panorama.mMoveDir = contentJo.getDouble("MoveDir");

				panorama.mMcLocationX = contentJo.getInt("X");
				panorama.mMcLocationY = contentJo.getInt("Y");
				panorama.mPId = contentJo.getString("ID");
				panorama.mType = contentJo.getString("Type");
				if(panorama.mType.equals(Panorama.PANO_TYPE_STREET)) {
					String rname = contentJo.getString("Rname");
					if(TextUtils.isEmpty(rname)) {
						rname = "未知道路";
					}
					panorama.mStreetName = rname;
				}else {
					String rname = contentJo.getString("Rname");
					panorama.mName = rname;
				}
				
				JSONArray entersArray = contentJo.getJSONArray("Enters");
				if(entersArray != null) {
					//如果附近有内景点的话
					for(int i = 0; i < entersArray.length(); i++) {
						IndoorPanorama indoorData = new IndoorPanorama();
						indoorData.mIId = entersArray.getJSONObject(i).getString("IID");
						indoorData.mName = entersArray.getJSONObject(i).getString("Name");
						indoorData.mMcLocationX = entersArray.getJSONObject(i).getInt("X");
						indoorData.mMcLocationY = entersArray.getJSONObject(i).getInt("Y");
						List<IndoorPanorama> data = new ArrayList<IndoorPanorama>();
						data.add(indoorData);
						panorama.mNearIndoorScapeList = data;
					}
				}
				
			}else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return panorama;
	}
	
    /**
     * 解析通过iid获取的内景描述信息的json
     * @param json
     */
	public static ArrayList<HashMap<String, String>> parseIdataJson(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		ArrayList<HashMap<String, String>> indoorAlbumInfoList = new ArrayList<HashMap<String,String>>();
		JSONObject jo = null;
		try {
			jo = new JSONObject(json);
			int errno = getErrorNo(jo);
			if(errno == 0) {
				JSONArray contentArray = jo.getJSONArray("content");
				JSONObject contentJo = null;
				if(contentArray != null) {
					contentJo = contentArray.getJSONObject(0);
				}
				JSONObject panoinfo = contentJo.getJSONObject("interinfo");
				JSONArray photoArrays = panoinfo.getJSONArray("Floors");
				for(int i = 0; photoArrays != null && i < photoArrays.length(); i++) {
					JSONObject pointsJo = photoArrays.getJSONObject(i);
					JSONArray pointsArray = pointsJo.getJSONArray("Points");
					
					for(int j = 0; pointsArray != null && j < pointsArray.length(); j++) {
						String name = pointsArray.getJSONObject(j).getString("name");
						String pid = pointsArray.getJSONObject(j).getString("PID");
						String ablumUrl = formUrlString(pid);
						HashMap<String, String> hashmap = new HashMap<String, String>();
						hashmap.put(KEY_NAME, name);
						hashmap.put(KEY_PID, pid);
						hashmap.put(KEY_URL, ablumUrl);
						indoorAlbumInfoList.add(hashmap);						
					}
				}
			}else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return indoorAlbumInfoList;
	}
	
	
	private static int getErrorNo(JSONObject jo) throws JSONException {
		JSONObject resultJo = jo.getJSONObject("result");
		int errno = resultJo.getInt("error");
		return errno;
	}
	
    private static String formUrlString(String pid) {
        return "http://pcsv1.map.bdimg.com/scape/?qt=pdata&sid=" + pid + "&pos=0_0&z=0";
    }

}
