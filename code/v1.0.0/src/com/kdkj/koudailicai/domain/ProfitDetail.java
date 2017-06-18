package com.kdkj.koudailicai.domain;

import com.google.gson.Gson;

/**
 * 	收益详情的实体类
 * 		"project_name": "口袋宝",
        "duein_capital": 165900,
        "apr": "8.00",
        "duein_money": 166078,
        "period_label": "",
        
        "created_at": "",
        "interest_start_date": "",
        "last_repay_date": "",
        
        "interest_date": "",
        "repay_date": "",
        "expression": "年化利率×投资金额×持有天数/365"
 * @author liaoheng
 * @creation 2014-12-11
 *
 */
public class ProfitDetail extends BaseDomain {	
	String project_name;
	String duein_capital;
	String apr;
	String duein_money;
	String period_label;
	String created_at;
	String interest_start_date;
	String last_repay_date;
	String interest_date;
	String repay_date;
	String expression;
	
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getDuein_capital() {
		return duein_capital;
	}
	public void setDuein_capital(String duein_capital) {
		this.duein_capital = duein_capital;
	}
	public String getApr() {
		return apr;
	}
	public void setApr(String apr) {
		this.apr = apr;
	}
	public String getDuein_money() {
		return duein_money;
	}
	public void setDuein_money(String duein_money) {
		this.duein_money = duein_money;
	}
	public String getPeriod_label() {
		return period_label;
	}
	public void setPeriod_label(String period_label) {
		this.period_label = period_label;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getInterest_start_date() {
		return interest_start_date;
	}
	public void setInterest_start_date(String interest_start_date) {
		this.interest_start_date = interest_start_date;
	}
	public String getLast_repay_date() {
		return last_repay_date;
	}
	public void setLast_repay_date(String last_repay_date) {
		this.last_repay_date = last_repay_date;
	}
	public String getInterest_date() {
		return interest_date;
	}
	public void setInterest_date(String interest_date) {
		this.interest_date = interest_date;
	}
	public String getRepay_date() {
		return repay_date;
	}
	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
}
