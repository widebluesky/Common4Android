package com.hiputto.common4android;

import com.hiputto.common4android.anim.HP_RotateXAnimation;
import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_AnimationUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends HP_BaseActivity {

	private ImageView imageView;
	private Button button;
	private int centerX ;
	private int centerY ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
//		imageView.setVisibility(View.INVISIBLE);
		button = (Button) findViewById(R.id.button);
		
		centerX = getScreenWidthPixels()/2;
		centerY = getScreenHeightPixels()/2;
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//播放动画
//				Animation animation =  new HP_AnimationUtils().animTranslate(0, 0, 0, 500, 1000);
//				animation.setInterpolator(new OvershootInterpolator());
				
				imageView.startAnimation(new HP_RotateXAnimation(0, 90, centerX, centerY));
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
