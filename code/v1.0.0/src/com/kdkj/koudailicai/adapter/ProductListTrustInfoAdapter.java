package com.kdkj.koudailicai.adapter;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ProductListTrustInfo;
import com.kdkj.koudailicai.lib.ui.RoundProgressBar;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;

public class ProductListTrustInfoAdapter extends
		ArrayAdapter<ProductListTrustInfo> {

	private static final String TAG = ProductListTrustInfoAdapter.class
			.getName();
	private List<ProductListTrustInfo> productListTrustItem;
	private LayoutInflater layoutInflater;
	private Context mContext;
	private int screenWidth;
	private int screenHeight;
    private Handler mHandler=new Handler();
    private int progress=0;
    private int curCount=0;
	public ProductListTrustInfoAdapter(Context context, int textViewResourceId,
			List<ProductListTrustInfo> productListTrustItem) {
		super(context, textViewResourceId,productListTrustItem);
		this.productListTrustItem = productListTrustItem;
		this.mContext = context;
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.productListTrustItem.size();
	}

	@Override
	public ProductListTrustInfo getItem(int position) {
		// TODO Auto-generated method stub
		return this.productListTrustItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.productListTrustItem.get(position).getProductID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		ProductListTrustInfo item = this.productListTrustItem.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.projecttrustlist, null);
			holder = new ViewHolder();
			holder.listView = (RelativeLayout) convertView.findViewById(R.id.list);
			holder.productStatusInfo = (TextView) convertView.findViewById(R.id.productStatusInfo);
			holder.productRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.productroundProgressBar);
			holder.typeImg = (RelativeLayout) convertView.findViewById(R.id.typeImg);
			holder.productName = (TextView) convertView.findViewById(R.id.productName);
			holder.apr = (TextView) convertView.findViewById(R.id.apr);
			holder.investNum = (TextView) convertView.findViewById(R.id.investNum);
			holder.period = (TextView) convertView.findViewById(R.id.period);
			holder.minInvestAccount = (TextView) convertView.findViewById(R.id.minInvestAccount);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams listParams = (LayoutParams) holder.listView.getLayoutParams();
		listParams.height = (int) (screenHeight * (1 - 0.08 - 0.09 - 0.075) / 4);
		holder.listView.setLayoutParams(listParams);
		RelativeLayout.LayoutParams iconParams = (LayoutParams) holder.typeImg.getLayoutParams();
		iconParams.width = (int) (screenWidth * 0.2);
		iconParams.height = (int) (screenWidth * 0.2);
		holder.typeImg.setLayoutParams(iconParams);
		holder.productName.setText(item.getName());
		holder.apr.setText(item.getApr() + "%");
		holder.investNum.setText(item.getSuccessNumber() + "人");
		holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_red_color));
		if (item.getStatus().equals(G.PRODUCT_STATUS.STATUS_REPAYED)) {
			holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_label_color));
			holder.productStatusInfo.setText("已还款");
			holder.productRoundProgressBar.setMax(100);
			holder.productRoundProgressBar.setProgress(100);
		} else if (item.getStatus().equals(G.PRODUCT_STATUS.STATUS_REPAYING)) {
			holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_label_color));			
			holder.productStatusInfo.setText("还款中");
			holder.productRoundProgressBar.setMax(100);
			holder.productRoundProgressBar.setProgress(100);
		} else {
			final int progressInt;
			if(Double.parseDouble(item.getSuccessMoney())
					/ Double.parseDouble(item.getTotalMoney()) * 100<100&&Math.ceil(Double.parseDouble(item
							.getSuccessMoney())
							/ Double.parseDouble(item.getTotalMoney()) * 100)==100) {
				progressInt=99;
			} else {
				progressInt=(int) Math.ceil(Double.parseDouble(item.getSuccessMoney())
										  / Double.parseDouble(item.getTotalMoney()) * 100);
			}
			if(curCount<3) {
				if (progressInt < 100) {
					holder.productRoundProgressBar.setAnimProgress(progressInt);
				} else {
					holder.productRoundProgressBar.setProgress(progressInt);
				}
			} else {
				holder.productRoundProgressBar.setProgress(progressInt);
			}
			holder.productStatusInfo.setText("" + progressInt + "%");
			holder.productRoundProgressBar.setMax(100);
//			holder.productRoundProgressBar.setProgress(progressInt);
          
		}
		holder.period.setText(item.getPeriod()
				+ (item.getIsDay().equals("0") ? "个月" : "天"));
		holder.minInvestAccount.setText(Tool.toIntAccount(item
				.getMinInvestMoney()) + "元");

		if (item.getTotalMoney().equals(item.getSuccessMoney())) {
			holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_label_color));
			holder.statusIcon.setVisibility(View.VISIBLE);
			holder.statusIcon.setBackgroundResource(R.drawable.grey_jiaobiao);
			holder.status.setVisibility(View.VISIBLE);
			holder.status.setText("完");
		} else {
			if (item.getIsNovice().equals("1")) {
				holder.status.setVisibility(View.VISIBLE);
				holder.statusIcon.setVisibility(View.VISIBLE);
				holder.statusIcon.setBackgroundResource(R.drawable.jiaobiao);
				holder.status.setText("新");
			} else {
				holder.status.setVisibility(View.GONE);
				holder.statusIcon.setVisibility(View.GONE);
			}
		}
		curCount++;
		return convertView;
	}
	public int getCurCount() {
		return curCount;
	}

	public void setCurCount(int curCount) {
		this.curCount = curCount;
	}
	private static class ViewHolder {

		private RelativeLayout listView;
		private TextView productStatusInfo;
		private RoundProgressBar productRoundProgressBar;
		private RelativeLayout typeImg;
		private TextView productName;
		private TextView apr;
		private TextView investNum;
		private TextView period;
		private TextView minInvestAccount;
		private TextView status;
		private ImageView statusIcon;
	}
}
