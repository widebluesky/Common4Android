package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_AnimationUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
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
				imageView.startAnimation(new HP_AnimationUtils().animAlpha(0.1f, 1.0f, 500,new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
//						imageView.getLayoutParams().;
//						params.
						
						LayoutParams params = (LayoutParams) imageView.getLayoutParams();
						params.height = 0;
						params.width = 0;
						imageView.setLayoutParams(params);
						imageView.clearAnimation();
						
					}
				}));
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
