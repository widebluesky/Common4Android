package com.hiputto.common4android.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HP_NetWorkUtils {
	private int REQUEST_TIMEOUT = 10 * 1000;// 请求超时
	private int SO_TIMEOUT = 10 * 1000; // 数据接收超时

	public void setRequestTimeOut(int seconds) {
		REQUEST_TIMEOUT = seconds * 1000;
	}

	public void setSoTimeOut(int seconds) {
		SO_TIMEOUT = seconds * 1000;
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
	
	public void getRequestData(String url,OnRequestDataFinished onRequestDataFinished){
		String resultStr = "";
		boolean isSuccess = false;
		byte[] data = null;
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				DataInputStream dataInputStream = new DataInputStream(httpResponse.getEntity().getContent());
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				
				int index = 0;
				byte[] buffer = new byte[1024];
				while ((index = dataInputStream.read(buffer, 0, buffer.length)) != -1) {                     
					byteArrayOutputStream.write(buffer, 0, index);
				}
				
				data =  byteArrayOutputStream.toByteArray();
				
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
			onRequestDataFinished.onRequestDataFinished(resultStr, data, isSuccess);
		}
	}
	
	public void getRequestBitmap(String url,OnRequestBitmapFinished bitmapLoadFinished) {
		String resultStr = "";
		boolean isSuccess = false;
		Bitmap bitmap = null;
		
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				DataInputStream dataInputStream = new DataInputStream(httpResponse.getEntity().getContent());
				bitmap = BitmapFactory.decodeStream(dataInputStream);
				
				dataInputStream.close();
			}

			isSuccess = true;
			
		}  catch (Exception e) {
			Log.e("HP_" + e.getClass().getName(), e.getMessage());
			isSuccess = false;
			bitmap = null;
			resultStr = e.toString();
		} finally {
			bitmapLoadFinished.onRequestBitmapFinished(resultStr,bitmap, isSuccess);
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
		public void onRequestDataFinished(String resultStr, byte[] data, boolean isSuccess);
	}
	
	public interface OnRequestBitmapFinished {
		public void onRequestBitmapFinished(String resultStr, Bitmap bitmap, boolean isSuccess);
	}

}
