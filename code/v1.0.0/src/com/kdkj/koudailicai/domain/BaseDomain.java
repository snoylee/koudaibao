package com.kdkj.koudailicai.domain;

import org.json.JSONObject;

/**
 * 供实体子类几次那个
 * @author liaoheng
 * @creation 2014-12-12
 *
 */
public abstract class BaseDomain {
	
	public String toJson(){
		return GsonHelper.toJson(this);		
	}	

}
