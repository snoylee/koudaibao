package com.kdkj.koudailicai.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.viewpager.ViewPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GuideActivity extends BaseActivity{
	private double distanceTop = 0.151;
	private double distanceBottom = 0.181;
	private double distanceH = 0.026;
	private ImageView guideOne, guideTwo, guideThree;
	private TextView oneWord, twoWord, threeWord;
	private int screenHeight;
	private int screenWidth;
	// 蓝色
	private double blueDistance = 0.125;
	// 绿色
	private double greenDistance = 0.065;
	private ViewPager viewPager;
	private List<View> listViews = new ArrayList<View>();
	private LinearLayout dot;
	private ImageView curDot;
	private int offset; // 位移量
	private int curPos = 0; // 记录当前的位置
	private RelativeLayout guideLayout;
    private ImageView dotView;
    private LinearLayout dotLayout;
 // 底部小点图片
 	private ImageView[] dots;

 	// 记录当前选中位置
 	private int currentIndex;
 	private RelativeLayout ll;
 	
 	private View guideImage;
 	private TextView openApp;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		findView();
		initContent();
	}

	private void findView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		ll = (RelativeLayout) findViewById(R.id.guidelayout);
	}

	private void initContent() {
		LayoutInflater mInflater = getLayoutInflater();
		View guideOneView = mInflater.inflate(R.layout.activity_guide_page,
				null);
		listViews.add(guideOneView);
		guideOne = (ImageView) guideOneView.findViewById(R.id.guideone);
		oneWord = (TextView) guideOneView.findViewById(R.id.oneword);
		View guideTwoView = mInflater.inflate(R.layout.activity_guide_page1,
				null);
		guideTwo = (ImageView) guideTwoView.findViewById(R.id.guidetwo);
		twoWord = (TextView) guideTwoView.findViewById(R.id.twoword);
		listViews.add(guideTwoView);
		View guideThreeView = mInflater.inflate(R.layout.activity_guide_page2,
				null);
		guideLayout = (RelativeLayout) guideThreeView
				.findViewById(R.id.guidelayout);
		guideThree = (ImageView) guideThreeView.findViewById(R.id.guidethree);
		threeWord = (TextView) guideThreeView.findViewById(R.id.threeword);
		listViews.add(guideThreeView);
		
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		RelativeLayout.LayoutParams redLayoutParams = (RelativeLayout.LayoutParams) guideOne
				.getLayoutParams();
		redLayoutParams.topMargin = (int) (screenHeight * distanceTop);
		guideOne.setLayoutParams(redLayoutParams);

		RelativeLayout.LayoutParams guideOneWidth = (RelativeLayout.LayoutParams) guideOne
				.getLayoutParams();
		guideOneWidth.width = (int) (screenWidth * 0.8);
		guideOneWidth.height = (int) (screenWidth * 0.8);
		guideOne.setLayoutParams(guideOneWidth);
		
		//第四张图
		View guideFourView = mInflater.inflate(R.layout.activity_guide_page3,null);
		guideImage=guideFourView.findViewById(R.id.guideImage);
		openApp=(TextView)guideFourView.findViewById(R.id.openApp);
		listViews.add(guideFourView);

		viewPager.setAdapter(new ViewPagerAdapter(listViews));
		viewPager.setCurrentItem(0);
		RelativeLayout.LayoutParams fourdotLayoutParams = (RelativeLayout.LayoutParams) guideImage.getLayoutParams();
		fourdotLayoutParams.topMargin = (int) (screenHeight * 0.153);
		fourdotLayoutParams.height = (int) (screenWidth * 0.65);
		fourdotLayoutParams.width = (int) (screenWidth * 0.65);
		guideImage.setLayoutParams(fourdotLayoutParams);

		RelativeLayout.LayoutParams openAppHeight = (RelativeLayout.LayoutParams) openApp.getLayoutParams();
		openAppHeight.bottomMargin = (int) (screenHeight * 0.185);
		openAppHeight.height = (int) (screenWidth * 0.16);
		openAppHeight.width = (int) (screenWidth * 0.7);
		openApp.setLayoutParams(openAppHeight);
	
		
		//圆点
		if(Tool.isMX())
		{
			RelativeLayout.LayoutParams dotLayoutParams = (RelativeLayout.LayoutParams) ll.getLayoutParams();
			dotLayoutParams.bottomMargin = 40;
			ll.setLayoutParams(dotLayoutParams);
		}
		
		
		//红色
		RelativeLayout.LayoutParams redBottomLayoutParams = (RelativeLayout.LayoutParams) oneWord.getLayoutParams();
		redBottomLayoutParams.bottomMargin = (int) (!Tool.isMX() ? (screenHeight * distanceBottom) : (screenHeight * distanceBottom - 145));
		oneWord.setLayoutParams(redBottomLayoutParams);

		// 蓝色
		RelativeLayout.LayoutParams blueLayoutParams = (RelativeLayout.LayoutParams) guideTwo
				.getLayoutParams();
		blueLayoutParams.topMargin = (int) (screenHeight * blueDistance);
		guideTwo.setLayoutParams(blueLayoutParams);

		RelativeLayout.LayoutParams guidetwoWidth = (RelativeLayout.LayoutParams) guideTwo
				.getLayoutParams();
		guidetwoWidth.width = (int) (screenWidth * 0.85);
		guidetwoWidth.height = (int) (screenWidth * 0.85);
		guideTwo.setLayoutParams(guidetwoWidth);

		RelativeLayout.LayoutParams blueBottomLayoutParams = (RelativeLayout.LayoutParams) twoWord
				.getLayoutParams();
		blueBottomLayoutParams.bottomMargin = (int) (!Tool.isMX() ? (screenHeight * distanceBottom) : (screenHeight * distanceBottom - 145));
		twoWord.setLayoutParams(blueBottomLayoutParams);

		// 绿色
		RelativeLayout.LayoutParams greenLayoutParams = (RelativeLayout.LayoutParams) guideThree
				.getLayoutParams();
		greenLayoutParams.topMargin = (int) (screenHeight * 0.053);
		guideThree.setLayoutParams(greenLayoutParams);

		RelativeLayout.LayoutParams guidethreeWidth = (RelativeLayout.LayoutParams) guideThree
				.getLayoutParams();
		guidethreeWidth.width = (int) (screenWidth * 0.8);
		guidethreeWidth.height = (int) (screenWidth * 0.91);
		guideThree.setLayoutParams(guidethreeWidth);

		RelativeLayout.LayoutParams greenBottomLayoutParams = (RelativeLayout.LayoutParams) threeWord
				.getLayoutParams();
		greenBottomLayoutParams.bottomMargin = (int) (!Tool.isMX() ? (screenHeight * distanceBottom) : (screenHeight * distanceBottom - 145));
		threeWord.setLayoutParams(greenBottomLayoutParams);
		initDots();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setCurrentDot(arg0);
				if(currentIndex==3)
				{
					openApp.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							goHome();
							setGuided();
						}
					});
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void initDots() {
		dots = new ImageView[listViews.size()];
		// 循环取得小点图片
		for (int i = 0; i < listViews.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}
	
	private void setCurrentDot(int position) {
		if (position < 0 || position > listViews.size() - 1
				|| currentIndex == position) {
			return;
		}
		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = position;
		if(position==3)
		{
		ll.setVisibility(View.GONE);
		}else {
			ll.setVisibility(View.VISIBLE);
		}
	}

	private void goHome() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	private void setGuided() {
		this.getApplicationContext().setFirstIn();
	}
	
}
