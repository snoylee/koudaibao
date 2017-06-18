package com.kdkj.koudailicai.view.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ShareInfo;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.WebViewActivity;
import com.kdkj.koudailicai.view.more.AboutKoudaiActivity;
import com.kdkj.koudailicai.view.more.ActiveCenterActivity;
import com.kdkj.koudailicai.view.more.CustomShareBoard;
import com.kdkj.koudailicai.view.more.CustomShareBoard.CustomShareBoardInterface;
import com.kdkj.koudailicai.view.more.FeedBackActivity;
import com.kdkj.koudailicai.view.more.HelpCenterActivity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreFragment extends BaseFragment implements CustomShareBoardInterface{
	private FragmentActivity mActivity;
	private View mParent;
	private String LOG_TAG = MoreFragment.class.getName();
	private TitleView moretitle;
	private double relativenumber = 0.077;
	private double bottomnumberheight = 0.114;
	private double bottomnumberwidth = 0.203;
	private RelativeLayout activecenter, messagecenter, helpcenter,
			checkupdate, aboutkoudai;
	private RelativeLayout tofriend, feedback, attention, encourage;
	private int screenHeight;
	private int screenWidth;
	private TextView morebuttonlogin;
	private String url;
	private Toast toast;
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private String shareUrl;
	ShareInfo shareInfo;
	private AlertDialog alterDialog;
	private ImageView toFriendImage;
    private RelativeLayout friend,feedBack1;
    private boolean canShare = true;
    
	public static MoreFragment newInstance(int index) {
		MoreFragment f = new MoreFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG_TAG, "call onActivityCreated");
		mActivity = getActivity();
		mParent = getView();
		parseUrl();
		init();
		initTitle();
		initsize();
		listers();
		// 配置需要分享的相关平台
		configPlatforms();
	}

	private void init() {
		moretitle = (TitleView) mParent.findViewById(R.id.moretitle);
		activecenter = (RelativeLayout) mParent.findViewById(R.id.activecenter);
		messagecenter = (RelativeLayout) mParent.findViewById(R.id.messagecenter);
		helpcenter = (RelativeLayout) mParent.findViewById(R.id.helpcenter);
		checkupdate = (RelativeLayout) mParent.findViewById(R.id.checkupdate);
		aboutkoudai = (RelativeLayout) mParent.findViewById(R.id.aboutkoudai);
		tofriend = (RelativeLayout) mParent.findViewById(R.id.tofriend);
		feedback = (RelativeLayout) mParent.findViewById(R.id.feedback);
		friend=(RelativeLayout)mParent.findViewById(R.id.friend);
		feedBack1=(RelativeLayout)mParent.findViewById(R.id.back);
		morebuttonlogin = (TextView) mParent.findViewById(R.id.morebuttonlogin);
		toFriendImage = (ImageView) mParent.findViewById(R.id.tofriendimage);
		if (KDLCApplication.app.getSession().get("isLogin") == null
				|| KDLCApplication.app.getSession().get("isLogin").equals("0")) {
			morebuttonlogin.setVisibility(View.GONE);
		} else {
			morebuttonlogin.setVisibility(View.VISIBLE);
		}
	}

	private void initTitle() {

		moretitle.setTitle(R.string.more_title);
	}

	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			url = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PAGE_FXBZJ);
			shareUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_SHARE_INFO);
		} else {
			url = G.URL_GET_PAGE_FXBZJ;
			shareUrl = G.URL_GET_SHARE_INFO;
		}
	}
	private void listers() {
		activecenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity,
						ActiveCenterActivity.class);
				mActivity.startActivity(intent);
			}
		});
		messagecenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, WebViewActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("title", "风险控制");
				mActivity.startActivity(intent);
			}
		});
		helpcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, HelpCenterActivity.class);
				mActivity.startActivity(intent);
			}
		});
		checkupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = KdlcDialog.showProgressDialog(mActivity, "正在检查...");
				/*
				 * toast=Toast.makeText(mActivity, "检查更新中...",
				 * Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER, 0, 0);
				 * toast.setGravity(Gravity.CENTER|Gravity.BOTTOM, 0,
				 * (int)(screenHeight*(0.09))); toast.show();
				 */

				// UmengUpdateAgent.update(mActivity);
				// 手动更新程序
				appUpdate();
			}
		});
		aboutkoudai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, AboutKoudaiActivity.class);
				mActivity.startActivity(intent);

			}
		});
		feedBack1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, FeedBackActivity.class);
				mActivity.startActivity(intent);
			}
		});
		morebuttonlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alterDialog = KdlcDialog.showConfirmDialog(mActivity, false,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alterDialog.cancel();
								KDLCApplication.app.logout();
								Intent intent = new Intent(mActivity,
										MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								mActivity.startActivity(intent);
							}
						}, "您确定退出登录吗？");

			}
		});
		friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(canShare) {
					dialog = KdlcDialog.showProgressDialog(mActivity);
					sendHttpGet(shareUrl, getShareInfoListener, new ShareErrorListener());
				}
			}
		});
	}

	private void initsize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams) activecenter
				.getLayoutParams();
		lastDayProfitsLayoutParams.height = (int) (screenHeight * relativenumber);
		activecenter.setLayoutParams(lastDayProfitsLayoutParams);
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams1 = (RelativeLayout.LayoutParams) messagecenter
				.getLayoutParams();
		lastDayProfitsLayoutParams1.height = (int) (screenHeight * relativenumber);
		messagecenter.setLayoutParams(lastDayProfitsLayoutParams1);
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams2 = (RelativeLayout.LayoutParams) helpcenter
				.getLayoutParams();
		lastDayProfitsLayoutParams2.height = (int) (screenHeight * relativenumber);
		helpcenter.setLayoutParams(lastDayProfitsLayoutParams2);
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams3 = (RelativeLayout.LayoutParams) checkupdate
				.getLayoutParams();
		lastDayProfitsLayoutParams3.height = (int) (screenHeight * relativenumber);
		checkupdate.setLayoutParams(lastDayProfitsLayoutParams3);
		RelativeLayout.LayoutParams lastDayProfitsLayoutParams4 = (RelativeLayout.LayoutParams) aboutkoudai
				.getLayoutParams();
		lastDayProfitsLayoutParams4.height = (int) (screenHeight * relativenumber);
		aboutkoudai.setLayoutParams(lastDayProfitsLayoutParams4);

		RelativeLayout.LayoutParams friendLayoutParamswidth = (RelativeLayout.LayoutParams) tofriend
				.getLayoutParams();
		friendLayoutParamswidth.width= (int)(screenWidth)/2;
		tofriend.setLayoutParams(friendLayoutParamswidth);
		RelativeLayout.LayoutParams friendLayoutParamsheight = (RelativeLayout.LayoutParams) friend
				.getLayoutParams();
		friendLayoutParamsheight.height =(int)(screenWidth)/5;
		Log.v("2314354654654645", screenWidth+"");
		friend.setLayoutParams(friendLayoutParamsheight);
		RelativeLayout.LayoutParams friendLayoutwidth = (RelativeLayout.LayoutParams) friend
				.getLayoutParams();
		friendLayoutwidth.width =(int)(screenWidth)/5;
		Log.v("2314354654654645", screenWidth+"");
		friend.setLayoutParams(friendLayoutwidth);
		

		RelativeLayout.LayoutParams feedbackLayoutParamswidth = (RelativeLayout.LayoutParams) feedback
				.getLayoutParams();
		feedbackLayoutParamswidth.width =(int)(screenWidth)/2;
		feedback.setLayoutParams(feedbackLayoutParamswidth);
		RelativeLayout.LayoutParams feedbackLayoutParamsheight = (RelativeLayout.LayoutParams) feedBack1
				.getLayoutParams();
		feedbackLayoutParamsheight.height = (int)(screenWidth)/5;
		feedBack1.setLayoutParams(feedbackLayoutParamsheight);
		RelativeLayout.LayoutParams feedbackLayoutwdith = (RelativeLayout.LayoutParams) feedBack1
				.getLayoutParams();
		feedbackLayoutwdith.width = (int)(screenWidth)/5;
		feedBack1.setLayoutParams(feedbackLayoutwdith);
