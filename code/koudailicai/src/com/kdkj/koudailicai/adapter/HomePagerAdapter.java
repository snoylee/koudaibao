package com.kdkj.koudailicai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.GroupAdapter.ViewHolder;
import com.kdkj.koudailicai.adapter.HomePagerAdapter.PlaceholderFragment.HomePageViewHolder;
import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.kdkj.koudailicai.view.invest.InvestChargeActivity;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomePagerAdapter extends FragmentPagerAdapter {
	public List<HomeProductInfo> dataList;
	private FragmentManager fm;
	private static boolean isAnimationOver = false;// 是否完成动画
	private ArrayList<PlaceholderFragment> fragmentList=new ArrayList<PlaceholderFragment>();

	public HomePagerAdapter(FragmentManager fm, List<HomeProductInfo> dataList) {
		super(fm);
		this.fm = fm;
		this.dataList = dataList;
		for(int i=0;i<dataList.size();i++){
			fragmentList.add(PlaceholderFragment.newInstance(i, dataList.get(i)));
		}
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return dataList.get(position).getName();
	}

	public static class PlaceholderFragment extends Fragment {
		
		// 尺寸占比例
		private static final double titleMargin = 0.09;// 标题距离顶部
		private static final double circleMargin = 0.03; // 圆环距离标题
		private static final double circleDiameter = 0.36;// 圆环直径
		private static final double tipMargin = 0.03;// tip距离圆环
		private static final double investBtnMargin = 0.1;// 立即赚钱按钮距tip
		private static final double safeMargin = 0.02;// 安全保证距按钮
		private static final double investBtnWidth = 0.8;// 按钮占屏幕宽度比

		private TranslateAnimation arrowUp, arrowDown;
		int animationStage = 1;// 动画到达的阶段
		private HomePageViewHolder viewHolder;
		private AnimationListener animationListener = new AnimationListener() {

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
				// TODO Auto-generated method stub
				if (animationStage == 3
						&& !"scrolled".equals(KDLCApplication.app
								.getSessionVal(G.HOMEPAGE_SCROLLED))) {
					startArrowAnimation();
				}
				animationStage++;
				Message msg = handler.obtainMessage();
				msg.sendToTarget();
			}
		};
		public Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (animationStage) {
				case 2:					
					viewHolder.circleView.startAnimation(animation2);
					animation2.setAnimationListener(animationListener);
					viewHolder.tipView.startAnimation(animation2);
					break;
				case 3:
					isAnimationOver = true;
					KDLCApplication.app.getSession().set("homeAnimationShowed", "1");
					viewHolder.investBtn.startAnimation(animation3);
					animation3.setAnimationListener(animationListener);
					viewHolder.safeTip.startAnimation(animation3);
					break;
				}
				if(msg.arg1==100){
					LogUtil.info("隐藏arrow");
					//先撤销动画，否则设置无效
					if(null!=viewHolder&&viewHolder.arrow!=null){
						viewHolder.arrow.clearAnimation();
						viewHolder.arrow.setVisibility(View.GONE);	
						KDLCApplication.app.setSessionVal(G.HOMEPAGE_SCROLLED,
								"scrolled");
					}
					
				}
								
			}

		};

		Animation animation1 = AnimationUtils.loadAnimation(
				KDLCApplication.app, R.anim.fade_in);
		Animation animation2 = AnimationUtils.loadAnimation(
				KDLCApplication.app, R.anim.fade_in);
		Animation animation3 = AnimationUtils.loadAnimation(
				KDLCApplication.app, R.anim.fade_in);

		public static PlaceholderFragment newInstance(int position,
				HomeProductInfo productInfo) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putParcelable(G.HOME_PRODUCT_INFO_SER_KEY, productInfo);
			args.putInt("position", position);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			HomeProductInfo productInfo = (HomeProductInfo) getArguments()
					.getParcelable(G.HOME_PRODUCT_INFO_SER_KEY);
			int position = getArguments().getInt("position");
			View pageView = inflater.inflate(R.layout.home_first_page,
					container, false);
			HomePageViewHolder viewHolder = getViewHolder(pageView);
			initSize(viewHolder);
			setColor(viewHolder, position);
			if("0".equals(KDLCApplication.app.getSession().get("homeAnimationShowed"))){
				setAnimations(viewHolder, position);
			}
			setContents(viewHolder, productInfo);
			if ("scrolled".equals(KDLCApplication.app
					.getSessionVal(G.HOMEPAGE_SCROLLED))) {
				viewHolder.arrow.setVisibility(View.INVISIBLE);
			}
			return pageView;
		}		

		private HomePageViewHolder getViewHolder(View pageView) {
			HomePageViewHolder viewHolder = new HomePageViewHolder();
			viewHolder.pageInfoView = (RelativeLayout) pageView
					.findViewById(R.id.pageInfoView);
			viewHolder.productName = (TextView) pageView
					.findViewById(R.id.productName);
			// viewHoler.fakeTitle = (TextView)
			// pageView.findViewById(R.id.fakeTitle);

			viewHolder.aprtext_rl = (RelativeLayout) pageView
					.findViewById(R.id.aprtext_rl);
			viewHolder.aprView = (RelativeLayout) pageView
					.findViewById(R.id.aprView);
			viewHolder.aprText = (TextView) pageView.findViewById(R.id.aprText);
			viewHolder.circleView = (ImageView) pageView
					.findViewById(R.id.circleView);
			viewHolder.aprText = (TextView) pageView.findViewById(R.id.aprText);
			viewHolder.accountLimitTip = (TextView) pageView
					.findViewById(R.id.accountLimitTip);
			viewHolder.timeLimitTip = (TextView) pageView
					.findViewById(R.id.timeLimitTip);
			viewHolder.progressTip = (TextView) pageView
					.findViewById(R.id.progressTip);
			viewHolder.safeText = (TextView) pageView
					.findViewById(R.id.safeText);
			viewHolder.tipView = (LinearLayout) pageView
					.findViewById(R.id.tipView);
			viewHolder.safeImage = (ImageView) pageView
					.findViewById(R.id.safeImage);
			viewHolder.investBtn = (TextView) pageView
					.findViewById(R.id.investBtn);
			viewHolder.safeTip = (LinearLayout) pageView
					.findViewById(R.id.safeTip);
			viewHolder.arrow = (ImageView) pageView
					.findViewById(R.id.arrow_homepage);
			return viewHolder;
		}

		private void initSize(HomePageViewHolder viewHolder) {
			int screenHeight = Integer.parseInt(KDLCApplication.app
					.getSession().get("screenHeight"));
			int screenWidth = Integer.parseInt(KDLCApplication.app.getSession()
					.get("screenWidth"));
			int bottomMenuHeight = (int) (screenHeight * 0.09);
			// 初始化标题位置
			RelativeLayout.LayoutParams titleViewParam = (RelativeLayout.LayoutParams) viewHolder.productName
					.getLayoutParams();
			titleViewParam.topMargin = (int) (screenHeight * titleMargin);
			viewHolder.productName.setLayoutParams(titleViewParam);
			if(screenWidth <= 480) {
				viewHolder.aprText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 52);
			} else {
				viewHolder.aprText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
			}
			// 初始化年利率
			RelativeLayout.LayoutParams aprViewParam = (RelativeLayout.LayoutParams) viewHolder.aprView
					.getLayoutParams();
			aprViewParam.topMargin = (int) (screenHeight * circleMargin);
			viewHolder.aprView.setLayoutParams(aprViewParam);
			// 初始化圆环
			RelativeLayout.LayoutParams circleParam = (RelativeLayout.LayoutParams) viewHolder.circleView
					.getLayoutParams();
			circleParam.height = (int) (screenHeight * circleDiameter);
			circleParam.width = (int) (screenHeight * circleDiameter);
			viewHolder.circleView.setLayoutParams(circleParam);
			// 初始化tip
			RelativeLayout.LayoutParams tipParam = (RelativeLayout.LayoutParams) viewHolder.tipView
					.getLayoutParams();
			tipParam.topMargin = (int) (screenHeight * tipMargin);
			viewHolder.tipView.setLayoutParams(tipParam);
			// 初始化按钮
			RelativeLayout.LayoutParams btnParam = (RelativeLayout.LayoutParams) viewHolder.investBtn
					.getLayoutParams();
			btnParam.topMargin = (int) (screenHeight * investBtnMargin);
			viewHolder.investBtn.setLayoutParams(btnParam);
			// 初始化安全保障
			RelativeLayout.LayoutParams safeParam = (RelativeLayout.LayoutParams) viewHolder.safeTip
					.getLayoutParams();
			safeParam.topMargin = (int) (screenHeight * safeMargin);
			viewHolder.safeTip.setLayoutParams(safeParam);
		}

		private void setColor(HomePageViewHolder viewHolder, int position) {
			if (position == 1) {
				viewHolder.pageInfoView
						.setBackgroundResource(R.drawable.homeback2);
				viewHolder.safeImage.setBackgroundResource(R.drawable.baoblue);
				viewHolder.circleView
						.setBackgroundResource(R.drawable.second_circle);
				viewHolder.safeText.setTextColor(getResources().getColor(
						R.color.home_second_safe_text_color));
			} else if (position == 2) {
				viewHolder.pageInfoView
						.setBackgroundResource(R.drawable.homeback3);
				viewHolder.safeImage
						.setBackgroundResource(R.drawable.baoyellow);
				viewHolder.circleView
						.setBackgroundResource(R.drawable.third_circle);
				viewHolder.safeText.setTextColor(getResources().getColor(
						R.color.home_third_safe_text_color));
			} else {
				viewHolder.pageInfoView
						.setBackgroundResource(R.drawable.homeback1);
				viewHolder.safeImage.setBackgroundResource(R.drawable.baored);
				viewHolder.circleView
						.setBackgroundResource(R.drawable.first_circle);
				viewHolder.safeText.setTextColor(getResources().getColor(
						R.color.home_first_safe_text_color));
			}
		}

		private void setContents(HomePageViewHolder viewHolder,
				final HomeProductInfo productInfo) {
			viewHolder.productName.setText(productInfo.getName());
			viewHolder.aprText.setText(productInfo.getApr());
			viewHolder.accountLimitTip.setText(Tool.toIntAccount(productInfo
					.getMinInvestMoney()) + "元起购");
			viewHolder.timeLimitTip
					.setText(productInfo.getProductId() == 0 ? productInfo
							.getWords() : productInfo.getWords() + "期限");
			viewHolder.progressTip.setText(productInfo.getSuccessPercent()
					+ "%完成");
			if (KDLCApplication.app.isGlobalConfCompleted()) {
				viewHolder.safeText.setText(KDLCApplication.app
						.getConfVal(G.GCK_VAL_WARRANT_WORD));
			} else {
				viewHolder.safeText.setText(G.VAL_WARRANT_WORD);
			}
			viewHolder.investBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),
							InvestAccountActivity.class);
					Bundle bundle = new Bundle();
					ProductBaseInfo productBaseInfo = new ProductBaseInfo();
					productBaseInfo.getFromHomeInfo(productInfo);
					bundle.putParcelable(G.PRODUCT_INFO_SER_KEY,
							productBaseInfo);
					intent.putExtras(bundle);
					((MainActivity) getActivity())
							.startActivityNeedLogin(intent);
				}
			});
		}

		// 启动箭头动画，先上后下
		private void twoStageAnimation() {			
			arrowUp.setAnimationListener(new AnimationListener() {

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
					// TODO Auto-generated method stub
					viewHolder.arrow.startAnimation(arrowDown);
				}
			});
			viewHolder.arrow.startAnimation(arrowUp);			
			
		}

		private void startArrowAnimation() {
			viewHolder.arrow.setVisibility(View.VISIBLE);

			arrowUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					-0.5f);
			arrowUp.setDuration(500);
			arrowUp.setInterpolator(new DecelerateInterpolator());
			arrowUp.setFillAfter(true);

			arrowDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF,
					0.0f);
			arrowDown.setDuration(500);
			arrowDown.setInterpolator(new AccelerateInterpolator());
			arrowDown.setFillAfter(true);
			// arrowUp.setFillBefore(true);
			// arrowUp.setRepeatCount(Animation.INFINITE);
			// arrowUp.setRepeatMode(Animation.RESTART);
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(viewHolder.arrow.getVisibility()==View.VISIBLE){
						twoStageAnimation();
					}
					else
						return;
					handler.postDelayed(this, 5 * 1000);
				}
			};
			handler.postDelayed(runnable, 5 * 1000);// 每隔五秒执行一次
		}

		public class HomePageViewHolder {
			private RelativeLayout pageInfoView;
			private TextView productName;
			private TextView fakeTitle;
			private RelativeLayout aprView;
			private RelativeLayout aprtext_rl;
			private TextView aprText;
			private ImageView circleView;
			private TextView accountLimitTip;
			private TextView timeLimitTip;
			private TextView progressTip;
			private LinearLayout tipView;
			private TextView investBtn;
			private LinearLayout safeTip;
			private ImageView safeImage;
			private TextView safeText;
			private ImageView arrow;

		}

		/**
		 * 启动动画，第一阶段
		 * 
		 * @param viewHoler
		 * @param positon
		 */
		private void setAnimations(HomePageViewHolder viewHoler, int positon) {
			
			if (positon == 0 && !isAnimationOver) {
				viewHolder = viewHoler;
				viewHolder.safeTip.setVisibility(View.INVISIBLE);
				viewHolder.investBtn.setVisibility(View.INVISIBLE);
				viewHolder.tipView.setVisibility(View.INVISIBLE);
				// viewHolder.aprView.setVisibility(View.INVISIBLE);
				viewHolder.aprtext_rl.setVisibility(View.INVISIBLE);
				viewHolder.circleView.setVisibility(View.INVISIBLE);
				viewHolder.productName.startAnimation(animation1);
				animation1.setAnimationListener(animationListener);
				viewHolder.aprtext_rl.startAnimation(animation1);
			}

		}
	}

	/*
	 * @Override public float getPageWidth(int position) { // TODO
	 * Auto-generated method stub if(position==2){ return 1f; } else return
	 * 0.91f;
	 * 
	 * //return super.getPageWidth(position); }
	 */

}
