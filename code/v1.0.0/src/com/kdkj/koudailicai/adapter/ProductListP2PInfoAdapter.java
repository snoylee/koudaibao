package com.kdkj.koudailicai.adapter;

import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ProductListP2PInfo;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.RoundProgressBar;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductListP2PInfoAdapter extends ArrayAdapter {
	private LayoutInflater layoutInflater = null;
	private List<ProductListP2PInfo> items;
	private int screenHeight;
	private int screenWidth;
	private double product_list_height = 0.185;
	private RelativeLayout list;
	private Context mContext;
	private int progress=0;
	private Handler handler;
	private static final int UPDATE = 0;
	private int curCount=0;
	public ProductListP2PInfoAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		items = objects;
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.items.get(position).getProductID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ProductListP2PInfo item = this.items.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.productlist_p2p_info,
					null);
			holder = new ViewHolder();
			holder.listView = (RelativeLayout) convertView
					.findViewById(R.id.list);
			holder.kdbIcon = (ImageView) convertView.findViewById(R.id.kdbIcon);
			holder.productStatusInfo = (TextView) convertView
					.findViewById(R.id.productStatusInfo);
			holder.productRoundProgressBar = (RoundProgressBar) convertView
					.findViewById(R.id.productroundProgressBar);
			holder.typeImg = (RelativeLayout) convertView
					.findViewById(R.id.typeImg);
			holder.productName = (TextView) convertView
					.findViewById(R.id.productName);
			holder.aprLabel = (TextView) convertView
					.findViewById(R.id.aprLabel);
			holder.apr = (TextView) convertView.findViewById(R.id.apr);
			holder.investNum = (TextView) convertView
					.findViewById(R.id.investNum);
			holder.period = (TextView) convertView.findViewById(R.id.period);
			holder.periodLabel = (TextView) convertView
					.findViewById(R.id.periodLabel);
			holder.minInvestAccount = (TextView) convertView
					.findViewById(R.id.minInvestAccount);
			holder.minInvestAccountLabel = (TextView) convertView
					.findViewById(R.id.minInvestAccountLabel);
			holder.divider1 = (ImageView) convertView
					.findViewById(R.id.divider1);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.statusIcon = (ImageView) convertView
					.findViewById(R.id.statusIcon);
			holder.newHuman=(TextView)convertView.findViewById(R.id.newhuman);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置列表高度
		RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) holder.listView
				.getLayoutParams();
		listParams.height = (int) (screenHeight * (1 - 0.08 - 0.09 - 0.075) / 4);
		holder.listView.setLayoutParams(listParams);
		// 设置图片大小
		RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams) holder.typeImg
				.getLayoutParams();
		iconParams.height = (int) (screenWidth * 0.2);
		iconParams.width = (int) (screenWidth * 0.2);
		holder.typeImg.setLayoutParams(iconParams);
		holder.productName.setText(item.getName());
		holder.apr.setText(item.getApr() + "%");
		holder.investNum.setText(item.getSuccessNumber() + "人");
		if (item.getProductType() == 1) {
			// 隐藏控件
			holder.newHuman.setVisibility(View.GONE);
			holder.productStatusInfo.setVisibility(View.GONE);
			holder.productRoundProgressBar.setVisibility(View.GONE);
			holder.periodLabel.setVisibility(View.GONE);
			holder.minInvestAccount.setVisibility(View.VISIBLE);
			holder.minInvestAccount.setText(Tool.toIntAccount(item
					.getMinInvestMoney()) + "元");
			// holder.minInvestAccount.setVisibility(View.GONE);
			// holder.minInvestAccountLabel.setVisibility(View.GONE);
			// holder.divider1.setVisibility(View.GONE);
			holder.minInvestAccountLabel.setVisibility(View.VISIBLE);
			// 显示控件
			holder.status.setVisibility(View.VISIBLE);
			holder.statusIcon.setVisibility(View.VISIBLE);
			holder.statusIcon.setBackgroundResource(R.drawable.jiaobiao);
			holder.status.setText("热");
			holder.kdbIcon.setVisibility(View.VISIBLE);
			// 设置内容
			holder.kdbIcon.setBackgroundResource(R.drawable.koudaibao);
			holder.period.setText(item.getSummary());
		} else {
			
			// 隐藏控件
			holder.newHuman.setVisibility(View.GONE);
			holder.kdbIcon.setVisibility(View.INVISIBLE);
			// 显示控件
			holder.productStatusInfo.setVisibility(View.VISIBLE);
			holder.productRoundProgressBar.setVisibility(View.VISIBLE);
			holder.periodLabel.setVisibility(View.VISIBLE);
			holder.minInvestAccount.setVisibility(View.VISIBLE);
			holder.minInvestAccountLabel.setVisibility(View.VISIBLE);
			holder.divider1.setVisibility(View.VISIBLE);
			// 设置内容
			holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_red_color));
			if (item.getStatus().equals(G.PRODUCT_STATUS.STATUS_REPAYED)) {
				holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_label_color));
				holder.productStatusInfo.setText("已还款");
				holder.productRoundProgressBar.setMax(100);
				holder.productRoundProgressBar.setProgress(100);
			} else if (item.getStatus()
					.equals(G.PRODUCT_STATUS.STATUS_REPAYING)) {
				holder.productStatusInfo.setTextColor(mContext.getResources().getColor(R.color.global_label_color));
				holder.productStatusInfo.setText("还款中");
				holder.productRoundProgressBar.setMax(100);
				holder.productRoundProgressBar.setProgress(100);
			} else {
				final int progressInt;
				if(Double.parseDouble(item.getSuccessMoney())
						/ Double.parseDouble(item.getTotalMoney()) * 100<100&&Math.ceil(Double.parseDouble(item
								.getSuccessMoney())
								/ Double.parseDouble(item.getTotalMoney()) * 100)==100)
				{
					progressInt=99;
				}else {
					progressInt=(int) Math.ceil(Double.parseDouble(item
							.getSuccessMoney())
							/ Double.parseDouble(item.getTotalMoney()) * 100);
				}
				if(getCurCount()<3)
				{
					if (progressInt < 100) {
					holder.productRoundProgressBar.setAnimProgress(progressInt);
				}else {
					holder.productRoundProgressBar.setProgress(progressInt);
				}
				}else {
					holder.productRoundProgressBar.setProgress(progressInt);
				}
				holder.productStatusInfo.setText("" + progressInt + "%");
				holder.productRoundProgressBar.setMax(100);
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
					holder.newHuman.setVisibility(View.VISIBLE);
				} else {
					holder.status.setVisibility(View.GONE);
					holder.statusIcon.setVisibility(View.GONE);
				}
			}
			curCount++;
		}
		return convertView;
	}

	public int getCurCount() {
		return curCount;
	}

	public void setCurCount(int curCount) {
		this.curCount = curCount;
	}

	/**
	 * 界面上的显示控件
	 */
	private static class ViewHolder {
		private RelativeLayout listView;
		private ImageView kdbIcon;
		private TextView productStatusInfo;
		private RoundProgressBar productRoundProgressBar;
		private RelativeLayout typeImg;
		private TextView productName;
		private TextView aprLabel;
		private TextView apr;
		private TextView investNum;
		private TextView period;
		private TextView periodLabel;
		private TextView minInvestAccount;
		private TextView minInvestAccountLabel;
		private ImageView divider1;
		private TextView status;
		private ImageView statusIcon;
		private TextView newHuman;
	}

}
