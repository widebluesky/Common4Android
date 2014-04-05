/**
 * 
 */
package com.hiputto.common4android.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.hiputto.common4android.exception.HP_ErrorHttpStatusException;
import com.hiputto.common4android.manager.HP_DefaultThreadPool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author xuyi
 * 
 */
public class HP_NetUtils {

	/**
	 * Http请求方式
	 */
	public static enum HTTP_METHOD {
		POST("POST"), GET("GET");

		private String value;

		private HTTP_METHOD(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 网络请求超时，网络连接超时
	 */
	private int REQUEST_TIMEOUT = 30 * 1000;// 请求超时
	private int SOCKET_TIMEOUT = 30 * 1000; // 数据接收超时

	/**
	 * 设置请求超时时间
	 * 
	 * @param time
	 */
	public void setRequestTimeOut(int time) {
		this.REQUEST_TIMEOUT = time;
	}

	/**
	 * 设置连接超时时间
	 * 
	 * @param time
	 */
	public void setSoTimeOut(int time) {
		this.SOCKET_TIMEOUT = time;
	}

	public int getRequestTimeOut() {
		return this.REQUEST_TIMEOUT;
	}

	public int getSoTimeOut() {
		return this.SOCKET_TIMEOUT;
	}

	private HttpClient client = null;
	private HttpRequestBase httpRequest = null;
	private HttpResponse httpResponse = null;

	/**
	 * 获得网络请求的httpclient
	 * 
	 * @return
	 */
	private HttpClient getHttpClient() {
		try {
			if (client == null) {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new NetSSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpParams params = new BasicHttpParams();

				HttpConnectionParams.setConnectionTimeout(params,
						REQUEST_TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);

				HttpConnectionParams.setConnectionTimeout(params,
						REQUEST_TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
				client = new DefaultHttpClient(ccm, params);
			}

			return client;

		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * 增加SSL支持
	 * 
	 * @author xuyi
	 * 
	 */
	private static class NetSSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public NetSSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连接　　　　　　
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是3G网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是wifi网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 网络请求回调，返回httpResponse
	 * 
	 * @author xuyi
	 * 
	 */
	public interface OnRequestFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	}

	/**
	 * 网络请求回调，返回byte[]数据
	 * 
	 * @author xuyi
	 * 
	 */
	public interface OnRequestDataFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse, byte[] data) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	}

	/**
	 * 网络请求回调，返回bitmap图片
	 * 
	 * @author xuyi
	 * 
	 */
	public interface OnRequestBitmapFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Bitmap bitmap) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	}

	/**
	 * 网络请求回调，返回drawable图片
	 * 
	 * @author xuyi
	 * 
	 */
	public interface OnRequestDrawableFinished {

		public void onSuccess(Drawable drawable) throws Exception;

		public void onFailure(Exception e);
	}

