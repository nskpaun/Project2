package com.is.the.vyne.demo.adapters;

import java.util.ArrayList;

import com.is.the.vyne.demo.models.Store;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FollowingAdapter extends BaseAdapter {
	public ArrayList<Store> storeList;
    Context context;
    
   // Constructor
    public FollowingAdapter (Context c, ArrayList<Store> sl) {
        context = c;
        storeList = sl;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
	    TextView tv = ((TextView) convertView);
	    
	    tv.setTextColor(Color.BLACK);
	    tv.setCompoundDrawables(null,null,null,null);
	    tv.setText(storeList.get(position).name);
	    tv.setGravity(Gravity.CENTER);
	    tv.setPadding(10, 15, 10, 15);
        return convertView;
    }

}
