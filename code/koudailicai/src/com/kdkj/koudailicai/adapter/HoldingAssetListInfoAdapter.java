package com.kdkj.koudailicai.adapter;


import java.util.List;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.HoldingAssetListInfoRecord;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferCancelActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferMoneyActivity;

import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutAccountActivity;
public class HoldingAssetListInfoAdapter extends BaseAdapter{
    private List<HoldingAssetListInfoRecord>  items;
    private LayoutInflater layoutInflater = null;
	private Context context;
	private String url;
	private String cessionName;
	
	//size
	private int screenHeight;
	private int screenWidth;
	
	//dimens
	private double paddingLeft = 0.0234;
	private double holdingTotalViewPaddingTop = 0.008;
	private double holdingTotalViewPaddingBottom = 0.051;
	private double holdingTotalViewHeight = 0.203;
	private double kdbInfoViewHeight = 0.104;
	private double holdingDueinProfitsViewHeight = 0.078;
	private double holdingDueinCapitalViewHeight = 0.078;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	private AlertDialog alterDialog;
    public HoldingAssetListInfoAdapter(Context context,List objects) {
    	screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = objects ;
    	Log.e("position--HoldingAssetListInfoAdapter----", this.items+"");
    }
    
	//控制每个item的布局
	@Override
	public int getItemViewType(int p) {
		if(items.get(p) != null && items.get(p).getInfoType() == 1)
			return TYPE_1;
		else
			return TYPE_2;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getCount() {
		Log.e("position--HoldingAssetListInfoAdapter--this.items.size()--", this.items.size()+"");
		return this.items.size();
		
	}
	@Override
	public Object getItem(int position) {
		Log.e("position--HoldingAssetListInfoAdapter--this.items.get(position)--", this.items.get(position)+"");
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AmountHolder amountHolder = null;
		DealHolder dealHolder = null;
    	final HoldingAssetListInfoRecord item = this.items.get(position);
    	int type = getItemViewType(position);
    	
    	if(convertView == null) {
    		//按当前所需的样式，确定new的布局
    		switch (type) {
			case TYPE_1:
				convertView =layoutInflater.inflate(R.layout.holdingasset_list_item_f,null);
				amountHolder = new AmountHolder();
				amountHolder.holding_asset_money = (NumChangeTextView)convertView.findViewById(R.id.holding_asset_money);
				amountHolder.total_holding_asset_money = (TextView)convertView.findViewById(R.id.total_holding_asset_money);
				//amountHolder.pay_holding_asset_money = (TextView)convertView.findViewById(R.id.pay_holding_asset_money);
				amountHolder.wait_collection_money = (TextView)convertView.findViewById(R.id.wait_collection_money);
				amountHolder.no_income_money = (TextView)convertView.findViewById(R.id.no_income_money);
				amountHolder.freeze_account = (TextView)convertView.findViewById(R.id.freeze_account);
				amountHolder.pay_out = (TextView)convertView.findViewById(R.id.pay_out);
				//适配View
				amountHolder.holdingTopView = (RelativeLayout)convertView.findViewById(R.id.holdingTopView);
				amountHolder.kdbInfoView = (RelativeLayout)convertView.findViewById(R.id.kdbInfoView);
				amountHolder.holdingDueinCapitalView = (RelativeLayout)convertView.findViewById(R.id.holdingDueinCapitalView);
				amountHolder.bottomView = (View)convertView.findViewById(R.id.bottomView);
				//amountHolder.holdingDueinProfitsView = (RelativeLayout)convertView.findViewById(R.id.holdingDueinProfitsView);
				//进行适配
/*				RelativeLayout.LayoutParams holdingTopViewLayoutParams = (RelativeLayout.LayoutParams) amountHolder.holdingTopView.getLayoutParams();
				holdingTopViewLayoutParams.height = (int) (screenHeight*holdingTotalViewHeight);
				amountHolder.holdingTopView.setPadding((int)(screenWidth*paddingLeft), (int)(screenHeight*holdingTotalViewPaddingTop), (int)(screenWidth*paddingLeft), (int)(screenHeight*holdingTotalViewPaddingBottom));
				amountHolder.holdingTopView.setLayoutParams(holdingTopViewLayoutParams);
				
				RelativeLayout.LayoutParams kdbInfoViewLayoutParams = (RelativeLayout.LayoutParams) amountHolder.kdbInfoView.getLayoutParams();
				kdbInfoViewLayoutParams.height = (int) (screenHeight*kdbInfoViewHeight);
				amountHolder.kdbInfoView.setLayoutParams(kdbInfoViewLayoutParams);
				amountHolder.kdbInfoView.setPadding((int)(screenWidth*paddingLeft), 0, (int)(screenWidth*paddingLeft), 0);

				RelativeLayout.LayoutParams holdingDueinProfitsViewLayoutParams = (RelativeLayout.LayoutParams) amountHolder.holdingDueinProfitsView.getLayoutParams();
				holdingDueinProfitsViewLayoutParams.height = (int) (screenHeight*holdingDueinProfitsViewHeight);
				amountHolder.holdingDueinProfitsView.setLayoutParams(holdingDueinProfitsViewLayoutParams);				
				amountHolder.holdingDueinProfitsView.setPadding((int)(screenWidth*paddingLeft), 0, (int)(screenWidth*paddingLeft), 0);

				RelativeLayout.LayoutParams holdingDueinCapitalViewLayoutParams = (RelativeLayout.LayoutParams) amountHolder.holdingDueinCapitalView.getLayoutParams();
				holdingDueinCapitalViewLayoutParams.height = (int) (screenHeight*holdingDueinCapitalViewHeight);
				amountHolder.holdingDueinCapitalView.setLayoutParams(holdingDueinCapitalViewLayoutParams);
				amountHolder.holdingDueinCapitalView.setPadding((int)(screenWidth*paddingLeft), 0, (int)(screenWidth*paddingLeft), 0);*/
				
				convertView.setTag(amountHolder);
				break;
			case TYPE_2:
				convertView = layoutInflater.inflate(R.layout.holdingasset_list_item_s,null);
		    	dealHolder = new DealHolder();
		    	dealHolder.holdingAssetView = (RelativeLayout)convertView.findViewById(R.id.holdingAssetView);
		    	dealHolder.projectInfoTopView = (RelativeLayout)convertView.findViewById(R.id.projectInfoTopView);
		    	dealHolder.projectInfoBottomView = (RelativeLayout)convertView.findViewById(R.id.projectInfoBottomView);
		    	dealHolder.projectName = (TextView) convertView.findViewById(R.id.projectName);
		    	dealHolder.projectStatus = (TextView) convertView.findViewById(R.id.projectStatus);
		    	dealHolder.projectApr = (TextView)convertView.findViewById(R.id.projectApr);
		    	dealHolder.interestStartDate = (TextView)convertView.findViewById(R.id.interestStartDate);
		    	dealHolder.statusBtn = (TextView) convertView.findViewById(R.id.statusBtn);
		    	dealHolder.investAccount = (TextView)convertView.findViewById(R.id.investAccount);
		    	dealHolder.duienProfit = (TextView)convertView.findViewById(R.id.duienProfit);
		    	dealHolder.duienCapital = (TextView) convertView.findViewById(R.id.duienCapital);
		    	dealHolder.lastDate = (TextView)convertView.findViewById(R.id.lastDate);
		    	//进行适配
/*				dealHolder.holdingAssetView.setPadding((int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft));
				dealHolder.projectInfoTopView.setPadding((int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2));
				dealHolder.projectInfoBottomView.setPadding((int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), (int)(screenWidth*paddingLeft/2), 0);*/
				convertView.setTag(dealHolder);
				break;
			default:
				break;
			}
    	}else{
    		switch (type) {
			case TYPE_1:
				amountHolder = (AmountHolder)convertView.getTag();
				break;
			case TYPE_2:
				dealHolder = (DealHolder)convertView.getTag();
				break;
			default:
				break;
			}
    	}
    	switch (type) {
		case TYPE_1:
				amountHolder.holding_asset_money.setNumText(Tool.toDeciDouble(item.getTotalHoldMoney()));
				amountHolder.total_holding_asset_money.setText(Tool.toDeciDouble(item.getKdbTotalMoney()));
				//long kdbInvestAccount = Long.parseLong(item.getKdbTotalMoney()) - Long.parseLong(item.getKdbTotalProfits());
				//amountHolder.pay_holding_asset_money.setText(context.getResources().getString(R.string.investmenttal)+" "+Tool.toDeciDouble(String.valueOf(kdbInvestAccount))+"（元）");
				amountHolder.wait_collection_money.setText(Tool.toDeciDouble(item.getKdbDueinCapital()));
				amountHolder.no_income_money.setText(Tool.toDeciDouble(item.getKdbDueinProfits()));
				amountHolder.freeze_account.setText(Tool.toDeciDouble(item.getKdbInvestingMoney()));
				if("0".equals(item.getKdbTotalMoney())) {
					amountHolder.pay_out.setText("转出记录");
					amountHolder.pay_out.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent traAccin = new Intent(context,KdbRollOutListActivity.class);
							context.startActivity(traAccin);
						}
					});
				} else {
					amountHolder.pay_out.setText("转出");
					amountHolder.pay_out.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent traAccin = new Intent(context,KdbRollOutAccountActivity.class);
							traAccin.putExtra("total_holding_asset_money", item.getKdbTotalMoney());
							context.startActivity(traAccin);
						}
					});
				}
				if(getCount() == 1) {
					amountHolder.bottomView.setVisibility(View.GONE);
				} else {
					amountHolder.bottomView.setVisibility(View.VISIBLE);
				}
				break;
		case TYPE_2:
			if(item.getBtnType().equals("2")){
				dealHolder.statusBtn.setVisibility(View.VISIBLE);
				dealHolder.statusBtn.setText("转让");
				dealHolder.statusBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					   Intent holdingAccin = new Intent(context,TransferMoneyActivity.class);
					   holdingAccin.putExtra("invest_id", item.getInvestId());
					   context.startActivity(holdingAccin);
					}
				});
			} else if(item.getBtnType().equals("3")) {
				
				dealHolder.statusBtn.setVisibility(View.VISIBLE);
				dealHolder.statusBtn.setText("取消");
				parseUrl();
				dealHolder.statusBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alterDialog=KdlcDialog.showConfirmDialog(context, false, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alterDialog.cancel();
								cessionName = item.getProjectName();
								BaseActivity baseActivity=new BaseActivity();
								alterDialog = KdlcDialog.showProgressDialog(context, "正在取消...");
//								baseActivity.setDialog(alterDialog);
								HttpParams params=new HttpParams();
								params.add("invest_id", item.getInvestId());
								((BaseActivity) context).sendHttpPost(url, params, responseListener,errListener);	
								
							}
						}, "是否取消转让");
					}
				});
			} else {
				dealHolder.statusBtn.setVisibility(View.INVISIBLE);
			}
			 dealHolder.projectName.setText(item.getProjectName());
			 dealHolder.projectStatus.setText("（"+item.getStatusLabel()+"）");
			 dealHolder.projectApr.setText(item.getProjectApr()+"%");
			 dealHolder.interestStartDate.setText("起息日："+item.getInerestAtartDate());
			 dealHolder.investAccount.setText(Tool.toDeciDouble(item.getDueinCapiTal())+"元");
			 dealHolder.duienProfit.setText(Tool.toDeciDouble(item.getDueinProfits())+"元");
			 Long totalMoney = Long.parseLong(item.getDueinProfits()) + Long.parseLong(item.getDueinCapiTal());
			 dealHolder.duienCapital.setText(Tool.toDeciDouble(String.valueOf(totalMoney))+"元");
			 dealHolder.lastDate.setText(item.getLastRepayDate());
			 break;
		default:
				break;
		}
    
		return convertView;
	}
	
	/**
    * 界面上的显示控件，第一个布局资源
    */
	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			url = KDLCApplication.app.getActionUrl(G.GCK_API_CREDIT_CANCEL_ASSIGNMENT);
		} else {
			url = G.URL_POST_CREDIT_CANCEL_ASSIGNMENT;
		}
	}
	
	private Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			alterDialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					Intent intent=new Intent();
					intent.putExtra("transferName", cessionName);
					intent.setClass(context, TransferCancelActivity.class);
					context.startActivity(intent);	
				} else {
					KdlcDialog.showInformDialog((BaseActivity) context, response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}
	};
	
	private ErrorListener errListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			alterDialog.cancel();
			if(NoConnectionError.class.isInstance(error)) {				
				KdlcDialog.showBottomToast("网络未连接，请检查网络设置后再试");
			} else {
				KdlcDialog.showBottomToast("网络出错，请稍后再试");
			}
		}
		
	};

	private static class AmountHolder {
		private RelativeLayout holdingTopView;
		private RelativeLayout kdbInfoView;
		private RelativeLayout holdingDueinProfitsView;
		private RelativeLayout holdingDueinCapitalView;
	   	private NumChangeTextView holding_asset_money;
	   	private TextView total_holding_asset_money;
	   	//private TextView pay_holding_asset_money;
	   	private TextView wait_collection_money;
	   	private TextView no_income_money;
	   	private TextView freeze_account;
	   	private TextView pay_out;
	   	private View bottomView;
	 }
	 
		//第二个布局资源
		private static class DealHolder{
			private RelativeLayout holdingAssetView;
			private RelativeLayout projectInfoTopView;
			private RelativeLayout projectInfoBottomView;
			
		   	private TextView projectName;
		   	private TextView projectStatus;
		   	private TextView projectApr;
		   	private TextView interestStartDate;
		   	private TextView statusBtn;
		   	private TextView investAccount;
		   	private TextView duienProfit;
		   	private TextView duienCapital;
		   	private TextView lastDate;

		}

}
