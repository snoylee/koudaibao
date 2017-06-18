package com.kdkj.koudailicai.domain;


import android.os.Parcel;
import android.os.Parcelable;

import com.kdkj.koudailicai.util.Tool;

public class InvestOrderInfo implements Parcelable {
	private int productId;
	private String productName;
	private String useRemain;
	private String investAccount;
	private String payPassword;
	private String orderId;
	private String productType;
	
	public InvestOrderInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InvestOrderInfo [productId=" + productId + ", productName="
				+ productName + ", useRemain=" + useRemain + ", investAccount="
				+ investAccount + ", payPassword=" + payPassword + ", orderId="
				+ orderId + ", productType=" + productType + "]";
	}

	public InvestOrderInfo(int productId, String productName, String useRemain,
			String investAccount, String payPassword, String orderId,
			String productType) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.useRemain = useRemain;
		this.investAccount = investAccount;
		this.payPassword = payPassword;
		this.orderId = orderId;
		this.productType = productType;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(productId);
		dest.writeString(productName);
		dest.writeString(useRemain);
		dest.writeString(investAccount);
		dest.writeString(payPassword);
		dest.writeString(orderId);
		dest.writeString(productType);
	}

	public static final Creator<InvestOrderInfo> CREATOR = new Creator<InvestOrderInfo>() {

		@Override
		public InvestOrderInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InvestOrderInfo[size];
		}

		@Override
		public InvestOrderInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			InvestOrderInfo productBaseInfo = new InvestOrderInfo(
					source.readInt(), source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
			return productBaseInfo;
		}
	};

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getUseRemain() {
		return useRemain;
	}

	public void setUseRemain(String useRemain) {
		this.useRemain = useRemain;
	}

	public String getInvestAccount() {
		return investAccount;
	}

	public void setInvestAccount(String investAccount) {
		this.investAccount = investAccount;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
