package com.kdkj.koudailicai.adapter;

import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.PaymentDetailsInfo;
import com.kdkj.koudailicai.util.Tool;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kdkj.koudailicai.view.KDLCApplication;
public class PaymentDetailsAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<PaymentDetailsInfo> paymentDetailsInfo;
    private int screenHeight;
    private int screenWidth;
	public PaymentDetailsAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId,objects);
		// TODO Auto-generated constructor stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		paymentDetailsInfo = objects;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return paymentDetailsInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return paymentDetailsInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return paymentDetailsInfo.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PaymentDetailsInfo item = this.paymentDetailsInfo.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			
				convertView = layoutInflater.inflate(R.layout.activity_payment_details_info, null);
				RelativeLayout payment = (RelativeLayout) convertView.findViewById(R.id.payment);
				RelativeLayout.LayoutParams paymentLayoutParams = (RelativeLayout.LayoutParams) payment.getLayoutParams();
				paymentLayoutParams.height = (int) (screenHeight * 0.09);
				payment.setLayoutParams(paymentLayoutParams);
				viewHolder = new ViewHolder();
				viewHolder.title = ((TextView) convertView.findViewById(R.id.tv_payment_details_title));
				viewHolder.time = ((TextView) convertView.findViewById(R.id.tv_payment_details_time));
				viewHolder.mark = ((TextView) convertView.findViewById(R.id.tv_payment_details_mark));
				viewHolder.number = ((TextView) convertView.findViewById(R.id.tv_payment_details_number));
				convertView.setTag(viewHolder);

		} else {
				viewHolder = (ViewHolder) convertView.getTag();
		}
			viewHolder.title.setText(item.getTitle());
			viewHolder.time.setText(Tool.unixTimeToDate(item.getCreatedAt(), "yyyy-MM-dd HH:mm:ss").substring(0, 16));
			viewHolder.mark.setText(item.getTag());
			if (item.getTag().equals("+")) {
				viewHolder.mark.setTextColor(Color.rgb(255, 0, 0));
				//viewHolder.number.setText(Tool.toDeciDouble(item.getMoney()));
				//viewHolder.number.setTextColor(Color.rgb(255, 0, 0));
			}else {
				viewHolder.mark.setTextColor(Color.rgb(1, 179, 93));
				//viewHolder.number.setText(Tool.toDeciDouble(item.getMoney()));
				//viewHolder.number.setTextColor(Color.rgb(1, 179, 93));
			}
			viewHolder.number.setText(Tool.toDeciDouble(item.getMoney()));

		return convertView;
	}

	private static class ViewHolder {
		private TextView title;
		private TextView time;
		private TextView mark;
		private TextView number;
	}
}
