package com.kdkj.koudailicai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.TenderConstructionInfo;
import com.kdkj.koudailicai.util.Tool;

public class TenderConstructionInfoAdapter extends ArrayAdapter{
	private List<TenderConstructionInfo> items;
	private LayoutInflater layoutInflater = null;
	private Context mContext;
	private final int VIEW_TYPE = 2;
	private final int TYPE_1 = 0;
	
	public TenderConstructionInfoAdapter(Context context, int textViewResourceId, List itemList) {
	        super(context, textViewResourceId, itemList);
	        layoutInflater = LayoutInflater.from(context);
	        items = itemList;
	        mContext = context;
	}
	 
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		TenderConstructionInfo tenderConstructionInfo = this.items.get(position);
		return tenderConstructionInfo != null ? tenderConstructionInfo.getInfoType() : 0;
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
		if(Tool.isBlank(this.items.get(position).getId())) 
			return 0;
		return Long.parseLong(this.items.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		TenderConstructionInfo item = this.items.get(position);
		int type = getItemViewType(position);
		if(type == TYPE_1){
			convertView = layoutInflater.inflate(R.layout.tender_invest_item, null);
		} else {
			if (convertView == null) {
				convertView=layoutInflater.inflate(R.layout.tender_construction_item,null);
				holder = new ViewHolder();
				holder.investMoney= (TextView) convertView.findViewById(R.id.tender_money);
				holder.tenderDate = (TextView) convertView.findViewById(R.id.tender_date);
				holder.tenderPhone = (TextView) convertView.findViewById(R.id.tender_phone);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.investMoney.setText(Tool.toDeciDouble(item.getInvest_money()));
			holder.tenderDate.setText(Tool.unixTimeToDate(item.getCreated_at(), "yyyy-MM-dd HH:mm"));
			holder.tenderPhone.setText(item.getUsername());
		}
		return convertView;
	}
	
	private static class ViewHolder{
	    private TextView investMoney;
	    private TextView tenderDate;
	    private TextView tenderPhone;
    }

}
