package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_AnimationUtils;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends HP_BaseActivity {

	private LinearLayout menuLayout, mainLayout;
	private Button button;

	private VelocityTracker mVelocityTracker;
	private int mLastMotionX;
	private boolean isSliding = false;
	private boolean hasSlidedRight = false;
	private int remainingSlidedAreaWidth = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		remainingSlidedAreaWidth = getScreenWidth()/2;
		
		menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);

		button = (Button) mainLayout.getChildAt(0);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(hasSlidedRight){
					doLeftSlide();
				}else{
					doRightSlide();
				}
				
			}
		});
		
		mainLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (isSliding) {
					return false;
				}

				int x = (int) event.getX();
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();// 获得VelocityTracker类实例
				}
				mVelocityTracker.addMovement(event);// 将事件加入到VelocityTracker类实例中

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastMotionX = x;
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					// 设置units的值为1000，意思为一秒时间内运动了多少个像素
					mVelocityTracker.computeCurrentVelocity(1000);
					float velocityX = mVelocityTracker.getXVelocity();

					if ((velocityX > 500 || velocityX < -500)
							&& Math.abs(x - mLastMotionX) > 150) {
						// 速度快，并且移动了150像素
						if (velocityX > 0) {
							// 右滑
							doRightSlide();
						} else {
							// 左滑
							doLeftSlide();
						}
					}
					releaseVelocityTracker();
					break;
				case MotionEvent.ACTION_CANCEL:
					releaseVelocityTracker();
					break;
				}
				return true;
			}
		});
	}

	protected void doLeftSlide() {
		if (hasSlidedRight) {
			mainLayout.startAnimation(new HP_AnimationUtils().animTranslate(0,
					-getScreenWidth() + remainingSlidedAreaWidth, 0, 0, 500,
					slideAnimationListener));
		}

	}

	protected void doRightSlide() {
		if (!hasSlidedRight) {
			mainLayout.startAnimation(new HP_AnimationUtils().animTranslate(0,
					getScreenWidth() - remainingSlidedAreaWidth, 0, 0, 500,
					slideAnimationListener));
		}

	}

	private void releaseVelocityTracker() {
		if (null != mVelocityTracker) {
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	AnimationListener slideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation arg0) {
			isSliding = true;
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationEnd(Animation arg0) {
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.FILL_PARENT);
			params.width = getScreenWidth();
			params.height = getScreenHeight();

			if (!hasSlidedRight) {
				params.setMargins(getScreenWidth() - remainingSlidedAreaWidth,
						0, 0, 0);
				hasSlidedRight = true;
			} else {
				params.setMargins(0, 0, 0, 0);
				hasSlidedRight = false;
			}

			mainLayout.setLayoutParams(params);
			mainLayout.clearAnimation();
			isSliding = false;
		}
	};

}
