package com.kdkj.koudailicai.util.global;

import android.R.integer;
import android.content.Context;

public final class G {

	private G() {
	}
	public static final String APP_NAME = "口袋理财";
	//app市场标记
	public static final String MARKET_NAME = "none";
	//微信分享
	public static final String APP_ID = "wx789cbeca28006605";
	//Bugly使用
	public static final String BUGLY_APP_ID="900001185";
	public static final String BUGLY_APP_KEY="yGtjpHWxVdGn9VvK";
	// 全局配置文件格式版本号
	public static final int GLOBAL_CONFIG_FORMAT_VERSION = 1;

	// 资源加载线程池大小
	public static final int RESOURCE_THREAD_POOL_SIZE = 4;
	//刷新延迟时间
	public static final int AUTO_LOAD_DELAYED = 500;
	//activity之间的code
	public static final int REQ_CODE_ACTIVITY_LOGIN = 1;
	public static final int REQ_CODE_FRAGMENT_LOGIN = 2;
	public static final int RET_CODE_LOGIN_SUCCESS = 3;
	//广播消息
	public static final String HOLD_ASSET_REFRESH = "hold_asset_refresh";
	public static final String WITH_DRAW_MESSAGE="with_draw_refresh";
	public static final String MAIN_SHOW_HOME = "main_show_home";
	public static final String PAY_PASSWORD_SETTED = "pay_password_setted";
	public static final String BROADCAST_SET_BANK_ID = "set_bank_id";
	// 全局配置接口URL
	public static final String GLOBAL_CONFIG_URL = "http://api.koudailc.com/app/config";
	public static final String URL_PREFIX = "http://api.koudailc.com";
	public static final String URL_SITE = "http://www.koudailc.com";
	
//	public static final String GLOBAL_CONFIG_URL = "http://192.168.1.214/frontend/web/app/config";
//	public static final String URL_PREFIX = "http://192.168.1.214/frontend/web";
//	public static final String URL_SITE = "http://192.168.1.214/frontend/web";
	
	public static final String URL_GET_INDEX = URL_PREFIX+"/app/index";
	public static final String URL_GET_SPLASH_IMG = URL_PREFIX+"/app/launch-img";
	
	public static final String URL_GET_APP_UPGRADE = URL_PREFIX+"/app/upgrade";
	public static final String URL_GET_SELFCENTER_RECORD = URL_PREFIX+"/account/home";
	public static final String URL_GET_PODUCT_P2P_LIST = URL_PREFIX+"/project/p2p-list";
	public static final String URL_GET_PODUCT_TRUST_LIST = URL_PREFIX+"/project/trust-list";
	public static final String URL_GET_PODUCT_CESSION_LIST = URL_PREFIX+"/credit/market-for-app";
	public static final String URL_GET_PROJECT_DETAIL = URL_PREFIX+"/project/detail";

	public static final String URL_GET_ACCOUNT_HOLD = URL_PREFIX+"/account/hold";
	public static final String URL_GET_KDB_DETAIL = URL_PREFIX+"/koudaibao/info";
	public static final String URL_GET_ACCOUNT_KDB_TRADES= URL_PREFIX+"/account/kdb-trades";
	public static final String URL_GET_ACCOUNT_PROJECT_TRADES = URL_PREFIX+"/account/project-trades";

	public static final String URL_GET_TOTAL_PROFIT=URL_PREFIX+"/account/total-profits";
	public static final String URL_GET_USER_REAL_VERIFY=URL_PREFIX+"/user/real-verify";
	public static final String URL_POST_USER_REGGET=URL_PREFIX+"/user/reg-get-code";
	public static final String URL_GET_USER_SUPPORT_BANKS=URL_PREFIX+"/user/support-banks";
	public static final String URL_GET_KDB_ROLL_OUT=URL_PREFIX+"/koudaibao/rollout";
	public static final String URL_GET_KDB_ROLLOUT_LIST=URL_PREFIX+"/koudaibao/rollout-list";
	public static final String URL_POST_LOGIN = URL_PREFIX+"/user/login";
	public static final String URL_GET_REGISTER = URL_PREFIX+"/user/register";
	public static final String URL_GET_ACCOUNT_LASTDAY_PROFITS = URL_PREFIX+"/account/lastday-profits"; 
	public static final String URL_POST_USER_REGISTER=URL_PREFIX+"/user/register";
	public static final String URL_GET_CREDIT_RECENTLY=URL_PREFIX+"/credit/recently-applied-assignable-items";
	public static final String URL_GET_ACCOUNT_REMAIN=URL_PREFIX+"/account/remain";
	public static final String URL_POST_USER_CHANGE_PWD = URL_PREFIX+"/user/change-pwd";
	public static final String URL_GET_PAGE_HELP_LIST=URL_PREFIX+"/page/help-list";
	public static final String URL_POST_PAGE_ADD_SUGGEST=URL_PREFIX+"/page/add-suggest";
	public static final String URL_GET_USER_CHANGE_PAY_PASSWORD=URL_PREFIX+"/user/change-paypassword";
	public static final String URL_GET_USER_BANK_ACCOUNT_INFO = URL_PREFIX+"/account/get"; 
	public static final String URL_POST_KDB_INVEST = URL_PREFIX+"/koudaibao/invest";
	public static final String URL_POST_PRODUCT_INVEST = URL_PREFIX+"/project/invest";	


