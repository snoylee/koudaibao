package com.kdkj.koudailicai.lib.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class NumChangeTextView extends TextView {

	private static final int REFRESH = 1;
	// 递增 的变量值
	private double mRate;
	//初始值
	private String startValue;
	// 当前显示的值
	private double mCurValue;
	// 当前变化后最终状态的目标值
	private double endValue;
    //刷新时间
	private int refreshTime;
    //显示始终保持两位小数
	DecimalFormat fnum = new DecimalFormat("0.00");
	
	public NumChangeTextView(Context context) {
		super(context);
	}

	public NumChangeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NumChangeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH:
				if (mCurValue < endValue) {
					setText(fnum.format(mCurValue));
					mCurValue += mRate;
					mHandler.sendEmptyMessageDelayed(REFRESH, refreshTime);
				} else {
					setNumTextComplete(startValue);
				}
				break;
			default:
				setNumTextComplete(startValue);
				break;
			}
		};
	};

	public void setNumText(String value) {
		if("0.00".equals(value)||"0".equals(value)){
			setText(value);
		}else{
			setNumText(value, 17, 30);
		}
		
	}

	/**
	 * 
	 * @param value 要设置的数值
	 * @param rate 数值改变的次数
	 * @param refreshTime 刷新的时间 (毫秒)
	 */
	public void setNumText(String value, int rate, int refreshTime) {
		this.startValue = value;
		this.mCurValue = 0.00;
		this.endValue = Double.parseDouble(value);
		if(endValue>10){
			this.mRate = (double) (endValue / rate+Math.random());
		}else if(endValue<1){
			this.mRate=endValue;
		}else{
			this.mRate = (double) (endValue / rate);
			
		}
		
		this.refreshTime = refreshTime;
		BigDecimal b = new BigDecimal(mRate);
		mRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		mHandler.sendEmptyMessageDelayed(REFRESH, 0);
	}
	
	private void setNumTextComplete(String value){
		setText(value);
	}
	
}
