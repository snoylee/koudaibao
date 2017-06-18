package com.kdkj.koudailicai.util;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.fragment.BaseFragment;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KdlcDialog {
	private static Toast toast;
	
	public static void showToast(String msg) {
		showToast(msg, Gravity.CENTER, 0, -100);
	}
	
	public static void showBottomToast(String msg) {
		Log.d("sada", "showBottomToast");
		msg = Tool.isBlank(msg) ? "网络出错，请稍后再试" : msg;
		if (toast != null) {
			toast.cancel();
		}
		toast = new Toast(KDLCApplication.app);
		View toastRoot = LayoutInflater.from(KDLCApplication.app).inflate(R.layout.toast, null);  
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
        message.setMaxWidth((int)(screenWidth*0.7));
        message.setText(msg);
		toast.setView(toastRoot);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void showNetErrToast(String msg) {
		msg = Tool.isBlank(msg) ? "网络出错，请稍后再试" : msg;
		if (toast != null) {
			toast.cancel();
		}
		toast = new Toast(KDLCApplication.app);
		View toastRoot = LayoutInflater.from(KDLCApplication.app).inflate(R.layout.toast, null);  
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
        message.setMaxWidth((int)(screenWidth*0.7));
        message.setText(msg);
		toast.setView(toastRoot);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void showErrorToast(String msg) {
		showToast(msg, Gravity.CENTER, 0, -100, Toast.LENGTH_LONG);
	}
	
	public static void showToast(String msg, int gravity, int xOffset, int yOffset) {
		showToast(msg, gravity, xOffset, yOffset, Toast.LENGTH_SHORT);
	}
	
	public static void showToast(String msg, int gravity, int xOffset, int yOffset, int duration) {
		msg = Tool.isBlank(msg) ? "网络出错，请稍后再试" : msg;
		if (toast == null) {
			toast = new Toast(KDLCApplication.app);
		}
		View toastRoot = LayoutInflater.from(KDLCApplication.app).inflate(R.layout.toast, null);  
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
        message.setMaxWidth((int)(screenWidth*0.7));
        message.setText(msg);  
		toast.setView(toastRoot);
		toast.setDuration(duration);
		toast.setGravity(gravity, xOffset, yOffset);
		toast.show();
	}
	
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
	
	
	
	//通用加载提示窗
	public static AlertDialog showProgressDialog(Context context)
	{
		return showProgressDialog(context, "正在加载...");
	}
	
	//自定义内容加载提示窗
	public static AlertDialog showProgressDialog(Context context, String content)
	{
		if(Tool.isBlank(content)) {
			content = "正在加载...";
		}
		cancelToast();
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setCancelable(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_progress);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		TextView contentView = (TextView) window.findViewById(R.id.contentView);
		LinearLayout dialogView = (LinearLayout) window.findViewById(R.id.dialog_view);
		FrameLayout.LayoutParams dialogViewParams = (FrameLayout.LayoutParams) dialogView.getLayoutParams();
		dialogViewParams.height = (int) (screenWidth*0.25);
		dialogViewParams.width =  (int) (screenWidth*0.468);
		dialogView.setLayoutParams(dialogViewParams);
		contentView.setText(content);
		return dlg;
	}
	
	//自定义错误提示窗，点击确定关闭dialog
	public static AlertDialog showInformDialog(final BaseActivity context, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_alert);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		TextView btnRight = (TextView) window.findViewById(R.id.btnRight);
		TextView contentView = (TextView) window.findViewById(R.id.contentView);	
		LinearLayout.LayoutParams contentViewParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
		contentViewParams.height = (int) (screenWidth*0.275);
		contentView.setLayoutParams(contentViewParams);
		contentView.setMaxWidth((int) (screenWidth*0.8));
		LinearLayout.LayoutParams btnViewParams = (LinearLayout.LayoutParams) btnRight.getLayoutParams();
		btnViewParams.height = (int) (screenWidth*0.1);
		btnViewParams.width =  (int) (screenWidth*0.8);
		btnRight.setLayoutParams(btnViewParams);		
		contentView.setText(content);
		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dlg.cancel();
			}
		});
		return dlg;
	}
	
	//自定义错误提示窗，点击确定关闭dialog
	public static AlertDialog showInformDialogContext(final Context context, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_alert);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		TextView btnRight = (TextView) window.findViewById(R.id.btnRight);
		TextView contentView = (TextView) window.findViewById(R.id.contentView);	
		LinearLayout.LayoutParams contentViewParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
		contentViewParams.height = (int) (screenWidth*0.275);
		contentView.setLayoutParams(contentViewParams);
		contentView.setMaxWidth((int) (screenWidth*0.8));
		LinearLayout.LayoutParams btnViewParams = (LinearLayout.LayoutParams) btnRight.getLayoutParams();
		btnViewParams.height = (int) (screenWidth*0.1);
		btnViewParams.width =  (int) (screenWidth*0.8);
		btnRight.setLayoutParams(btnViewParams);		
		contentView.setText(content);
		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dlg.cancel();
			}
		});
		return dlg;
	}
	
	//自定义提示回退窗，点击确定关闭activity
	public static AlertDialog showBackDialog(final BaseActivity context, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		OnClickListener ok = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		};
		return showAlertDialog(context, ok, content);
	}
	
	
	//通用确认取消提示弹窗
	public static AlertDialog showConfirmDialog(Context context, boolean defaultOk, OnClickListener onClickListener, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_confirm);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		LinearLayout btnView = (LinearLayout) window.findViewById(R.id.btnView);
		TextView contentView = (TextView) window.findViewById(R.id.contentView);	
		LinearLayout.LayoutParams contentViewParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
		contentViewParams.height = (int) (screenWidth*0.275);
		contentView.setLayoutParams(contentViewParams);
		contentView.setMaxWidth((int) (screenWidth*0.8));
		LinearLayout.LayoutParams btnViewParams = (LinearLayout.LayoutParams) btnView.getLayoutParams();
		btnViewParams.height = (int) (screenWidth*0.1);
		btnViewParams.width =  (int) (screenWidth*0.8);
		btnView.setLayoutParams(btnViewParams);		
		contentView.setText(content);
		 // 确认按钮
		 TextView btnRight = (TextView) window.findViewById(R.id.btn_right);
		 TextView btnLeft = (TextView) window.findViewById(R.id.btn_left);
		 if(defaultOk) {
			 btnLeft.setText(R.string.btn_cancel_text);
			 btnRight.setText(R.string.btn_ok_text);
			 btnRight.setOnClickListener(onClickListener);
			 btnLeft.setOnClickListener(new View.OnClickListener() {
				   public void onClick(View v) {
					    dlg.cancel();
					  }
			 });
		 } else {
			 btnLeft.setText(R.string.btn_ok_text);
			 btnRight.setText(R.string.btn_cancel_text);
			 btnLeft.setOnClickListener(onClickListener);
			 btnRight.setOnClickListener(new View.OnClickListener() {
				   public void onClick(View v) {
					    dlg.cancel();
					  }
			 });			 
		 }
		 return dlg;
	}
	
	//通用确认,取消返回提示弹窗
	public static AlertDialog showConfirmBackDialog(final BaseActivity context, boolean defaultOk, OnClickListener onClickListener, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		dlg.setCancelable(false);
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_confirm);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		LinearLayout btnView = (LinearLayout) window.findViewById(R.id.btnView);
		TextView contentView = (TextView) window.findViewById(R.id.contentView);	
		LinearLayout.LayoutParams contentViewParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
		contentViewParams.height = (int) (screenWidth*0.275);
		contentView.setLayoutParams(contentViewParams);
		contentView.setMaxWidth((int) (screenWidth*0.8));
		LinearLayout.LayoutParams btnViewParams = (LinearLayout.LayoutParams) btnView.getLayoutParams();
		btnViewParams.height = (int) (screenWidth*0.1);
		btnViewParams.width =  (int) (screenWidth*0.8);
		btnView.setLayoutParams(btnViewParams);		
		contentView.setText(content);
		 // 确认按钮
		 TextView btnRight = (TextView) window.findViewById(R.id.btn_right);
		 TextView btnLeft = (TextView) window.findViewById(R.id.btn_left);
		 if(defaultOk) {
			 btnLeft.setText(R.string.btn_cancel_text);
			 btnRight.setText(R.string.btn_ok_text);
			 btnRight.setOnClickListener(onClickListener);
			 btnLeft.setOnClickListener(new View.OnClickListener() {
				   public void onClick(View v) {
					   	dlg.cancel();
					   	context.finish();
				}
			 });
		 } else {
			 btnLeft.setText(R.string.btn_ok_text);
			 btnRight.setText(R.string.btn_cancel_text);
			 btnLeft.setOnClickListener(onClickListener);
			 btnRight.setOnClickListener(new View.OnClickListener() {
				   public void onClick(View v) {
					   	dlg.cancel();
					   	context.finish();
				}
			 });			 
		 }
		 return dlg;
	}
	
	//通用确认提示弹窗
	public static AlertDialog showAlertDialog(Context context, OnClickListener ok, String content)
	{
		cancelToast();
		content = Tool.isBlank(content) ? "网络出错，请稍后再试" : content;
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setCancelable(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_alert);
		//适配大小
		int screenWidth = Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"));
		TextView btnRight = (TextView) window.findViewById(R.id.btnRight);
		TextView contentView = (TextView) window.findViewById(R.id.contentView);	
		LinearLayout.LayoutParams contentViewParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
		contentViewParams.height = (int) (screenWidth*0.275);
		contentView.setLayoutParams(contentViewParams);
		contentView.setMaxWidth((int) (screenWidth*0.8));
		LinearLayout.LayoutParams btnViewParams = (LinearLayout.LayoutParams) btnRight.getLayoutParams();
		btnViewParams.height = (int) (screenWidth*0.1);
		btnViewParams.width =  (int) (screenWidth*0.8);
		btnRight.setLayoutParams(btnViewParams);		
		contentView.setText(content);
		btnRight.setOnClickListener(ok);
		return dlg;
	}
	
	//通用确认取消提示弹窗
	public static AlertDialog showLoginDialog(final BaseActivity context)
	{
		cancelToast();
		String content = "您当前的登录状态已经失效，请重新登录";
		OnClickListener goLogin = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, LoginAlreadyActivity.class);
				context.startActivity(intent);
				context.closeDialog();
			}
		};
		return showAlertDialog(context, goLogin, content);
	}
	
