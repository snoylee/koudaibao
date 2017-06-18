package com.kdkj.koudailicai.adapter;

import java.util.List;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.EarningsYesterdayInfo;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
public class EarningsYesterdayAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<EarningsYesterdayInfo> earningsYesterdayInfo;
    private int screenHeight;
    private int screenWidth;
    final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	public EarningsYesterdayAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		earningsYesterdayInfo = objects;
	}

	//控制每个item的布局
	@Override
	public int getItemViewType(int position) {
		EarningsYesterdayInfo info = this.earningsYesterdayInfo.get(position);
		return info != null ? info.getInfoType() : 0;	
	}

	@Override
	public int getViewTypeCount() {
		return  VIEW_TYPE;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return earningsYesterdayInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return earningsYesterdayInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return earningsYesterdayInfo.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		EarningsYesterdayInfo item = this.earningsYesterdayInfo.get(position);
		AccountHolder accountHolder = null;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (position == 0) {
				convertView = layoutInflater.inflate(R.layout.activity_earnings,null);
				RelativeLayout rl_earnings = (RelativeLayout) convertView.findViewById(R.id.rl_earnings);
				RelativeLayout.LayoutParams rl_earningsLayoutParams = (RelativeLayout.LayoutParams) rl_earnings.getLayoutParams();
				rl_earningsLayoutParams.height = (int) (screenHeight * 0.22);
				rl_earnings.setLayoutParams(rl_earningsLayoutParams);
				accountHolder = new AccountHolder();
				//accountHolder.tv_earnings_title = ((TextView) convertView.findViewById(R.id.tv_earnings_title));
				accountHolder.tv_earnings_money = (NumChangeTextView) convertView.findViewById(R.id.tv_earnings_money);
				convertView.setTag(accountHolder);
			} else {
				convertView = layoutInflater.inflate(R.layout.activity_earnings_yesterday_info, null);
				RelativeLayout rl_earnings_info = (RelativeLayout) convertView.findViewById(R.id.rl_earnings_info);
				RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams) rl_earnings_info.getLayoutParams();
				lastDayProfitsLayoutParams.height = (int) (screenHeight * 0.09);
				rl_earnings_info.setLayoutParams(lastDayProfitsLayoutParams);
				viewHolder = new ViewHolder();
				viewHolder.tv_earnings_info_title = ((TextView) convertView.findViewById(R.id.tv_earnings_info_title));
				//viewHolder.tv_earnings_info_time = ((TextView) convertView.findViewById(R.id.tv_earnings_info_time));
				viewHolder.tv_earnings_info_number = ((TextView) convertView.findViewById(R.id.tv_earnings_info_number));
				convertView.setTag(viewHolder);
			}

		} else {
			if (position == 0) {
				accountHolder = (AccountHolder) convertView.getTag();
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

		}
		if (position == 0) {
			//accountHolder.tv_earnings_title.setText(Tool.unixTimeToDate(item.getLastdate(), "MM月dd日收益（元）"));
			Log.v("74878978978997", Tool.toDeciDouble(item.getLastday_profits())+"");
			accountHolder.tv_earnings_money.setNumText(Tool.toDeciDouble(item.getLastday_profits()));
		} else {
			viewHolder.tv_earnings_info_title.setText(item.getProject_name());
			//viewHolder.tv_earnings_info_time.setText(item.getDate());
			viewHolder.tv_earnings_info_number.setText(Tool.toDeciDouble(item.getTotal_profits()));
		}
		return convertView;
	}

	private static class AccountHolder {
		//private TextView tv_earnings_title;
		private NumChangeTextView tv_earnings_money;
	}

	private static class ViewHolder {
		private TextView tv_earnings_info_title;
		//private TextView tv_earnings_info_time;
		private TextView tv_earnings_info_number;
	}

}

