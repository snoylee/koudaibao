package com.kdkj.koudailicai.adapter;

import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.HelpCenterInfo;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpCenterAdapter extends ArrayAdapter {
	private List<HelpCenterInfo> item;
	private LayoutInflater layoutInflater = null;
	private int screenHeight;
	private int screenWidth;
    private String url;
	public HelpCenterAdapter(Context context, int textViewResourceId,
			List objects) {
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
		final HelpCenterInfo items = this.item.get(position);
		parseUrl();
		HelpCenterHolder helpCenterHolder=null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_help_info,
					null);
			helpCenterHolder=new HelpCenterHolder();
			helpCenterHolder.helptimeholder=(TextView) convertView.findViewById(R.id.helptime);
			helpCenterHolder.helpinfoholder=(TextView)convertView.findViewById(R.id.helpinfo);
			helpCenterHolder.helpLayout=(RelativeLayout)convertView.findViewById(R.id.helplayout);
//			RelativeLayout.LayoutParams startFeeLayoutParams = (RelativeLayout.LayoutParams) helpCenterHolder.helpLayout.getLayoutParams();
//		    startFeeLayoutParams.height = (int) (screenHeight * 0.160);
//		    helpCenterHolder.helpLayout.setLayoutParams(startFeeLayoutParams); 
//		    RelativeLayout.LayoutParams helptimeLayoutParams = (RelativeLayout.LayoutParams) helpCenterHolder.helptimeholder.getLayoutParams();
//		    helptimeLayoutParams.topMargin = (int) (screenHeight * 0.026);
//		    helpCenterHolder.helptimeholder.setLayoutParams(helptimeLayoutParams);
//		    RelativeLayout.LayoutParams helpinfoLayoutParams = (RelativeLayout.LayoutParams) helpCenterHolder.helpinfoholder.getLayoutParams();
//		    helpinfoLayoutParams.bottomMargin = (int) (screenHeight * 0.026);
//		    helpCenterHolder.helpinfoholder.setLayoutParams(helpinfoLayoutParams);
			convertView.setTag(helpCenterHolder);

		} else {
			helpCenterHolder = (HelpCenterHolder) convertView.getTag();
		}
		helpCenterHolder.helptimeholder.setText(items.getTitle());
		helpCenterHolder.helpinfoholder.setText(items.getContent());
		helpCenterHolder.helpLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getContext(),WebViewActivity.class);
				intent.putExtra("url", url+"?id="+items.getHelpId());
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
					G.GCK_API_POST_PAGE_DETAIL);
			Log.d("asdasd", "parseurl:" + url);
		} else {
			url = G.URL_POST_PAGE_DETAIL;
		}
	}
	private static class HelpCenterHolder {
		private TextView helptimeholder;
		private TextView helpinfoholder;
		private RelativeLayout helpLayout;
	}

}
