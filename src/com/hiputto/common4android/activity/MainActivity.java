package com.hiputto.common4android.activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiputto.common4android.R;
import com.hiputto.common4android.exception.HP_ErrorHttpStatusException;
import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_DateUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestDrawableFinished;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestFinished;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends HP_BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// logErrorMessage(new HP_ErrorHttpStatusException().getMessage());

		// doRequest();
		for (int i = 0; i < 1; i++) {
			String url = "";
			url = "https://api.weibo.com/2/statuses/home_timeline.json?"
					+ "access_token=2.004IPtFCgsKwwCc4b784204bU6I4UC"
					+ "&count=100";
			url = "http://t10.baidu.com/it/u=1809038154,4292577532&fm=58";
			
			HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
			netWorkUtils.doAsyncTest(url, new OnRequestDrawableFinished() {
				
				@Override
				public void onSuccess(Drawable drawable) throws Exception {
					System.out.println("onSuccess");
				}
				
				@Override
				public void onFailure(Exception e) {
					System.out.println("onFailure");
					
				}
			});
//			doNetWorkUtils(url);
		}
		
//		try {
//			doSomeThing(url);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void doNetWorkUtils(String url) {
		HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
		netWorkUtils.doAsyncPostRequestBitmap(url, new OnRequestBitmapFinished() {
			
			@Override
			public void onSuccess(HttpRequestBase httpRequest,
					HttpResponse httpResponse, Bitmap bitmap) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(HttpRequestBase httpRequest,
					HttpResponse httpResponse, Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void doSomeThing(String url) throws ClientProtocolException, IOException, JSONException {
		// 实例化请求对象
        HttpGet request = new HttpGet(url);
        
//        // 设置要发送的数据 以 name=value 键值对
//        HttpEntity requestEntity = new UrlEncodedFormEntity(params, "UTF-8");
//        request.setEntity(requestEntity);
        //获取HttpClient
        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {  
            public void process(final HttpRequest request,  
                    final HttpContext context) throws HttpException,  
                    IOException {  
                if (!request.containsHeader("Accept-Encoding")) {  
                    request.addHeader("Accept-Encoding", "gzip");  
                }  
            }  
          
        });  

        //设置请求超时时间
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        // 发送请求 其实底层就是 OutputStream,并获得响应
        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 获得服务器响应返回的数据
            //         
            HttpEntity entity = response.getEntity();
            Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                for (HeaderElement element : ceheader.getElements()) {
                    if ("gzip".equalsIgnoreCase(element.getName())) {
                        entity = new GzipDecompressingEntity(response.getEntity());
                        break;
                    }
                }
            }

            String html = EntityUtils.toString(entity, "UTF-8");
           
            
            JSONObject jsonObject = new JSONObject(html);
            logErrorMessage("json数据:"+jsonObject.getJSONArray("statuses").length());
		}

	}

	public void doRequest() {
		String url = "https://api.weibo.com/2/users/show.json";
		// url = "http://www.youyouapp.com/beta_appinterfaces/login";
		// url = "http://tp2.sinaimg.cn/1904178193/180/5610154048/0";

		url = "https://api.weibo.com/2/statuses/home_timeline.json";
		// 请求参数：count=100&access_token=2.004IPtFCgsKwwCc4b784204bU6I4UC

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("access_token", "2.004IPtFCgsKwwCc4b784204bU6I4UC");
		hashMap.put("count", "100");

		HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();

		netWorkUtils.doAsyncGetRequest(url, hashMap, new OnRequestFinished() {

			@Override
			public void onSuccess(HttpRequestBase httpRequest,
					HttpResponse httpResponse) throws Exception {

				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					String content = EntityUtils.toString(
							httpResponse.getEntity(), HTTP.UTF_8);
					// DataInputStream dataInputStream = new DataInputStream(
					// httpResponse.getEntity().getContent());
					// Bitmap bitmap =
					// BitmapFactory.decodeStream(dataInputStream);
					// dataInputStream.close();
					//
					// ImageView imageView = (ImageView)
					// findViewById(R.id.imageview);
					// imageView.setImageBitmap(bitmap);

					logErrorMessage("onSuccess");

					logErrorMessage(content);

				} else {

					logErrorMessage("onFailure");
				}

			}

			@Override
			public void onFailure(HttpRequestBase httpRequest,
					HttpResponse httpResponse, Exception e) {
				// TODO Auto-generated method stub

			}
		});

		// HashMap<String, String> hashMap = new HashMap<String, String>();
		// hashMap.put("uid", "1904178193");
		// hashMap.put("access_token", "2.004IPtFCgsKwwCc4b784204bU6I4UC");
		//
		// netWorkUtils.doAsyncGetRequest(url, hashMap, new OnRequestFinished()
		// {
		//
		// @Override
		// public void onSuccess(HttpRequestBase httpRequest,
		// HttpResponse httpResponse) throws Exception {
		// logErrorMessage("onSuccess:" +
		// httpResponse.getStatusLine().getStatusCode());
		// if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		// {
		// BufferedReader reader = new BufferedReader(
		// new InputStreamReader(httpResponse.getEntity()
		// .getContent()));
		//
		// StringBuilder sb = new StringBuilder();
		// for (String s = reader.readLine(); s != null; s = reader
		// .readLine()) {
		// sb.append(s);
		// }
		// reader.close();
		//
		// logErrorMessage("onSuccess:" + sb.toString());
		//
		// } else {
		// logErrorMessage("onFailure");
		// }
		// }
		//
		// @Override
		// public void onFailure(HttpRequestBase httpRequest,
		// HttpResponse httpResponse, Exception e) {
		// e.printStackTrace();
		// logErrorMessage("onFailure:" + e.getMessage());
		// }
		// });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
