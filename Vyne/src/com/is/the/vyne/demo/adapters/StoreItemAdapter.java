package com.is.the.vyne.demo.adapters;

import java.util.ArrayList;

import com.is.the.vyne.demo.models.VyneItem;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class StoreItemAdapter extends BaseAdapter {
	ArrayList<VyneItem> itemList;
    Context context;
    
   // Constructor
    public StoreItemAdapter (Context c, ArrayList<VyneItem> sl) {
        context = c;
        itemList = sl;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
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
	    tv.setText(itemList.get(position).name);
	    tv.setGravity(Gravity.CENTER);
	    tv.setPadding(10, 15, 10, 15);
        return convertView;
    }
}
