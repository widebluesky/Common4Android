package com.picperweek.common4android.activity;

import com.picperweek.common4android.R;
import com.picperweek.common4android.base.BaseActivity;

public class NextActivity extends BaseActivity {

	@Override
	public boolean needTranslucent() {
		return true;
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_next);
	}

	@Override
	public void initStaticData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		setStatusBarAlpha(0);
		setCanSlideBack(true);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
