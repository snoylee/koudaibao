package com.kdkj.koudailicai.view.selfcenter.password;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferSuccessActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TransferPasswordActivity extends BaseActivity {
	private TitleView transpasswordtitle;
	private TextView transpasswordname;
	private TextView money;
	private EditText transferaccount;
	private TextView transferBtn;
	private String url;
    private TextView lostpassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transfer_password);
		parseUrl();
		findViews();
		inittitle();
		initcontent();
		listener();
	}

	private void findViews() {
		transpasswordtitle = (TitleView) findViewById(R.id.transpasswordtitle);
		transpasswordname = (TextView) findViewById(R.id.transpasswordname);
		money = (TextView) findViewById(R.id.money);
		transferaccount = (EditText) findViewById(R.id.transferaccount);
		transferBtn = (TextView) findViewById(R.id.transferBtn);
		lostpassword=(TextView)findViewById(R.id.lostpassword);
	}

	private void parseUrl() {
		if (this.getApplicationContext().getGlobalConfigManager() != null
				&& this.getApplicationContext().getGlobalConfigManager()
						.isComplete()) {
			Log.d("asdasd", "parseurl");
			url = this.getApplicationContext().getActionUrl(
					G.GCK_API_URL_POST_CREDIT_ASSIGN);
			Log.d("asdasd", "parseurl:" + url);
		} else {
			url = G.URL_POST_CREDIT_ASSIGN;
		}
	}

	private void initcontent() {
		transpasswordname.setText(getIntent().getStringExtra("transferName"));
		money.setText(getIntent().getStringExtra("money"));
	}

	private void inittitle() {
		transpasswordtitle.setTitle(R.string.transfertitle);
		transpasswordtitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TransferPasswordActivity.this.finish();
			}
		});
		transpasswordtitle.setLeftImageButton(R.drawable.back);
		transpasswordtitle.setLeftTextButton("返回");

	}

	private void listener() {
		textChange txChange = new textChange();
		transferaccount.addTextChangedListener(txChange);
		transferaccount.setKeyListener(new DigitsKeyListener(false,false));
		lostpassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TransferPasswordActivity.this, BackPasswordActivity.class);
				intent.putExtra("find_pwd", "find_pay_pwd");
				startActivity(intent);
			}
		});
	}

	private OnClickListener transBtn = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog = KdlcDialog.showProgressDialog(TransferPasswordActivity.this, "正在提交...");
			getKdbRemainInfo();
		}
	};

	private void getKdbRemainInfo() {
		HttpParams vals = new HttpParams();
		vals.add("invest_id", getIntent().getStringExtra("id"));
		Log.v("dasderrwerwerewrwe", getIntent().getStringExtra("id"));
		vals.add("assign_fee", getIntent().getStringExtra("money"));
		vals.add("pay_password", transferaccount.getText().toString());
		sendHttpPost(TransferPasswordActivity.this.url, vals, transferListener,
				new PullToRefreshHttpErrorListener(
						TransferPasswordActivity.this));
	}

	private Listener<JSONObject> transferListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
				KDLCApplication.app.setSessionVal("selfCenterAutoRefresh", "1");
                Intent intent=new Intent(TransferPasswordActivity.this,TransferSuccessActivity.class);
                intent.putExtra("transferName", getIntent().getStringExtra("transferName"));
                intent.putExtra("money", getIntent().getStringExtra("money"));
                startActivity(intent);
                TransferPasswordActivity.this.finish();
				}else{
					KdlcDialog.showInformDialog(TransferPasswordActivity.this, response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				KdlcDialog.showBottomToast("");
				e.printStackTrace();
			}
		}

	};

	// EditText监听器
	class textChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign1 = false;
			if (transferaccount.getText().length() > 0) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}

			if (Sign1) {
				transferBtn
						.setBackgroundResource(R.drawable.btn_red_background);
				transferBtn.setOnClickListener(transBtn);
			} else {
				transferBtn
						.setBackgroundResource(R.drawable.btn_grey_background);
				transferBtn.setClickable(false);
			}
		}

	}
}
