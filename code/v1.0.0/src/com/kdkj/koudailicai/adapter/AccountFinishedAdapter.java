package com.kdkj.koudailicai.adapter;


import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.AccountFinishedInfo;
import com.kdkj.koudailicai.domain.KdbRollOutListInfo;
import com.kdkj.koudailicai.domain.ProductListP2PInfo;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountFinishedAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<AccountFinishedInfo> item;
	private int screenHeight;
	private int screenWidth;
	private double rollout_list_height = 0.1;
    
	public AccountFinishedAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
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
		// TODO Auto-generated method stub
		AccountFinishedInfo items=this.item.get(position);
		AccountFinishedHolder holder=null;

		if(convertView==null)
		{
			convertView = layoutInflater.inflate(R.layout.activity_acccount_finishinfo, null);
			RelativeLayout accountListView=(RelativeLayout)convertView.findViewById(R.id.accountlistview);
//			RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams)accountListView.getLayoutParams();
//			lastDayProfitsLayoutParams.height = (int) (screenHeight*rollout_list_height);
//			accountListView.setLayoutParams(lastDayProfitsLayoutParams);
			holder=new AccountFinishedHolder();
			holder.accountFinishedName=(TextView)convertView.findViewById(R.id.accountfinished);
			holder.accountFinishedLabel=(TextView)convertView.findViewById(R.id.status_label);
			holder.accountFinishedMoney=(TextView)convertView.findViewById(R.id.finishmoney);
			holder.accountFinishedDate=(TextView)convertView.findViewById(R.id.updated_at);
			convertView.setTag(holder);
		
		}else {
			holder = (AccountFinishedHolder) convertView.getTag();
		}
		holder.accountFinishedName.setText(items.getProjectName());
		holder.accountFinishedLabel.setText(items.getStatusLabel());
		holder.accountFinishedMoney.setText(Tool.toDeciDouble(items.getInvsetMoney())+"元");
		holder.accountFinishedDate.setText((Tool.unixTimeToDate(items.getUpDate(),"yyyy-MM-dd HH:mm")));
		return convertView;
	}
	private static class AccountFinishedHolder {
		private TextView accountFinishedName;
		private TextView accountFinishedLabel;
		private TextView accountFinishedMoney;
		private TextView accountFinishedDate;
	}

}
