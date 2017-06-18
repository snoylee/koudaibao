package com.kdkj.koudailicai.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.login.LoginActivity;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.View;

public class Tool {

	// unicode转中文
	public static String decode(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}
		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; i++) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5)
						&& ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
								.charAt(i + 1) == 'U')))
					try {
						retBuf.append((char) Integer.parseInt(
								unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException localNumberFormatException) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}
		return retBuf.toString();
	}

	/**
	 * utf-8 转unicode
	 * 
	 * @param inStr
	 * @return String
	 */
	public static String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		String uStr = "";
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			char sValue = str.charAt(i);
			if (iValue <= 256) {
				// uStr+="& "+Integer.toHexString(iValue)+";";
				// uStr+="\\"+Integer.toHexString(iValue);
				uStr += sValue;
			} else {
				// uStr+="&#x"+Integer.toHexString(iValue)+";";
				uStr += "\\u" + Integer.toHexString(iValue);
			}
		}
		return uStr;
	}

	// 转32位大写MD5
	public final static String get32MD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			Log.e("Tool", "NoSuchAlgorithmException:" + e.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Tool", "UnsupportedEncodingException:" + e.toString());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	// 判断是否是手机号码
	public static boolean isMobileNO(String mobiles) {
		if(isBlank(mobiles))
			return false;
		Pattern p = Pattern
				.compile("^1[0-9]{10}$");
		//^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 判断是否是邮箱地址
	public static boolean isEmail(String email) {
		if(isBlank(email))
			return false;
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	// 电话号码部分加*号
	public static String changeMobile(String telephone) {
		if(isBlank(telephone)) {
			return "";
		}
		return telephone.substring(0, 3) + "****"
				+ telephone.substring(7, telephone.length());
	}
   //姓名加*号
	public static String changeName(String name) {
		if(isBlank(name)) {
			return "";
		}
		return "*"+name.substring(1, name.length());
	}
	
	//身份证号加*号
	public static String changeIdentity(String identity) {
		if(isBlank(identity)) {
			return "";
		}
		if(identity.length() != 15 && identity.length() != 18) {
			return "身份证号码异常";
		}
		return identity.substring(0,4)+"************"+identity.substring(identity.length()-2,identity.length());
	}
	
	// 小数进位
	public static double carryAigit(float number) {
		return Math.ceil(number);
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    
	// 判断身份证是否合法
    public static boolean validatePersonalId(String personalId) {
    	return IdCardUtils.IDCardValidate(personalId);
	}
    
	//名字为汉字
	public static boolean isWord(String str) {
		if(Tool.isBlank(str) || str.trim().length() < 2) {
			return false;
		}
//		int n = 0,count=0;
//		
//		for (int i = 0; i < str.length(); i++) {
//			n = (int) str.charAt(i);
//			if ((19968 <= n && n <= 171941)) {
//				count++;
//			}
//		}
//		if(count==str.length())
//			return true;
//		else {
//			return false;
//		}
		return true;
	}
	
	public static boolean isWord2(String str) {
		if (str == null) {
			return false;
		}
		// 大小写不同：\\p 表示包含，\\P 表示不包含
		// \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
		String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str.trim()).find();
	}
	
	//检查银行卡号
	 public   static   boolean  checkBankCard(String cardId) {  
         char  bit = getBankCardCheckCode(cardId.substring( 0 , cardId.length() -  1 ));  
         if (bit ==  'N' ){  
             return   false ;  
         }  
         return  cardId.charAt(cardId.length() -  1 ) == bit;  
   }  
     
   public   static   char  getBankCardCheckCode(String nonCheckCodeCardId){  
       if (nonCheckCodeCardId ==  null  || nonCheckCodeCardId.trim().length() ==  0   
               || !nonCheckCodeCardId.matches("\\d+" )) {  
        //如果传的不是数据返回N   
           return   'N' ;  
       }  
       char [] chs = nonCheckCodeCardId.trim().toCharArray();  
       int  luhmSum =  0 ;  
       for ( int  i = chs.length -  1 , j =  0 ; i >=  0 ; i--, j++) {  
           int  k = chs[i] -  '0' ;  
           if (j %  2  ==  0 ) {  
               k *= 2 ;  
               k = k / 10  + k %  10 ;  
           }  
           luhmSum += k;             
       }  
       return  (luhmSum %  10  ==  0 ) ?  '0'  : ( char )(( 10  - luhmSum %  10 ) +  '0' );  
   }  
	// 判断网络是否连接
	public static boolean isNetworkAvailable(Activity mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 微信分享使用
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// 金额由10000分转为100.00元
	public static String toDeciDouble(String num) {
		if (isBlank(num)) {
			return "0.00";
		}
		int len = num.length();
		if (len == 1) {
			return "0.0" + num;
		} else if (len == 2) {
			return "0." + num;
		} else {
			return num.substring(0, len - 2) + "." + num.substring(len - 2);
		}
	}

	// 金额由10000分转为100元
	public static String toIntAccount(String num) {
		if (isBlank(num)) {
			return "0";
		}
		return "" + Long.parseLong(num) / 100;
	}

	// 金额由1000000分转为10,000元
	public static String toDivAccount(String num) {
		if (isBlank(num)) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(Long.parseLong(num) / 100);
	}
	
	// 金额由10000.00转为10,000.00元
	public static String toDivAccount2(String num) {
		if (isBlank(num)) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#,###.00");
		return df.format(Double.parseDouble(num));
	}

	public static String toFFDouble(double num) {
		if(Double.isNaN(num)) {
			return "0.00";
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		BigDecimal bd = new BigDecimal(num);
		num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return toDeciDouble(nf.format(num * 100));
	}
	
	//将输入的数字转为0.00格式的double
	public static String toForDouble(String num) {
		if(Tool.isBlank(num)) {
			return "0.00";
		}
		return (new DecimalFormat("0.00")).format(Double.parseDouble(num));
	}
	
	//将输入的数字转为0.00格式的double
	public static Double toForDouble2(String num) {
		if(Tool.isBlank(num)) {
			return 0.00;
		}
		return Double.parseDouble((new DecimalFormat("0.00")).format(Double.parseDouble(num)));
	}
	
	public static String trimHeadZero(String num) {
		if(isBlank(num)) {
			return "0";
		} else {
			return num.replaceFirst("^0*", "");
		}
	}

	// 判断字符串对象为null或者""
	public static boolean isBlank(String str) {
		return (str == null || str.isEmpty() || "null".equals(str));
	}

	public static List<String> toList(JSONArray arr) {
		List<String> str_list = new ArrayList<String>();
		for (int i = 0; i < arr.length(); i++) {
			try {
				str_list.add(arr.getString(i));
			} catch (JSONException e) {
				Log.e("Tool", "Failed To List");
				return str_list;
			}
		}
		return str_list;
	}

	// url 解析
	public static String getUrl(String url) {
		String ret_url = "";
		if (url.contains("?")) {
			ret_url = url + "&";
		} else {
			ret_url = url + "?";
		}
		ret_url += "clientType=android&appVersion=" + KDLCApplication.app.getAppVersion() + 
				   "&deviceId="   + KDLCApplication.app.getDeviceId() + 
				   "&deviceName=" + KDLCApplication.app.getDeviceName() + 
				   "&osVersion="  + KDLCApplication.app.getOsVersion()  + 
				   "&appMarket="  + G.MARKET_NAME;
		return ret_url.replace(" ", "");
	}

	@SuppressLint("SimpleDateFormat")
	public static String getNowTime(String format) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	// unix时间戳转日期  yyyy-MM-dd-HH-mm:ss:ms   
	@SuppressLint("SimpleDateFormat")
	public static String unixTimeToDate(String timestampString, String formats) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date(timestamp));
		return date;
	}

	public static String encodePassword(String password) {
		String str_begin = password.substring(0, 3);
		String str_end = password.substring(3);
		String new_password = str_end + str_begin;
		return new_password;
	}

	public static String decodePassword(String password) {
		int len = password.length();
		String str_begin = password.substring(0, len - 3);
		String str_end = password.substring(len - 3);
		String new_password = str_end + str_begin;
		return new_password;
	}
	
	public static void startActivityNeedLogin(Context context, Intent intent) {
		if(!KDLCApplication.app.hasLogin()) {
			intent.setClass(context, LoginAlreadyActivity.class);
		}
		context.startActivity(intent);
	}
	
	public static void goToActivity(Activity fromActivity, Class<?> toActivity) {
		Intent intent = new Intent();
		intent.setClass(fromActivity, toActivity);
		fromActivity.startActivity(intent);
	}
	
	public static String signParams(HttpParams params) {
		String sign = "";
		if(params != null && params.size() > 0) {
			sign = params.toString();
			sign += "**kdlc**";
		}
		return Base64.encodeToString(sign.getBytes(), Base64.NO_WRAP);
	}
	
	public static boolean isMX() {
		String deviceName = KDLCApplication.app.getDeviceName();
		if("MX1".equals(deviceName) || "MX2".equals(deviceName) || "MX3".equals(deviceName) || "MX4".equals(deviceName)) {
			return true;
		}
		return false;
	}
	
	public static void resetPullRefreshView(PullToRefreshBase refreshView) {
		if(refreshView != null && refreshView.isRefreshing()) {
			refreshView.onRefreshComplete();
		}
	}
	
	//判断view是否存在
	public static boolean isVisible(View view) {
		if(view != null && view.getVisibility() == View.VISIBLE) {
			return true;
		} else
			return false;
	}
	
	//标题优化
	public static String changeTitle(String title) {
		if(isBlank(title)) {
			return "口袋理财";
		} else {
			return title.length() > 8 ? title.substring(0, 8)+"..." : title;
		}
	}
	
	public static boolean hasCacheData(Class<?> clazz) {
		return KdlcDB.findAllByClass(clazz) != null && KdlcDB.findAllByClass(HomeProductInfo.class).size() > 0;
	}
	
}
