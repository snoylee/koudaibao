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

public class PaymentInfoZAdapter extends BaseAdapter {

	private ArrayList<PaymentDetailsInfo> itemz;

	public PaymentInfoZAdapter() {
		super();

	}
	public PaymentInfoZAdapter(ArrayList<PaymentDetailsInfo> itemz) {
		this.itemz = itemz;
	}
	@Override
	public int getCount() {
		return this.itemz.size();
	}
	@Override
	public Object getItem(int position) {

		return this.itemz.get(position);
	}
	@Override
	public long getItemId(int position) {

		return this.itemz.get(position).getId();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		// TrasactionRecordInfoD item=this.itemz.get(position);
		if (convertView == null) {
			if (itemz != null) {
				// 根据转台status进行判断调试
				LayoutInflater inflater = LayoutInflater.from(((View) parent).getContext());
				view = inflater.inflate(R.layout.activity_payment_details_info,null);
				TextView titleZ = (TextView) view.findViewById(R.id.tv_payment_details_title);
				TextView timeZ = (TextView) view.findViewById(R.id.tv_payment_details_time);
				TextView tagZ = (TextView) view.findViewById(R.id.tv_payment_details_mark);
				TextView moneyZ = (TextView) view.findViewById(R.id.tv_payment_details_number);
				PaymentDetailsInfo item = this.itemz.get(position);
				titleZ.setText(item.getTitle());
				timeZ.setText(Tool.unixTimeToDate(item.getCreatedAt(),"yyyy-MM-dd HH:mm"));
				tagZ.setText(item.getTag());
				moneyZ.setText(Tool.toDeciDouble(item.getMoney())+"元");
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

