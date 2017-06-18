package com.kdkj.koudailicai.view.viewpager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter{

	private List<View> views;
	
	public ViewPagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		 ((ViewPager) container).removeView(views.get(position));
	}



	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(views.get(position), 0);
          return views.get(position);
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(this.views != null){
			return this.views.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