	public static final String URL_GET_KDB_INVEST_LIST = URL_PREFIX+"/koudaibao/invest-list";	
	public static final String URL_GET_PRODUC_TINVEST_CESSION_INVEST = URL_PREFIX+"/project/invest-list";	


	public static final String URL_POST_USER_RESET_PASSWORD_CODE = URL_PREFIX+"/user/reset-pwd-code";
	public static final String URL_POST_USER_VERIFY_RESET_PASSWORD = URL_PREFIX+"/user/verify-reset-password";
	public static final String URL_POST_USER_RESET_PASSWORD = URL_PREFIX+"/user/reset-password";
	public static final String URL_POST_USER_RESET_PAY_PASSWORD = URL_PREFIX+"/user/reset-paypassword";
	public static final String URL_POST_CESSION_INVEST = URL_PREFIX+"/credit/apply-assignment";	
	
	public static final String URL_POST_SET_PAYPASSWORD = URL_PREFIX+"/user/set-paypassword";
	public static final String URL_GET_PAGE_ACTIVITY_DETAIL=URL_PREFIX+"/page/activity-detail";
	public static final String URL_GET_PAGE_FXBZJ=URL_PREFIX+"/page/fxbzj";

	public static final String URL_GET_ACCOUNT_WITHDRAW_LOG=URL_PREFIX+"/account/withdraw-log";

	public static final String URL_GET_ACCOUNT_GET = URL_PREFIX+"/account/get";
	public static final String URL_POST_ACCOUNT_WITH_DRAW = URL_PREFIX+"/account/withdraw";
	public static final String URL_GET_USER_BIND_CARD = URL_PREFIX+"/user/bind-card";
	public static final String URL_H5_PROJECT_DETAIL = URL_PREFIX+"/project/desc-detail";
	public static final String URL_H5_KDB_DETAIL = URL_PREFIX+"/koudaibao/desc-detail";
    public static final String URL_POST_CREDIT_ASSIGN = URL_PREFIX+"/credit/assign";
    public static final String URL_POST_CREDIT_INVEST_BYID = URL_PREFIX+"/credit/invest-by-id";
    public static final String URL_POST_APP_DEVICE_REPORT = URL_PREFIX+"/app/device-report";
    public static final String URL_POST_CREDIT_CANCEL_ASSIGNMENT = URL_PREFIX+"/credit/cancel-assignment";
    public static final String URL_GET_PAGE_AGREEMENT = URL_PREFIX + "/page/agreement";
    public static final String URL_POST_PAGE_DETAIL=URL_PREFIX+"/page/detail";
    public static final String URL_GET_USER_CARDS=URL_PREFIX+"/user/cards";
    public static final String URL_GET_PROFIT_DETAIL=URL_PREFIX+"/account/profits-detail";
    public static final String URL_GET_KDB_INVEST_ORDER=URL_PREFIX+"/koudaibao/invest-order";
    public static final String URL_GET_PRODUCT_INVEST_ORDER=URL_PREFIX+"/project/invest-order";
    public static final String URL_GET_WITHDRAW_ORDER=URL_PREFIX+"/account/withdraw-order";
    public static final String URL_GET_ACCOUNT_FINISHED=URL_PREFIX+"/account/finished-proj";
    public static final String URL_GET_KDB_INVEST_CODE=URL_PREFIX+"/koudaibao/invest-captcha";
    public static final String URL_GET_PRODUCT_INVEST_CODE=URL_PREFIX+"/project/invest-captcha";    
    public static final String URL_POST_USER_STATE=URL_PREFIX+"/user/state";
    //本地配置信息
    public static final String VAL_APP_TELE= "400-002-0802";
    public static final String VAL_COMPANY_ADDRESS="上海市杨浦区淞沪路303号";
    public static final String VAL_SITE_URL="www.koudailc.com";
    public static final String VAL_COMPANY_EMAIL="hr@koudailc.com";
    public static final String VAL_APP_NAME="口袋理财";
    public static final String VAL_COMPANY_ABOUT="口袋理财成立于2014年12月，注册资金1000万元。公司以改善普通大众理财习惯为己任，" +
    		"以丰富普通大众理财渠道及保障投资人债权权益为企业核心价值观，旨在通过互联网技术和严格的风险控制管理技术为广大投资者带来更高、更稳健的投资收益。" +
    		"让普惠金融惠及更多的人民群众，实现金融民主化的目标。";
    public static final String VAL_WARRANT_WORD = "工商银行监管风险准备金";
	public static final String URL_GET_KDB_REMAIN=URL_PREFIX+"/koudaibao/today-remain";
	public static final String URL_GET_ACCOUNT_REMAIN_LIST = URL_PREFIX+"/account/remain-list";
	//全局配置文件中配置信息key 

