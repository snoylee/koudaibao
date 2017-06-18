package com.kdkj.koudailicai.view.fragment;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.util.ResourceUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentIndicator extends LinearLayout implements OnClickListener {

	private int mDefaultIndicator = 0;
	public static int mCurIndicator; //当前位置
	private static View[] mIndicators;
	
	private OnIndicateListener mOnIndicateListener;
	private double bottomMenuHeightRate = 0.09;
	private int screenHeight;
	private int screenWidth;
	
	private static final String TAG_ICON_0 = "icon_tag_0";
	private static final String TAG_ICON_1 = "icon_tag_1";
	private static final String TAG_ICON_2 = "icon_tag_2";
	private static final String TAG_ICON_3 = "icon_tag_3";
	
	private static final String TAG_TEXT_0 = "text_tag_0";
	private static final String TAG_TEXT_1 = "text_tag_1";
	private static final String TAG_TEXT_2 = "text_tag_2";
	private static final String TAG_TEXT_3 = "text_tag_3";
	
	private static int COLOR_UNSELECT;
	private static int COLOR_SELECT;
	private int BACK_COLOR = this.getResources().getColor(R.color.bottom_back_color);

	private FragmentIndicator(Context context) {
		super(context);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth  = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		COLOR_UNSELECT = this.getResources().getColor(R.color.bottom_text_color);
		COLOR_SELECT = this.getResources().getColor(R.color.bottom_text_pressed_color);
	}

	public FragmentIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth  = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		mCurIndicator = mDefaultIndicator;
		COLOR_UNSELECT = this.getResources().getColor(R.color.bottom_text_color);
		COLOR_SELECT = this.getResources().getColor(R.color.bottom_text_pressed_color);
		setOrientation(LinearLayout.HORIZONTAL);
		init();
	}
	
	private View createIndicator(int iconResID, int stringResID, int stringColor, String iconTag, String textTag) {
		int menuHeight = (int) (screenHeight*bottomMenuHeightRate);//底部菜单栏高度占显示屏幕的0.09 
		LinearLayout view = new LinearLayout(getContext());
		view.setOrientation(LinearLayout.VERTICAL);
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, menuHeight, 1));
		view.setGravity(Gravity.CENTER);
		view.setPadding(0, menuHeight/7, 0, menuHeight/10);
		
		ImageView iconView = new ImageView(getContext());
		iconView.setTag(iconTag);
		iconView.setLayoutParams(new LinearLayout.LayoutParams(menuHeight*3/7, menuHeight*3/7));
		iconView.setImageResource(iconResID);
		
		TextView textView = new TextView(getContext());
		textView.setTag(textTag);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textView.setTextColor(stringColor);
		textView.setTextSize(ResourceUtil.getXmlDef(this.getContext(), R.dimen.bottom_text_size));
		textView.setText(stringResID);
		textView.setGravity(Gravity.CENTER);
		
		view.addView(iconView);
		view.addView(textView);
		return view;

	}

	private void init() {
		mIndicators = new View[4];
		mIndicators[0] = createIndicator(R.drawable.tabicon_01_pressed, R.string.tab_01, COLOR_SELECT, TAG_ICON_0, TAG_TEXT_0);
		mIndicators[0].setBackgroundColor(BACK_COLOR);
		mIndicators[0].setTag(Integer.valueOf(0));
		mIndicators[0].setOnClickListener(this);
		addView(mIndicators[0]);
		mIndicators[1] = createIndicator(R.drawable.tabicon_02, R.string.tab_02, COLOR_UNSELECT, TAG_ICON_1, TAG_TEXT_1);
		mIndicators[1].setBackgroundColor(BACK_COLOR);
		mIndicators[1].setTag(Integer.valueOf(1));
		mIndicators[1].setOnClickListener(this);
		addView(mIndicators[1]);
		mIndicators[2] = createIndicator(R.drawable.tabicon_03, R.string.tab_03, COLOR_UNSELECT, TAG_ICON_2, TAG_TEXT_2);
		mIndicators[2].setBackgroundColor(BACK_COLOR);
		mIndicators[2].setTag(Integer.valueOf(2));
		mIndicators[2].setOnClickListener(this);
		addView(mIndicators[2]);
		mIndicators[3] = createIndicator(R.drawable.tabicon_04, R.string.tab_04, COLOR_UNSELECT, TAG_ICON_3, TAG_TEXT_3);
		mIndicators[3].setBackgroundColor(BACK_COLOR);
		mIndicators[3].setTag(Integer.valueOf(3));
		mIndicators[3].setOnClickListener(this);
		addView(mIndicators[3]);
	}

	public static void setIndicator(int which) {
		// clear previous status.
		ImageView prevIcon;
		TextView prevText;
		switch(mCurIndicator) {
			case 0:
				prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_0);
				prevIcon.setImageResource(R.drawable.tabicon_01);
				prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_0);
				prevText.setTextColor(COLOR_UNSELECT);
				break;
			case 1:
				prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_1);
				prevIcon.setImageResource(R.drawable.tabicon_02);
				prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_1);
				prevText.setTextColor(COLOR_UNSELECT);
				break;
			case 2:
				prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_2);
				prevIcon.setImageResource(R.drawable.tabicon_03);
				prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_2);
				prevText.setTextColor(COLOR_UNSELECT);
				break;
			case 3:
				prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_3);
				prevIcon.setImageResource(R.drawable.tabicon_04);
				prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_3);
				prevText.setTextColor(COLOR_UNSELECT);
				break;	
		}
		
		// update current status.
		ImageView currIcon;
		TextView currText;
		switch(which) {
			case 0:
				currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_0);
				currIcon.setImageResource(R.drawable.tabicon_01_pressed);
				currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_0);
				currText.setTextColor(COLOR_SELECT);
				break;
			case 1:
				currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_1);
				currIcon.setImageResource(R.drawable.tabicon_02_pressed);
				currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_1);
				currText.setTextColor(COLOR_SELECT);
				break;
			case 2:
				currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_2);
				currIcon.setImageResource(R.drawable.tabicon_03_pressed);
				currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_2);
				currText.setTextColor(COLOR_SELECT);
				break;
			case 3:
				currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_3);
				currIcon.setImageResource(R.drawable.tabicon_04_pressed);
				currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_3);
				currText.setTextColor(COLOR_SELECT);
				break;	
		}
		mCurIndicator = which;
	}

	public interface OnIndicateListener {
		public void onIndicate(View v, int which);
		public void goLogin();
		public void goNewer();
		public void closePop();
	}

	public void setOnIndicateListener(OnIndicateListener listener) {
		mOnIndicateListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (mOnIndicateListener != null) {
			int tag = (Integer) v.getTag();
			if(tag != 2)
				mOnIndicateListener.closePop();
			switch (tag) {
			case 0:
				if (mCurIndicator != 0) {
					mOnIndicateListener.onIndicate(v, 0);
					setIndicator(0);
				}
				break;
			case 1:
				if (mCurIndicator != 1) {
					KDLCApplication.app.getSession().set("p2pListAutoRefreshClick", "1");
					mOnIndicateListener.onIndicate(v, 1);
					setIndicator(1);
				}
				break;
			case 2:
				if (mCurIndicator != 2) {
					KDLCApplication.app.getSession().set("selfCenterAutoRefreshClick", "1");
					if(Tool.isBlank(KDLCApplication.app.getSessionVal("uid"))) {
						mOnIndicateListener.goNewer();
					} else if(!KDLCApplication.app.hasLogin()) {
						mOnIndicateListener.goLogin();
					} else {
						mOnIndicateListener.onIndicate(v, 2);
						setIndicator(2);
					}
				}
				break;
			case 3:
				if (mCurIndicator != 3) {
					mOnIndicateListener.onIndicate(v, 3);
					setIndicator(3);
				}
				break;	
			default:
				break;
			}
		}
	}
}
