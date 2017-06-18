package com.kdkj.koudailicai.lib.ui.RelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class ResizeRelativeLayout extends RelativeLayout{
	private static int count = 0;
	private ResizeListener resizeListener;
	
	public ResizeRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ResizeRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ResizeRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setResizeListener(ResizeListener resizeListener) {
		this.resizeListener = resizeListener;
	}
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {    
        super.onSizeChanged(w, h, oldw, oldh);
        resizeListener.onResize(w, h, oldw, oldh);
    }
      
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
      
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
