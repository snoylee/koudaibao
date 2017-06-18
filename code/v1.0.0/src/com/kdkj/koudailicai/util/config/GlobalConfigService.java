package com.kdkj.koudailicai.util.config;

import java.io.File;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.lib.http.HttpRequestGet;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;

public class GlobalConfigService implements Listener<JSONObject>, ErrorListener{
	private static String LOG_TAG = GlobalConfigService.class.getName();
	private String confUrl = G.GLOBAL_CONFIG_URL;
	private String version;
	private boolean isNew = false;
	private ConfigManager globalConfiger;
	public static  GlobalConfigService globalConf;
	private String filesPath;
	public String confPath;
	public interface ConfigCompleteListener {
		public void configComplete();
	}
	
	public ConfigCompleteListener completeListener;
	
	public GlobalConfigService() {
		super();
	}
	
	public GlobalConfigService(String filesPath, ConfigCompleteListener completeListener) {
		super();
		this.filesPath = filesPath;
		this.confPath = filesPath+ "/conf";
		this.completeListener = completeListener;
	}
	
	//是否已完成
	public boolean isCompleted() {
		return this.globalConfiger != null && this.globalConfiger.isComplete();
	}
	
	//获取接口url
	public String getActionUrl(int key) {
		return isCompleted() ? globalConfiger.getString(key) : "";
	}
	
	//获取下发配置值
	public String getConfVal(int key) {
		return isCompleted() ? globalConfiger.getString(key) : "";
	}
		
	public void updateConf() {
		Log.d(LOG_TAG, "updateConf start");
		if(globalConfiger == null) {
			Log.d("globalConfiger", "new ConfigManager");
			globalConfiger = new ConfigManager();
			globalConfiger.setStatus(ConfigManager.STATUS_INIT);
		}
		String confVersion = KDLCApplication.app.getSessionVal(G.GLOBAL_CONFIG_KEY);
		if(confVersion == null || confVersion.isEmpty()) {
			confVersion = "0";
		}
		Log.d(LOG_TAG, "confVersion:"+confVersion);
		setVersion(confVersion);
		String url = Tool.getUrl(confUrl+"?configVersion="+getVersion());
		HttpRequestGet httpRequest = new HttpRequestGet(url, this, this);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}

	//加载全局配置
	public void updateLocalConf() {
		try{
			if (isNew()) {
				Log.d(LOG_TAG, "Has New Version, Now Load New Conf");
				clearConfigFile();
				globalConfiger.save(confPath+ G.GLOBAL_CONFIG_FILENAME);
				globalConfiger.setStatus(ConfigManager.STATUS_COMPLETE);
				KDLCApplication.app.setSessionVal(G.GLOBAL_CONFIG_KEY, String.valueOf(globalConfiger.getVersion()));//更新版本号
			} else {
				Log.d(LOG_TAG, "Has No New Version, Now Load Local Conf");				
				ConfigManager localConfigManager = new ConfigManager();
				boolean isLocalAvailable = localConfigManager.restore(this.confPath + G.GLOBAL_CONFIG_FILENAME);
				if (isLocalAvailable) {
					localConfigManager.setStatus(ConfigManager.STATUS_COMPLETE);
				} else {
					localConfigManager.setStatus(ConfigManager.STATUS_FAILED);
				}
				globalConfiger = localConfigManager;
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "Has Exception, Load Conf FAILED");							
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConfigManager localConfigManager = new ConfigManager();
			localConfigManager.setStatus(ConfigManager.STATUS_FAILED);
			globalConfiger = localConfigManager;
		}
		completeListener.configComplete();
	}	
	