	public static final String URL_GET_GET_ACTIVITYS=URL_PREFIX+"/page/list-activity";

	//分享信息下发接口
	public static final String URL_GET_SHARE_INFO = URL_PREFIX+"/page/share-info";
	//获取登录用户基本信息
	public static final String URL_GET_LOGIN_USER_INFO = URL_PREFIX+"/user/info";
	
	//全局配置文件中配置信息key
	public static final int GCK_LOGO_IMG   = 1; //logo图片
	public static final int GCK_SPLASH_IMG = 2; //启动页图片
	public static final String GLOBAL_CONFIG_FILENAME = "global.cfg";

	public static final String SPLASH_IMAGE_FILENAME = "splash.png";
	public static final String SPLASH_IMAGE_CONF_VERSION = "splashConfVersion";
	public static final String GLOBAL_CONFIG_KEY = "configVersion";
	public static final String FIRST_IN_KEY = "firstIn";
	public static final String APP_CUR_VERSION = "appCurVersion";
	public static final String SPLASH_IMAGE_URL_KEY = "getLaunchImg";
	public static final String GLOBAL_CONFIG_URL_KEY = "dataUrl";
	
	public static final String HOMEPAGE_SCROLLED = "homepagescrolled";
	
	public static final String HOME_INFO_UPDATED = "homeinfoupdated";//是否更新过主页数据
	//URL的key
	//public static final int GCK_API_GET_ARTICLE_DETAIL = 1;
	public static final int GCK_API_GET_SPLASH_IMAGE = 2;
	public static final int GCK_API_GET_PODUCT_P2P_LIST = 3;
	public static final int GCK_API_GET_SELFCENTER_RECORD = 4;
	public static final int GCK_API_GET_PROJECT_DETAIL = 5;
	public static final int GCK_API_GET_ACCOUNT_HOLD = 6;
	public static final int GCK_API_GET_KDB_DETAIL = 7;
	public static final int GCK_API_GET_TOTAL_PROFIT=8;
	public static final int GCK_API_GET_PODUCT_TRUST_LIST=9;
	public static final int GCK_API_GET_ACCOUNT_KDB_TRADES = 10;
	public static final int GCK_API_GET_ACCOUNT_PROJECT_TRADES= 11;
	public static final int GCK_API_GET_PODUCT_CESSION_LIST = 12;
	public static final int GCK_API_GET_USER_REALVERIFY = 13;
	public static final int GCK_API_GET_SUPPORT_BANKS=14;
	public static final int GCK_API_GET_ROLL_OUT=15;
	public static final int GCK_API_GET_ROLLOUT_LIST=16;
	public static final int GCK_API_POST_USER_REGGET_A=17;
	public static final int GCK_API_GET_USER_REGISTER = 18;
	public static final int GCK_API_GET_LASTDAY_PROFITS = 19;
	public static final int GCK_API_POST_LOGIN=20;
	public static final int GCK_API_POST_USER_REGISTER=21;
	public static final int GCK_API_GET_CREDIT_RECENTLY=22;
	public static final int GCK_API_GET_ACCOUNT_REAMAIN=23;
    public static final int GCK_API_POST_USER_CHANGE_PWD = 24;
    public static final int GCK_API_GET_PAGE_HELP_LIST=25;
    public static final int GCK_API_POST_PAGE_ADD_SUGGEST=26;
    public static final int GCK_API_GET_USER_CHANGE_PAY_PASSWORD=27;
    public static final int GCK_API_POST_KDB_INVEST = 28;
    public static final int GCK_API_POST_PRODUCT_INVEST = 29;	
    public static final int GCK_API_GET_USER_BANK_ACCOUNT_INFO = 30;


    public static final int GCK_API_GET_KDB_INVEST_LIST = 31;
    public static final int GCK_API_GET_ACTIVITYS=32;

    public static final int GCK_API_POST_USER_RESET_PASSWORD_CODE = 33;
    public static final int GCK_API_POST_USER_VERIFY_RESET_PASSWORD = 34;
    public static final int GCK_API_POST_USER_RESET_PASSWORD = 35;

