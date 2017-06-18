package com.kdkj.koudailicai.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 检测网络状态改变
 * @author liaoheng
 * @creation 2014-12-10
 *
 */
public class NetStateReceiver extends BroadcastReceiver {
	private enum NET_STATE{
		G1,
		G2,
		G3,
		G4,
		WIFI		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
	private void saveToSession(NET_STATE state){
		if(NET_STATE.G1.ordinal()==0){
			
		}
	}

}
