package com.image.indicator.entity;

/**
 * 新闻实体类
 * @Description: 新闻实体类

 * @File: News.java

 * @Package com.image.indicator.entity

 * @Author Hanyonglu

 * @Date 2012-6-18 下午02:25:02

 * @Version V1.0
 */
public class News {
	// ID
	private int id;
	// 简要标题
	private String simpleTitle;
	// 完整标题
	private String fullTitle;
	// 链接地址
	private String newsUrl;
	// 新闻内容
	private String newsContent;
	// 查看次数
	private int viewCount;
	// 评论次数
	private int commentCount;
	// 是否被读过
	private boolean isReaded;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSimpleTitle() {
		return simpleTitle;
	}
	
	public void setSimpleTitle(String simpleTitle) {
		this.simpleTitle = simpleTitle;
	}
	
	public String getFullTitle() {
		return fullTitle;
	}
	
	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}
	
	public String getNewsUrl() {
		return newsUrl;
	}
	
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	
	public String getNewsContent() {
		return newsContent;
	}
	
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	
	public int getViewCount() {
		return viewCount;
	}
	
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	public int getCommentCount() {
		return commentCount;
	}
	
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public boolean isReaded() {
		return isReaded;
	}
	
	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
}
