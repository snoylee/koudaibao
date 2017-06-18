package com.kdkj.koudailicai.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.tsz.afinal.FinalDb;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.ExceptionHelper;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.config.ConfigManager;
import com.kdkj.koudailicai.util.config.GlobalConfigService;
import com.kdkj.koudailicai.util.config.GlobalConfigService.ConfigCompleteListener;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.util.image.GetRemoteImageService;
import com.kdkj.koudailicai.util.image.GetRemoteImageService.ImageResponse;
import com.kdkj.koudailicai.util.resource.OnDownloadFinishedListener;
import com.kdkj.koudailicai.util.resource.RemoteResource;
import com.kdkj.koudailicai.util.resource.ResourceDownloader;
import com.kdkj.koudailicai.util.session.Session;
import com.kdkj.koudailicai.util.session.SharedPreferencesSession;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;

public class KDLCApplication extends Application implements ConfigCompleteListener{
	private ExecutorService resource_task_executor;
	public  FinalDb kdDb;
	public static KDLCApplication app;
	public static boolean updateChecked=false;//是否检查过软件更新
	private Session session;
	public GlobalConfigService globalConfigService;
	private String filesPath;
	public String confPath;
	public String imgPath;
	private String LOG_TAG="KDLCApplication";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化对象
		KDLCApplication.app = this;
		//初始化session
		this.session = new SharedPreferencesSession(this);
		//初始化全局配置
		this.globalConfigService = new GlobalConfigService(getFilesDir().getAbsolutePath(), this);
		//初始化finalDb
//		if(isFirstIn()) {
//			cleanDatabases();
//		}
		this.kdDb = FinalDb.create(this);
		//初始化volley
		RequestManager.init(this);
		//初始化bugly
		initBugly();
		// 初始化环境变量
		this.filesPath = this.getFilesDir().getAbsolutePath();
		this.imgPath = this.filesPath + "/img/";
		this.confPath = this.filesPath + "/conf";
		File f = new File(imgPath);
		if (!f.exists())
		     f.mkdir();
		File f2 = new File(confPath);
		if (!f2.exists())
		     f2.mkdir();		
		// 初始化线程池
		this.resource_task_executor = (ExecutorService)Executors.newFixedThreadPool(G.RESOURCE_THREAD_POOL_SIZE);
		Log.d(LOG_TAG, "Application End.");

	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "Application finished.");
		super.onTerminate();
	}
	
	private void initBugly() {       
         String appId = G.BUGLY_APP_ID;   //上Bugly(bugly.qq.com)注册产品获取的AppId
         boolean isDebug = true;  //true代表App处于调试阶段，false代表App发布阶段
         UserStrategy strategy = new UserStrategy(this); //App的策略Bean
         //strategy.setAppChannel( "myChannel");     //设置渠道号
         //strategy.setAppVersion( "1.0.0");      //App的版本，默认为AndroidManifest中的版本号
         strategy.setAppReportDelay(5000);  //设置SDK处理延时，毫秒
         CrashReport.initCrashReport(this , appId ,isDebug, strategy);  //初始化SDK 
	}
	
	//加载全局配置
	public void loadGlobalConfigInBackground() {
		Log.d(LOG_TAG, "loadGlobalConfigInBackground start");
		this.globalConfigService.updateConf();
	}
	
	//加载启动页图片
	public void loadSplashImageInBackground() {
		this.getResourceTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadSplashImage();
			}
		});
	}
	
	public void loadSplashImage(){
		String getLaunchImgUrl = "";
		if(!isGlobalConfCompleted()) {
			getLaunchImgUrl = Tool.getUrl(G.URL_GET_SPLASH_IMG);
		} else {
			getLaunchImgUrl = Tool.getUrl(getActionUrl(G.GCK_API_GET_SPLASH_IMAGE));
		}
		Log.d(LOG_TAG, "loadSplashImage start");
		//获取接口url和当前版本
		String versionNo = getImageVersion(G.SPLASH_IMAGE_CONF_VERSION);
		Log.d(LOG_TAG, "image versionNo:"+versionNo);
		GetRemoteImageService splashImageService = new GetRemoteImageService(getLaunchImgUrl, versionNo);
		try {
			final ImageResponse resp = (ImageResponse) splashImageService.execute();
			//如有更新，则再次下载图片
			if(resp.isNew()) {
				RemoteResource splashResource = new RemoteResource();
				splashResource.setUrl(resp.getUrl());
				splashResource.setPath(imgPath+G.SPLASH_IMAGE_FILENAME);
				splashResource.setVersionNo(resp.getVersion());
				ResourceDownloader downloader = new ResourceDownloader();
				downloader.setOnDownloadFinishedListener(new OnDownloadFinishedListener() {
					@Override
					public void onFinished() {
						// TODO Auto-generated method stub
						setImageVersion(G.SPLASH_IMAGE_CONF_VERSION, resp.getVersion());
					}
				});
				downloader.addTask(splashResource);
				downloader.exit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
		}
	}
	
	//获取全局配置信息
	public ConfigManager getGlobalConfigManager() {
		return globalConfigService.getGlobalConfiger();
	}
	
	public boolean isGlobalConfCompleted() {
		return globalConfigService != null && globalConfigService.isCompleted();
	}
	
	//获取接口url
	public String getActionUrl(int key) {
		return globalConfigService.getActionUrl(key);
	}
	
	//获取下发配置值
	public String getConfVal(int key) {
		return globalConfigService.getConfVal(key);
	}

	public String getFilesPath() {
		return filesPath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public String getConfPath() {
		return confPath;
	}

	public ExecutorService getResourceTaskExecutor() {
		// TODO Auto-generated method stub
		return this.resource_task_executor;
	}
	
	public Session getSession() {
		return this.session;
	}
	
	public String getSessionId() {
		return this.session.get("sessionid");
	}
	
	public boolean sessionEqual(String key, String cVal) {
		return this.session.get(key) != null && this.session.get(key).equals(cVal);
	}
	
	public String getSessionVal(String key) {
		return this.session.get(key);
	}
	
	public void setSessionVal(String key, String value) {
		this.session.set(key, value);
	}
	
	//获取当前启动页图片版本号
	public String getImageVersion(String key) {
		String confVersion = this.session.get(key);
		if(confVersion != null && !confVersion.isEmpty()) {
			return confVersion;
		} else {
			return "0"; 
		}
	}
	
	//设置当前启动页图片版本号
	public void setImageVersion(String key, String version) {
		Map<String, String> vals = new HashMap<String, String>();
		vals.put(key, version);
		this.session.set(vals);
	}
	
	//判断是否首次启动app
	public boolean isFirstIn() {
		String appCurVesion = this.session.get(G.APP_CUR_VERSION);
		if(appCurVesion != null && appCurVesion.equals(getAppVersion())) {
			return false;
		}
		return true;
	}
	
	
	public void setFirstIn() {
		this.setSessionVal(G.APP_CUR_VERSION, getAppVersion());
	}
	
	//获取当前app的版本号
	public String getAppVersion(){
		try {
			PackageManager packageManager = getPackageManager();     
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	//获取当前设备ID
	public String getDeviceId(){
		try {
			TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	        return tm.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//获取当前设备信息
	public String getDeviceName(){
		try {
			return android.os.Build.MODEL;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//获取当前系统的版本号
	public String getOsVersion(){
		try {
			return android.os.Build.VERSION.RELEASE;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public boolean hasLogin() {
		return sessionEqual("isLogin", "1");
	}
	
	//退出清空session
	public void logout() {
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("isLogin", "0");
		vals.put("sessionid", "");
		this.session.set(vals);
	}
	
	public void logout1()
	{
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("gesture", "0");
		this.session.set(vals);
	}
	
	public boolean checkRealStatus() {
		return sessionEqual("real_verify_status", "1");
	}
	
	public boolean checkCardStatus(){
		return sessionEqual("card_bind_status", "1");
	}
	
	/**
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	*/
	public int dip2px(float dpValue) {
	  final float scale = this.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	}

	/**
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	*/
	public int px2dip(float pxValue) {
	  final float scale = this.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	} 

	// 初始化消息    0已读 1未读
    public void initMessageCenter(String readStatus) {
	   Map<String, String> vals = new HashMap<String, String>();
	   vals.put("message_center_status", readStatus);
	   this.session.set(vals);
	}

	@Override
	public void configComplete() {
		// TODO Auto-generated method stub
		loadSplashImageInBackground();
	}
	
	public void cleanDatabases() {
		deleteFilesByDirectory(new File("/data/data/"+this.getPackageName()+"/databases"));
	}
	
	 private void deleteFilesByDirectory(File directory) {
	        if (directory != null && directory.exists() && directory.isDirectory()) {
	            for (File item : directory.listFiles()) {
	            	Log.d("asdas", "sadas:"+item.getName());
	            	if(item.getName().contains("afinal"))
	                item.delete();
	            }
	        }
	 }

}
