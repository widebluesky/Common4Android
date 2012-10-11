package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_AnimationUtils;

import android.graphics.Interpolator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends HP_BaseActivity {

	private ImageView imageView;
	private Button button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setVisibility(View.INVISIBLE);
		button = (Button) findViewById(R.id.button);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//播放动画

				Animation animation =  new HP_AnimationUtils().animTranslate(0, 0, 0, 500, 1000);
				
				animation.setInterpolator(new OvershootInterpolator());
				imageView.startAnimation(animation);
			}
		});
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("click", "click");
			}
		});
		
//		new AccelerateInterpolator();
//		new DecelerateInterpolator();
//		new LinearInterpolator();
//		new AccelerateDecelerateInterpolator();
//		new CycleInterpolator(cycles);
	}
	

}
