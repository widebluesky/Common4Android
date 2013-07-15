package com.hiputto.common4android.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

import com.hiputto.common4android.R;
import com.hiputto.common4android.exception.HP_ErrorHttpStatusException;
import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestFinished;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends HP_BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		logErrorMessage(new HP_ErrorHttpStatusException().getMessage());

		// doRequest();

	}

	public void doRequest() {
		String url = "http://192.168.1.78/beta_appinterfaces/login";
		HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("key1", "a");
		hashMap.put("key2", "a");
		hashMap.put("key3", "a");
		hashMap.put("key4", "a");
		hashMap.put("key5", "a");

		netWorkUtils.doAsyncRequest(url, new OnRequestFinished() {

			@Override
			public void onSuccess(HttpRequestBase httpRequest,
					HttpResponse httpResponse) throws Exception {
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity()
									.getContent()));

					StringBuilder sb = new StringBuilder();
					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						sb.append(s);
					}
					reader.close();

					logErrorMessage("onSuccess:" + sb.toString());

				}
			}

			@Override
			public void onFailure(HttpRequestBase httpRequest,
					HttpResponse httpResponse, Exception e) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
