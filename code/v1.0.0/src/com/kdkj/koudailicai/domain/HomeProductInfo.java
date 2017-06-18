package com.kdkj.koudailicai.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeProductInfo implements Parcelable {
	/**
	 * 
	 */
	private int id;
	private int productId; // 第一个固定为口袋宝所以id为0，其他都有项目id
	private String name;
	private String apr;
	private String totalMoney;
	private String successMoney;
	private String successPercent;
	private String minInvestMoney;
	private String words;
	private String isNovice;

	public HomeProductInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HomeProductInfo(int id, int productId, String name, String apr,
			String totalMoney, String successMoney, String successPercent,
			String minInvestMoney, String words, String isNovice) {
		super();
		this.id = id;
		this.productId = productId;
		this.name = name;
		this.apr = apr;
		this.totalMoney = totalMoney;
		this.successMoney = successMoney;
		this.successPercent = successPercent;
		this.minInvestMoney = minInvestMoney;
		this.words = words;
		this.isNovice = isNovice;
	}

	public HomeProductInfo(int productId, String name, String apr,
			String totalMoney, String successMoney, String successPercent,
			String minInvestMoney, String words, String isNovice) {
		super();
		this.productId = productId;
		this.name = name;
		this.apr = apr;
		this.totalMoney = totalMoney;
		this.successMoney = successMoney;
		this.successPercent = successPercent;
		this.minInvestMoney = minInvestMoney;
		this.words = words;
		this.isNovice = isNovice;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(String successMoney) {
		this.successMoney = successMoney;
	}

	public String getSuccessPercent() {
		return successPercent;
	}

	public void setSuccessPercent(String successPercent) {
		this.successPercent = successPercent;
	}

	public String getMinInvestMoney() {
		return minInvestMoney;
	}

	public void setMinInvestMoney(String minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
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
		dest.writeString(name);
		dest.writeString(apr);
		dest.writeString(totalMoney);
		dest.writeString(successMoney);
		dest.writeString(successPercent);
		dest.writeString(minInvestMoney);
		dest.writeString(words);
		dest.writeString(isNovice);
	}

	public String getIsNovice() {
		return isNovice;
	}

	public void setIsNovice(String isNovice) {
		this.isNovice = isNovice;
	}

	public static final Creator<HomeProductInfo> CREATOR = new Creator<HomeProductInfo>() {

		@Override
		public HomeProductInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new HomeProductInfo[size];
		}

		@Override
		public HomeProductInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			HomeProductInfo homeProductInfo = new HomeProductInfo(
					source.readInt(), source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
			return homeProductInfo;
		}
	};
}
