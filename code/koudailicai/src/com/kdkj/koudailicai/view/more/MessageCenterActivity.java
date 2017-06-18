package com.kdkj.koudailicai.view.more;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.NotificationListAdapter;
import com.kdkj.koudailicai.domain.HelpCenterInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.SplashActivity;
import com.kdkj.koudailicai.xgpush.XGNotification;

public class MessageCenterActivity extends BaseActivity implements HttpErrorInterface{
	private static final String LOG_TAG = "MessageCenterActivity";

	private TitleView messagetitle;
	private PullToRefreshListView lvNotification;
	private List<XGNotification> notificationItems = new ArrayList<XGNotification>();
	private NotificationListAdapter notificationListAdapter;
	private Handler mHandler = new Handler();
	private Context mContext;
	private AlertDialog alterDialog;
	private int activitiesNum = 0;
	private String fromActivity = "activity";
	private NotificationManager cancelNotificationManager;
	//网络出错页面
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		mContext = this;
	   
		init();
		inittitle();
		notificationItemsRefresh();
		// 消息中心显示，标记所有已读
		KDLCApplication.app.initMessageCenter("0");
		initPushMessage();
	}

	private void init() {
		messagetitle = (TitleView) findViewById(R.id.messagetitle);
		
		errNetLayout = (RelativeLayout) findViewById(R.id.errNetLayout);
		noDataLayout = (RelativeLayout) findViewById(R.id.noDataLayout);
		networkLoad = (TextView) findViewById(R.id.networkload);
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				lvNotification.setLoadRefreshing();
			}
		});
		
		lvNotification = (PullToRefreshListView) findViewById(R.id.lv_notification);		
		notificationItems.addAll((List<XGNotification>) KdlcDB.findAllByClass(XGNotification.class));
		if(notificationItems == null || notificationItems.size() == 0){
			showNoDataView();
		}else{
			showListView();
		}
		Collections.reverse(notificationItems);
		notificationListAdapter = new NotificationListAdapter(
				getApplicationContext(), R.layout.activity_message_center,
				notificationItems);
		lvNotification.setAdapter(notificationListAdapter);
	}

	private void inittitle() {
		messagetitle.setTitle(R.string.more_message_list);
		messagetitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				backClick();
			}
		});
		messagetitle.setLeftImageButton(R.drawable.back);
		messagetitle.setLeftTextButton("返回");

		messagetitle.setRightTextButton("清空");
		messagetitle.showRightButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (KdlcDB.findAllByClass(XGNotification.class) != null
						&& KdlcDB.findAllByClass(XGNotification.class).size() != 0){
					alterDialog = KdlcDialog.showConfirmDialog(mContext, true, clickListener, "是否清空所有消息？");
				}
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(alterDialog.isShowing()){
				alterDialog.dismiss();
			}
			notificationItems.clear();
			if(Tool.hasCacheData(XGNotification.class))
				KdlcDB.deleteAllByClass(XGNotification.class);
			notificationListAdapter.notifyDataSetChanged();
			showNoDataView();
		}
	};
	
	private void notificationItemsRefresh() {
		lvNotification.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// 下拉刷新，标记已读
				KDLCApplication.app.initMessageCenter("0");

				if (lvNotification != null) {
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							notificationItems = (List<XGNotification>) KdlcDB
									.findAllByClass(XGNotification.class);
							if(notificationItems != null &&notificationItems.size() !=0) {
								showListView();
								Collections.reverse(notificationItems);
								notificationListAdapter = new NotificationListAdapter(
										mContext, R.layout.activity_message_center,
										notificationItems);
								lvNotification.setAdapter(notificationListAdapter);
							}
							lvNotification.onRefreshComplete();
						}
					}, 500);
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(lvNotification);
		} else {
			createFlag = false;
		}
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
		lvNotification.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		lvNotification.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		lvNotification.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == keyCode) {
			backClick();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void backClick() {
		if (activitiesNum == 2 && fromActivity.contains("tencent")) {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(MessageCenterActivity.this, SplashActivity.class);
			startActivity(intent);
		}
		MessageCenterActivity.this.finish();
	}
	
	//初始化推送通知打开条件判断
	private void initPushMessage(){
		//获取activity个数
		ActivityManager am = (ActivityManager)mContext.getSystemService(ACTIVITY_SERVICE);
		List<RunningTaskInfo> appTask = am.getRunningTasks(1);
		if(appTask!=null&&appTask.size()!=0){
			fromActivity = appTask.get(0).baseActivity.getClassName();
			activitiesNum = appTask.get(0).numActivities;
		}
		//清空通知栏
//		cancelNotificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		cancelNotificationManager.cancelAll();
	}
}
