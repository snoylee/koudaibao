package com.kdkj.koudailicai.domain;

import android.R.integer;

/**
 * 产品列表详细信息
 * 
 * @author ok
 * 
 */
public class ProductItemInfo {
	private int id; // 标ID

	private int period; // 借款期限

	private String rate; // 年利率

	private int investAmount; // 已收到款
    
	private String success_money;//成功投标数
	
	private String total_money;//总金额
	
	public String getSuccess_money() {
		return success_money;
	}

	public void setSuccess_money(String success_money) {
		this.success_money = success_money;
	}

	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	private boolean isDayMarked; // 是否天标

	private int type; // 标类型

	private int status; // 状态

	private int totalAmount; // 总金额

	private String title; // 标题

    
	private int cur_invest_times;//口袋宝人数
	
	private int min_invest_money;//最小投资 金额
    



	private String s_statue;

	private String is_novice;
    
	private String is_day;
	public String getIs_day() {
		return is_day;
	}

	public void setIs_day(String is_day) {
		this.is_day = is_day;
	}

	public int getCur_invest_times() {
		return cur_invest_times;
	}

	public void setCur_invest_times(int cur_invest_times) {
		this.cur_invest_times = cur_invest_times;
	}

	public int getMin_invest_money() {
		return min_invest_money;
	}

	public void setMin_invest_money(int min_invest_money) {
		this.min_invest_money = min_invest_money;
	}

	public String getS_statue() {
		return s_statue;
	}

	public void setS_statue(String s_statue) {
		this.s_statue = s_statue;
	}

	public String getIs_novice() {
		return is_novice;
	}

	public void setIs_novice(String is_novice) {
		this.is_novice = is_novice;
	}

	public ProductItemInfo() {
		super();
	}

	public ProductItemInfo(String title, String rate, int cur_invest_times,
			String s_statue) {
		super();
		this.title = title;
		this.rate = rate;
		this.cur_invest_times = cur_invest_times;
		this.s_statue = s_statue;
	}

	public ProductItemInfo(String title, String rate, int cur_invest_times,
			int period, int min_invest_money,String is_novice,String is_day,
			String success_money,String total_money) {
		super();
		this.title = title;
		this.rate = rate;
		this.cur_invest_times = cur_invest_times;
		this.period = period;
		this.min_invest_money = min_invest_money;
		this.is_novice=is_novice;
		this.is_day=is_day;
		this.success_money=success_money;
		this.total_money=total_money;
	}

	public String getStatue() {
		return s_statue;
	}

	public void setId(String s_statue) {
		this.s_statue = s_statue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public int getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(int investAmount) {
		this.investAmount = investAmount;
	}

	public boolean isDayMarked() {
		return isDayMarked;
	}

	public void setDayMarked(boolean isDayMarked) {
		this.isDayMarked = isDayMarked;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTimes(int cur_invest_times) {
		this.cur_invest_times = cur_invest_times;
	}

	public int getTimes() {
		return cur_invest_times;
	}

	public void setMin(int min_invest_money) {
		this.min_invest_money = min_invest_money;
	}

	public int getMin() {
		return min_invest_money;
	}
}
