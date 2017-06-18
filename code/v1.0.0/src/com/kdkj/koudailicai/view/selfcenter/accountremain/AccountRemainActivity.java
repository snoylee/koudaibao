package com.kdkj.koudailicai.view.selfcenter.accountremain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.lib.ui.RemainTitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.AddBankCardActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferPasswordActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.TotalProfitActivity;
import com.kdkj.koudailicai.adapter.GroupAdapter;
public class AccountRemainActivity extends BaseActivity implements HttpErrorInterface {
	private RemainTitleView accountRemainTitle;
	private NumChangeTextView usMoney;
	private NumChangeTextView withMoney;
	private String url;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private NumChangeTextView remainderMoney;
	private String LOG_TAG = AccountRemainActivity.class.getName();
	private int screenHeight;
	private double accountRemainHeight = 0.188;
	private double listHeight = 0.079;
	private RelativeLayout accountRemainlayout;
	private RelativeLayout accountRemain;
	private RelativeLayout usAbleMoney;
	private RelativeLayout withDrawingMoney;
	private Handler mHandler = new Handler();
	private String remainMoney;
	private PopupWindow popupWindow;
	private View view;
	private ListView lvGroup;
	private List<String> groups;
	private int num = 0;
	private boolean remainShow = false;
	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_remain);
		parseUrl();
		findViews();
		initSize();
