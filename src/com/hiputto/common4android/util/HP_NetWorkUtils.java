package com.hiputto.common4android.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
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
import com.hiputto.common4android.util.HP_AsyncTaskUtils.AsyncTaskSteps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class HP_NetWorkUtils {

	public static enum HTTP_METHOD {
		// newest不是latest...
		POST("POST"), GET("GET");

		private String value;

		private HTTP_METHOD(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private HttpClient getHttpClient() {
		try {
			// BasicHttpParams httpParams = new BasicHttpParams();
			// HttpConnectionParams.setConnectionTimeout(httpParams,
			// REQUEST_TIMEOUT);
			// HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
			// HttpClient httpClient = new DefaultHttpClient(httpParams);

			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
			HttpClient client = new DefaultHttpClient(ccm, params);
			return client;

		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public interface OnRequestFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);

	}

	public interface OnRequestDataFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse, byte[] data) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	}

	public interface OnRequestBitmapFinished {

		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Bitmap bitmap) throws Exception;

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	}

	private int REQUEST_TIMEOUT = 5 * 1000;// 请求超时
	private int SOCKET_TIMEOUT = 0 * 1000; // 数据接收超时

	public void setRequestTimeOut(int time) {
		REQUEST_TIMEOUT = time;
	}

	public void setSoTimeOut(int time) {
		SOCKET_TIMEOUT = time;
	}

	public int getRequestTimeOut() {
		return this.REQUEST_TIMEOUT;
	}

	public int getSoTimeOut() {
		return this.SOCKET_TIMEOUT;

	}

	// 判断网络是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// return cm.getActiveNetworkInfo().isAvailable();
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

	// 判断是否是3G网络
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	// 判断是否是wifi网络
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

	public void doGetRequest(String url, OnRequestFinished onRequestFinished) {

		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		try {

			httpRequest = new HttpGet(url);

			httpResponse = httpClient.execute(httpRequest);

			onRequestFinished.onSuccess(httpRequest, httpResponse);

		} catch (Exception e) {

			onRequestFinished.onFailure(httpRequest, httpResponse, e);

		}
	}

	public void doGetRequest(String url, HashMap<String, String> params,
			OnRequestFinished onRequestFinished) {

		Iterator<String> keySetIterator = params.keySet().iterator();
		Iterator<String> valueIterator = params.values().iterator();

		if (!params.isEmpty()) {
			url += "?";

			while (keySetIterator.hasNext()) {
				if (String.valueOf(url.charAt(url.length() - 1)).equals("?")) {
					url += keySetIterator.next() + "=" + valueIterator.next();
				} else {
					url += "&" + keySetIterator.next() + "="
							+ valueIterator.next();
				}
			}
		}
		doGetRequest(url, onRequestFinished);
	}

	public void doPostRequest(String url, OnRequestFinished onRequestFinished) {

		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		httpRequest = new HttpPost(url);

		try {

			httpResponse = httpClient.execute(httpRequest);

			onRequestFinished.onSuccess(httpRequest, httpResponse);

		} catch (Exception e) {

			onRequestFinished.onFailure(httpRequest, httpResponse, e);

		}

	}

	public void doPostRequest(String url,
			List<NameValuePair> nameValuePairList,
			OnRequestFinished onRequestFinished) {

		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		httpRequest = new HttpPost(url);

		try {

			if (nameValuePairList != null) {
				((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(
						nameValuePairList, HTTP.UTF_8));
			}

			httpResponse = httpClient.execute(httpRequest);

			onRequestFinished.onSuccess(httpRequest, httpResponse);

		} catch (Exception e) {

			onRequestFinished.onFailure(httpRequest, httpResponse, e);

		}
	}

	public void doPostRequest(String url, MultipartEntity multipartEntity,
			OnRequestFinished onRequestFinished) {
		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		try {

			httpRequest = new HttpPost(url);

			if (multipartEntity != null) {
				((HttpPost) httpRequest).setEntity(multipartEntity);
			}

			httpResponse = httpClient.execute(httpRequest);

			onRequestFinished.onSuccess(httpRequest, httpResponse);

		} catch (Exception e) {

			onRequestFinished.onFailure(httpRequest, httpResponse, e);

		}
	}

	public void doRequestData(String url,
			OnRequestDataFinished onRequestDataFinished) {

		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		byte[] data = null;

		try {
			httpRequest = new HttpPost(url);

			httpResponse = httpClient.execute(httpRequest);

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

	public void doRequestBitmap(String url,
			OnRequestBitmapFinished onRequestBitmapFinished) {

		HttpClient httpClient = getHttpClient();

		HttpRequestBase httpRequest = null;

		HttpResponse httpResponse = null;

		try {
			Bitmap bitmap = null;

			httpRequest = new HttpPost(url);

			httpResponse = httpClient.execute(httpRequest);

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

	public AsyncTask<String, Integer, HashMap<String, Object>> doAsyncPostRequest(
			final String url, final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				doPostRequest(url, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {

						hashMap.put("isSuccess", true);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception exception) {

						hashMap.put("isSuccess", false);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
						hashMap.put("exception", exception);
					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				if (isSuccess) {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");
					try {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");

					Exception exception = (Exception) hashMap.get("exception");

					onRequestFinished.onFailure(httpRequest, httpResponse,
							exception);
				}

			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onCancelled() {

			}
		}).execute("");
	}

	public AsyncTask<String, Integer, HashMap<String, Object>> doAsyncPostRequest(
			final String url, final List<NameValuePair> nameValuePairList,
			final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				doPostRequest(url, nameValuePairList, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {

						hashMap.put("isSuccess", true);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);

					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception exception) {

						hashMap.put("isSuccess", false);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
						hashMap.put("exception", exception);

					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				if (isSuccess) {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");
					try {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");

					Exception exception = (Exception) hashMap.get("exception");

					onRequestFinished.onFailure(httpRequest, httpResponse,
							exception);
				}

			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onCancelled() {

			}
		}).execute("");
	}

	public AsyncTask<String, Integer, HashMap<String, Object>> doAsyncPostRequest(
			final String url, final MultipartEntity multipartEntity,
			final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				doPostRequest(url, multipartEntity, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {

						hashMap.put("isSuccess", true);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);

					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception exception) {

						hashMap.put("isSuccess", false);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
						hashMap.put("exception", exception);

					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				if (isSuccess) {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");
					try {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");

					Exception exception = (Exception) hashMap.get("exception");

					onRequestFinished.onFailure(httpRequest, httpResponse,
							exception);
				}

			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onCancelled() {

			}
		}).execute("");
	}

	public AsyncTask<String, Integer, HashMap<String, Object>> doAsyncGetRequest(
			final String url, final HashMap<String, String> hashMapParams,
			final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				doGetRequest(url, hashMapParams, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {
						hashMap.put("isSuccess", true);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);

					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception exception) {
						hashMap.put("isSuccess", false);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
						hashMap.put("exception", exception);

					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				if (isSuccess) {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");
					try {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");

					Exception exception = (Exception) hashMap.get("exception");

					onRequestFinished.onFailure(httpRequest, httpResponse,
							exception);
				}

			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onCancelled() {

			}
		}).execute("");
	}

	public AsyncTask<String, Integer, HashMap<String, Object>> doAsyncGetRequest(
			final String url, final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				doGetRequest(url, new OnRequestFinished() {

					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse) throws Exception {
						hashMap.put("isSuccess", true);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);

					}

					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception exception) {
						hashMap.put("isSuccess", false);
						hashMap.put("httpRequest", httpRequest);
						hashMap.put("httpResponse", httpResponse);
						hashMap.put("exception", exception);

					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				if (isSuccess) {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");
					try {
						onRequestFinished.onSuccess(httpRequest, httpResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					HttpRequestBase httpRequest = (HttpRequestBase) hashMap
							.get("httpRequest");
					HttpResponse httpResponse = (HttpResponse) hashMap
							.get("httpResponse");

					Exception exception = (Exception) hashMap.get("exception");

					onRequestFinished.onFailure(httpRequest, httpResponse,
							exception);
				}

			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onCancelled() {

			}
		}).execute("");
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
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
}
