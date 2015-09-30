package wu.a.wuliu.model;

/**
 * <pre>
 * 用户评论
 * @author junxu.wang
 * @d2015年7月12日
 * </pre>
 *
 */
public class UserComent {
	private String phone;
	private String commentContent;
	private int commentScore;
	private long time;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public int getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(int commentScore) {
		this.commentScore = commentScore;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
