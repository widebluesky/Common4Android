package com.hiputto.common4android.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import com.hiputto.common4android.util.HP_NetWorkAsyncTask.AsyncTaskSteps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class HP_NetWorkUtils {
	private int REQUEST_TIMEOUT = 5 * 1000;// 请求超时
	private int SO_TIMEOUT = 0 * 1000; // 数据接收超时

	public void setRequestTimeOut(int time) {
		REQUEST_TIMEOUT = time;
	}

	public void setSoTimeOut(int time) {
		SO_TIMEOUT = time;
	}

	public int getRequestTimeOut() {
		return this.REQUEST_TIMEOUT;
	}

	public int getSoTimeOut() {
		return this.SO_TIMEOUT;
	}

	public void sendRequestStrEntity(String url, String str,
			OnRequestFinished onRequestFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			StringEntity entity = new StringEntity(str, HTTP.UTF_8);

			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent()));

				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					sb.append(s);
				}
				resultStr = sb.toString();
			}

			isSuccess = true;

		} catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			resultStr = e.toString();
		} finally {
			onRequestFinished.onRequestFinished(resultStr, isSuccess);
		}
	}

	public void sendRequest(String url, OnRequestFinished onRequestFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent()));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					sb.append(s);
				}
				resultStr = sb.toString();
			}

			isSuccess = true;

		} catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			resultStr = e.toString();
		} finally {
			onRequestFinished.onRequestFinished(resultStr, isSuccess);
		}
	}

	public void sendRequestParamsEntity(String url,
			List<NameValuePair> nameValuePairList,
			OnRequestFinished onRequestFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent()));

				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					sb.append(s);
				}
				resultStr = sb.toString();
			}

			isSuccess = true;

		} catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			resultStr = e.toString();
		} finally {
			onRequestFinished.onRequestFinished(resultStr, isSuccess);
		}
	}

	public void getRequestData(String url,
			OnRequestDataFinished onRequestDataFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		byte[] data = null;
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
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
			}

			isSuccess = true;

		} catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			resultStr = e.toString();
			data = null;
		} finally {
			onRequestDataFinished.onRequestDataFinished(resultStr, data,
					isSuccess);
		}
	}

	public void getRequestBitmap(String url,
			OnRequestBitmapFinished bitmapLoadFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		Bitmap bitmap = null;

		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				DataInputStream dataInputStream = new DataInputStream(
						httpResponse.getEntity().getContent());
				bitmap = BitmapFactory.decodeStream(dataInputStream);

				dataInputStream.close();
			}

			isSuccess = true;

		} catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			bitmap = null;
			resultStr = e.toString();
		} finally {
			bitmapLoadFinished.onRequestBitmapFinished(resultStr, bitmap,
					isSuccess);
		}
	}

	private HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	public interface OnRequestFinished {
		public void onRequestFinished(String resultStr, boolean isSuccess);
	}

	public interface OnRequestDataFinished {
		public void onRequestDataFinished(String resultStr, byte[] data,
				boolean isSuccess);
	}

	public interface OnRequestBitmapFinished {
		public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
				boolean isSuccess);
	}

	public AsyncTask<String, Integer, HashMap<String, Object>> sendAsyncRequest(
			final String url, final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {
			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();

				sendRequest(url, new OnRequestFinished() {
					@Override
					public void onRequestFinished(String resultStr,
							boolean isSuccess) {
						hashMap.put("isSuccess", isSuccess);
						hashMap.put("resultStr", resultStr);
					}
				});
				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				String resultStr = (String) hashMap.get("resultStr");
				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				onRequestFinished.onRequestFinished(resultStr, isSuccess);
			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}
		}).execute("");

	}

	public AsyncTask<String, Integer, HashMap<String, Object>> sendAsyncRequestStrEntity(
			final String url, final String str,
			final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {

			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();
				sendRequestStrEntity(url, str, new OnRequestFinished() {
					@Override
					public void onRequestFinished(String resultStr,
							boolean isSuccess) {
						hashMap.put("isSuccess", isSuccess);
						hashMap.put("resultStr", resultStr);
					}
				});
				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				String resultStr = (String) hashMap.get("resultStr");
				boolean isSuccess = (Boolean) hashMap.get("isSuccess");

				onRequestFinished.onRequestFinished(resultStr, isSuccess);
			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}
		}).execute("");

	}

	public AsyncTask<String, Integer, HashMap<String, Object>> sendAsyncRequestParamsEntity(
			final String url, final List<NameValuePair> nameValuePairList,
			final OnRequestFinished onRequestFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {

			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();
				sendRequestParamsEntity(url, nameValuePairList,
						new OnRequestFinished() {
							@Override
							public void onRequestFinished(String resultStr,
									boolean isSuccess) {
								hashMap.put("isSuccess", isSuccess);
								hashMap.put("resultStr", resultStr);
							}
						});
				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				String resultStr = (String) hashMap.get("resultStr");
				boolean isSuccess = (Boolean) hashMap.get("isSuccess");

				onRequestFinished.onRequestFinished(resultStr, isSuccess);
			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}
		}).execute("");

	}

	public AsyncTask<String, Integer, HashMap<String, Object>> getAsyncRequestData(
			final String url, final OnRequestDataFinished onRequestDataFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {

			@Override
			public void onPreExecute() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();
				getRequestData(url, new OnRequestDataFinished() {
					@Override
					public void onRequestDataFinished(String resultStr,
							byte[] data, boolean isSuccess) {
						hashMap.put("isSuccess", isSuccess);
						hashMap.put("resultStr", resultStr);
						hashMap.put("data", data);

					}
				});
				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {
				String resultStr = (String) hashMap.get("resultStr");
				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				byte[] data = (byte[]) hashMap.get("data");

				onRequestDataFinished.onRequestDataFinished(resultStr, data,
						isSuccess);
			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}
		}).execute("");

	}

	public AsyncTask<String, Integer, HashMap<String, Object>> getAsyncRequestBitmap(
			final String url, final OnRequestBitmapFinished bitmapLoadFinished) {

		return new HP_NetWorkAsyncTask(new AsyncTaskSteps() {

			@Override
			public void onPreExecute() {
			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {
				final HashMap<String, Object> hashMap = new HashMap<String, Object>();
				getRequestBitmap(url, new OnRequestBitmapFinished() {

					@Override
					public void onRequestBitmapFinished(String resultStr,
							Bitmap bitmap, boolean isSuccess) {
						hashMap.put("isSuccess", isSuccess);
						hashMap.put("resultStr", resultStr);
						hashMap.put("bitmap", bitmap);
					}
				});

				return hashMap;
			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {
				String resultStr = (String) hashMap.get("resultStr");
				boolean isSuccess = (Boolean) hashMap.get("isSuccess");
				Bitmap bitmap = (Bitmap) hashMap.get("bitmap");
				bitmapLoadFinished.onRequestBitmapFinished(resultStr, bitmap,
						isSuccess);
			}

			@Override
			public void onProgressUpdate(Integer... values) {

			}
		}).execute("");

	}

}