//		RelativeLayout.LayoutParams attentionLayoutParamsheight = (RelativeLayout.LayoutParams) attention
//				.getLayoutParams();
		
	}

	@Override
	public void onStart() {
		super.onStart();
		morebuttonlogin.setVisibility(KDLCApplication.app.hasLogin() ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "call onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(LOG_TAG, "call onStop");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "call onDestroy");
	}

	/**
	 * 更新应用程序
	 */
	private void appUpdate() {
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);// 非wifi也可以更新
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				dialog.cancel();
				LogUtil.info(updateStatus + "");
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(mActivity, updateInfo);
					if (UmengUpdateAgent.isIgnore(mActivity, updateInfo)) {
						// 有更新且被忽略
						LogUtil.info("将版本设置为未忽略，并重新提示下载");
						UmengUpdateAgent.forceUpdate(mActivity);
						return;
					}
					break;
				case UpdateStatus.No: // has no update
					KdlcDialog.showBottomToast("已经是最新版本");
					break;
				default:
					KdlcDialog.showBottomToast("");
					break;

				}
			}
		});
		UmengUpdateAgent.update(mActivity);
	}

	private Listener<JSONObject> getShareInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			if (dialog.isShowing()) {
				dialog.cancel();
			}
			try {
				if (response.getInt("code") == 0) {
					JSONObject getShareInfo = response.getJSONObject("data");
					shareInfo = new ShareInfo(getShareInfo.getString("title"),
							getShareInfo.getString("desc"),
							getShareInfo.getString("url"), getShareInfo.getString("summary"), getShareInfo.getString("androidDownloadUrl"));
					// 设置分享的内容
					setShareContent();
					// 展示自定义分享页面
					postShare();
				} else {
					KdlcDialog.showBottomToast("");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				KdlcDialog.showBottomToast("");
				e.printStackTrace();
			}
		}
	};

	private void setShareContent() {
		UMImage urlImage = new UMImage(mActivity, R.drawable.logo_108);
		mController.getConfig().closeToast();
		String  targetUrl = "";
		String  desc = "";
		String  title = "";
		String  summary = "";
		String  downloadUrl ="";
		String  smsPrefix = "";
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 设置微信分享内容
		if (shareInfo != null) {
			//设置默认内容
			targetUrl = Tool.isBlank(shareInfo.getUrl()) ? "http://www.koudailc.com/" : shareInfo.getUrl();
			desc = Tool.isBlank(shareInfo.getDesc()) ? "一款专注服务普通大众的移动理财工具，所有产品收益率均在8%+，支持随取随存，本息垫付" : shareInfo.getDesc();
			title = Tool.isBlank(shareInfo.getTitle()) ? "口袋理财" : shareInfo.getTitle();
			summary = Tool.isBlank(shareInfo.getSummary()) ? "口袋理财-专业的移动理财平台，所有产品收益率均在8%+，支持随取随存，本息垫付" : shareInfo.getSummary();
			downloadUrl = Tool.isBlank(shareInfo.getAndroidDownloadUrl()) ? "http://www.koudailc.com/" : shareInfo.getAndroidDownloadUrl();
			smsPrefix = "亲爱的小伙伴，我发现了一款好玩可赚钱的东东：口袋理财，专注服务普通大众的移动理财工具，所有产品收益率均在8%+，支持随取随存，本息垫付。现在分享给你，点击";
			//设置微信好友
			WeiXinShareContent weixinShareContent = new WeiXinShareContent();
			weixinShareContent.setShareContent(desc);
			weixinShareContent.setTitle(title);
			weixinShareContent.setTargetUrl(targetUrl);
			weixinShareContent.setShareImage(urlImage);
			mController.setShareMedia(weixinShareContent);

			// 设置朋友圈分享内容
			CircleShareContent circleShareContent = new CircleShareContent();
			circleShareContent.setTitle(summary);
			circleShareContent.setShareContent(desc);
			circleShareContent.setTargetUrl(targetUrl);
			circleShareContent.setShareImage(urlImage);
			mController.setShareMedia(circleShareContent);

			// 设置QQ空间分享内容
			QZoneShareContent qzone = new QZoneShareContent();
			qzone.setShareContent(desc);
			qzone.setTargetUrl(targetUrl);
			qzone.setTitle(title);
			qzone.setShareImage(urlImage);
			mController.setShareMedia(qzone);
			
			// 设置QQ好友分享内容
		    QQShareContent qqShareContent = new QQShareContent();
		    qqShareContent.setShareContent(desc);
		    qqShareContent.setTargetUrl(targetUrl);
		    qqShareContent.setTitle(title);
		    qqShareContent.setShareImage(urlImage);
			mController.setShareMedia(qqShareContent);

			// 设置sina微博分享内容
			SinaShareContent sinaShareContent = new SinaShareContent();
			sinaShareContent.setShareContent(desc);
			sinaShareContent.setTargetUrl(targetUrl);
			sinaShareContent.setTitle(title);
			sinaShareContent.setShareImage(urlImage);
			mController.setShareMedia(sinaShareContent);
			
			//设置短信分享内容
		    SmsShareContent smsShareContent = new SmsShareContent();
		    smsShareContent.setShareContent(desc);//smsPrefix+downloadUrl+"，速度下载体验吧");
			mController.setShareMedia(smsShareContent);
		}
	}

	private void postShare() {
		CustomShareBoard shareBoard = new CustomShareBoard(mActivity, this);
		shareBoard.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	private void configPlatforms() {
		addWXPlatform();
		addQQQZonePlatform();
		// 添加短信平台
        addSMS();
	}

	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// 在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx8e437fcbec67b90c";
		String appSecret = "522ec6a068c9f94856f9a6d44b3f1599";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1103678075";
		String appKey = "NhqvJ1hN8bSgUayb";
		 // 添加QQ支持, 并且设置QQ分享内容的target url
		 UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
		 appId, appKey);
		 qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	private void addSMS(){
		// 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
	}

	@Override
	public void shareClose() {
		// TODO Auto-generated method stub
		canShare = true;
	}
	
	public class ShareErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			if(NoConnectionError.class.isInstance(error)) {
				KdlcDialog.showNetErrToast("网络未连接，请检查网络设置后再试");
			} else {
				KdlcDialog.showNetErrToast("网络出错，请稍后再试");
			}
			canShare = true;
			if(dialog != null)
				dialog.cancel();
		}
	}
}
