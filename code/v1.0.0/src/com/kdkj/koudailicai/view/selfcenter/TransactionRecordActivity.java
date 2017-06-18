package com.kdkj.koudailicai.view.selfcenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.TransListPagerAdapter;
import com.kdkj.koudailicai.adapter.TrasactionRecordKdbInfoAdapter;
import com.kdkj.koudailicai.adapter.TrasactionRecordOtherInfoAdapter;
import com.kdkj.koudailicai.domain.TrasactionRecordKdbInfo;
import com.kdkj.koudailicai.domain.TrasactionRecordOtherInfo;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawListActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;
public class TransactionRecordActivity extends BaseActivity {
	private String LOG_TAG = TransactionRecordActivity.class.getName();
	// views
	private TitleView mTitle;
	private TextView current_title;
	private TextView regular_intervals;
	private ImageView imageView;
	private PullToRefreshListView  listViewH,listViewD;
	private LayoutInflater lf;

	private ViewPager viewPager;
	private int currIndex = 0;// 当前页卡编号
	private List<View> pageViewList = new ArrayList<View>();
	private RelativeLayout hTrasaction,dTrasaction; 
	// sizes
	private int bmWidth;
	private int offSet;
	private int screenHeight;
	private int screenWidth;

	// dimens
	private double marginLeft = 0.049;
	private double marginTop= 0.025;
	private double marginRight= 0.097;

	// contents
	private String getAccountKdbTradesUrl;
	private String getAccountProjectTradesUrl;
	private TransListPagerAdapter pageAdapter;
	private List<TrasactionRecordKdbInfo> itemKdbList = new ArrayList<TrasactionRecordKdbInfo>();
	private List<TrasactionRecordOtherInfo> itemOtherList = new ArrayList<TrasactionRecordOtherInfo>();
	private TrasactionRecordKdbInfo trasactionH;
	private TrasactionRecordOtherInfo trasactionD;
	private RelativeLayout recordList;
	private RelativeLayout hList;
	private TrasactionRecordKdbInfoAdapter traRecHAdapter;
	private TrasactionRecordOtherInfoAdapter traRecDAdapter;
	private int initIndex = 0;
	
	private int hPage = 1;
	private int hPageSize = 15;
	private boolean hHasMore = true;
	private Mode hCurMode = Mode.PULL_FROM_START;
	
	private int dPage = 1;
	private int dPageSize = 15;
	private boolean dHasMore = true;
	private Mode dCurMode = Mode.PULL_FROM_START;
	
	private Handler mHandler = new Handler();

	//网络出错页面
	private RelativeLayout errNetLayoutD;
	private RelativeLayout noDataLayoutD;
	private TextView networkLoadD;
	private TextView networkTextD;

