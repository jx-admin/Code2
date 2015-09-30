package com.act.sctc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class HttpRequest {
	private HttpRequestListener listener;

	public interface HttpRequestListener {
		public void onHttpRequestFailed(int tag);

		public void onHttpRequestOK(int tag, byte[] data);
	}

	public HttpRequest(HttpRequestListener listener) {
		this.listener = listener;
	}

	private class MessageObject {
		public HttpRequest request;
		public int tag;
		public byte[] data;
	}

	private final static int MSG_REQUEST_FAILED = 1000;
	private final static int MSG_REQUEST_OK = 1001;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			MessageObject obj = (MessageObject) msg.obj;
			if (msg.what == MSG_REQUEST_FAILED) {
				obj.request.listener.onHttpRequestFailed(obj.tag);
			} else if (msg.what == MSG_REQUEST_OK) {
				obj.request.listener.onHttpRequestOK(obj.tag, obj.data);
			}
		}
	};

	private static SchemeRegistry schemeRegistry = null;
	private static HttpParams httpParams = null;

	private HttpClient getClient() {
		if (schemeRegistry == null) {
			schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
			schemeRegistry.register(new Scheme("https", new SSLSocketFactory(), 8443));
			httpParams = new BasicHttpParams();
			// ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
			// HttpHost localhost = new HttpHost("127.0.0.1", 80);
			// connPerRoute.setMaxForRoute(new HttpRoute(localhost), 50);
			// ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
			// connPerRoute);
			// ConnManagerParams.setMaxTotalConnections(httpParams, 10);
			// HttpConnectionParams.setTcpNoDelay(httpParams, true);
			// HttpConnectionParams.setConnectionTimeout(httpParams, 0);

			// ConnManagerParams.setTimeout(httpParams, 8000);
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
			// HttpProtocolParams.setUseExpectContinue(httpParams, false);
		}

		ClientConnectionManager connectionManager = new SingleClientConnManager(httpParams, schemeRegistry);
		HttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
		return httpClient;
	}

	public void get(final int tag, final String url) {
		get(tag, url, null);
	}

	public void get(final int tag, final String url, final File file) {
		if (Logger.DEBUG) {
			Logger.debug("HttpRequest.get: " + url);
		}
		new Thread() {
			public void run() {
				try {
					HttpClient client = getClient();
					HttpGet request = new HttpGet(url);

					doRequest(tag, client, request, file);
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(MSG_REQUEST_FAILED, tag, null);
				}
			}
		}.start();
	}

	public void post(final int tag, final String url, final List<NameValuePair> params) {
		if (Logger.DEBUG) {
			Logger.debug("HttpRequest.post: " + url);
		}

		new Thread() {
			public void run() {
				try {
					HttpPost request = new HttpPost(url);

					if (params != null && params.size() > 0) {
						try {
							request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						} catch (Throwable t) {
							sendMessage(MSG_REQUEST_FAILED, tag, null);
							t.printStackTrace();
							return;
						}
					}
					HttpClient client = getClient();
					doRequest(tag, client, request, null);
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(MSG_REQUEST_FAILED, tag, null);
				}
			}
		}.start();
	}

	private void doRequest(int tag, HttpClient client, HttpUriRequest request, File file) {
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (file != null) {
					InputStream input = entity.getContent();
					FileOutputStream output = new FileOutputStream(file);
					byte data[] = new byte[64 * 1024];
					while (true) {
						int bytes = input.read(data);
						if (bytes <= 0) {
							break;
						}
						output.write(data, 0, bytes);
					}
					input.close();
					output.close();
					sendMessage(MSG_REQUEST_OK, tag, null);
				} else {
					byte[] data = EntityUtils.toByteArray(entity);
					sendMessage(MSG_REQUEST_OK, tag, data);
				}
			} else {
				sendMessage(MSG_REQUEST_FAILED, tag, null);
			}
		} catch (Throwable t) {
			sendMessage(MSG_REQUEST_FAILED, tag, null);
			t.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private void sendMessage(int what, int tag, byte[] data) {
		MessageObject obj = new MessageObject();
		obj.request = this;
		obj.tag = tag;
		obj.data = data;
		Message msg = Message.obtain(handler, what, obj);
		handler.sendMessage(msg);
	}

	private class SSLSocketFactory implements SocketFactory, LayeredSocketFactory {
		private SSLContext sslContext = null;

		private SSLContext createSSLContext() throws IOException {
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] { new HttpsTrustManager(null) }, null);
				return context;
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		}

		private SSLContext getSSLContext() throws IOException {
			if (this.sslContext == null) {
				this.sslContext = createSSLContext();
			}
			return this.sslContext;
		}

		public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort,
				HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
			int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
			int soTimeout = HttpConnectionParams.getSoTimeout(params);

			InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
			SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

			if ((localAddress != null) || (localPort > 0)) {
				if (localPort < 0) {
					localPort = 0;
				}
				InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
				sslsock.bind(isa);
			}

			sslsock.connect(remoteAddress, connTimeout);
			sslsock.setSoTimeout(soTimeout);
			return sslsock;

		}

		public Socket createSocket() throws IOException {
			return getSSLContext().getSocketFactory().createSocket();
		}

		public boolean isSecure(Socket socket) throws IllegalArgumentException {
			return true;
		}

		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
				UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		public boolean equals(Object obj) {
			return ((obj != null) && obj.getClass().equals(SSLSocketFactory.class));
		}

		public int hashCode() {
			return SSLSocketFactory.class.hashCode();
		}
	}

	private class HttpsTrustManager implements X509TrustManager {

		private X509TrustManager trustManager = null;

		public HttpsTrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
			super();
			TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			factory.init(keystore);
			TrustManager[] trustmanagers = factory.getTrustManagers();
			if (trustmanagers.length == 0) {
				throw new NoSuchAlgorithmException("Found no trust manager");
			}
			this.trustManager = (X509TrustManager) trustmanagers[0];
		}

		public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
			trustManager.checkClientTrusted(certificates, authType);
		}

		public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
			if ((certificates != null) && (certificates.length == 1)) {
				certificates[0].checkValidity();
			} else {
				trustManager.checkServerTrusted(certificates, authType);
			}
		}

		public X509Certificate[] getAcceptedIssuers() {
			return this.trustManager.getAcceptedIssuers();
		}
	}
}
