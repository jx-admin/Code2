package com.aess.aemm.networkutils;

import java.io.BufferedReader;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
//import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
//import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.aess.aemm.apkmag.ApkInfo;


import android.content.Context;
import android.util.Log;

public class SocketClient {
	public static final String TAG = "SocketClient";
	public static final int TIME_OUT = -3;
	public static final int CONNECT_ERROR = -4;
	public static final String HTTP = "http://";
	
	private Socket mSocket = null;
	private OutputStream mOut = null;
	private InputStream mIn = null;
	SocketAddress mAddress;
	Context mContext;

	public SocketClient(Context c, String host, int port) {
		if (mSocket != null) {
			Log.i(TAG, "connhost close");
			close();
			mSocket = null;
		}
		
		mAddress = new InetSocketAddress(host, port);
		
		try {
			mSocket = new TrustAllSSLSocketFactory().createSocket();
		} catch (IOException e) {
			mSocket = null;
		}
		mContext = c;
	}

	public boolean isInitOk() {
		boolean rlt = false;
		if (null != mSocket) {
			rlt = true;
		}
		return rlt;
	}
    
	public int connHost() {
		if (mSocket == null) {
			Log.e(TAG, "socket connect: No network");
			return -1;
		}
		try {
			mSocket.connect(mAddress, 60000);
			Log.i(TAG, "connhost success");
		} catch (SocketTimeoutException e) {
			Log.e(TAG, "socket connect " + mSocket.getInetAddress() + " " + TIME_OUT, e);
			return TIME_OUT;
		} catch (ConnectException e) {
			Log.e(TAG, "socket ConnectException ", e);
			return CONNECT_ERROR;
		} catch (NoRouteToHostException e) {
			Log.e(TAG, "socket NoRouteToHostException ", e);
			return CONNECT_ERROR;
		} catch (SocketException e) {
			Log.e(TAG, "socket SocketException ", e);
			return CONNECT_ERROR;
		} catch (Exception e) {
			Log.e(TAG, "socket connect: ", e);
			return -1;
		}
		return 1;
	}

	public int sendReq(byte[] data) {
		try {
			mOut = mSocket.getOutputStream();
			if (mOut != null) {
				mOut.write(data);
				mOut.flush();
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, "TIME_OUT", e);
			return TIME_OUT;
		} catch (Exception e) {
			Log.e(TAG, "sendreq", e);
			return 0;
		}
		return 1;
	}

	public int sendReq(byte data) {
		try {
			mOut = mSocket.getOutputStream();
			if (mOut != null) {
				mOut.write(data);
				mOut.flush();
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, "TIME_OUT", e);
			return TIME_OUT;
		} catch (Exception e) {
			Log.e(TAG, "sendreq", e);
			return 0;
		}
		return 1;
	}

	public String recvData() {
		String rltStr = null;
		if (null == mSocket) {
			return rltStr;
		}
		try {
			mIn = mSocket.getInputStream();
			if (mIn != null) {
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(mIn));
				StringBuffer buffer = new StringBuffer();
				String line = "";
				int i = 0;
				while (i < 10) {
					i++;
					line = l_reader.readLine();
					if (line == null) {
						break;
					} else {
						if (line.equals(""))
							break;
					}
					buffer.append(line);
				}
				rltStr = buffer.toString();
			}

		} catch (SocketTimeoutException e) {
			Log.e(TAG, "recvData TIME_OUT", e);
			rltStr = null;
		} catch (Exception e) {
			Log.e(TAG, "recvData Exception", e);
			rltStr = null;
		}
		return rltStr;
	}

	public int close() {
		try {
			if (mSocket == null)
				return 0;
			if (mOut != null)
				mOut.close();
			if (mIn != null)
				mIn.close();
			mSocket.close();
			mSocket = null;
		} catch (Exception e) {
			Log.e(TAG, "close", e);
			return 0;
		}
		return 1;
	}

	public void shutdown() {
		if (mSocket == null || mSocket.isClosed())
			return;
		try {
			if (mSocket.isInputShutdown()) {
				mSocket.shutdownInput();
			}
			if (mSocket.isOutputShutdown()) {
				mSocket.shutdownOutput();
			}
		} catch (Exception e) {
			Log.e(TAG, "shutdown", e);
		}
	}
	
	public static int isTrafficLimtOver(Context cxt) {
		ApkInfo apkInfo = new ApkInfo(cxt);
		
		if (apkInfo.isLimitOver(cxt)) {
			Log.w(TAG, "Traffic Is Limit");
			return -1;
		}
		return 1;
	}

	private static final class TrustAllSSLSocketFactory implements
			LayeredSocketFactory {

		private static final TrustAllSSLSocketFactory DEFAULT_FACTORY = new TrustAllSSLSocketFactory();

		@SuppressWarnings("unused")
		public static TrustAllSSLSocketFactory getSocketFactory() {
			return DEFAULT_FACTORY;
		}

		private SSLContext sslcontext;
		private javax.net.ssl.SSLSocketFactory socketfactory;

		private TrustAllSSLSocketFactory() {
			super();
			TrustManager[] tm = new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// do nothing
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// do nothing
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

			} };
			try {
				sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(null, tm, new java.security.SecureRandom());
				socketfactory = this.sslcontext.getSocketFactory();
			} catch (NoSuchAlgorithmException e) {
				Log.e(TAG, "NoSuchAlgorithmException!", e);
			} catch (KeyManagementException e) {
				Log.e(TAG, "KeyManagementException!", e);
			}
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(
					socket, host, port, autoClose);
			return sslSocket;
		}

		@Override
		public Socket connectSocket(Socket sock, String host, int port,
				InetAddress localAddress, int localPort, HttpParams params)
				throws IOException, UnknownHostException,
				ConnectTimeoutException {
			if (host == null) {
				throw new IllegalArgumentException(
						"Target host may not be null.");
			}
			if (params == null) {
				throw new IllegalArgumentException(
						"Parameters may not be null.");
			}

			SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock
					: createSocket());

			if ((localAddress != null) || (localPort > 0)) {

				// we need to bind explicitly
				if (localPort < 0) {
					localPort = 0; // indicates "any"
				}

				InetSocketAddress isa = new InetSocketAddress(localAddress,
						localPort);
				sslsock.bind(isa);
			}

			int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
			int soTimeout = HttpConnectionParams.getSoTimeout(params);

			InetSocketAddress remoteAddress;
			remoteAddress = new InetSocketAddress(host, port);

			sslsock.connect(remoteAddress, connTimeout);

			sslsock.setSoTimeout(soTimeout);

			return sslsock;
		}

		@Override
		public Socket createSocket() throws IOException {
			// the cast makes sure that the factory is working as expected
			return (SSLSocket) this.socketfactory.createSocket();
		}

		@Override
		public boolean isSecure(Socket sock) throws IllegalArgumentException {
			return true;
		}
	}
}
