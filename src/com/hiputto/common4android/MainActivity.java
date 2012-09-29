package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_NetWorkAsyncTask;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestFinished;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends HP_BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		HP_NetWorkUtils hp_NetWorkUtils = new HP_NetWorkUtils();
		String url = "http://www.baidu.com";
		
		HP_NetWorkAsyncTask hp_NetWorkAsyncTask = (HP_NetWorkAsyncTask) hp_NetWorkUtils.sendAsyncRequest(url, new OnRequestFinished() {
			
			@Override
			public void onRequestFinished(String resultStr, boolean isSuccess) {
				
				Log.d("ads", "asdf"+resultStr);
			
			}
		});
		
		
		
	}

}
