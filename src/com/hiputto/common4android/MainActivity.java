package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_SystemIntentUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends HP_BaseActivity {

	private ImageView imageView;
	private Button button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		button = (Button) findViewById(R.id.button);
		
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//播放动画
				startActivity(new HP_SystemIntentUtils().getOpenCameraIntent());
			}
		});
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("click", "click");
			}
		});
	}

}
