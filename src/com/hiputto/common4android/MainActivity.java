package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_BitmapUtils;
import com.hiputto.common4android.util.HP_NetWorkAsyncTask;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestFinished;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends HP_BaseActivity {

	ImageView imageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		
		String imageUrl = "http://m3.img.libdd.com/farm4/2012/0929/11/37146BE13C8C7638E83030DE6C64FA096862475EF698_480_382.JPEG";
		
		HP_NetWorkUtils hp_NetWorkUtils = new HP_NetWorkUtils();
		
		hp_NetWorkUtils.getAsyncRequestBitmap(imageUrl, new OnRequestBitmapFinished() {
			
			@Override
			public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
					boolean isSuccess) {
				Bitmap bm = HP_BitmapUtils.bitmap2Round(bitmap, (float) 20.0);
				bm = HP_BitmapUtils.bitmap2Resize(bm, 500, 500);
				bm = HP_BitmapUtils.bitmap2ReflectionImage(bm,10);
				imageView.setImageBitmap(bm);
			}
		});
		
	}

}
