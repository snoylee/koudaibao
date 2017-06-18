package com.kdkj.koudailicai.util.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.AbstractService;
import com.kdkj.koudailicai.lib.http.HttpService;


public class GetGlobalConfigService extends HttpService {

	public static class Response extends AbstractService.Response{

		private ConfigManager globalConfigManager;

		private boolean isNew;

		private Response() {

		}

		public boolean isNew() {
			return isNew;
		}

		public ConfigManager getGlobalConfigManager() {
			return globalConfigManager;
		}

	}

	private long version;

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	public Response execute() throws Exception {
		// TODO Auto-generated method stub
		return (Response) super.execute();
	}

	@Override
	protected String getRequestUrl() {
		// TODO Auto-generated method stub
		return G.GLOBAL_CONFIG_URL;
	}

	@Override
	protected List<NameValuePair> getParams() {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("configVersion", version + ""));
		return params;
	}

	@Override
	protected Response parseResponse(String content) throws Exception {
		// TODO Auto-generated method stub
		Log.d("Ser","Parse Conf Response Start");
		Response resp = new Response();
		JSONObject confRoot = new JSONObject(content);
		if(confRoot.getInt("code") == 0) {
			Log.d("Ser","is New");
			resp.isNew = true;
			resp.globalConfigManager = new ConfigManager();
			JSONObject actionNode = confRoot.getJSONObject(G.GLOBAL_CONFIG_URL_KEY);
			resp.globalConfigManager.setVersion(confRoot.getLong(G.GLOBAL_CONFIG_KEY));
			resp.globalConfigManager.putValue(G.GCK_VAL_APP_TELE, confRoot.getString("callCenter"));
			resp.globalConfigManager.putValue(G.GCK_VAL_COMPANY_ADDRESS, confRoot.getString("companyAddress"));
			resp.globalConfigManager.putValue(G.GCK_VAL_SITE_URL, confRoot.getString("siteUrl"));
			resp.globalConfigManager.putValue(G.GCK_VAL_COMPANY_EMAIL, confRoot.getString("companyEmail"));
			resp.globalConfigManager.putValue(G.GCK_VAL_APP_NAME, confRoot.getString("name"));
			resp.globalConfigManager.putValue(G.GCK_VAL_COMPANY_ABOUT, confRoot.getString("companyAbout"));
			resp.globalConfigManager.putValue(G.GCK_VAL_WARRANT_WORD, confRoot.getString("warrantWords"));
			
			resp.globalConfigManager.putValue(G.GCK_API_GET_INDEX, actionNode.getString("getIndex"));
//			resp.globalConfigManager.putValue(G.GCK_API_GET_ARTICLE_DETAIL, actionNode.getString("getArticleDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_SPLASH_IMAGE, actionNode.getString("getLaunchImg"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_SELFCENTER_RECORD, actionNode.getString("accountHome"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PROJECT_DETAIL, actionNode.getString("projectDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_HOLD, actionNode.getString("accountHold"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_KDB_DETAIL, actionNode.getString("kdbInfo"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_KDB_TRADES, actionNode.getString("accountKdbTrades"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_PROJECT_TRADES, actionNode.getString("accountProjectTrades"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_TOTAL_PROFIT, actionNode.getString("accountTotalProfits"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PODUCT_P2P_LIST, actionNode.getString("projectP2pList"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PODUCT_TRUST_LIST, actionNode.getString("projectTrustList"));		
			resp.globalConfigManager.putValue(G.GCK_API_GET_PODUCT_CESSION_LIST, actionNode.getString("creditMarketForApp"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_REALVERIFY, actionNode.getString("userRealVerify"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_SUPPORT_BANKS, actionNode.getString("userSupportBanks"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ROLL_OUT, actionNode.getString("kdbRollout"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ROLLOUT_LIST, actionNode.getString("kdbRolloutList"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_REGGET_A, actionNode.getString("userRegGetCode"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_REGISTER, actionNode.getString("userRegister"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_LOGIN, actionNode.getString("userLogin"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_LASTDAY_PROFITS, actionNode.getString("accountLastdayProfits"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_REGISTER, actionNode.getString("userRegister"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_CREDIT_RECENTLY, actionNode.getString("creditRecentlyAppliedAssignableItems"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_REAMAIN, actionNode.getString("accountRemain"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_CHANGE_PWD, actionNode.getString("userChangePwd"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PAGE_HELP_LIST, actionNode.getString("pageHelpList"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_PAGE_ADD_SUGGEST, actionNode.getString("pageAddSuggest"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_CHANGE_PAY_PASSWORD, actionNode.getString("userChangePaypassword"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_BANK_ACCOUNT_INFO, actionNode.getString("accountGet"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_KDB_INVEST, actionNode.getString("kdbInvest"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_PRODUCT_INVEST, actionNode.getString("projectInvest"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_RESET_PASSWORD_CODE, actionNode.getString("userResetPwdCode"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_VERIFY_RESET_PASSWORD, actionNode.getString("userVerifyResetPassword"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_RESET_PASSWORD, actionNode.getString("userResetPassword"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_RESET_PAY_PASSWORD, actionNode.getString("userResetPaypassword"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_KDB_INVEST_LIST, actionNode.getString("kdbInvestList"));			
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACTIVITYS, actionNode.getString("pageActivityList"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_CESSION_INVEST, actionNode.getString("creditApplyAssignment"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PRODUC_TINVEST_CESSION_INVEST, actionNode.getString("projectInvestList"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_SET_PAYPASSWORD, actionNode.getString("userSetPaypassword"));
			resp.globalConfigManager.putValue(G.GCK_API_PAGE_ACTIVITY_DETAIL, actionNode.getString("pageActivityDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PAGE_FXBZJ, actionNode.getString("pageFxbzj"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_WITHDRAW_LOG, actionNode.getString("accountWithdrawLog"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_GET, actionNode.getString("accountGet"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_ACCOUNT_WITH_DRAW, actionNode.getString("accountWithdraw"));
			resp.globalConfigManager.putValue(G.GCK_API_H5_PROJECT_DETAIL, actionNode.getString("projectDescDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_H5_KDB_DETAIL, actionNode.getString("kdbDescDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_BIND_CARD, actionNode.getString("userBindCard"));
			resp.globalConfigManager.putValue(G.GCK_API_URL_POST_CREDIT_ASSIGN, actionNode.getString("creditAssign"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_KDB_REMAIN, actionNode.getString("kdbTodayRemain"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_REMAIN_LIST, actionNode.getString("accountRemainList"));
			resp.globalConfigManager.putValue(G.GCK_API_CREDIT_INVEST_BYID, actionNode.getString("creditInvestById"));
			resp.globalConfigManager.putValue(G.GCK_API__APP_DEVICE_REPORT, actionNode.getString("appDeviceReport"));
			resp.globalConfigManager.putValue(G.GCK_API_CREDIT_CANCEL_ASSIGNMENT, actionNode.getString("creditCancelAssignment"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PAGE_AGREEMENT, actionNode.getString("pageAgreement"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_PAGE_DETAIL, actionNode.getString("pageDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_SHARE_INFO, actionNode.getString("pageShareInfo"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_USER_CARDS, actionNode.getString("userCards"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PROFIT_DETAIL, actionNode.getString("accountProfitsDetail"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_KDB_INVEST_ORDER, actionNode.getString("kdbInvestOrder"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PRODUCT_INVEST_ORDER, actionNode.getString("projectInvestOrder"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_WITHDRAW_ORDER, actionNode.getString("accountWithdrawOrder"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_ACCOUNT_FINISHED, actionNode.getString("accountFinishedProj"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_KDB_INVEST_CODE, actionNode.getString("kdbInvestCaptcha"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_PRODUCT_INVEST_CODE, actionNode.getString("projectInvestCaptcha"));
			resp.globalConfigManager.putValue(G.GCK_API_POST_USER_STATE, actionNode.getString("userState"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_LOGIN_USER_INFO, actionNode.getString("loginUserInfo"));
			resp.globalConfigManager.putValue(G.GCK_API_GET_APP_UPGRADE, actionNode.getString("appUpgrade"));
			resp.globalConfigManager.setStatus(ConfigManager.STATUS_READY);
		} else {
			Log.d("Ser","is Not New");
			resp.isNew = false;
		}
		Log.d("Ser","Parse Conf Response End");
		return resp;
	}

}
