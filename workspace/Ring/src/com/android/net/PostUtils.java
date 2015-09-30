package com.android.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.android.log.CLog;

/**
 * <pre>
 * 一、需要用到的场景
 
在jQuery中使用$.post()就可以方便的发起一个post请求，在android程序中有时也要从服务器获取一些数据，就也必须得使用post请求了。
 
二、需要用到的主要类
 
在android中使用post请求主要要用到的类是HttpPost、HttpResponse、EntityUtils 
 
三、主要思路
 
1、创建HttpPost实例，设置需要请求服务器的url。
 
2、为创建的HttpPost实例设置参数，参数设置时使用键值对的方式用到NameValuePair类。
 
3、发起post请求获取返回实例HttpResponse
 
4、使用EntityUtils对返回值的实体进行处理（可以取得返回的字符串，也可以取得返回的byte数组）

首先说一下get和post的区别

get请求方式是将提交的参数拼接在url地址后面，例如http://www.baidu.com/index.jsp?num=23&jjj=888;
 但是这种形式对于那种比较隐私的参数是不适合的，而且参数的大小也是有限制的，一般是1K左右吧，对于上传文件
 就不是很适合。

post请求方式是将参数放在消息体内将其发送到服务器，所以对大小没有限制，对于隐私的内容也比较合适。
 如下Post请求
 POST /LoginCheck HTTP/1.1
 Accept: text/html, application/xhtml+xml, * /*
 Referer: http://192.168.2.1/login.asp
 Accept-Language: zh-CN
 User-Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)
 Content-Type: application/x-www-form-urlencoded
 Accept-Encoding: gzip, deflate
 Host: 192.168.2.1
 Content-Length: 39
 Connection: Keep-Alive
 Cache-Control: no-cache
 Cookie: language=en

Username=admin&checkEn=0&Password=admin   //参数位置
 

 * @author junxu.wang
 *
 */
public class PostUtils {
	private static final CLog cLog=new CLog(PostUtils.class.getSimpleName());
	
