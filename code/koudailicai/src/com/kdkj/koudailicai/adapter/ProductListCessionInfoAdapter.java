package com.kdkj.koudailicai.adapter;

import java.util.List;









import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.CessionTradeInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.domain.ProductListCessionInfo;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferMoneyActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferRecordActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.kdkj.koudailicai.view.invest.InvestChargeActivity;
import com.kdkj.koudailicai.view.product.ProductDetailActivity;


public class ProductListCessionInfoAdapter extends ArrayAdapter{
    private LayoutInflater layoutInflater = null;
	private List<ProductListCessionInfo> transferZoneitem;
	private Context context;
	
	final int VIEW_TYPE = 3;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	final int TYPE_3 = 2;
			
    public ProductListCessionInfoAdapter(Context context, int textViewResourceId, List objects) {
        super(context, textViewResourceId, objects);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        transferZoneitem = objects;
    }
	//控制每个item的布局
	@Override
	public int getItemViewType(int p) {
		ProductListCessionInfo curCessionInfo = this.transferZoneitem.get(p);
		return curCessionInfo != null ? curCessionInfo.getInfoType() : 0;	
	}

	@Override
	public int getViewTypeCount() {
		
		return  VIEW_TYPE;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return this.transferZoneitem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.transferZoneitem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.transferZoneitem.get(position).getId();
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	AccountHolder accountHolder = null;
    	DealHolder dealHolder = null;
    	ViewHolder holder = null;
    	int type = getItemViewType(position);
    	final ProductListCessionInfo transInfo = this.transferZoneitem.get(position);
    	if(convertView == null){
    		//按当前所需的样式，确定new的布局
    		switch (type) {
			case TYPE_1:
				convertView =layoutInflater.inflate(R.layout.cession_list_total_info,null);
	    		accountHolder = new AccountHolder();
	    		accountHolder.Transfer_accumulated_count = (TextView)convertView.findViewById(R.id.Transfer_accumulated_count);
	    		accountHolder.Transfer_accumulated_amount = (TextView)convertView.findViewById(R.id.Transfer_accumulated_amount);
				convertView.setTag(accountHolder);
				break;
			case TYPE_2:
				convertView = layoutInflater.inflate(R.layout.cession_list_record_info,null);
		    	dealHolder = new DealHolder();
		    	dealHolder.recordTitle = (LinearLayout)convertView.findViewById(R.id.recordTitle);
		    	dealHolder.firstTime = (TextView)convertView.findViewById(R.id.firstTime);
		    	dealHolder.firstAccount = (TextView)convertView.findViewById(R.id.firstAccount);
		    	dealHolder.firstApr = (TextView)convertView.findViewById(R.id.firstApr);
		    	dealHolder.secondTime = (TextView)convertView.findViewById(R.id.secondTime);
		    	dealHolder.secondAccount = (TextView)convertView.findViewById(R.id.secondAccount);
		    	dealHolder.secondApr = (TextView)convertView.findViewById(R.id.secondApr);
		    	dealHolder.thirdTime = (TextView)convertView.findViewById(R.id.thirdTime);
		    	dealHolder.thirdAccount = (TextView)convertView.findViewById(R.id.thirdAccount);
		    	dealHolder.thirdApr = (TextView)convertView.findViewById(R.id.thirdApr);
		    	dealHolder.recordView = (RelativeLayout)convertView.findViewById(R.id.recordView);
		    	dealHolder.firstRecord = (LinearLayout)convertView.findViewById(R.id.firstRecord);
		    	dealHolder.secondRecord = (LinearLayout)convertView.findViewById(R.id.secondRecord);
		    	dealHolder.thirdRecord = (LinearLayout)convertView.findViewById(R.id.thirdRecord);
		    	dealHolder.noOracle = (TextView)convertView.findViewById(R.id.noOracle);
		    	dealHolder.transor_list_one = (ImageView)convertView.findViewById(R.id.tradeRecordIcon);
		    	dealHolder.itemLabel = (TextView)convertView.findViewById(R.id.itemLabel);
		    	convertView.setTag(dealHolder);
				break;
			case TYPE_3:
				convertView=layoutInflater.inflate(R.layout.cession_list_product_info,null);
	    		holder = new ViewHolder();
	    		holder.noData = (TextView)convertView.findViewById(R.id.noData);
	    		holder.infoView = (RelativeLayout)convertView.findViewById(R.id.infoView);
    		    holder.transfer_project_name = (TextView)convertView.findViewById(R.id.transfer_project_name);
    		    holder.transfer_type_name = (TextView)convertView.findViewById(R.id.transfer_type_name); 
    		    holder.assgin_fee = (TextView)convertView.findViewById(R.id.assgin_fee);
	            holder.assgin_rate = (TextView)convertView.findViewById(R.id.assgin_rate);
	            holder.rest_days = (TextView)convertView.findViewById(R.id.rest_days);
	            holder.apr = (TextView)convertView.findViewById(R.id.apr);
	            holder.apr.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
	            holder.apr.getPaint().setAntiAlias(true);
	            holder.atonce_buy = (TextView)convertView.findViewById(R.id.atonce_buy);
	            convertView.setTag(holder);
				break;
			default:
				break;
			}
    	}else{
    		switch (type) {
			case TYPE_1:
				accountHolder = (AccountHolder)convertView.getTag();
				break;
			case TYPE_2:
				dealHolder = (DealHolder)convertView.getTag();
				break;
			case TYPE_3:
				holder = (ViewHolder)convertView.getTag();
				break;
			default:
				break;
			}
    	}
    	switch (type) {
		case TYPE_1:
			 accountHolder.Transfer_accumulated_count.setText(transInfo.getAccumulatedCount());
    		 accountHolder.Transfer_accumulated_amount.setText(Tool.toDeciDouble(transInfo.getAccumulatedAmount()));
			break;
		case TYPE_2:
		     dealHolder.recordView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent traAccin = new Intent(context,TransferRecordActivity.class); 
					context.startActivity(traAccin);
				}
		     });
			 List<CessionTradeInfo> dealList = transInfo.getDealList();
			 switch (dealList.size()) {
				case 0:
					 dealHolder.noOracle.setVisibility(View.VISIBLE);
					 dealHolder.firstRecord.setVisibility(View.GONE);
					 dealHolder.secondRecord.setVisibility(View.GONE);
					 dealHolder.thirdRecord.setVisibility(View.GONE);
					 dealHolder.transor_list_one.setVisibility(View.GONE);
					 dealHolder.itemLabel.setVisibility(View.VISIBLE);
					 dealHolder.recordView.setOnClickListener(null);
					break;
				case 1:
					 dealHolder.noOracle.setVisibility(View.GONE);
					 dealHolder.firstRecord.setVisibility(View.VISIBLE);
					 dealHolder.secondRecord.setVisibility(View.GONE);
					 dealHolder.thirdRecord.setVisibility(View.GONE);
					 dealHolder.transor_list_one.setVisibility(View.VISIBLE);
					 dealHolder.itemLabel.setVisibility(View.VISIBLE);
					break;
				case 2:
					 dealHolder.noOracle.setVisibility(View.GONE);
					 dealHolder.firstRecord.setVisibility(View.VISIBLE);
					 dealHolder.secondRecord.setVisibility(View.VISIBLE);
					 dealHolder.thirdRecord.setVisibility(View.GONE);
					 dealHolder.transor_list_one.setVisibility(View.VISIBLE);
					 dealHolder.itemLabel.setVisibility(View.VISIBLE);
					break;	
				default:
					 dealHolder.noOracle.setVisibility(View.GONE);
					 dealHolder.firstRecord.setVisibility(View.VISIBLE);
					 dealHolder.secondRecord.setVisibility(View.VISIBLE);
					 dealHolder.thirdRecord.setVisibility(View.VISIBLE);
					 dealHolder.transor_list_one.setVisibility(View.VISIBLE);
					 dealHolder.itemLabel.setVisibility(View.VISIBLE);
					break;
			 }
			 for(int i = 0; i <dealList.size(); i++) {
				 if(i == 0) {
					 dealHolder.firstTime.setText(dealList.get(i).getTradeTime().substring(0, 16));
					 dealHolder.firstAccount.setText(Tool.toDeciDouble(dealList.get(i).getDueinCapital()));
					 dealHolder.firstApr.setText(dealList.get(i).getProjectApr()+"%"); 
				 } else if(i == 1) {
					 dealHolder.secondTime.setText(dealList.get(i).getTradeTime().substring(0, 16));
					 dealHolder.secondAccount.setText(Tool.toDeciDouble(dealList.get(i).getDueinCapital()));
					 dealHolder.secondApr.setText(dealList.get(i).getProjectApr()+"%");
				 }else if(i == 2) {
					 dealHolder.thirdTime.setText(dealList.get(i).getTradeTime().substring(0, 16));
					 dealHolder.thirdAccount.setText(Tool.toDeciDouble(dealList.get(i).getDueinCapital()));
					 dealHolder.thirdApr.setText(dealList.get(i).getProjectApr()+"%");
					 break;
				 }
			 }
			break;
		case TYPE_3:
			 if("1".equals(transInfo.getNoCession())) {
				 holder.noData.setVisibility(View.VISIBLE);
				 holder.infoView.setVisibility(View.GONE);
			 } else {
				 holder.noData.setVisibility(View.GONE);
				 holder.infoView.setVisibility(View.VISIBLE);
				 holder.transfer_project_name.setText(transInfo.getName());
				 holder.transfer_type_name.setText(transInfo.getUserName());
	    		 holder.assgin_fee.setText(Tool.toDeciDouble(transInfo.getAssginFee()));
	    		 holder.assgin_rate.setText(transInfo.getAssginRate()+"%");
	    		 holder.rest_days.setText(transInfo.getRestDays()+"天");
	    		 holder.apr.setText(transInfo.getApr()+"%");
	    		 holder.atonce_buy.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, InvestChargeActivity.class);
						Bundle bundle = new Bundle();
						ProductBaseInfo productBaseInfo = new ProductBaseInfo();
						productBaseInfo.getFromCessionInfo(transInfo);
						bundle.putParcelable(G.PRODUCT_INFO_SER_KEY, productBaseInfo);
						bundle.putString("investAccount",Tool.toDeciDouble(transInfo.getAssginFee()));
						intent.putExtras(bundle); 
						((MainActivity) context).startActivityNeedLogin(intent);
					}
				}); 
			 }

			break;
		default:
			break;
		}
        return convertView;
    }
    
    /**
     * 界面上的显示控件，第一个布局资源
     */
    private static class AccountHolder{
	   	private TextView Transfer_accumulated_count;
	   	private TextView Transfer_accumulated_amount;
	   	//适配的View
	   	private LinearLayout lin_transfer_one;
	   
	    
    }
    //第二个布局资源
    private static class DealHolder{
    	
    	private RelativeLayout recordView;
    	private LinearLayout firstRecord;
    	private LinearLayout recordTitle;
    	private TextView noOracle;
	   	private TextView firstTime;
	   	private TextView firstAccount;
	   	private TextView firstApr;
	   	
	   	private LinearLayout secondRecord;
	   	private TextView secondTime;
	   	private TextView secondAccount;
	   	private TextView secondApr;
	   	private LinearLayout thirdRecord;
	   	private TextView thirdTime;
	   	private TextView thirdAccount;
	   	private TextView thirdApr;  
	   	private ImageView transor_list_one;
	   	private TextView itemLabel;
    }
    //第三个布局资源
    private static class ViewHolder{
    	private RelativeLayout infoView;
    	private TextView transfer_project_name;
    	private TextView transfer_type_name;
	    private TextView assgin_fee;
	   	private TextView assgin_rate;
	    private TextView rest_days;
	    private TextView apr;
	    private TextView atonce_buy;
	    private TextView noData;
	    
    }

}
