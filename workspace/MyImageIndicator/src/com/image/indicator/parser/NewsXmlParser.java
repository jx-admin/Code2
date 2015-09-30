package com.image.indicator.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.image.indicator.R;
import com.image.indicator.entity.News;
import com.image.indicator.utility.FileAccess;

/**
 * 解析新闻数据列表
 * @Description: 解析新闻数据列表，这里只是个示例，具体地不再实现。

 * @File: NewsXmlParser.java

 * @Package com.image.indicator.parser

 * @Author Hanyonglu

 * @Date 2012-6-18 下午02:31:26

 * @Version V1.0
 */
public class NewsXmlParser {
	// 新闻列表
	private List<HashMap<String, News>> newsList = null;
	
	// 滑动图片的集合，这里设置成了固定加载，当然也可动态加载。
	private int[] slideImages = {
			R.drawable.image01,
			R.drawable.image02,
			R.drawable.image03,
			R.drawable.image04,
			R.drawable.image05};
	
	// 滑动标题的集合
	private int[] slideTitles = {
			R.string.title1,
			R.string.title2,
			R.string.title3,
			R.string.title4,
			R.string.title5,
	};
	
	// 滑动链接的集合
	private String[] slideUrls = {
			"http://mobile.csdn.net/a/20120616/2806676.html",
			"http://cloud.csdn.net/a/20120614/2806646.html",
			"http://mobile.csdn.net/a/20120613/2806603.html",
			"http://news.csdn.net/a/20120612/2806565.html",
			"http://mobile.csdn.net/a/20120615/2806659.html",
	};
	
	public int[] getSlideImages(){
		return slideImages;
	}
	
	public int[] getSlideTitles(){
		return slideTitles;
	}
	
	public String[] getSlideUrls(){
		return slideUrls;
	}
	
	/**
	 * 获取XmlPullParser对象
	 * @param result
	 * @return
	 */
	private XmlPullParser getXmlPullParser(String result){
		XmlPullParser parser = Xml.newPullParser();
		InputStream inputStream = FileAccess.String2InputStream(result);
		
		try {
			parser.setInput(inputStream, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return parser;
	}
	
	public int getNewsListCount(String result){
		int count = -1;
		
		try {
			XmlPullParser parser = getXmlPullParser(result);
	        int event = parser.getEventType();//产生第一个事件
	        
	        while(event != XmlPullParser.END_DOCUMENT){
	        	switch(event){
	        	case XmlPullParser.START_DOCUMENT:
	        		break;
	        	case XmlPullParser.START_TAG://判断当前事件是否是标签元素开始事件
	        		if("count".equals(parser.getName())){//判断开始标签元素是否是count
	        			count = Integer.parseInt(parser.nextText());
	                }
	        		
	        		break;
	        	case XmlPullParser.END_TAG://判断当前事件是否是标签元素结束事件
//	        		if("count".equals(parser.getName())){//判断开始标签元素是否是count
//	        			count = Integer.parseInt(parser.nextText());
//	                }
	        		
	        		break;
	        	}
            
	        	event = parser.next();//进入下一个元素并触发相应事件
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// 无返回值，则返回-1
		return count;
	}
}
