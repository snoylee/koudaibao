package com.kdkj.koudailicai.adapter;

import java.util.HashMap;
import java.util.List;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.xgpush.XGNotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class NotificationListAdapter extends ArrayAdapter<XGNotification> {

	private LayoutInflater layoutInflater = null;
	private List<XGNotification> notifications = null ;
	private HashMap<Integer, View>  temp = new HashMap<Integer, View>();
	private int screenHeight;
	private int screenWidth;
	public NotificationListAdapter(Context context, int resource,
			List<XGNotification> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		layoutInflater = LayoutInflater.from(context);
		notifications = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notifications.size();
	}

	@Override
	public XGNotification getItem(int position) {
		// TODO Auto-generated method stub
		return notifications.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder viewholder;
		XGNotification item = notifications.get(position);
		int minheight = (int)(screenHeight*(1-0.08)/4);
		RelativeLayout listview_layout;
		if(temp.get(position)==null){
				convertView = layoutInflater.inflate(R.layout.notification_list_item, null);
				viewholder = new ViewHolder();
				viewholder.notification_time = (TextView)convertView.findViewById(R.id.notification_time);
				viewholder.notification_icon = (ImageView)convertView.findViewById(R.id.notification_icon);
				viewholder.notification_content = (TextView)convertView.findViewById(R.id.notification_content);
				viewholder.notification_time.setText(item.getUpdate_time());
				viewholder.notification_content.setText(item.getContent());
			  temp.put(position, convertView);
		}else{
			convertView = temp.get(position);
			viewholder = (ViewHolder) convertView.getTag();
		}
		listview_layout = (RelativeLayout)convertView.findViewById(R.id.listview_layout);
		LayoutParams layout = (LayoutParams) listview_layout.getLayoutParams();
		layout.height = minheight;
		listview_layout.setLayoutParams(layout);
		return convertView;
	}

	public static class ViewHolder{
		private TextView notification_time;
		private ImageView notification_icon;
		private TextView notification_content;
	}
	
}
