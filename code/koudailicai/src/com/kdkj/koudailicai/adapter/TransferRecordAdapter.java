package com.kdkj.koudailicai.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.CessionTradeInfo;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;
public class TransferRecordAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<CessionTradeInfo> transferRecord;
	private float textSize;
	private int screenHeight;
    private int screenWidth;
    
    final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	public TransferRecordAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		transferRecord = objects;
	}
	
	//控制每个item的布局
	@Override
	public int getItemViewType(int position) {
		CessionTradeInfo info = this.transferRecord.get(position);
		return info != null ? info.getInfoType() : 0;	
	}

	@Override
	public int getViewTypeCount() {
		return  VIEW_TYPE;
	}
	
	@Override
	public int getCount() {
		return transferRecord.size();
	}

	@Override
	public Object getItem(int position) {
		return transferRecord.get(position);
	}

	public long getItemId(int position) {
		return transferRecord.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CessionTradeInfo item = this.transferRecord.get(position);
		AccountHolder accountHolder = null;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (getItemViewType(position) == 0) {
				convertView = layoutInflater.inflate(R.layout.transfer_record_info, null);
				accountHolder = new AccountHolder();
				accountHolder.tvTotalNumber = ((TextView) convertView.findViewById(R.id.tv_total_number));
				convertView.setTag(accountHolder);
			} else {
				convertView = layoutInflater.inflate(R.layout.transfer_record_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvItemTime = ((TextView) convertView.findViewById(R.id.tv_item_time));
				viewHolder.tvItemMoney = ((TextView) convertView.findViewById(R.id.tv_item_money));
				viewHolder.tvItemInterestRate = ((TextView) convertView.findViewById(R.id.tv_item_interest_rate));
				convertView.setTag(viewHolder);
			}
		} else {
			if (getItemViewType(position) == 0) {
				accountHolder = (AccountHolder) convertView.getTag();
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

		}
		if (getItemViewType(position) == 0) {
			accountHolder.tvTotalNumber.setText(item.getCreditItemsCount());
		} else {
			viewHolder.tvItemTime.setText(item.getTradeTime().substring(0, 16));
			viewHolder.tvItemMoney.setText(Tool.toDeciDouble(item.getDueinCapital()));
			viewHolder.tvItemInterestRate.setText(item.getProjectApr()+"%");
		}
		return convertView;
	}
	
	private static class AccountHolder {
		private TextView tvTotalNumber;
	}

	private static class ViewHolder {
		private TextView tvItemTime;
		private TextView tvItemMoney;
		private TextView tvItemInterestRate;
	}
}
