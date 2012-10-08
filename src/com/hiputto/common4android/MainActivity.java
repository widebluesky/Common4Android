package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_BitmapUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends HP_BaseActivity {

	ImageView imageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		
		String imageUrl = "http://m2.img.libdd.com/farm4/2012/1008/09/CCCA769D1889542204D0ECBB0DFA0F19148484189977_500_316.jpg";
		
		HP_NetWorkUtils hp_NetWorkUtils = new HP_NetWorkUtils();
		
		hp_NetWorkUtils.getAsyncRequestBitmap(imageUrl, new OnRequestBitmapFinished() {
			
			@Override
			public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
					boolean isSuccess) {
				Bitmap bm = HP_BitmapUtils.bitmap2Round(bitmap, (float) 20.0);
				bm = HP_BitmapUtils.bitmap2WatermarkText(bm, "asdfasdfaasdfadsfdsafdasfadsasdfdasfad", Color.RED, 50, 0, 0,true);
				
				imageView.setImageBitmap(bm);
			}
		});
		
	}

}
