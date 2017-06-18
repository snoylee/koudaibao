package com.kdkj.koudailicai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ProductListCessionInfo;
import com.kdkj.koudailicai.domain.TotalSelfProfitInfo;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;

public class TotalSelfProfitInfoAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<TotalSelfProfitInfo> totalProfitsList;
	private int screenHeight;
	private int screenWidth;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	
	public TotalSelfProfitInfoAdapter(Context context, int textViewResourceId, List objects) {
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		totalProfitsList = objects;
	}
	
	//控制每个item的布局
	@Override
	public int getItemViewType(int position) {
		TotalSelfProfitInfo profitInfo = this.totalProfitsList.get(position);
		return profitInfo != null ? profitInfo.getInfoType() : 0;	
	}

	@Override
	public int getViewTypeCount() {
		return  VIEW_TYPE;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.totalProfitsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.totalProfitsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.totalProfitsList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TotalSelfProfitInfo item = this.totalProfitsList.get(position);
		int type = getItemViewType(position);
		if(type == TYPE_1) {
			AccountHolder accountHolder = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.totalselfprofit, null);
				RelativeLayout totalmoney = (RelativeLayout) convertView.findViewById(R.id.totalmoney);
				RelativeLayout.LayoutParams totalmoneyLayoutParams = (RelativeLayout.LayoutParams) totalmoney.getLayoutParams();
				totalmoneyLayoutParams.height = (int) (screenHeight * 0.22);
				totalmoney.setLayoutParams(totalmoneyLayoutParams);
				accountHolder = new AccountHolder();
				accountHolder.transferAccumulatedCount = ((NumChangeTextView) convertView.findViewById(R.id.remaindermoney));
				convertView.setTag(accountHolder);
			} else {
				accountHolder = (AccountHolder) convertView.getTag();
			}
			accountHolder.transferAccumulatedCount.setNumText(Tool.toDeciDouble(item.getTotal_profits()));
		} else {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.totalselfprofitinfo, null);
				RelativeLayout totallistview = (RelativeLayout) convertView.findViewById(R.id.totallistview);
				RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams) totallistview.getLayoutParams();
				lastDayProfitsLayoutParams.height = (int) (screenHeight * 0.09);
				totallistview.setLayoutParams(lastDayProfitsLayoutParams);
				viewHolder = new ViewHolder();
				viewHolder.periodLabel = ((TextView) convertView.findViewById(R.id.k_list));
				viewHolder.minInvestAccountLabel = ((TextView) convertView.findViewById(R.id.profitsAccount));
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.periodLabel.setText(item.getProject_name());
			viewHolder.minInvestAccountLabel.setText(Tool.toDeciDouble(item.getTotal_money()));
		}
		return convertView;
	}

	private static class AccountHolder {
		private NumChangeTextView transferAccumulatedCount;
	}

	private static class ViewHolder {
		private TextView periodLabel;
		private TextView minInvestAccountLabel;
	}

}
