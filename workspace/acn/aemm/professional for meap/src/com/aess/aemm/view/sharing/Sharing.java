package com.aess.aemm.view.sharing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.aess.aemm.view.sharing.SharingActivity.SharingViewAdapter;


/**
 * @author junxu.wang
 * <sharings commandid="命令标识" >
	<contenttype>
		<!—内容分类，每次请求都会包含此节点-->
  <![CDATA[
          	消息体(Json格式的消息体)
  			]]>
	</contenttype>
    <!--需要客户端显示的分享内容-->
<sharing contenttype="内容分类" mime="mime类型" name="文件名" download-url="下载地址">
<!—
下载地址触发将命令标识和真实内容下载地址返回客户端，
客户端再下载相关内容，当commandid为0时，表示该分享内容已过期，
需从客户端分享内容列表中删除，下载返回应答协议如下：
<content commandid="命令标识" download-url="内容下载地址" />
 -->
  	</sharing>
<!—分享内容可能会有多条记录-->
  </sharings>

 */
public class Sharing {
	public static final String TAG="Sharing";
	public final class XML {
		public static final String SHARINGS= "sharings",
		COMMANDID = "commandid", CONTENTTYPE = "contenttype",STATUS="status",
		SHARING = "sharing", MIME = "mime",FILETYPE="filetype",
				NAME = "name", DOWNLOAD_URL = "download-url", IM = "content";
		public static final String ACCOUNT = "thirdinfo";
	}
	
	public static final int DEFAULT=-1,READ=1,OPENFAIL=2,UNPAR=3;
	
	public static String commandId;
	public static List<String> types=new ArrayList<String>();
	public static List<Sharing> Sharings=new ArrayList<Sharing>();
	public String mContentType,mMime,mName,mDownload_url;
	public String mCommandId,mFileDownload_url;
	public Bitmap mIcon;
	public int mFileType,mStatus=DEFAULT;
	public SharingViewAdapter.GridHolder tag;
	public String mFile;
	public boolean isDownloading;
	
	
	public static Sharing parseInfo(Element root) {
		Log.d(TAG, "parse Sharing xml ...");
		Sharing result=null;
		NodeList nl = root.getElementsByTagName(Sharing.XML.SHARINGS);
		if (nl == null || nl.getLength() == 0) {
			return result;
		}
		Sharing mSharing;
		Element sharings=(Element)nl.item(0);
		Sharing.commandId=sharings.getAttribute(Sharing.XML.COMMANDID);
		NodeList sharingList=sharings.getChildNodes();
		
		if (sharingList != null && sharingList.getLength() > 0) {
			Sharing.Sharings.clear();
			for(int i=sharingList.getLength()-1;i>=0;i--){
				Element sharing = (Element) sharingList.item(i);
				if(sharing.getTagName().equals(Sharing.XML.CONTENTTYPE)){
					if(sharing.getNodeType()==Node.CDATA_SECTION_NODE)
					try {
						
//						Log.d(TAG,sharing.getNodeValue());
						JSONObject jsonObject=new JSONObject(sharing.getNodeValue());
						Log.d(TAG,jsonObject.toString());
					} catch (DOMException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(sharing.getTagName().equals(Sharing.XML.SHARING)){
					mSharing=new Sharing();
					mSharing.mName=sharing.getAttribute(Sharing.XML.NAME);
//					mSharing.mMime=sharing.getAttribute(Sharing.XML.MIME);
					mSharing.mContentType=sharing.getAttribute(Sharing.XML.CONTENTTYPE);
					mSharing.mDownload_url=sharing.getAttribute(Sharing.XML.DOWNLOAD_URL);
					try{
						mSharing.mStatus=Integer.parseInt(sharing.getAttribute(Sharing.XML.STATUS));
						mSharing.mFileType=Integer.parseInt(sharing.getAttribute(Sharing.XML.FILETYPE))-1;
						}catch(Exception e){e.printStackTrace();}
					Element color=(Element) sharing.getFirstChild();
					if(color!=null){
						NodeList grayChildsNodes = color.getChildNodes();

					for (int k = 0; k < grayChildsNodes.getLength(); k++) {
						Node grayChildNode = (Node) grayChildsNodes.item(k);

						if (grayChildNode.getNodeType() == Node.CDATA_SECTION_NODE) {
							String icon64= grayChildNode.getNodeValue();
							if (!TextUtils.isEmpty(icon64)) {
								byte[] temp = Base64.decode(icon64,0);
								InputStream is  = new ByteArrayInputStream(temp);
								mSharing.mIcon = BitmapFactory.decodeStream(is);
							}
						}
					}
					/*if (color.getNodeType() == Node.CDATA_SECTION_NODE) {
						String icon64 = color.getNodeValue();
						if(icon64!=null){
							byte[] temp = Base64.decode(icon64,0);
							InputStream is  = new ByteArrayInputStream(temp);
							mSharing.mIcon = BitmapFactory.decodeStream(is);
						}
					}*/
					}
					Sharings.add(mSharing);
					Log.d(TAG,"name:"+mSharing.mName+" fileType:"+mSharing.mFileType);
				}
			}
		}
		return result;
	}
	

	
	public static int parseDownLoadInfo(Element root,Sharing mSharing) {
		Log.d(TAG, "parse parseDownLoadInfo xml ...");
//		NodeList nl = root.getElementsByTagName(Sharing.XML.SHARINGS);
//		if (nl == null || nl.getLength() == 0) {
//			return -1;
//		}
//		Element sharings=(Element)nl.item(0);
//		NodeList sharingList=sharings.getChildNodes();
//		
//		if (sharingList != null && sharingList.getLength() > 0) {
//			for(int i=sharingList.getLength()-1;i>=0;i--){
//				Element sharing = (Element) sharingList.item(i);
//				if(sharing.getTagName().equals(Sharing.XML.SHARING)){
					Element content=root;//(Element) sharing.getFirstChild();
					if(content!=null){
						mSharing.mCommandId=content.getAttribute(Sharing.XML.COMMANDID);
						mSharing.mFileDownload_url=content.getAttribute(Sharing.XML.DOWNLOAD_URL);
						mSharing.mMime=content.getAttribute(Sharing.XML.MIME);
					}
//				}
//			}
//		}
		return 0;
	}
}
