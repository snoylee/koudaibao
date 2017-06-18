package com.kdkj.koudailicai.adapter;

import java.util.List;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class TransListPagerAdapter extends PagerAdapter {
	List<View> viewLists;
	
    public TransListPagerAdapter(List<View> viewLists2) {
    	viewLists=viewLists2;
    }
    
	@Override
	public Object instantiateItem(View view,int position)
	{
		((ViewPager)view).addView(viewLists.get(position),0);
		return viewLists.get(position);
		
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getCount() {
		return viewLists.size();
	}

	@Override
	public void destroyItem(View view, int position, Object object)                       //销毁Item
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

}
