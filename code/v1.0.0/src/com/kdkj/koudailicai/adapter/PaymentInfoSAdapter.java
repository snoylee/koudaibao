package com.kdkj.koudailicai.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.PaymentDetailsInfo;
import com.kdkj.koudailicai.util.Tool;

public class PaymentInfoSAdapter extends BaseAdapter {
	private ArrayList<PaymentDetailsInfo> items;

	public PaymentInfoSAdapter() {
		super();

	}
	public PaymentInfoSAdapter(ArrayList<PaymentDetailsInfo> items) {
		this.items = items;
	}
	@Override
	public int getCount() {
		return this.items.size();
	}
	@Override
	public Object getItem(int position) {

		return this.items.get(position);
	}
	@Override
	public long getItemId(int position) {

		return this.items.get(position).getId();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		// TrasactionRecordInfoD item=this.items.get(position);
		if (convertView == null) {
			if (items != null) {
				// 根据转台status进行判断调试
				LayoutInflater inflater = LayoutInflater.from(((View) parent).getContext());
				view = inflater.inflate(R.layout.activity_payment_details_info,null);
				TextView titleS = (TextView) view.findViewById(R.id.tv_payment_details_title);
				TextView timeS = (TextView) view.findViewById(R.id.tv_payment_details_time);
				TextView tagS = (TextView) view.findViewById(R.id.tv_payment_details_mark);
				TextView moneyS = (TextView) view.findViewById(R.id.tv_payment_details_number);
				PaymentDetailsInfo item = this.items.get(position);
				titleS.setText(item.getTitle());
				timeS.setText(Tool.unixTimeToDate(item.getCreatedAt(),"yyyy-MM-dd HH:mm"));
				tagS.setText(item.getTag());
				moneyS.setText(Tool.toDeciDouble(item.getMoney())+"元");
			} else {
				LayoutInflater inflater = LayoutInflater.from(((View) parent).getContext());
				view = inflater.inflate(R.layout.activity_payment_details_info,null);
			}
		} else {
			view = convertView;
		}
		return view;
	}

}
