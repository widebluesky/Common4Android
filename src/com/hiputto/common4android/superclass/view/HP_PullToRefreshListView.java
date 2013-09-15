package com.hiputto.common4android.superclass.view;

import java.util.Date;

import com.hiputto.common4android.R;
import com.hiputto.common4android.util.HP_DateUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HP_PullToRefreshListView extends ListView implements
		OnScrollListener {
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	// 正在刷新
	private final static int REFRESHING = 2;
	// 刷新完成
	private final static int DONE = 3;
	private final static int LOADING = 4;

	private final static int RATIO = 3;
	private LayoutInflater inflater;
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private boolean isRefreshable;

	int i = 1;

	public HP_PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public HP_PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(
				android.R.color.transparent));
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.head, null);
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		Log.v("@@@@@@", "width:" + headContentWidth + " height:"
				+ headContentHeight);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v("@@@@@@", "ACTION_DOWN 这是第  " + i++ + "步" + 1);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						Log.v("@@@@@@",
								"ACTION_UP PULL_To_REFRESH and changeHeaderViewByState()"
										+ " 这是第  " + i++ + "步前" + 2);
						changeHeaderViewByState();
						Log.v("@@@@@@",
								"ACTION_UP PULL_To_REFRESH and changeHeaderViewByState() "
										+ "这是第  " + i++ + "步后" + 2);
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						Log.v("@@@@@@",
								"ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState() "
										+ "这是第  " + i++ + "步" + 3);
						changeHeaderViewByState();
						onRefresh();
						Log.v("@@@@@@",
								"ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState()"
										+ " 这是第  " + i++ + "步" + 3);
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					startY = tempY;
					Log.v("@@@@@@", "ACTION_MOVE 这是第  " + i++ + "步" + 4);
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
							Log.v("@@@@@@", "changeHeaderViewByState() 这是第  "
									+ i++ + "步" + 5);
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							Log.v("@@@@@@",
									"ACTION_MOVE RELEASE_To_REFRESH 2  changeHeaderViewByState "
											+ "这是第  " + i++ + "步" + 6);
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							Log.v("@@@@@@", "changeHeaderViewByState "
									+ "这是第  " + i++ + "步前" + 7);
							changeHeaderViewByState();
							Log.v("@@@@@@", "changeHeaderViewByState "
									+ "这是第  " + i++ + "步后" + 7);
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							Log.v("@@@@@@",
									"ACTION_MOVE changeHeaderViewByState PULL_To_REFRESH 2"
											+ " 这是第  " + i++ + "步" + 8);
						}
					}
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							Log.v("@@@@@@",
									"ACTION_MOVE DONE changeHeaderViewByState "
											+ "这是第  " + i++ + "步前" + 9);
							changeHeaderViewByState();
							Log.v("@@@@@@",
									"ACTION_MOVE DONE changeHeaderViewByState "
											+ "这是第  " + i++ + "步后" + 9);
						}
					}
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
						Log.v("@@@@@@", -1 * headContentHeight
								+ (tempY - startY) / RATIO
								+ "ACTION_MOVE PULL_To_REFRESH 3  这是第  " + i++
								+ "步" + 10);
					}
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
						Log.v("@@@@@@", "ACTION_MOVE PULL_To_REFRESH 4 这是第  "
								+ i++ + "步" + 11);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("松开刷新");
			Log.v("@@@@@@", "RELEASE_To_REFRESH 这是第  " + i++ + "步" + 12
					+ "请释放 刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
				// tipsTextview.setText("isBack  is false ！！！");
			}
			Log.v("@@@@@@", "PULL_To_REFRESH 这是第  " + i++ + "步" + 13
					+ "  changeHeaderViewByState()");
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("加载中 ...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			Log.v("@@@@@@", "REFRESHING 这是第  " + i++ + "步"
					+ "正在加载中 ...REFRESHING");
			break;
		case DONE:
			TranslateAnimation bounceAnimation = new TranslateAnimation(
					TranslateAnimation.ABSOLUTE, 0,
					TranslateAnimation.ABSOLUTE, 0,
					TranslateAnimation.ABSOLUTE, 0,
					TranslateAnimation.ABSOLUTE, -1 * headContentHeight
							- headView.getPaddingTop());
			bounceAnimation.setDuration(200);
			bounceAnimation.setFillEnabled(true);
			bounceAnimation.setFillAfter(false);
			bounceAnimation.setFillBefore(true);
			bounceAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					headView.setPadding(0, -1 * headContentHeight, 0, 0);
					progressBar.setVisibility(View.GONE);
					arrowImageView.clearAnimation();
					arrowImageView.setImageResource(R.drawable.common_arrow);
					tipsTextview.setText("已经加载完毕");
					lastUpdatedTextView.setVisibility(View.VISIBLE);
					Log.v("@@@@@@", "DONE 这是第  " + i++ + "步" + "已经加载完毕- DONE ");

				}
			});
			startAnimation(bounceAnimation);
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		state = DONE;

		lastUpdatedTextView.setText("最后更新: "
				+ HP_DateUtils.getDateStringFromDate(new Date(),
						"yyy-MM-dd hh:mm"));
		changeHeaderViewByState();
		Log.v("@@@@@@", "onRefreshComplete() 被调用。。。");
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
			Log.v("@@@@@@", "onRefresh被调用，这是第  " + i++ + "步");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText("");
		// lastUpdatedTextView.setText("this is in MyListView:"
		// + new Date().toLocaleString());
		super.setAdapter(adapter);
	}
}