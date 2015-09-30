package wu.a.activity;

import java.util.List;

import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class SocialConstants {
	public static final boolean OUTLINE = true;
	public static final boolean DEBUG = true;
	public static final String URL = "";

	public static void log(String log) {
		if (DEBUG) {
			Log.d("iRace_debug", log);
		}
	}

	public static void log(String log, List ls) {
		if (DEBUG) {
			if (ls != null) {
				Log.d("iRace_debug", log + " size=" + ls.size());
				for (Object obj : ls) {
					Log.d("iRace_debug", log + obj.toString());
				}
			} else {
				Log.d("iRace_debug", log + ls);
			}
		}
	}

	/**
	 * 读取新增用户 消息 俱乐部 红点
	 * 
	 * @param userId
	 * @return
	 */
	public static String getCheckNewFriendRequest(String userId, String userPwd) {
		return URL + "/iRaceBE/social_checkVerUpdate.do?userID=" + userId
				+ "&pwd=" + userPwd;
	}

	/**
	 * 读取全部用户
	 * 
	 * @param userId
	 * @return
	 */
	public static String getAllFriendRequest(String userId, String userPwd) {
		return URL + "/iRaceBE/social_allFriend.do?userID=" + userId + "&pwd="
				+ userPwd;
	}

	/**
	 * 操作用户请求
	 * 
	 * @param userIduserID当前用户ID
	 * @param friendId
	 *            friendID当前操作的好友ID
	 * @param exeType
	 *            exeType 执行类型 =pass 通过 refuse 拒绝 del 删除 add 添加好友(查询好友时使用)
	 * @return reqest
	 * @see Reply
	 */
	public static String getExeFriend(String userId, String userPwd,
			int friendId, String exeType) {
		return URL + "/iRaceBE/social_exeFriend.do?userID=" + userId + "&pwd="
				+ userPwd + "&friendID=" + friendId + "&exeType=" + exeType;
	}

	/**
	 * 查找用户 u
	 * 
	 * @param userId
	 *            serID当前用户ID
	 * @param userPwd
	 * @param param
	 *            param 昵称, 手机号, 邮箱, 内部用户ID（扫码取得）
	 * @param type
	 *            type 查询类型 =id 等于 id 时指定 param 代表 ID精准查询 否则为 模糊查询
	 * @return
	 */
	public static String getFindFriend(String userId, String userPwd,
			String param, String type) {
		return URL + "/iRaceBE/social_findFriend.do?userID=" + userId + "&pwd="
				+ userPwd + "&param=" + param + "&type=" + type;
	}

	/**
	 * 显示消息 （每读一次取20条）
	 * 
	 * @param userId
	 *            userID当前用户ID
	 * @param userPwd
	 *            step=20 (直接先写死20 二期再升级可配置)
	 * @param step
	 * @param startId
	 *            空 或 上拉刷新时最后一条
	 * @return
	 */
	public static String getQueryBBS(String userId, String userPwd,
			int startId, int step) {
		return URL + "/iRaceBE/social_queryBBS.do?userID=" + userId
				+ "&userPwd=" + userPwd + "&startID=" + startId + "&step="
				+ step;
	}

	/**
	 * 查询俱乐部
	 * 
	 * @param userId
	 *            userID当前用户ID
	 * @param userPwd
	 *            pwd 本地存的 密文密码
	 * @param lat
	 *            lat 当前纬度 (android从 RaceActivity.currPostion.latitute取)
	 * @param lng
	 *            lng 当前经度 (android从 RaceActivity.currPosition.longitude取)
	 * @param city
	 *            city 当前城市名 （RaceActivity.currCityName）
	 * @param name
	 *            name 俱乐部名e
	 * @param clubID
	 *            俱乐部ID Name 和 clubID 互斥 查询车队名 name=队名&clubID= 查询俱乐部ID
	 *            name=&clubID=123
	 * 
	 * @return
	 */
	public static String getQueryClub(String userId, String userPwd,
			double lat, double lng, String city, String name, String clubID) {
		return URL + "/iRaceBE/social_queryClub.do?userID=" + userId + "&pwd="
				+ userPwd + "&lat=" + lat + "&lng=" + lat + "&city=" + city
				+ "&name=" + name + "&clubID=" + clubID;
	}

	public static final int CLUB_RANK_TYPE_NATIONWIDE = 1;
	public static final int CLUB_RANK_TYPE_LOCAL = 2;
	public static final int CLUB_RANK_TYPE_MY_CLUB = 3;

	/**
	 * <pre>
	 * 排序俱乐部  全国 本地
	 * userID 当前用户ID
	 * pwd 本地存的密文密码
	 * city  当前城市名（RaceActivity.currCityName）
	 * type  1 全国    2 本市   3 我的俱乐部
	 * @see #CLUB_RANK_TYPE_LOCAL 
	 * @see #CLUB_RANK_TYPE_NATIONWIDE
	 * @see #CLUB_RANK_TYPE_MY_CLUB
	 * @param userId
	 * @param userPwd
	 * @param type 
	 * @return
	 * </pre>
	 */
	public static String getRankClubRequest(String userId, String userPwd,
			int type) {
		return URL + "/iRaceBE/social_rankClub.do?userID=" + userId + "&pwd="
				+ userPwd + "&type=" + type;
	}

	/**
	 * 俱乐部详情 serID当前用户ID pwd 本地存的 密文密码 clubID 点选的俱乐部ID
	 * 
	 * @param userId
	 * @param userPwd
	 * @param clubID
	 * @return
	 */
	public static String getClubInfo(String userId, String userPwd, long clubID) {
		return URL + "/iRaceBE/social_clubInfo.do?userID=" + userId + "&pwd="
				+ userPwd + "&clubID=" + clubID;
	}

	/**
	 * <pre>
	 * 社区用户信息显示
	 * @param userId userID查询用户ID
	 * @return
	 * </pre>
	 */
	public static String getPersonInfo(int userId) {
		return URL + "/iRaceBE/social_personInfo.do?userID=" + userId;
	}

	public static String getJoinClub(String userId, long clubId) {
		return URL + "/iRaceBE/social_joinClub.do?userID=" + userId
				+ "&clubID=" + clubId;
	}

}
