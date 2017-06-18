package com.kdkj.koudailicai.lib.http;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.util.KdlcDialog;

public class BaseHttpErrorListener implements ErrorListener {
	private Context mContext;
	private HttpErrorInterface errInterface = null;
	private AlertDialog dialog = null;
	private PullToRefreshBase mRefreshView = null;
	private boolean netErrShowAlert = false;
	private OnClickListener clickListener;

	public BaseHttpErrorListener(Context context) {
		this.mContext = context;
	}
	
	public BaseHttpErrorListener(Context context, HttpErrorInterface errInterface) {
		this.mContext = context;
		this.errInterface = errInterface;
	}

	public BaseHttpErrorListener(Context context, AlertDialog dialog) {
		this.mContext = context;
		this.dialog = dialog;
	}
	
	public BaseHttpErrorListener(Context context, HttpErrorInterface errInterface, AlertDialog dialog) {
		this.mContext = context;
		this.errInterface = errInterface;
		this.dialog = dialog;
	}
	
	public HttpErrorInterface getErrInterface() {
		return errInterface;
	}

	public void setErrInterface(HttpErrorInterface errInterface) {
		this.errInterface = errInterface;
	}

	public AlertDialog getDialog() {
		return dialog;
	}

	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}

	public PullToRefreshBase getRefreshView() {
		return mRefreshView;
	}

	public void setRefreshView(PullToRefreshBase mRefreshView) {
		
		this.mRefreshView = mRefreshView;
	}
	
	public OnClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
	
	public boolean isNetErrShowAlert() {
		return netErrShowAlert;
	}

	public void setNetErrShowAlert(boolean netErrShowAlert) {
		this.netErrShowAlert = netErrShowAlert;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		if(dialog != null) {
			dialog.cancel();
		}
		
		if(mRefreshView != null && mRefreshView.isRefreshing()) {
			mRefreshView.onRefreshComplete();
			mRefreshView.setCurrentMode(Mode.PULL_FROM_START);
		}
		
		if(NoConnectionError.class.isInstance(error)) {
			if(errInterface != null) {
				errInterface.showNoNetwork();
			} else {
				if(netErrShowAlert)
					KdlcDialog.showAlertDialog(mContext, clickListener, "网络未连接，请检查网络设置后再试");
				else
					KdlcDialog.showNetErrToast("网络未连接，请检查网络设置后再试");
			}
		
		} else {
			if(errInterface != null) {
				errInterface.showErrReq();
			} else {
				if(netErrShowAlert)
					KdlcDialog.showAlertDialog(mContext, clickListener, "网络出错，请稍后再试");
				else
					KdlcDialog.showNetErrToast("网络出错，请稍后再试");
			}
		}
		error.printStackTrace();
	}
	
	public interface HttpErrorInterface {
		public void showNoNetwork();
		public void showErrReq();
	}
	
}