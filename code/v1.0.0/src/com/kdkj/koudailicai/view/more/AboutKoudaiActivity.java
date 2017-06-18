package com.kdkj.koudailicai.view.more;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.WebViewActivity;

public class AboutKoudaiActivity extends BaseActivity {
	private TitleView abouttitle;
	private String tele;
	private String address;
	private String site;
//	private String email;
	private String name;
	private String appAbout;
	private TextView title;
	private TextView aboutcontent;
	private TextView abouttele;
	private TextView aboutaddress;
	private TextView aboutsite;
//	private TextView aboutcite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_koudai);
		parseUrl();
		findView();
		inittitle();
		contents();
	}

	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			tele = getApplicationContext().getConfVal(G.GCK_VAL_APP_TELE);
			address = getApplicationContext().getConfVal(
					G.GCK_VAL_COMPANY_ADDRESS);
			site = getApplicationContext().getConfVal(G.GCK_VAL_SITE_URL);
//			email = getApplicationContext().getConfVal(G.GCK_VAL_COMPANY_EMAIL);
			name = getApplicationContext().getConfVal(G.GCK_VAL_APP_NAME);
			appAbout = getApplicationContext().getConfVal(
					G.GCK_VAL_COMPANY_ABOUT);
		} else {
			tele = G.VAL_APP_TELE;
			address = G.VAL_COMPANY_ADDRESS;
			site = G.VAL_SITE_URL;
//			email = G.VAL_COMPANY_EMAIL;
			name = G.VAL_APP_NAME;
			appAbout = G.VAL_COMPANY_ABOUT;
		}
	}

	private void findView() {
		abouttitle = (TitleView) findViewById(R.id.abouttitle);
		title = (TextView) findViewById(R.id.title);
		aboutcontent = (TextView) findViewById(R.id.aboutcontent);
		abouttele = (TextView) findViewById(R.id.abouttele_num);
		aboutaddress = (TextView) findViewById(R.id.aboutaddress);
		aboutsite = (TextView) findViewById(R.id.aboutsite);
//		aboutcite = (TextView) findViewById(R.id.aboutcite);
	}
   private void contents(){
	   title.setText(name);
	   aboutcontent.setText(appAbout);
	   abouttele.setText(tele);
	   abouttele.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	   abouttele.getPaint().setAntiAlias(true);
	   aboutaddress.setText("公司地址："+address);
	   aboutsite.setText(site);
	   aboutsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	   aboutsite.getPaint().setAntiAlias(true);
	   abouttele.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			tele = tele.replaceAll("-", "").trim().toString();
			 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tele));  
             startActivity(intent); 
		}
	});
	   aboutsite.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Intent intent=new Intent(AboutKoudaiActivity.this,WebViewActivity.class);
//			intent.putExtra("url", "http://"+site+"/");
//			intent.putExtra("title","口袋理财");
//			AboutKoudaiActivity.this.startActivity(intent);
			Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+site+"/")); 
			AboutKoudaiActivity.this.startActivity(intent);
		}
	});
//	   aboutcite.setText(email);
//	   aboutcite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
   }
	private void inittitle() {
		abouttitle.setTitle(R.string.more_about_koudai);
		abouttitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AboutKoudaiActivity.this.finish();
			}
		});
		abouttitle.setLeftImageButton(R.drawable.back);
		abouttitle.setLeftTextButton("返回");

	}
}