	//删除下发配置文件
	private void clearConfigFile() {
		new File(confPath + G.GLOBAL_CONFIG_FILENAME).delete();
	}
	
	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		try{
			Log.d("reponse", "reponse:"+response.toString());
			if(response.getInt("code") == 0) {
				Log.d("Ser","is New");
				setNew(true);
				//获取配置信息
				globalConfiger.setVersion(response.getLong(G.GLOBAL_CONFIG_KEY));
				globalConfiger.putValue(G.GCK_VAL_APP_TELE, response.getString("callCenter"));
				globalConfiger.putValue(G.GCK_VAL_COMPANY_ADDRESS, response.getString("companyAddress"));
				globalConfiger.putValue(G.GCK_VAL_SITE_URL, response.getString("siteUrl"));
				globalConfiger.putValue(G.GCK_VAL_COMPANY_EMAIL, response.getString("companyEmail"));
				globalConfiger.putValue(G.GCK_VAL_APP_NAME, response.getString("name"));
				globalConfiger.putValue(G.GCK_VAL_COMPANY_ABOUT, response.getString("companyAbout"));
				globalConfiger.putValue(G.GCK_VAL_WARRANT_WORD, response.getString("warrantWords"));
				//获取url
				JSONObject actionNode = response.getJSONObject(G.GLOBAL_CONFIG_URL_KEY);
				globalConfiger.putValue(G.GCK_API_GET_INDEX, actionNode.getString("getIndex"));
	//			globalConfiger.putValue(G.GCK_API_GET_ARTICLE_DETAIL, actionNode.getString("getArticleDetail"));
				globalConfiger.putValue(G.GCK_API_GET_SPLASH_IMAGE, actionNode.getString("getLaunchImg"));
				globalConfiger.putValue(G.GCK_API_GET_SELFCENTER_RECORD, actionNode.getString("accountHome"));
				globalConfiger.putValue(G.GCK_API_GET_PROJECT_DETAIL, actionNode.getString("projectDetail"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_HOLD, actionNode.getString("accountHold"));
				globalConfiger.putValue(G.GCK_API_GET_KDB_DETAIL, actionNode.getString("kdbInfo"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_KDB_TRADES, actionNode.getString("accountKdbTrades"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_PROJECT_TRADES, actionNode.getString("accountProjectTrades"));
				globalConfiger.putValue(G.GCK_API_GET_TOTAL_PROFIT, actionNode.getString("accountTotalProfits"));
				globalConfiger.putValue(G.GCK_API_GET_PODUCT_P2P_LIST, actionNode.getString("projectP2pList"));
				globalConfiger.putValue(G.GCK_API_GET_PODUCT_TRUST_LIST, actionNode.getString("projectTrustList"));		
				globalConfiger.putValue(G.GCK_API_GET_PODUCT_CESSION_LIST, actionNode.getString("creditMarketForApp"));
				globalConfiger.putValue(G.GCK_API_GET_USER_REALVERIFY, actionNode.getString("userRealVerify"));
				globalConfiger.putValue(G.GCK_API_GET_SUPPORT_BANKS, actionNode.getString("userSupportBanks"));
				globalConfiger.putValue(G.GCK_API_GET_ROLL_OUT, actionNode.getString("kdbRollout"));
				globalConfiger.putValue(G.GCK_API_GET_ROLLOUT_LIST, actionNode.getString("kdbRolloutList"));
				globalConfiger.putValue(G.GCK_API_POST_USER_REGGET_A, actionNode.getString("userRegGetCode"));
				globalConfiger.putValue(G.GCK_API_GET_USER_REGISTER, actionNode.getString("userRegister"));
				globalConfiger.putValue(G.GCK_API_POST_LOGIN, actionNode.getString("userLogin"));
				globalConfiger.putValue(G.GCK_API_GET_LASTDAY_PROFITS, actionNode.getString("accountLastdayProfits"));
				globalConfiger.putValue(G.GCK_API_POST_USER_REGISTER, actionNode.getString("userRegister"));
				globalConfiger.putValue(G.GCK_API_GET_CREDIT_RECENTLY, actionNode.getString("creditRecentlyAppliedAssignableItems"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_REAMAIN, actionNode.getString("accountRemain"));
				globalConfiger.putValue(G.GCK_API_POST_USER_CHANGE_PWD, actionNode.getString("userChangePwd"));
				globalConfiger.putValue(G.GCK_API_GET_PAGE_HELP_LIST, actionNode.getString("pageHelpList"));
				globalConfiger.putValue(G.GCK_API_POST_PAGE_ADD_SUGGEST, actionNode.getString("pageAddSuggest"));
				globalConfiger.putValue(G.GCK_API_GET_USER_CHANGE_PAY_PASSWORD, actionNode.getString("userChangePaypassword"));
				globalConfiger.putValue(G.GCK_API_GET_USER_BANK_ACCOUNT_INFO, actionNode.getString("accountGet"));
				globalConfiger.putValue(G.GCK_API_POST_KDB_INVEST, actionNode.getString("kdbInvest"));
				globalConfiger.putValue(G.GCK_API_POST_PRODUCT_INVEST, actionNode.getString("projectInvest"));
				globalConfiger.putValue(G.GCK_API_POST_USER_RESET_PASSWORD_CODE, actionNode.getString("userResetPwdCode"));
				globalConfiger.putValue(G.GCK_API_POST_USER_VERIFY_RESET_PASSWORD, actionNode.getString("userVerifyResetPassword"));
				globalConfiger.putValue(G.GCK_API_POST_USER_RESET_PASSWORD, actionNode.getString("userResetPassword"));
				globalConfiger.putValue(G.GCK_API_POST_USER_RESET_PAY_PASSWORD, actionNode.getString("userResetPaypassword"));
				globalConfiger.putValue(G.GCK_API_GET_KDB_INVEST_LIST, actionNode.getString("kdbInvestList"));			
				globalConfiger.putValue(G.GCK_API_GET_ACTIVITYS, actionNode.getString("pageActivityList"));
				globalConfiger.putValue(G.GCK_API_POST_CESSION_INVEST, actionNode.getString("creditApplyAssignment"));
				globalConfiger.putValue(G.GCK_API_GET_PRODUC_TINVEST_CESSION_INVEST, actionNode.getString("projectInvestList"));
				globalConfiger.putValue(G.GCK_API_POST_SET_PAYPASSWORD, actionNode.getString("userSetPaypassword"));
				globalConfiger.putValue(G.GCK_API_PAGE_ACTIVITY_DETAIL, actionNode.getString("pageActivityDetail"));
				globalConfiger.putValue(G.GCK_API_GET_PAGE_FXBZJ, actionNode.getString("pageFxbzj"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_WITHDRAW_LOG, actionNode.getString("accountWithdrawLog"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_GET, actionNode.getString("accountGet"));
				globalConfiger.putValue(G.GCK_API_POST_ACCOUNT_WITH_DRAW, actionNode.getString("accountWithdraw"));
				globalConfiger.putValue(G.GCK_API_H5_PROJECT_DETAIL, actionNode.getString("projectDescDetail"));
				globalConfiger.putValue(G.GCK_API_H5_KDB_DETAIL, actionNode.getString("kdbDescDetail"));
				globalConfiger.putValue(G.GCK_API_GET_USER_BIND_CARD, actionNode.getString("userBindCard"));
				globalConfiger.putValue(G.GCK_API_URL_POST_CREDIT_ASSIGN, actionNode.getString("creditAssign"));
				globalConfiger.putValue(G.GCK_API_GET_KDB_REMAIN, actionNode.getString("kdbTodayRemain"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_REMAIN_LIST, actionNode.getString("accountRemainList"));
				globalConfiger.putValue(G.GCK_API_CREDIT_INVEST_BYID, actionNode.getString("creditInvestById"));
				globalConfiger.putValue(G.GCK_API__APP_DEVICE_REPORT, actionNode.getString("appDeviceReport"));
				globalConfiger.putValue(G.GCK_API_CREDIT_CANCEL_ASSIGNMENT, actionNode.getString("creditCancelAssignment"));
				globalConfiger.putValue(G.GCK_API_GET_PAGE_AGREEMENT, actionNode.getString("pageAgreement"));
				globalConfiger.putValue(G.GCK_API_POST_PAGE_DETAIL, actionNode.getString("pageDetail"));
				globalConfiger.putValue(G.GCK_API_GET_SHARE_INFO, actionNode.getString("pageShareInfo"));
				globalConfiger.putValue(G.GCK_API_GET_USER_CARDS, actionNode.getString("userCards"));
				globalConfiger.putValue(G.GCK_API_GET_PROFIT_DETAIL, actionNode.getString("accountProfitsDetail"));
				globalConfiger.putValue(G.GCK_API_GET_KDB_INVEST_ORDER, actionNode.getString("kdbInvestOrder"));
				globalConfiger.putValue(G.GCK_API_GET_PRODUCT_INVEST_ORDER, actionNode.getString("projectInvestOrder"));
				globalConfiger.putValue(G.GCK_API_GET_WITHDRAW_ORDER, actionNode.getString("accountWithdrawOrder"));
				globalConfiger.putValue(G.GCK_API_GET_ACCOUNT_FINISHED, actionNode.getString("accountFinishedProj"));
				globalConfiger.putValue(G.GCK_API_GET_KDB_INVEST_CODE, actionNode.getString("kdbInvestCaptcha"));
				globalConfiger.putValue(G.GCK_API_GET_PRODUCT_INVEST_CODE, actionNode.getString("projectInvestCaptcha"));
				globalConfiger.putValue(G.GCK_API_POST_USER_STATE, actionNode.getString("userState"));
				globalConfiger.putValue(G.GCK_API_GET_LOGIN_USER_INFO, actionNode.getString("userInfo"));
				globalConfiger.putValue(G.GCK_API_GET_APP_UPGRADE, actionNode.getString("appUpgrade"));
				globalConfiger.setStatus(ConfigManager.STATUS_READY);
				Log.d("asdsa", "status:"+globalConfiger.getStatus());
			} else {
				setNew(false);
			}
		} catch (Exception e) {
			globalConfiger.setStatus(ConfigManager.STATUS_FAILED);
			e.printStackTrace();
			setNew(false);
		}
		updateLocalConf();
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		updateLocalConf();
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isNew() {
		return isNew;
	}
	
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public ConfigManager getGlobalConfiger() {
		return globalConfiger;
	}
	
	public void setGlobalConfiger(ConfigManager globalConfiger) {
		this.globalConfiger = globalConfiger;
	}

	public String getFilesPath() {
		return filesPath;
	}

	public void setFilesPath(String filesPath) {
		this.filesPath = filesPath;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}
	
}
