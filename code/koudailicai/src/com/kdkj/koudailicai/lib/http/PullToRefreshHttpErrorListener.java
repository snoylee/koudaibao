package com.kdkj.koudailicai.lib.http;

import android.content.Context;

import com.android.volley.VolleyError;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;

public class PullToRefreshHttpErrorListener extends BaseHttpErrorListener {
	
	private PullToRefreshBase mRefreshView;
	
	public PullToRefreshHttpErrorListener(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
   
	public PullToRefreshHttpErrorListener(Context context, PullToRefreshBase refreshView) {
		super(context);
		this.mRefreshView = refreshView;
	}
	public PullToRefreshHttpErrorListener(Context context, PullToRefreshBase refreshView, HttpErrorInterface errInterface) {
		super(context, errInterface);
		this.mRefreshView = refreshView;
	}
	@Override
	public void onErrorResponse(VolleyError error) {
		super.onErrorResponse(error);
		if(mRefreshView != null && mRefreshView.isRefreshing()) {
			mRefreshView.onRefreshComplete();
			mRefreshView.setCurrentMode(Mode.PULL_FROM_START);
		}
	}
}