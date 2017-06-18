/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.kdkj.koudailicai.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshWebView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;

public final class WebViewActivity extends BaseActivity {

	private PullToRefreshWebView mPullRefreshWebView;
	private WebView mWebView;
	private TitleView mTitle;
	private String title;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initContent();
		findViews();
		initTitle();
	}
	
	private void initTitle() {
		mTitle.setTitle(Tool.changeTitle(this.title));
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WebViewActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}
	
	private void initContent() {
		Intent intent = this.getIntent();
		url = Tool.isBlank(intent.getStringExtra("url")) ? G.URL_SITE : intent.getStringExtra("url");
		title = Tool.isBlank(intent.getStringExtra("title")) ?  G.APP_NAME : intent.getStringExtra("title");
	}
	
	public void setTitle(String title) {
		this.title = title;
		mTitle.setTitle(Tool.changeTitle(this.title));
	}
	
	private void findViews() {
		mTitle = (TitleView) this.findViewById(R.id.title);
		mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
		mWebView = mPullRefreshWebView.getRefreshableView();
		dialog = KdlcDialog.showProgressDialog(WebViewActivity.this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(Tool.getUrl(url));
		mWebView.setWebViewClient(new KdlcWebViewClient());
		mWebView.setWebChromeClient(new KdlcWebChromeClient());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Tool.resetPullRefreshView(mPullRefreshWebView);
	}
	
	public final class KdlcWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			dialog.cancel();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
			Log.d("adasd", "onReceivedError");
			KdlcDialog.showBottomToast("");
			dialog.cancel();
		}		 
	}

	public final class KdlcWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.d("adasd", "asdas:"+newProgress);
			if (newProgress == 100) {
				WebViewActivity.this.mPullRefreshWebView.onRefreshComplete();
			}
		}
	}

}
