package com.hiputto.common4android.activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

import com.hiputto.common4android.R;
import com.hiputto.common4android.exception.HP_ErrorHttpStatusException;
import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_DateUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestFinished;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends HP_BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// logErrorMessage(new HP_ErrorHttpStatusException().getMessage());

		doRequest();

	}

	public void doRequest() {
		String url = "https://api.weibo.com/2/users/show.json";
		// url = "http://www.youyouapp.com/beta_appinterfaces/login";
		url = "http://tp2.sinaimg.cn/1904178193/180/5610154048/0";

		HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();

		netWorkUtils.doAsyncGetRequest(url, new OnRequestFinished() {

			@Override
			public void onSuccess(HttpRequestBase httpRequest,
					HttpResponse httpResponse) throws Exception {

				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					
					DataInputStream dataInputStream = new DataInputStream(
							httpResponse.getEntity().getContent());
					Bitmap bitmap = BitmapFactory.decodeStream(dataInputStream);
					dataInputStream.close();

					ImageView imageView = (ImageView) findViewById(R.id.imageview);
					imageView.setImageBitmap(bitmap);

					logErrorMessage("onSuccess");

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
