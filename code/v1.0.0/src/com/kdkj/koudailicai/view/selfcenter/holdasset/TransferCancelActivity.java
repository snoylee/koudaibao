package com.kdkj.koudailicai.view.selfcenter.holdasset;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.global.G;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.kdkj.koudailicai.view.BaseActivity;
public class TransferCancelActivity extends BaseActivity {
	private TitleView canceltitle;
	private TextView cancelname;
	private TextView cancelbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transfer_cancel);
		findview();
		initTitle();
		content();
		listener();
	}

	private void findview() {
		canceltitle = (TitleView) findViewById(R.id.canceltitle);
		cancelname = (TextView) findViewById(R.id.cancelname);
		cancelbtn = (TextView) findViewById(R.id.cancelbtn);
	}

	private void content() {
		cancelname.setText(getIntent().getStringExtra("transferName"));
	}

	private void listener() {
		cancelbtn.setOnClickListener(btn);
	}

	private OnClickListener btn = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			backToHoldAsset();
		}
	};

	private void initTitle() {
		// TODO Auto-generated method stub
		canceltitle.setTitle("取消成功");
		canceltitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backToHoldAsset();
			}
		});
		canceltitle.setLeftImageButton(R.drawable.back);
		canceltitle.setLeftTextButton("返回");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToHoldAsset();
		}
		return false;
	}
	
	public void backToHoldAsset() {
		Intent intent = new Intent(G.HOLD_ASSET_REFRESH);
        sendBroadcast(intent);
        TransferCancelActivity.this.finish();
	}
}