    public static final int GCK_API_POST_USER_RESET_PAY_PASSWORD = 36;
    public static final int GCK_API_POST_CESSION_INVEST = 37;
    public static final int GCK_API_GET_PRODUC_TINVEST_CESSION_INVEST = 38;
    
    public static final int GCK_API_GET_INDEX = 39;
    
    public static final int GCK_API_POST_SET_PAYPASSWORD = 40;
    
    public static final int GCK_API_PAGE_ACTIVITY_DETAIL=41;
    public static final int GCK_API_GET_PAGE_FXBZJ=42;

    public static final int GCK_API_GET_ACCOUNT_GET=43;
    public static final int GCK_API_POST_ACCOUNT_WITH_DRAW=44;
    public static final int GCK_API_H5_PROJECT_DETAIL=45;
    public static final int GCK_API_H5_KDB_DETAIL=46;	
    public static final int GCK_API_GET_ACCOUNT_WITHDRAW_LOG = 47;
	public static final int GCK_API_GET_USER_BIND_CARD=48;
	public static final int GCK_API_URL_POST_CREDIT_ASSIGN=49;
	
	public static final int GCK_VAL_APP_TELE = 50;
	public static final int GCK_VAL_COMPANY_ADDRESS=51; 
	public static final int GCK_VAL_SITE_URL=52;
	public static final int GCK_VAL_COMPANY_EMAIL=53;
    public static final int GCK_VAL_APP_NAME=54;
    public static final int GCK_VAL_COMPANY_ABOUT=55;
	public static final int GCK_API_GET_ACCOUNT_REMAIN_LIST = 57;
	public static final int GCK_API_GET_KDB_REMAIN = 56;
	public static final int GCK_API_CREDIT_INVEST_BYID=58;
	public static final int GCK_API__APP_DEVICE_REPORT=59;
	public static final int GCK_API_CREDIT_CANCEL_ASSIGNMENT=60;
	public static final int GCK_API_GET_PAGE_AGREEMENT = 61;
	public static final int GCK_API_POST_PAGE_DETAIL=62;
	public static final int GCK_VAL_WARRANT_WORD = 63;
	public static final int GCK_API_GET_SHARE_INFO = 64;
	public static final int GCK_API_GET_USER_CARDS=65;
	public static final int GCK_API_GET_USER_BANK_INFO = 66;
	public static final int GCK_API_GET_PROFIT_DETAIL = 67;
	public static final int GCK_API_GET_KDB_INVEST_ORDER = 68;	
	public static final int GCK_API_GET_PRODUCT_INVEST_ORDER = 69;
	public static final int GCK_API_GET_WITHDRAW_ORDER = 70;
	public static final int GCK_API_GET_ACCOUNT_FINISHED=71;
	public static final int GCK_API_GET_KDB_INVEST_CODE=72;
	public static final int GCK_API_GET_PRODUCT_INVEST_CODE=73;
	public static final int GCK_API_POST_USER_STATE=74;
	public static final int GCK_API_GET_LOGIN_USER_INFO = 75 ;
	public static final int GCK_API_GET_APP_UPGRADE = 76 ;
    //序列化传递对象key

	public static final String PRODUCT_INFO_SER_KEY = "productInfoSerKey";
	public static final String INVEST_ORDER_INFO_SER_KEY = "investOrderInfo";
	//序列化传递对象key
	public static final String INVEST_INFO_SER_KEY = "investInfoSerKey";
	//首页序列号key
	public static final String HOME_PRODUCT_INFO_SER_KEY = "homeProductInfoSerKey";
	//项目状态
	public static class PRODUCT_STATUS {
		public static final String STATUS_NEW = "1";
		public static final String STATUS_NEW_CANCEL = "2";
		public static final String STATUS_PUBLISHED = "3";
		public static final String STATUS_PUBLISHED_CANCEL = "4";
		public static final String STATUS_FULL = "5";
		public static final String STATUS_FULL_CANCEL = "6";
		public static final String STATUS_REPAYING = "7";
		public static final String STATUS_REPAYED = "8";
	}
	//持有资产状态
	public static class ASSETS_STATUS {
		public static final String STATUS_PENDING = "1";
		public static final String STATUS_SUCCESS = "2";
		public static final String STATUS_CANCELED = "3";
		public static final String STATUS_ASSIGNING = "4";
		public static final String STATUS_PARTLY_ASSIGNED = "5"; // 暂时不会使用
		public static final String STATUS_FULLY_ASSIGNED = "6";
		public static final String STATUS_REPAYED = "7";
		
	}
	
	//Fragment标签
	public static String TO_FRAGMENT_KEY = "toFragment";
	public static class FRAGMENT_TAG {
		public static final int HOME   = 0;
		public static final int LIST   = 1;
		public static final int CENTER = 2;
		public static final int MORE   = 3;
		
	}	
}
