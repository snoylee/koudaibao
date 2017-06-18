package com.kdkj.koudailicai.view.selfcenter.profit;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.GsonHelper;
import com.kdkj.koudailicai.domain.ProfitDetail;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.accountremain.AccountRemainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.*;

/**
 * 单个理财产品的收益详情
 * 
 * @author liaoheng
 * @creation 2014-12-11
 * 
 */
public class ProfitDetailActivity extends BaseActivity implements HttpErrorInterface {
	private TitleView titleview;
	private String profitId;
	private String url = "";
	private ViewHolder viewHolder = new ViewHolder();
	private PullToRefreshScrollView mPullRefreshScrollView;
	private RelativeLayout rl_root;// 显示数据
	private Handler mHandler = new Handler();
	private ProfitDetail detail;
	
	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income_details);
		initTitle();
		initView();
		getContents();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(mPullRefreshScrollView);
		} else {
			createFlag = false;
		}
	}
	
	private void initTitle() {
		titleview = (TitleView) findViewById(R.id.profittitle);
		titleview.setTitle(R.string.profitdetail);
		titleview.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ProfitDetailActivity.this.finish();
			}
		});
		titleview.setLeftImageButton(R.drawable.back);
		titleview.setLeftTextButton("返回");
	}
	
	private void initView() {
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.profit_detail_scrillview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				errorListener.setRefreshView(mPullRefreshScrollView);
				errorListener.setErrInterface(detail == null ? ProfitDetailActivity.this : null);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						sendHttpGet(url, getDetailListener);// 发起http请求
					}
				}, G.AUTO_LOAD_DELAYED);
			}
		});
		viewHolder.initView(this);
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRefreshView();
				mPullRefreshScrollView.setLoadRefreshing();
			}
		});
	}
	
	private void getContents() {
		profitId = getIntent().getExtras().getString("profitId");
		url = parseUrl(url) + "?id=" + profitId;// 加入参数
		mHandler.postDelayed(new Runnable() {
			public void run() {
				mPullRefreshScrollView.setLoadRefreshing();
			}
		}, 500);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 处理请求返回的数据
	 */
	private Listener<JSONObject> getDetailListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject arg0) {
			try {
				if (arg0.getInt("code") == 0) {
					rl_root.setVisibility(View.VISIBLE);
					JSONObject data = arg0.getJSONObject("data");
					detail = GsonHelper.fromJson(data, ProfitDetail.class);
					viewHolder.setView(detail);
				} else if (arg0.getInt("code") == -2) {
					dialog = KdlcDialog.showLoginDialog(ProfitDetailActivity.this);
				} else {
					if(detail == null) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(ProfitDetailActivity.this, arg0.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(detail == null) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			mPullRefreshScrollView.onRefreshComplete();
		}
	};

	class ViewHolder {
		public TextView product_number;// 产品名
		public TextView principal_number;// 投资本金
		public TextView yields_number;// 年化收益
		public TextView totle_yields_number;// 总收益
		public TextView limit_title;// 期限title
		public TextView limit_number;// 期限

		public RelativeLayout rl_time;
		public TextView buy_number;
		public TextView rates_number;
		public TextView reimbursement_number;

		public RelativeLayout rl_day;
		public TextView buy_day_number;
		public TextView rates_day_number;
		public TextView reimbursement_day_number;

		/**
		 * 初始化控件
		 * 
		 * @param parentView
		 */
		public void initView(Activity parentView) {
			this.product_number = (TextView) parentView
					.findViewById(R.id.product_number);
			this.principal_number = (TextView) parentView
					.findViewById(R.id.principal_number);
			this.yields_number = (TextView) parentView
					.findViewById(R.id.yields_number);
			this.totle_yields_number = (TextView) parentView
					.findViewById(R.id.totle_yields_number);
			this.limit_title = (TextView) parentView
					.findViewById(R.id.limit_title);
			this.limit_number = (TextView) parentView
					.findViewById(R.id.limit_number);

			this.rl_time = (RelativeLayout) parentView
					.findViewById(R.id.rl_time);
			this.buy_number = (TextView) parentView
					.findViewById(R.id.buy_number);
			this.rates_number = (TextView) parentView
					.findViewById(R.id.rates_number);
			this.reimbursement_number = (TextView) parentView
					.findViewById(R.id.reimbursement_number);

			this.rl_day = (RelativeLayout) parentView.findViewById(R.id.rl_day);
			this.buy_day_number = (TextView) parentView
					.findViewById(R.id.buy_day_number);
			this.rates_day_number = (TextView) parentView
					.findViewById(R.id.rates_day_number);
			this.reimbursement_day_number = (TextView) parentView
					.findViewById(R.id.reimbursement_day_number);
			this.setView(new ProfitDetail());
		}

		public void setView(ProfitDetail detail) {
			this.product_number.setText(Tool.isBlank(detail.getProject_name()) ? "" : detail.getProject_name());
			this.principal_number.setText(
					Tool.isBlank(detail.getDuein_capital()) 
					? "" : Tool.toDeciDouble(detail.getDuein_capital()));
			this.principal_number.append("元");
			this.yields_number.setText(Tool.isBlank(detail.getApr()) ? "" : detail.getApr() + "%");
			String realProfit = (Tool.isBlank(detail.getDuein_money()) || Tool.isBlank(detail.getDuein_capital())) ? 
								 "0.00" : Tool.toDeciDouble(""+(Long.parseLong(detail.getDuein_money()) - Long.parseLong(detail.getDuein_capital())));
			this.totle_yields_number.setText(realProfit);
			this.totle_yields_number.append("元");
			
			if ("0".equals(profitId)) {
				// 口袋宝没有购买时间
				
				this.limit_title.setText("计算公式");
				this.limit_number.setText(null == detail.getExpression() ? ""
						: detail.getExpression());

				this.rl_time.setVisibility(View.GONE);
				this.rl_day.setVisibility(View.GONE);

			} else {
				this.limit_title.setText("项目期限");
				this.rl_time.setVisibility(View.VISIBLE);
				this.rl_day.setVisibility(View.VISIBLE);

				this.limit_number.setText(null == detail.getPeriod_label() ? ""
						: detail.getPeriod_label());

				this.buy_number.setText(null == detail.getCreated_at() ? ""
						: detail.getCreated_at());
				this.rates_number.setText(null == detail
						.getInterest_start_date() ? "" : detail
						.getInterest_start_date());
				this.reimbursement_number.setText(null == detail
						.getLast_repay_date() ? "" : detail
						.getLast_repay_date());

				this.buy_day_number
						.setText(null == detail.getInterest_date() ? ""
								: detail.getInterest_date());
				this.rates_day_number
						.setText(null == detail.getRepay_date() ? "" : detail
								.getRepay_date());
				this.reimbursement_day_number.setText(null == detail
						.getExpression() ? "" : detail.getExpression());

			}

		}
	}

	private String parseUrl(String url) {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			url = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PROFIT_DETAIL);
		} else {
			url = G.URL_GET_PROFIT_DETAIL;
		}
		return url;
	}
	
	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
		showNetView(false);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		showNetView(true);
	}
	
	private void showNetView(boolean err) {
		mPullRefreshScrollView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}

	private void showRefreshView() {
		mPullRefreshScrollView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
}
