package com.kdkj.koudailicai.adapter;

import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.WithdrawListInfo;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WithdrawListAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<WithdrawListInfo> withdrawListInfo;
    private int screenHeight;
    private int screenWidth;
	public WithdrawListAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId,objects);
		// TODO Auto-generated constructor stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		withdrawListInfo = objects;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return withdrawListInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return withdrawListInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return withdrawListInfo.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		WithdrawListInfo item = this.withdrawListInfo.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_withdraw_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvWithdrawMoney = ((TextView) convertView.findViewById(R.id.tv_withdraw_money));
			viewHolder.tvWithdrawTitle = ((TextView) convertView.findViewById(R.id.tv_withdraw_title));
			viewHolder.tvWithdrawTime = ((TextView) convertView.findViewById(R.id.tv_withdraw_time));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvWithdrawMoney.setText(Tool.toDeciDouble(item.getMoney())+"元");
		viewHolder.tvWithdrawTitle.setText(item.getStatusLabel());
		viewHolder.tvWithdrawTime.setText(Tool.unixTimeToDate(item.getCreatedAt(), "yyyy-MM-dd HH:mm"));
		return convertView;
	}

	private static class ViewHolder {
		private TextView tvWithdrawMoney;
		private TextView tvWithdrawTitle;
		private TextView tvWithdrawTime;
	}
}
