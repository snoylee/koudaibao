package com.kdkj.koudailicai.lib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class KdlcProgressBar extends ProgressBar {
	private String text_progress;
	private Paint mPaint;//画笔
	private int curProgress;
	private int maxProgress;
	private static final int INTERVAL = 1;
	public KdlcProgressBar(Context context) {
		super(context);
		initPaint();
	}
	public KdlcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
	public KdlcProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }
	
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		setTextProgress(progress);
	}
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Rect rect=new Rect();
		this.mPaint.getTextBounds(this.text_progress, 0, this.text_progress.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text_progress, x, y, this.mPaint);
	}
	/**
	 * 
	 *description: 初始化画笔
	 *Create by lll on 2013-8-13 下午1:41:49
	 */
	private void initPaint(){
		this.mPaint=new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(Color.WHITE);
	}
	private void setTextProgress(int progress){ 
        this.text_progress = "";
	}
	
	public synchronized void setAnimProgress(int progress) {
		maxProgress = progress;
		curProgress = 0;
		mHandler.sendEmptyMessageDelayed(INTERVAL, 5);
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INTERVAL:
				if (curProgress < maxProgress) {
					setProgress(curProgress);
					curProgress += 1;
					mHandler.sendEmptyMessageDelayed(INTERVAL, 5);
				} else {
					setProgress(curProgress);
				}
				break;
			default:
				setProgress(curProgress);
				break;
			}
		};
	};


}
