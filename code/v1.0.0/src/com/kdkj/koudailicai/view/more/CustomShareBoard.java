package com.kdkj.koudailicai.view.more;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CustomShareBoard extends PopupWindow implements OnClickListener{

	private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private Activity mActivity;
	private RelativeLayout layout;
	private boolean weixinShareFlag = true;
	private boolean circleShareFlag = true;
	private boolean qzoneShareFlag = true;
	private boolean sinaShareFlag = true;
	private boolean qqShareFlag = true;
	private boolean smsShareFlag = true;
	private CustomShareBoardInterface shareInterface;
	private boolean sinaShare = false;
	public interface CustomShareBoardInterface {
		public void shareClose();
	}
	public CustomShareBoard(Activity activity, CustomShareBoardInterface shareInterface){
		super(activity);
		this.mActivity =activity;
		this.shareInterface = shareInterface;
		initView(activity);
	}
	
	private void initView(Context context){
		View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
		layout = (RelativeLayout)rootView.findViewById(R.id.popwindow);
		rootView.findViewById(R.id.wechat).setOnClickListener(this);
		rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
		rootView.findViewById(R.id.share_cancel).setOnClickListener(this);
		rootView.findViewById(R.id.qzone).setOnClickListener(this);
		rootView.findViewById(R.id.sina_weibo).setOnClickListener(this);
		rootView.findViewById(R.id.qq).setOnClickListener(this);
		rootView.findViewById(R.id.sms).setOnClickListener(this);
		setContentView(rootView);
		Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.umeng_socialize_shareboard_animation_in);
		layout.setAnimation(animation);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ALPHA_8)));
		setTouchable(true);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.sina_weibo) {
			sinaShare = true;
		} else {
			sinaShare = false;
		}
		switch (v.getId()) {
		case R.id.wechat:
			if(weixinShareFlag){
				performShare(SHARE_MEDIA.WEIXIN);
				weixinShareFlag = false;
			}
			break;
		case R.id.wechat_circle:
			if(circleShareFlag){
				performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
				circleShareFlag = false;
			}
            break;
		case R.id.qzone:
			if(qzoneShareFlag){
				performShare(SHARE_MEDIA.QZONE);
				qzoneShareFlag = false;
			}
			break;
		case R.id.sina_weibo:
			if(sinaShareFlag){
				performShare(SHARE_MEDIA.SINA);
				sinaShareFlag = false;
			}
			break;
		case R.id.qq:
            if(qqShareFlag){
            	performShare(SHARE_MEDIA.QQ);
            	qqShareFlag = false;
			}
			break;
		case R.id.sms:
            if(smsShareFlag) {
            	performShare(SHARE_MEDIA.SMS);
            	smsShareFlag = false;
			}
			break;
       case R.id.share_cancel:
            dismiss();
            break;
		default:
			break;
		}
	}

	private void performShare(SHARE_MEDIA platform){
		mController.postShare(mActivity, platform, new SnsPostListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				if(sinaShare) {
					KdlcDialog.showBottomToast("已分享至新浪微博...");
					sinaShare = false;
					dismiss();
				}
			}
			
			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				// TODO Auto-generated method stub
				String showText = "";
	                if (eCode == StatusCode.ST_CODE_SUCCESSED && 
	                	!platform.equals(SHARE_MEDIA.SMS) && 
	                	!platform.equals(SHARE_MEDIA.SINA)) {
	                    showText = "分享成功";
	                    KdlcDialog.showBottomToast(showText);
	                    dismiss();
	                } else if(eCode == StatusCode.ST_CODE_ERROR || 
							  eCode == StatusCode.ST_CODE_ERROR_INVALID_DATA || 
							  eCode == StatusCode.ST_CODE_ERROR_WEIXIN
							  ) {
	                	showText = "分享失败";
	                    KdlcDialog.showBottomToast(showText);
	                }
	                weixinShareFlag = true;
                	circleShareFlag = true;
                	qzoneShareFlag = true;
                	sinaShareFlag = true;
                	qqShareFlag = true;
                	smsShareFlag = true;
			}
		});
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
        shareInterface.shareClose();
	}
	
}
