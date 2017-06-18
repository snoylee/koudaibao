package com.kdkj.koudailicai.lib;
import com.kdkj.koudailicai.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {                
		//Intent it = new Intent(context, AlarmReceiver.class);
		//it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//context.startActivity(it);
		// 收到广播后启动Activity,简单起见，直接就跳到了设置alarm的Activity
		// intent必须加上Intent.FLAG_ACTIVITY_NEW_TASK flag
		//发起网请求获取数据添加到本地数据库
		LogUtil.info("每隔十分钟执行一次");
	}
}