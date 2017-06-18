package com.kdkj.koudailicai.adapter;

import java.util.List;

import com.kdkj.koudailicai.R;
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

public class KdbRollOutListAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<KdbRollOutListInfo> item;
	private int screenHeight;
	private int screenWidth;
	private double rollout_list_height = 0.091;
    
	public KdbRollOutListAdapter(Context context, int textViewResourceId,
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
		// TODO Auto-generated method stub
		KdbRollOutListInfo items=this.item.get(position);
		RollOutHolder holder=null;
		if(convertView==null)
		{
			convertView = layoutInflater.inflate(R.layout.activity_rollout_listinfo, null);
			RelativeLayout rolloutlistview=(RelativeLayout)convertView.findViewById(R.id.rolloutlistview);
			RelativeLayout.LayoutParams lastDayProfitsLayoutParams = (RelativeLayout.LayoutParams)rolloutlistview.getLayoutParams();
			lastDayProfitsLayoutParams.height = (int) (screenHeight*rollout_list_height);
			rolloutlistview.setLayoutParams(lastDayProfitsLayoutParams);
			holder=new RollOutHolder();
			holder.rolloutMoney=(TextView)convertView.findViewById(R.id.rolloutAccount);
			holder.rolloutDate=(TextView)convertView.findViewById(R.id.k_date);
			convertView.setTag(holder);
		
		}else {
			holder = (RollOutHolder) convertView.getTag();
		}
		holder.rolloutMoney.setText(Tool.toDeciDouble(items.getRolloutmoney())+"元");
		holder.rolloutDate.setText(Tool.unixTimeToDate(items.getRollouttime(),"yyyy-MM-dd HH:mm"));
		return convertView;
	}
	private static class RollOutHolder {
		private TextView rolloutMoney;
		private TextView rolloutDate;
	}

}
