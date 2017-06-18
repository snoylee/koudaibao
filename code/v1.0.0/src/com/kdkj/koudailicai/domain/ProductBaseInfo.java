package com.kdkj.koudailicai.domain;


import android.os.Parcel;
import android.os.Parcelable;

import com.kdkj.koudailicai.util.Tool;

public class ProductBaseInfo implements Parcelable {
	private int id;
	private int productId;
	private String type;
	private String name;
	private String minMoney;
	private String leftMoney;
	private String apr;
	private String fee;
	private String time;
	private String riskControlManaged;
	private String isNovice;
	
	public ProductBaseInfo() {
		super();
	}

	public ProductBaseInfo(int id, String name, String minMoney,
			String leftMoney, String apr, String fee, String time,
			String riskControlManaged, String isNovice, int productId,String type) {
		super();
		this.id = id;
		this.name = name;
		this.minMoney = minMoney;
		this.leftMoney = leftMoney;
		this.apr = apr;
		this.fee = fee;
		this.time = time;
		this.riskControlManaged = riskControlManaged;
		this.isNovice = isNovice;
		this.productId = productId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(String minMoney) {
		this.minMoney = minMoney;
	}

	public String getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(String leftMoney) {
		this.leftMoney = leftMoney;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getIsNovice() {
		return isNovice;
	}

	public void setIsNovice(String isNovice) {
		this.isNovice = isNovice;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ProductBaseInfo [id=" + id + ",productId=" + productId + ",type=" + type + ", name=" + name + ", minMoney="
				+ minMoney +  ", leftMoney=" + leftMoney+", apr=" + apr + ", fee=" + fee + ", time=" + time
				+ ", riskControlManaged=" + riskControlManaged + ", isNovice=" + isNovice + "]";
	}

	public String getRiskControlManaged() {
		return riskControlManaged;
	}

	public void setRiskControlManaged(String riskControlManaged) {
		this.riskControlManaged = riskControlManaged;
	}

	public void getFromKdb(KdbDetailInfo kdbInfo) {
		if (kdbInfo != null) {
			this.setType("kdb");
			this.setProductId(0);
			this.setName(kdbInfo.getTitle());
			this.setApr(kdbInfo.getApr() + "%");
			this.setLeftMoney(Tool.toDeciDouble(kdbInfo.getRemainMoney()));
			this.setMinMoney(String.valueOf(Integer.parseInt(kdbInfo.getMinInvestMoney()) / 100));
			this.setTime("随取随存");
			this.setFee("无");
			this.setRiskControlManaged(kdbInfo.getRiskControlManaged());
			this.setIsNovice("0");
		}
	}

	public void getFromProduct(ProductDetailInfo productInfo) {
		if (productInfo != null) {
			this.setType(productInfo.getType());
			this.setProductId(productInfo.getProductId());
			this.setName(productInfo.getName());
			this.setApr(productInfo.getApr() + "%");
			this.setMinMoney(String.valueOf(Integer.parseInt(productInfo
					.getMinInvestMoney()) / 100));
			this.setLeftMoney(String.valueOf((Long.parseLong(productInfo
					.getTotalMoney()) - Long.parseLong(productInfo
					.getSuccessMoney())) / 100));
			this.setTime(productInfo.getPeriod()
					+ (productInfo.getIsDay().equals("1") ? "天" : "个月"));
			this.setFee("无");
			this.setRiskControlManaged(productInfo.getRiskControlManaged());
			this.setIsNovice(productInfo.getIsNovice());
		}
	}

	public void getFromCessionInfo(ProductListCessionInfo cessionInfo) {
		if (cessionInfo != null) {
			this.setType("cession");
			this.setProductId(Integer.parseInt(cessionInfo.getInvestId()));
			this.setName(cessionInfo.getName() + "（来自转让）");
			this.setApr(cessionInfo.getAssginRate() + "%");
			this.setLeftMoney(cessionInfo.getAssginFee());
			this.setTime(cessionInfo.getRestDays() + "天");
			this.setFee("无");
			this.setRiskControlManaged("账户由第三方托管");
			this.setIsNovice("0");
		}
	}

	public void getFromHomeInfo(HomeProductInfo homeInfo) {
		if (homeInfo != null) {
			this.setType(homeInfo.getProductId() == 0 ? "kdb" : "product");
			this.setProductId(homeInfo.getProductId());
			this.setName(homeInfo.getName());
			this.setApr(homeInfo.getApr() + "%");
			this.setMinMoney(Tool.toIntAccount(homeInfo.getMinInvestMoney()));
			if(homeInfo.getProductId() == 0) {
				this.setLeftMoney(Tool.toDeciDouble(String.valueOf(Long
						.parseLong(homeInfo.getTotalMoney())
						- Long.parseLong(homeInfo.getSuccessMoney()))));
			} else {
				this.setLeftMoney(Tool.toIntAccount(String.valueOf(Long
						.parseLong(homeInfo.getTotalMoney())
						- Long.parseLong(homeInfo.getSuccessMoney()))));
			}
			this.setIsNovice(homeInfo.getIsNovice());
			this.setTime(homeInfo.getWords());
			this.setFee("无");
			this.setRiskControlManaged("账户由第三方托管");
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(minMoney);
		dest.writeString(leftMoney);
		dest.writeString(apr);
		dest.writeString(fee);
		dest.writeString(time);
		dest.writeString(riskControlManaged);
		dest.writeString(isNovice);
		dest.writeInt(productId);
		dest.writeString(type);
		
	}

	public static final Creator<ProductBaseInfo> CREATOR = new Creator<ProductBaseInfo>() {

		@Override
		public ProductBaseInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProductBaseInfo[size];
		}

		@Override
		public ProductBaseInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ProductBaseInfo productBaseInfo = new ProductBaseInfo(
					source.readInt(), source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readInt(), source.readString());
			return productBaseInfo;
		}
	};
}
