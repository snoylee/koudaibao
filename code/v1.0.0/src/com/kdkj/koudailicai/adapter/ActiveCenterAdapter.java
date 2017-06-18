package com.kdkj.koudailicai.adapter;

import java.net.URL;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ActiveCenterInfo;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActiveCenterAdapter extends ArrayAdapter {
	private List<ActiveCenterInfo> item;
	private LayoutInflater layoutInflater = null;
	private int screenHeight;
	private int screenWidth;
    private String url;
	public ActiveCenterAdapter(Context context, int textViewResourceId,
			List objects) {
		// TODO Auto-generated constructor stub
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		item = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.item.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ActiveCenterInfo items = this.item.get(position);
		ActiveCenterHolder ActiveCenterHolder = null;
		 parseUrl();
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_active_centerinfo,
					null);
			ActiveCenterHolder = new ActiveCenterHolder();
			ActiveCenterHolder.Activeimageholder = (NetworkImageView) convertView.findViewById(R.id.acimage);
			ActiveCenterHolder.ActiveTimeholder = (TextView) convertView.findViewById(R.id.actime);
			ActiveCenterHolder.Activetitleholder=(TextView)convertView.findViewById(R.id.acname);			
			ActiveCenterHolder.aclayout=(RelativeLayout)convertView.findViewById(R.id.aclayout);
			convertView.setTag(ActiveCenterHolder);

		} else {
			ActiveCenterHolder = (ActiveCenterHolder) convertView.getTag();
		}	
		ActiveCenterHolder.Activeimageholder.setDefaultImageResId(R.drawable.pic_1);  
		ActiveCenterHolder.Activeimageholder.setErrorImageResId(R.drawable.pic_1); 
		ImageLoader imageLoader = RequestManager.getImageLoader();
//		Log.v("dsjdhsakdhsakjd", items.getImageString());
     	ActiveCenterHolder.Activeimageholder.setImageUrl(items.getImageString(),imageLoader);
		ActiveCenterHolder.ActiveTimeholder.setText(items.getTimeString());
		ActiveCenterHolder.Activetitleholder.setText(items.getContentAbstract());
		ActiveCenterHolder.aclayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getContext(),WebViewActivity.class);
				intent.putExtra("url", url+"?id="+items.getActiveId());
				intent.putExtra("title", items.getTitle());
				getContext().startActivity(intent);
				
			}
		});
		return convertView;
	}
	private void parseUrl() {
		if (KDLCApplication.app.getGlobalConfigManager() != null
				&& KDLCApplication.app.getGlobalConfigManager()
						.isComplete()) {
			Log.d("asdasd", "parseurl");
			url = KDLCApplication.app.getActionUrl(
					G.GCK_API_PAGE_ACTIVITY_DETAIL);
			Log.d("asdasd", "parseurl:" + url);
		} else {
			url = G.URL_GET_PAGE_ACTIVITY_DETAIL;
		}
	}
	private static class ActiveCenterHolder {
		private NetworkImageView Activeimageholder;
		private TextView ActiveTimeholder;
		private TextView Activetitleholder;
		private RelativeLayout aclayout;
	}

}