	//网络出错页面
	private RelativeLayout errNetLayoutH;
	private RelativeLayout noDataLayoutH;
	private TextView networkLoadH;
	private TextView networkTextH;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_record);
		parseUrl();
		initTitle();
		initCursor();
		initListView();
		initPager();
		initContents();
	}
	
	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			getAccountKdbTradesUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_ACCOUNT_KDB_TRADES);
			getAccountProjectTradesUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_ACCOUNT_PROJECT_TRADES);
		} else {
			getAccountKdbTradesUrl = G.URL_GET_ACCOUNT_KDB_TRADES;
			getAccountProjectTradesUrl = G.URL_GET_ACCOUNT_PROJECT_TRADES;
		}
	}
	
	private void initTitle() {
		mTitle = (TitleView) findViewById(R.id.title);
		mTitle.setTitle(R.string.trasaction_record_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//setContents();
				TransactionRecordActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
		
		current_title = (TextView) findViewById(R.id.current_title);
		regular_intervals = (TextView) findViewById(R.id.regular_intervals);
		current_title.setOnClickListener(new MyOnClickListener(0));
		regular_intervals.setOnClickListener(new MyOnClickListener(1));
	}
	
	private void initCursor() {
		imageView = (ImageView) findViewById(R.id.transctionrecord_cursor);
		bmWidth = BitmapFactory.decodeResource(getResources(), R.drawable.redbar).getWidth();// 获取图片宽度
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int screenW = mDisplayMetrics.widthPixels;
		offSet = (screenW / 2 - bmWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offSet, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	private void initListView() {

		
		LayoutInflater mInflater = LayoutInflater.from(TransactionRecordActivity.this);
		View viewH =  mInflater.inflate(R.layout.trasaction_record_list_h, null);
		recordList=(RelativeLayout)viewH.findViewById(R.id.recordlist);
		listViewH  = (PullToRefreshListView) viewH.findViewById(R.id.trasactionRecordlistview);	
		traRecHAdapter = new TrasactionRecordKdbInfoAdapter(TransactionRecordActivity.this, R.layout.tender_construction_item, itemKdbList);
		listViewH.setAdapter(traRecHAdapter);
		setHListViewRefresh();//设置刷新
		errNetLayoutH = (RelativeLayout) viewH.findViewById(R.id.errNetLayout);
		noDataLayoutH = (RelativeLayout) viewH.findViewById(R.id.noDataLayout);
		networkLoadH = (TextView) viewH.findViewById(R.id.networkload);
		networkTextH = (TextView) viewH.findViewById(R.id.networktext);
		networkLoadH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView(0);
				listViewH.setLoadRefreshing();
			}
		});
		pageViewList.add(recordList);
		
		View viewD = mInflater.inflate(R.layout.trasaction_record_list_d, null);
		hList=(RelativeLayout)viewD.findViewById(R.id.h_list);
		listViewD = (PullToRefreshListView)viewD.findViewById(R.id.trasactionRecordlistviewOne);
		traRecDAdapter = new TrasactionRecordOtherInfoAdapter(TransactionRecordActivity.this, R.layout.trasaction_record_list_d, itemOtherList);		
		listViewD.setAdapter(traRecDAdapter);
		setDListViewRefresh();
		errNetLayoutD = (RelativeLayout) viewD.findViewById(R.id.errNetLayout);
		noDataLayoutD = (RelativeLayout) viewD.findViewById(R.id.noDataLayout);
		networkLoadD = (TextView) viewD.findViewById(R.id.networkload);
		networkTextD = (TextView) viewD.findViewById(R.id.networktext);
		networkLoadD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView(1);
				listViewD.setLoadRefreshing();
			}
		});
		pageViewList.add(hList);
	}
	
	private void initPager() {
		pageAdapter = new TransListPagerAdapter(pageViewList);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(initIndex);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private void initContents() {
		Log.d(LOG_TAG, "initContents");
		mHandler.postDelayed(new Runnable() {
			public void run() {
				listViewH.setLoadRefreshing();
			}
		}, 500);
		
	}
	
	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		LinearLayout.LayoutParams currentTitleLayoutParams = (LinearLayout.LayoutParams)current_title.getLayoutParams();
		currentTitleLayoutParams.leftMargin = (int) (screenWidth*marginLeft);
		currentTitleLayoutParams.topMargin = (int) (screenHeight*marginTop);
		current_title.setLayoutParams(currentTitleLayoutParams);
		LinearLayout.LayoutParams regulaIntervalsLayoutParams = (LinearLayout.LayoutParams)regular_intervals.getLayoutParams();
		regulaIntervalsLayoutParams .topMargin = (int) (screenHeight*marginTop);
		regulaIntervalsLayoutParams .rightMargin = (int) (screenHeight*marginRight);
		regular_intervals.setLayoutParams(currentTitleLayoutParams);
	}
	
	//设置listViewH
	private void setHListViewRefresh(){
		//下拉刷新
		listViewH.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (hCurMode.equals(Mode.PULL_FROM_END)) {
					hPage += 1;
					sendHttpGet(getAccountKdbTradesUrl+"?page="+hPage+"&pageSize="+hPageSize, getListViewHListListener, new DealListErrorListener(0));
				} else {
					hPage = 1;
					hHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getAccountKdbTradesUrl+"?page="+hPage+"&pageSize="+hPageSize, getListViewHListListener, new DealListErrorListener(0));
						}
					}, 500);
				}
			}
		});
		
		//判断最后一列状态上拉刷新
		listViewH.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(hHasMore == true && !listViewH.isRefreshing()) {
					hCurMode = Mode.PULL_FROM_END;
					listViewH.setCurrentMode(hCurMode);
					listViewH.setLoadRefreshing();
				}
			}
			
		});
	}
	
	//设置listViewD
	private void setDListViewRefresh(){
		//下拉刷新
		listViewD.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (dCurMode.equals(Mode.PULL_FROM_END)) {
					dPage += 1;
					sendHttpGet(getAccountProjectTradesUrl+"?page="+dPage+"&pageSize="+dPageSize, getListViewDListListener, new DealListErrorListener(1));
				} else {
					dPage = 1;
					dHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getAccountProjectTradesUrl+"?page="+dPage+"&pageSize="+dPageSize, getListViewDListListener, new DealListErrorListener(1));
						}
					}, 500);
				}
			}
		});
		
		//判断最后一列状态上拉刷新
		listViewD.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(dHasMore == true && !listViewD.isRefreshing()) {
					Log.d(LOG_TAG, "litieshan tag");
					dCurMode = Mode.PULL_FROM_END;
					listViewD.setCurrentMode(dCurMode);
					listViewD.setLoadRefreshing();
				}
			}
			
		});
	}

	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);

		}
	}

	private Listener<JSONObject> getListViewHListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
					Log.d(LOG_TAG, "execute result:" + response);
					// setContents();

					if (response.getInt("code") == 0) {
						JSONArray list = response.getJSONArray("data");
						  if(hPage == 1){
							  //清空原始数据
							  itemKdbList.clear();
						  }
						  for (int i = 0; i < list.length(); i++) {
							  trasactionH = new TrasactionRecordKdbInfo();
							  trasactionH.setId(Integer.parseInt(list.getJSONObject(i).getString("id")));
							  trasactionH.setCreatdAt(list.getJSONObject(i).getString("created_at"));
							  trasactionH.setInvestMoney(list.getJSONObject(i).getString("invest_money"));
							  trasactionH.setStatus(list.getJSONObject(i).getString("status"));
							  itemKdbList.add(trasactionH);
							  Log.d(LOG_TAG,"execute ----list---- result:-----item---"+ itemKdbList);
							  Log.d(LOG_TAG,"execute ---trasactionH----- result:--------"+ trasactionH);
							  Log.d(LOG_TAG,"execute ---trasactionH----- result:--------"+ list.getJSONObject(i).getString("status"));
						  }
						  if (itemKdbList.size()<=0) {
								showNoDataView(0);
						  }
						  if(list.length() < hPageSize){
								hHasMore = false;
							}
							traRecHAdapter.notifyDataSetChanged();
					}else if(response.getInt("code") == -2){
		    			dialog = KdlcDialog.showLoginDialog(TransactionRecordActivity.this);
		    		} 
					else {
						if(itemKdbList.size() <= 0) {
							showNetView(0, true);
						}
						dialog =KdlcDialog.showInformDialog(TransactionRecordActivity.this ,response.getString("message"));
					}
					
					
			} catch (Exception e) {
				if(itemKdbList.size() <= 0) {
					showNetView(0, true);
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if(listViewH != null) {
			   listViewH.onRefreshComplete();
			   if (hCurMode.equals(Mode.PULL_FROM_END)) {
				   hCurMode = Mode.PULL_FROM_START;
				   listViewH.setCurrentMode(hCurMode);
			   }
			}
		}
	};
	
	private Listener<JSONObject> getListViewDListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// 返回HTML页面的内容
			try {
		
					// setContents();
					if (response.getInt("code") == 0) {
						JSONArray list = response.getJSONArray("data");
						if(dPage == 1){
							  //清空原始数据
							  itemOtherList.clear();
						}
						for (int i = 0; i < list.length(); i++) {
							trasactionD = new TrasactionRecordOtherInfo();
							trasactionD.setId(Integer.parseInt(list.getJSONObject(i).getString("id")));
							trasactionD.setCreatdAt(list.getJSONObject(i).getString("created_at"));
							trasactionD.setInvestMoney(list.getJSONObject(i).getString("invest_money"));
							trasactionD.setStatus(list.getJSONObject(i).getString("status"));
							trasactionD.setInvestName(list.getJSONObject(i).getString("name"));
							itemOtherList.add(trasactionD);
							Log.d(LOG_TAG,"execute ---trasactionD----- result:--------"+ trasactionD);
							Log.d(LOG_TAG,"execute ---itemOtherList----- result:--------"+ itemOtherList);
							Log.d(LOG_TAG,"execute ---trasactionH----- result:--------"+ list.getJSONObject(i).getString("name"));
						}
						if (itemOtherList.size()<=0) {
							 showNoDataView(1);
						}
						if( list.length() < dPageSize){
							dHasMore = false;
						}
						traRecDAdapter.notifyDataSetChanged();
					} else if(response.getInt("code") == -2){
		    			dialog = KdlcDialog.showLoginDialog(TransactionRecordActivity.this);
		    		} else {
						if(itemOtherList.size() <= 0) {
							showNetView(1, true);
						}
						dialog = KdlcDialog.showInformDialog(TransactionRecordActivity.this ,response.getString("message"));
					}
					
			} catch (Exception e) {
				if(itemKdbList.size() <= 0) {
					showNetView(1, true);
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if(listViewD != null) {
				listViewD.onRefreshComplete();
				if(dCurMode.equals(Mode.PULL_FROM_END)) {
					dCurMode = Mode.PULL_FROM_START;
					listViewD.setCurrentMode(dCurMode);
				}
		}
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offSet * 2 + bmWidth;// 页卡1 -> 页卡2 偏移量

		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			switch (arg0) {
			case 0:
				if(itemKdbList.size() <= 0 && Tool.isVisible(listViewH)) {
					mHandler.postDelayed(new Runnable() {
						public void run() {
							listViewH.setLoadRefreshing();
						}
					}, 500);
				}
				
				break;
			case 1:
				if(itemOtherList.size() <= 0 && Tool.isVisible(listViewD)) {
					mHandler.postDelayed(new Runnable() {
						public void run() {
							listViewD.setLoadRefreshing();
							
						}
					}, 500);             
				}
				break;
			default:
				break;
			}
			
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(listViewH);
			Tool.resetPullRefreshView(listViewD);
		} else {
			createFlag = false;
		}
	}
	
	private void showNetView(int flag, boolean err) {
		if(flag == 0) {
			if(itemKdbList.size() <= 0) {
				listViewH.setVisibility(View.GONE);
				noDataLayoutH.setVisibility(View.GONE);
				errNetLayoutH.setVisibility(View.VISIBLE);
				networkTextH.setText(err ? "网络出错" : "网络未连接");
			} else {
				Tool.resetPullRefreshView(listViewH);
			}
		} else {
			if(itemOtherList.size() <= 0) {
				listViewD.setVisibility(View.GONE);
				noDataLayoutD.setVisibility(View.GONE);
				errNetLayoutD.setVisibility(View.VISIBLE);
				networkTextD.setText(err ? "网络出错" : "网络未连接");
			} else {
				Tool.resetPullRefreshView(listViewD);
			}
		}
	}
	
	private void showNoDataView(int flag) {
		if(flag == 0) {
			listViewH.setVisibility(View.GONE);
			errNetLayoutH.setVisibility(View.GONE);
			noDataLayoutH.setVisibility(View.VISIBLE);
		} else {
			listViewD.setVisibility(View.GONE);
			errNetLayoutD.setVisibility(View.GONE);
			noDataLayoutD.setVisibility(View.VISIBLE);
		}
	}
	
	private void showListView(int flag) {
		if(flag == 0) {
			listViewH.setVisibility(View.VISIBLE);
			errNetLayoutH.setVisibility(View.GONE);
			noDataLayoutH.setVisibility(View.GONE);	
		} else {
			listViewD.setVisibility(View.VISIBLE);
			errNetLayoutD.setVisibility(View.GONE);
			noDataLayoutD.setVisibility(View.GONE);	
		}	
	}
	
	//首次加载使用错误监听
	private class DealListErrorListener implements ErrorListener {
		private int flag;
		public DealListErrorListener(int flag) {
			this.flag = flag;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			if(NoConnectionError.class.isInstance(error)) {
				showNetView(flag, false);
			} else {
				showNetView(flag, true);
			}
			if(flag == 0) {
				Tool.resetPullRefreshView(listViewH);
			} else {
				Tool.resetPullRefreshView(listViewD);
			}
		}
	}


}
