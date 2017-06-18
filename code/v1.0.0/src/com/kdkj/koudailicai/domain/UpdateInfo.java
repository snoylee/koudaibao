package com.kdkj.koudailicai.domain;

/**
 * "code": 0,
    "has_upgrade": 1, // 主要根据这个判断是否有新版本
    "is_force_upgrade": 0, // 是否强制更新
    "new_version": "1.0.0",
    "new_features": "1、新特点1斯蒂芬\\n2、新特点2斯蒂字多字多字多斯蒂芬斯蒂芬斯蒂芬芬\\n3、新特点3斯蒂芬\\n"
 * @author liaoheng
 * @creation 2014-12-18
 *
 */
public class UpdateInfo extends BaseDomain {
	public static int FORCE_TO_UPDATE=1;
	public static int HAS_UPDATE=1;
	int code;
	int has_upgrade;
	int is_force_upgrade;
	String new_version;
	String new_features;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getHas_upgrade() {
		return has_upgrade;
	}
	public void setHas_upgrade(int has_upgrade) {
		this.has_upgrade = has_upgrade;
	}
	public int getIs_force_upgrade() {
		return is_force_upgrade;
	}
	public void setIs_force_upgrade(int is_force_upgrade) {
		this.is_force_upgrade = is_force_upgrade;
	}
	public String getNew_version() {
		return new_version;
	}
	public void setNew_version(String new_version) {
		this.new_version = new_version;
	}
	public String getNew_features() {
		return new_features;
	}
	public void setNew_features(String new_features) {
		this.new_features = new_features;
	}
	

}
