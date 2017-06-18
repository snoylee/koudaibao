package com.kdkj.koudailicai.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.TrasactionRecordKdbInfo;
import com.kdkj.koudailicai.util.Tool;

public class TrasactionRecordKdbInfoAdapter extends ArrayAdapter {
	private List<TrasactionRecordKdbInfo> recordList;
	private LayoutInflater layoutInflater = null;
	private Context mContext;
	
    public TrasactionRecordKdbInfoAdapter(Context context, int textViewResourceId, List itemList) {
        super(context, textViewResourceId, itemList);
        layoutInflater = LayoutInflater.from(context);
        recordList = itemList;
        mContext = context;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.recordList.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.recordList.get(position).getId();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TrasactionRecordKdbInfo recordInfo	= this.recordList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView=layoutInflater.inflate(R.layout.trasaction_record_list_item_h,null);
			holder = new ViewHolder();
			holder.investMoney = (TextView) convertView.findViewById(R.id.investMoney);
			holder.statusLabel = (TextView) convertView.findViewById(R.id.statusLabel);
			holder.projectName = (TextView) convertView.findViewById(R.id.project_name);
			holder.interestStartDate = (TextView) convertView.findViewById(R.id.interest_start_date);
	    	convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.investMoney.setText(Tool.toDeciDouble(recordInfo.getInvestMoney())+"元");
		holder.statusLabel.setText(recordInfo.getStatus());
		holder.projectName.setText("口袋宝");
		holder.interestStartDate.setText(Tool.unixTimeToDate(recordInfo.getCreatdAt(), "yyyy-MM-dd HH:mm"));
		return convertView;
	}
	
    /**
     * 界面上的显示控件
     */
    private static class ViewHolder{
	    private TextView investMoney;
	    private TextView statusLabel;
	    private TextView projectName;
	    private TextView interestStartDate;
    }

}