//		getContents();
		initTitle();
		registerBoradcastReceiver();
		showWindow(accountRemainTitle);

	}

	private void findViews() {
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.accountremainrefreshroot);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
					@Override
					public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
						errorListener.setRefreshView(mPullRefreshScrollView);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								getKdbRemainInfo();
							}
						}, 500);
					}
		});
		accountRemainlayout = (RelativeLayout) findViewById(R.id.accountRemainlayout);
		remainderMoney = (NumChangeTextView) findViewById(R.id.remaindermoney);
		usMoney = (NumChangeTextView) findViewById(R.id.usmoney);
		withMoney = (NumChangeTextView) findViewById(R.id.withmoney);
		accountRemain = (RelativeLayout) findViewById(R.id.accountremain);
		usAbleMoney = (RelativeLayout) findViewById(R.id.usablemoney);
		withDrawingMoney = (RelativeLayout) findViewById(R.id.withdrawingmoney);
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				mPullRefreshScrollView.setLoadRefreshing();
			}
		});
		
		dialog = KdlcDialog.showProgressDialog(AccountRemainActivity.this, "正在加载...");
		getKdbRemainInfo();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_REAMAIN);
		} else {
			url = G.URL_GET_ACCOUNT_REMAIN;
		}
	}

	private void getContents() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				mPullRefreshScrollView.setLoadRefreshing();
			}
		}, 500);
	}

	private void initTitle() {
		accountRemainTitle = (RemainTitleView) findViewById(R.id.accountremaintitle);
		accountRemainTitle.setTitle("账户余额");
		accountRemainTitle.showMidButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
						accountRemainTitle.Aarrw(R.drawable.arrow_down);
					} else {
						popupWindow.showAsDropDown(accountRemainTitle, 0, 0);
						accountRemainTitle.Aarrw(R.drawable.arrow_up);
					}
				} else {
					showWindow(accountRemainTitle);
					accountRemainTitle.Aarrw(R.drawable.arrow_down);
					popupWindow.showAsDropDown(accountRemainTitle, 0, 0);
				}
				num++;
			}
		});
		
		accountRemainTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// AccountRemainActivity.this.finish();
				accountRemainTitle.Aarrw(R.drawable.arrow_down);
				AccountRemainActivity.this.finish();
			}
		});
		accountRemainTitle.setLeftImageButton(R.drawable.back);
		accountRemainTitle.setLeftTextButton("返回");
	}

	private void Dialog() {
		dialog = KdlcDialog.showConfirmDialog(AccountRemainActivity.this, true, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(AccountRemainActivity.this,
												   ConfirmIdentityActivity.class);
						startActivity(intent);
					}
				}, "您还未通过实名认证，请先进行实名认证");
	}

	private void cardDialog() {
		dialog = KdlcDialog.showConfirmDialog(AccountRemainActivity.this,
				true, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(AccountRemainActivity.this, AddBankCardActivity.class);
						startActivity(intent);
					}
				}, "您还未绑定银行卡，请先绑定银行卡");
	}

	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams) accountRemain
				.getLayoutParams();
		lastDayProfitsLayoutParams.height = (int) (screenHeight * accountRemainHeight);
		accountRemain.setLayoutParams(lastDayProfitsLayoutParams);
		RelativeLayout.LayoutParams usablemoneyLayoutParams = (RelativeLayout.LayoutParams) usAbleMoney
				.getLayoutParams();
		usablemoneyLayoutParams.height = (int) (screenHeight * listHeight);
		usAbleMoney.setLayoutParams(usablemoneyLayoutParams);
		RelativeLayout.LayoutParams withdrawingmoneyLayoutParams = (RelativeLayout.LayoutParams) withDrawingMoney
				.getLayoutParams();
		withdrawingmoneyLayoutParams.height = (int) (screenHeight * listHeight);
		withDrawingMoney.setLayoutParams(withdrawingmoneyLayoutParams);

	}

	private void getKdbRemainInfo() {
		errorListener.setErrInterface(remainShow ? null : AccountRemainActivity.this);
		sendHttpGet(AccountRemainActivity.this.url, getKdbInfoListener);
	}

	private Listener<JSONObject> getKdbInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "response:" + response.toString());
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					JSONObject data = response.getJSONObject("data");
					remainderMoney.setNumText(Tool.toDeciDouble(data.getString("money")));
					usMoney.setNumText(Tool.toDeciDouble(data.getString("usable_money")));
					withMoney.setNumText(Tool.toDeciDouble(data.getString("withdrawing_money")));
					remainMoney = Tool.toDeciDouble(data.getString("usable_money"));
				      if(!Tool.isBlank(remainMoney) && remainMoney.equals("0.00")){
				    	  accountRemainTitle.showRightButton(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(AccountRemainActivity.this, WithdrawListActivity.class);
									startActivity(intent);
								}
				    	  });
				    	  accountRemainTitle.setRightTextButton("提现记录");
				      } else {
				    	  accountRemainTitle.showRightButton(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (!KDLCApplication.app.checkRealStatus()) {
										Dialog();
									} else if (!KDLCApplication.app.checkCardStatus()) {
										cardDialog();
									} else {
										Intent intent = new Intent(AccountRemainActivity.this,WithdrawActivity.class);
										intent.putExtra("remainmoney", remainMoney);
										startActivity(intent);
									}
								}
				    	  });
				    	  accountRemainTitle.setRightTextButton("提现");
				      }
				      accountRemainlayout.setVisibility(View.VISIBLE);
				      remainShow = true;
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(AccountRemainActivity.this);
        		}  else {
        			dialog =KdlcDialog.showInformDialog(AccountRemainActivity.this ,response.getString("message"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
			mPullRefreshScrollView.onRefreshComplete();
		}
	};

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

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(G.WITH_DRAW_MESSAGE)) {
				getContents();
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(G.WITH_DRAW_MESSAGE);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void showWindow(View parent) {
		LayoutInflater layoutInflater = getLayoutInflater();
		view = layoutInflater.inflate(R.layout.group_list, null);
		TextView lvView = (TextView) view.findViewById(R.id.lvview);
		lvView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				accountRemainTitle.Aarrw(R.drawable.arrow_down);
			}
		});
		if (popupWindow == null) {
			lvGroup = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<String>();
			groups.add("收支明细");
			groups.add("支出明细");
			groups.add("收入明细");
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lvGroup.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setOutsideTouchable(true);
		// // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				LogUtil.info("set arrow down");
				accountRemainTitle.Aarrw(R.drawable.arrow_down);
			}
		});
		lvGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				if (groups.get(position).equals("收支明细") || position == 0) {
					accountRemainTitle.Aarrw(R.drawable.arrow_down);
					Intent intent = new Intent();
					intent.setClass(AccountRemainActivity.this,
							PaymentDetailsActivity.class);
					startActivity(intent);

				} else if (groups.get(position).equals("支出明细") || position == 1) {
					accountRemainTitle.Aarrw(R.drawable.arrow_down);
					Intent intent = new Intent();
					intent.setClass(AccountRemainActivity.this,
							SpendingDetailsActivity.class);
					startActivity(intent);

				} else {
					accountRemainTitle.Aarrw(R.drawable.arrow_down);
					Intent intent = new Intent();
					intent.setClass(AccountRemainActivity.this,
							IncomeDetailsActivity.class);
					startActivity(intent);

				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (num > 0) {
				accountRemainTitle.Aarrw(R.drawable.arrow_up);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				AccountRemainActivity.this.finish();
			} else {
				AccountRemainActivity.this.finish();
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
	

	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
		dialog.cancel();
		showNetView(false);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		dialog.cancel();
		showNetView(true);
	}
	
	private void showNetView(boolean err) {
		mPullRefreshScrollView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}

	private void showListView() {
		mPullRefreshScrollView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
}