	public void httpGet(String baseUrl){
		String str = "密码";
		String url = baseUrl + "?wd=" + str;
		
		//生成一个请求对象
		HttpGet httpGet = new HttpGet(url);
		//生成一个http客户端对象
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpResponse httpResponse;
		HttpEntity httpEntity;
		InputStream inputStream=null;
		//发送请求
		try {
			
			httpResponse = httpClient.execute(httpGet);//接收响应
			httpEntity = httpResponse.getEntity();//取出响应
			//客户端收到响应的信息流
			inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = "";
			String line = "";
			while((line = reader.readLine()) != null){
				result = result + line;
			}
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{//最后一定要关闭输入流
			try{
				inputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public  void httpPost(){
		String url = "http://192.168.2.112:8080/JustsyApp/Applet"; 
		// 第一步，创建HttpPost对象 
		HttpPost httpPost = new HttpPost(url); 
		
		// 设置HTTP POST请求参数必须用NameValuePair对象 
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("action", "downloadAndroidApp")); 
		params.add(new BasicNameValuePair("packageId", "89dcb664-50a7-4bf2-aeed-49c08af6a58a")); 
		params.add(new BasicNameValuePair("uuid", "test_ok1")); 
		
		HttpResponse httpResponse = null; 
		try { 
			// 设置httpPost请求参数 
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
			httpResponse = new DefaultHttpClient().execute(httpPost); 
			//System.out.println(httpResponse.getStatusLine().getStatusCode()); 
			if (httpResponse.getStatusLine().getStatusCode() == 200) { 
				// 第三步，使用getEntity方法活得返回结果 
				String result = EntityUtils.toString(httpResponse.getEntity()); 
				cLog.println("result:" + result); 
			} 
		} catch (ClientProtocolException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		cLog.println("end url..."); 
	} 
	
	
	
	
	private String address = "http://192.168.2.101:80/server/loginServlet";
	public boolean httpURLConnectionGet(String address,String username, String password) throws Exception {
		username = URLEncoder.encode(username);// 中文数据需要经过URL编码
		password = URLEncoder.encode(password);
		String params = "username=" + username + "&password=" + password; 
		//将参数拼接在URl地址后面
		URL url = new URL(address + "?" + params);
		//通过url地址打开连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//设置超时时间
		conn.setConnectTimeout(3000);
		//设置请求方式
		conn.setRequestMethod("GET");
		//如果返回的状态码是200，则一切Ok，连接成功。
		return conn.getResponseCode() == 200;
	}
	
	/**在android中通过post方式提交数据。*/
	public boolean httpURLConnectionPost(String username, String password) throws Exception {
		username = URLEncoder.encode(username);// 中文数据需要经过URL编码
		password = URLEncoder.encode(password);
		String params = "username=" + username + "&password=" + password; 
		byte[] data = params.getBytes();
		
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);
		//这是请求方式为POST
		conn.setRequestMethod("POST");
		//设置post请求必要的请求头
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 请求头, 必须设置
		conn.setRequestProperty("Content-Length", data.length + "");// 注意是字节长度, 不是字符长度
		
		conn.setDoOutput(true);// 准备写出
		conn.getOutputStream().write(data);// 写出数据
		
		return conn.getResponseCode() == 200;
	}
	public void toHttpURLConnection() throws IOException{
		String path = "http://192.168.2.115:8080/android-web-server/httpConnectServlet.do?PackageID=89dcb664-50a7-4bf2-aeed-49c08af6a58a"; 
		URL url = new URL(path); 
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
		conn.setRequestMethod("POST"); 
		conn.setConnectTimeout(5000); 
		System.out.println(conn.getResponseCode()); 
		
	}
	
	private void httpUrlConnectionPost(){
		String pathUrl = "http://172.20.0.206:8082/TestServelt/login.do";
	}
	private void httpUrlConnectionPost(String pathUrl){
		try{
			
			//建立连接
			URL url=new URL(pathUrl);
			HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
			
			////设置连接属性
			httpConn.setDoOutput(true);//使用 URL 连接进行输出
			httpConn.setDoInput(true);//使用 URL 连接进行输入
			httpConn.setUseCaches(false);//忽略缓存
			httpConn.setRequestMethod("POST");//设置URL请求方法
			String requestString = "客服端要以以流方式发送到服务端的数据...";
			
			//设置请求属性
			//获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("UTF-8");
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			String name=URLEncoder.encode("黄武艺","utf-8");
			httpConn.setRequestProperty("NAME", name);
			
			//建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			//获得响应状态
			int responseCode = httpConn.getResponseCode();
			if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
				
				//当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				//处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				cLog.println(sb.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**方式二：HttpURLConnection、URL(import java.net.HttpURLConnection;import java.net.URL;import java.net.URLEncoder;)*/
	public static void httpUrlConnectionPost(String pathUrl,String fileName){
		File file=new File(fileName);
		cLog.println(fileName+"("+file.getName()+") send->"+pathUrl);
		FileInputStream fis=null;
		//建立输出流，并写入数据
		OutputStream outputStream=null;
		try{
			
			//建立连接
			URL url=new URL(pathUrl);
			HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
			
			////设置连接属性
			httpConn.setDoOutput(true);//使用 URL 连接进行输出
			httpConn.setDoInput(true);//使用 URL 连接进行输入
			httpConn.setUseCaches(false);//忽略缓存
			httpConn.setRequestMethod("POST");//设置URL请求方法
			
			fis=new FileInputStream(file);
			//设置请求属性
			//获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			httpConn.setRequestProperty("Content-length", "" + file.length());
			httpConn.setRequestProperty("Content-Type", "audio/mp3");//http://tool.oschina.net/commons
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			String name=URLEncoder.encode(file.getName(),"UTF-8");
			httpConn.setRequestProperty("NAME", name);
			
			outputStream = httpConn.getOutputStream();
			byte []buffer=new byte[fis.available()];
			while (fis.read(buffer)>0) {
				outputStream.write(buffer);
			}
			fis.close();
			fis=null;
			outputStream.close();
			outputStream=null;
			//获得响应状态
			int responseCode = httpConn.getResponseCode();
			if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
				
				//当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				//处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				cLog.println(sb.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				if(fis!=null){
					fis.close();
				}
				if(outputStream!=null){
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 在android中用get方式向服务器提交请求：
 

在android模拟器中访问本机中的tomcat服务器时，注意：不能写localhost，因为模拟器是一个单独的手机系统，所以要写真是的IP地址。
 否则无法访问到服务器。
 

//要访问的服务器地址，下面的代码是要向服务器提交用户名和密码，提交时中文先要经过URLEncoder编码，因为模拟器默认的编码格式是utf-8
 //而tomcat内部默认的编码格式是ISO8859-1，所以先将参数进行编码，再向服务器提交。
 
	 * 
	 */
	public static boolean httpURLConnectionGet(String pathUrl, String params) throws Exception {
		//将参数拼接在URl地址后面
		URL url = new URL(pathUrl + "?" + params);
		cLog.println("send->"+url.toString());
		//通过url地址打开连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//设置超时时间
		conn.setConnectTimeout(3000);
		//设置请求方式
		conn.setRequestMethod("GET");
		//如果返回的状态码是200，则一切Ok，连接成功。
		return conn.getResponseCode() == 200;
	}
	
	public static String sendHttpPostResquest(String path, Map<String, String> params, String encoding) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();      //封装请求体参数
		if((params != null) && !params.isEmpty()) {
			for(Map.Entry<String, String> param : params.entrySet()) {
				list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encoding);    //对请求体参数进行URL编码
			HttpPost httpPost = new HttpPost(path);           //创建一个POST方式的HttpRequest对象
			httpPost.setEntity(entity);                       //设置POST方式的请求体
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpPost);                      //执行POST请求
			int reponseCode = httpResponse.getStatusLine().getStatusCode();            //获得服务器的响应码
			if(reponseCode == HttpStatus.SC_OK) {
				String resultData = EntityUtils.toString(httpResponse.getEntity());    //获得服务器的响应内容
				return resultData;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * Function  :   发送Post请求到服务器
	 * Param     :   params请求体内容，encode编码格式
	 * Author    :   博客园-依旧淡然
	 */
	public static String submitPostData(URL url,Map<String, String> params, String encode) {
		
		byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
		try {        
			
			HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setConnectTimeout(3000);        //设置连接超时时间
			httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
			httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
			httpURLConnection.setRequestMethod("POST");    //设置以Post方式提交数据
			httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
			//设置请求体的类型是文本类型
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//设置请求体的长度
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
			//获得输出流，向服务器写入数据
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(data);
			
			int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
			if(response == HttpURLConnection.HTTP_OK) {
				InputStream inptStream = httpURLConnection.getInputStream();
				return dealResponseResult(inptStream);                     //处理服务器的响应结果
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * Function  :   封装请求体信息
	 * Param     :   params请求体内容，encode编码格式
	 */
	public static StringBuffer getRequestData(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
		try {
			for(Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey())
				.append("=")
				.append(URLEncoder.encode(entry.getValue(), encode))
				.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}
	
	/*
	 * Function  :   处理服务器的响应结果（将输入流转化成字符串）
	 * Param     :   inputStream服务器的响应输入流
	 */
	public static String dealResponseResult(InputStream inputStream) {
		String resultData = null;      //存储处理结果
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultData = new String(byteArrayOutputStream.toByteArray());    
		return resultData;
	}
}