/*	//退出提示弹窗
	public static AlertDialog showLoadingDialog(Context context)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.load_dialog);
		return dlg;
	}
	
	//退出提示弹窗
	public static AlertDialog showExitDialog(Context context, OnClickListener ok)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		 // 确认按钮
		 TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		 btnOk.setOnClickListener(ok);
		 // 关闭alert对话框架
		 TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
		   cancel.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
			    dlg.cancel();
			  }
		   });
		   return dlg;
	}
	
    //提示弹窗
	public static void showAlertDialog(Context context, String alert_content)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alert_dialog);
		//设置提醒内容
		((TextView) window.findViewById(R.id.txt_content)).setText(alert_content);
		// 确认按钮
		TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}
	
    //自定义提示弹窗
	public static AlertDialog showAlertActionDialog(Context context, OnClickListener ok, String btn_content, String alert_content)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alert_dialog);
		//设置提醒内容
		((TextView) window.findViewById(R.id.txt_content)).setText(alert_content);
		// 确认按钮
		TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		btnOk.setText(btn_content);
		btnOk.setOnClickListener(ok);
		return dlg;
	}
	
	//网络连接设置弹窗
	public static void showConNetDialog(final Context context)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		((TextView) window.findViewById(R.id.txt_content)).setText("网络未连接，是否打开网络设置？");
		 // 确认按钮
		 TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		 btnOk.setOnClickListener(new View.OnClickListener() {  
             @Override  
             public void onClick(View v) {  
                 int currentapiVersion=android.os.Build.VERSION.SDK_INT;  
                 Log.d("Tool", "currentapiVersion = " + currentapiVersion);  
                 Intent intent;  
                 if(currentapiVersion < 11){  
                     intent = new Intent();  
                     intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");  
                 }else{  
                     //3.0以后  
                     //intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
                     intent = new Intent(android.provider.Settings.ACTION_SETTINGS);  
                 }  
                 context.startActivity(intent);
                 dlg.cancel();
             }  
         }); 
		 
		 // 关闭alert对话框架
		 TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
		   cancel.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
			    dlg.cancel();
			  }
		   });
	}
	
	//通用确认取消提示弹窗
	public static AlertDialog showConfirmDialog(Context context, OnClickListener ok, String content)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		((TextView) window.findViewById(R.id.txt_content)).setText(content);
		 // 确认按钮
		 TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		 btnOk.setOnClickListener(ok);
		 
		 // 关闭alert对话框架
		 TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
		   cancel.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
			    dlg.cancel();
			  }
		   });
		 return dlg;
	}
	
	//通用确认取消提示弹窗
	public static AlertDialog showConfirmDialog(Context context, OnClickListener ok, String content, String title)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		((TextView) window.findViewById(R.id.txt_content)).setText(content);
		if(title != null)
		{
			((TextView)window.findViewById(R.id.txt_title)).setText(title);
		}
		 // 确认按钮
		 TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		 btnOk.setOnClickListener(ok);
		 
		 // 关闭alert对话框架
		 TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
		   cancel.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
			    dlg.cancel();
			  }
		   });
		 return dlg;
	}
	//通用确认取消提示弹窗
	public static void showReloginDialog(final Context context)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alert_dialog);
		((TextView) window.findViewById(R.id.txt_content)).setText("未登录或登录状态已过期，请登录");
		 // 确认按钮
		 TextView btnOk = (TextView) window.findViewById(R.id.btn_ok);
		 btnOk.setOnClickListener(new View.OnClickListener() {  
				public void onClick(View v){
					//跳转到登录页面
					Intent intent = new Intent();
					intent.setClass(context, LoginActivity.class);
					context.startActivity(intent);
					dlg.cancel();
				}
         }); 
	}
	
	//分享页面弹窗
	//通用确认取消提示弹窗
	public static AlertDialog showShareDialog(final Context context, OnClickListener toFriend, OnClickListener toQuan)
	{
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.wx_share_dialog);
		//分享给好友按钮
		ImageView toFre  = (ImageView)window.findViewById(R.id.wxLogo);
		ImageView toQua = (ImageView)window.findViewById(R.id.quanLogo);
		toFre.setOnClickListener(toFriend);
		toQua.setOnClickListener(toQuan);
		 // 取消按钮
		 TextView btnCancel = (TextView) window.findViewById(R.id.btn_cancel);
		 btnCancel.setOnClickListener(new View.OnClickListener() {  
				public void onClick(View v){
					dlg.cancel();
				}
         }); 
		 return dlg;
	}*/
}
