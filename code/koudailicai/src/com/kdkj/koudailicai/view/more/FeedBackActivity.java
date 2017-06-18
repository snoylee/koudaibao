package com.kdkj.koudailicai.view.more;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.FeedBackAdapter;
import com.kdkj.koudailicai.adapter.GroupAdapter;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.accountremain.AccountRemainActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.IncomeDetailsActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.PaymentDetailsActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.SpendingDetailsActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawActivity;

public class FeedBackActivity extends BaseActivity implements ResizeListener {
	private String LOG_TAG = FeedBackActivity.class.getName();
	private ResizeRelativeLayout feedbackParentView;
	private TitleView feedbacktitle;
	private TextView logininpassword;
	private Handler mHandler = new Handler();
	private ScrollView feedbackscrollview;
	private EditText input_feedback;
	private EditText input_phone;
	private TextView buttonlogin;
	private TextView tip;
	private String url;
	private boolean Sign1 = false;
	private boolean Sign2 = false;
	private int curClick=-1;
	private PopupWindow popupWindow;
	private View view;
	private ListView feedBackListView;
	private List<String> groups;
	private String type="0";
	private RelativeLayout feedBackType;
	private String user_info;
	private String tel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		parseUrl();
		init();
		inittitle();
		setListeners();
	}

	private void init() {
		feedbackParentView = (ResizeRelativeLayout) this.findViewById(R.id.feedbackParentView);
		feedbackParentView.setResizeListener(this);
		feedbacktitle = (TitleView) findViewById(R.id.feedbacktitle);
		logininpassword = (TextView) findViewById(R.id.logininpassword);
		feedBackType=(RelativeLayout)findViewById(R.id.feedbacktype);
		input_feedback = (EditText) findViewById(R.id.input_feedback);
		input_phone = (EditText) findViewById(R.id.input_phone);
		buttonlogin = (TextView) findViewById(R.id.buttonlogin);
		feedbackscrollview = (ScrollView) findViewById(R.id.feedbackscrollview);
		tip = (TextView) findViewById(R.id.tip);
		tip.setText("如有疑问，您也可以直接联系:"+tel);
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			tel = getApplicationContext().getConfVal(G.GCK_VAL_APP_TELE);
			url = getApplicationContext().getActionUrl(G.GCK_API_POST_PAGE_ADD_SUGGEST);
		} else {
			url = G.URL_POST_PAGE_ADD_SUGGEST;
			tel = G.VAL_APP_TELE;
		}
	}

	private void inittitle() {
		feedbacktitle.setTitle(R.string.more_feed_back);
		feedbacktitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FeedBackActivity.this.finish();
			}
		});
		feedbacktitle.setLeftImageButton(R.drawable.back);
		feedbacktitle.setLeftTextButton("返回");

	}

	private void setListeners() {
		textChange tc2 = new textChange();
		input_feedback.addTextChangedListener(tc2);
		input_phone.addTextChangedListener(tc2);
		input_feedback.setOnTouchListener(clickListener);
		input_phone.setOnTouchListener(clickListener);
		feedBackType.setOnClickListener(typeListener);
	}
	private OnClickListener typeListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (popupWindow != null) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				} else {
					popupWindow.showAsDropDown(feedBackType, 0, 0);
				}
			} else {
				showWindow(feedBackType);
				popupWindow.showAsDropDown(feedBackType, 0, 0);
			}

		
		}
	};
	private void showWindow(View parent) {
		LayoutInflater layoutInflater = getLayoutInflater();
		view = layoutInflater.inflate(R.layout.activity_type_list, null);
		if (popupWindow == null) {
			feedBackListView = (ListView) view.findViewById(R.id.feedbackGroup);
			groups = new ArrayList<String>();
			groups.add("功能问题");
			groups.add("用户体验");
			groups.add("您的需求");
			groups.add("其他");
			FeedBackAdapter groupAdapter = new FeedBackAdapter(FeedBackActivity.this, groups);
			feedBackListView.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setOutsideTouchable(true);
		// // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		feedBackListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				logininpassword.setTextColor(FeedBackActivity.this.getResources().getColor(R.color.global_black_color));
				if (groups.get(position).equals("功能问题") || position == 0) {
					type="1";
					logininpassword.setText("功能问题");
				} else if (groups.get(position).equals("用户体验") || position == 1) {
					type="2";
					logininpassword.setText("用户体验");

				} else if (groups.get(position).equals("您的需求") || position == 2) {
					type="3";
					logininpassword.setText("您的需求");

				} else {
					type="4";
					logininpassword.setText("其他");

				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	private OnTouchListener clickListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionevent) {
			// TODO Auto-generated method stub
			curClick = view.getId();
			if (motionevent.getAction() == MotionEvent.ACTION_UP) {
				switch (view.getId()) {
				case R.id.input_feedback:
					break;
				case R.id.input_phone:
					feedbackscrollview.fullScroll(ScrollView.FOCUS_DOWN);
					input_phone.requestFocus();
					break;
				
				}
			}

			return false;
		}
	};
	
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
			Sign1 = input_feedback.getText().length() > 0;
			Sign2 = input_phone.getText().length() > 0;
			if (Sign1) {
				buttonlogin
						.setBackgroundResource(R.drawable.btn_red_background);
				buttonlogin.setOnClickListener(modify);

			}

			else {
				buttonlogin
						.setBackgroundResource(R.drawable.btn_grey_background);
				buttonlogin.setClickable(false);
			}
		}

	}

	private OnClickListener modify = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog = KdlcDialog.showProgressDialog(FeedBackActivity.this, "正在提交...");
			getKdbRemainInfo();
		}
	};

	private void getKdbRemainInfo() {
		HttpParams params = new HttpParams();
		params.add("type", type);
	   if (Sign1 == true && Sign2 == false) {
	        if(!Tool.isBlank(KDLCApplication.app.getSessionVal("username")))
	        {
	            params.add("user_info", KDLCApplication.app.getSessionVal("username"));
	        }else{
	        	params.add("user_info", "visitor");
	        }
			
			params.add("content", input_feedback.getText().toString());

		} else {
			params.add("user_info", input_phone.getText().toString());
			params.add("content", input_feedback.getText().toString());
		}
		sendHttpPost(FeedBackActivity.this.url, params, getKdbInfoListener);
	}

	private Listener<JSONObject> getKdbInfoListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "response:" + response.toString());
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					KdlcDialog.showBackDialog(FeedBackActivity.this, response.getString("msg"));
				
				} else {
					KdlcDialog.showInformDialog(FeedBackActivity.this ,response.getString("message"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}

	};
	private void setFocus(int resId) {
		switch (resId) {
		case R.id.input_feedback:
			input_feedback.requestFocus();
			break;
		case R.id.input_phone:
			input_phone.requestFocus();
			break;
		}
	}
	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if(curClick== R.id.input_phone)
					{
						feedbackscrollview.fullScroll(ScrollView.FOCUS_DOWN);
					}
					setFocus(curClick);
				}
			
			});
		}
	}
	
}
