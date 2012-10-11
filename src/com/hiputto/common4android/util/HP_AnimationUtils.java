package com.hiputto.common4android.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class HP_AnimationUtils {

	// 所有动画结束后，需要在onAnimationEnd中重新设置view的位置、大小。
	// 然后clearAnimation。
	// 执行动画的view最好是RelativeLayout下的子组件，不会影响其他组件。
	//
	// View view = new View();
	// view.startAnimation(new HP_AnimationUtils().animTranslate(0,100,0, 100,
	// 500,new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// // TODO Auto-generated method stub
	// LayoutParams params = (LayoutParams) imageView.getLayoutParams();
	// params.height = imageView.getMeasuredHeight();
	// params.width = imageView.getMeasuredWidth();
	// params.setMargins(100, 100, 0, 0);
	// imageView.setLayoutParams(params);
	// imageView.clearAnimation();
	//
	// }
	// }));

	private Animation animationTranslate, animationRotate, animationScale,
			animationAlpha;

	public Animation animScale(float fromX, float toX, float fromY, float toY,
			long duration) {
		animationScale = new ScaleAnimation(1f, toX, 1f, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationScale.setDuration(duration);
		animationScale.setFillAfter(true);
		return animationScale;
	}

	public Animation animScale(float fromX, float toX, float fromY, float toY,
			long duration, AnimationListener animationListener) {
		animationScale = new ScaleAnimation(1f, toX, 1f, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationScale.setDuration(duration);
		animationScale.setFillAfter(true);
		animationScale.setAnimationListener(animationListener);
		return animationScale;
	}

	public Animation animRotate(float fromDegrees, float toDegrees,
			float pivotXValue, float pivotYValue, long duration) {
		animationRotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setDuration(duration);
		animationRotate.setFillAfter(true);
		return animationRotate;
	}

	public Animation animRotate(float fromDegrees, float toDegrees,
			float pivotXValue, float pivotYValue, long duration,
			AnimationListener animationListener) {
		animationRotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setDuration(duration);
		animationRotate.setAnimationListener(animationListener);
		animationRotate.setFillAfter(true);
		return animationRotate;
	}

	public Animation animTranslate(float fromX, float toX, float fromY,
			float toY, long duration) {
		animationTranslate = new TranslateAnimation(fromX, toX, fromY, toY);
		animationTranslate.setDuration(duration);
		animationTranslate.setFillAfter(true);
		return animationTranslate;
	}

	public Animation animTranslate(float fromX, float toX, float fromY,
			float toY, long duration, AnimationListener animationListener) {
		animationTranslate = new TranslateAnimation(fromX, toX, fromY, toY);
		animationTranslate.setDuration(duration);
		animationTranslate.setAnimationListener(animationListener);
		animationTranslate.setFillAfter(true);
		return animationTranslate;
	}

	public Animation animAlpha(float fromAlpha, float toAlpha, long duration) {
		animationAlpha = new AlphaAnimation(fromAlpha, toAlpha);
		animationAlpha.setDuration(duration);
		animationAlpha.setFillAfter(true);
		return animationAlpha;
	}

	public Animation animAlpha(float fromAlpha, float toAlpha, long duration,
			AnimationListener animationListener) {
		animationAlpha = new AlphaAnimation(fromAlpha, toAlpha);
		animationAlpha.setDuration(duration);
		animationAlpha.setFillAfter(true);
		animationAlpha.setAnimationListener(animationListener);
		return animationAlpha;
	}

}