	/**
	 * 同步Get请求
	 * 
	 * @param url
	 * @param onRequestFinished
	 * @return
	 */
	public void doGetRequest(String url, OnRequestFinished onRequestFinished) {
		try {
			httpRequest = new HttpGet(url);
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				onRequestFinished.onSuccess(httpRequest, httpResponse);
			} else {
				onRequestFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步Get请求
	 * 
	 * @param url
	 * @param onRequestFinished
	 * @return
	 */
	public Thread doAsyncGetRequestData(final String url,
			final OnRequestDataFinished onRequestDataFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					byte[] data = null;
					httpRequest = new HttpGet(url);
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						DataInputStream dataInputStream = new DataInputStream(
								httpResponse.getEntity().getContent());
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int index = 0;
						byte[] buffer = new byte[1024];
						while ((index = dataInputStream.read(buffer, 0,
								buffer.length)) != -1) {
							byteArrayOutputStream.write(buffer, 0, index);
						}
						data = byteArrayOutputStream.toByteArray();
						dataInputStream.close();
						byteArrayOutputStream.close();
						onRequestDataFinished.onSuccess(httpRequest,
								httpResponse, data);
					} else {
						onRequestDataFinished
								.onFailure(httpRequest, httpResponse,
										new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestDataFinished.onFailure(httpRequest, httpResponse,
							e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	public Thread doAsyncGetRequest(final String url,
			final OnRequestFinished onRequestFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					httpRequest = new HttpGet(url);
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} else {
						onRequestFinished.onFailure(httpRequest, httpResponse,
								new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestFinished.onFailure(httpRequest, httpResponse, e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 同步Post请求
	 * 
	 * @param url
	 * @param onRequestFinished
	 */
	public void doPostRequest(String url, OnRequestFinished onRequestFinished) {
		httpRequest = new HttpPost(url);
		try {
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				onRequestFinished.onSuccess(httpRequest, httpResponse);
			} else {
				onRequestFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步Post请求
	 * 
	 * @param url
	 * @param onRequestFinished
	 * @return
	 */
	public Thread doAsyncPostRequest(final String url,
			final OnRequestFinished onRequestFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					httpRequest = new HttpPost(url);
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} else {
						onRequestFinished.onFailure(httpRequest, httpResponse,
								new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestFinished.onFailure(httpRequest, httpResponse, e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 同步Post请求
	 * 
	 * @param url
	 * @param nameValuePairList
	 * @param onRequestFinished
	 */
	public void doPostRequest(String url,
			List<NameValuePair> nameValuePairList,
			OnRequestFinished onRequestFinished) {
		httpRequest = new HttpPost(url);
		try {
			if (nameValuePairList != null) {
				((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(
						nameValuePairList, HTTP.UTF_8));
			}
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				onRequestFinished.onSuccess(httpRequest, httpResponse);
			} else {
				onRequestFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步Post请求
	 * 
	 * @param url
	 * @param nameValuePairList
	 * @param onRequestFinished
	 */
	public Thread doAsyncPostRequest(final String url,
			final List<NameValuePair> nameValuePairList,
			final OnRequestFinished onRequestFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					httpRequest = new HttpPost(url);
					if (nameValuePairList != null) {
						((HttpPost) httpRequest)
								.setEntity(new UrlEncodedFormEntity(
										nameValuePairList, HTTP.UTF_8));
					}
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} else {
						onRequestFinished.onFailure(httpRequest, httpResponse,
								new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestFinished.onFailure(httpRequest, httpResponse, e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 同步Post请求
	 * 
	 * @param url
	 * @param multipartEntity
	 * @param onRequestFinished
	 */
	public void doPostRequest(String url, MultipartEntity multipartEntity,
			OnRequestFinished onRequestFinished) {
		try {
			httpRequest = new HttpPost(url);
			if (multipartEntity != null) {
				((HttpPost) httpRequest).setEntity(multipartEntity);
			}
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				onRequestFinished.onSuccess(httpRequest, httpResponse);
			} else {
				onRequestFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步Post请求
	 * 
	 * @param url
	 * @param multipartEntity
	 * @param onRequestFinished
	 */
	public Thread doAsyncPostRequest(final String url,
			final MultipartEntity multipartEntity,
			final OnRequestFinished onRequestFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					httpRequest = new HttpPost(url);
					if (multipartEntity != null) {
						((HttpPost) httpRequest).setEntity(multipartEntity);
					}
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} else {
						onRequestFinished.onFailure(httpRequest, httpResponse,
								new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestFinished.onFailure(httpRequest, httpResponse, e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 同步PostData请求
	 * 
	 * @param url
	 * @param onRequestDataFinished
	 */
	public void doPostRequestData(String url,
			OnRequestDataFinished onRequestDataFinished) {
		byte[] data = null;
		try {
			httpRequest = new HttpPost(url);
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				DataInputStream dataInputStream = new DataInputStream(
						httpResponse.getEntity().getContent());
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				int index = 0;
				byte[] buffer = new byte[1024];
				while ((index = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
					byteArrayOutputStream.write(buffer, 0, index);
				}
				data = byteArrayOutputStream.toByteArray();
				dataInputStream.close();
				byteArrayOutputStream.close();
				onRequestDataFinished
						.onSuccess(httpRequest, httpResponse, data);
			} else {
				onRequestDataFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestDataFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步PostData请求
	 * 
	 * @param url
	 * @param onRequestDataFinished
	 */
	public Thread doAsyncPostRequestData(final String url,
			final OnRequestDataFinished onRequestDataFinished) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				byte[] data = null;
				try {
					httpRequest = new HttpPost(url);
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						DataInputStream dataInputStream = new DataInputStream(
								httpResponse.getEntity().getContent());
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int index = 0;
						byte[] buffer = new byte[1024];
						while ((index = dataInputStream.read(buffer, 0,
								buffer.length)) != -1) {
							byteArrayOutputStream.write(buffer, 0, index);
						}
						data = byteArrayOutputStream.toByteArray();
						dataInputStream.close();
						byteArrayOutputStream.close();
						onRequestDataFinished.onSuccess(httpRequest,
								httpResponse, data);
					} else {
						onRequestDataFinished
								.onFailure(httpRequest, httpResponse,
										new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestDataFinished.onFailure(httpRequest, httpResponse,
							e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 异步PostData请求
	 * 
	 * @param url
	 * @param params
	 * @param onRequestDataFinished
	 * @return
	 */
	public Thread doAsyncPostRequestData(final String url,
			final List<NameValuePair> params,
			final OnRequestDataFinished onRequestDataFinished) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				byte[] data = null;
				try {
					httpRequest = new HttpPost(url);
					if (params != null) {
						((HttpPost) httpRequest)
								.setEntity(new UrlEncodedFormEntity(params,
										HTTP.UTF_8));
					}

					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						DataInputStream dataInputStream = new DataInputStream(
								httpResponse.getEntity().getContent());
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int index = 0;
						byte[] buffer = new byte[1024];
						while ((index = dataInputStream.read(buffer, 0,
								buffer.length)) != -1) {
							byteArrayOutputStream.write(buffer, 0, index);
						}
						data = byteArrayOutputStream.toByteArray();
						dataInputStream.close();
						byteArrayOutputStream.close();
						onRequestDataFinished.onSuccess(httpRequest,
								httpResponse, data);
					} else {
						onRequestDataFinished
								.onFailure(httpRequest, httpResponse,
										new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestDataFinished.onFailure(httpRequest, httpResponse,
							e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;

	}

	/**
	 * 同步PostBitmap请求
	 * 
	 * @param url
	 * @param onRequestBitmapFinished
	 */
	public void doPostRequestBitmap(String url,
			OnRequestBitmapFinished onRequestBitmapFinished) {
		try {
			Bitmap bitmap = null;
			httpRequest = new HttpPost(url);
			httpResponse = getHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				DataInputStream dataInputStream = new DataInputStream(
						httpResponse.getEntity().getContent());
				bitmap = BitmapFactory.decodeStream(dataInputStream);
				dataInputStream.close();
				onRequestBitmapFinished.onSuccess(httpRequest, httpResponse,
						bitmap);
			} else {
				onRequestBitmapFinished.onFailure(httpRequest, httpResponse,
						new HP_ErrorHttpStatusException());
			}
		} catch (Exception e) {
			onRequestBitmapFinished.onFailure(httpRequest, httpResponse, e);
		}
	}

	/**
	 * 异步PostBitmap请求
	 * 
	 * @param url
	 * @param onRequestBitmapFinished
	 */
	public Thread doAsyncPostRequestBitmap(final String url,
			final OnRequestBitmapFinished onRequestBitmapFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Bitmap bitmap = null;
					httpRequest = new HttpPost(url);
					httpResponse = getHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						DataInputStream dataInputStream = new DataInputStream(
								httpResponse.getEntity().getContent());
						bitmap = BitmapFactory.decodeStream(dataInputStream);
						dataInputStream.close();
						onRequestBitmapFinished.onSuccess(httpRequest,
								httpResponse, bitmap);
					} else {
						onRequestBitmapFinished
								.onFailure(httpRequest, httpResponse,
										new HP_ErrorHttpStatusException());
					}
				} catch (Exception e) {
					onRequestBitmapFinished.onFailure(httpRequest,
							httpResponse, e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 同步PostDrawable请求
	 * 
	 * @param url
	 * @param onRequestDrawableFinished
	 */
	public void doRequestDrawable(String url,
			OnRequestDrawableFinished onRequestDrawableFinished) {
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(new URL(url).openStream(),
					"drawable");
			onRequestDrawableFinished.onSuccess(drawable);
		} catch (Exception e) {
			onRequestDrawableFinished.onFailure(e);
		}
	}

	/**
	 * 异步PostDrawable请求
	 * 
	 * @param url
	 * @param onRequestDrawableFinished
	 */
	public Thread doAsyncRequestDrawable(final String url,
			final OnRequestDrawableFinished onRequestDrawableFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Drawable drawable = null;
				try {
					// 可以在这里通过文件名来判断，是否本地有此图片
					drawable = Drawable.createFromStream(
							new URL(url).openStream(), "drawable");
					onRequestDrawableFinished.onSuccess(drawable);
				} catch (Exception e) {
					onRequestDrawableFinished.onFailure(e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(new Thread(runnable));
		return thread;
	}

	/**
	 * 下载图片
	 * 
	 * @return
	 */
	public Thread doAsyncGetRequestBitmap(final String imageUrl, final OnRequestBitmapFinished onRequestBitmapFinished) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {

				doGetRequest(imageUrl, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {

						try {
							Bitmap bitmap = null;

							if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

								DataInputStream dataInputStream = new DataInputStream(
										httpResponse.getEntity().getContent());
								bitmap = BitmapFactory.decodeStream(dataInputStream);

								dataInputStream.close();

								onRequestBitmapFinished.onSuccess(httpRequest, httpResponse,
										bitmap);
							} else {
								onRequestBitmapFinished.onFailure(httpRequest, httpResponse,
										new HP_ErrorHttpStatusException());
							}

						} catch (Exception e) {
							onRequestBitmapFinished.onFailure(httpRequest, httpResponse, e);
						}
					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception e) {
						onRequestBitmapFinished.onFailure(httpRequest, httpResponse, e);
					}
				});
			}
		};
		Thread thread = new Thread(runnable);
		HP_DefaultThreadPool.getInstance().execute(thread);
		return thread;
	}

}
