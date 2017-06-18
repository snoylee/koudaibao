package com.kdkj.koudailicai.view.selfcenter.Gesture;

import java.util.List;

import com.kdkj.koudailicai.view.KDLCApplication;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

public class AppUtil {
    
	/**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));// 手机屏幕的宽度
		int height = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}
    
}
