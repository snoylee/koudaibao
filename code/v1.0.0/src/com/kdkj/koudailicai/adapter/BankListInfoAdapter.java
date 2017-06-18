package com.kdkj.koudailicai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.BankSupportInfo;
import com.kdkj.koudailicai.domain.TrasactionRecordKdbInfo;
import com.kdkj.koudailicai.util.Tool;

public class BankListInfoAdapter extends ArrayAdapter {
	private List<BankSupportInfo> bankList;
	private LayoutInflater layoutInflater = null;
	private Context mContext;
	
    public BankListInfoAdapter(Context context, int textViewResourceId, List itemList) {
        super(context, textViewResourceId, itemList);
        layoutInflater = LayoutInflater.from(context);
        bankList = itemList;
        mContext = context;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.bankList.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.bankList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong(this.bankList.get(position).getCode());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BankSupportInfo bankInfo = this.bankList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView=layoutInflater.inflate(R.layout.item_choose_bank_info,null);
			holder = new ViewHolder();
			holder.bankName = (TextView) convertView.findViewById(R.id.bankName);
	    	convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.bankName.setText(bankInfo.getName());
		if("0".equals(bankInfo.getCode())) {
			holder.bankName.setBackgroundResource(R.color.global_back_black_color);
			holder.bankName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		} else {
			holder.bankName.setBackgroundResource(R.color.global_white_color);
			holder.bankName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		}
		return convertView;
	}
	
    /**
     * 界面上的显示控件
     */
    private static class ViewHolder{
	    private TextView bankName;
    }

}
